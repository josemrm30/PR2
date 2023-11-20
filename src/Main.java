import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;


public class Main {

    private static Logger log;
    private static LectorDatos data;

    public static void createLogger(Long seed, int elite, int kBest, int kWorst, int population) throws IOException {
        Path dir = Path.of("./log");
        if (!Files.isDirectory(dir)) {
            Files.createDirectory(dir);
        }
        String logFile = "log/" + Utils.config.getFile() + "_" + seed + "_P=" + population + "_E=" + elite + "_kBest=" + kBest + "_kWorst=" + kWorst + ".txt";

        log = Logger.getLogger(EvolutiveAlgorithm.class.getName() + " " + logFile);
        if (Utils.config.getConsoleLog()) {
            ConsoleHandler consoleHand = new ConsoleHandler();
            log.addHandler(consoleHand);
        } else {
            FileHandler fileHand = new FileHandler(logFile);
            log.setUseParentHandlers(false);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHand.setFormatter(formatter);
            log.addHandler(fileHand);
        }
    }

    public static void evolutiveAlgorithm() throws IOException {
        for (int i = 0; i < Utils.config.getSeeds().size(); i++) {
            for (int j = 0; j < Utils.config.getElite().size(); j++) {
                for (int k = 0; k < Utils.config.getKBest().size(); k++) {
                    for (int l = 0; l < Utils.config.getPopulation().size(); l++) {
                        createLogger(Utils.config.getSeeds().get(i), Utils.config.getElite().get(j), Utils.config.getKBest().get(k), Utils.config.getKWorst(), Utils.config.getPopulation().get(l));
                        long initTime = System.currentTimeMillis();
                        Integer actualEvaluations = 0;
                        EvolutiveAlgorithm genes = new EvolutiveAlgorithm(Utils.config.getSeeds().get(i), Utils.config.getElite().get(j), Utils.config.getKBest().get(k), log);

                        genes.initialization(Utils.config.getPopulation().get(l), data.getCiudades().length);
                        actualEvaluations = genes.evaluation(genes.getPopulation(), actualEvaluations);
                        while (actualEvaluations < Utils.config.getEvaluations() && ((System.currentTimeMillis() - initTime) / 1000) < Utils.config.getTime()) {

                            genes.selection();

                            genes.cross();

                            genes.mutation();

                            actualEvaluations = genes.evaluation(genes.getNewPopulation(), actualEvaluations);

                            genes.replacement();
                        }
                        long endTime = System.currentTimeMillis();
                        long diff = endTime - initTime;
                        log.log(Level.INFO, "Run time = " + diff + " milliseconds. " + "Best Fitness = " + genes.getElites().get(0).getFitness());
                        for (Handler handler : log.getHandlers()) {
                            handler.close();
                        }
                    }
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        Utils.loadFiles(args);
        data = Utils.getFileData();


        evolutiveAlgorithm();
    }
}