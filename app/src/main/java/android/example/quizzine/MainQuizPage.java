package android.example.quizzine;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class MainQuizPage extends AppCompatActivity {

    TextView questionView;
    RadioGroup radioGroup;
    RadioButton option1;
    RadioButton option2;
    RadioButton option3;
    RadioButton option4;
    Button next;
    CountDownTimer countDownTimer;
    int timerVal = 0;
    ProgressBar progressBar;
    boolean flag = false;

    //to prevent user from going back to previous screen by pressing back button
    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz_page);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);



        questionView = (TextView) findViewById(R.id.questTxtView);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);
        next = (Button) findViewById(R.id.next);

        String[] correctAns = new String[1];
        correctAns[0] = "";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dref = db.collection("quizzes").document(getIntent().getStringExtra("qId"));

        //reading the data
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //the call will return a list of HashMaps containing the documents
                        List<HashMap<String,String>> list = (List<HashMap<String, String>>) document.get("questions");
                        Log.d("lists:",""+list);

                        int[] i = new int[1];
                        i[0] = 0;
                        int[] count = new int[1];

                       questionView.setText(list.get(0).get("question"));
                       option1.setText(list.get(0).get("option1"));
                        option2.setText(list.get(0).get("option2"));
                        option3.setText(list.get(0).get("option3"));
                        option4.setText(list.get(0).get("option4"));
                        correctAns[0] = list.get(0).get("correct");

                        //setting uo a countdown timer and patching it up with the progress bar at thee top
                        countDownTimer = new CountDownTimer(20000,1000) {
                            @Override
                            public void onTick(long l) {
                                timerVal++;
                                progressBar.setProgress((int)timerVal*100/(20000/1000));
                            }

                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void onFinish() {
                                next.performClick();
                            }
                        };
                        countDownTimer.start();

                        i[0]++;

                        next.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void onClick(View view) {

                                countDownTimer.cancel();
                                timerVal = 0;
                                progressBar.setProgress(0);

                                //countDownTimer.onFinish();
                                if(option1.isChecked()){
                                    if(option1.getText().equals(correctAns[0])){
                                        count[0]++;
                                    }

                                }else if(option2.isChecked()){
                                    if(option2.getText().equals(correctAns[0])){
                                        count[0]++;
                                    }
                                    //option2.setChecked(false);
                                }else if(option3.isChecked()){
                                    if(option3.getText().equals(correctAns[0])){
                                        count[0]++;
                                    }
                                    //option3.setChecked(false);
                                }else if(option4.isChecked()){
                                    if(option4.getText().equals(correctAns[0])){
                                        count[0]++;
                                    }
                                    //option4.setChecked(false);
                                }else{
                                    if(timerVal!=0) {
                                        Toast.makeText(MainQuizPage.this, "Choose an option", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                radioGroup.clearCheck();
                                if(i[0] ==list.size()-1){
                                    next.setText("submit");
                                }
                                if(i[0]==list.size()){
                                    Toast.makeText(MainQuizPage.this, ""+count[0], Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainQuizPage.this,MainActivity.class));
                                }
                                questionView.setText(list.get(i[0]).get("question"));
                                option1.setText(list.get(i[0]).get("option1"));
                                option2.setText(list.get(i[0]).get("option2"));
                                option3.setText(list.get(i[0]).get("option3"));
                                option4.setText(list.get(i[0]).get("option4"));
                                correctAns[0] = list.get(i[0]).get("correct");

                                //resetting the counter and progress bar for next question
                               countDownTimer.start();
                               //progressBar.setProgress();

                                i[0]++;
                            }
                        });


                        //Log.d("doc snap", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("error12", "No such document");
                    }
                }else{
                    Log.d("failure",""+task.getException());
                }
            }
        });
    }
}