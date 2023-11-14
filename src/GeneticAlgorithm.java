import java.util.*;

public class GeneticAlgorithm {

    private ArrayList<Integer[]> population = new ArrayList<>();
    private Random rand;

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
            population.add(auxGen2);
        }
    }

    public void greedyInitialization(int greedyIndividual, int numGens) {
        /*
         *
         *   declaro individuo
         *   declaro marcaje
         *       gen inicial aleatoria
         *       marco gen inicial
         *       hasta que todas estén marcadas
         *           compruebo las 5 más cercanas
         *           almaceno las 5 más cercanas no marcadas
         *           selecciono una aleatoria
         *           marco la seleccionada

         */
        int[][] cities = Utils.citiesByDistance(numGens);
        ArrayList<Integer> initialIndividual = new ArrayList<>();
        Set<Integer> marked = new HashSet<>();

        //en otro bucle
        int gen = rand.nextInt(numGens);
        marked.add(gen);
        initialIndividual.add(gen);

        for (int i = 0; i < numGens - 1; i++) {
            int indexGen = 0;
            ArrayList<Integer> candidates = new ArrayList<>();
            int numCandidates = 0;
            while (numCandidates < Utils.config.getGreedyRandomSize() && (numCandidates != (numGens -1 -indexGen))) {
                if (!marked.contains(cities[gen][indexGen])) {
                    candidates.add(cities[gen][indexGen]);
                    numCandidates++;
                }
                indexGen++;
            }


            int randomCandidate = rand.nextInt(numCandidates);
            marked.add(candidates.get(randomCandidate));
            initialIndividual.add(candidates.get(randomCandidate));
            gen = candidates.get(randomCandidate);
            if (i == 123) {
                System.out.println("aa");
            }
        }
    }


    public void selection() {

    }

    public void replacement() {

    }

    public void OX2() {
        OX2Child();
        OX2Child();
    }

    public void OX2Child() {
    }

    public void mutation() {

    }

    public void evaluation() {

    }

}
