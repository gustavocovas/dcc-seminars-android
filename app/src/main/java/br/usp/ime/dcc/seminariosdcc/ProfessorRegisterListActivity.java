package br.usp.ime.dcc.seminariosdcc;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import br.usp.ime.dcc.seminariosdcc.utils.SeminarsWebService;
import br.usp.ime.dcc.seminariosdcc.utils.UserStore;

public class ProfessorRegisterListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RequestQueue requestQueue;
    private UserStore userStore;

    private ListView professorListView;
    private ArrayAdapter professorsAdapter;


    private ArrayList<String> professorNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_register_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestQueue = Volley.newRequestQueue(this);
        userStore = new UserStore(getApplicationContext());

        if (!userStore.isLoggedIn()) {
            redirectToLogin();
        }

        setupProfessorList();
        fetchProfessors();
    }

    private void setupProfessorList() {
        professorListView = (ListView) findViewById(R.id.list_professor_register);
        professorsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, professorNames);
        professorListView.setAdapter(professorsAdapter);


    }

    private void fetchProfessors() {
        String professorsURL = SeminarsWebService.URL + "/teacher";

        StringRequest professorsRequest = new StringRequest(
                Request.Method.GET,
                professorsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseFetchProfessorsResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyFetchProfessorsFailure();
                    }
                });

        requestQueue.add(professorsRequest);
    }

    private void parseFetchProfessorsResponse(String response) {
        boolean wasSuccessful;

        JSONObject responseJSONObject;
        try {
            responseJSONObject = new JSONObject(response);
            wasSuccessful = responseJSONObject.getBoolean("success");

            if (wasSuccessful) {
                professorNames.clear();

                JSONArray seminarsJSONArray = responseJSONObject.getJSONArray("data");
                for (int i = 0; i < seminarsJSONArray.length(); i++) {
                    JSONObject seminar = seminarsJSONArray.getJSONObject(i);
                    professorNames.add(seminar.getString("name"));
                }
                professorsAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            wasSuccessful = false;
        }

        if (!wasSuccessful) {
            notifyFetchProfessorsFailure();
        }
    }

    private void notifyFetchProfessorsFailure() {
        Snackbar.make(professorListView, "Erro carregando professores", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    private void redirectToLogin() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_seminar_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            fetchProfessors();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.signup_professor_button) {
            Intent ProfessorSignUp = new Intent(ProfessorRegisterListActivity.this, ProfessorSignUpActivity.class);
            startActivity(ProfessorSignUp);
        } else if (id == R.id.edit_professor_button) {
            Intent editProfile = new Intent(ProfessorRegisterListActivity.this, ProfessorEditProfileActivity.class);
            startActivity(editProfile);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


