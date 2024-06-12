package com.example.btlandroidapi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.adapter.GoodsDetailAdapter;
import com.example.btlandroidapi.model.Goods;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {

    private Context mContext;
    private List<String> mHDBList; // Danh sách các mã HDB
    private List<List<Goods>> mGoodsList; // Danh sách các danh sách thuốc tương ứng

    private String title;
    int color;
    int layout;
    public GoodsAdapter(Context context, List<String> HDBList, List<List<Goods>> goodsList, int layout, String title, int color) {
        mContext = context;
        mHDBList = HDBList;
        mGoodsList = goodsList;
        this.layout = layout;
        this.title = title;
        this.color = color;
    }

    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        String HDB = mHDBList.get(position);
        holder.tvHDB.setText(HDB);

        // Lấy danh sách thuốc tương ứng với mã HDB hiện tại và đặt Adapter cho RecyclerView con
        List<Goods> goods = mGoodsList.get(position);
        // Tạo một LinearLayoutManager để quản lý việc hiển thị các mục theo chiều dọc
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        // Thiết lập layout manager cho RecyclerView
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.tv_title.setText(title);
        holder.tv_title.setTextColor(color);
        // Tạo một instance của GoodsDetailAdapter với dữ liệu goods và layout item
        GoodsDetailAdapter detailAdapter = new GoodsDetailAdapter(mContext, goods, R.layout.item_goods_detail);
        // Thiết lập adapter cho RecyclerView
        holder.recyclerView.setAdapter(detailAdapter);
    }

    @Override
    public int getItemCount() {
        return mHDBList.size();
    }

    public static class GoodsViewHolder extends RecyclerView.ViewHolder {
        TextView tvHDB, tv_title;
        RecyclerView recyclerView;

        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHDB = itemView.findViewById(R.id.tv_hdb);
            tv_title = itemView.findViewById(R.id.tv_title);
            recyclerView = itemView.findViewById(R.id.recycleview_goods_detail);

        }
    }
}
