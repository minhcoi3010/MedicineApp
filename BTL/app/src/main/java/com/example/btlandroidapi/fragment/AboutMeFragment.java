package com.example.btlandroidapi.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.btlandroidapi.activity.DeliveryActivity;
import com.example.btlandroidapi.activity.ProcessingActivity;
import com.example.btlandroidapi.contants.Contants;
import com.example.btlandroidapi.singleton.CustomerSingleton;
import com.example.btlandroidapi.activity.InfoUserActivity;
import com.example.btlandroidapi.activity.LoginActivity;
import com.example.btlandroidapi.activity.MainActivity;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.model.KhachHang;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeFragment extends Fragment {

    LinearLayout ll_thongtinkhachhang, ll_logout, ll_processing, ll_delivering, ll_delivered;
    CustomerSingleton customerSingleton;
    TextView tv_about_me_name;
    ImageView img_back_about_me;
    MedicineInterface medicineInterface;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerSingleton = CustomerSingleton.getInstance();
        medicineInterface = MedicineUtils.getBookService();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_about_me, container, false);
        InitWidget(rootview);
        medicineInterface.getThongTinKhachHang(customerSingleton.getCustomerId()).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if(response.isSuccessful()){
                    KhachHang khachHang = response.body();
                    tv_about_me_name.setText("Xin Chào " + khachHang.getTenKH());
                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {

            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        ll_processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProcessingActivity.class);
                startActivity(intent);
            }
        });
        ll_thongtinkhachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InfoUserActivity.class);
                startActivity(intent);
            }
        });
        img_back_about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        ll_delivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeliveryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Contants.Bundel_Delivery, Contants.Delivering);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ll_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeliveryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Contants.Bundel_Delivery, Contants.Delivered);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootview;
    }

    private void InitWidget(View rootview) {
        ll_thongtinkhachhang = rootview.findViewById(R.id.ll_thongtinkhachhang);
        tv_about_me_name = rootview.findViewById(R.id.tv_about_me_name);
        img_back_about_me = rootview.findViewById(R.id.img_back_about_me);
        ll_processing = rootview.findViewById(R.id.ll_processing);
        ll_delivering = rootview.findViewById(R.id.ll_delivering);
        ll_delivered = rootview.findViewById(R.id.ll_delivered);
        ll_logout = rootview.findViewById(R.id.ll_logout);

    }


}

