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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class CorneredEdge extends AbstractEdge {

	private final StringProperty textProperty;
	private final Orientation orientation;

	public CorneredEdge(ICell source, ICell target, Orientation orientation) {
		super(source, target);
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

	public static class EdgeGraphic extends Pane {

		private final Group group;
		private final Text text;

		public EdgeGraphic(Graph graph, CorneredEdge edge, Orientation orientation, StringProperty textProperty) {
			final DoubleBinding sourceX = edge.getSource().getXAnchor(graph, edge);
			final DoubleBinding sourceY = edge.getSource().getYAnchor(graph, edge);
			final DoubleBinding targetX = edge.getTarget().getXAnchor(graph, edge);
			final DoubleBinding targetY = edge.getTarget().getYAnchor(graph, edge);

			text = new Text();
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
				group = new Group();
				final Line lineA = new Line();
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(targetX);
				lineA.endYProperty().bind(sourceY);
				group.getChildren().add(lineA);
				final Line lineB = new Line();
				lineB.startXProperty().bind(targetX);
				lineB.startYProperty().bind(sourceY);
				lineB.endXProperty().bind(targetX);
				lineB.endYProperty().bind(targetY);
				group.getChildren().add(lineB);

				text.xProperty().bind(targetX.subtract(textWidth.divide(2)));
				text.yProperty().bind(sourceY.subtract(textHeight.divide(2)));
			} else {
				group = new Group();
				final Line lineA = new Line();
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(sourceX);
				lineA.endYProperty().bind(targetY);
				group.getChildren().add(lineA);
				final Line lineB = new Line();
				lineB.startXProperty().bind(sourceX);
				lineB.startYProperty().bind(targetY);
				lineB.endXProperty().bind(targetX);
				lineB.endYProperty().bind(targetY);
				group.getChildren().add(lineB);

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