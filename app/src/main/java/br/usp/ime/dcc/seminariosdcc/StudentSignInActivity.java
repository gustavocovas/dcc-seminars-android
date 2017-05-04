package br.usp.ime.dcc.seminariosdcc;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

public class StudentSignInActivity extends AppCompatActivity {

    private Button signInButton;
    private TextInputEditText nuspInput;
    private TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in);

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
                login();
            }
        });
    }

    private boolean canSubmit() {
        return  nuspInput != null &&
                nuspInput.getText().toString().length() > 0 &&
                passwordInput != null &&
                passwordInput.getText().toString().length() > 0;
    }

    private void updateSignInButtonStatus() {
        signInButton.setEnabled(canSubmit());
    }


    void login() {

    }

}
