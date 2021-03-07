package com.fxgraph.edges;

import com.fxgraph.graph.Arrow;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractEdgeGraphic extends Pane {
	protected final Group group = new Group();
	protected final Text text = new Text();
	protected final Arrow arrow = new Arrow();

	public Group getGroup() {
		return group;
	}

	public Text getText() {
		return text;
	}

	public Arrow getArrow() {
		return arrow;
	}

	protected void setupArrow(Region target, DoubleBinding sourceX, DoubleBinding sourceY, DoubleBinding targetX, DoubleBinding targetY) {
		// Fallback point, should not be necessary for normal shapes
		final Point2D fallback = new Point2D(targetX.get(), targetY.get());
		// Calculate where the arrow should be drawn based on the intersection with the shape.
		final DoubleBinding targetXFitted = Bindings.createDoubleBinding(() -> getIntercept(
				new Point2D(sourceX.get(), sourceY.get()),
				new Point2D(targetX.get(), targetY.get()),
				target).orElse(fallback).getX(),
				// dependency properties
				targetX, targetY, sourceX, sourceY);
		final DoubleBinding targetYFitted = Bindings.createDoubleBinding(() -> getIntercept(
				new Point2D(sourceX.get(), sourceY.get()),
				new Point2D(targetX.get(), targetY.get()),
				target).orElse(fallback).getY(),
				// dependency properties
				targetX, targetY, sourceX, sourceY);
		// Set position bindings
		arrow.startXProperty().bind(sourceX);
		arrow.startYProperty().bind(sourceY);
		arrow.endXProperty().bind(targetXFitted);
		arrow.endYProperty().bind(targetYFitted);
		// Add style
		arrow.getStyleClass().add("arrow");
	}

	protected Optional<Point2D> getIntercept(Point2D source, Point2D target, Region shape) {
		// Create the lines that represent the shape (Currently rectangle-only, also see below for rectangle-assumed logic)
		Point2D shapeNW = new Point2D(shape.getLayoutX(), shape.getLayoutY());
		Point2D shapeNE = new Point2D(shape.getLayoutX() + shape.getWidth(), shape.getLayoutY());
		Point2D shapeSW = new Point2D(shape.getLayoutX(), shape.getLayoutY() + shape.getHeight());
		Point2D shapeSE = new Point2D(shape.getLayoutX() + shape.getWidth(), shape.getLayoutY() + shape.getHeight());
		Point2D[][] shapeLines = new Point2D[][] {
				{ shapeNW, shapeNE },
				{ shapeNE, shapeSE },
				{ shapeSE, shapeSW },
				{ shapeSW, shapeNW },
		};

		// Collect the intersections with the shape and sort based on distance from the source.
		SortedSet<Point2D> intersections = new TreeSet<>((p1, p2) -> {
			double p1Dist= p1.distance(source);
			double p2Dist= p2.distance(source);
			if (p1Dist > p2Dist) {
				return 1;
			} else if (p1Dist == p2Dist) {
				return 0;
			} else {
				return -1;
			}
		});

		// For each line creating the shape check its intersection points.
		// Discard any intersection that is not within the shape's bounds (assumed rectangle).
		for (Point2D[] shapeLine : shapeLines) {
			Point2D intersect = calculateInterceptionPoint(source, target, shapeLine[0], shapeLine[1]);
			if (intersect != null &&
					intersect.getX() >= shape.getLayoutX() &&
					intersect.getX() <= shape.getLayoutX() + shape.getWidth() &&
					intersect.getY() >= shape.getLayoutY() &&
					intersect.getY() <= shape.getLayoutY() + shape.getHeight()) {
				intersections.add(intersect);
			}
		}
		if (intersections.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(intersections.first());
	}

	private static Point2D calculateInterceptionPoint(Point2D line1Start, Point2D line1End,
													  Point2D line2Start, Point2D line2End) {
		double xDiff1 = line1Start.getX() - line1End.getX();
		double yDiff1 = line1End.getY() - line1Start.getY();
		double mod1 =
				yDiff1 * line1Start.getX() +
				xDiff1 * line1Start.getY();

		double xDiff2 = line2Start.getX() - line2End.getX();
		double yDiff2 = line2End.getY() - line2Start.getY();
		double mod2 =
				yDiff2 * line2Start.getX() +
				xDiff2 * line2Start.getY();

		double delta = yDiff1 * xDiff2 - yDiff2 * xDiff1;
		if (delta == 0) {
			return null;
		}
		return new Point2D(
				((xDiff2 * mod1 - xDiff1 * mod2) / delta),
				((yDiff1 * mod2 - yDiff2 * mod1) / delta));
	}
}
