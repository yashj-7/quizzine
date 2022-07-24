package android.example.quizzine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddQuestions extends AppCompatActivity {

    EditText ques;
    EditText opt1;
    EditText opt2;
    EditText opt3;
    EditText opt4;
    EditText ans;
    Button add;
    Button end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);

        ques = (EditText)findViewById(R.id.ques);
        opt1 = (EditText)findViewById(R.id.opt1);
        opt2 = (EditText)findViewById(R.id.opt2);
        opt3 = (EditText)findViewById(R.id.opt3);
        opt4 = (EditText)findViewById(R.id.opt4);
        ans = (EditText)findViewById(R.id.ans);
        add = (Button)findViewById(R.id.add);
        end = (Button)findViewById(R.id.end);

        //generating a random code for every quiz.
        UUID x = UUID.randomUUID();

        FirebaseFirestore db= FirebaseFirestore.getInstance();

        DocumentReference dref = db.collection("quizzes").document("quiz1");

        DocumentReference dref2 = db.collection("quizzes").document(x.toString());
        int[] i = new int[1];
        i[0] = 1;

        ArrayList<HashMap<String,String>> aList = new ArrayList<>();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //writing data

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("question"+i[0],ques.getText().toString());
                hashMap.put("option1",opt1.getText().toString());
                hashMap.put("option2",opt2.getText().toString());
                hashMap.put("option3",opt3.getText().toString());
                hashMap.put("option4",opt4.getText().toString());
                hashMap.put("correct",ans.getText().toString());

                aList.add(new HashMap<>(hashMap));
//                HashMap<String,String> map = new HashMap<>();
//                map.put("question","what is your fav food");
//                map.put("option1","opt1");
//                map.put("option2","opt2");


                ques.setText("Enter question");
                opt1.setText("enter option1");
                opt2.setText("enter option2");
                opt3.setText("enter option3");
                opt4.setText("enter option4");
                ans.setText("enter correct option");
                i[0]++;
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, ArrayList<HashMap<String,String>>> mainHm = new HashMap<>();
                mainHm.put("questions",aList);
                //aList.add(map);
                dref2.set(mainHm).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("mess","working add");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("failure","failed");
                    }
                });

                Intent it = new Intent(AddQuestions.this,CreatedQuizDetails.class);
                it.putExtra("ID",x.toString());
                startActivity(it);
            }
        });

//        //reading the data
//        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//
//                        List<HashMap<String,String>> list = (List<HashMap<String, String>>) document.get("questions");
//
//                        Log.d("list",""+list.get(0).get("question"));
//                        Log.d("list",""+list.get(0).get("option1"));
//
//                        //Log.d("doc snap", "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d("error12", "No such document");
//                    }
//                }else{
//                    Log.d("failure",""+task.getException());
//                }
//            }
//        });

    }
}