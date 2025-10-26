package com.mst;

import com.mst.algorithms.KruskalAlgorithm;
import com.mst.algorithms.MSTResult;
import com.mst.algorithms.PrimAlgorithm;
import com.mst.io.JSONParser;
import com.mst.io.JSONWriter;
import com.mst.graph.Graph;

import java.util.*;

/**
 * Main application for MST computation
 */
public class MSTApplication {

    public static void main(String[] args) {
        try {
            String inputFile = args.length > 0 ? args[0] : "src/main/resources/input.json";
            String outputFile = args.length > 1 ? args[1] : "src/main/resources/output.json";

            System.out.println("Reading input from: " + inputFile);
            List<JSONParser.GraphData> graphDataList = JSONParser.parseInputFile(inputFile);

            List<JSONWriter.ResultData> results = new ArrayList<>();

            for (JSONParser.GraphData graphData : graphDataList) {
                System.out.println("\n=== Processing Graph " + graphData.getId() + " ===");

                Graph graph = graphData.getGraph();
                System.out.println("Vertices: " + graph.getVertexCount());
                System.out.println("Edges: " + graph.getEdgeCount());

                // Check connectivity
                if (!graph.isConnected()) {
                    System.out.println("WARNING: Graph is not connected!");
                    continue;
                }

                // Run Prim's algorithm
                System.out.println("\nRunning Prim's Algorithm...");
                PrimAlgorithm prim = new PrimAlgorithm();
                MSTResult primResult = prim.findMST(graph);
                System.out.println(primResult);

                // Run Kruskal's algorithm
                System.out.println("Running Kruskal's Algorithm...");
                KruskalAlgorithm kruskal = new KruskalAlgorithm();
                MSTResult kruskalResult = kruskal.findMST(graph);
                System.out.println(kruskalResult);

                // Verify results match
                if (primResult.getTotalCost() == kruskalResult.getTotalCost()) {
                    System.out.println("✓ Both algorithms produced same total cost: " +
                            primResult.getTotalCost());
                } else {
                    System.out.println("✗ ERROR: Algorithms produced different costs!");
                }

                // Store results
                results.add(new JSONWriter.ResultData(
                        graphData.getId(),
                        graph.getVertexCount(),
                        graph.getEdgeCount(),
                        primResult,
                        kruskalResult
                ));
            }

            // Write results to file
            System.out.println("\nWriting results to: " + outputFile);
            JSONWriter.writeResults(outputFile, results);
            System.out.println("Done!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}