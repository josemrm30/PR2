import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class Main {

    private static Logger log;
    private static LectorDatos data;
    static ExecutorService executor = Executors.newCachedThreadPool();


    public static void runFiles() throws IOException, InterruptedException {
        int num = Utils.config.getSeeds().size() * Utils.config.getElite().size() * Utils.config.getKBest().size() * Utils.config.getPopulation().size();
        CountDownLatch cdl = new CountDownLatch(num);
        for (int i = 0; i < Utils.config.getSeeds().size(); i++) {
            for (int j = 0; j < Utils.config.getElite().size(); j++) {
                for (int k = 0; k < Utils.config.getKBest().size(); k++) {
                    for (int l = 0; l < Utils.config.getPopulation().size(); l++) {
                        Metaheuristic meta = new Metaheuristic(cdl, data, Utils.config.getSeeds().get(i), Utils.config.getElite().get(j), Utils.config.getKBest().get(k), Utils.config.getKWorst(), Utils.config.getPopulation().get(l));
                        executor.execute(meta);
                    }
                }
            }
        }
        cdl.await();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Utils.loadFiles(args);
        data = Utils.getFileData();
        Path dir = Path.of("./log");
        if (!Files.isDirectory(dir)) {
            Files.createDirectory(dir);
        }
        runFiles();
        executor.shutdown();
    }
}