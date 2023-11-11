import java.io.IOException;
import java.util.ArrayList;

public class Utils {
    static Configurator config;
    static ArrayList<LectorDatos> data = new ArrayList<>();

    public static void loadFiles(String[] args) throws IOException {
        config = new Configurator(args[0]);

        for (int i = 0; i < config.getFiles().size(); i++) {
            LectorDatos lector = new LectorDatos(config.getFiles().get(i));
            data.add(lector);
        }
    }
}