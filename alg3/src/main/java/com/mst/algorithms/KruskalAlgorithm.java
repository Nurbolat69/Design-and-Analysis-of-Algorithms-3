package com.mst.algorithms;

import com.mst.graph.Edge;
import com.mst.graph.Graph;
import java.util.*;

/**
 * Implementation of Kruskal's algorithm for finding Minimum Spanning Tree
 */
public class KruskalAlgorithm {
    private int operationsCount;

    public MSTResult findMST(Graph graph) {
        operationsCount = 0;
        long startTime = System.nanoTime();

        Set<String> vertices = graph.getVertices();

        if (vertices.isEmpty() || !graph.isConnected()) {
            return new MSTResult(new ArrayList<>(), 0, operationsCount, 0);
        }

        List<Edge> mstEdges = new ArrayList<>();
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        Collections.sort(sortedEdges);
        operationsCount++;

        // Initialize Union-Find structure
        UnionFind uf = new UnionFind(vertices);

        int totalCost = 0;

        // Process edges in sorted order
        for (Edge edge : sortedEdges) {
            operationsCount++;

            String from = edge.getFrom();
            String to = edge.getTo();

            // Check if adding this edge creates a cycle
            if (!uf.connected(from, to)) {
                operationsCount++;
                uf.union(from, to);
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                operationsCount += 2;

                // Stop if we have enough edges
                if (mstEdges.size() == vertices.size() - 1) {
                    break;
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        operationsCount += uf.getOperationsCount();

        return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }

    public int getOperationsCount() {
        return operationsCount;
    }

    /**
     * Union-Find (Disjoint Set) data structure with path compression
     */
    private static class UnionFind {
        private final Map<String, String> parent;
        private final Map<String, Integer> rank;
        private int operationsCount;

        public UnionFind(Set<String> vertices) {
            parent = new HashMap<>();
            rank = new HashMap<>();
            operationsCount = 0;

            for (String v : vertices) {
                parent.put(v, v);
                rank.put(v, 0);
            }
        }

        public String find(String vertex) {
            operationsCount++;
            if (!parent.get(vertex).equals(vertex)) {
                parent.put(vertex, find(parent.get(vertex))); // Path compression
                operationsCount++;
            }
            return parent.get(vertex);
        }

        public boolean connected(String v1, String v2) {
            operationsCount++;
            return find(v1).equals(find(v2));
        }

        public void union(String v1, String v2) {
            String root1 = find(v1);
            String root2 = find(v2);
            operationsCount++;

            if (root1.equals(root2)) {
                return;
            }

            // Union by rank
            int rank1 = rank.get(root1);
            int rank2 = rank.get(root2);
            operationsCount++;

            if (rank1 < rank2) {
                parent.put(root1, root2);
            } else if (rank1 > rank2) {
                parent.put(root2, root1);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank1 + 1);
            }
            operationsCount++;
        }

        public int getOperationsCount() {
            return operationsCount;
        }
    }
}