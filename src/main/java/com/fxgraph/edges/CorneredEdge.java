package com.fxgraph.edges;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class CorneredEdge extends AbstractEdge {

	private final StringProperty textProperty;
	private final Orientation orientation;

	public CorneredEdge(ICell source, ICell target, Orientation orientation) {
		this(source, target, false, orientation);
	}

	public CorneredEdge(ICell source, ICell target, boolean isDirected, Orientation orientation) {
		super(source, target, isDirected);
		this.orientation = orientation;
		textProperty = new SimpleStringProperty();
	}

	@Override
	public EdgeGraphic getGraphic(Graph graph) {
		return new EdgeGraphic(graph, this, orientation, textProperty);
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public static class EdgeGraphic extends AbstractEdgeGraphic {

		public EdgeGraphic(Graph graph, CorneredEdge edge, Orientation orientation, StringProperty textProperty) {
			super();
			final DoubleBinding sourceX = edge.getSource().getXAnchor(graph);
			final DoubleBinding sourceY = edge.getSource().getYAnchor(graph);
			final DoubleBinding targetX = edge.getTarget().getXAnchor(graph);
			final DoubleBinding targetY = edge.getTarget().getYAnchor(graph);

			text.textProperty().bind(textProperty);
			text.getStyleClass().add("edge-text");
			final DoubleProperty textWidth = new SimpleDoubleProperty();
			final DoubleProperty textHeight = new SimpleDoubleProperty();
			final Runnable recalculateWidth = () -> {
				textWidth.set(text.getLayoutBounds().getWidth());
				textHeight.set(text.getLayoutBounds().getHeight());
			};
			text.parentProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			text.textProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());

			if(orientation == Orientation.HORIZONTAL) {
				final Line lineA = new Line();
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(targetX);
				lineA.endYProperty().bind(sourceY);
				group.getChildren().add(lineA);

				if (edge.isDirected()) {
					arrow.getStyleClass().add("arrow");
					arrow.startXProperty().bind(targetX);
					arrow.startYProperty().bind(sourceY);
					arrow.endXProperty().bind(targetX);
					arrow.endYProperty().bind(targetY);
					group.getChildren().add(arrow);
				} else {
					final Line lineB = new Line();
					lineB.startXProperty().bind(targetX);
					lineB.startYProperty().bind(sourceY);
					lineB.endXProperty().bind(targetX);
					lineB.endYProperty().bind(targetY);
					group.getChildren().add(lineB);
				}


				text.xProperty().bind(targetX.subtract(textWidth.divide(2)));
				text.yProperty().bind(sourceY.subtract(textHeight.divide(2)));
			} else {
				final Line lineA = new Line();
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(sourceX);
				lineA.endYProperty().bind(targetY);
				group.getChildren().add(lineA);

				if (edge.isDirected()) {
					arrow.getStyleClass().add("arrow");
					arrow.startXProperty().bind(sourceX);
					arrow.startYProperty().bind(targetY);
					arrow.endXProperty().bind(targetX);
					arrow.endYProperty().bind(targetY);
					group.getChildren().add(arrow);
				} else {
					final Line lineB = new Line();
					lineB.startXProperty().bind(sourceX);
					lineB.startYProperty().bind(targetY);
					lineB.endXProperty().bind(targetX);
					lineB.endYProperty().bind(targetY);
					group.getChildren().add(lineB);
				}

				text.xProperty().bind(sourceX.subtract(textWidth.divide(2)));
				text.yProperty().bind(targetY.subtract(textHeight.divide(2)));
			}

			group.getChildren().add(text);
			getChildren().add(group);
		}

		public Group getGroup() {
			return group;
		}

		public Text getText() {
			return text;
		}

	}

}