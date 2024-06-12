package com.example.btlandroidapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidapi.viewmodel.CartViewModel;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.Medicine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


    Context context;
    List<Medicine> list;
    int layout;

    List<Integer> quantityList;
    CartViewModel cartViewModel;
    public CartAdapter(Context context, List<Medicine> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
        quantityList = new ArrayList<>();
        cartViewModel = CartViewModel.getInstance(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Medicine medicine = list.get(position);
        holder.tv_name_item_cart.setText(medicine.getTenThuoc());
        holder.tv_mota_item_cart.setText(medicine.getMoTa());
        holder.tv_dongiaban_item_cart.setText(String.valueOf(medicine.getDonGiaBan()) + " / hộp");
        String imageName = medicine.getAnh(); // Lấy tên tệp ảnh từ cơ sở dữ liệu

        String str = imageName;
        String fileName = str.substring(0, str.lastIndexOf("."));
        int imageResource = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());
        Picasso.get()
                .load(imageResource)
                .placeholder(R.drawable.baseline_image_24) // Hình ảnh tạm thời hiển thị trong khi đang tải
                .error(R.drawable.baseline_error_24)
                .into(holder.img_anh_item_cart);
        quantityList.add(position,1);
        holder.img_minus_item_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.tv_soluong_item_cart.getText().toString());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    holder.tv_soluong_item_cart.setText(String.valueOf(currentQuantity));
                    quantityList.set(position, currentQuantity);
                    //Toast.makeText(context, String.valueOf(quantityList.get(position)), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.img_plus_item_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.tv_soluong_item_cart.getText().toString());
                if (currentQuantity < 99) {
                    currentQuantity++;
                    holder.tv_soluong_item_cart.setText(String.valueOf(currentQuantity));
                    quantityList.set(position, currentQuantity);
                    //Toast.makeText(context, String.valueOf(quantityList.get(position)), Toast.LENGTH_SHORT).show();
                }
            }
        });



        holder.img_delete_item_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the item at the current position from the list
                list.remove(position);
                cartViewModel.removeFromCart(position);
                // Notify the adapter that the data has changed
                notifyItemRemoved(position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public List<Integer> getQuantityList() {
        return quantityList;
    }


    public class CartViewHolder extends RecyclerView.ViewHolder{
    public ImageView img_anh_item_cart, img_minus_item_cart, img_plus_item_cart, img_delete_item_cart;
    public TextView tv_mota_item_cart, tv_dongiaban_item_cart, tv_soluong_item_cart, tv_name_item_cart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img_anh_item_cart = itemView.findViewById(R.id.img_anh_item_cart);
            img_minus_item_cart = itemView.findViewById(R.id.img_minus_item_cart);
            img_plus_item_cart = itemView.findViewById(R.id.img_plus_item_cart);
            img_delete_item_cart = itemView.findViewById(R.id.img_delete_item_cart);
            tv_mota_item_cart = itemView.findViewById(R.id.tv_mota_item_cart);
            tv_dongiaban_item_cart = itemView.findViewById(R.id.tv_dongiaban_item_cart);
            tv_soluong_item_cart = itemView.findViewById(R.id.tv_soluong_item_cart);
            tv_name_item_cart = itemView.findViewById(R.id.tv_name_item_cart);
        }
    }
}
