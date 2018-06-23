package com.fxgraph.graph;

import com.fxgraph.cells.RectangleCell;
import com.fxgraph.cells.TriangleCell;
import com.fxgraph.layout.RandomLayout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	Graph graph = new Graph();

	@Override
	public void start(Stage primaryStage) {
		final BorderPane root = new BorderPane();

		graph = new Graph();

		root.setCenter(graph.getScrollPane());

		final Scene scene = new Scene(root, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();

		addGraphComponents();

		graph.layout(new RandomLayout());
	}

	private void addGraphComponents() {

		final Model model = graph.getModel();

		graph.beginUpdate();

		final Cell cellA = new RectangleCell();
		final Cell cellB = new RectangleCell();
		final Cell cellC = new RectangleCell();
		final Cell cellD = new TriangleCell();
		final Cell cellE = new TriangleCell();
		final Cell cellF = new RectangleCell();
		final Cell cellG = new RectangleCell();

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
		model.addEdge(cellD, cellG);

		graph.endUpdate();

	}

	public static void main(String[] args) {
		launch(args);
	}
}