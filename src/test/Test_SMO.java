package test;

import algo.Data;
import algo.DataReader;
import algo.Vertex;
import java.io.File;
import smo.SpiderMonkeyOptimization;

public class Test_SMO {
    public static void main(String[] args) {
        // Dataset
        File file = new File("dataset/burma14.tsp");
        Vertex[] arrayVertex = DataReader.read(file);
        Data data = new Data(arrayVertex);

        // SMO-------------------------------------------------------------------
        int MAX_ITERATION = 100;
        int allowedMaximumGroup = 4;
        double perturbationRate = 0.5; // pr
        int localLeaderLimit = 10;
        int globalLeaderLimit = 10;
        int totalNumberOfSpiderMonkey = 1000;
        SpiderMonkeyOptimization smo = new SpiderMonkeyOptimization(data, MAX_ITERATION, allowedMaximumGroup, perturbationRate, localLeaderLimit, globalLeaderLimit, totalNumberOfSpiderMonkey);
        System.out.print("Best Individual SMO: ");
        System.out.println(smo.bestIndividual.toString());
        // END OF SMO------------------------------------------------------------
    }
}
