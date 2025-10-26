package com.mst.io;

import com.google.gson.*;
import com.mst.graph.Graph;
import java.io.*;
import java.util.*;

/**
 * Parser for reading graph data from JSON files
 */
public class JSONParser {

    /**
     * Parse graphs from input JSON file
     */
    public static List<GraphData> parseInputFile(String filename) throws IOException {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(filename)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray graphsArray = jsonObject.getAsJsonArray("graphs");

            List<GraphData> graphDataList = new ArrayList<>();

            for (JsonElement graphElement : graphsArray) {
                JsonObject graphObj = graphElement.getAsJsonObject();

                int id = graphObj.get("id").getAsInt();
                JsonArray nodesArray = graphObj.getAsJsonArray("nodes");
                JsonArray edgesArray = graphObj.getAsJsonArray("edges");

                Graph graph = new Graph();

                // Add vertices
                for (JsonElement nodeElement : nodesArray) {
                    graph.addVertex(nodeElement.getAsString());
                }

                // Add edges
                for (JsonElement edgeElement : edgesArray) {
                    JsonObject edgeObj = edgeElement.getAsJsonObject();
                    String from = edgeObj.get("from").getAsString();
                    String to = edgeObj.get("to").getAsString();
                    int weight = edgeObj.get("weight").getAsInt();

                    graph.addEdge(from, to, weight);
                }

                graphDataList.add(new GraphData(id, graph));
            }

            return graphDataList;
        }
    }

    /**
     * Container for graph data with ID
     */
    public static class GraphData {
        private final int id;
        private final Graph graph;

        public GraphData(int id, Graph graph) {
            this.id = id;
            this.graph = graph;
        }

        public int getId() {
            return id;
        }

        public Graph getGraph() {
            return graph;
        }
    }
}