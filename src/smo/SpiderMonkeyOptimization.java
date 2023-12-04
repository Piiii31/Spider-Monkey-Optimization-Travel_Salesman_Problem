package smo;

import algo.Data;
import algo.Individual;
import algo.Swap;
import algo.Vertex;

import java.util.Random;

public class SpiderMonkeyOptimization {

    //INPUT-----------------------
    //Dataset
    private Data data = null;
    private Vertex[] arrayVertex = null;//Array of Vertices/Cities
    private int nVertex = 0;

    //Parameter SMO
    private int chromosomeLength;
    private int I; //Total Number of Iterations
    private int MG; //Allowed Maximum Group
    private double pr; //Perturbation Rate
    private int LLL; //Local Leader Limit
    private int GLL; //Global Leader Limit
    private int N; //Total Number of Spider Monkeys = nPopulasi     

    //Output
    public Individual bestIndividual = null;

    //Random
    private Random random = new Random();

    public SpiderMonkeyOptimization(
            Data data, int MAX_ITERATION, int allowedMaximumGroup, double perturbationRate,
            int localLeaderLimit, int globalLeaderLimit, int totalNumberOfSpiderMonkey) {

        this.data = data;
        this.arrayVertex = data.getArrayVertex();
        this.nVertex = this.arrayVertex.length;
        this.chromosomeLength = this.nVertex + 1;

        //set parameters SMO
        this.I = MAX_ITERATION;
        this.MG = allowedMaximumGroup;
        this.pr = perturbationRate;
        this.LLL = localLeaderLimit;
        this.GLL = globalLeaderLimit;
        this.N = totalNumberOfSpiderMonkey;

        //RUN SMO
        this.run();
    }

    public boolean validateParameters() {
        boolean valid = true;
        if (this.MG >= this.N) {
            this.MG = this.N / 2;
        }
        return valid;
    }

    public Individual[] generateRandomPopulation() {
        Individual[] population = null;
        if (this.data != null && this.N > 0) {
            population = new Individual[this.N];
            for (int p = 0; p < population.length; p++) {
                population[p] = new Individual(data);
                population[p].generateRandomChromosome();
                population[p].calculateFitnessValue();
            }
        }
        return population;
    }

