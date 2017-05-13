package br.usp.ime.dcc.seminariosdcc.shared;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserStore {
    private Context context;

    private String NUSP_FILENAME = "user.nusp";
    private String PASS_FILENAME = "user.pass";

    public UserStore(Context context) {
        this.context = context;
    }

    public boolean isLoggedIn() {
        boolean isLoggedIn;

        String nusp = getNusp();
        String pass = getPass();

        isLoggedIn = (nusp != null && !nusp.isEmpty() &&
                      pass != null && !pass.isEmpty());

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

    public String getNusp() {
        try {
            return readStringFromFile(NUSP_FILENAME);
        } catch (IOException e) {
            return null;
        }
    }

    public String getPass() {
        try {
            return readStringFromFile(PASS_FILENAME);
        } catch (IOException e) {
            return null;
        }
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
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        StringBuilder sb = new StringBuilder();
        String line;
        while(( line = br.readLine()) != null ) {
            sb.append( line );
            sb.append( '\n' );
        }
        fis.close();

        return sb.toString();
    }

    private void setNusp(String nusp) throws IOException {
        saveStringToFile(NUSP_FILENAME, nusp);
    }

    private void removeNusp() throws IOException {
        removeFile(NUSP_FILENAME);
    }

    private void setPass(String pass) throws IOException {
        saveStringToFile(PASS_FILENAME, pass);
    }

    private void removePass() throws IOException {
        removeFile(PASS_FILENAME);
    }
}
