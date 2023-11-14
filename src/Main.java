import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {


        Utils.loadFiles(args);
        LectorDatos data = Utils.getFileData();

       /* for (int i = 0; i < data.getCiudades().length; i++) {
            //System.out.println("city = " + i);
            //System.out.println(Arrays.toString(data.getCiudades()[i]));
            System.out.println(Arrays.toString(data.getDistancias()[i]));
        }
        */

        GeneticAlgorithm genes = new GeneticAlgorithm(Utils.config.getSeeds().get(0));
        genes.initialization(Utils.config.getPopulation(), data.getCiudades().length);

            System.out.println("testing 1");
            System.out.println(Arrays.deepToString(Utils.citiesByDistance(data.getCiudades().length)));

    }
}