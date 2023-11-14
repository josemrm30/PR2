import java.io.IOException;


public class Utils {
    static Configurator config;
    private static LectorDatos data;

    public Utils() {

    }

    public static void loadFiles(String[] args) throws IOException {
        config = new Configurator(args[0]);

        for (int i = 0; i < config.getFiles().size(); i++) {

            data = new LectorDatos(config.getFiles().get(i));
        }

    }

    public static LectorDatos getData() {
        return data;
    }
}