package com.fxgraph.graph;

import java.io.Serializable;
import java.util.List;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.edges.Edge;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;

public class Model implements Serializable {

	private static final long serialVersionUID = 172247271876446110L;

	private final ICell root;

	private ObservableList<ICell> allCells;
	private transient ObservableList<ICell> addedCells;
	private transient ObservableList<ICell> removedCells;

	private ObservableList<IEdge> allEdges;
	private transient ObservableList<IEdge> addedEdges;
	private transient ObservableList<IEdge> removedEdges;

	public Model() {
		root = new AbstractCell() {
			@Override
			public Region getGraphic(Graph graph) {
				return null;
			}
		};
		// clear model, create lists
		clear();
	}

	public void clear() {
		allCells = FXCollections.observableArrayList();
		addedCells = FXCollections.observableArrayList();
		removedCells = FXCollections.observableArrayList();

		allEdges = FXCollections.observableArrayList();
		addedEdges = FXCollections.observableArrayList();
		removedEdges = FXCollections.observableArrayList();
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

	public ObservableList<ICell> getAddedCells() {
		return addedCells;
	}

	public ObservableList<ICell> getRemovedCells() {
		return removedCells;
	}

	public ObservableList<ICell> getAllCells() {
		return allCells;
	}

	public ObservableList<IEdge> getAddedEdges() {
		return addedEdges;
	}

	public ObservableList<IEdge> getRemovedEdges() {
		return removedEdges;
	}

	public ObservableList<IEdge> getAllEdges() {
		return allEdges;
	}

	public void addCell(ICell cell) {
		if(cell == null) {
			throw new NullPointerException("Cannot add a null cell");
		}
		addedCells.add(cell);
	}

	public void addEdge(ICell sourceCell, ICell targetCell) {
		final IEdge edge = new Edge(sourceCell, targetCell);
		addEdge(edge);
	}

	public void addEdge(IEdge edge) {
		if(edge == null) {
			throw new NullPointerException("Cannot add a null edge");
		}
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
				root.addCellChild(cell);
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
			root.removeCellChild(cell);
		}
	}

	public ICell getRoot() {
		return root;
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