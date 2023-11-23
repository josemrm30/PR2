import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AlgEDB {
    private final int numElites;
    private final int numKBest;
    private int generation = 0;
    private ArrayList<Individual> population = new ArrayList<>();
    private final Random rand;
    private final Logger log;
    private ArrayList<Individual> elites;
    private ArrayList<Individual> worsts;
    private ArrayList<Individual> newPopulation = new ArrayList<>();
    private int[][] cities;

    public AlgEDB(long seed, int elite, int kbest, Logger log, int[][] citiesList) {
        rand = new Random(seed);
        numElites = elite;
        numKBest = kbest;
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

    public void elite() {
        elites = new ArrayList<>();
        ArrayList<Double> maxFitness = new ArrayList<>();
        for (int i = 0; i < numElites; i++) {
            maxFitness.add(Double.MAX_VALUE);
        }
        for (int i = 0; i < numElites; i++) {
            for (Individual individual : population) {
                if (individual.getFitness() < maxFitness.get(i) && !elites.contains(individual)) {
                    maxFitness.set(i, individual.getFitness());
                    if (elites.size() == i) {
                        elites.add(individual);
                    } else {
                        elites.set(i, individual);
                    }
                }
            }
        }
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < elites.size(); i++) {
            msg.append(" Fitness = ").append(elites.get(i).getFitness()).append(" Elite ").append(i).append(" ").append(Arrays.deepToString(elites.get(i).getGens())).append("\n");
        }
        log.log(Level.INFO, "Generation " + generation);
        log.log(Level.INFO, msg.toString());
    }

    public void selection() {
        generation++;

        if (generation < 3 || generation % 100 == 0) {
            StringBuilder msg = new StringBuilder();
            for (Individual individual : population) {
                msg.append(" Fitness = ").append(individual.getFitness()).append(" ").append(Arrays.deepToString(individual.getGens())).append("\n");
            }
            log.log(Level.INFO, "Generation " + generation);
            log.log(Level.INFO, msg.toString());

        }
        elite();

        for (int i = 0; i < population.size(); i++) {
            int[] randomPositions = new int[numKBest];

            for (int j = 0; j < numKBest; j++) {
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


    public void cross() {
        ArrayList<Individual> auxPopulation = new ArrayList<>();
        for (int i = 0; i < newPopulation.size() / 2; i++) {
            ArrayList<Individual> children = new ArrayList<>();
            int pos1 = rand.nextInt(newPopulation.size());
            int pos2 = rand.nextInt(newPopulation.size());
            double random = rand.nextDouble(1);
            children.add(newPopulation.get(pos1));
            children.add(newPopulation.get(pos2));
            if (random < Utils.config.getCrossProb()) {
                children = crossOX2(children.get(0), children.get(1));
            }
            auxPopulation.addAll(children);
        }
        newPopulation = auxPopulation;
    }

    public ArrayList<Individual> crossOX2(Individual parent1, Individual parent2) {
        ArrayList<Individual> children = new ArrayList<>();
        children.add(OX2Child(parent1, parent2));
        children.add(OX2Child(parent2, parent1));
        return children;
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

    public void mutation() {
        for (Individual individual : newPopulation) {
            double probMutation = rand.nextDouble(1);
            if (probMutation < Utils.config.getMutationProb()) {
                int pos1 = rand.nextInt(individual.getGens().length);
                int pos2 = rand.nextInt(individual.getGens().length);
                Utils.swap(individual.getGens(), pos1, pos2);
                int aux = individual.getGens()[pos1];
                individual.setGen(pos1, individual.getGens()[pos2]);
                individual.setGen(pos2, aux);
            }
        }
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
            if(newPopulation.get(i).getFitness() < population.get(i).getFitness()){
                population.set(i,newPopulation.get(i));
            }
        }
        newPopulation = new ArrayList<>();
    }

    public void worstTournament() {
        int kWorst = Utils.config.getKWorst();
        for (Individual elite : elites) {
            if (!newPopulation.contains(elite)) {
                worst(kWorst);
                int randomPosition = rand.nextInt(kWorst);
                Individual aux = worsts.get(randomPosition);
                int position = newPopulation.indexOf(aux);
                newPopulation.set(position, elite);
            }
        }
    }

    public void worst(int kWorst) {
        worsts = new ArrayList<>();
        ArrayList<Double> minFitness = new ArrayList<>();
        for (int i = 0; i < kWorst; i++) {
            minFitness.add(Double.MIN_VALUE);
        }
        for (int i = 0; i < kWorst; i++) {
            for (Individual individual : newPopulation) {
                if (individual.getFitness() > minFitness.get(i) && !worsts.contains(individual)) {
                    minFitness.set(i, individual.getFitness());
                    if (worsts.size() == i) {
                        worsts.add(individual);
                    } else {
                        worsts.set(i, individual);
                    }
                }
            }
        }
    }

    public void EvolutionDiferentialB() {
        for (int i = 0; i < newPopulation.size(); i++) {
            Individual secuencial = newPopulation.get(i);

            ArrayList<Individual> selected = new ArrayList<>();
            int countindividual = 0;
            while (countindividual < 3) {

                Individual randtour1;
                Individual randtour2;
                do{
                    randtour1 = newPopulation.get(rand.nextInt(newPopulation.size()));
                }while(randtour1 == secuencial);
                do{
                    randtour2 = newPopulation.get(rand.nextInt(newPopulation.size()));
                }while(randtour2 == randtour1 || randtour2 == secuencial);

                Individual best = randtour1;
                if (randtour1.getFitness() < randtour2.getFitness()) {
                    best = randtour1;
                } else if (randtour2.getFitness() < randtour1.getFitness()) {
                    best = randtour2;
                }
                int iguales = 0;
                for (int j = 0; j < selected.size(); j++) {
                    if (best == selected.get(i)) {
                        iguales++;
                        break;
                    }
                }
                if (iguales == 0) {
                    selected.add(best);
                    countindividual++;
                }
            }
            Individual rand1 = new Individual(selected.get(0));
            Individual rand2 = new Individual(selected.get(1));
            int corte1 = rand.nextInt(newPopulation.size() - 2);
            int corte2 = corte1 + 1;
            for (int j = 0; j < rand1.getGens().length; j++) {
                if (rand1.getGens()[j] == secuencial.getGens()[corte1]) {
                    int datosecuencial = secuencial.getGens()[j];
                    for (int k = 0; k < secuencial.getGens().length; k++) {
                        if (datosecuencial == secuencial.getGens()[k]) {
                            int aux = rand1.getGens()[j];
                            rand1.getGens()[j] = rand1.getGens()[k];
                            rand1.getGens()[k] = aux;
                        }
                    }
                }
            }
            for (int j = 0; j < rand2.getGens().length; j++) {
                if (rand2.getGens()[j] == secuencial.getGens()[corte2]) {
                    int datarand1 = rand1.getGens()[j];
                    for (int k = 0; k < rand1.getGens().length; k++) {
                        if (datarand1 == rand1.getGens()[k]) {
                            int aux = rand1.getGens()[j];
                            rand1.getGens()[j] = rand1.getGens()[k];
                            rand1.getGens()[k] = aux;
                        }
                    }
                }
            }
            //el resultado esta en rand1

            Individual objetive = new Individual(selected.get(2));
            newPopulation.add(OX2Child(objetive, rand1));
        }
    }
}

