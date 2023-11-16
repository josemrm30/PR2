import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Utils.loadFiles(args);
        LectorDatos data = Utils.getFileData();

        EvolutiveAlgorithm genes = new EvolutiveAlgorithm(Utils.config.getSeeds().get(0));

        genes.initialization(Utils.config.getPopulation(), data.getCiudades().length);

        genes.selection(Utils.config.getkBest());

        genes.cross();

        genes.mutation();

        genes.replacement();
    }
}