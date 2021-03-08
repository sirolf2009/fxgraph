package com.fxgraph.edges;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class CorneredLoopEdge extends AbstractEdge {

	private final DoubleProperty separationProperty = new SimpleDoubleProperty(15);
	private final DoubleProperty widthProperty = new SimpleDoubleProperty(0.5);
	private final StringProperty textProperty;
	private final Position pos;

	public CorneredLoopEdge(ICell target, Position pos) {
		super(target, target, true);
		this.pos = pos;
		textProperty = new SimpleStringProperty();
	}

	@Override
	protected void linkCells() {
		// no-op
	}

	@Override
	public EdgeGraphic getGraphic(Graph graph) {
		return new EdgeGraphic(graph, this, pos, textProperty, separationProperty, widthProperty);
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public DoubleProperty separationProperty() {
		return separationProperty;
	}

	public DoubleProperty widthProperty() {
		return widthProperty;
	}

	public enum Position {
		TOP, RIGHT, BOTTOM, LEFT;

		boolean isVertical() {
			return this == LEFT || this == RIGHT;
		}

		boolean isHorizontal() {
			return this == TOP || this == BOTTOM;
		}
	}

	public static class EdgeGraphic extends AbstractEdgeGraphic {

		private final DoubleBinding sourceX;
		private final DoubleBinding sourceY;
		private final DoubleBinding targetX;
		private final DoubleBinding targetY;
		private final DoubleBinding centerX;
		private final DoubleBinding centerY;
		private final Line lineA = new Line();
		private final Line lineB = new Line();
		private final Line lineC = new Line();

		public EdgeGraphic(Graph graph, CorneredLoopEdge edge, Position position, StringProperty textProperty,
						   DoubleProperty separation, DoubleProperty width) {
			DoubleBinding w = edge.getTarget().getWidth(graph).add(0.000001);
			DoubleBinding h = edge.getTarget().getHeight(graph).add(0.000001);
			DoubleBinding x = edge.getTarget().getXAnchor(graph);
			DoubleBinding y = edge.getTarget().getYAnchor(graph);

			if (position.isVertical()) {
				sourceY = y.subtract(h.divide(2.0).multiply(width));
				targetY = y.add(h.divide(2.0).multiply(width));

				if (position == Position.LEFT) {
					sourceX = x.subtract(w.divide(2.0));
					targetX = sourceX;
					centerX = sourceX.subtract(w.divide(2.0)).subtract(separation);
				} else {
					sourceX = x.add(w.divide(2.0));
					targetX = sourceX;
					centerX = sourceX.add(w.divide(2.0)).add(separation);
				}

				centerY = sourceY.add(targetY).divide(2);
			} else {
				sourceX = x.subtract(w.divide(2.0).multiply(width));
				targetX = x.add(w.divide(2.0).multiply(width));

				if (position == Position.TOP) {
					sourceY = y.subtract(h.divide(2.0));
					targetY = sourceY;
					centerY = sourceY.subtract(h.divide(2.0)).subtract(separation);
				} else {
					sourceY = y.add(h.divide(2.0));
					targetY = sourceY;
					centerY = sourceY.add(h.divide(2.0)).add(separation);
				}

				centerX = sourceX.add(targetX).divide(2);
			}

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

			if (position == Position.TOP) {
				text.yProperty().bind(centerY.subtract(textHeight.divide(2)));
			} else {
				text.yProperty().bind(centerY.add(textHeight.divide(2)));
			}
			text.xProperty().bind(centerX.subtract(textWidth.divide(2)));


			if(position.isVertical()) {
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(centerX);
				lineA.endYProperty().bind(sourceY);
				group.getChildren().add(lineA);

				lineB.startXProperty().bind(centerX);
				lineB.startYProperty().bind(sourceY);
				lineB.endXProperty().bind(centerX);
				lineB.endYProperty().bind(targetY);
				group.getChildren().add(lineB);

				setupArrow(centerX, targetY, targetX, targetY);
				group.getChildren().add(arrow);
			} else {
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(sourceX);
				lineA.endYProperty().bind(centerY);
				group.getChildren().add(lineA);

				lineB.startXProperty().bind(sourceX);
				lineB.startYProperty().bind(centerY);
				lineB.endXProperty().bind(targetX);
				lineB.endYProperty().bind(centerY);
				group.getChildren().add(lineB);

				setupArrow(targetX, centerY, targetX, targetY);
				group.getChildren().add(arrow);
			}

			group.getChildren().add(text);
			getChildren().add(group);
		}

		public DoubleBinding getSourceX() {
			return sourceX;
		}

		public DoubleBinding getSourceY() {
			return sourceY;
		}

		public DoubleBinding getTargetX() {
			return targetX;
		}

		public DoubleBinding getTargetY() {
			return targetY;
		}

		public DoubleBinding getCenterX() {
			return centerX;
		}

		public DoubleBinding getCenterY() {
			return centerY;
		}

		public Group getGroup() {
			return group;
		}

		public Line getLineA() {
			return lineA;
		}

		public Line getLineB() {
			return lineB;
		}

		public Line getLineC() {
			return lineC;
		}

		public Text getText() {
			return text;
		}

	}

}