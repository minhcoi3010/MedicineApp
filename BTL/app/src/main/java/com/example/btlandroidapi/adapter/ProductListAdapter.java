package com.example.btlandroidapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    Context context;
    List<Medicine> list;
    int layout;

    public ProductListAdapter(Context context, List<Medicine> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ProductListAdapter.ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_buy, parent, false);
        return new ProductListAdapter.ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ProductListViewHolder holder, int position) {
        Medicine medicine = list.get(position);
        holder.tv_mo_ta_list_buy.setText(medicine.getMoTa());
        holder.tv_ten_thuoc_list_buy.setText(medicine.getTenThuoc());
        holder.tv_dongiaban_list_buy.setText(String.valueOf(medicine.getDonGiaBan()) + " / hộp");
        String imageName = medicine.getAnh(); // Lấy tên tệp ảnh từ cơ sở dữ liệu

        String str = imageName;
        String fileName = str.substring(0, str.lastIndexOf("."));
        int imageResource = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());
        Picasso.get()
                .load(imageResource)
                .placeholder(R.drawable.baseline_image_24) // Hình ảnh tạm thời hiển thị trong khi đang tải
                .error(R.drawable.baseline_error_24)
                .into(holder.img_anh_thuoc_list_buy);

        holder.ll_list_view_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Contants.Medicine_Selected, medicine.getMaThuoc());
                intent.putExtra(Contants.Bundel_MaThuoc, bundle);
                context.startActivity(intent);
            }
        });
        holder.btn_mua_list_buy.setOnClickListener(new View.OnClickListener() {
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
        return list.size();
    }
    public class ProductListViewHolder extends RecyclerView.ViewHolder{

        LinearLayout ll_list_view_buy;
        ImageView img_anh_thuoc_list_buy;
        TextView tv_mo_ta_list_buy, tv_dongiaban_list_buy, tv_ten_thuoc_list_buy;
        Button btn_mua_list_buy;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_list_view_buy = itemView.findViewById(R.id.ll_list_view_buy);
            img_anh_thuoc_list_buy = itemView.findViewById(R.id.img_anh_thuoc_list_buy);
            tv_mo_ta_list_buy = itemView.findViewById(R.id.tv_mo_ta_list_buy);
            tv_ten_thuoc_list_buy = itemView.findViewById(R.id.tv_ten_thuoc_list_buy);
            tv_dongiaban_list_buy = itemView.findViewById(R.id.tv_dongiaban_list_buy);
            btn_mua_list_buy = itemView.findViewById(R.id.btn_mua_list_buy);
        }
    }
}
