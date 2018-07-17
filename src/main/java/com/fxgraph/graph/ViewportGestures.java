package com.fxgraph.graph;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Listeners for making the scene's viewport draggable and zoomable
 */
public class ViewportGestures {

	private static final double MAX_SCALE = 10.0d;
	private static final double MIN_SCALE = .1d;

	private final PannableCanvas.DragContext sceneDragContext = new PannableCanvas.DragContext();

	PannableCanvas canvas;

	public ViewportGestures(PannableCanvas canvas) {
		this.canvas = canvas;
	}

	public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	public EventHandler<ScrollEvent> getOnScrollEventHandler() {
		return onScrollEventHandler;
	}

	private final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {

			// right mouse button => panning
			if(!event.isSecondaryButtonDown()) {
				return;
			}

			sceneDragContext.mouseAnchorX = event.getSceneX();
			sceneDragContext.mouseAnchorY = event.getSceneY();

			sceneDragContext.translateAnchorX = canvas.getTranslateX();
			sceneDragContext.translateAnchorY = canvas.getTranslateY();

		}

	};

	private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {

			// right mouse button => panning
			if(!event.isSecondaryButtonDown()) {
				return;
			}

			canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
			canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

			event.consume();
		}
	};

	/**
	 * Mouse wheel handler: zoom to pivot point
	 */
	private final EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {

			final double delta = 1.2;

			double scale = canvas.getScale(); // currently we only use Y, same value is used for X
			final double oldScale = scale;

			if(event.getDeltaY() < 0) {
				scale /= delta;
			} else {
				scale *= delta;
			}

			scale = clamp(scale, MIN_SCALE, MAX_SCALE);

			final double f = (scale / oldScale) - 1;

			final double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX()));
			final double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY()));

			canvas.setScale(scale);

			// note: pivot value must be untransformed, i. e. without scaling
			canvas.setPivot(f * dx, f * dy);

			event.consume();

		}

	};

	public static double clamp(double value, double min, double max) {

		if(Double.compare(value, min) < 0) {
			return min;
		}

		if(Double.compare(value, max) > 0) {
			return max;
		}

		return value;
	}
}