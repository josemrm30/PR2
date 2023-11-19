public class Individual {

    private final Integer[] gens;
    private double fitness = 0;

    public Individual(Integer[] gens) {
        this.gens = gens;
    }

    public Individual(Individual copy) {
        gens = new Integer[copy.gens.length];
        System.arraycopy(copy.gens, 0, this.gens, 0, copy.getGens().length);

        this.fitness = 0;
    }



    public Integer[] getGens() {
        return gens;
    }

    public void setGen(int pos, int gen){
        gens[pos] = gen;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
