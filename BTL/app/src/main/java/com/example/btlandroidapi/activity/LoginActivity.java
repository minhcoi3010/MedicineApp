package com.example.btlandroidapi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlandroidapi.singleton.CustomerSingleton;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.AccountKH;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView tv_signup;
    EditText edt_login_email, edt_login_password;
    Button login_button;
    MedicineInterface medicineInterface;
    CustomerSingleton customerSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitWitget();
        medicineInterface = MedicineUtils.getBookService();
        customerSingleton = CustomerSingleton.getInstance();

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tentk = edt_login_email.getText().toString();
                String pass = edt_login_password.getText().toString();
                medicineInterface.getAccountKH(tentk, pass).enqueue(new Callback<AccountKH>() {
                    @Override
                    public void onResponse(Call<AccountKH> call, Response<AccountKH> response) {
                       if(response.isSuccessful()){
                           Toast.makeText(LoginActivity.this, "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           startActivity(intent);

                           AccountKH accountKH = response.body();

                           customerSingleton.setCustomerId(accountKH.getMaKH());

                       }else{
                           Toast.makeText(LoginActivity.this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                       }
                    }

                    @Override
                    public void onFailure(Call<AccountKH> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void InitWitget() {
        tv_signup = findViewById(R.id.signupRedirectText);
        edt_login_email = findViewById(R.id.edt_login_email);
        edt_login_password = findViewById(R.id.edt_login_password);
        login_button = findViewById(R.id.login_button);
    }
}