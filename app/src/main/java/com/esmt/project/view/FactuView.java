package com.esmt.project.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.esmt.project.R;
import com.esmt.project.interfaces.ItemClickListner;

public class FactuView extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtFactuCategory, txtFactuNumber, txtFactuPrice,txtDateLimit;
    public ImageView imageView;
    public ItemClickListner listner;

    public FactuView(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.imageInvoice);
        txtFactuCategory= (TextView) itemView.findViewById(R.id.nameInvoice);
        txtFactuNumber = (TextView) itemView.findViewById(R.id.referenceInvoice);
        txtDateLimit = (TextView) itemView.findViewById(R.id.dateLimiteInvoice);
        txtFactuPrice = (TextView) itemView.findViewById(R.id.priceInvoice);
    }
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
