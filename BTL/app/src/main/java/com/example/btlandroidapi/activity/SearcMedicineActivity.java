package com.example.btlandroidapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

public class SearcMedicineActivity extends AppCompatActivity {

    EditText edt_search_medicine;
    ImageView img_search_medicine, img_back_product_list;
    List<Medicine> list;
    ProductListAdapter productListAdapter;
    RecyclerView recycleview_search_medicine;
    MedicineInterface medicineInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searc_medicine);
        medicineInterface = MedicineUtils.getBookService();

        InitWidget();
        recycleview_search_medicine.setLayoutManager(new GridLayoutManager(this, 2));
        img_search_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenthuoc = edt_search_medicine.getText().toString();
                medicineInterface.getThuocByTenThuoc(tenthuoc).enqueue(new Callback<ArrayList<Medicine>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Medicine>> call, Response<ArrayList<Medicine>> response) {
                        if(response.isSuccessful()){
                            list = response.body();
                            productListAdapter = new ProductListAdapter(SearcMedicineActivity.this, list, R.layout.item_listview_buy);
                            recycleview_search_medicine.setAdapter(productListAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Medicine>> call, Throwable t) {

                    }
                });
            }
        });
        img_back_product_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearcMedicineActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void InitWidget() {
        edt_search_medicine = findViewById(R.id.edt_search_medicine);
        img_search_medicine = findViewById(R.id.img_search_medicine);
        img_back_product_list = findViewById(R.id.img_back_product_list);
        recycleview_search_medicine = findViewById(R.id.recycleview_search_medicine);
    }
}