package com.fxgraph.edges;

import com.fxgraph.graph.Arrow;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

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

	public Point2D getIntercept(Point2D source, Point2D target, Region shape) {
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


		for (Point2D[] shapeLine : shapeLines) {
			Point2D intersect = calculateInterceptionPoint(source, target, shapeLine[0], shapeLine[1]);
			if (intersect != null) {
				return intersect;
			}
		}
		return null;
	}

	private static Point2D calculateInterceptionPoint(Point2D s1, Point2D s2, Point2D d1, Point2D d2) {

		double a1 = s2.getY() - s1.getY();
		double b1 = s1.getX() - s2.getX();
		double c1 = a1 * s1.getX() + b1 * s1.getY();

		double a2 = d2.getY() - d1.getY();
		double b2 = d1.getX() - d2.getX();
		double c2 = a2 * d1.getX() + b2 * d1.getY();

		double delta = a1 * b2 - a2 * b1;
		if (delta == 0) {
			return null;
		}
		return new Point2D(((b2 * c1 - b1 * c2) / delta), ((a1 * c2 - a2 * c1) / delta));

	}
}
