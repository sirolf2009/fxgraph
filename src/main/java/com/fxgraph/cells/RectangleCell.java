package com.fxgraph.cells;

import com.fxgraph.graph.Graph;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleCell extends AbstractCell {

	public RectangleCell() {
	}

	@Override
	public Region getGraphic(Graph graph) {
		final Rectangle view = new Rectangle(50, 50);

		view.setStroke(Color.DODGERBLUE);
		view.setFill(Color.DODGERBLUE);

		final Pane pane = new Pane(view);
		pane.setPrefSize(50, 50);
		view.widthProperty().bind(pane.prefWidthProperty());
		view.heightProperty().bind(pane.prefHeightProperty());
		CellGestures.makeResizable(graph, pane);

		return pane;
	}

}
