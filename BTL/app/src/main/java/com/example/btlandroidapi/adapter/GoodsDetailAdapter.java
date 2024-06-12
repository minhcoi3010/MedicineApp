package com.example.btlandroidapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.Goods;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GoodsDetailAdapter extends RecyclerView.Adapter<GoodsDetailAdapter.GoodsDetailViewHolder> {

    Context context;
    List<Goods> goodsList;
    int layout;

    public GoodsDetailAdapter(Context context, List<Goods> goodsList, int layout) {
        this.context = context;
        this.goodsList = goodsList;
        this.layout = layout;
    }

    @NonNull
    @Override
    public GoodsDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_detail, parent, false);
        return new GoodsDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsDetailViewHolder holder, int position) {
        Goods goods = goodsList.get(position);
        if(goods != null){
            holder.tv_ten_thuoc_goods_detail.setText("Tên Thuốc : " + goods.getTenThuoc());
            holder.tv_sl_goods_detail.setText("SL : " + String.valueOf(goods.getSLBan()));
            holder.tv_dongiaban_goods_detail.setText("Giá Bán : " + String.valueOf(goods.getDonGiaBan()) + "/ hộp");
            holder.tv_thanhtien_goods_detail.setText("Thành Tiền : " + String.valueOf(goods.getThanhTien()));
            String imageName = goods.getAnh(); // Lấy tên tệp ảnh từ cơ sở dữ liệu

            String str = imageName;
            String fileName = str.substring(0, str.lastIndexOf("."));
            int imageResource = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());
            Picasso.get()
                    .load(imageResource)
                    .placeholder(R.drawable.baseline_image_24) // Hình ảnh tạm thời hiển thị trong khi đang tải
                    .error(R.drawable.baseline_error_24)
                    .into(holder.img_thuoc_goods_detail);

        }
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public class GoodsDetailViewHolder extends RecyclerView.ViewHolder{

        TextView tv_ten_thuoc_goods_detail, tv_sl_goods_detail,tv_dongiaban_goods_detail,tv_thanhtien_goods_detail;
        ImageView img_thuoc_goods_detail;
        public GoodsDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten_thuoc_goods_detail = itemView.findViewById(R.id.tv_ten_thuoc_goods_detail);
            tv_sl_goods_detail = itemView.findViewById(R.id.tv_sl_goods_detail);
            tv_dongiaban_goods_detail = itemView.findViewById(R.id.tv_dongiaban_goods_detail);
            tv_thanhtien_goods_detail = itemView.findViewById(R.id.tv_thanhtien_goods_detail);
            img_thuoc_goods_detail = itemView.findViewById(R.id.img_thuoc_goods_detail);
        }
    }
}
