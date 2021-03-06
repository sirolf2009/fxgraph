package com.fxgraph.edges;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class Edge extends AbstractEdge {

	private transient final StringProperty textProperty;

	public Edge(ICell source, ICell target) {
		this(source, target, false);
	}

	public Edge(ICell source, ICell target, boolean isDirected) {
		super(source, target, isDirected);
		textProperty = new SimpleStringProperty();
	}

	@Override
	public EdgeGraphic getGraphic(Graph graph) {
		return new EdgeGraphic(graph, this, textProperty);
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public static class EdgeGraphic extends AbstractEdgeGraphic {

		private final Line line;

		public EdgeGraphic(Graph graph, Edge edge, StringProperty textProperty) {
			line = new Line();

			final DoubleBinding sourceX = edge.getSource().getXAnchor(graph);
			final DoubleBinding sourceY = edge.getSource().getYAnchor(graph);
			final DoubleBinding targetX = edge.getTarget().getXAnchor(graph);
			final DoubleBinding targetY = edge.getTarget().getYAnchor(graph);

			if (edge.isDirected()) {
				arrow.getStyleClass().add("arrow");

				arrow.startXProperty().bind(sourceX);
				arrow.startYProperty().bind(sourceY);

				// TODO: How can we make it so that the arrow head consistently tracks the edge of the shape?
				//  - For now we can just work with assumption of it being a square. 
				Point2D arrowTarget = getIntercept(
						new Point2D(sourceX.get(), sourceY.get()),
						new Point2D(targetX.get(), targetY.get()),
						graph.getGraphic(edge.getSource()));
				DoubleBinding arrowEndX = targetX.subtract(edge.getTarget().getWidth(graph).divide(2));
				DoubleBinding arrowEndY = targetY.subtract(edge.getTarget().getHeight(graph).divide(2));

				arrow.endXProperty().bind(targetX);
				arrow.endYProperty().bind(targetY);
				group.getChildren().add(arrow);
			} else {
				line.startXProperty().bind(sourceX);
				line.startYProperty().bind(sourceY);

				line.endXProperty().bind(targetX);
				line.endYProperty().bind(targetY);
				group.getChildren().add(line);
			}



			final DoubleProperty textWidth = new SimpleDoubleProperty();
			final DoubleProperty textHeight = new SimpleDoubleProperty();
			text.textProperty().bind(textProperty);
			text.getStyleClass().add("edge-text");
			text.xProperty().bind(sourceX.add(targetX).divide(2).subtract(textWidth.divide(2)));
			text.yProperty().bind(sourceY.add(targetY).divide(2).subtract(textHeight.divide(2)));
			final Runnable recalculateWidth = () -> {
				textWidth.set(text.getLayoutBounds().getWidth());
				textHeight.set(text.getLayoutBounds().getHeight());
			};
			text.parentProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			text.textProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			group.getChildren().add(text);
			getChildren().add(group);
		}

		public Line getLine() {
			return line;
		}

	}

}