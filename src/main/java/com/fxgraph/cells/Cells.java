package com.fxgraph.cells;

import java.util.Arrays;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cells {

	public static void makeResizable(Region region) {
		final double handleRadius = 6d;

		final Rectangle resizeHandleNW = new Rectangle(handleRadius, handleRadius, Color.BLACK);
		resizeHandleNW.xProperty().bind(region.layoutXProperty().subtract(handleRadius / 2));
		resizeHandleNW.yProperty().bind(region.layoutYProperty().subtract(handleRadius / 2));

		final Rectangle resizeHandleSE = new Rectangle(handleRadius, handleRadius, Color.BLACK);
		resizeHandleSE.xProperty().bind(region.layoutXProperty().add(region.widthProperty()).subtract(handleRadius / 2));
		resizeHandleSE.yProperty().bind(region.layoutYProperty().add(region.heightProperty()).subtract(handleRadius / 2));

		region.parentProperty().addListener((obs, oldParent, newParent) -> {
			for(final Rectangle c : Arrays.asList(resizeHandleNW, resizeHandleSE)) {
				final Pane currentParent = (Pane) c.getParent();
				if(currentParent != null) {
					currentParent.getChildren().remove(c);
				}
				((Pane) newParent).getChildren().add(c);
			}
		});

		final Wrapper<Point2D> mouseLocation = new Wrapper<>();

		setUpDragging(resizeHandleNW, mouseLocation, Cursor.NW_RESIZE);
		setUpDragging(resizeHandleSE, mouseLocation, Cursor.SE_RESIZE);

		resizeHandleNW.setOnMouseDragged(event -> {
			if(mouseLocation.value != null) {
				final double deltaX = event.getSceneX() - mouseLocation.value.getX();
				final double deltaY = event.getSceneY() - mouseLocation.value.getY();
				final double newX = region.getLayoutX() - deltaX;

				if(newX != 0 && newX >= handleRadius && newX <= region.getLayoutX() + region.getWidth() - handleRadius) {
					region.setLayoutX(newX);
					region.setPrefWidth(region.getPrefWidth() - deltaX);
				}
				final double newY = region.getLayoutY() + deltaY;
				if(newY != 0 && newY >= handleRadius
						&& newY <= region.getLayoutY() + region.getHeight() - handleRadius) {
					region.setLayoutY(newY);
					region.setPrefHeight(region.getPrefHeight() - deltaY);
				}
				mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
			}
		});

		resizeHandleSE.setOnMouseDragged(event -> {
			if(mouseLocation.value != null) {
				final double deltaX = event.getSceneX() - mouseLocation.value.getX();
				final double deltaY = event.getSceneY() - mouseLocation.value.getY();
				final double newMaxX = region.getLayoutX() + region.getWidth() + deltaX;
				if(newMaxX >= region.getLayoutX()
						&& newMaxX <= region.getParent().getBoundsInLocal().getWidth() - handleRadius) {
					region.setPrefWidth(region.getPrefWidth() + deltaX);
				}
				final double newMaxY = region.getLayoutY() + region.getHeight() + deltaY;
				if(newMaxY >= region.getLayoutY()
						&& newMaxY <= region.getParent().getBoundsInLocal().getHeight() - handleRadius) {
					region.setPrefHeight(region.getPrefHeight() + deltaY);
				}
				mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
			}
		});
	}

	private static void setUpDragging(Node node, Wrapper<Point2D> mouseLocation, Cursor hoverCursor) {
		node.setOnMouseEntered(event -> {
			node.getParent().setCursor(hoverCursor);
		});
		node.setOnMouseExited(event -> {
			node.getParent().setCursor(Cursor.DEFAULT);
		});
		node.setOnDragDetected(event -> {
			node.getParent().setCursor(Cursor.CLOSED_HAND);
			mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
		});

		node.setOnMouseReleased(event -> {
			node.getParent().setCursor(Cursor.DEFAULT);
			mouseLocation.value = null;
		});
	}

	static class Wrapper<T> {
		T value;
	}

}
