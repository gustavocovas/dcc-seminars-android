package br.usp.ime.dcc.seminariosdcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentSignInSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in_sign_up);

        Button signInButton = (Button) findViewById(R.id.signin_student_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentSignIn = new Intent(StudentSignInSignUpActivity.this, StudentSignInActivity.class);
                startActivity(studentSignIn);
            }
        });

        Button signUpButton = (Button) findViewById(R.id.signup_student_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentSignUp = new Intent(StudentSignInSignUpActivity.this, StudentSignUpActivity.class);
                startActivity(studentSignUp);
            }
        });
    }
}
