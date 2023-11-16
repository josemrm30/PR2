public class Individual {

    private Integer[] gens;
    private double fitness = 0;

    public Individual(Integer[] gens) {
        this.gens = gens;
        calculateFitness();
    }

    public Individual(Individual copy) {
        gens = new Integer[copy.gens.length];
        System.arraycopy(copy.gens, 0, this.gens, 0, copy.getGens().length);

        this.fitness = copy.fitness;
    }

    public void calculateFitness() {
        double[][] ciudades = Utils.getFileData().getDistancias();
        for (int i = 0; i < gens.length; i++) {
            fitness += ciudades[gens[i]][gens[(i + 1) % gens.length]];
        }
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
}
