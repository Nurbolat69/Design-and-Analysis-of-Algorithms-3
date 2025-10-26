package com.mst.io;

import com.google.gson.*;
import com.mst.algorithms.MSTResult;
import com.mst.graph.Edge;

import java.io.*;
import java.util.*;

/**
 * Writer for saving MST results to JSON files
 */
public class JSONWriter {

    /**
     * Write MST results to output JSON file
     */
    public static void writeResults(String filename, List<ResultData> results) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject root = new JsonObject();
        JsonArray resultsArray = new JsonArray();

        for (ResultData result : results) {
            JsonObject resultObj = new JsonObject();

            resultObj.addProperty("graph_id", result.graphId);

            // Input stats
            JsonObject inputStats = new JsonObject();
            inputStats.addProperty("vertices", result.vertexCount);
            inputStats.addProperty("edges", result.edgeCount);
            resultObj.add("input_stats", inputStats);

            // Prim's results
            resultObj.add("prim", createAlgorithmResult(result.primResult));

            // Kruskal's results
            resultObj.add("kruskal", createAlgorithmResult(result.kruskalResult));

            resultsArray.add(resultObj);
        }

        root.add("results", resultsArray);

        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(root, writer);
        }
    }

    private static JsonObject createAlgorithmResult(MSTResult result) {
        JsonObject algResult = new JsonObject();

        JsonArray edgesArray = new JsonArray();
        for (Edge edge : result.getMstEdges()) {
            JsonObject edgeObj = new JsonObject();
            edgeObj.addProperty("from", edge.getFrom());
            edgeObj.addProperty("to", edge.getTo());
            edgeObj.addProperty("weight", edge.getWeight());
            edgesArray.add(edgeObj);
        }

        algResult.add("mst_edges", edgesArray);
        algResult.addProperty("total_cost", result.getTotalCost());
        algResult.addProperty("operations_count", result.getOperationsCount());
        algResult.addProperty("execution_time_ms",
                Math.round(result.getExecutionTimeMs() * 100.0) / 100.0);

        return algResult;
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