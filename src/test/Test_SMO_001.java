package test;

import algo.Data;
import algo.DataReader;
import algo.Vertex;
import java.io.File;
import smo.SpiderMonkeyOptimization;

public class Test_SMO_001 {

    public static void main(String[] args) {
        // Dataset
        File file = new File("dataset/burma14.tsp");
        Vertex[] arrayVertex = DataReader.read(file);
        Data data = new Data(arrayVertex);

        int numTest = 10;

        double SumTourCostSMO = 0;

        double AverageTourCostSMO = -1;

        double MinTourCostGA = Double.MAX_VALUE;
        double MinTourCostSMO = Double.MAX_VALUE;

        // Perform testing for each algorithm (GA and SMO) numTest times
        for (int test = 1; test <= numTest; test++) {
            System.out.println("Test_" + test + "------------------------------------");

            // SMO-------------------------------------------------------------------
            int MAX_ITERATION = 100; // iteration
            int allowedMaximumGroup = 4;
            double perturbationRate = 0.5; // pr
            int localLeaderLimit = 10;
            int globalLeaderLimit = 10;
            int totalNumberOfSpiderMonkey = 1000; // population
            SpiderMonkeyOptimization smo = new SpiderMonkeyOptimization(data, MAX_ITERATION, allowedMaximumGroup, perturbationRate, localLeaderLimit, globalLeaderLimit, totalNumberOfSpiderMonkey);
            System.out.print("Best Individual SMO: ");
            System.out.println(smo.bestIndividual.toString());
            double totalDistanceSMO = smo.bestIndividual.getTotalDistance();
            SumTourCostSMO = SumTourCostSMO + totalDistanceSMO;
            if (totalDistanceSMO < MinTourCostSMO) {
                MinTourCostSMO = totalDistanceSMO;
            }
            // END OF SMO------------------------------------------------------------
        }// end of loop for testing

        // Calculate the average total distance achievement
        AverageTourCostSMO = SumTourCostSMO / numTest;

        System.out.println("=========================================================");
        System.out.println("C O M P A R I S O N    R E S U L T S    O F    A L G O R I T H M S");
        System.out.println("=========================================================");

        System.out.println("Average Tour Cost SMO = " + AverageTourCostSMO);
        System.out.println("---------------------------------------------------------");

        System.out.println("Minimum Tour Cost SMO = " + MinTourCostSMO);
        System.out.println("---------------------------------------------------------");

    }
}
