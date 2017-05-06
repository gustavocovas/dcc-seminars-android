package br.usp.ime.dcc.seminariosdcc.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserStore {
    private Context context;

    private String NUSP_FILENAME = "user.nusp";
    private String PASS_FILENAME = "user.pass";

    public UserStore(Context context) {
        this.context = context;
    }

    public boolean isLoggedIn() {
        boolean isLoggedIn;

        try {
            isLoggedIn = (!getNusp().isEmpty() &&
                          !getPass().isEmpty());
        } catch (IOException e) {
            isLoggedIn = false;
        }

        return isLoggedIn;
    }

    public void storeLoginCredentials(String nusp, String pass) throws IOException {
        setNusp(nusp);
        setPass(pass);
    }

    public void removeLoginCredentials() throws IOException {
        removeNusp();
        removePass();
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

    private void setNusp(String nusp) throws IOException {
        saveStringToFile(NUSP_FILENAME, nusp);
    }

    private String getNusp() throws IOException {
        return readStringFromFile(NUSP_FILENAME);
    }

    private void removeNusp() throws IOException {
        removeFile(NUSP_FILENAME);
    }

    private void setPass(String pass) throws IOException {
        saveStringToFile(PASS_FILENAME, pass);
    }

    private String getPass() throws IOException {
        return readStringFromFile(PASS_FILENAME);
    }

    private void removePass() throws IOException {
        removeFile(PASS_FILENAME);
    }
}
