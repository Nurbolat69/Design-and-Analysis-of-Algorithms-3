package com.mst.graph;

import java.util.Objects;

/**
 * Represents a weighted edge in an undirected graph
 */
public class Edge implements Comparable<Edge> {
    private final String from;
    private final String to;
    private final int weight;

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Compare edges by weight (for sorting)
     */
    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight &&
                ((from.equals(edge.from) && to.equals(edge.to)) ||
                        (from.equals(edge.to) && to.equals(edge.from)));
    }

    @Override
    public int hashCode() {
        // For undirected edges, hash should be same for (A,B) and (B,A)
        return Objects.hash(Math.min(from.hashCode(), to.hashCode()),
                Math.max(from.hashCode(), to.hashCode()),
                weight);
    }

    @Override
    public String toString() {
        return from + " -- " + to + " [" + weight + "]";
    }
}