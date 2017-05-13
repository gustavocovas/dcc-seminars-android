package br.usp.ime.dcc.seminariosdcc.professor;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.usp.ime.dcc.seminariosdcc.R;
import br.usp.ime.dcc.seminariosdcc.shared.SeminarsWebService;

public class ProfessorAddAttendenceActivity extends AppCompatActivity {

    private RequestQueue queue;

    private Button addButton;
    private TextInputEditText nuspInput;

    private boolean submitting = false;

    private String seminarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_add_attendence);

        queue = Volley.newRequestQueue(this);
        seminarId = getIntent().getStringExtra("seminar_id");

        initializeViews();
    }

    private void initializeViews() {
        nuspInput = (TextInputEditText) findViewById(R.id.text_input_nusp_attendance);
        addButton = (Button) findViewById(R.id.button_add_attendence);

        updateButtonStatus();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateButtonStatus();
            }
        };

        nuspInput.addTextChangedListener(watcher);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private boolean canSubmit() {
        return  !submitting &&
                nuspInput != null &&
                nuspInput.getText().toString().length() > 0;
    }

    private void updateButtonStatus() {
        addButton.setEnabled(canSubmit());
    }

    void submit() {
        submitting = true;
        String addSeminarURL = SeminarsWebService.URL + "/attendence/submit";

        StringRequest addSeminarRequest = new StringRequest(
                Request.Method.POST,
                addSeminarURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean wasSuccessful;

                        try {
                            JSONObject responseJSONObject = new JSONObject(response);
                            wasSuccessful = responseJSONObject.getBoolean("success");
                        } catch (JSONException e) {
                            wasSuccessful = false;
                        }

                        if (wasSuccessful) {
                            finish();
                        } else {
                            notifyAddFailure();
                        }

                        ProfessorAddAttendenceActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyAddFailure();
                        ProfessorAddAttendenceActivity.this.submitting = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seminar_id", seminarId);
                params.put("nusp", nuspInput.getText().toString().trim());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(addSeminarRequest);
    }

    private void notifyAddFailure() {
        Snackbar.make(addButton, "Não foi possível adicionar o seminário", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
