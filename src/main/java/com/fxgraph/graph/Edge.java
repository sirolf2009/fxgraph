package com.fxgraph.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Edge extends Group {

	private final Cell source;
	private final Cell target;
	private final Line line;
	private final Text text;
	private final StringProperty textProperty;

	public Edge(Cell source, Cell target) {
		this.source = source;
		this.target = target;

		source.addCellChild(target);
		target.addCellParent(source);

		line = new Line();

		line.startXProperty().bind(source.layoutXProperty().add(source.widthProperty().divide(2)));
		line.startYProperty().bind(source.layoutYProperty().add(source.heightProperty().divide(2)));

		line.endXProperty().bind(target.layoutXProperty().add(target.widthProperty().divide(2)));
		line.endYProperty().bind(target.layoutYProperty().add(target.heightProperty().divide(2)));

		getChildren().add(line);

		final DoubleProperty textWidth = new SimpleDoubleProperty();
		final DoubleProperty textHeight = new SimpleDoubleProperty();
		text = new Text();
		text.getStyleClass().add("edge-text");
		text.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2).subtract(textWidth.divide(2)));
		text.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(textHeight.divide(2)));
		this.textProperty = text.textProperty();
		textProperty.addListener((obs, oldVal, newVal) -> {
			textWidth.set(text.getLayoutBounds().getWidth());
			textHeight.set(text.getLayoutBounds().getHeight());
		});
		getChildren().add(text);
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public Text getText() {
		return text;
	}

	public Cell getSource() {
		return source;
	}

	public Cell getTarget() {
		return target;
	}

	public Line getLine() {
		return line;
	}

}