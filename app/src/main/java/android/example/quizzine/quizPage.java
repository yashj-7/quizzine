package android.example.quizzine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class quizPage extends AppCompatActivity {

    TextView quizId;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        quizId = (TextView)findViewById(R.id.ID);
        start = (Button)findViewById(R.id.start);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        start.setOnClickListener(new View.OnClickListener() {

            //to check if the quiz with given id exists or not
            @Override
            public void onClick(View view) {
                db.collection("quizzes").document(quizId.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(quizPage.this, "quiz exists", Toast.LENGTH_SHORT).show();
                                Intent y = new Intent(quizPage.this,MainQuizPage.class);
                                y.putExtra("qId",quizId.getText().toString());
                                startActivity(y);
                            } else {
                                Toast.makeText(quizPage.this, "No quiz exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("failure", "Failed with: ", task.getException());
                        }
                    }
                });
            }
        });

    }
}