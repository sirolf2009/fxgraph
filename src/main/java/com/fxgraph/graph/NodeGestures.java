package com.fxgraph.graph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class NodeGestures {

	private final DragContext dragContext = new DragContext();
	private final Graph graph;
	private MouseButton dragButton = MouseButton.PRIMARY;
	private Node lastTransparentNode;

	public NodeGestures(Graph graph) {
		this.graph = graph;
	}

	public void setDragButton(MouseButton dragButton) {
		this.dragButton = dragButton;
	}

	public MouseButton getDragButton() {
		return dragButton;
	}

	public void makeDraggable(final Node node) {
		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
	}

	public void makeUndraggable(final Node node) {
		node.setOnMousePressed(null);
		node.setOnMouseDragged(null);
	}

	public void revertLastNodeTransparency() {
		setNodeMouseTransparency(lastTransparentNode, false);
	}

	private void setNodeMouseTransparency(Node node, boolean value) {
		if (node == null) {
			return;
		}
		node.setMouseTransparent(value);
		if (node instanceof Parent) {
			for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
				setNodeMouseTransparency(child, value);
			}
		}
	}

	final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			final Node node = (Node) event.getSource();

			if (event.getButton() == graph.getViewportGestures().getPanButton()) {
				// make the node transparent to mouse events if it is the pan button clicked.
				// this allows the pan button to be used while over the node.
				// we will restore its transparency when the mouse button is released.
				lastTransparentNode = node;
				setNodeMouseTransparency(node, true);
			} else {
				final double scale = graph.getScale();

				dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
				dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

				event.consume();
			}
		}
	};

	final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {

			final Node node = (Node) event.getSource();

			if (event.getButton() == getDragButton()) {
				double offsetX = event.getScreenX() + dragContext.x;
				double offsetY = event.getScreenY() + dragContext.y;

				// adjust the offset in case we are zoomed
				final double scale = graph.getScale();

				offsetX /= scale;
				offsetY /= scale;

				node.relocate(offsetX, offsetY);

				// only consume if target button.
				// allows for "pass-through" of events to parent when not the target button.
				event.consume();
			}
		}
	};

	public static class DragContext {
		double x;
		double y;
	}
}