package com.fxgraph.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.edges.Edge;

import javafx.scene.layout.Region;

public class Model implements Serializable {

	private static final long serialVersionUID = 172247271876446110L;

	private final ICell graphParent;

	private List<ICell> allCells;
	private transient List<ICell> addedCells;
	private transient List<ICell> removedCells;

	private List<IEdge> allEdges;
	private transient List<IEdge> addedEdges;
	private transient List<IEdge> removedEdges;

	public Model() {
		graphParent = new AbstractCell() {
			@Override
			public Region getGraphic(Graph graph) {
				return null;
			}
		};
		// clear model, create lists
		clear();
	}

	public void clear() {
		allCells = new ArrayList<>();
		addedCells = new ArrayList<>();
		removedCells = new ArrayList<>();

		allEdges = new ArrayList<>();
		addedEdges = new ArrayList<>();
		removedEdges = new ArrayList<>();
	}

	public void clearAddedLists() {
		addedCells.clear();
		addedEdges.clear();
	}

	public void endUpdate() {
		// every cell must have a parent, if it doesn't, then the graphParent is
		// the parent
		attachOrphansToGraphParent(getAddedCells());

		// remove reference to graphParent
		disconnectFromGraphParent(getRemovedCells());

		// merge added & removed cells with all cells
		merge();
	}

	public List<ICell> getAddedCells() {
		return addedCells;
	}

	public List<ICell> getRemovedCells() {
		return removedCells;
	}

	public List<ICell> getAllCells() {
		return allCells;
	}

	public List<IEdge> getAddedEdges() {
		return addedEdges;
	}

	public List<IEdge> getRemovedEdges() {
		return removedEdges;
	}

	public List<IEdge> getAllEdges() {
		return allEdges;
	}

	public void addCell(ICell cell) {
		addedCells.add(cell);
	}

	public void addEdge(ICell sourceCell, ICell targetCell) {
		final IEdge edge = new Edge(sourceCell, targetCell);
		addEdge(edge);
	}

	public void addEdge(IEdge edge) {
		addedEdges.add(edge);
	}

	/**
	 * Attach all cells which don't have a parent to graphParent
	 *
	 * @param cellList
	 */
	public void attachOrphansToGraphParent(List<ICell> cellList) {
		for(final ICell cell : cellList) {
			if(cell.getCellParents().size() == 0) {
				graphParent.addCellChild(cell);
			}
		}
	}

	/**
	 * Remove the graphParent reference if it is set
	 *
	 * @param cellList
	 */
	public void disconnectFromGraphParent(List<ICell> cellList) {
		for(final ICell cell : cellList) {
			graphParent.removeCellChild(cell);
		}
	}

	public void merge() {
		// cells
		allCells.addAll(addedCells);
		allCells.removeAll(removedCells);

		addedCells.clear();
		removedCells.clear();

		// edges
		allEdges.addAll(addedEdges);
		allEdges.removeAll(removedEdges);

		addedEdges.clear();
		removedEdges.clear();
	}
}