package com.fxgraph.cells;

import com.fxgraph.graph.Graph;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleCell extends AbstractCell {

	@Override
	public Region getGraphic(Graph graph) {
		final Rectangle view = new Rectangle(50, 50);

		view.setStroke(Color.DODGERBLUE);
		view.setFill(Color.DODGERBLUE);
		return new Pane(view);
	}

}
