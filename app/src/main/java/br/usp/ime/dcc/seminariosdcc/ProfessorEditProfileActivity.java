package br.usp.ime.dcc.seminariosdcc;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.usp.ime.dcc.seminariosdcc.utils.SeminarsWebService;
import br.usp.ime.dcc.seminariosdcc.utils.UserStore;

public class ProfessorEditProfileActivity extends AppCompatActivity {

    private RequestQueue queue;


    private Button removeButton;
    private Button updateButton;
    private TextInputEditText nuspInput;
    private TextInputEditText nameInput;
    private TextInputEditText passwordInput;

    private boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_edit_profile);

        queue = Volley.newRequestQueue(this);


        initializeViews();
    }

    private void initializeViews() {
        nuspInput = (TextInputEditText) findViewById(R.id.text_input_nusp_professor_edit_profile);
        nameInput = (TextInputEditText) findViewById(R.id.text_input_name_professor_edit_profile);
        passwordInput = (TextInputEditText) findViewById(R.id.text_input_password_professor_edit_profile);
        updateButton = (Button) findViewById(R.id.button_professor_update_profile);
        removeButton = (Button) findViewById(R.id.button_professor_remove_profile);


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

        nuspInput.addTextChangedListener(watcher);
        nameInput.addTextChangedListener(watcher);
        passwordInput.addTextChangedListener(watcher);

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
                nuspInput != null &&
                nuspInput.getText().toString().length() > 0 &&
                nameInput != null &&
                nameInput.getText().toString().length() > 0 &&
                passwordInput != null &&
                passwordInput.getText().toString().length() > 0;
    }

    private void updateButtonsStatus() {
        removeButton.setEnabled(canSubmit());
        updateButton.setEnabled(canSubmit());
    }

    private void notifyUpdateFailure() {
        Snackbar.make(updateButton, "Não foi possível alterar o cadastro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void notifyRemoveFailure() {
        Snackbar.make(removeButton, "Não foi possível remover o cadastro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    void submitUpdate() {
        submitting = true;
        String updateProfileURL = SeminarsWebService.URL + "/teacher/edit";

        StringRequest updateRequest = new StringRequest(
                Request.Method.POST,
                updateProfileURL,
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

                        ProfessorEditProfileActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyUpdateFailure();
                        ProfessorEditProfileActivity.this.submitting = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nusp", nuspInput.getText().toString().trim());
                params.put("name", nameInput.getText().toString().trim());
                params.put("pass", passwordInput.getText().toString().trim());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(updateRequest);
    }

    void submitRemove() {
        submitting = true;
        String removeProfileURL = SeminarsWebService.URL + "/teacher/delete";

        StringRequest removeRequest = new StringRequest(
                Request.Method.POST,
                removeProfileURL,
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

                        ProfessorEditProfileActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyRemoveFailure();
                        ProfessorEditProfileActivity.this.submitting = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nusp", nuspInput.getText().toString().trim());
                params.put("name", nameInput.getText().toString().trim());
                params.put("pass", passwordInput.getText().toString().trim());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(removeRequest);
    }
}
