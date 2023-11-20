import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;


public class Main {

    private static Logger log;
    private static LectorDatos data;

    public static void createLogger() throws IOException {
        Path dir = Path.of("./log");
        if (!Files.isDirectory(dir)) {
            Files.createDirectory(dir);
        }
        String logFile = "log/" + Utils.config.getFile() + "_" + Utils.config.getSeed() + "_" + "P=" + Utils.config.getPopulation() + "E=" + Utils.config.getElite() + "kBest=" + Utils.config.getKBest() + "kWorst=" + Utils.config.getKWorst() + ".txt";

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

    public static void evolutiveAlgorithm() {
        long initTime = System.currentTimeMillis();
        Integer actualEvaluations = 0;
        EvolutiveAlgorithm genes = new EvolutiveAlgorithm(Utils.config.getSeed(), log);

        genes.initialization(Utils.config.getPopulation(), data.getCiudades().length);
        actualEvaluations = genes.evaluation(genes.getPopulation(), actualEvaluations);
        while (actualEvaluations < Utils.config.getEvaluations() && ((System.currentTimeMillis() - initTime) / 1000) < Utils.config.getTime()) {

            genes.selection(Utils.config.getKBest());

            genes.cross();

            genes.mutation();

            actualEvaluations = genes.evaluation(genes.getNewPopulation(), actualEvaluations);

            genes.replacement();
            if (actualEvaluations % 100 == 0) {
                System.out.println(actualEvaluations);
            }
        }
        long endTime = System.currentTimeMillis();
        long diff = endTime - initTime;
        genes.elite(1);
        log.log(Level.INFO, "Run time = " + diff + " milliseconds. " + "Best Fitness = " + genes.getElites().get(0).getFitness());

    }

    public static void main(String[] args) throws IOException {
        Utils.loadFiles(args);
        data = Utils.getFileData();
        createLogger();


        evolutiveAlgorithm();
    }
}