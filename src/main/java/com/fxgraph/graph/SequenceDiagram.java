package com.fxgraph.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.edges.AbstractEdge;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class SequenceDiagram extends Graph {

	private static final int verticalSpacing = 200;
	private static final int horizontalSpacing = 50;
	
	List<ActorCell> actors = new ArrayList<>();
	List<MessageEdge> messages = new ArrayList<>();
	
	public void addActor(String actor, double length) {
		addActor(new ActorCell(actor, new SimpleDoubleProperty(length)));
	}
	
	public void addActor(ActorCell actor) {
		actors.add(actor);
		getModel().addCell(actor);
		endUpdate();
	}

	public void addMessage(ActorCell source, ActorCell target, String name, int index) {
		addMessage(new MessageEdge(source, target, name, index));
	}
	
	public void addMessage(MessageEdge edge) {
		messages.add(edge);
		getModel().addEdge(edge);
		endUpdate();
	}
	
	public void layout() {
		AtomicInteger counter = new AtomicInteger();
		actors.forEach(actor -> {
			getGraphic(actor).setLayoutX(counter.getAndIncrement()*verticalSpacing);
			getGraphic(actor).setLayoutY(0);
		});
		
		messages.stream().sorted().forEach(edge -> {
			edge.yOffsetProperty().set(edge.index * horizontalSpacing);
		});
	}
	
	static class ActorCell extends AbstractCell {
		
		private final String name;
		private final DoubleProperty lifeLineLength;
		
		public ActorCell(String name, Double lifeLineLength) {
			this(name, new SimpleDoubleProperty(lifeLineLength));
		}
		
		public ActorCell(String name, DoubleProperty lifeLineLength) {
			this.name = name;
			this.lifeLineLength = lifeLineLength;
		}

		@Override
		public Region getGraphic(Graph graph) {
			Label label = new Label(name);
			Line lifeLine = new Line();
			lifeLine.startXProperty().bind(label.widthProperty().divide(2));
			lifeLine.setStartY(0);
			lifeLine.endXProperty().bind(label.widthProperty().divide(2));
			lifeLine.endYProperty().bind(lifeLineLength);
			lifeLine.getStrokeDashArray().add(4d);
			return new Pane(label, lifeLine);
		}
		
		@Override
		public DoubleBinding getXAnchor(Graph graph, IEdge edge) {
			final Region graphic = graph.getGraphic(this);
			final Label label = (Label) graphic.getChildrenUnmodifiable().get(0);
			return graphic.layoutXProperty().add(label.widthProperty().divide(2));
		}

		@Override
		public DoubleBinding getYAnchor(Graph graph, IEdge edge) {
			final Region graphic = graph.getGraphic(this);
			final Label label = (Label) graphic.getChildrenUnmodifiable().get(0);
			return graphic.layoutYProperty().add(label.heightProperty().divide(2));
		}
		
	}
	
	static class MessageEdge extends AbstractEdge implements Comparable<MessageEdge> {
		
		private final String name;
		private final int index;
		private final DoubleProperty yOffsetProperty = new SimpleDoubleProperty();
		
		public MessageEdge(ActorCell source, ActorCell target, String name, int index) {
			super(source, target);
			this.name = name;
			this.index = index;
		}

		@Override
		public Region getGraphic(Graph graph) {
			Group group = new Group();
			Arrow arrow = new Arrow();

			final DoubleBinding sourceX = getSource().getXAnchor(graph, this);
			final DoubleBinding sourceY = getSource().getYAnchor(graph, this).add(yOffsetProperty);
			final DoubleBinding targetX = getTarget().getXAnchor(graph, this);
			final DoubleBinding targetY = getTarget().getYAnchor(graph, this).add(yOffsetProperty);

			arrow.startXProperty().bind(sourceX);
			arrow.startYProperty().bind(sourceY);

			arrow.endXProperty().bind(targetX);
			arrow.endYProperty().bind(targetY);
			group.getChildren().add(arrow);

			final DoubleProperty textWidth = new SimpleDoubleProperty();
			final DoubleProperty textHeight = new SimpleDoubleProperty();
			
			Text text = new Text(name);
			text.getStyleClass().add("edge-text");
			text.xProperty().bind(arrow.startXProperty().add(arrow.endXProperty()).divide(2).subtract(textWidth.divide(2)));
			text.yProperty().bind(arrow.startYProperty().add(arrow.endYProperty()).divide(2).subtract(textHeight.divide(2)));
			final Runnable recalculateWidth = () -> {
				textWidth.set(text.getLayoutBounds().getWidth());
				textHeight.set(text.getLayoutBounds().getHeight());
			};
			text.parentProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			text.textProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			group.getChildren().add(text);
			return new Pane(group);
		}

		@Override
		public int compareTo(MessageEdge o) {
			return Integer.valueOf(index).compareTo(o.index);
		}
		
		public DoubleProperty yOffsetProperty() {
			return yOffsetProperty;
		}
		
	}

}
