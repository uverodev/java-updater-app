package updaterapp;

import org.apache.commons.io.FileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

/**
 * Descripción: Cómo descargar archivos desde URL en Java
 * <p>
 * Using Java input output stream Using apache common IO Using NIO
 *
 * @autor: Zhao Xinguo
 * @date: 2018/6/7 10:15
 */
public class UpdaterApp {

    public static JbarraProgreso jBarra;

    public static void main(String[] args) {
        GestorActualizaciones gActualizaciones = new GestorActualizaciones();
        jBarra = new JbarraProgreso();

        String ZIPFILE = "C://Medempre//updates//archive.zip";
        String ZIPURL = "https://github.com/uverodev/java-updates/raw/main/lilianortigoza/lilianortigoza.zip";

        String URLJSON = "https://raw.githubusercontent.com/uverodev/java-updates/main/lilianortigoza/updates.json";
        String FILEJSON = "C://Medempre//updates//update.json";

        String FILEUPDATE = "C://Medempre//";

        if (gActualizaciones.checkUpdate(FILEJSON, URLJSON)) {
            jBarra.setLocationRelativeTo(null);
            jBarra.setVisible(true);
            jBarra.progressBar.setValue(1);
            jBarra.jbTexto.setText("Descargando...");
            gActualizaciones.downloadUpdate(ZIPFILE, ZIPURL);
            jBarra.progressBar.setValue(50);
            jBarra.jbTexto.setText("Instalando");
            gActualizaciones.unzip(ZIPFILE, FILEUPDATE);
            jBarra.progressBar.setValue(100);
            //gActualizaciones.setCurrentVersion();
            jBarra.setVisible(false);
            jBarra.jbTexto.setText("Finalizando");
        }

        try {
            run();
        } catch (IOException | URISyntaxException e) {
        }

    }

    public static void run() throws IOException, URISyntaxException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File file = new File("new-3-medempre.jar");


        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(file.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }

}
