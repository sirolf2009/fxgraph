package com.fxgraph;

import com.fxgraph.cells.RectangleCell;
import com.fxgraph.edges.CorneredEdge;
import com.fxgraph.edges.CorneredLoopEdge;
import com.fxgraph.edges.CorneredLoopEdge.Position;
import com.fxgraph.edges.DoubleCorneredEdge;
import com.fxgraph.edges.Edge;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;
import com.fxgraph.graph.Model;
import com.fxgraph.layout.AbegoTreeLayout;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.abego.treelayout.Configuration.Location;

public class BasicGraphDemo extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Graph graph = new Graph();

		// Add content to graph
		populateGraph(graph);

		// Layout nodes
		AbegoTreeLayout layout = new AbegoTreeLayout(200, 200, Location.Top);
		graph.layout(layout);

		// Configure interaction buttons and behavior
		graph.getViewportGestures().setPanButton(MouseButton.SECONDARY);
		graph.getNodeGestures().setDragButton(MouseButton.PRIMARY);

		// Display the graph
		stage.setScene(new Scene(new BorderPane(graph.getCanvas())));
		stage.show();
	}

	private void populateGraph(Graph graph) {
		final Model model = graph.getModel();
		graph.beginUpdate();
		final ICell cellA = new RectangleCell();
		final ICell cellB = new RectangleCell();
		final ICell cellC = new RectangleCell();
		final ICell cellD = new RectangleCell();
		final ICell cellE = new RectangleCell();
		final ICell cellF = new RectangleCell();
		final ICell cellG = new RectangleCell();

		model.addCell(cellA);
		model.addCell(cellB);
		model.addCell(cellC);
		model.addCell(cellD);
		model.addCell(cellE);
		model.addCell(cellF);
		model.addCell(cellG);

		final Edge edgeAB = new Edge(cellA, cellB, true);
		edgeAB.textProperty().set("Directed Edge");
		model.addEdge(edgeAB);

		final CorneredEdge edgeAC = new CorneredEdge(cellA, cellC, true, Orientation.HORIZONTAL);
		edgeAC.textProperty().set("Directed CorneredEdge");
		model.addEdge(edgeAC);

		final DoubleCorneredEdge edgeBE = new DoubleCorneredEdge(cellB, cellE, true, Orientation.HORIZONTAL);
		edgeBE.textProperty().set("Directed DoubleCorneredEdge");
		model.addEdge(edgeBE);

		final Edge edgeCF = new Edge(cellC, cellF, true);
		edgeCF.textProperty().set("Directed Edge");
		model.addEdge(edgeCF);

		final CorneredLoopEdge loopFTop = new CorneredLoopEdge(cellF, Position.TOP);
		loopFTop.textProperty().set("Loop top");
		model.addEdge(loopFTop);

		model.addEdge(cellC, cellG);

		model.addEdge(cellB, cellD);

		graph.endUpdate();
	}
}
