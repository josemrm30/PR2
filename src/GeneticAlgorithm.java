import java.util.*;

public class GeneticAlgorithm {

    private ArrayList<Individual> population = new ArrayList<>();
    private final Random rand;
    private final ArrayList<Individual> elites = new ArrayList<>();

    private ArrayList<Individual> newPopulation = new ArrayList<>();

    public GeneticAlgorithm(long seed) {
        rand = new Random(seed);
    }

    public void initialization(int numIndividuals, int numGens) {
        int randomIndividual = (int) (numIndividuals * 0.8);
        int greedyIndividual = (int) (numIndividuals * 0.2);
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
            int[][] cities = Utils.citiesByDistance(numGens);
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

    public void elite(int numElites) {
        ArrayList<Double> maxFitness = new ArrayList<>();
        for (int i = 0; i < numElites; i++) {
            maxFitness.add(Double.MAX_VALUE);
        }
        for (int i = 0; i < numElites; i++) {
            for (int j = 0; j <population.size(); j++) {
                if (population.get(j).getFitness() < maxFitness.get(i) && !elites.contains(population.get(j))) {
                    maxFitness.set(i, population.get(j).getFitness());
                    if (elites.size() == i) {
                        elites.add(population.get(j));
                    } else {
                        elites.set(i, population.get(j));
                    }
                }
            }
        }
    }

    public void selection(int kbest) {
        elite(Utils.config.getElite());
        for (int i = 0; i < population.size(); i++) {
            int[] randomPositions = new int[kbest];

            for (int j = 0; j < kbest; j++) {
                randomPositions[j] = rand.nextInt(population.size());
            }
            Individual selected = null;
            double selectedFitness = Double.MAX_VALUE;

            for (int randomPosition : randomPositions) {
                if (population.get(randomPosition).getFitness() < selectedFitness) {
                    selected = population.get(randomPosition);
                    selectedFitness = selected.getFitness();
                }
            }
            newPopulation.add(selected);
        }
    }

    public void replacement() {

    }

    public void cross() {
        ArrayList<Individual> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.size() / 2; i++) {
            ArrayList<Individual> children = new ArrayList<>();
            int pos1 = rand.nextInt(population.size());
            int pos2 = rand.nextInt(population.size());
            double random = rand.nextDouble(1);
            children.add(population.get(pos1));
            children.add(population.get(pos2));
            if (random < Utils.config.getCrossProb()) {
                children = crossOX2(children.get(0), children.get(1));
            }
            newPopulation.addAll(children);
        }
        population = newPopulation;
    }

    public ArrayList<Individual> crossOX2(Individual parent1, Individual parent2) {
        ArrayList<Individual> children = new ArrayList<>();
        children.add(OX2Child(parent1, parent2));
        children.add(OX2Child(parent2, parent1));
        return children;
    }

    public Individual OX2Child(Individual parent1, Individual parent2) {

        Individual child = new Individual(parent2);
        boolean[] marked1 = new boolean[parent1.getGens().length];
        for (int i = 0; i < parent1.getGens().length; i++) {
            double random = rand.nextDouble(1);
            if (random < 0.5) {
                marked1[i] = true;
            }
        }
        for (int i = 0; i < parent2.getGens().length; i++) {
            for (int j = 0; j < parent1.getGens().length; j++) {
                if (marked1[i]) {
                    if (Objects.equals(parent2.getGens()[j], parent1.getGens()[i])) {
                        child.getGens()[j] = parent1.getGens()[i];
                    }
                }
            }
        }

        System.out.println("testing");
        System.out.println(Arrays.deepToString(child.getGens()));


        return child;
    }

    public void mutation(Individual individuo) {
        double probMutation = rand.nextDouble(1);
        if (probMutation < Utils.config.getMutationProb()) {
            int pos1 = rand.nextInt(individuo.getGens().length);
            int pos2 = rand.nextInt(individuo.getGens().length);
            int aux = individuo.getGens()[pos1];
            individuo.setGen(pos1, individuo.getGens()[pos2]);
            individuo.setGen(pos2, aux);
        }
    }

    public void evaluation() {

    }

    public ArrayList<Individual> getPopulation() {
        return population;
    }
}
