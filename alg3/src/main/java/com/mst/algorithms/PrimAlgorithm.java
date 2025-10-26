package com.mst.algorithms;

import com.mst.graph.Edge;
import com.mst.graph.Graph;
import java.util.*;

/**
 * Implementation of Prim's algorithm for finding Minimum Spanning Tree
 */
public class PrimAlgorithm {
    private int operationsCount;

    public MSTResult findMST(Graph graph) {
        operationsCount = 0;
        long startTime = System.nanoTime();

        Set<String> vertices = graph.getVertices();

        if (vertices.isEmpty() || !graph.isConnected()) {
            return new MSTResult(new ArrayList<>(), 0, operationsCount, 0);
        }

        List<Edge> mstEdges = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        // Start with the first vertex
        String start = vertices.iterator().next();
        visited.add(start);
        pq.addAll(graph.getAdjacentEdges(start));
        operationsCount++;

        int totalCost = 0;

        // Build MST
        while (!pq.isEmpty() && visited.size() < vertices.size()) {
            Edge edge = pq.poll();
            operationsCount++;

            String to = edge.getTo();

            // Skip if already visited
            if (visited.contains(to)) {
                operationsCount++;
                continue;
            }

            // Add edge to MST
            mstEdges.add(edge);
            totalCost += edge.getWeight();
            visited.add(to);
            operationsCount += 3;

            // Add all edges from the new vertex
            for (Edge adjacentEdge : graph.getAdjacentEdges(to)) {
                operationsCount++;
                if (!visited.contains(adjacentEdge.getTo())) {
                    pq.offer(adjacentEdge);
                    operationsCount++;
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }

    public int getOperationsCount() {
        return operationsCount;
    }
}