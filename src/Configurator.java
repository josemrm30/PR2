import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Configurator {
    private final ArrayList<String> files = new ArrayList<>();
    private final ArrayList<String> algorithms = new ArrayList<>();
    private final ArrayList<Long> seeds = new ArrayList<>();
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

    public Configurator(String path) throws IOException {
        String line;
        FileReader f;
        f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);

        while ((line = b.readLine()) != null) {
            String[] splited = line.split("=");
            switch (splited[0]) {
                case "Files":
                    String[] v = splited[1].split(" ");
                    files.addAll(Arrays.asList(v));
                    break;
                case "Seeds":
                    String[] vSeeds = splited[1].split(" ");
                    for (String vSeed : vSeeds) {
                        seeds.add(Long.parseLong(vSeed));
                    }
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
                default:
                    throw new IllegalStateException("Unexpected value: " + splited[0]);
            }
        }
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public ArrayList<String> getAlgorithms() {
        return algorithms;
    }

    public ArrayList<Long> getSeeds() {
        return seeds;
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

    public int getkBest() {
        return kBest;
    }

    public int getkWorst() {
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
}