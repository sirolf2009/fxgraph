package com.fxgraph.graph;

import java.util.List;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Region;

public interface ICell extends IGraphNode {

	void addCellChild(ICell cell);

	List<ICell> getCellChildren();

	void addCellParent(ICell cell);

	List<ICell> getCellParents();

	void removeCellChild(ICell cell);

	default DoubleBinding getXAnchor(Graph graph) {
		final Region graphic = graph.getGraphic(this);
		return graphic.layoutXProperty().add(graphic.widthProperty().divide(2));
	}

	default DoubleBinding getYAnchor(Graph graph) {
		final Region graphic = graph.getGraphic(this);
		return graphic.layoutYProperty().add(graphic.heightProperty().divide(2));
	}

	default ReadOnlyDoubleProperty getWidth(Graph graph) {
		final Region graphic = graph.getGraphic(this);
		return graphic.widthProperty();
	}

	default ReadOnlyDoubleProperty getHeight(Graph graph) {
		final Region graphic = graph.getGraphic(this);
		return graphic.heightProperty();
	}

}
