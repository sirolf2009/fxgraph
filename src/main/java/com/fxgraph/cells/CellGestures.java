package com.fxgraph.cells;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellGestures {

	static final double handleRadius = 6d;

	static DragNodeSupplier NORTH = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final DoubleBinding halfWidthProperty = widthProperty.divide(2);

			final Rectangle resizeHandleN = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleN.xProperty().bind(xProperty.add(halfWidthProperty).subtract(handleRadius / 2));
			resizeHandleN.yProperty().bind(yProperty.subtract(handleRadius / 2));

			setUpDragging(resizeHandleN, mouseLocation, Cursor.N_RESIZE);

			resizeHandleN.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragNorth(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleN;
		}
	};

	static DragNodeSupplier NORTH_EAST = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();

			final Rectangle resizeHandleNE = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleNE.xProperty().bind(xProperty.add(widthProperty).subtract(handleRadius / 2));
			resizeHandleNE.yProperty().bind(yProperty.subtract(handleRadius / 2));

			setUpDragging(resizeHandleNE, mouseLocation, Cursor.NE_RESIZE);

			resizeHandleNE.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragNorth(event, mouseLocation, region, handleRadius);
					dragEast(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleNE;
		}
	};

	static DragNodeSupplier EAST = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();
			final DoubleBinding halfHeightProperty = heightProperty.divide(2);

			final Rectangle resizeHandleE = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleE.xProperty().bind(xProperty.add(widthProperty).subtract(handleRadius / 2));
			resizeHandleE.yProperty().bind(yProperty.add(halfHeightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleE, mouseLocation, Cursor.E_RESIZE);

			resizeHandleE.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragEast(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleE;
		}
	};

	static DragNodeSupplier SOUTH_EAST = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();

			final Rectangle resizeHandleSE = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleSE.xProperty().bind(xProperty.add(widthProperty).subtract(handleRadius / 2));
			resizeHandleSE.yProperty().bind(yProperty.add(heightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleSE, mouseLocation, Cursor.SE_RESIZE);

			resizeHandleSE.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragSouth(event, mouseLocation, region, handleRadius);
					dragEast(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleSE;
		}
	};

	static DragNodeSupplier SOUTH = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final DoubleBinding halfWidthProperty = widthProperty.divide(2);
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();

			final Rectangle resizeHandleS = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleS.xProperty().bind(xProperty.add(halfWidthProperty).subtract(handleRadius / 2));
			resizeHandleS.yProperty().bind(yProperty.add(heightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleS, mouseLocation, Cursor.S_RESIZE);

			resizeHandleS.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragSouth(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleS;
		}
	};

	static DragNodeSupplier SOUTH_WEST = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();

			final Rectangle resizeHandleSW = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleSW.xProperty().bind(xProperty.subtract(handleRadius / 2));
			resizeHandleSW.yProperty().bind(yProperty.add(heightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleSW, mouseLocation, Cursor.SW_RESIZE);

			resizeHandleSW.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragSouth(event, mouseLocation, region, handleRadius);
					dragWest(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleSW;
		}
	};

	static DragNodeSupplier WEST = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();
			final DoubleBinding halfHeightProperty = heightProperty.divide(2);

			final Rectangle resizeHandleW = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleW.xProperty().bind(xProperty.subtract(handleRadius / 2));
			resizeHandleW.yProperty().bind(yProperty.add(halfHeightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleW, mouseLocation, Cursor.W_RESIZE);

			resizeHandleW.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragWest(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleW;
		}
	};

	static DragNodeSupplier NORTH_WEST = new DragNodeSupplier() {
		@Override
		public Node apply(Region region, Wrapper<Point2D> mouseLocation) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();

			final Rectangle resizeHandleNW = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleNW.xProperty().bind(xProperty.subtract(handleRadius / 2));
			resizeHandleNW.yProperty().bind(yProperty.subtract(handleRadius / 2));

			setUpDragging(resizeHandleNW, mouseLocation, Cursor.NW_RESIZE);

			resizeHandleNW.setOnMouseDragged(event -> {
				if(mouseLocation.value != null) {
					dragNorth(event, mouseLocation, region, handleRadius);
					dragWest(event, mouseLocation, region, handleRadius);
					mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			return resizeHandleNW;
		}
	};

	public static void makeResizable(Region region) {
		makeResizable(region, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);
	}

	public static void makeResizable(Region region, DragNodeSupplier... nodeSuppliers) {
		final Wrapper<Point2D> mouseLocation = new Wrapper<>();
		final List<Node> dragNodes = Arrays.stream(nodeSuppliers).map(supplier -> supplier.apply(region, mouseLocation)).collect(Collectors.toList());
		region.parentProperty().addListener((obs, oldParent, newParent) -> {
			for(final Node c : dragNodes) {
				final Pane currentParent = (Pane) c.getParent();
				if(currentParent != null) {
					currentParent.getChildren().remove(c);
				}
				((Pane) newParent).getChildren().add(c);
			}
		});
	}

	private static void dragNorth(MouseEvent event, Wrapper<Point2D> mouseLocation, Region region, double handleRadius) {
		final double deltaY = event.getSceneY() - mouseLocation.value.getY();
		final double newY = region.getLayoutY() + deltaY;
		if(newY != 0 && newY >= handleRadius && newY <= region.getLayoutY() + region.getHeight() - handleRadius) {
			region.setLayoutY(newY);
			region.setPrefHeight(region.getPrefHeight() - deltaY);
		}
	}

	private static void dragEast(MouseEvent event, Wrapper<Point2D> mouseLocation, Region region, double handleRadius) {
		final double deltaX = event.getSceneX() - mouseLocation.value.getX();
		final double newMaxX = region.getLayoutX() + region.getWidth() + deltaX;
		if(newMaxX >= region.getLayoutX() && newMaxX <= region.getParent().getBoundsInLocal().getWidth() - handleRadius) {
			region.setPrefWidth(region.getPrefWidth() + deltaX);
		}
	}

	private static void dragSouth(MouseEvent event, Wrapper<Point2D> mouseLocation, Region region, double handleRadius) {
		final double deltaY = event.getSceneY() - mouseLocation.value.getY();
		final double newMaxY = region.getLayoutY() + region.getHeight() + deltaY;
		if(newMaxY >= region.getLayoutY() && newMaxY <= region.getParent().getBoundsInLocal().getHeight() - handleRadius) {
			region.setPrefHeight(region.getPrefHeight() + deltaY);
		}
	}

	private static void dragWest(MouseEvent event, Wrapper<Point2D> mouseLocation, Region region, double handleRadius) {
		final double deltaX = event.getSceneX() - mouseLocation.value.getX();
		final double newX = region.getLayoutX() + deltaX;
		if(newX != 0 && newX <= region.getParent().getBoundsInLocal().getWidth() - handleRadius) {
			region.setLayoutX(newX);
			region.setPrefWidth(region.getPrefWidth() - deltaX);
		}
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

	static interface DragNodeSupplier {

		public Node apply(Region region, Wrapper<Point2D> mouseLocation);

	}

}
