/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updaterapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.json.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.io.IOUtils;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javafx.scene.control.ProgressBar;
import javax.swing.JOptionPane;

/**
 *
 * @author MBAPPE
 */
public class GestorActualizaciones {

    String versionUpdate = "";
    String versionCurrent = "";

    public GestorActualizaciones() {

    }

    public boolean checkUpdate(String FILEJson, String URLjson) {
        downloadUpdate(FILEJson, URLjson);
        String jsonTxt = "";
        try {
            InputStream is = new FileInputStream(FILEJson);
            jsonTxt = IOUtils.toString(is, "UTF-8");
        } catch (Exception e) {

        }
        JSONObject obj = new JSONObject(jsonTxt);

        getCurrentVersion();
        versionUpdate = obj.get("version").toString();

        if (!versionCurrent.equals(obj.get("version"))) {
            return true;
        }
        System.out.println("Version igual, no hay actualizaciones");
        return false;
    }

    public void downloadUpdate(String fileName, String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                File newFile = null;
                String fileName = ze.getName();

                newFile = new File(destDir + File.separator + fileName);
                if (!ze.getName().endsWith("/")) {
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());

                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                }
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getCurrentVersion() {
        String jsonTxt = "";
        try {
            InputStream is = new FileInputStream("updates.json");
            jsonTxt = IOUtils.toString(is, "UTF-8");
            JSONObject obj = new JSONObject(jsonTxt);

            versionCurrent = obj.get("version").toString();

        } catch (Exception e) {
        }
    }

    public boolean setCurrentVersion() {
        String jsonTxt = "";
        try {
            InputStream is = new FileInputStream("version.json");
            jsonTxt = IOUtils.toString(is, "UTF-8");
            JSONObject obj = new JSONObject(jsonTxt);

            obj.put("version", versionUpdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
