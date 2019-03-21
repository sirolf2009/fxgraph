package com.fxgraph.graph;

import java.util.Arrays;

import org.abego.treelayout.Configuration.Location;

import com.fxgraph.cells.RectangleCell;
import com.fxgraph.cells.TriangleCell;
import com.fxgraph.edges.CorneredEdge;
import com.fxgraph.edges.DoubleCorneredEdge;
import com.fxgraph.edges.Edge;
import com.fxgraph.graph.SequenceDiagram.ActorCell;
import com.fxgraph.layout.AbegoTreeLayout;
import com.fxgraph.layout.RandomLayout;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class MainApp extends Application {


	@Override
	public void start(Stage primaryStage) {
		final SplitPane root = new SplitPane();

		Graph graph = new Graph();
		addGraphComponents(graph);
		root.getItems().add(graph.getCanvas());

		Graph tree = new Graph();
		addTreeComponents(tree);
		root.getItems().add(tree.getCanvas());
		
		SequenceDiagram seqDiagram = new SequenceDiagram();
		addSequenceDiagramComponents(seqDiagram);
		root.getItems().add(seqDiagram.getCanvas());

		final Scene scene = new Scene(root, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void addGraphComponents(Graph graph) {
		final Model model = graph.getModel();
		graph.beginUpdate();

		final ICell cellA = new RectangleCell();
		final ICell cellB = new RectangleCell();
		final ICell cellC = new RectangleCell();
		final ICell cellD = new TriangleCell();
		final ICell cellE = new TriangleCell();
		final ICell cellF = new RectangleCell();
		final ICell cellG = new RectangleCell();

		model.addCell(cellA);
		model.addCell(cellB);
		model.addCell(cellC);
		model.addCell(cellD);
		model.addCell(cellE);
		model.addCell(cellF);
		model.addCell(cellG);

		model.addEdge(cellA, cellB);
		model.addEdge(cellA, cellC);
		model.addEdge(cellB, cellC);
		model.addEdge(cellC, cellD);
		model.addEdge(cellB, cellE);
		model.addEdge(cellD, cellF);

		final Edge edge = new Edge(cellD, cellG);
		edge.textProperty().set("Edges can have text too!");
		model.addEdge(edge);

		graph.endUpdate();
		graph.layout(new RandomLayout());
	}

	private void addTreeComponents(Graph graph) {
		final Model model = graph.getModel();
		graph.beginUpdate();

		final ICell cellA = new RectangleCell();
		final ICell cellB = new RectangleCell();
		final ICell cellC = new RectangleCell();
		final ICell cellD = new TriangleCell();
		final ICell cellE = new TriangleCell();
		final ICell cellF = new RectangleCell();
		final ICell cellG = new RectangleCell();

		model.addCell(cellA);
		model.addCell(cellB);
		model.addCell(cellC);
		model.addCell(cellD);
		model.addCell(cellE);
		model.addCell(cellF);
		model.addCell(cellG);

		final Edge edgeAB = new Edge(cellA, cellB);
		edgeAB.textProperty().set("Edges can have text too!");
		model.addEdge(edgeAB);
		final CorneredEdge edgeAC = new CorneredEdge(cellA, cellC, Orientation.HORIZONTAL);
		edgeAC.textProperty().set("Edges can have corners too!");
		model.addEdge(edgeAC);
		model.addEdge(cellB, cellD);
		final DoubleCorneredEdge edgeBE = new DoubleCorneredEdge(cellB, cellE, Orientation.HORIZONTAL);
		edgeBE.textProperty().set("You can implement custom edges and nodes too!");
		model.addEdge(edgeBE);
		model.addEdge(cellC, cellF);
		model.addEdge(cellC, cellG);

		graph.endUpdate();
		graph.layout(new AbegoTreeLayout(200, 200, Location.Top));
	}
	
	private void addSequenceDiagramComponents(SequenceDiagram seqDiagram) {
		ActorCell actorA = new ActorCell("Actor A", 400d);
		ActorCell actorB = new ActorCell("Actor B", 400d);
		ActorCell actorC = new ActorCell("Actor C", 400d);
		Arrays.asList(actorA, actorB, actorC).forEach(actor -> seqDiagram.addActor(actor));
		
		seqDiagram.addMessage(actorA, actorB, "checkEmail", 1);
		seqDiagram.addMessage(actorB, actorC, "readSavedUser", 2);
		seqDiagram.addMessage(actorC, actorB, "savedUser", 3);
		seqDiagram.addMessage(actorB, actorA, "noNewEmails", 4);
		
		seqDiagram.layout();
	}

	public static void main(String[] args) {
		launch(args);
	}
}