import java.io.IOException;



public class Main {

    static LectorDatos data;

    public static void evolutiveAlgorithm(){
        Integer actualEvaluations = 0;
        EvolutiveAlgorithm genes = new EvolutiveAlgorithm(Utils.config.getSeeds().get(0));
        genes.initialization(Utils.config.getPopulation(), data.getCiudades().length);
        while (actualEvaluations < Utils.config.getEvaluations()) {

            actualEvaluations = genes.evaluation(genes.getPopulation(), actualEvaluations);

            genes.selection(Utils.config.getkBest());

            genes.cross();

            genes.mutation();

            actualEvaluations = genes.evaluation(genes.getNewPopulation(), actualEvaluations);

            genes.replacement();
            if(actualEvaluations % 100 == 0){
                System.out.println(actualEvaluations);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Utils.loadFiles(args);
        data = Utils.getFileData();

        evolutiveAlgorithm();
    }
}