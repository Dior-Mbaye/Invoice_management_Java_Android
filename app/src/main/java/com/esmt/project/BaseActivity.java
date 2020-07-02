package com.esmt.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    TextInputEditText txtNom, txtPrenom, txtEmail, txtPassword;
    Button btnAdd;

    FirebaseDatabase rootNode;

    public Boolean validateNom() {
        String val = txtNom.getText().toString();
        if(val.isEmpty()){
            txtNom.setError("Field cannot be empty");
            return false;
        }else{
            txtNom.setError(null);
            return true;
        }
    }

    public Boolean validatePrenom() {
        String val = txtPrenom.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if(val.isEmpty()){
            txtPrenom.setError("Field cannot be empty");
            return false;
        }else if (val.length()>=15){
            txtPrenom.setError("Prenom est long");
            return false;
        }else if(!val.matches(noWhiteSpace)){
            txtPrenom.setError("No space");
            return false;
        }else{
            txtPrenom.setError(null);
            return true;
        }
    }

    public Boolean validateEmail() {
        String val = txtEmail.getText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            txtEmail.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(emailPattern)){
            txtEmail.setError("Invalid email address");
            return false;
        }else{
            txtEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = txtPassword.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if(val.isEmpty()){
            txtPassword.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(passwordVal)){
            txtPassword.setError("\n" + "The password should contain at least one uppercase letter, as well as lowercase letters, numbers and special characters  ");
            return false;
        }else{
            txtPassword.setError(null);
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        txtNom = (TextInputEditText) findViewById(R.id.txtNomBD);
        txtPrenom = (TextInputEditText) findViewById(R.id.txtPrenomBD);
        txtEmail = (TextInputEditText) findViewById(R.id.txtEmailBD);
        txtPassword = (TextInputEditText) findViewById(R.id.txtPasswordBD);

        btnAdd = findViewById(R.id.btnAddUser);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(); }
        });

        }

    private void createAccount() {
        final DatabaseReference reference;

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        if(!validateNom() | !validatePrenom() | !validateEmail() | !validatePassword()){
            return;
        }
        //get all the values
        String nom = txtNom.getText().toString();
        String prenom = txtPrenom.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();


        UserHelperClass helperClass = new UserHelperClass(nom, prenom, email, password);
        reference.child(nom).setValue(helperClass);

        Intent inscriptionIntent = new Intent(BaseActivity.this, WelcomeActivity.class);
        startActivity(inscriptionIntent);

    }
}