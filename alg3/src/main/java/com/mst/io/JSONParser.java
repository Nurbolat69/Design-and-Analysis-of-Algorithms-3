package com.mst.io;

import com.mst.graph.Graph;
import java.io.*;
import java.util.*;

/**
 * Simple JSON parser without external dependencies
 */
public class JSONParser {

    /**
     * Parse graphs from input JSON file
     */
    public static List<GraphData> parseInputFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }
        }

        return parseGraphs(content.toString());
    }

    private static List<GraphData> parseGraphs(String json) {
        List<GraphData> graphDataList = new ArrayList<>();

        // Find "graphs" array
        int graphsStart = json.indexOf("\"graphs\"");
        if (graphsStart == -1) return graphDataList;

        int arrayStart = json.indexOf('[', graphsStart);
        int arrayEnd = findMatchingBracket(json, arrayStart);

        String graphsArray = json.substring(arrayStart + 1, arrayEnd);

        // Parse each graph object
        List<String> graphObjects = splitGraphObjects(graphsArray);

        for (String graphObj : graphObjects) {
            GraphData graphData = parseGraphObject(graphObj);
            if (graphData != null) {
                graphDataList.add(graphData);
            }
        }

        return graphDataList;
    }

    private static GraphData parseGraphObject(String graphObj) {
        try {
            // Parse id
            int id = parseIntValue(graphObj, "id");

            // Parse nodes
            List<String> nodes = parseStringArray(graphObj, "nodes");

            // Parse edges
            List<EdgeData> edges = parseEdgesArray(graphObj);

            // Build graph
            Graph graph = new Graph();

            for (String node : nodes) {
                graph.addVertex(node);
            }

            for (EdgeData edge : edges) {
                graph.addEdge(edge.from, edge.to, edge.weight);
            }

            return new GraphData(id, graph);

        } catch (Exception e) {
            System.err.println("Error parsing graph: " + e.getMessage());
            return null;
        }
    }

    private static int parseIntValue(String json, String key) {
        String search = "\"" + key + "\"";
        int keyPos = json.indexOf(search);
        if (keyPos == -1) return 0;

        int colonPos = json.indexOf(':', keyPos);
        int commaPos = json.indexOf(',', colonPos);
        int bracePos = json.indexOf('}', colonPos);

        int endPos = (commaPos != -1 && commaPos < bracePos) ? commaPos : bracePos;

        String value = json.substring(colonPos + 1, endPos).trim();
        return Integer.parseInt(value);
    }

    private static String parseStringValue(String json, String key) {
        String search = "\"" + key + "\"";
        int keyPos = json.indexOf(search);
        if (keyPos == -1) return "";

        int colonPos = json.indexOf(':', keyPos);
        int quoteStart = json.indexOf('"', colonPos);
        int quoteEnd = json.indexOf('"', quoteStart + 1);

        return json.substring(quoteStart + 1, quoteEnd);
    }

    private static List<String> parseStringArray(String json, String key) {
        List<String> result = new ArrayList<>();

        String search = "\"" + key + "\"";
        int keyPos = json.indexOf(search);
        if (keyPos == -1) return result;

        int arrayStart = json.indexOf('[', keyPos);
        int arrayEnd = findMatchingBracket(json, arrayStart);

        String arrayContent = json.substring(arrayStart + 1, arrayEnd);

        int pos = 0;
        while (pos < arrayContent.length()) {
            int quoteStart = arrayContent.indexOf('"', pos);
            if (quoteStart == -1) break;

            int quoteEnd = arrayContent.indexOf('"', quoteStart + 1);
            if (quoteEnd == -1) break;

            String value = arrayContent.substring(quoteStart + 1, quoteEnd);
            result.add(value);

            pos = quoteEnd + 1;
        }

        return result;
    }

    private static List<EdgeData> parseEdgesArray(String json) {
        List<EdgeData> edges = new ArrayList<>();

        int edgesStart = json.indexOf("\"edges\"");
        if (edgesStart == -1) return edges;

        int arrayStart = json.indexOf('[', edgesStart);
        int arrayEnd = findMatchingBracket(json, arrayStart);

        String arrayContent = json.substring(arrayStart + 1, arrayEnd);

        List<String> edgeObjects = splitEdgeObjects(arrayContent);

        for (String edgeObj : edgeObjects) {
            String from = parseStringValue(edgeObj, "from");
            String to = parseStringValue(edgeObj, "to");
            int weight = parseIntValue(edgeObj, "weight");

            edges.add(new EdgeData(from, to, weight));
        }

        return edges;
    }

    private static List<String> splitGraphObjects(String array) {
        List<String> objects = new ArrayList<>();

        int depth = 0;
        int start = 0;

        for (int i = 0; i < array.length(); i++) {
            char c = array.charAt(i);

            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    objects.add(array.substring(start, i + 1));
                }
            }
        }

        return objects;
    }

    private static List<String> splitEdgeObjects(String array) {
        return splitGraphObjects(array);
    }

    private static int findMatchingBracket(String json, int start) {
        if (start >= json.length()) return -1;

        char openBracket = json.charAt(start);
        char closeBracket = (openBracket == '[') ? ']' : '}';

        int depth = 1;

        for (int i = start + 1; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == openBracket) {
                depth++;
            } else if (c == closeBracket) {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Container for edge data
     */
    private static class EdgeData {
        String from;
        String to;
        int weight;

        EdgeData(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
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