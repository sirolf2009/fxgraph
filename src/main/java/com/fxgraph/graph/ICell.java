package com.fxgraph.graph;

import java.util.List;

public interface ICell extends IGraphNode {

	public void addCellChild(ICell cell);

	public List<ICell> getCellChildren();

	public void addCellParent(ICell cell);

	public List<ICell> getCellParents();

	public void removeCellChild(ICell cell);

}
