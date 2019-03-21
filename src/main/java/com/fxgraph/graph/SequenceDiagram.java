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

	private int verticalSpacing = 200;
	private int horizontalSpacing = 50;
	
	List<IActorCell> actors = new ArrayList<>();
	List<IMessageEdge> messages = new ArrayList<>();
	
	public void addActor(String actor, double length) {
		addActor(new ActorCell(actor, new SimpleDoubleProperty(length)));
	}
	
	public void addActor(IActorCell actor) {
		actors.add(actor);
		getModel().addCell(actor);
		endUpdate();
	}

	public void addMessage(IActorCell source, IActorCell target, String name) {
		addMessage(new MessageEdge(source, target, name));
	}
	
	public void addMessage(IMessageEdge edge) {
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
		
		counter.set(0);
		messages.forEach(edge -> {
			edge.yOffsetProperty().set(counter.incrementAndGet() * horizontalSpacing);
		});
	}
	
	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}

	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}

	public static interface IActorCell extends ICell {
		
		public String getName();
		
	}
	
	public static class ActorCell extends AbstractCell implements IActorCell {
		
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
			lifeLine.getStyleClass().add("life-line");
			lifeLine.startXProperty().bind(label.widthProperty().divide(2));
			lifeLine.setStartY(0);
			lifeLine.endXProperty().bind(label.widthProperty().divide(2));
			lifeLine.endYProperty().bind(lifeLineLength);
			lifeLine.getStrokeDashArray().add(4d);
			Pane pane = new Pane(label, lifeLine);
			pane.getStyleClass().add("actor-cell");
			return pane;
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
		
		public String getName() {
			return name;
		}
		
	}
	
	public static interface IMessageEdge extends IEdge {
		
		public DoubleProperty yOffsetProperty();
		
	}
	
	public static class MessageEdge extends AbstractEdge implements IMessageEdge {
		
		private final String name;
		private final DoubleProperty yOffsetProperty = new SimpleDoubleProperty();
		
		public MessageEdge(IActorCell source, IActorCell target, String name) {
			super(source, target);
			this.name = name;
		}

		@Override
		public Region getGraphic(Graph graph) {
			Group group = new Group();
			Arrow arrow = new Arrow();
			arrow.getStyleClass().add("arrow");

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
			Pane pane = new Pane(group);
			pane.getStyleClass().add("message-edge");
			return pane;
		}
		
		public DoubleProperty yOffsetProperty() {
			return yOffsetProperty;
		}
		
	}

}
