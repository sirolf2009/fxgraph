package com.fxgraph.graph;

import javafx.scene.layout.Region;

public interface IGraphNode {

	public Region getGraphic(Graph graph);
	default void onAddedToGraph(Graph graph, Region region) {}
	default void onRemovedFromGraph(Graph graph, Region region) {}

}
