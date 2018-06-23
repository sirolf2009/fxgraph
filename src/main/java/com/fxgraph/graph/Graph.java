package com.fxgraph.graph;

import com.fxgraph.layout.Layout;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class Graph {

	private final Model model;

	private final Group canvas;

	private final ZoomableScrollPane scrollPane;

	MouseGestures mouseGestures;

	/**
	 * the pane wrapper is necessary or else the scrollpane would always align the
	 * top-most and left-most child to the top and left eg when you drag the top
	 * child down, the entire scrollpane would move down
	 */
	CellLayer cellLayer;

	public Graph() {

		this.model = new Model();

		canvas = new Group();
		cellLayer = new CellLayer();

		canvas.getChildren().add(cellLayer);

		mouseGestures = new MouseGestures(this);

		scrollPane = new ZoomableScrollPane(canvas);

		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

	}

	public ScrollPane getScrollPane() {
		return this.scrollPane;
	}

	public Pane getCellLayer() {
		return this.cellLayer;
	}

	public Model getModel() {
		return model;
	}

	public void beginUpdate() {
	}

	public void endUpdate() {

		// add components to graph pane
		getCellLayer().getChildren().addAll(model.getAddedEdges());
		getCellLayer().getChildren().addAll(model.getAddedCells());

		// remove components from graph pane
		getCellLayer().getChildren().removeAll(model.getRemovedCells());
		getCellLayer().getChildren().removeAll(model.getRemovedEdges());

		// enable dragging of cells
		for (final Cell cell : model.getAddedCells()) {
			mouseGestures.makeDraggable(cell);
		}

		// every cell must have a parent, if it doesn't, then the graphParent is
		// the parent
		getModel().attachOrphansToGraphParent(model.getAddedCells());

		// remove reference to graphParent
		getModel().disconnectFromGraphParent(model.getRemovedCells());

		// merge added & removed cells with all cells
		getModel().merge();

	}

	public double getScale() {
		return this.scrollPane.getScaleValue();
	}

	public void layout(Layout layout) {
		layout.execute(this);
	}
}