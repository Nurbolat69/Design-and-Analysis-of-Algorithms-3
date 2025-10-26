package com.mst.algorithms;

import com.mst.graph.Edge;
import java.util.List;

/**
 * Result container for MST algorithm execution
 */
public class MSTResult {
    private final List<Edge> mstEdges;
    private final int totalCost;
    private final int operationsCount;
    private final double executionTimeMs;

    public MSTResult(List<Edge> mstEdges, int totalCost, int operationsCount, double executionTimeMs) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.operationsCount = operationsCount;
        this.executionTimeMs = executionTimeMs;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getOperationsCount() {
        return operationsCount;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MST Result:\n");
        sb.append("Total Cost: ").append(totalCost).append("\n");
        sb.append("Operations: ").append(operationsCount).append("\n");
        sb.append("Execution Time: ").append(String.format("%.2f", executionTimeMs)).append(" ms\n");
        sb.append("Edges:\n");
        for (Edge edge : mstEdges) {
            sb.append("  ").append(edge).append("\n");
        }
        return sb.toString();
    }
}