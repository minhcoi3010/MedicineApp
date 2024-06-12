package com.example.btlandroidapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidapi.contants.Contants;
import com.example.btlandroidapi.activity.DetailProductActivity;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.Medicine;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.BestSellerViewHolder> {

    Context context;
    List<Medicine> medicineList;
    int layout;

    public BestSellerAdapter(Context context, List<Medicine> medicineList, int layout) {
        this.context = context;
        this.medicineList = medicineList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public BestSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_bestseller, parent, false);
        return new BestSellerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.tv_ten_thuoc_main_bestseller.setText(medicine.getTenThuoc());
        holder.tv_mo_ta_main_bestseller.setText(medicine.getMoTa());
        holder.tv_dongiaban_main_bestseller.setText(String.valueOf(medicine.getDonGiaBan()) + " / hộp");
        String imageName = medicine.getAnh(); // Lấy tên tệp ảnh từ cơ sở dữ liệu

        String str = imageName;
        String fileName = str.substring(0, str.lastIndexOf("."));
        int imageResource = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());
        Picasso.get()
                .load(imageResource)
                .placeholder(R.drawable.baseline_image_24) // Hình ảnh tạm thời hiển thị trong khi đang tải
                .error(R.drawable.baseline_error_24)
                .into(holder.img_thuoc_main_bestseller);
        holder.ll_main_bestseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Contants.Medicine_Selected, medicine.getMaThuoc());
                intent.putExtra(Contants.Bundel_MaThuoc, bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class BestSellerViewHolder extends RecyclerView.ViewHolder{

        LinearLayout ll_main_bestseller;
        ImageView img_thuoc_main_bestseller;
        TextView tv_ten_thuoc_main_bestseller, tv_mo_ta_main_bestseller, tv_dongiaban_main_bestseller;
        public BestSellerViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main_bestseller = itemView.findViewById(R.id.ll_main_bestseller);
            img_thuoc_main_bestseller = itemView.findViewById(R.id.img_thuoc_main_bestseller);
            tv_ten_thuoc_main_bestseller = itemView.findViewById(R.id.tv_ten_thuoc_main_bestseller);
            tv_mo_ta_main_bestseller = itemView.findViewById(R.id.tv_mo_ta_main_bestseller);
            tv_dongiaban_main_bestseller = itemView.findViewById(R.id.tv_dongiaban_main_bestseller);
        }
    }
}
