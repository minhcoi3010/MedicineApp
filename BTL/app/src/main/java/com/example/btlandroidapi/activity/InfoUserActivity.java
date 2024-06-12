package com.example.btlandroidapi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btlandroidapi.singleton.CustomerSingleton;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.KhachHang;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoUserActivity extends AppCompatActivity {

    TextView tv_infouser_name,tv_infouser_gender,tv_infouser_date,tv_infouser_address,tv_infouser_phonenumber;
    ImageView img_back_info_user;
    Button btn_update_khachhang;
    MedicineInterface medicineInterface;
    CustomerSingleton customerSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        customerSingleton = CustomerSingleton.getInstance();
        InitWidget();
        String makh = customerSingleton.getCustomerId();
        medicineInterface = MedicineUtils.getBookService();
        medicineInterface.getThongTinKhachHang(makh).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if(response.isSuccessful()){
                    KhachHang khachHang = response.body();
                    tv_infouser_name.setText(khachHang.getTenKH());
                    tv_infouser_gender.setText(khachHang.getGioiTinh());
                    tv_infouser_date.setText(khachHang.getNgaySinh());
                    tv_infouser_address.setText(khachHang.getDiaChi());
                    tv_infouser_phonenumber.setText(khachHang.getSDT());
                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {

            }
        });

        img_back_info_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_update_khachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoUserActivity.this, UpdateUserActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void InitWidget() {
        tv_infouser_name = findViewById(R.id.tv_infouser_name);
        tv_infouser_gender = findViewById(R.id.tv_infouser_gender);
        tv_infouser_date = findViewById(R.id.tv_infouser_date);
        tv_infouser_address = findViewById(R.id.tv_infouser_address);
        tv_infouser_phonenumber = findViewById(R.id.tv_infouser_phonenumber);
        img_back_info_user = findViewById(R.id.img_back_info_user);
        btn_update_khachhang = findViewById(R.id.btn_update_khachhang);
    }
}