/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author user
 */
import algo.Data;
import algo.DataReader;
import algo.Vertex;
import smo.SpiderMonkeyOptimization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Test_SMO_001_GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SMO and GA Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton runButton = new JButton("Run Algorithm");
            runButton.setBackground(Color.RED);
            runButton.setForeground(Color.WHITE); // Set text color to white
            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    runAlgorithm();
                }
            });

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Color.BLUE);
            panel.add(runButton, new GridBagConstraints());

            frame.getContentPane().add(panel);
            frame.setSize(500, 150);  // Increased window size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void runAlgorithm() {
        // Your existing code for algorithm execution
        File file = new File("dataset/burma14.tsp");
        Vertex[] arrayVertex = DataReader.read(file);
        Data data = new Data(arrayVertex);

        int numTest = 10;

        double SumTourCostSMO = 0;

        double AverageTourCostSMO = -1;

        double MinTourCostGA = Double.MAX_VALUE;
        double MinTourCostSMO = Double.MAX_VALUE;

        // Create a JFrame for displaying test results
        JFrame testResultsFrame = new JFrame("Test Results");
        testResultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a JTextArea to display test results
        JTextArea testResultsTextArea = new JTextArea(20, 40);
        testResultsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(testResultsTextArea);

        // Add JTextArea to the JFrame
        testResultsFrame.getContentPane().add(scrollPane);
        testResultsFrame.setSize(600, 400);
        testResultsFrame.setLocationRelativeTo(null);
        testResultsFrame.setVisible(true);

        for (int test = 1; test <= numTest; test++) {
            testResultsTextArea.append("Test_" + test + "------------------------------------\n");

            // SMO-------------------------------------------------------------------
            int MAX_ITERATION = 100;
            int allowedMaximumGroup = 4;
            double perturbationRate = 0.5;
            int localLeaderLimit = 10;
            int globalLeaderLimit = 10;
            int totalNumberOfSpiderMonkey = 1000;
            SpiderMonkeyOptimization smo = new SpiderMonkeyOptimization(data, MAX_ITERATION, allowedMaximumGroup, perturbationRate, localLeaderLimit, globalLeaderLimit, totalNumberOfSpiderMonkey);
            testResultsTextArea.append("Best Individual SMO: " + smo.bestIndividual.toString() + "\n");
            double totalDistanceSMO = smo.bestIndividual.getTotalDistance();
            SumTourCostSMO = SumTourCostSMO + totalDistanceSMO;
            if (totalDistanceSMO < MinTourCostSMO) {
                MinTourCostSMO = totalDistanceSMO;
            }
            // END OF SMO------------------------------------------------------------
        }

        AverageTourCostSMO = SumTourCostSMO / numTest;

        // Display final results in a dialog after the tests
        JOptionPane.showMessageDialog(null,
                "Average Tour Cost SMO = " + AverageTourCostSMO + "\n" +
                        "Minimum Tour Cost SMO = " + MinTourCostSMO,
                "Algorithm Results",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
