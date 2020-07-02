package com.esmt.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esmt.project.prevalent.Prevalent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectActivity extends AppCompatActivity {
    TextInputEditText txtLogin, txtPassword;
    private ProgressDialog dialog;
    private Button btnConnect;
    private  TextView forgetPwd;
    private ProgressDialog loadingBar;
    private TextView adminLink, notAdminLink;
    private String parentDbName = "users";

    public Boolean validateLogin() {
        String val = txtLogin.getText().toString();
        if(val.isEmpty()){
            txtLogin.setError("Field cannot be empty");
            return false;
        }else{
            txtLogin.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = txtPassword.getText().toString();
        if(val.isEmpty()){
            txtPassword.setError("Field cannot be empty");
            return false;
        }else{
            txtPassword.setError(null);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        txtLogin = (TextInputEditText) findViewById(R.id.txtLogin);
        txtPassword = (TextInputEditText) findViewById(R.id.txtPassword);
         forgetPwd = (TextView) findViewById(R.id.forgetPassword);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);

        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConnectActivity.this, "Please re-register", Toast.LENGTH_SHORT).show();
            }
        });


        btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConnect.setText("Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConnect.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "users";
            }
        });



        }
    private void loginUser()
    {
        if (!validateLogin() | !validatePassword()) {
            return;
        } else {
            isLogin();
        }
    }

    private void isLogin() {
        final String loginEntered = txtLogin.getText().toString().trim();
        final String passwordEntered = txtPassword.getText().toString().trim();


        final DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(loginEntered).exists()) {
                    UserHelperClass userDate = dataSnapshot.child(parentDbName).child(loginEntered).getValue(UserHelperClass.class);
                    if (userDate.getNom().equals(loginEntered)) {
                        if (userDate.getPassword().equals(passwordEntered)) {
                            if (parentDbName == "Admins"){
                                Toast.makeText(ConnectActivity.this, "Welcome ...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(ConnectActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userDate;
                                startActivity(intent);
                            }else if (parentDbName == "users"){
                                Toast.makeText(ConnectActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Prevalent.currentOnlineUser = userDate;
                                Intent intent = new Intent(ConnectActivity.this, MenuActivity.class);
                                intent.putExtra("LastName", loginEntered);
                                startActivity(intent);
                            }
                        }
                    }
                } else {
                    Toast.makeText(ConnectActivity.this, "This account does not exist", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConnectActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}



