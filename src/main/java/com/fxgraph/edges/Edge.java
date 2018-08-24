package com.fxgraph.edges;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Edge extends AbstractEdge {

	private transient final StringProperty textProperty;

	public Edge(ICell source, ICell target) {
		super(source, target);
		textProperty = new SimpleStringProperty();
	}

	@Override
	public EdgeGraphic getGraphic(Graph graph) {
		return new EdgeGraphic(graph, this, textProperty);
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public static class EdgeGraphic extends Pane {

		private final Group group;
		private final Line line;
		private final Text text;

		public EdgeGraphic(Graph graph, Edge edge, StringProperty textProperty) {
			group = new Group();
			line = new Line();

			final DoubleBinding sourceX = edge.getSource().getXAnchor(graph, edge);
			final DoubleBinding sourceY = edge.getSource().getYAnchor(graph, edge);
			final DoubleBinding targetX = edge.getTarget().getXAnchor(graph, edge);
			final DoubleBinding targetY = edge.getTarget().getYAnchor(graph, edge);

			line.startXProperty().bind(sourceX);
			line.startYProperty().bind(sourceY);

			line.endXProperty().bind(targetX);
			line.endYProperty().bind(targetY);
			group.getChildren().add(line);

			final DoubleProperty textWidth = new SimpleDoubleProperty();
			final DoubleProperty textHeight = new SimpleDoubleProperty();
			text = new Text();
			text.textProperty().bind(textProperty);
			text.getStyleClass().add("edge-text");
			text.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2).subtract(textWidth.divide(2)));
			text.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(textHeight.divide(2)));
			final Runnable recalculateWidth = () -> {
				textWidth.set(text.getLayoutBounds().getWidth());
				textHeight.set(text.getLayoutBounds().getHeight());
			};
			text.parentProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			text.textProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			group.getChildren().add(text);
			getChildren().add(group);
		}

		public Group getGroup() {
			return group;
		}

		public Line getLine() {
			return line;
		}

		public Text getText() {
			return text;
		}

	}

}