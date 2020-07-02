package com.esmt.project.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esmt.project.R;


public class UserView extends RecyclerView.ViewHolder {

    public TextView userName ,userLast, userMail, create_bill;


    public UserView(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.user_name);
        userLast = itemView.findViewById(R.id.lastName);
        userMail = itemView.findViewById(R.id.Mail);
        create_bill = itemView.findViewById(R.id.create_bill);
    }
}