package com.fxgraph.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxgraph.layout.Layout;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class Graph {

	private final Model model;

	private final Group canvas;

	private final ZoomableScrollPane scrollPane;

	private final Map<IGraphNode, Region> graphics;

	MouseGestures mouseGestures;

	/**
	 * the pane wrapper is necessary or else the scrollpane would always align the
	 * top-most and left-most child to the top and left eg when you drag the top
	 * child down, the entire scrollpane would move down
	 */
	CellLayer cellLayer;

	public Graph() {
		this(new Model());
	}

	public Graph(Model model) {
		this.model = model;

		canvas = new Group();
		cellLayer = new CellLayer();

		canvas.getChildren().add(cellLayer);

		mouseGestures = new MouseGestures(this);

		scrollPane = new ZoomableScrollPane(canvas);

		graphics = new HashMap<IGraphNode, Region>();

		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		addEdges(getModel().getAllEdges());
		addCells(getModel().getAllCells());
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
		getCellLayer().getChildren().clear();
	}

	public void endUpdate() {
		// add components to graph pane
		addEdges(model.getAddedEdges());
		addCells(model.getAddedCells());

		// remove components to graph pane
		model.getRemovedCells().stream().map(cell -> getGraphic(cell))
				.forEach(cellGraphic -> getCellLayer().getChildren().remove(cellGraphic));
		model.getRemovedEdges().stream().map(edge -> getGraphic(edge))
				.forEach(edgeGraphic -> getCellLayer().getChildren().remove(edgeGraphic));

		// clean up the model
		getModel().endUpdate();

	}

	private void addEdges(List<IEdge> edges) {
		edges.stream().map(edge -> getGraphic(edge))
				.forEach(edgeGraphic -> getCellLayer().getChildren().add(edgeGraphic));
	}

	private void addCells(List<ICell> cells) {
		cells.stream().map(cell -> getGraphic(cell)).forEach(cellGraphic -> {
			getCellLayer().getChildren().add(cellGraphic);
			mouseGestures.makeDraggable(cellGraphic);
		});
	}

	public Region getGraphic(IGraphNode node) {
		if (!graphics.containsKey(node)) {
			graphics.put(node, node.getGraphic(this));
		}
		return graphics.get(node);
	}

	public double getScale() {
		return this.scrollPane.getScaleValue();
	}

	public void layout(Layout layout) {
		layout.execute(this);
	}
}