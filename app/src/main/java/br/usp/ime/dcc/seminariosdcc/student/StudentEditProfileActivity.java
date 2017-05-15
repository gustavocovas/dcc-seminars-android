package br.usp.ime.dcc.seminariosdcc.student;

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
import br.usp.ime.dcc.seminariosdcc.shared.UserStore;

public class StudentEditProfileActivity extends AppCompatActivity {

    private RequestQueue queue;
    private UserStore userStore;

    private Button submitButton;
    private TextInputEditText nameInput;
    private TextInputEditText passwordInput;

    private boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);

        queue = Volley.newRequestQueue(this);
        userStore = new UserStore(this);

        initializeViews();

        fetchUserData();
    }

    private void initializeViews() {
        nameInput = (TextInputEditText) findViewById(R.id.text_input_name_student_edit_profile);
        passwordInput = (TextInputEditText) findViewById(R.id.text_input_password_student_edit_profile);
        submitButton = (Button) findViewById(R.id.button_student_edit_profile);

        updateSubmitButtonStatus();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSubmitButtonStatus();
            }
        };

        nameInput.addTextChangedListener(watcher);
        passwordInput.addTextChangedListener(watcher);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void fetchUserData() {
        final String nusp = userStore.getNusp();
        final String pass = userStore.getPass();
        String studentReadURL = SeminarsWebService.URL + "/student/get/" + nusp;

        StringRequest studentReadRequest = new StringRequest(
                Request.Method.GET,
                studentReadURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean wasSuccessful;
                        String name = "";

                        try {
                            JSONObject responseJSONObject = new JSONObject(response);
                            wasSuccessful = responseJSONObject.getBoolean("success");
                            name = responseJSONObject.getJSONObject("data").getString("name");
                        } catch (JSONException e) {
                            wasSuccessful = false;
                        }

                        if (wasSuccessful) {
                            nameInput.setText(name);
                            passwordInput.setText(pass);
                        } else {
                            handleFetchError();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleFetchError();
                    }
                }
        );

        queue.add(studentReadRequest);
    }

    private void handleFetchError() {
        notifyFetchFailure();
        finish();
    }

    private boolean canSubmit() {
        return  !submitting &&
                nameInput != null &&
                nameInput.getText().toString().length() > 0 &&
                passwordInput != null &&
                passwordInput.getText().toString().length() > 0;
    }

    private void updateSubmitButtonStatus() {
        submitButton.setEnabled(canSubmit());
    }

    private void notifyFetchFailure() {
        Snackbar.make(submitButton, "Erro carregando dados cadastrais", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void notifySubmitFailure() {
        Snackbar.make(submitButton, "Não foi possível atualizar seu cadastro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    void submit() {
        submitting = true;
        String updateProfileURL = SeminarsWebService.URL + "/student/edit";

        StringRequest signUpRequest = new StringRequest(
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
                            notifySubmitFailure();
                        }

                        StudentEditProfileActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifySubmitFailure();
                        StudentEditProfileActivity.this.submitting = false;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nusp", userStore.getNusp());
                params.put("name", nameInput.getText().toString().trim());
                params.put("pass", passwordInput.getText().toString().trim());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(signUpRequest);
    }
}
