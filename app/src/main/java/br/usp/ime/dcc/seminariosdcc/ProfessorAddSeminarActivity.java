package br.usp.ime.dcc.seminariosdcc;

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

import br.usp.ime.dcc.seminariosdcc.utils.SeminarsWebService;

public class ProfessorAddSeminarActivity extends AppCompatActivity {

    private RequestQueue queue;

    private Button addButton;
    private TextInputEditText nameInput;

    private boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_add_seminar);

        queue = Volley.newRequestQueue(this);


        initializeViews();
    }

    private void initializeViews() {
        nameInput = (TextInputEditText) findViewById(R.id.text_input_name_seminar);
        addButton = (Button) findViewById(R.id.button_add_seminar);


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


        nameInput.addTextChangedListener(watcher);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    private boolean canSubmit() {
        return  !submitting &&
                nameInput != null &&
                nameInput.getText().toString().length() > 0;
    }

    private void updateButtonStatus() {
        addButton.setEnabled(canSubmit());
    }


    private void notifyAddFailure() {
        Snackbar.make(addButton, "Não foi possível adicionar o seminário", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    void submit() {
        submitting = true;
        String addSeminarURL = SeminarsWebService.URL + "/student/add";

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

                        ProfessorAddSeminarActivity.this.submitting = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notifyAddFailure();
                        ProfessorAddSeminarActivity.this.submitting = false;
                    }
                }) {


            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        queue.add(addSeminarRequest);
    }

}

