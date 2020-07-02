package com.esmt.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FactureActivity extends AppCompatActivity {
    private TextView eau,senelec,canal,wifi;
    private ImageView ff;
    private String UserLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facture);


        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            UserLast = bundle.get("userLast").toString();
        }

        eau = (TextView) findViewById(R.id.eau);
         senelec = (TextView) findViewById(R.id.senelec);
        canal = (TextView) findViewById(R.id.canal);
        wifi = (TextView) findViewById(R.id.wifi);

        ff = (ImageView) findViewById(R.id.ff);

        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FactureActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        eau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FactureActivity.this, PayerActivity.class);
                intent.putExtra("category", "eau");
                intent.putExtra("userLast", UserLast);
                startActivity(intent);
            }
        });


        senelec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FactureActivity.this, PayerActivity.class);
                intent.putExtra("category", "senelec");
                intent.putExtra("userLast", UserLast);
                startActivity(intent);
            }
        });


        canal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FactureActivity.this, PayerActivity.class);
                intent.putExtra("category", "canal");
                intent.putExtra("userLast", UserLast);
                startActivity(intent);
            }
        });


        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FactureActivity.this, PayerActivity.class);
                intent.putExtra("category", "wifi");
                intent.putExtra("userLast", UserLast);
                startActivity(intent);
            }
        });

    }
}