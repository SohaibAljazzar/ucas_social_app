package com.example.ucasinsta62;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.ucasinsta62.databinding.ActivityLoginBinding;
//import com.example.ucasinsta62.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView txt_signup;
    LinearLayout linearLayout;

    FirebaseAuth auth;
    //    private  ActivityLoginBinding binding;
    private ValueAnimator valueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_login);

        //animation
        valueAnimator = ValueAnimator.ofInt(0, 50, 0, 0, -50, 0);
        valueAnimator.setDuration(200);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        txt_signup = findViewById(R.id.txt_signup);
        linearLayout = findViewById(R.id.animator);

        auth = FirebaseAuth.getInstance();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int animated_value = (int) animation.getAnimatedValue();
                linearLayout.setTranslationX(animated_value);
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {
                    valueAnimator.start();
                    pd.dismiss();
                } else  {
                    auth.signInWithEmailAndPassword(str_email, str_password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(auth.getCurrentUser().getUid());

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pd.dismiss();
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                pd.dismiss();
                                            }
                                        });
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    public boolean isValidEmail(String email) {
        // Email pattern regex
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check if the email matches the pattern
        return Pattern.matches(emailPattern, email);
    }

    public boolean isValidPassword(String password) {
        // Define your password validation criteria
        int minimumLength = 8; // Minimum length of the password

        // Perform the password validation
        return password.length() >= minimumLength;
    }
}
