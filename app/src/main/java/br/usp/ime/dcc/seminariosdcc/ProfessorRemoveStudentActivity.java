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

    private Button removeButton;
    private Button seminarsSeenButton;
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


        removeButton = (Button) findViewById(R.id.button_remove_student);
        seminarsSeenButton = (Button) findViewById(R.id.button_seminars_student);


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



        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeStudent();
            }
        });
        seminarsSeenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seminarsStudent();
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
                                notifyRemoveSuccess();
                            } else {
                                handleRemoveFetchError();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleRemoveFetchError();
                        }
                    }
            );

            queue.add(studentRemoveRequest);
        } catch (IOException e) {
            handleRemoveFetchError();
        }
    }

    private void seminarsStudent() {
        try {
            final String nusp = userStore.getNusp();

            String studentSeminarsURL = SeminarsWebService.URL + "/attendence/listSeminars" + nusp;

            StringRequest studentSeminarsRequest = new StringRequest(
                    Request.Method.POST,
                    studentSeminarsURL,
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

                            } else {
                                handleStudentsSeminarsFetchError();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleStudentsSeminarsFetchError();
                        }
                    }
            );

            queue.add(studentSeminarsRequest);
        } catch (IOException e) {
            handleStudentsSeminarsFetchError();
        }
    }

    private void handleRemoveFetchError() {
        notifyRemoveFailure();
        finish();
    }

    private void handleStudentsSeminarsFetchError() {
        notifyStudentSeminarsFailure();
        finish();
    }

    private boolean canSubmit() {
        return  !submitting &&
                nuspInput != null &&
                nuspInput.getText().toString().length() > 0 ;

    }

    private void updateButtonsStatus() {
        removeButton.setEnabled(canSubmit());
        seminarsSeenButton.setEnabled(canSubmit());
    }

    private void notifyRemoveFailure() {
        Snackbar.make(removeButton, "Não foi possível remover o cadastro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void notifyRemoveSuccess() {
        Snackbar.make(removeButton, "Aluno removido com sucesso", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void notifyStudentSeminarsFailure() {
        Snackbar.make(removeButton, "Não foi possível remover o cadastro", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}