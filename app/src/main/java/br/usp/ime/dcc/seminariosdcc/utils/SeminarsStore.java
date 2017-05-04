package br.usp.ime.dcc.seminariosdcc.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SeminarsStore {
    private Context context;

    private String NUSP_FILENAME = "user.nusp";
    private String PASS_FILENAME = "user.pass";

    public SeminarsStore(Context context) {
        this.context = context;
    }

    private void saveStringToFile(String filename, String string) throws IOException {
        FileOutputStream fos = context.getApplicationContext()
                .openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    }

    private void removeFile(String filename) throws IOException {
        saveStringToFile(filename, "");
    }

    private String readStringFromFile(String filename) throws IOException {
        FileInputStream fis = context.getApplicationContext()
                .openFileInput(filename);
        int size;
        String string = null;

        while ((size = fis.read()) != -1) {
            string += Character.toString((char) size);
        }
        fis.close();

        return string;
    }

    public void setNusp(String nusp) throws IOException {
        saveStringToFile(NUSP_FILENAME, nusp);
    }

    public String getNusp() throws IOException {
        return readStringFromFile(NUSP_FILENAME);
    }

    public void removeNusp() throws IOException {
        removeFile(NUSP_FILENAME);
    }

    public void setPass(String pass) throws IOException {
        saveStringToFile(PASS_FILENAME, pass);
    }

    public String getPass() throws IOException {
        return readStringFromFile(PASS_FILENAME);
    }

    public void removePass() throws IOException {
        removeFile(PASS_FILENAME);
    }
}
