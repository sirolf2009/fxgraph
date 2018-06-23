package com.fxgraph.cells;

import com.fxgraph.graph.Cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TriangleCell extends Cell {

	public TriangleCell() {
		final double width = 50;
		final double height = 50;

		final Polygon view = new Polygon(width / 2, 0, width, height, 0, height);

		view.setStroke(Color.RED);
		view.setFill(Color.RED);

		setView(view);
	}

}