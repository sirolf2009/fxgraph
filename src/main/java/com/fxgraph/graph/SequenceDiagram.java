package com.fxgraph.graph;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.edges.MessageEdge;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SequenceDiagram extends Graph {

	private double verticalSpacing = 200;
	private double horizontalSpacing = 50;
	
	private List<IActorCell> actors = new ArrayList<>();
	private List<IMessageEdge> messages = new ArrayList<>();
	
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
		actors.stream().map(actor -> getGraphic(actor)).forEach(actor -> {
			actor.setLayoutX(counter.getAndIncrement()*verticalSpacing);
			actor.setLayoutY(0);
			actor.toFront();
		});
		
		counter.set(0);
		messages.forEach(edge -> {
			edge.yOffsetProperty().set(counter.incrementAndGet() * horizontalSpacing);
		});
	}
	
	public double getVerticalSpacing() {
		return verticalSpacing;
	}

	public void setVerticalSpacing(double verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}

	public double getHorizontalSpacing() {
		return horizontalSpacing;
	}

	public void setHorizontalSpacing(double horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}
	
	public List<IActorCell> getActors() {
		return actors;
	}
	
	public List<IMessageEdge> getMessages() {
		return messages;
	}

	public interface IActorCell extends ICell {
		
		String getName();
		
	}

	public interface IMessageEdge extends IEdge {

		DoubleProperty yOffsetProperty();

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
		public DoubleBinding getXAnchor(Graph graph) {
			final Region graphic = graph.getGraphic(this);
			final Label label = (Label) graphic.getChildrenUnmodifiable().get(0);
			return graphic.layoutXProperty().add(label.widthProperty().divide(2));
		}

		@Override
		public DoubleBinding getYAnchor(Graph graph) {
			final Region graphic = graph.getGraphic(this);
			final Label label = (Label) graphic.getChildrenUnmodifiable().get(0);
			return graphic.layoutYProperty().add(label.heightProperty().divide(2));
		}
		
		public String getName() {
			return name;
		}
		
	}
}
