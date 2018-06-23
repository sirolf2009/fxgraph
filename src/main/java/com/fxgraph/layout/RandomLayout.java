package com.fxgraph.layout;

import java.util.List;
import java.util.Random;

import com.fxgraph.graph.Cell;
import com.fxgraph.graph.Graph;

public class RandomLayout extends Layout {

	private final Random rnd = new Random();

	@Override
	public void execute(Graph graph) {
		final List<Cell> cells = graph.getModel().getAllCells();

		for (final Cell cell : cells) {
			final double x = rnd.nextDouble() * 500;
			final double y = rnd.nextDouble() * 500;

			cell.relocate(x, y);
		}
	}

}