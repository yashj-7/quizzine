package android.example.quizzine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreatedQuizDetails extends AppCompatActivity {

    TextView quizId;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_quiz_details);

        quizId = (TextView) findViewById(R.id.quizId);
        home = (Button) findViewById(R.id.homeBtn);

        quizId.setText(quizId.getText().toString()+" "+getIntent().getStringExtra("ID"));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreatedQuizDetails.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}