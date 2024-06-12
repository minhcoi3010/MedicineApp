package com.example.btlandroidapi.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.btlandroidapi.activity.MainActivity;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.adapter.ProductAdapter;
import com.example.btlandroidapi.model.Product;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    MedicineInterface medicineInterface;
    List<Product> list = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView img_back_product;
    ProductAdapter productAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_product, container, false);

        medicineInterface = MedicineUtils.getBookService();
        recyclerView = rootview.findViewById(R.id.recycleview_product);
        img_back_product = rootview.findViewById(R.id.img_back_product);
        recyclerView.setHasFixedSize(true);

        // Sử dụng GridLayoutManager với 2 cột
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        medicineInterface.getAllTenThuoc().enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    productAdapter = new ProductAdapter(getContext(), list, R.layout.item_product);
                    recyclerView.setAdapter(productAdapter);
                    //Toast.makeText(getContext(), "Thêm Sản Phẩm Thành Công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });

        img_back_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return rootview;
    }
}