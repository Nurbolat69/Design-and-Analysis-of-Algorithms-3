package com.mst;

import com.mst.algorithms.*;
import com.mst.graph.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Comprehensive test suite for MST algorithms
 */
public class MSTAlgorithmTest {

    private Graph createSimpleGraph() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 3);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 7);
        graph.addEdge("C", "E", 8);
        graph.addEdge("D", "E", 6);
        return graph;
    }

    @Test
    @DisplayName("Both algorithms produce same total cost")
    public void testSameTotalCost() {
        Graph graph = createSimpleGraph();

        PrimAlgorithm prim = new PrimAlgorithm();
        KruskalAlgorithm kruskal = new KruskalAlgorithm();

        MSTResult primResult = prim.findMST(graph);
        MSTResult kruskalResult = kruskal.findMST(graph);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(),
                "Both algorithms should produce same total cost");
    }

    @Test
    @DisplayName("MST has V-1 edges")
    public void testCorrectEdgeCount() {
        Graph graph = createSimpleGraph();

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        int expectedEdges = graph.getVertexCount() - 1;
        assertEquals(expectedEdges, result.getMstEdges().size(),
                "MST should have V-1 edges");
    }

    @Test
    @DisplayName("MST is acyclic")
    public void testMSTIsAcyclic() {
        Graph graph = createSimpleGraph();

        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        MSTResult result = kruskal.findMST(graph);

        // Build graph from MST edges and check for cycles
        Graph mstGraph = new Graph();
        for (Edge edge : result.getMstEdges()) {
            mstGraph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
        }

        assertTrue(isAcyclic(mstGraph), "MST should be acyclic");
    }

    @Test
    @DisplayName("MST connects all vertices")
    public void testMSTConnectsAllVertices() {
        Graph graph = createSimpleGraph();

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        // Build graph from MST edges
        Graph mstGraph = new Graph();
        for (Edge edge : result.getMstEdges()) {
            mstGraph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
        }

        assertTrue(mstGraph.isConnected(), "MST should connect all vertices");
    }

    @Test
    @DisplayName("Empty graph handling")
    public void testEmptyGraph() {
        Graph graph = new Graph();

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        assertEquals(0, result.getTotalCost());
        assertEquals(0, result.getMstEdges().size());
    }

    @Test
    @DisplayName("Disconnected graph handling")
    public void testDisconnectedGraph() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 1);
        graph.addEdge("C", "D", 2);

        assertFalse(graph.isConnected(), "Graph should be disconnected");

        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        MSTResult result = kruskal.findMST(graph);

        assertEquals(0, result.getTotalCost());
    }

    @Test
    @DisplayName("Execution time is non-negative")
    public void testExecutionTimeNonNegative() {
        Graph graph = createSimpleGraph();

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        assertTrue(result.getExecutionTimeMs() >= 0,
                "Execution time should be non-negative");
    }

    @Test
    @DisplayName("Operations count is positive")
    public void testOperationsCountPositive() {
        Graph graph = createSimpleGraph();

        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        MSTResult result = kruskal.findMST(graph);

        assertTrue(result.getOperationsCount() > 0,
                "Operations count should be positive");
    }

    @Test
    @DisplayName("Single edge graph")
    public void testSingleEdgeGraph() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 5);

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        assertEquals(5, result.getTotalCost());
        assertEquals(1, result.getMstEdges().size());
    }

    @Test
    @DisplayName("Large graph performance")
    public void testLargeGraphPerformance() {
        // Create a larger graph
        Graph graph = new Graph();
        int n = 20;

        // Create a complete graph
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                String from = "V" + i;
                String to = "V" + j;
                int weight = (i + j) % 10 + 1;
                graph.addEdge(from, to, weight);
            }
        }

        PrimAlgorithm prim = new PrimAlgorithm();
        KruskalAlgorithm kruskal = new KruskalAlgorithm();

        MSTResult primResult = prim.findMST(graph);
        MSTResult kruskalResult = kruskal.findMST(graph);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
        assertTrue(primResult.getExecutionTimeMs() < 1000,
                "Prim's should complete in reasonable time");
        assertTrue(kruskalResult.getExecutionTimeMs() < 1000,
                "Kruskal's should complete in reasonable time");
    }

    // Helper method to check if graph is acyclic
    private boolean isAcyclic(Graph graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                if (hasCycleDFS(graph, vertex, null, visited, recursionStack)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasCycleDFS(Graph graph, String current, String parent,
                                Set<String> visited, Set<String> recStack) {
        visited.add(current);
        recStack.add(current);

        for (Edge edge : graph.getAdjacentEdges(current)) {
            String neighbor = edge.getTo();

            if (!visited.contains(neighbor)) {
                if (hasCycleDFS(graph, neighbor, current, visited, recStack)) {
                    return true;
                }
            } else if (!neighbor.equals(parent) && recStack.contains(neighbor)) {
                return true;
            }
        }

        recStack.remove(current);
        return false;
    }
}