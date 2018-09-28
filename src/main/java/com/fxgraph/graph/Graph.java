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
	private final ViewportGestures viewportGestures;

	public Graph() {
		this(new Model());
	}

	public Graph(Model model) {
		this.model = model;

		nodeGestures = new NodeGestures(this);

		pannableCanvas = new PannableCanvas();
		viewportGestures = new ViewportGestures(pannableCanvas);
		pannableCanvas.parentProperty().addListener((obs, oldVal, newVal) -> {
			if(oldVal != null) {
				oldVal.removeEventFilter(MouseEvent.MOUSE_PRESSED, viewportGestures.getOnMousePressedEventHandler());
				oldVal.removeEventFilter(MouseEvent.MOUSE_DRAGGED, viewportGestures.getOnMouseDraggedEventHandler());
				oldVal.removeEventFilter(ScrollEvent.ANY, viewportGestures.getOnScrollEventHandler());
			}
			if(newVal != null) {
				newVal.addEventFilter(MouseEvent.MOUSE_PRESSED, viewportGestures.getOnMousePressedEventHandler());
				newVal.addEventFilter(MouseEvent.MOUSE_DRAGGED, viewportGestures.getOnMouseDraggedEventHandler());
				newVal.addEventFilter(ScrollEvent.ANY, viewportGestures.getOnScrollEventHandler());
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
		edges.stream().map(edge -> {
			try {
				return getGraphic(edge);
			} catch(final Exception e) {
				throw new RuntimeException("failed to get graphic for " + edge, e);
			}
		}).forEach(edgeGraphic -> getCanvas().getChildren().add(edgeGraphic));
	}

	private void addCells(List<ICell> cells) {
		cells.stream().map(cell -> getGraphic(cell)).forEach(cellGraphic -> {
			getCanvas().getChildren().add(cellGraphic);
			nodeGestures.makeDraggable(cellGraphic);
		});
	}

	public Region getGraphic(IGraphNode node) {
		try {
			if(!graphics.containsKey(node)) {
				createGraphic(node);
			}
			return graphics.get(node);
		} catch(final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Region createGraphic(IGraphNode node) {
		return graphics.put(node, node.getGraphic(this));
	}

	public double getScale() {
		return getCanvas().getScale();
	}

	public void layout(Layout layout) {
		layout.execute(this);
	}

	public NodeGestures getNodeGestures() {
		return nodeGestures;
	}

	public ViewportGestures getViewportGestures() {
		return viewportGestures;
	}
}