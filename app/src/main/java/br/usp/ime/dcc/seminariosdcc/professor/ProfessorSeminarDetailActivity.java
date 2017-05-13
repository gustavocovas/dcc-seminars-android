package br.usp.ime.dcc.seminariosdcc.professor;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.usp.ime.dcc.seminariosdcc.R;
import br.usp.ime.dcc.seminariosdcc.shared.SeminarsWebService;

public class ProfessorSeminarDetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private TextView seminarNameTextView;
    private Button generateQRCodeButton;
    private Button manualRegisterStudentButton;

    private ListView attendencesListView;
    private ArrayAdapter attendencesAdapter;

    private ArrayList<String> attendencesNusps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_seminar_detail);

        requestQueue = Volley.newRequestQueue(this);
        seminarNameTextView = (TextView) findViewById(R.id.text_professor_seminar_detail);
        generateQRCodeButton = (Button) findViewById(R.id.button_professor_generate_QRCode);
        manualRegisterStudentButton = (Button) findViewById(R.id.button_manual_register);

        setupSeminarsList();

        Intent intent = getIntent();
        String id = intent.getStringExtra("seminar.id");

        fetchSeminar(id);
        fetchAttendences(id);
    }

    private void setupSeminarsList() {
        attendencesListView = (ListView) findViewById(R.id.list_attendence);
        attendencesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, attendencesNusps);
        attendencesListView.setAdapter(attendencesAdapter);
    }

    private void fetchSeminar(String id) {
        String seminarsURL = SeminarsWebService.URL + "/seminar/get/" + id;

        StringRequest seminarsRequest = new StringRequest(
                Request.Method.GET,
                seminarsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseFetchSeminarResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyFetchSeminarFailure();
                    }
                });

        requestQueue.add(seminarsRequest);
    }

    private void fetchAttendences(final String seminar_id) {
        String seminarsURL = SeminarsWebService.URL + "/attendence/listStudents";

        StringRequest seminarsRequest = new StringRequest(
                Request.Method.POST,
                seminarsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseFetchAttendencesResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyFetchAttendencesFailure();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seminar_id", seminar_id);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        requestQueue.add(seminarsRequest);
    }

    private void parseFetchSeminarResponse(String response) {
        boolean wasSuccessful;

        JSONObject responseJSONObject;
        try {
            responseJSONObject = new JSONObject(response);
            wasSuccessful = responseJSONObject.getBoolean("success");

            if (wasSuccessful) {
                JSONObject seminar = responseJSONObject.getJSONObject("data");
                String seminarName = seminar.getString("name");
                seminarNameTextView.setText(seminarName);
                generateQRCodeButton.setEnabled(true);
                manualRegisterStudentButton.setEnabled(true);
            }
        } catch (JSONException e) {
            wasSuccessful = false;
        }

        if (!wasSuccessful) {
            notifyFetchSeminarFailure();
            finish();
        }
    }

    private void notifyFetchSeminarFailure() {
        Snackbar.make(seminarNameTextView, "Erro carregando seminário", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void parseFetchAttendencesResponse(String response) {
        boolean wasSuccessful;

        JSONObject responseJSONObject;
        try {
            responseJSONObject = new JSONObject(response);
            wasSuccessful = responseJSONObject.getBoolean("success");

            if (wasSuccessful) {
                attendencesNusps.clear();

                JSONArray seminarsJSONArray = responseJSONObject.getJSONArray("data");
                for (int i = 0; i < seminarsJSONArray.length(); i++) {
                    JSONObject seminar = seminarsJSONArray.getJSONObject(i);
                    attendencesNusps.add(seminar.getString("student_nusp"));
                }
                attendencesAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            wasSuccessful = false;
        }

        if (!wasSuccessful) {
            notifyFetchAttendencesFailure();
        }
    }

    private void notifyFetchAttendencesFailure() {
        Snackbar.make(seminarNameTextView, "Erro carregando presenças", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
