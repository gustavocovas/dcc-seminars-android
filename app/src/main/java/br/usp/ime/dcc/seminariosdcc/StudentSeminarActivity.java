package br.usp.ime.dcc.seminariosdcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import br.usp.ime.dcc.seminariosdcc.utils.SeminarsStore;

public class StudentSeminarActivity extends AppCompatActivity {

    private SeminarsStore seminarsStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        seminarsStore = new SeminarsStore(getApplicationContext());
        setContentView(R.layout.activity_student_seminar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!userIsLoggedIn()) {
            redirectToLogin();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_seminar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            try {
                seminarsStore.removeNusp();
                seminarsStore.removePass();
            } catch (IOException e) {
                e.printStackTrace();
            }
            redirectToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean userIsLoggedIn() {
        boolean isLoggedIn;

        try {
            isLoggedIn = (!seminarsStore.getNusp().isEmpty() &&
                          !seminarsStore.getPass().isEmpty());
        } catch (IOException e) {
            isLoggedIn = false;
        }

        return isLoggedIn;
    }

    private void redirectToLogin() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }
}
