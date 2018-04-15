package com.example.theon.expensetracker;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class SignupActivity extends AppCompatActivity {

    private Button signupBtn;
    private TextView loginLink;
    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        signupBtn = (Button) findViewById(R.id.signup);
        nameText = (EditText) findViewById(R.id.name);
        emailText =(EditText) findViewById(R.id.email_id);
        passwordText = (EditText) findViewById(R.id.pwd);
        loginLink = (TextView) findViewById(R.id.login);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // returns to teh login activity
                finish();
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        //String name = nameText.getText().toString();
        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d("SignupActivity.class", "Sign up success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    onSignupSuccess();
//                    ArrayList<String> friends=new ArrayList<>();
//                    friends.add(email);
//                    userDB = FirebaseDatabase.getInstance().getReference("users");
//
//                    String id = userDB.push().getKey();
//                    User user = new User(email,fName,lName,friends);
//                    userDB.child(id).setValue(user);user

                    Toast toast = Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.setPadding(20, 20, 20, 20);
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                    toast.show();
                    onSignupSuccess();

//                    startActivity(new Intent(RegisterActivity.this,LandingActivity.class));
                }

                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "User already exists, please login", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.setPadding(20, 20, 20, 20);
                    view.setBackgroundColor(getResources().getColor(R.color.login));
                    toast.show();
                    onSignupFailed();
                }

                progressDialog.dismiss();
            }
        });




        /* Need to add to db here */

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//
//                        onSignupSuccess();
//
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    public void onSignupSuccess() {
        signupBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError("Password length should be minimum 5 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
