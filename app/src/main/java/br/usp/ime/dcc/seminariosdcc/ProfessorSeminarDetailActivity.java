package br.usp.ime.dcc.seminariosdcc;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.usp.ime.dcc.seminariosdcc.utils.SeminarsWebService;

public class ProfessorSeminarDetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private TextView seminarNameTextView;
    private Button generateQRCodeButton;
    private Button manualRegisterStudentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_seminar_detail);

        requestQueue = Volley.newRequestQueue(this);
        seminarNameTextView = (TextView) findViewById(R.id.text_professor_seminar_detail);
        generateQRCodeButton = (Button) findViewById(R.id.button_professor_generate_QRCode);
        manualRegisterStudentButton = (Button) findViewById(R.id.button_manual_register);

        Intent intent = getIntent();
        String id = intent.getStringExtra("seminar.id");

        fetchSeminar(id);
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
}
