package com.example.btlandroidapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.adapter.GoodsAdapter;
import com.example.btlandroidapi.model.Goods;
import com.example.btlandroidapi.model.SoHDB;
import com.example.btlandroidapi.retrofit.MedicineUtils;
import com.example.btlandroidapi.singleton.CustomerSingleton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingActivity extends AppCompatActivity {

    RecyclerView recycle_view_processing;
    CustomerSingleton customerSingleton;
    MedicineInterface medicineInterface;
    List<SoHDB> soHDBList;
    List<Goods> goodsList;
    List<List<Goods>> lists;
    List<String> stringList;
    GoodsAdapter goodsAdapter;
    ImageView img_processing_back;
    int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);
        customerSingleton = CustomerSingleton.getInstance();
        String makh = customerSingleton.getCustomerId();
        InitWidget();
        medicineInterface = MedicineUtils.getBookService();
        soHDBList = new ArrayList<>();
        goodsList = new ArrayList<>();
        lists = new ArrayList<>(); // Initialize the lists
        stringList = new ArrayList<>(); // Initialize the stringList
        medicineInterface.getHDBIdWithMaNVNull(makh).enqueue(new Callback<List<SoHDB>>() {
            @Override
            public void onResponse(Call<List<SoHDB>> call, Response<List<SoHDB>> response) {
                if(response.isSuccessful()){
                    soHDBList = response.body();
                    sendSequentialAPIRequests();
                }
            }

            @Override
            public void onFailure(Call<List<SoHDB>> call, Throwable t) {

            }
        });

        img_processing_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void InitWidget() {

        recycle_view_processing = findViewById(R.id.recycle_view_processing);
        img_processing_back = findViewById(R.id.img_processing_back);
    }
    private void sendSequentialAPIRequests() {
        if (currentIndex < soHDBList.size()) {
            SoHDB soHDB = soHDBList.get(currentIndex);
            String hdb = soHDB.getSoHDB();
            stringList.add(hdb);

            // Gửi yêu cầu API cho hdb hiện tại
            medicineInterface.getHDBDetailWithMaNVNull(hdb).enqueue(new Callback<List<Goods>>() {
                @Override
                public void onResponse(Call<List<Goods>> call, Response<List<Goods>> response) {
                    if (response.isSuccessful()) {
                        // Xử lý phản hồi
                        goodsList = response.body();
                        lists.add(goodsList);
                        // Tăng biến index để gửi yêu cầu cho hdb tiếp theo
                        currentIndex++;

                        if (currentIndex == soHDBList.size()) {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ProcessingActivity.this);
                            recycle_view_processing.setLayoutManager(layoutManager);
                            goodsAdapter = new GoodsAdapter(ProcessingActivity.this, stringList, lists, R.layout.item_goods, "Đang Xử Lý", Color.RED);
                            recycle_view_processing.setAdapter(goodsAdapter);
                        }
                        // Gọi lại phương thức để gửi yêu cầu cho hdb tiếp theo
                        sendSequentialAPIRequests();
                    }
                }

                @Override
                public void onFailure(Call<List<Goods>> call, Throwable t) {
                    // Xử lý lỗi và có thể thực hiện xử lý nếu cần
                }
            });
        } else {
            // Nếu đã gửi tất cả các yêu cầu, bạn có thể thực hiện các hành động hoặc hiển thị dữ liệu ở đây
        }
    }
}