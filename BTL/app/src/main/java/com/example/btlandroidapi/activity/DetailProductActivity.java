package com.example.btlandroidapi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlandroidapi.viewmodel.CartViewModel;
import com.example.btlandroidapi.contants.Contants;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.Medicine;
import com.example.btlandroidapi.retrofit.MedicineUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {

    CartViewModel cartViewModel;
    ImageView img_detail_anh, img_detail_back, img_cart_detail;
    Button btn_back_detail, btn_buy_detail;
    Medicine medicine;
    MedicineInterface medicineInterface;
    TextView tv_detail_mota,tv_detail_dongiaban,tv_name_detail,tv_thanhphan_detail, tv_xuatxu_detail, tv_mota_detail;
    TextView tv_ngaysx_detail,tv_ngayhh_detail,tv_maloai_detail, tv_soluong_detail, tv_trongluong_detail, tv_detail_ten_thuoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        cartViewModel = CartViewModel.getInstance(this);
        medicineInterface = MedicineUtils.getBookService();
        InitWidget();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Contants.Bundel_MaThuoc);
        if(bundle != null){
            String mathuoc  =  bundle.getString(Contants.Medicine_Selected);
            medicineInterface.getThuocByMaThuoc(mathuoc).enqueue(new Callback<Medicine>() {
                @Override
                public void onResponse(Call<Medicine> call, Response<Medicine> response) {
                    if(response.isSuccessful()){
                        medicine = response.body();
                        tv_detail_dongiaban.setText(String.valueOf(medicine.getDonGiaBan()) + " / hộp");
                        tv_name_detail.setText(medicine.getTenThuoc());
                        tv_detail_mota.setText(medicine.getMoTa());
                        tv_thanhphan_detail.setText(medicine.getThanhPhan());
                        tv_xuatxu_detail.setText(medicine.getXuatXu());
                        tv_ngaysx_detail.setText(String.valueOf(medicine.getNgaySX()));
                        tv_ngayhh_detail.setText(String.valueOf(medicine.getNgayHH()));
                        tv_maloai_detail.setText(medicine.getMaLoai());
                        tv_soluong_detail.setText(String.valueOf(medicine.getSoLuong()));
                        tv_trongluong_detail.setText(String.valueOf(medicine.getTrongLuong()));
                        tv_detail_ten_thuoc.setText(medicine.getTenThuoc());
                        tv_mota_detail.setText(medicine.getMoTa());
                        String imageName = medicine.getAnh(); // Lấy tên tệp ảnh từ cơ sở dữ liệu

                        String str = imageName;
                        String fileName = str.substring(0, str.lastIndexOf("."));
                        int imageResource = getResources().getIdentifier(fileName, "drawable", getPackageName());
                        Picasso.get().load(imageResource).into(img_detail_anh);
                    }
                }

                @Override
                public void onFailure(Call<Medicine> call, Throwable t) {

                }
            });

        }

        img_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_buy_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartViewModel.addToCart(medicine);
                Toast.makeText(DetailProductActivity.this, "Đã Thêm Vào Giỏ Hàng", Toast.LENGTH_SHORT).show();
                onBackPressed(); // Go back after adding

            }
        });

        img_cart_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void InitWidget() {
        img_detail_anh = findViewById(R.id.img_detail_anh);
        tv_detail_mota = findViewById(R.id.tv_detail_mota);
        tv_detail_dongiaban = findViewById(R.id.tv_detail_dongiaban);
        tv_name_detail = findViewById(R.id.tv_name_detail);
        tv_thanhphan_detail = findViewById(R.id.tv_thanhphan_detail);
        tv_xuatxu_detail = findViewById(R.id.tv_xuatxu_detail);
        tv_ngaysx_detail = findViewById(R.id.tv_ngaysx_detail);
        tv_ngayhh_detail = findViewById(R.id.tv_ngayhh_detail);
        tv_maloai_detail = findViewById(R.id.tv_maloai_detail);
        tv_soluong_detail = findViewById(R.id.tv_soluong_detail);
        tv_trongluong_detail = findViewById(R.id.tv_trongluong_detail);
        tv_detail_ten_thuoc = findViewById(R.id.tv_detail_ten_thuoc);
        tv_mota_detail = findViewById(R.id.tv_mota_detail);
        img_cart_detail = findViewById(R.id.img_cart_detail);

        img_detail_back = findViewById(R.id.img_detail_back);
        btn_back_detail = findViewById(R.id.btn_back_detail);
        btn_buy_detail = findViewById(R.id.btn_buy_detail);
    }
}