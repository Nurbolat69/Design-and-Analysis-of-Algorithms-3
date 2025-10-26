package com.mst.io;

import com.mst.algorithms.MSTResult;
import com.mst.graph.Edge;
import java.io.*;
import java.util.*;

/**
 * Simple JSON writer without external dependencies
 */
public class JSONWriter {

    /**
     * Write MST results to output JSON file
     */
    public static void writeResults(String filename, List<ResultData> results) throws IOException {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"results\": [\n");

        for (int i = 0; i < results.size(); i++) {
            ResultData result = results.get(i);

            json.append("    {\n");
            json.append("      \"graph_id\": ").append(result.graphId).append(",\n");

            // Input stats
            json.append("      \"input_stats\": {\n");
            json.append("        \"vertices\": ").append(result.vertexCount).append(",\n");
            json.append("        \"edges\": ").append(result.edgeCount).append("\n");
            json.append("      },\n");

            // Prim's results
            json.append("      \"prim\": ");
            json.append(formatAlgorithmResult(result.primResult));
            json.append(",\n");

            // Kruskal's results
            json.append("      \"kruskal\": ");
            json.append(formatAlgorithmResult(result.kruskalResult));
            json.append("\n");

            json.append("    }");

            if (i < results.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(json.toString());
        }
    }

    private static String formatAlgorithmResult(MSTResult result) {
        StringBuilder json = new StringBuilder();

        json.append("{\n");

        // MST edges
        json.append("        \"mst_edges\": [\n");
        List<Edge> edges = result.getMstEdges();

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            json.append("          {");
            json.append("\"from\": \"").append(escapeString(edge.getFrom())).append("\", ");
            json.append("\"to\": \"").append(escapeString(edge.getTo())).append("\", ");
            json.append("\"weight\": ").append(edge.getWeight());
            json.append("}");

            if (i < edges.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("        ],\n");

        // Total cost
        json.append("        \"total_cost\": ").append(result.getTotalCost()).append(",\n");

        // Operations count
        json.append("        \"operations_count\": ").append(result.getOperationsCount()).append(",\n");

        // Execution time
        json.append("        \"execution_time_ms\": ");
        json.append(String.format("%.2f", result.getExecutionTimeMs()));
        json.append("\n");

        json.append("      }");

        return json.toString();
    }

    private static String escapeString(String str) {
        if (str == null) return "";

        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Container for result data
     */
    public static class ResultData {
        private final int graphId;
        private final int vertexCount;
        private final int edgeCount;
        private final MSTResult primResult;
        private final MSTResult kruskalResult;

        public ResultData(int graphId, int vertexCount, int edgeCount,
                          MSTResult primResult, MSTResult kruskalResult) {
            this.graphId = graphId;
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
            this.primResult = primResult;
            this.kruskalResult = kruskalResult;
        }
    }
}