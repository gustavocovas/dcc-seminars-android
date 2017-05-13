package br.usp.ime.dcc.seminariosdcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ProfessorOptions extends AppCompatActivity {


    private void redirectToLogin() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_options);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Button seminarsButton = (Button) findViewById(R.id.seminars_button);
        seminarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent professorSeminarList = new Intent(ProfessorOptions.this, ProfessorSeminarListActivity.class);
                startActivity(professorSeminarList);
            }
        });

        Button professorsButton = (Button) findViewById(R.id.professors_button);
        professorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent professorRegisterList = new Intent(ProfessorOptions.this, ProfessorRegisterListActivity.class);
                startActivity(professorRegisterList);
            }
        });

        Button removestudentButton = (Button) findViewById(R.id.remove_student_button);
        removestudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent professorRemoveStudent = new Intent(ProfessorOptions.this, ProfessorRemoveStudentActivity.class);
                startActivity(professorRemoveStudent);
            }
        });

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToLogin();
            }
        });
    }
}
