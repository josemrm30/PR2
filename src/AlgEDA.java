import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AlgEDA {
    private int generation = 0;
    private final ArrayList<Individual> population = new ArrayList<>();
    private final Random rand;
    private final Logger log;
    private final int numKBest;
    private ArrayList<Individual> newPopulation = new ArrayList<>();
    private final int[][] cities;

    public AlgEDA(long seed, int kBest, Logger log, int[][] citiesList) {
        rand = new Random(seed);
        numKBest = kBest;
        this.log = log;
        cities = citiesList;
    }

    public void initialization(int numIndividuals, int numGens) {
        int randomIndividual = (int) (numIndividuals * Utils.config.getRandomRate());
        int greedyIndividual = (int) (numIndividuals * Utils.config.getGreedyRate());
        if (numIndividuals % 2 != 0) {
            randomIndividual++;
        }
        randomInitialization(randomIndividual, numGens);
        greedyInitialization(greedyIndividual, numGens);
    }

    public void randomInitialization(int randomIndividual, int numGens) {
        Integer[] initialIndividual = new Integer[numGens];
        for (int i = 0; i < numGens; i++) {
            initialIndividual[i] = i;
        }
        for (int i = 0; i < randomIndividual; i++) {
            List<Integer> auxList = Arrays.asList(initialIndividual.clone());
            Collections.shuffle(auxList);
            Integer[] auxGen2 = auxList.toArray(new Integer[0]);
            population.add(new Individual(auxGen2));
        }
    }

    public void greedyInitialization(int greedyIndividual, int numGens) {
        for (int i = 0; i < greedyIndividual; i++) {
            ArrayList<Integer> initialIndividual = new ArrayList<>();
            Set<Integer> marked = new HashSet<>();

            int gen = rand.nextInt(numGens);
            marked.add(gen);
            initialIndividual.add(gen);

            do {
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int j = 0; j < numGens && candidates.size() < Utils.config.getGreedyRandomSize(); j++) {
                    if (!marked.contains(cities[gen][j])) {
                        candidates.add(cities[gen][j]);
                    }
                }
                int randomCandidate = rand.nextInt(candidates.size());
                marked.add(candidates.get(randomCandidate));
                initialIndividual.add(candidates.get(randomCandidate));
                gen = candidates.get(randomCandidate);

            } while (initialIndividual.size() < numGens);
            population.add(new Individual(initialIndividual.toArray(new Integer[0])));
        }
    }

    public Individual OX2Child(Individual parent1, Individual parent2) {
        ArrayList<Integer> contained = new ArrayList<>();
        Individual child = new Individual(parent2);
        for (int i = 0; i < parent1.getGens().length; i++) {
            double random = rand.nextDouble(1);
            if (random < 0.5) {
                contained.add(parent1.getGens()[i]);
            }
        }
        Iterator<Integer> it = contained.iterator();

        for (int i = 0; i < parent2.getGens().length; i++) {
            if (contained.contains(parent2.getGens()[i])) {
                child.getGens()[i] = it.next();
            }
        }
        return child;
    }

    public Integer evaluation(ArrayList<Individual> popu, Integer actualEvaluations) {
        for (Individual individual : popu) {
            if (individual.getFitness() == 0) {
                calculateFitness(individual);
                actualEvaluations++;
            }
        }
        return actualEvaluations;
    }

    public void calculateFitness(Individual evaluated) {
        double fitness = 0;
        double[][] cities = Utils.getFileData().getDistancias();
        for (int i = 0; i < evaluated.getGens().length; i++) {
            fitness += cities[evaluated.getGens()[i]][evaluated.getGens()[(i + 1) % evaluated.getGens().length]];
        }
        evaluated.setFitness(fitness);
    }

    public ArrayList<Individual> getPopulation() {
        return population;
    }

    public ArrayList<Individual> getNewPopulation() {
        return newPopulation;
    }

    public void replacement() {
        for (int i = 0; i < population.size(); i++) {
            if (newPopulation.get(i).getFitness() < population.get(i).getFitness()) {
                population.set(i, newPopulation.get(i));
            }
        }
        newPopulation = new ArrayList<>();
    }

    public void checkBest() {
        double bestFitness = Double.MAX_VALUE;
        int pos = 0;
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getFitness() < bestFitness) {
                bestFitness = population.get(i).getFitness();
                pos = i;
            }
        }
        log.log(Level.INFO, " Fitness = " + population.get(pos).getFitness() + " Best " + pos + " " + Arrays.deepToString(population.get(pos).getGens()) + "\n");
    }

    public void selectionRecombination() {
        generation++;

        if (generation < 3 || generation % 100 == 0) {
            StringBuilder msg = new StringBuilder();
            for (Individual individual : population) {
                msg.append(" Fitness = ").append(individual.getFitness()).append(" ").append(Arrays.deepToString(individual.getGens())).append("\n");
            }
            log.log(Level.INFO, "Generation " + generation);
            log.log(Level.INFO, msg.toString());

        }

        for (int i = 0; i < population.size(); i++) {
            Individual parent = population.get(i);
            Individual rand1;
            Individual rand2;
            Individual objective;
            Individual newParent = new Individual(parent);
            Set<Individual> selectionList = new HashSet<>();
            selectionList.add(parent);

            do {
                rand1 = population.get(rand.nextInt(population.size()));
            } while (!selectionList.add(rand1));
            do {
                rand2 = population.get(rand.nextInt(population.size()));
            } while (!selectionList.add(rand2));

            Set<Individual> randomTournament = new HashSet<>();
            Individual random;
            do {
                random = population.get(rand.nextInt(population.size()));
                if (!selectionList.contains(random)) {
                    randomTournament.add(random);
                    selectionList.add(random);
                }
            } while (randomTournament.size() != numKBest);

            Iterator<Individual> it = randomTournament.iterator();
            Individual iteration = it.next();
            objective = iteration;
            while (it.hasNext()) {
                iteration = it.next();
                if (iteration.getFitness() < objective.getFitness()) {
                    objective = iteration;
                }

            }
            int cut1 = rand.nextInt(newParent.getGens().length - 1);
            int cut2 = cut1 + 1;
            Utils.swap(newParent.getGens(), cut1, cut2);

            for (int j = 0; j < rand1.getGens().length; j++) {
                if (rand1.getGens()[j].equals(newParent.getGens()[cut1])) {
                    int parentMatch = newParent.getGens()[j];
                    for (int k = 0; k < rand1.getGens().length; k++) {
                        if (parentMatch == rand1.getGens()[k]) {
                            Utils.swap(rand1.getGens(), j, k);
                        }
                    }
                }
            }

            for (int j = 0; j < rand2.getGens().length; j++) {
                if (rand2.getGens()[j].equals(newParent.getGens()[cut2])) {
                    int parentMatch = rand2.getGens()[j];
                    for (int k = 0; k < rand1.getGens().length; k++) {
                        if (parentMatch == rand1.getGens()[k]) {
                            Utils.swap(rand1.getGens(), j, k);
                        }
                    }
                }
            }
            //el resultado esta en rand1

            newPopulation.add(OX2Child(rand1, objective));
        }
        if (generation % 100 == 0) {
            checkBest();
        }

    }
}

