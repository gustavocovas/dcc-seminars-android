package br.usp.ime.dcc.seminariosdcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button studentButton = (Button) findViewById(R.id.student_button);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentSignInSignUp = new Intent(LoginActivity.this, StudentSignInActivity.class);
                startActivity(studentSignInSignUp);
            }
        });

        Button signUpButton = (Button) findViewById(R.id.register_student_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentSignUp = new Intent(LoginActivity.this, StudentSignUpActivity.class);
                startActivity(studentSignUp);
            }
        });

        Button professorButton = (Button) findViewById(R.id.professor_button);
        professorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent professorSignIn = new Intent(LoginActivity.this, ProfessorSignInActivity.class);
                startActivity(professorSignIn);
            }
        });
    }
}
