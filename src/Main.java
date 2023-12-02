import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static LectorDatos data;
    static ExecutorService executor;


    public static void runFiles() throws IOException, InterruptedException {
        int num = Utils.config.getSeeds().size() * Utils.config.getElite().size() * Utils.config.getKBest().size() * Utils.config.getPopulation().size() + (Utils.config.getSeeds().size() * 2);
        CountDownLatch cdl = new CountDownLatch(num);
        int[][] cities = Utils.citiesByDistance(data.getCiudades().length);
        for (int i = 0; i < Utils.config.getAlgorithms().size(); i++) {
            if (Utils.config.getAlgorithms().get(i).equals("GenOX2")) {
                for (int j = 0; j < Utils.config.getSeeds().size(); j++) {
                    for (int k = 0; k < Utils.config.getElite().size(); k++) {
                        for (int l = 0; l < Utils.config.getKBest().size(); l++) {
                            for (int m = 0; m < Utils.config.getPopulation().size(); m++) {
                                Metaheuristic meta = new Metaheuristic(Utils.config.getAlgorithms().get(i), cdl, data, Utils.config.getSeeds().get(j), Utils.config.getElite().get(k), Utils.config.getKBest().get(l), Utils.config.getKWorst(), Utils.config.getPopulation().get(m), cities);
                                executor.execute(meta);
                            }
                        }
                    }
                }
            } else {
                for (int j = 0; j < Utils.config.getSeeds().size(); j++) {
                    Metaheuristic meta = new Metaheuristic(Utils.config.getAlgorithms().get(i), cdl, data, Utils.config.getSeeds().get(j), Utils.config.getEdKBest(), Utils.config.getEdPopulation(), cities);
                    executor.execute(meta);
                }
            }
        }
        cdl.await();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Utils.loadFiles(args);
        executor = Executors.newFixedThreadPool(Utils.config.getThreads());
        data = Utils.getFileData();
        Path dir = Path.of("./log");
        if (!Files.isDirectory(dir)) {
            Files.createDirectory(dir);
        }

        runFiles();
        executor.shutdown();
    }
}