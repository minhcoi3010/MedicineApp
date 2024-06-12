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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.AccountKH;
import com.example.btlandroidapi.model.KhachHang;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    TextView tv_login;
    Button btn_signup;
    String makh = "B";
    String gender = "Nam";
    RadioButton rbt_gender_male, rbt_gender_female;
    MedicineInterface medicineInterface;
    EditText edt_account_signup,edt_password_signup,edt_name_signup,edt_gender_signup,edt_dateofbirth_signup, edt_address_signup,edt_phonenumber_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        InitWidget();
        medicineInterface = MedicineUtils.getBookService();

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        rbt_gender_male.setChecked(true);
        edt_dateofbirth_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(SignUpActivity.this);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_account_signup != null && edt_password_signup != null && edt_name_signup != null && (rbt_gender_male.isChecked() || rbt_gender_female.isChecked() ) && edt_dateofbirth_signup != null && edt_address_signup != null && edt_phonenumber_signup != null){
                    String account = edt_account_signup.getText().toString();
                    String password = edt_password_signup.getText().toString();
                    medicineInterface.getAccountKH(account, password).enqueue(new Callback<AccountKH>() {
                        @Override
                        public void onResponse(Call<AccountKH> call, Response<AccountKH> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Tài Khoản Đã Tồn Tại", Toast.LENGTH_SHORT).show();
                            }else{
                                String name = edt_name_signup.getText().toString();
                                if(rbt_gender_male.isChecked()){
                                    gender = "Nam";
                                }else{
                                    gender = "Nữ";
                                }
                                String dateofbirth = edt_dateofbirth_signup.getText().toString();
                                String address = edt_address_signup.getText().toString();
                                String phonenumber = edt_phonenumber_signup.getText().toString();

                                KhachHang khachHang = new KhachHang(makh, name, gender, dateofbirth, address, phonenumber);
                                medicineInterface.insertKhachHang(khachHang).enqueue(new Callback<KhachHang>() {
                                    @Override
                                    public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                                        if(response.isSuccessful()){
                                            KhachHang khachHang1 = response.body();
                                            AccountKH accountKH = new AccountKH(khachHang1.getMaKH(), account, password);
                                            medicineInterface.insertAccountKH(accountKH).enqueue(new Callback<AccountKH>() {
                                                @Override
                                                public void onResponse(Call<AccountKH> call, Response<AccountKH> response) {
                                                    if(response.isSuccessful()){
                                                        Toast.makeText(SignUpActivity.this, "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<AccountKH> call, Throwable t) {
                                                    Toast.makeText(SignUpActivity.this, "Đăng Ký Thất Bại", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<KhachHang> call, Throwable t) {
                                        Toast.makeText(SignUpActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<AccountKH> call, Throwable t) {

                        }
                    });

                }else{
                    Toast.makeText(SignUpActivity.this, "Hãy Nhập Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show();
                }
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
                        edt_dateofbirth_signup.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    public static String generateCustomerId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss"); // Định dạng thời gian
        String timestamp = dateFormat.format(new Date()); // Lấy thời gian hiện tại
        return "KH" + "_" + timestamp; // Kết hợp tiền tố và thời gian
    }
    private void InitWidget() {

        tv_login = findViewById(R.id.loginRedirectText);
        edt_account_signup = findViewById(R.id.edt_account_signup);
        edt_password_signup = findViewById(R.id.edt_password_signup);
        edt_name_signup = findViewById(R.id.edt_name_signup);
        rbt_gender_male = findViewById(R.id.rbt_gender_male);
        rbt_gender_female = findViewById(R.id.rbt_gender_female);
        edt_dateofbirth_signup = findViewById(R.id.edt_dateofbirth_signup);
        edt_address_signup = findViewById(R.id.edt_address_signup);
        edt_phonenumber_signup = findViewById(R.id.edt_phonenumber_signup);
        btn_signup = findViewById(R.id.signup_button);
    }
}