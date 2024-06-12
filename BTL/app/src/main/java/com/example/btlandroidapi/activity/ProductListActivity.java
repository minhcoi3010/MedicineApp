package com.example.btlandroidapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btlandroidapi.contants.Contants;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.adapter.ProductListAdapter;
import com.example.btlandroidapi.model.Medicine;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    ImageView img_back_product_list;
    RecyclerView recycleview_product_list;
    List<Medicine> list;

    TextView tv_product_list_title;
    ProductListAdapter productListAdapter;

    MedicineInterface medicineInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Intent intent = getIntent();
        String productId = intent.getStringExtra(Contants.Ma_Loai);
        String[] parts = productId.split("-");


        String partA = parts[0];
        String partB = parts[1];
        InitWidget();
        tv_product_list_title.setText(partB);
        recycleview_product_list.setLayoutManager(new GridLayoutManager(this, 2));
        medicineInterface = MedicineUtils.getBookService();
        medicineInterface.getThuocByMaLoai(partA).enqueue(new Callback<ArrayList<Medicine>>() {
            @Override
            public void onResponse(Call<ArrayList<Medicine>> call, Response<ArrayList<Medicine>> response) {
                list = response.body();
                // Set up your RecyclerView adapter
                productListAdapter = new ProductListAdapter(ProductListActivity.this, list, R.layout.item_listview_buy);
                recycleview_product_list.setAdapter(productListAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Medicine>> call, Throwable t) {

            }
        });

        img_back_product_list.setOnClickListener(new View.OnClickListener() {
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
        img_back_product_list = findViewById(R.id.img_back_product_list);
        tv_product_list_title = findViewById(R.id.tv_product_list_title);
        recycleview_product_list = findViewById(R.id.recycleview_product_list);
        list = new ArrayList<>();
    }
}