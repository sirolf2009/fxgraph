package com.fxgraph.graph;

import java.util.HashMap;
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
	 * the pane wrapper is necessary or else the scrollpane would always align the top-most and left-most child to the top and left eg when you drag the top child down, the entire scrollpane would move down
	 */
	CellLayer cellLayer;

	public Graph() {

		this.model = new Model();

		canvas = new Group();
		cellLayer = new CellLayer();

		canvas.getChildren().add(cellLayer);

		mouseGestures = new MouseGestures(this);

		scrollPane = new ZoomableScrollPane(canvas);

		graphics = new HashMap<IGraphNode, Region>();

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
		getCellLayer().getChildren().clear();
	}

	public void endUpdate() {
		// add components to graph pane
		model.getAddedEdges().stream().map(edge -> getGraphic(edge)).forEach(edgeGraphic -> getCellLayer().getChildren().add(edgeGraphic));
		model.getAddedCells().stream().map(cell -> getGraphic(cell)).forEach(cellGraphic -> getCellLayer().getChildren().add(cellGraphic));

		// remove components to graph pane
		model.getRemovedCells().stream().map(cell -> getGraphic(cell)).forEach(cellGraphic -> getCellLayer().getChildren().remove(cellGraphic));
		model.getRemovedEdges().stream().map(edge -> getGraphic(edge)).forEach(edgeGraphic -> getCellLayer().getChildren().remove(edgeGraphic));

		// make nodes draggable
		model.getAddedCells().stream().map(cell -> getGraphic(cell)).forEach(cellGraphic -> mouseGestures.makeDraggable(cellGraphic));

		// clean up the model
		getModel().endUpdate();

	}

	public Region getGraphic(IGraphNode node) {
		if(!graphics.containsKey(node)) {
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