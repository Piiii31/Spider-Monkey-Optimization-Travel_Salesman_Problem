package algo;

import java.util.Random;

public class Individual {

    private int[] chromosome = null;
    private Data data = null;
    private double totalDistance = -1;
    private double fitnessValue = 0;

    public Individual(Data data) {
        this.data = data;
    }

    public Individual clone() {
        Individual cloning = new Individual(data);
        cloning.chromosome = this.chromosome.clone();
        this.calculateFitnessValue();
        return cloning;
    }

    public Individual(Data data, int[] chromosome) {
        this.data = data;
        this.chromosome = chromosome;
    }

    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public Data getData() {
        return data;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public static int randomBetween(int min, int max) {
        if (min >= max) {
            // Swap
            int temp = min;
            min = max;
            max = temp;
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public int[] generateRandomChromosome() {
        if (data != null && data.getArrayVertex().length > 0) {
            int n = data.getArrayVertex().length;
            int min = 0;
            int max = n - 1;

            // Random initial and final vertices
            int initialVertex = randomBetween(min, max);
            int finalVertex = initialVertex;

            //Chromosome
            this.chromosome = new int[n + 1];
            this.chromosome[0] = initialVertex; // Initial vertex
            this.chromosome[n] = finalVertex; // Final vertex

            // Random intermediate vertices
            for (int i = 1; i < n; i++) {
                boolean same = true;
                while (same) {
                    int r = randomBetween(min, max);
                    same = false;
                    for (int j = 0; j < i; j++) {
                        if (r == this.chromosome[j]) {
                            same = true;
                            break;
                        }
                    }
                    if (!same) {
                        this.chromosome[i] = r;
                    }
                }
            }
        }
        return this.chromosome;
    }

    public double calculateTotalDistance() {
        this.totalDistance = -1;
        if (chromosome != null) {
            double total = 0;
            for (int i = 1; i < chromosome.length; i++) {
                int indexVertex1 = chromosome[i - 1];
                int indexVertex2 = chromosome[i];
                double distance = data.calculateDistance(indexVertex1, indexVertex2);
                total += distance;
            }
            this.totalDistance = total;
        }
        return this.totalDistance;
    }

    public double calculateFitnessValue() {
        this.fitnessValue = 0;
        this.calculateTotalDistance();
        if (this.totalDistance > 0) {
            this.fitnessValue = 1.0 / this.totalDistance;
        }
        return this.fitnessValue;
    }

    @Override
    public String toString() {
        String result = "NULL";
        if (chromosome != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chromosome.length; i++) {
                if (i > 0) {
                    sb.append(" - ");
                }
                int indexVertex = chromosome[i];
                String label = data.getArrayVertex()[indexVertex].label;
                sb.append(label);
            }
            sb.append(" Total Distance: " + this.totalDistance);
            sb.append(" Fitness: " + this.fitnessValue);
            result = sb.toString();
        }
        return result;
    }
}
