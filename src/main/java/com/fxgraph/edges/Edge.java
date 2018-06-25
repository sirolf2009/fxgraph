package com.fxgraph.edges;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
		return new EdgeGraphic(graph, this);
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public static class EdgeGraphic extends Pane {

		private final Region sourceNode;
		private final Region targetNode;
		private final Group group;
		private final Line line;
		private final Text text;

		public EdgeGraphic(Graph graph, Edge edge) {
			this(graph.getGraphic(edge.getSource()), graph.getGraphic(edge.getTarget()), edge.textProperty);
		}

		public EdgeGraphic(Region sourceNode, Region targetNode, StringProperty textProperty) {
			this.sourceNode = sourceNode;
			this.targetNode = targetNode;
			group = new Group();
			line = new Line();

			line.startXProperty().bind(sourceNode.layoutXProperty().add(sourceNode.widthProperty().divide(2)));
			line.startYProperty().bind(sourceNode.layoutYProperty().add(sourceNode.heightProperty().divide(2)));

			line.endXProperty().bind(targetNode.layoutXProperty().add(targetNode.widthProperty().divide(2)));
			line.endYProperty().bind(targetNode.layoutYProperty().add(targetNode.heightProperty().divide(2)));
			group.getChildren().add(line);

			final DoubleProperty textWidth = new SimpleDoubleProperty();
			final DoubleProperty textHeight = new SimpleDoubleProperty();
			text = new Text();
			text.getStyleClass().add("edge-text");
			text.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2).subtract(textWidth.divide(2)));
			text.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(textHeight.divide(2)));
			text.textProperty().bind(textProperty);
			final Runnable recalculateWidth = () -> {
				textWidth.set(text.getLayoutBounds().getWidth());
				textHeight.set(text.getLayoutBounds().getHeight());
			};
			text.parentProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			textProperty.addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			group.getChildren().add(text);
			getChildren().add(group);
		}

		public Region getSourceNode() {
			return sourceNode;
		}

		public Region getTargetNode() {
			return targetNode;
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