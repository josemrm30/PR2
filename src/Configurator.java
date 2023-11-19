import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Configurator {
    private String file = "";
    private Long seed;
    private int population;
    private int greedyRandomSize;
    private int elite;
    private int kBest;
    private int kWorst;
    private double crossProb;
    private double mutationProb;
    private int evaluations;
    private int time;
    private Boolean consoleLog;
    private double randomRate;
    private double greedyRate;

    public Configurator(String path) throws IOException {
        String line;
        FileReader f;
        f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);

        while ((line = b.readLine()) != null) {
            String[] splited = line.split("=");
            switch (splited[0]) {
                case "Files":
                    file = splited[1];
                    break;
                case "Seeds":
                    seed = Long.parseLong(splited[1]);
                    break;
                case "Population":
                    population = Integer.parseInt(splited[1]);
                    break;
                case "GreedyRandomSize":
                    greedyRandomSize = Integer.parseInt(splited[1]);
                    break;
                case "Elite":
                    elite = Integer.parseInt(splited[1]);
                    break;
                case "kBest":
                    kBest = Integer.parseInt(splited[1]);
                    break;
                case "kWorst":
                    kWorst = Integer.parseInt(splited[1]);
                    break;
                case "CrossProb":
                    crossProb = Double.parseDouble(splited[1]);
                    break;
                case "MutationProb":
                    mutationProb = Double.parseDouble(splited[1]);
                    break;
                case "Evaluations":
                    evaluations = Integer.parseInt(splited[1]);
                    break;
                case "Time":
                    time = Integer.parseInt(splited[1]);
                    break;
                case "ConsoleLog":
                    consoleLog = Boolean.parseBoolean(splited[1]);
                    break;
                case "RandomRate":
                    randomRate = Double.parseDouble(splited[1]);
                    break;
                case "GreedyRate":
                    greedyRate = Double.parseDouble(splited[1]);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + splited[0]);
            }
        }
    }

    public String getFile() {
        return file;
    }


    public Long getSeed() {
        return seed;
    }

    public int getPopulation() {
        return population;
    }

    public int getGreedyRandomSize() {
        return greedyRandomSize;
    }

    public int getElite() {
        return elite;
    }

    public int getKBest() {
        return kBest;
    }

    public int getKWorst() {
        return kWorst;
    }

    public double getCrossProb() {
        return crossProb;
    }

    public double getMutationProb() {
        return mutationProb;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public int getTime() {
        return time;
    }

    public Boolean getConsoleLog() {
        return consoleLog;
    }

    public double getRandomRate() {
        return randomRate;
    }

    public double getGreedyRate() {
        return greedyRate;
    }
}