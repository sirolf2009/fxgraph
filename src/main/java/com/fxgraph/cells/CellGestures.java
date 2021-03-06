package com.fxgraph.cells;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fxgraph.graph.Graph;
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
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final DoubleBinding halfWidthProperty = widthProperty.divide(2);

			final Rectangle resizeHandleN = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleN.xProperty().bind(xProperty.add(halfWidthProperty).subtract(handleRadius / 2));
			resizeHandleN.yProperty().bind(yProperty.subtract(handleRadius / 2));

			setUpDragging(resizeHandleN, mouseEvent, Cursor.N_RESIZE);

			resizeHandleN.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragNorth(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleN;
		}
	};

	static DragNodeSupplier NORTH_EAST = new DragNodeSupplier() {
		@Override
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();

			final Rectangle resizeHandleNE = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleNE.xProperty().bind(xProperty.add(widthProperty).subtract(handleRadius / 2));
			resizeHandleNE.yProperty().bind(yProperty.subtract(handleRadius / 2));

			setUpDragging(resizeHandleNE, mouseEvent, Cursor.NE_RESIZE);

			resizeHandleNE.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragNorth(event, mouseEvent, region, handleRadius);
					dragEast(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleNE;
		}
	};

	static DragNodeSupplier EAST = new DragNodeSupplier() {
		@Override
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();
			final DoubleBinding halfHeightProperty = heightProperty.divide(2);

			final Rectangle resizeHandleE = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleE.xProperty().bind(xProperty.add(widthProperty).subtract(handleRadius / 2));
			resizeHandleE.yProperty().bind(yProperty.add(halfHeightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleE, mouseEvent, Cursor.E_RESIZE);

			resizeHandleE.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragEast(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleE;
		}
	};

	static DragNodeSupplier SOUTH_EAST = new DragNodeSupplier() {
		@Override
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();

			final Rectangle resizeHandleSE = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleSE.xProperty().bind(xProperty.add(widthProperty).subtract(handleRadius / 2));
			resizeHandleSE.yProperty().bind(yProperty.add(heightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleSE, mouseEvent, Cursor.SE_RESIZE);

			resizeHandleSE.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragSouth(event, mouseEvent, region, handleRadius);
					dragEast(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleSE;
		}
	};

	static DragNodeSupplier SOUTH = new DragNodeSupplier() {
		@Override
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty widthProperty = region.prefWidthProperty();
			final DoubleBinding halfWidthProperty = widthProperty.divide(2);
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();

			final Rectangle resizeHandleS = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleS.xProperty().bind(xProperty.add(halfWidthProperty).subtract(handleRadius / 2));
			resizeHandleS.yProperty().bind(yProperty.add(heightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleS, mouseEvent, Cursor.S_RESIZE);

			resizeHandleS.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragSouth(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleS;
		}
	};

	static DragNodeSupplier SOUTH_WEST = new DragNodeSupplier() {
		@Override
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();

			final Rectangle resizeHandleSW = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleSW.xProperty().bind(xProperty.subtract(handleRadius / 2));
			resizeHandleSW.yProperty().bind(yProperty.add(heightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleSW, mouseEvent, Cursor.SW_RESIZE);

			resizeHandleSW.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragSouth(event, mouseEvent, region, handleRadius);
					dragWest(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleSW;
		}
	};

	static DragNodeSupplier WEST = new DragNodeSupplier() {
		@Override
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();
			final ReadOnlyDoubleProperty heightProperty = region.prefHeightProperty();
			final DoubleBinding halfHeightProperty = heightProperty.divide(2);

			final Rectangle resizeHandleW = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleW.xProperty().bind(xProperty.subtract(handleRadius / 2));
			resizeHandleW.yProperty().bind(yProperty.add(halfHeightProperty).subtract(handleRadius / 2));

			setUpDragging(resizeHandleW, mouseEvent, Cursor.W_RESIZE);

			resizeHandleW.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragWest(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleW;
		}
	};

	static DragNodeSupplier NORTH_WEST = new DragNodeSupplier() {
		@Override
		public Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent) {
			final DoubleProperty xProperty = region.layoutXProperty();
			final DoubleProperty yProperty = region.layoutYProperty();

			final Rectangle resizeHandleNW = new Rectangle(handleRadius, handleRadius, Color.BLACK);
			resizeHandleNW.xProperty().bind(xProperty.subtract(handleRadius / 2));
			resizeHandleNW.yProperty().bind(yProperty.subtract(handleRadius / 2));

			setUpDragging(resizeHandleNW, mouseEvent, Cursor.NW_RESIZE);

			resizeHandleNW.setOnMouseDragged(event -> {
				if(mouseEvent.value != null && mouseEvent.value.getButton() == graph.getNodeGestures().getDragButton()) {
					dragNorth(event, mouseEvent, region, handleRadius);
					dragWest(event, mouseEvent, region, handleRadius);
					mouseEvent.value = event;
				}
			});
			return resizeHandleNW;
		}
	};

	public static void makeResizable(Graph graph, Region region) {
		makeResizable(graph, region, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);
	}

	public static void makeResizable(Graph graph, Region region, DragNodeSupplier... nodeSuppliers) {
		final Wrapper<MouseEvent> mouseEvent = new Wrapper<>();
		final List<Node> dragNodes = Arrays.stream(nodeSuppliers).map(supplier -> supplier.apply(graph, region, mouseEvent)).collect(Collectors.toList());
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

	private static void dragNorth(MouseEvent event, Wrapper<MouseEvent> mouseEvent, Region region, double handleRadius) {
		final double deltaY = event.getSceneY() - mouseEvent.value.getY();
		final double newY = region.getLayoutY() + deltaY;
		if(newY != 0 && newY >= handleRadius && newY <= region.getLayoutY() + region.getHeight() - handleRadius) {
			region.setLayoutY(newY);
			region.setPrefHeight(region.getPrefHeight() - deltaY);
		}
	}

	private static void dragEast(MouseEvent event, Wrapper<MouseEvent> mouseEvent, Region region, double handleRadius) {
		final double deltaX = event.getSceneX() - mouseEvent.value.getX();
		final double newMaxX = region.getLayoutX() + region.getWidth() + deltaX;
		if(newMaxX >= region.getLayoutX() && newMaxX <= region.getParent().getBoundsInLocal().getWidth() - handleRadius) {
			region.setPrefWidth(region.getPrefWidth() + deltaX);
		}
	}

	private static void dragSouth(MouseEvent event, Wrapper<MouseEvent> mouseEvent, Region region, double handleRadius) {
		final double deltaY = event.getSceneY() - mouseEvent.value.getY();
		final double newMaxY = region.getLayoutY() + region.getHeight() + deltaY;
		if(newMaxY >= region.getLayoutY() && newMaxY <= region.getParent().getBoundsInLocal().getHeight() - handleRadius) {
			region.setPrefHeight(region.getPrefHeight() + deltaY);
		}
	}

	private static void dragWest(MouseEvent event, Wrapper<MouseEvent> mouseEvent, Region region, double handleRadius) {
		final double deltaX = event.getSceneX() - mouseEvent.value.getX();
		final double newX = region.getLayoutX() + deltaX;
		if(newX != 0 && newX <= region.getParent().getBoundsInLocal().getWidth() - handleRadius) {
			region.setLayoutX(newX);
			region.setPrefWidth(region.getPrefWidth() - deltaX);
		}
	}

	private static void setUpDragging(Node node, Wrapper<MouseEvent> mouseEvent, Cursor hoverCursor) {
		node.setOnMouseEntered(event -> {
			node.getParent().setCursor(hoverCursor);
		});
		node.setOnMouseExited(event -> {
			node.getParent().setCursor(Cursor.DEFAULT);
		});
		node.setOnDragDetected(event -> {
			node.getParent().setCursor(Cursor.CLOSED_HAND);
			mouseEvent.value = event;
		});

		node.setOnMouseReleased(event -> {
			node.getParent().setCursor(Cursor.DEFAULT);
			mouseEvent.value = null;
		});
	}

	static class Wrapper<T> {
		T value;
	}

	interface DragNodeSupplier {
		Node apply(Graph graph, Region region, Wrapper<MouseEvent> mouseEvent);
	}

}
