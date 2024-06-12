package com.example.btlandroidapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidapi.contants.Contants;
import com.example.btlandroidapi.activity.ProductListActivity;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    List<Product> list;

    int layout;

    public ProductAdapter(Context context, List<Product> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
        holder.tv_product.setText(product.getTenLoai());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = list.get(holder.getAdapterPosition()).getMaLoai()+"-"+list.get(holder.getAdapterPosition()).getTenLoai();
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra(Contants.Ma_Loai, productId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView tv_product;
        LinearLayout linearLayout;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_product = itemView.findViewById(R.id.tv_product);
            linearLayout = itemView.findViewById(R.id.linearLayout_product);
        }
    }
}
