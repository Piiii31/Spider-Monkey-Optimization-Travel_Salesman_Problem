package algo;

import java.awt.Point;
import java.util.ArrayList;

public class Swap {

    public static Individual swapOperation(Individual individual, int indexVertex1, int indexVertex2) {
        Individual newIndividual = null;
        if (individual != null
                && individual.getChromosome() != null
                && indexVertex1 >= 0
                && indexVertex2 >= 0
                && indexVertex1 < individual.getChromosome().length - 1
                && indexVertex2 < individual.getChromosome().length - 1
                && indexVertex1 != indexVertex2) {

            newIndividual = individual.clone();
            int[] chromosome = newIndividual.getChromosome();

            // SWAP OPERATION    
            int temp = chromosome[indexVertex1];
            chromosome[indexVertex1] = chromosome[indexVertex2];
            chromosome[indexVertex2] = temp;

            if (indexVertex1 == 0 || indexVertex2 == 0) {
                chromosome[chromosome.length - 1] = chromosome[0];
            }
        }
        return newIndividual;
    }

    public static Individual swapSequence(Individual individual, int[][] swapOperators) {
        Individual newIndividual = null;
        if (individual != null && swapOperators != null) {
            newIndividual = individual.clone();
            for (int i = 0; i < swapOperators.length; i++) {
                int indexVertex1 = swapOperators[i][0];
                int indexVertex2 = swapOperators[i][1];
                newIndividual = swapOperation(newIndividual, indexVertex1, indexVertex2);
            }
        }
        return newIndividual;
    }

    public static Individual bestSwap(Individual individual, int[][] swapOperators) {
        Individual best = null;
        if (individual != null) {
            if (swapOperators != null) {
                Individual newIndividual = individual.clone();
                double bestFitness = -1;
                for (int i = 0; i < swapOperators.length; i++) {
                    int indexVertex1 = swapOperators[i][0];
                    int indexVertex2 = swapOperators[i][1];
                    newIndividual = swapOperation(newIndividual, indexVertex1, indexVertex2);
                    newIndividual.calculateFitnessValue();
                    if (newIndividual.getFitnessValue() > bestFitness) {
                        best = newIndividual.clone();
                        bestFitness = newIndividual.getFitnessValue();
                    }
                }
            } else {
                best = individual.clone();
            }
        }
        return best;
    }

    public static Individual add(Individual individual, int[][] swapOperators) {
        return bestSwap(individual, swapOperators);
    }

    public static int[][] subtract(Individual individual1, Individual individual2) {
        int[][] swapOperators = null;
        try {
            ArrayList<Point> listSwapOperator = new ArrayList<Point>();
            int[] chromosome1 = individual1.getChromosome().clone();
            int[] chromosome2 = individual2.getChromosome();
            for (int i = 0; i < chromosome1.length; i++) {
                int key = chromosome2[i];
                if (key != chromosome1[i]) {
                    for (int j = 1 + i; j < chromosome1.length; j++) {
                        if (key == chromosome1[j]) {
                            // SWAP
                            int temp = chromosome1[i];
                            chromosome1[i] = chromosome1[j];
                            chromosome1[j] = temp;

                            // SAVE SWAP OPERATOR
                            listSwapOperator.add(new Point(i, j));

                            // BREAK AFTER SAVING SWAP OPERATOR
                            break;
                        }
                    }
                }
            }

            // SET OUTPUT
            if (listSwapOperator.size() > 0) {
                int n = listSwapOperator.size();
                swapOperators = new int[n][2];
                for (int i = 0; i < n; i++) {
                    Point p = listSwapOperator.get(i);
                    swapOperators[i][0] = (int) p.getX();
                    swapOperators[i][1] = (int) p.getY();
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return swapOperators;
    }

    public static String swapOperatorsToString(int[][] swapOperators) {
        String result = "NULL";
        if (swapOperators != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            for (int i = 0; i < swapOperators.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("SO(" + swapOperators[i][0] + "," + swapOperators[i][1] + ")");
            }
            sb.append("}");
            result = sb.toString();
        }
        return result;
    }

    public static int[][] callBasicSwapSequence(int[][] swapOperators, int chromosomeLength) {
        int[][] basicSS = null;
        try {
            if (chromosomeLength > 1 && swapOperators != null) {
                int[] chromosome1 = new int[chromosomeLength];
                for (int i = 0; i < chromosome1.length; i++) {
                    chromosome1[i] = i;
                }
                Individual individual1 = new Individual(chromosome1);
                Individual individual2 = swapSequence(individual1, swapOperators);
                basicSS = subtract(individual1, individual2);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return basicSS;
    }

    public static int[][] mergeSwapSequence(int[][] swapOperators1, int[][] swapOperators2) {
        int[][] result = null;
        int size = 0;
        if (swapOperators1 != null) {
            size += swapOperators1.length;
        }
        if (swapOperators2 != null) {
            size += swapOperators2.length;
        }
        if (size > 0) {
            result = new int[size][2];
            int k = 0;
            if (swapOperators1 != null) {
                for (int i = 0; i < swapOperators1.length; i++) {
                    result[k][0] = swapOperators1[i][0];
                    result[k][1] = swapOperators1[i][1];
                    k++;
                }
            }
            if (swapOperators2 != null) {
                for (int i = 0; i < swapOperators2.length; i++) {
                    result[k][0] = swapOperators2[i][0];
                    result[k][1] = swapOperators2[i][1];
                    k++;
                }
            }
        }
        return result;
    }
}
