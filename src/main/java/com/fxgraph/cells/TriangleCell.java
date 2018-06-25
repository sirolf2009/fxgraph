package com.fxgraph.cells;

import com.fxgraph.graph.Graph;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TriangleCell extends AbstractCell {

	private final String text;

	public TriangleCell(String text) {
		this.text = text;
	}

	@Override
	public Region getGraphic(Graph graph) {
		final double width = 50;
		final double height = 50;

		final Polygon view = new Polygon(width / 2, 0, width, height, 0, height);

		view.setStroke(Color.RED);
		view.setFill(Color.RED);

		return new Pane(view);
	}

	@Override
	public String toString() {
		return text;
	}

	public String getText() {
		return text;
	}

}