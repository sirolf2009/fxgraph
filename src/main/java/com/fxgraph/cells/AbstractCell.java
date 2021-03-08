package com.fxgraph.cells;

import com.fxgraph.graph.ICell;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCell implements ICell {

	private final List<ICell> children = new ArrayList<>();
	private final List<ICell> parents = new ArrayList<>();

	@Override
	public void addCellChild(ICell cell) {
		children.add(cell);
	}

	@Override
	public List<ICell> getCellChildren() {
		return children;
	}

	@Override
	public void addCellParent(ICell cell) {
		parents.add(cell);
	}

	@Override
	public List<ICell> getCellParents() {
		return parents;
	}

	@Override
	public void removeCellChild(ICell cell) {
		children.remove(cell);
	}

}
