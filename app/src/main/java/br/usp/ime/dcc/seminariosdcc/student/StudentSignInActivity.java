package br.usp.ime.dcc.seminariosdcc.student;

import android.content.Intent;
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
import br.usp.ime.dcc.seminariosdcc.shared.UserStore;
import br.usp.ime.dcc.seminariosdcc.shared.SeminarsWebService;

public class StudentSignInActivity extends AppCompatActivity {

    private RequestQueue queue;

    private Button signInButton;
    private TextInputEditText nuspInput;
    private TextInputEditText passwordInput;

    private boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in);

        queue = Volley.newRequestQueue(this);

        nuspInput = (TextInputEditText) findViewById(R.id.text_input_nusp_student_sign_in);
        passwordInput = (TextInputEditText) findViewById(R.id.text_input_password_student_sign_in);
        signInButton = (Button) findViewById(R.id.button_student_sign_in);

        updateSignInButtonStatus();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSignInButtonStatus();
            }
        };

        nuspInput.addTextChangedListener(watcher);
        passwordInput.addTextChangedListener(watcher);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private boolean canSubmit() {
        return  !submitting &&
                nuspInput != null &&
                nuspInput.getText().toString().length() > 0 &&
                passwordInput != null &&
                passwordInput.getText().toString().length() > 0;
    }

    private void updateSignInButtonStatus() {
        signInButton.setEnabled(canSubmit());
    }

    private void notifySignInFailure() {
        Snackbar.make(signInButton, "Não foi possível fazer o login", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    void signIn() {
        submitting = true;
        String signInURL = SeminarsWebService.URL + "/login/student";

        StringRequest signInRequest = new StringRequest(
            Request.Method.POST,
            signInURL,
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
                        // Record user data and redirect to main activity;

                        UserStore userStore = new UserStore(getApplicationContext());
                        try {
                            userStore.storeLoginCredentials(
                                    nuspInput.getText().toString(),
                                    passwordInput.getText().toString()
                            );

                            Intent studentSeminars = new Intent(StudentSignInActivity.this, StudentSeminarListActivity.class);
                            studentSeminars.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(studentSeminars);
                            finish();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        notifySignInFailure();
                    }

                    StudentSignInActivity.this.submitting = false;
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    notifySignInFailure();
                    StudentSignInActivity.this.submitting = false;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nusp", nuspInput.getText().toString().trim());
                    params.put("pass", passwordInput.getText().toString().trim());
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }
        };

        queue.add(signInRequest);
    }
}