    private int randomBetween(int min, int max) {
        if (min >= max) {
            //swap
            int temp = min;
            min = max;
            max = temp;
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public void printIndividual(Individual individual) {
        System.out.println(individual.toString());
    }

    public void printSpiderMonkeyPopulation(Individual[] population, Individual[] localLeaders, Individual globalLeader) {
        System.out.println("=====================================================================================================================================");
        //print Global Leader
        if (globalLeader != null) {
            System.out.println("GL: " + globalLeader.toString());
        }
        System.out.println("-----------------------------------------------------------------");
        //print LocalLeader
        if (localLeaders != null) {
            for (int i = 0; i < localLeaders.length; i++) {
                if (localLeaders[i] != null) {
                    System.out.println("LL_" + (i + 1) + ": " + localLeaders[i].toString());
                }
            }
        }
        int groupSize = -1;
        if (population != null && localLeaders != null) {
            groupSize = (int) Math.floor((double) N / (double) localLeaders.length);
        }
        int gr = 0;
        for (int p = 0; p < population.length; p++) {
            if (groupSize > 0 && p % groupSize == 0 && gr < localLeaders.length) {
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Group_" + gr + ":");
                gr++;
            }
            //print SM
            System.out.println("SM_" + (p) + ": " + population[p].toString());
        }
        System.out.println("=====================================================================================================================================");
    }

    public Individual[] selectLocalLeader(Individual[] spiderMonkey, int g) {
        Individual[] LL = null;
        if (spiderMonkey != null && g > 0) {
            int groupSize = (int) Math.floor((double) N / (double) g);
            LL = new Individual[g];
            for (int k = 0; k < g; k++) {
                LL[k] = null;
                int indexLL = -1;
                double bestFitness = 0;
                boolean inGroup = true;
                int index = 0;
                while (inGroup) {
                    int i = groupSize * k + index;
                    if (i >= spiderMonkey.length) {
                        inGroup = false;
                        break;
                    }
                    Individual SMi = spiderMonkey[i];
                    if (SMi.getFitnessValue() > bestFitness) {
                        bestFitness = SMi.getFitnessValue();
                        indexLL = i;
                    }
                    index++;
                    if (index != 0 && index % groupSize == 0) {
                        inGroup = false;
                        if (k >= g - 1) {
                            inGroup = true;
                        } else {
                            break;
                        }
                    }
                }
                //set Local leader 
                LL[k] = spiderMonkey[indexLL].clone();
                LL[k].calculateFitnessValue();
            }
        }
        return LL;
    }

    public Individual selectGlobalLeader(Individual[] LL) {
        Individual GL = null;
        if (LL != null) {
            int indexGL = -1;
            double bestFitness = 0;
            int g = LL.length;
            for (int k = 0; k < g; k++) {
                if (LL[k].getFitnessValue() > bestFitness) {
                    bestFitness = LL[k].getFitnessValue();
                    indexGL = k;
                }
            }
            GL = LL[indexGL].clone();
            GL.calculateFitnessValue();
        }
        return GL;
    }

    public Individual[] updateSpiderMonkeyBasedOnLocalLeader(Individual[] spiderMonkey, Individual[] LL, Individual GL) {
        Individual[] SM = null;
        if (spiderMonkey != null && LL != null && GL != null) {
            double min_cost = Double.MAX_VALUE;
            SM = new Individual[spiderMonkey.length];
            int g = LL.length;
            if (g > 0) {
                int groupSize = (int) Math.floor((double) N / (double) g);
                //Update Spider Monkey based on Local Leader---------------------
                for (int k = 0; k < g; k++) {
                    boolean inGroup = true;
                    int index = 0;
                    while (inGroup) {
                        int i = groupSize * k + index;
                        if (i >= spiderMonkey.length) {
                            inGroup = false;
                            break;
                        }
                        double U = random.nextDouble();
                        if (U >= pr) {
                            int MIN = k * groupSize;
                            int MAX = (k + 1) * groupSize - 1;
                            if (spiderMonkey.length - 1 - MAX < groupSize) {
                                MAX = spiderMonkey.length - 1;
                            }
                            int indexRandom = randomBetween(MIN, MAX);
                            while (indexRandom == i) {
                                indexRandom = randomBetween(MIN, MAX);
                            }
                            Individual LLk = LL[k];//LocalLeader ke k
                            SM[i] = spiderMonkey[i];
                            Individual RSM = spiderMonkey[indexRandom];

                            int[][] LLk_SMi = Swap.subtract(LLk, SM[i]);//LLk - SMi 
                            int[][] RSM_SMi = Swap.subtract(RSM, SM[i]);//RSM - SMi

                            int[][] SSi = Swap.mergeSwapSequence(LLk_SMi, RSM_SMi);//Equation 6 in the paper
                            int[][] BSSi = Swap.callBasicSwapSequence(SSi, chromosomeLength);
                            Individual SMnewi = Swap.add(SM[i], BSSi);//obtain new individual;
                            if (SMnewi.getFitnessValue() > SM[i].getFitnessValue()) {
                                SM[i] = SMnewi;
                            }
                        } else {
                            SM[i] = spiderMonkey[i];
                        }
                        //check min_cost
                        if (SM[i].getTotalDistance() >= 0 && spiderMonkey[index].getTotalDistance() < min_cost) {
                            min_cost = SM[i].getTotalDistance();
                        }
                        //increment index
                        index++;
                        if (index != 0 && index % groupSize == 0) {
                            inGroup = false;
                            if (k >= g - 1) {
                                inGroup = true;
                            } else {
                                break;
                            }
                        }
                    }
                }//end of for (int k = 0; k < g; k++)//end of Update Spider Monkey based on Local Leader
                //Update Spider Monkey based on Global Leader---------------------
                for (int k = 0; k < g; k++) {
                    boolean inGroup = true;
                    int index = 0;
                    while (inGroup) {
                        int i = groupSize * k + index;
                        if (i >= SM.length) {
                            inGroup = false;
                            break;
                        }
                        double U = random.nextDouble();
                        double cost_i = SM[i].getTotalDistance();
                        double prob_i = 0.9 * (min_cost / cost_i) + 0.1;
                        if (U <= prob_i) {
                            int MIN = 0;
                            int MAX = SM.length - 1;
                            int indexRandom = randomBetween(MIN, MAX);
                            while (indexRandom == i) {
                                indexRandom = randomBetween(MIN, MAX);
                            }
                            Individual RSM = SM[indexRandom];

                            int[][] GL_SMi = Swap.subtract(GL, SM[i]);//LLk - SMi 
                            int[][] RSM_SMi = Swap.subtract(RSM, SM[i]);//RSM - SMi

                            int[][] SSi = Swap.mergeSwapSequence(GL_SMi, RSM_SMi);//Equation 6 in the paper
                            int[][] BSSi = Swap.callBasicSwapSequence(SSi, chromosomeLength);
                            Individual SMnewi = Swap.add(SM[i], BSSi);//obtain new individual;
                            SMnewi.calculateFitnessValue();
                            if (SMnewi.getFitnessValue() > SM[i].getFitnessValue()) {
                                SM[i] = SMnewi;
                            }
                        }

                        //increment index
                        index++;
                        if (index != 0 && index % groupSize == 0) {
                            inGroup = false;
                            if (k >= g - 1) {
                                inGroup = true;
                            } else {
                                break;
                            }
                        }
                    }
                }//end of for (int k = 0; k < g; k++)//end of Update Spider Monkey based on Global Leader
            }//end of if(g>0)            
        }//end of if 
        return SM;
    }

    public void run() {
        // Variables
        if (validateParameters()) {
            // Generate Random Population            
            Individual[] spiderMonkeys = generateRandomPopulation(); // Create N SpiderMonkeys
            int g = 1; // Initially consider all SM into one group  
            // Select local leader and global leader
            Individual[] localLeaders = selectLocalLeader(spiderMonkeys, g);
            Individual globalLeader = selectGlobalLeader(localLeaders); // Global Leader   
            // Limits
            int[] localLeaderLimits = new int[localLeaders.length]; // Local Leader Limit
            int globalLeaderLimitCount = 0; // Global Leader Limit Count

            // printPopulationSpiderMonkeys(spiderMonkeys, localLeaders, globalLeader);            
            // System.out.println("Global Leader: " + globalLeader.toString());
            // Start the iteration operation
            for (int t = 1; t <= I; t++) {
                spiderMonkeys =updateSpiderMonkeyBasedOnLocalLeader(spiderMonkeys, localLeaders, globalLeader);

                // Update phase local leader------------------------------------
                if (spiderMonkeys != null && g > 0) {
                    int groupSize = (int) Math.floor((double) N / (double) g);
                    for (int k = 0; k < g; k++) {
                        int indexLL = -1;
                        double bestFitness = localLeaders[k].getFitnessValue();
                        boolean inGroup = true;
                        int index = 0;
                        while (inGroup) {
                            int i = groupSize * k + index;
                            if (i >= spiderMonkeys.length) {
                                inGroup = false;
                                break;
                            }
                            Individual SMi = spiderMonkeys[i];
                            if (SMi.getFitnessValue() > bestFitness) {
                                bestFitness = SMi.getFitnessValue();
                                indexLL = i;
                            }
                            index++;
                            if (index != 0 && index % groupSize == 0) {
                                inGroup = false;
                                if (k >= g - 1) {
                                    inGroup = true;
                                } else {
                                    break;
                                }
                            }
                        } // end of while
                        // Set Local leader 
                        if (indexLL >= 0) {
                            localLeaders[k] = spiderMonkeys[indexLL].clone(); // Set a new Local Leader
                            localLeaders[k].calculateFitnessValue();
                            localLeaderLimits[k] = 0;
                        } else {
                            localLeaderLimits[k]++;
                        }
                    }
                }
                // end of update phase local leader------------------------------

                // Update phase global leader------------------------------------
                if (localLeaders != null) {
                    int indexGL = -1;
                    double bestFitness = globalLeader.getFitnessValue();
                    for (int k = 0; k < g; k++) {
                        if (localLeaders[k].getFitnessValue() > bestFitness) {
                            bestFitness = localLeaders[k].getFitnessValue();
                            indexGL = k;
                        }
                    }
                    if (indexGL >= 0) {
                        globalLeader = localLeaders[indexGL].clone();
                        globalLeader.calculateFitnessValue();
                        globalLeaderLimitCount = 0;
                    } else {
                        globalLeaderLimitCount++;
                    }
                }
                // end of update phase global leader-----------------------------

                // Decision Phase Local Leader-----------------------------------
                if (spiderMonkeys != null && g > 0) {
                    int groupSize = (int) Math.floor((double) N / (double) g);
                    for (int k = 0; k < g; k++) {
                        if (localLeaderLimits[k] > LLL) {
                            localLeaderLimits[k] = 0;
                        }
                        boolean inGroup = true;
                        int index = 0;
                        while (inGroup) {
                            int i = groupSize * k + index;
                            if (i >= spiderMonkeys.length) {
                                inGroup = false;
                                break;
                            }
                            double U = random.nextDouble();
                            if (U >= pr) {
                                spiderMonkeys[i] = new Individual(data);
                                spiderMonkeys[i].generateRandomChromosome();
                                spiderMonkeys[i].calculateFitnessValue();
                            } else {
                                // Initialize SMi using EQ 13 
                                Individual SMi = spiderMonkeys[i].clone();
                                U = random.nextDouble();
                                Individual SMi_A = null;
                                Individual SMi_B = null;
                                if (U >= pr) {
                                    int[][] ss = Swap.subtract(globalLeader, SMi);
                                    SMi_A = Swap.add(SMi, ss);
                                }
                                if (U >= pr) {
                                    int[][] ss = Swap.subtract(SMi, localLeaders[k]);
                                    SMi_B = Swap.add(SMi_A, ss);
                                }
                                Individual SMi_new = null;
                                if (SMi_B != null) {
                                    SMi_new = SMi_B;
                                } else if (SMi_A != null) {
                                    SMi_new = SMi_A;
                                } else {
                                    SMi_new = SMi;
                                }
                                if (SMi_new != null) {
                                    SMi_new.calculateFitnessValue();
                                    spiderMonkeys[i] = SMi_new;
                                }
                            }
                            index++;
                            if (index != 0 && index % groupSize == 0) {
                                inGroup = false;
                                if (k >= g - 1) {
                                    inGroup = true;
                                } else {
                                    break;
                                }
                            }
                        } // end of while
                    }
                }
                // End of Decision Phase Local Leader----------------------------

                // Decision Phase Global Leader----------------------------------
                if (globalLeaderLimitCount > GLL) {
                    // System.out.println("DECISION PHASE");
                    if (g < MG) {
                        g++; //
                    } else {
                        g = 1;
                    }
                    // Reset group 
                    // Limits
                    localLeaderLimits = new int[g]; // Local Leader Limit
                    globalLeaderLimitCount = 0; // Global Leader Limit

                    // Select local leader and global leader
                    localLeaders = selectLocalLeader(spiderMonkeys, g);
                    globalLeader = selectGlobalLeader(localLeaders); // Global Leader  
                } // End of Decision Phase Global Leader----------------------------

            } // end of iteration process      

            // System.out.println("Global Leader: " + globalLeader.toString());
            bestIndividual = globalLeader;

        }
      }//end of run()
  }
// end of run()
