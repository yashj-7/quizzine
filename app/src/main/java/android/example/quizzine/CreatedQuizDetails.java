package android.example.quizzine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        quizId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(getIntent().getStringExtra("ID"));
                Toast.makeText(getApplicationContext(), "Code Copied", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = getIntent().getStringExtra("ID");
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                /*Fire!*/
                startActivity(intent);
            }
        });
    }
}