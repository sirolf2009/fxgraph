package com.fxgraph;

import com.fxgraph.graph.SequenceDiagram;
import com.fxgraph.graph.SequenceDiagram.ActorCell;
import com.fxgraph.graph.SequenceDiagram.IActorCell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SequenceDiagramDemo extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		SequenceDiagram diagram = new SequenceDiagram();

		// Add content to sequence diagram
		populateGraph(diagram);

		// Layout nodes (Use the no-args to do sequence diagram formatting)
		diagram.layout();

		// Configure interaction buttons and behavior
		diagram.getViewportGestures().setPanButton(MouseButton.SECONDARY);

		// Display the graph
		stage.setScene(new Scene(new BorderPane(diagram.getCanvas())));
		stage.show();
	}

	private void populateGraph(SequenceDiagram diagram) {
		diagram.beginUpdate();

		final IActorCell cellA = new ActorCell("Planning", 60.0);
		final IActorCell cellB = new ActorCell("Design", 260.0);
		final IActorCell cellC = new ActorCell("Implementation", 260.0);
		final IActorCell cellD = new ActorCell("Testing", 360.0);
		final IActorCell cellE = new ActorCell("Release", 360.0);

		diagram.addActor(cellA);
		diagram.addActor(cellB);
		diagram.addActor(cellC);
		diagram.addActor(cellD);
		diagram.addActor(cellE);

		diagram.addMessage(cellA, cellB, "Idea fleshed out");
		diagram.addMessage(cellB, cellC, "Design plans created");
		diagram.addMessage(cellC, cellD, "Logic implemented");
		diagram.addMessage(cellC, cellB, "New requirement");
		diagram.addMessage(cellE, cellB, "New requirement");
		diagram.addMessage(cellD, cellE, "All tests pass");
		diagram.addMessage(cellE, cellD, "User reports bug");

		diagram.endUpdate();
	}
}
