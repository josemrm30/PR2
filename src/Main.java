import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Utils.loadFiles(args);
        LectorDatos data = Utils.getFileData();

        GeneticAlgorithm genes = new GeneticAlgorithm(Utils.config.getSeeds().get(0));
        genes.initialization(Utils.config.getPopulation(), data.getCiudades().length);

        // TODO: ask cristobal if shuffle random and greedy

        for (int i = 0; i < genes.getPopulation().size(); i++) {
            System.out.println("i = " + i + " " + genes.getPopulation().get(i).getFitness());
        }
        genes.selection(Utils.config.getkBest());
        for (int i = 0; i < genes.getPopulation().size(); i++) {
            System.out.println("i = " + i + " " + genes.getPopulation().get(i).getFitness());
        }
        genes.cross();
    }
}