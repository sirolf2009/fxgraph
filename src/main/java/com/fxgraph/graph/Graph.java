package com.fxgraph.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxgraph.layout.Layout;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;

public class Graph {

	private final Model model;
	private final PannableCanvas pannableCanvas;
	private final Map<IGraphNode, Region> graphics;
	private final NodeGestures nodeGestures;

	public Graph() {
		this(new Model());
	}

	public Graph(Model model) {
		this.model = model;

		nodeGestures = new NodeGestures(this);

		pannableCanvas = new PannableCanvas();
		final ViewportGestures sceneGestures = new ViewportGestures(pannableCanvas);
		pannableCanvas.parentProperty().addListener((obs, oldVal, newVal) -> {
			if(oldVal != null) {
				oldVal.removeEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
				oldVal.removeEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
				oldVal.removeEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
			}
			if(newVal != null) {
				newVal.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
				newVal.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
				newVal.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
			}
		});

		graphics = new HashMap<>();

		addEdges(getModel().getAllEdges());
		addCells(getModel().getAllCells());
	}

	public PannableCanvas getCanvas() {
		return pannableCanvas;
	}

	public Model getModel() {
		return model;
	}

	public void beginUpdate() {
		getCanvas().getChildren().clear();
	}

	public void endUpdate() {
		// add components to graph pane
		addEdges(model.getAddedEdges());
		addCells(model.getAddedCells());

		// remove components to graph pane
		model.getRemovedCells().stream().map(cell -> getGraphic(cell)).forEach(cellGraphic -> getCanvas().getChildren().remove(cellGraphic));
		model.getRemovedEdges().stream().map(edge -> getGraphic(edge)).forEach(edgeGraphic -> getCanvas().getChildren().remove(edgeGraphic));

		// clean up the model
		getModel().endUpdate();
	}

	private void addEdges(List<IEdge> edges) {
		edges.stream().map(edge -> getGraphic(edge)).forEach(edgeGraphic -> getCanvas().getChildren().add(edgeGraphic));
	}

	private void addCells(List<ICell> cells) {
		cells.stream().map(cell -> getGraphic(cell)).forEach(cellGraphic -> {
			getCanvas().getChildren().add(cellGraphic);
			nodeGestures.makeDraggable(cellGraphic);
		});
	}

	public Region getGraphic(IGraphNode node) {
		if(!graphics.containsKey(node)) {
			graphics.put(node, node.getGraphic(this));
		}
		return graphics.get(node);
	}

	public double getScale() {
		return getCanvas().getScale();
	}

	public void layout(Layout layout) {
		layout.execute(this);
	}
}