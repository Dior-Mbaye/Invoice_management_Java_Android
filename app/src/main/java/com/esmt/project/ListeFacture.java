package com.esmt.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esmt.project.model.User;
import com.esmt.project.view.UserView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListeFacture extends AppCompatActivity {

    private RecyclerView invoiceList;
    private DatabaseReference listeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        listeRef = FirebaseDatabase.getInstance().getReference().child("users");
        invoiceList = findViewById(R.id.list);
        invoiceList.setLayoutManager(new LinearLayoutManager(this));
        ImageView fer_com = (ImageView) findViewById(R.id.fer_com);
        fer_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListeFacture.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(listeRef, User.class)
                .build();

        FirebaseRecyclerAdapter<User, UserView> adapter = new FirebaseRecyclerAdapter<User, UserView>(options) {


            @NonNull
            @Override
            public UserView onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_layout, viewGroup, false);
                return new UserView(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserView holder, int i, @NonNull final User model) {
                holder.userName.setText(model.getNom());
                holder.userLast.setText(model.getPrenom());
                holder.userMail.setText(model.getEmail());

                holder.create_bill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String user_id = getRef(position).getKey();
                        Intent intent = new Intent(ListeFacture.this, FactureActivity.class);
                        intent.putExtra("userLast", model.getNom());
                        startActivity(intent);
                    }
                });
            }
        };

        invoiceList.setAdapter(adapter);
        adapter.startListening();
    }
}