package com.esmt.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class PayerActivity extends AppCompatActivity {
    private String categoryName , dateTime, price, ref,num , saveCurrentDate, saveCurrentTime, UserLast;
    private Button btnValider;
    private TextInputEditText date, reference, montant, numero;
    private String billRandomKey;
    Random randomNumber = new Random();
    int number = randomNumber.nextInt();
    private DatabaseReference invoiceRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer);

        Bundle bundle= getIntent().getExtras();
        if (bundle!= null) {// to avoid the NullPointerException
            categoryName = bundle.get("category").toString();
        }


        Bundle bundle2= getIntent().getExtras();
        if (bundle2!= null) {// to avoid the NullPointerException
            UserLast  = bundle2.get("userLast").toString();
        }


         invoiceRef= FirebaseDatabase.getInstance().getReference().child("Invoices").child(UserLast);

        btnValider = (Button) findViewById(R.id.btnValider);
        date = (TextInputEditText) findViewById(R.id.bill_date_limite);
        reference= (TextInputEditText) findViewById(R.id.reference);
        montant = (TextInputEditText) findViewById(R.id.montant);
        numero = (TextInputEditText) findViewById(R.id.Numero);
        loadingBar = new ProgressDialog(this);

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validateInvoiceData();
            }
        });
    }

    private void validateInvoiceData() {
        ref = reference.getText().toString();
        price = montant.getText().toString();
        dateTime = date.getText().toString();
        num = numero.getText().toString();


         if (TextUtils.isEmpty(ref))
        {
            Toast.makeText(this, "Renseigné la reference SVP...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "Renseigné le montant SVP...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dateTime))
        {
            Toast.makeText(this, "Renseigné la date SVP...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(num))
        {
            Toast.makeText(this, "Renseigné le numero SVP...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeInvoiceInformation();
        }
    }

    private void storeInvoiceInformation() {
        loadingBar.setTitle("Ajout de la facture");
        loadingBar.setMessage("Ajout de la facture en cours.Patientez SVP");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        billRandomKey = String.valueOf(number);


        saveInvoiceInfoToDatabase();

    }

    private void saveInvoiceInfoToDatabase() {
        HashMap<String, Object> invoiceMap = new HashMap<>();
        invoiceMap.put("bid", billRandomKey);
        invoiceMap.put("userLast", UserLast);
        invoiceMap.put("date", saveCurrentDate);
        invoiceMap.put("time", saveCurrentTime);
        invoiceMap.put("ref", ref);
        invoiceMap.put("category", categoryName);
        invoiceMap.put("montant", price);
        invoiceMap.put("dateLimit", dateTime);
        invoiceMap.put("numero", num);
       invoiceMap.put("status", 0);



        invoiceRef.child(billRandomKey).updateChildren(invoiceMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(PayerActivity.this, ListeFacture.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(PayerActivity.this, "Successful addition !!!..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(PayerActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}