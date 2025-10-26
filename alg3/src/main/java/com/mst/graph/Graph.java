package com.mst.graph;

import java.util.*;

/**
 * Graph data structure for representing undirected weighted graphs
 */
public class Graph {
    private final Map<String, List<Edge>> adjacencyList;
    private final Set<String> vertices;
    private final List<Edge> edges;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.vertices = new HashSet<>();
        this.edges = new ArrayList<>();
    }

    /**
     * Add a vertex to the graph
     */
    public void addVertex(String vertex) {
        vertices.add(vertex);
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Add an undirected edge to the graph
     */
    public void addEdge(String from, String to, int weight) {
        addVertex(from);
        addVertex(to);

        Edge edge = new Edge(from, to, weight);
        edges.add(edge);

        adjacencyList.get(from).add(edge);
        adjacencyList.get(to).add(new Edge(to, from, weight));
    }

    /**
     * Get all vertices in the graph
     */
    public Set<String> getVertices() {
        return new HashSet<>(vertices);
    }

    /**
     * Get all edges in the graph
     */
    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    /**
     * Get adjacent edges for a vertex
     */
    public List<Edge> getAdjacentEdges(String vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    /**
     * Get the number of vertices
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * Get the number of edges
     */
    public int getEdgeCount() {
        return edges.size();
    }

    /**
     * Check if the graph is connected
     */
    public boolean isConnected() {
        if (vertices.isEmpty()) return true;

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        String start = vertices.iterator().next();
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            for (Edge edge : getAdjacentEdges(current)) {
                String neighbor = edge.getTo();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return visited.size() == vertices.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices.size()).append(" vertices and ")
                .append(edges.size()).append(" edges:\n");

        for (Edge edge : edges) {
            sb.append(edge).append("\n");
        }

        return sb.toString();
    }
}