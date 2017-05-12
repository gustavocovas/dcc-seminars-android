package br.usp.ime.dcc.seminariosdcc;

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

import br.usp.ime.dcc.seminariosdcc.utils.SeminarsWebService;

public class ProfessorSignUpActivity extends AppCompatActivity {

    private RequestQueue queue;

    private Button signUpButton;
    private TextInputEditText nuspInput;
    private TextInputEditText nameInput;
    private TextInputEditText passwordInput;

    private boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_sign_up);

        queue = Volley.newRequestQueue(this);

        nuspInput = (TextInputEditText) findViewById(R.id.text_input_nusp_professor_sign_up);
        nameInput = (TextInputEditText) findViewById(R.id.text_input_name_professor_sign_up);
        passwordInput = (TextInputEditText) findViewById(R.id.text_input_password_professor_sign_up);
        signUpButton = (Button) findViewById(R.id.button_professor_sign_up);

        updateSignUpButtonStatus();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateSignUpButtonStatus();
            }
        };

        nuspInput.addTextChangedListener(watcher);
        nameInput.addTextChangedListener(watcher);
        passwordInput.addTextChangedListener(watcher);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
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

    private void updateSignUpButtonStatus() {
        signUpButton.setEnabled(canSubmit());
    }

    private void notifySignUpFailure() {
        Snackbar.make(signUpButton, "Não foi possível fazer o cadastro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void notifySignUpSucces() {
        Snackbar.make(signUpButton, "Cadastro Realizado com Sucesso", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    void signUp() {
        submitting = true;
        String signUpURL = SeminarsWebService.URL + "/teacher/add";

        StringRequest signUpRequest = new StringRequest(
                Request.Method.POST,
                signUpURL,
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
                            notifySignUpSucces();
                        } else {
                            notifySignUpFailure();
                        }

                        ProfessorSignUpActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifySignUpFailure();
                        ProfessorSignUpActivity.this.submitting = false;
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

        queue.add(signUpRequest);
    }
}
