package br.usp.ime.dcc.seminariosdcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

import br.usp.ime.dcc.seminariosdcc.utils.SeminarsStore;
import br.usp.ime.dcc.seminariosdcc.utils.SeminarsWebService;

public class StudentSeminarActivity extends AppCompatActivity {

    private RequestQueue queue;
    private SeminarsStore seminarsStore;

    private ListView seminarListView;

    private String[] seminars = new String[] {"Seminário 1", "Seminário 2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        seminarsStore = new SeminarsStore(getApplicationContext());

        setContentView(R.layout.activity_student_seminar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isUserLoggedIn()) {
            redirectToLogin();
        }

        setupSeminarsList();
    }

    private void setupSeminarsList() {
        seminarListView = (ListView) findViewById(R.id.list_student_seminars);
        final ArrayAdapter seminarsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, seminars);
        seminarListView.setAdapter(seminarsAdapter);
    }

    private void fetchSeminars() {
        String seminarsURL = SeminarsWebService.URL + "/seminar";

        StringRequest seminarsRequest = new StringRequest(
                Request.Method.GET,
                seminarsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO: Handle "Successful" case
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyFetchSeminarsFailure();
                    }
                });

        queue.add(seminarsRequest);
    }

    private void notifyFetchSeminarsFailure() {
        Snackbar.make(seminarListView, "Erro carregando seminários", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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

    private boolean isUserLoggedIn() {
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
