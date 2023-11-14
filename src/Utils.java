import java.io.IOException;
import java.util.*;


public class Utils {
    static Configurator config;
    private static LectorDatos fileData;

    public Utils() {

    }

    public static void loadFiles(String[] args) throws IOException {
        config = new Configurator(args[0]);

        for (int i = 0; i < config.getFiles().size(); i++) {

            fileData = new LectorDatos(config.getFiles().get(i));
        }

    }

    public static LectorDatos getFileData() {
        return fileData;
    }

    public static int[][] citiesByDistance(int tam) {
        int[][] cities = new int[tam][tam];

        for (int i = 0; i < tam; i++) {
            ArrayList<Map.Entry<Integer, Double>> cityDistances = new ArrayList<>();
            for (int j = 0; j < tam; j++) {
                cityDistances.add(new AbstractMap.SimpleEntry<>(j, fileData.getDistancias()[i][j]));
            }
            orderCities(cityDistances);

            int[] cityOrder = new int[tam];
            for (int j = 0; j < cityDistances.size(); j++) {
                cityOrder[j] = cityDistances.get(j).getKey();
            }
            cities[i] = cityOrder;
        }
        return cities;
    }

    private static void orderCities(List<Map.Entry<Integer, Double>> cityOrder) {
        cityOrder.sort(Map.Entry.comparingByValue());
    }
}