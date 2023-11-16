import java.util.*;

public class GeneticAlgorithm {

    private ArrayList<Individual> population = new ArrayList<>();
    private Random rand;
    private ArrayList<Individual> elites = new ArrayList<>();

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
            for (int j = 0; j < population.size(); j++) {
                if (population.get(j).getFitness() < maxFitness.get(i) && !elites.contains(population.get(j))) {
                    maxFitness.set(i,population.get(j).getFitness());
                    if (elites.isEmpty()) {
                        elites.add(population.get(j));
                    }else{
                        elites.set(i, population.get(j));
                    }
                }
            }
        }
        for (int i = 0; i < numElites; i++) {
            System.out.println(elites.toString());
        }

    }

    public void selection(int kbest) {
        elite(Utils.config.getElite());
        ArrayList<Individual> newPopulation = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            int[] randomPositions = new int[kbest];

            for (int j = 0; j < kbest; j++) {
                randomPositions[j] = rand.nextInt(population.size());
            }
            Individual selected = null;
            double selectedFitness = Double.MAX_VALUE;

            for (int j = 0; j < randomPositions.length; j++) {
                if (population.get(randomPositions[j]).getFitness() < selectedFitness) {
                    selected = population.get(randomPositions[j]);
                    selectedFitness = selected.getFitness();
                }
            }
            newPopulation.add(selected);
        }
        population = newPopulation;
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
    }

    public ArrayList<Individual> crossOX2(Individual parent1, Individual parent2) {
        ArrayList<Individual> children = new ArrayList<>();
        children.add(OX2Child(parent1, parent2));
        children.add(OX2Child(parent2, parent1));
        return children;
    }

    public Individual OX2Child(Individual parent1, Individual parent2) {
        int aleatorio;
        boolean[] marcadop1 = new boolean[parent1.getGens().length];
        boolean[] marcadop2 = new boolean[parent2.getGens().length];
        Individual children;
        int pos = 0;
        children = parent2;
        ArrayList<Integer> restantes = new ArrayList<Integer>();
        while (pos < parent1.getGens().length) {   // me recorro padre1 y marco
            aleatorio = rand.nextInt(100);
            if (aleatorio < 50) {
                //los busco en padre2 y los marco
                for (int i = 0; i < parent2.getGens().length; i++) {
                    if (parent1.getGens()[pos] == parent2.getGens()[i]) {
                        marcadop2[i] = true;
                        //padre2.solucion.set(i, -1); //lo marco con -1
                    }
                }
                marcadop1[pos] = true;
                restantes.add(parent1.getGens()[pos]);
            }
            pos++;
        }
        int indRestan = 0;
        for (int i = 0; i < parent2.getGens().length; i++) {
            if (marcadop2[i]) {
                children.setGen(i, restantes.get(indRestan));
                indRestan++;
            } else {
                children.setGen(i, parent2.getGens()[i]);
            }
        }
        return children;
    }

    public void mutacion(Individual individuo) {
        int pos1 = rand.nextInt(individuo.getGens().length);
        int pos2 = rand.nextInt(individuo.getGens().length);
        int aux = individuo.getGens()[pos1];
        individuo.setGen(pos1, individuo.getGens()[pos2]);
        individuo.setGen(pos2, aux);
    }

    public void evaluation() {

    }

    public ArrayList<Individual> getPopulation() {
        return population;
    }
}
