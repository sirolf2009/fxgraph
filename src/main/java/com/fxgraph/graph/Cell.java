package com.fxgraph.graph;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Cell extends Pane {

	List<Cell> children = new ArrayList<>();
	List<Cell> parents = new ArrayList<>();

	Node view;

	public Cell() {
	}

	public void addCellChild(Cell cell) {
		children.add(cell);
	}

	public List<Cell> getCellChildren() {
		return children;
	}

	public void addCellParent(Cell cell) {
		parents.add(cell);
	}

	public List<Cell> getCellParents() {
		return parents;
	}

	public void removeCellChild(Cell cell) {
		children.remove(cell);
	}

	public void setView(Node view) {

		this.view = view;
		getChildren().add(view);

	}

	public Node getView() {
		return this.view;
	}

}