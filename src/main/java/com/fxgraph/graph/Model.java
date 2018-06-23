package com.fxgraph.graph;

import java.util.ArrayList;
import java.util.List;

public class Model {

	Cell graphParent;

	List<Cell> allCells;
	List<Cell> addedCells;
	List<Cell> removedCells;

	List<Edge> allEdges;
	List<Edge> addedEdges;
	List<Edge> removedEdges;

	public Model() {
		graphParent = new Cell();
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

	public List<Cell> getAddedCells() {
		return addedCells;
	}

	public List<Cell> getRemovedCells() {
		return removedCells;
	}

	public List<Cell> getAllCells() {
		return allCells;
	}

	public List<Edge> getAddedEdges() {
		return addedEdges;
	}

	public List<Edge> getRemovedEdges() {
		return removedEdges;
	}

	public List<Edge> getAllEdges() {
		return allEdges;
	}

	public void addCell(Cell cell) {
		addedCells.add(cell);
	}

	public void addEdge(Cell sourceCell, Cell targetCell) {
		final Edge edge = new Edge(sourceCell, targetCell);
		addEdge(edge);
	}

	public void addEdge(Edge edge) {
		addedEdges.add(edge);
	}

	/**
	 * Attach all cells which don't have a parent to graphParent
	 *
	 * @param cellList
	 */
	public void attachOrphansToGraphParent(List<Cell> cellList) {
		for (final Cell cell : cellList) {
			if (cell.getCellParents().size() == 0) {
				graphParent.addCellChild(cell);
			}
		}
	}

	/**
	 * Remove the graphParent reference if it is set
	 *
	 * @param cellList
	 */
	public void disconnectFromGraphParent(List<Cell> cellList) {
		for (final Cell cell : cellList) {
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