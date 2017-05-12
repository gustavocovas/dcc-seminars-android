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

public class ProfessorRemoveStudentActivity extends AppCompatActivity {


    private RequestQueue queue;
    private UserStore userStore;

    private Button submitButton;
    private TextInputEditText nuspInput;

    private boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_remove_student);

        queue = Volley.newRequestQueue(this);
        userStore = new UserStore(this);

        initializeViews();
    }

    private void initializeViews() {
        nuspInput = (TextInputEditText) findViewById(R.id.text_nusp_student_to_be_removed);


        submitButton = (Button) findViewById(R.id.button_remove_student);


        updateRemoveButtonStatus();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateRemoveButtonStatus();
            }
        };

        nuspInput.addTextChangedListener(watcher);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeStudent();
            }
        });
    }

    private void removeStudent() {
        try {
            final String nusp = userStore.getNusp();

            String studentReadURL = SeminarsWebService.URL + "/student/delete/" + nusp;

            StringRequest studentRemoveRequest = new StringRequest(
                    Request.Method.POST,
                    studentReadURL,
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
                                //toast
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

            queue.add(studentRemoveRequest);
        } catch (IOException e) {
            handleFetchError();
        }
    }

    private void handleFetchError() {
        notifySubmitFailure();
        finish();
    }

    private boolean canSubmit() {
        return  !submitting &&
                nuspInput != null &&
                nuspInput.getText().toString().length() > 0 ;

    }

    private void updateRemoveButtonStatus() {
        submitButton.setEnabled(canSubmit());
    }

    private void notifySubmitFailure() {
        Snackbar.make(submitButton, "Não foi possível remover o cadastro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


}

