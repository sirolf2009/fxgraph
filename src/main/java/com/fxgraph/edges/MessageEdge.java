package com.fxgraph.edges;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.SequenceDiagram.IActorCell;
import com.fxgraph.graph.SequenceDiagram.IMessageEdge;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class MessageEdge extends AbstractEdge implements IMessageEdge {

	private final String name;
	private final DoubleProperty yOffsetProperty = new SimpleDoubleProperty();

	public MessageEdge(IActorCell source, IActorCell target, String name) {
		super(source, target, true);
		this.name = name;
	}

	@Override
	public DoubleProperty yOffsetProperty() {
		return yOffsetProperty;
	}

	@Override
	public Region getGraphic(Graph graph) {
		return new EdgeGraphic(graph, this);
	}

	private static class EdgeGraphic extends AbstractEdgeGraphic {
		public EdgeGraphic(Graph graph, MessageEdge edge) {
			final DoubleBinding sourceX = edge.getSource().getXAnchor(graph);
			final DoubleBinding sourceY = edge.getSource().getYAnchor(graph).add(edge.yOffsetProperty);
			final DoubleBinding targetX = edge.getTarget().getXAnchor(graph);
			final DoubleBinding targetY = edge.getTarget().getYAnchor(graph).add(edge.yOffsetProperty);

			if (edge.isDirected()) {
				Region target = graph.getGraphic(edge.getTarget());
				setupArrow(target, sourceX, sourceY, targetX, targetY);
				/*
				arrow.getStyleClass().add("arrow");

				arrow.startXProperty().bind(sourceX);
				arrow.startYProperty().bind(sourceY);

				arrow.endXProperty().bind(targetX);
				arrow.endYProperty().bind(targetY);

				 */
				group.getChildren().add(arrow);
			}

			final DoubleProperty textWidth = new SimpleDoubleProperty();
			final DoubleProperty textHeight = new SimpleDoubleProperty();

			Text text = new Text(edge.name);
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
			getStyleClass().add("message-edge");
		}
	}
}
