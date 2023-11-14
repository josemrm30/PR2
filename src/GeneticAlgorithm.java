import java.util.*;

public class GeneticAlgorithm {

    private ArrayList<Integer[]> population = new ArrayList<>();
    private Random rand;

    public GeneticAlgorithm(long seed) {
        rand = new Random(seed);
    }

    public void initialization(int tam, int numCities) {
        int randomGen = (int) (tam * 0.8);
        int greedyGen = (int) (tam * 0.2);
        if (tam % 2 != 0) {
            randomGen++;
        }

        Integer[] initialGen = new Integer[numCities];
        for (int i = 0; i < numCities; i++) {
            initialGen[i] = i;
        }
        for (int i = 0; i < randomGen; i++) {
            Integer[] auxGen;
            auxGen = initialGen.clone();
            List<Integer> auxList = Arrays.asList(auxGen);
            Collections.shuffle(auxList);
            Integer[] auxGen2 = auxList.toArray(new Integer[0]);
            population.add(auxGen2);
        }
        for (int i = 0; i < randomGen; i++) {
            System.out.println("Gen " + i);
            System.out.println(Arrays.toString(population.get(i)));
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
