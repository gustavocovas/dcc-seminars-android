package br.usp.ime.dcc.seminariosdcc.student;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import br.usp.ime.dcc.seminariosdcc.R;
import br.usp.ime.dcc.seminariosdcc.shared.SeminarsWebService;

public class StudentSeminarDetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private TextView seminarNameTextView;
    private Button presenceConfirmationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_seminar_detail);

        requestQueue = Volley.newRequestQueue(this);
        seminarNameTextView = (TextView) findViewById(R.id.text_student_seminar_detail);
        presenceConfirmationButton = (Button) findViewById(R.id.button_student_presence_confirmation);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //cancel
            }
            else {
                //Scanned successfully
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                presenceConfirmationButton.setEnabled(true);
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
