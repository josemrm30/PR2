import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;

public class Metaheuristic implements Runnable {

    private Logger log;
    private final CountDownLatch cdl;
    private final LectorDatos data;
    private final Long seed;
    private int elite;
    private final int kBest;
    private int kWorst;
    private final int population;
    private final int[][] cities;
    private final String alg;


    public Metaheuristic(String algorithm, CountDownLatch cdl, LectorDatos dat, Long seed, int elite, int kBest, int kWorst, int population, int[][] citiesList) throws IOException {
        this.alg = algorithm;
        this.cdl = cdl;
        this.data = dat;
        this.seed = seed;
        this.elite = elite;
        this.kBest = kBest;
        this.kWorst = kWorst;
        this.population = population;
        this.cities = citiesList;
    }

    public Metaheuristic(String algorithm, CountDownLatch cdl, LectorDatos dat, Long seed, int kBest, int population, int[][] citiesList) throws IOException {
        this.alg = algorithm;
        this.cdl = cdl;
        this.data = dat;
        this.seed = seed;
        this.kBest = kBest;
        this.population = population;
        this.cities = citiesList;
    }

    public void createLogger(Long seed, int elite, int kBest, int kWorst, int population) throws IOException {
        String logFile = "log/" + Utils.config.getFile() + "_" + seed + "_P=" + population + "_E=" + elite + "_kBest=" + kBest + "_kWorst=" + kWorst + ".txt";

        log = Logger.getLogger(ALGGenOX2.class.getName() + " " + logFile);
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

    @Override
    public void run() {
        try {
            createLogger(seed, elite, kBest, kWorst, population);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Integer actualEvaluations = 0;
        long initTime;
        long endTime;
        long diff;



        switch(alg) {
            case "GenOX2":
                ALGGenOX2 genes = new ALGGenOX2(seed, elite, kBest, log, cities);
                genes.initialization(population, data.getCiudades().length);
                initTime = System.currentTimeMillis();
                actualEvaluations = genes.evaluation(genes.getPopulation(), actualEvaluations);
                while (actualEvaluations < Utils.config.getEvaluations() && ((System.currentTimeMillis() - initTime) / 1000) < Utils.config.getTime()) {
                    genes.selection();

                    genes.cross();

                    genes.mutation();

                    actualEvaluations = genes.evaluation(genes.getNewPopulation(), actualEvaluations);

                    genes.replacement();
                }
                endTime = System.currentTimeMillis();
                diff = endTime - initTime;
                log.log(Level.INFO, "Run time = " + diff + " milliseconds. ");
                break;
            case "EDA":
                AlgEDA genesEDA = new AlgEDA(seed, kBest, log, cities);
                genesEDA.initialization(population, data.getCiudades().length);
                initTime = System.currentTimeMillis();
                actualEvaluations = genesEDA.evaluation(genesEDA.getPopulation(), actualEvaluations);
                while (actualEvaluations < Utils.config.getEvaluations() && ((System.currentTimeMillis() - initTime) / 1000) < Utils.config.getTime()) {
                    genesEDA.selectionRecombination();

                    actualEvaluations = genesEDA.evaluation(genesEDA.getNewPopulation(), actualEvaluations);

                    genesEDA.replacement();
                }
                endTime = System.currentTimeMillis();
                diff = endTime - initTime;
                log.log(Level.INFO, "Run time = " + diff + " milliseconds. ");
                break;
            case "EDB":
                AlgEDB genesEDB = new AlgEDB(seed, kBest, log, cities);
                genesEDB.initialization(population, data.getCiudades().length);
                initTime = System.currentTimeMillis();
                actualEvaluations = genesEDB.evaluation(genesEDB.getPopulation(), actualEvaluations);
                while (actualEvaluations < Utils.config.getEvaluations() && ((System.currentTimeMillis() - initTime) / 1000) < Utils.config.getTime()) {
                    genesEDB.selectionRecombination();

                    actualEvaluations = genesEDB.evaluation(genesEDB.getNewPopulation(), actualEvaluations);

                    genesEDB.replacement();
                }
                endTime = System.currentTimeMillis();
                diff = endTime - initTime;
                log.log(Level.INFO, "Run time = " + diff + " milliseconds. ");
                break;
        }



        cdl.countDown();
        for (Handler handler : log.getHandlers()) {
            handler.close();
        }
    }
}