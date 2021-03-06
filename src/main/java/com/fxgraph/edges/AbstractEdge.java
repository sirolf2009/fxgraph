package com.fxgraph.edges;

import com.fxgraph.graph.ICell;
import com.fxgraph.graph.IEdge;

public abstract class AbstractEdge implements IEdge {

	private final ICell source;
	private final ICell target;
	private final boolean isDirected;

	public AbstractEdge(ICell source, ICell target, boolean isDirected) {
		this.source = source;
		this.target = target;
		this.isDirected = isDirected;

		if(source == null) {
			throw new NullPointerException("Source cannot be null");
		}
		if(target == null) {
			throw new NullPointerException("Target cannot be null");
		}

		source.addCellParent(target);
		target.addCellChild(source);
	}

	@Override
	public ICell getSource() {
		return source;
	}

	@Override
	public ICell getTarget() {
		return target;
	}

	@Override
	public boolean isDirected() {
		return isDirected;
	}
}
