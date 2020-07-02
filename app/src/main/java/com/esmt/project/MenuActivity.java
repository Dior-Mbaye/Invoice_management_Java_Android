package com.esmt.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esmt.project.model.Factu;
import com.esmt.project.prevalent.Prevalent;
import com.esmt.project.view.FactuView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private Query invoiceRef;
    private DatabaseReference invoice_ref_update;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

       /* Bundle bundle = getIntent().getExtras();
        if (bundle== null) {
            UserType = bundle.get("LastName").toString();
        }*/
        userType = getIntent().getExtras().get("LastName").toString();
        System.out.println(userType);

        invoiceRef = FirebaseDatabase.getInstance().getReference().child("Invoices").child(userType).orderByChild("status").equalTo(0);

        Paper.init(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Invoice Management");
        setSupportActionBar(toolbar);



        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentOnlineUser.getNom());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Factu> options =
                new FirebaseRecyclerOptions.Builder<Factu>()
                        .setQuery(invoiceRef, Factu.class)
                        .build();


        FirebaseRecyclerAdapter<Factu, FactuView> adapter = new FirebaseRecyclerAdapter<Factu, FactuView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FactuView holder, int position, @NonNull final Factu model) {
                holder.txtFactuCategory.setText(model.getCategory());
                holder.txtFactuNumber.setText(model.getRef());
                holder.txtFactuPrice.setText(model.getMontant());
                holder.txtDateLimit.setText(model.getDateLimit());
                //Picasso.get().load(model.getImage()).into(holder.imageView);


                //Début


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence sequence[] = new CharSequence[]{
                                "Yes", "No"
                        };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                        builder.setTitle("I pay my bill " + model.getCategory());
                        builder.setItems(sequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {


                                    invoice_ref_update = FirebaseDatabase.getInstance().getReference().child("Invoices").child(model.getUserLast());

                                    HashMap<String, Object> billMap = new HashMap<>();
                                    billMap.put("status", 1);

                                    invoice_ref_update.child(model.getBid()).updateChildren(billMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(MenuActivity.this, "Payment successfully !!!..", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(MenuActivity.this, "Payment unsuccessfully !!!..", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                } else {
                                    builder.setCancelable(true);
                                }
                            }
                        });
                        builder.show();
                    }
                });




                //Fin
            }

            @NonNull
            @Override
            public FactuView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.factu_layout, parent, false);
                FactuView holder = new FactuView(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}