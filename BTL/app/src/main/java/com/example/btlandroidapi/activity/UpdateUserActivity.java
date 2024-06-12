package com.example.btlandroidapi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.btlandroidapi.singleton.CustomerSingleton;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.KhachHang;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    EditText edt_name_update, edt_gender_update, edt_dateofbirth_update, edt_address_update, edt_phonenumber_update;
    MedicineInterface medicineInterface;
    ImageView img_back_update_info_user;
    Button btn_update_info_khachhang;
    String gioitinh = "Nam";
    CustomerSingleton customerSingleton;
    RadioButton rbt_gender_male_update, rbt_gender_female_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        customerSingleton = CustomerSingleton.getInstance();
        medicineInterface = MedicineUtils.getBookService();
        InitWidget();
        String makh = customerSingleton.getCustomerId();
        medicineInterface.getThongTinKhachHang(makh).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if(response.isSuccessful()){
                    KhachHang khachHang = response.body();
                    edt_name_update.setText(khachHang.getTenKH());
                    if(khachHang.getGioiTinh().equals("Nam")){
                        rbt_gender_male_update.setChecked(true);
                    }else{
                        rbt_gender_female_update.setChecked(true);
                    }
                    edt_dateofbirth_update.setText(khachHang.getNgaySinh());
                    edt_address_update.setText(khachHang.getDiaChi());
                    edt_phonenumber_update.setText(khachHang.getSDT());

                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {

            }
        });

        edt_dateofbirth_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(UpdateUserActivity.this);
            }
        });
        btn_update_info_khachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_name_update != null && (rbt_gender_male_update.isChecked() || rbt_gender_female_update.isChecked()) || edt_dateofbirth_update != null || edt_address_update != null || edt_phonenumber_update != null)
                {
                    String name = edt_name_update.getText().toString();
                    if(rbt_gender_male_update.isChecked()){
                        gioitinh = "Nam";
                    }else{
                        gioitinh = "Nữ";
                    }
                    String ngaysinh = edt_dateofbirth_update.getText().toString();
                    String diachi = edt_address_update.getText().toString();
                    String sodienthoai = edt_phonenumber_update.getText().toString();
                    KhachHang khachHang = new KhachHang(makh, name, gioitinh, ngaysinh, diachi, sodienthoai);
                    medicineInterface.updateInfoKhachHang(khachHang).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){


                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    Toast.makeText(UpdateUserActivity.this, "Chỉnh Sửa Thông Tin Thành Công", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UpdateUserActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        img_back_update_info_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void showDatePickerDialog(Context context) {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = year +"-"+(monthOfYear + 1)+"-"+dayOfMonth;
                        // Hiển thị ngày được chọn trong EditText
                        edt_dateofbirth_update.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void InitWidget() {

        edt_name_update = findViewById(R.id.edt_name_update);

        edt_dateofbirth_update = findViewById(R.id.edt_dateofbirth_update);
        edt_address_update = findViewById(R.id.edt_address_update);
        edt_phonenumber_update = findViewById(R.id.edt_phonenumber_update);
        btn_update_info_khachhang = findViewById(R.id.btn_update_info_khachhang);
        img_back_update_info_user = findViewById(R.id.img_back_update_info_user);
        rbt_gender_male_update = findViewById(R.id.rbt_gender_male_update);
        rbt_gender_female_update = findViewById(R.id.rbt_gender_female_update);

    }
}