package br.usp.ime.dcc.seminariosdcc.professor;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import br.usp.ime.dcc.seminariosdcc.R;
import br.usp.ime.dcc.seminariosdcc.shared.SeminarsWebService;


public class ProfessorEditSeminarActivity extends AppCompatActivity {

    private RequestQueue queue;

    private Button removeButton;
    private Button updateButton;
    private TextInputEditText idInput;
    private TextInputEditText nameInput;

    private boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_edit_seminar);

        queue = Volley.newRequestQueue(this);


        initializeViews();
    }

    private void initializeViews() {
        idInput = (TextInputEditText) findViewById(R.id.text_input_id_seminar);
        nameInput = (TextInputEditText) findViewById(R.id.text_input_name_seminar);
        removeButton = (Button) findViewById(R.id.button_remove_seminar);
        updateButton = (Button) findViewById(R.id.button_update_seminar);


        updateButtonsStatus();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateButtonsStatus();
            }
        };

        idInput.addTextChangedListener(watcher);
        nameInput.addTextChangedListener(watcher);


        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRemove();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUpdate();
            }
        });
    }

    private boolean canSubmit() {
        return  !submitting &&
                idInput != null &&
                idInput.getText().toString().length() > 0 &&
                nameInput != null &&
                nameInput.getText().toString().length() > 0;
    }

    private void updateButtonsStatus() {
        removeButton.setEnabled(canSubmit());
        updateButton.setEnabled(canSubmit());
    }


    private void notifyRemoveFailure() {
        Snackbar.make(removeButton, "Não foi possível remover o seminário", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void notifyUpdateFailure() {
        Snackbar.make(updateButton, "Não foi possível alterar o seminário", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    void submitRemove() {
        submitting = true;
        String removeSeminarURL = SeminarsWebService.URL + "/student/remove";

        StringRequest removeSeminarRequest = new StringRequest(
                Request.Method.POST,
                removeSeminarURL,
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
                            notifyRemoveFailure();
                        }

                        ProfessorEditSeminarActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyRemoveFailure();
                        ProfessorEditSeminarActivity.this.submitting = false;
                    }
                }) {


            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(removeSeminarRequest);
    }

    void submitUpdate() {
        submitting = true;
        String updateSeminarURL = SeminarsWebService.URL + "/student/update";

        StringRequest updateSeminarRequest = new StringRequest(
                Request.Method.POST,
                updateSeminarURL,
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
                            notifyUpdateFailure();
                        }

                        ProfessorEditSeminarActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyUpdateFailure();
                        ProfessorEditSeminarActivity.this.submitting = false;
                    }
                }) {


            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(updateSeminarRequest);
    }

}
