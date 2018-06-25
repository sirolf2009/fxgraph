package com.fxgraph.layout;

import java.util.List;
import java.util.Random;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;

public class RandomLayout extends Layout {

	private final Random rnd = new Random();

	@Override
	public void execute(Graph graph) {
		final List<ICell> cells = graph.getModel().getAllCells();

		for(final ICell cell : cells) {
			final double x = rnd.nextDouble() * 500;
			final double y = rnd.nextDouble() * 500;

			graph.getGraphic(cell).relocate(x, y);
		}
	}

}