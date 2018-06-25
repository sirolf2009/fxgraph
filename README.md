# FXGraph

FXGraph is javafx graph visualizer. It's a continuation of a post I found on stackoverflow. I will be probably expand on it some more, but pull requests are appreciated.
You can find the original question here: https://stackoverflow.com/questions/30679025/graph-visualisation-like-yfiles-in-javafx

## Screenshot
![Screenshot](screenshot.png)

## Download
You can get it from maven central
```xml
<dependency>
	<groupId>com.sirolf2009</groupId>
	<artifactId>fxgraph</artifactId>
	<version>0.0.1</version>
</dependency>
``` 
Note: the current version 0.0.2-SNAPSHOT, is not backwards compatible with 0.0.1.

## Usage
You can look at ```com.fxgraph.graph.MainApp``` for a basic example. The relevant code is as follows
```java
Graph graph = new Graph();
Model model = graph.getModel();

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
``` 

## TODO
 - better support for custom cells/edges
 - easy to serialize data structure
 - unit tests
 - better layouts