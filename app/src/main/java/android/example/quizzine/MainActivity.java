package android.example.quizzine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button create;
    Button goTo;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button signOut;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference dref = db.collection("users").document("details");

//            dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if(task.isSuccessful()){
//                        DocumentSnapshot doc = task.getResult();
//                        ArrayList<UserDetails> list = (ArrayList<UserDetails>) doc.get("userDetails");
//                        count = list.size()+1;
//                    }else{
//
//                    }
//                }
//            });
            userId = UUID.randomUUID().toString();

            HashMap<String,HashMap<String,String>> mainMap = new HashMap<>();
            String userName = account.getDisplayName();
            String userEmail = account.getEmail();
            String picUrl = "";
            try{
                picUrl = account.getPhotoUrl().toString();
            }catch(NullPointerException npe){
                picUrl = "No image";
            }

            //UserDetails user = new UserDetails(userName,userEmail,picUrl,userId);
            HashMap<String,String> tempMap = new HashMap<>();
            tempMap.put("name",userName);
            tempMap.put("email",userEmail);
            tempMap.put("picture",picUrl);
            tempMap.put("Id",userId);
            mainMap.put("userDetails",tempMap);
            dref.set(mainMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });


//            Log.d("name:",account.getDisplayName());
//            try{
//                Log.d("pictureUrl:",account.getPhotoUrl().toString());
//            }catch(NullPointerException npe){
//                Log.d("messagePic","pic not found");
//            }
        }

        signOut = findViewById(R.id.signOut);
        create = (Button)findViewById(R.id.createQuiz);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddQuestions.class);
                startActivity(i);
            }
        });
        goTo = (Button)findViewById(R.id.gotoQuiz);
        goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(MainActivity.this,quizPage.class);
                startActivity(x);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsc.signOut();
                finish();
                startActivity(new Intent(MainActivity.this,loginActivity.class));
            }
        });
    }
}