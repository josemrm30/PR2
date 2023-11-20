import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Configurator {
    private String file = "";
    private ArrayList<Long> seeds = new ArrayList<>();
    private ArrayList<Integer> population = new ArrayList<>();
    private int greedyRandomSize;
    private ArrayList<Integer> elite = new ArrayList<>();
    private ArrayList<Integer> kBest = new ArrayList<>();
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
                    String[] vSeeds = splited[1].split(" ");
                    for (String vSeed : vSeeds) {
                        seeds.add(Long.parseLong(vSeed));
                    }
                    break;
                case "Population":
                    String[] vPopulations = splited[1].split(" ");
                    for (String vPopulation : vPopulations) {
                        population.add(Integer.parseInt(vPopulation));
                    }
                    break;
                case "GreedyRandomSize":
                    greedyRandomSize = Integer.parseInt(splited[1]);
                    break;
                case "Elite":
                    String[] vElites = splited[1].split(" ");
                    for (String vElite : vElites) {
                        elite.add(Integer.parseInt(vElite));
                    }
                    break;
                case "kBest":
                    String[] vkBests = splited[1].split(" ");
                    for (String vkBest : vkBests) {
                        kBest.add(Integer.parseInt(vkBest));
                    }
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


    public ArrayList<Long> getSeeds() {
        return seeds;
    }

    public ArrayList<Integer> getPopulation() {
        return population;
    }

    public int getGreedyRandomSize() {
        return greedyRandomSize;
    }

    public ArrayList<Integer> getElite() {
        return elite;
    }

    public ArrayList<Integer> getKBest() {
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