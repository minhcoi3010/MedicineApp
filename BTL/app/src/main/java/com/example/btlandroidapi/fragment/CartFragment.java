package com.example.btlandroidapi.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroidapi.viewmodel.CartViewModel;
import com.example.btlandroidapi.singleton.CustomerSingleton;
import com.example.btlandroidapi.activity.MainActivity;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.adapter.CartAdapter;
import com.example.btlandroidapi.model.ChiTietHDB;
import com.example.btlandroidapi.model.HoaDonBan;
import com.example.btlandroidapi.model.Medicine;
import com.example.btlandroidapi.model.NhanVien;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    List<Medicine> list;
    CartAdapter cartAdapter;
    RecyclerView recycleView_item_cart;
    ImageView img_back_cart;
    TextView tv_thongbao;
    Button btn_back_cart, btn_payment_cart;
    private CartViewModel cartViewModel;

    MedicineInterface medicineInterface;
    CustomerSingleton customerSingleton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartViewModel = CartViewModel.getInstance(getContext());
        list = new ArrayList<>();
        customerSingleton = CustomerSingleton.getInstance();
        medicineInterface = MedicineUtils.getBookService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_cart, container, false);
        InitWidget(rootview);

        recycleView_item_cart = rootview.findViewById(R.id.recycleView_item_cart);
        recycleView_item_cart.setLayoutManager(new LinearLayoutManager(getContext()));


        // Quan sát thay đổi trong giỏ hàng
        cartViewModel.getCartList().observe(getViewLifecycleOwner(), new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> medicines) {
                list.clear();
                list.addAll(medicines);
                cartAdapter.notifyDataSetChanged();

            }
        });


        cartAdapter = new CartAdapter(getContext(), list, R.layout.item_cart);
        recycleView_item_cart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        return rootview;
    }



    private void InitWidget(View rootview) {
        img_back_cart = rootview.findViewById(R.id.img_back_cart);
        btn_back_cart = rootview.findViewById(R.id.btn_back_cart);

        btn_payment_cart = rootview.findViewById(R.id.btn_payment_cart);

        img_back_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_back_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        btn_payment_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(list.size() != 0){
                   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                   builder.setTitle("Xác nhận thanh toán");
                   builder.setMessage("Bạn muốn thanh toán hay không?");
                   builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           // Xử lý thanh toán
                           //Toast.makeText(getActivity().getApplicationContext(), "Đang thanh toán...", Toast.LENGTH_SHORT).show();
                               // Tạo hóa đơn bán và thêm vào cơ sở dữ liệu
                               String hdb = generateInvoiceCode();
                               String makh = customerSingleton.getCustomerId();
                               HoaDonBan hoaDonBan = new HoaDonBan(hdb, null, new Date(), makh, 0);
                               medicineInterface.insertHoaDonBan(hoaDonBan).enqueue(new Callback<HoaDonBan>() {
                                   @Override
                                   public void onResponse(Call<HoaDonBan> call, Response<HoaDonBan> response) {
                                       if (response.isSuccessful()) {
                                           List<Integer> tt = getQuantityListFromAdapter();
                                           List<ChiTietHDB> listCTHDB = new ArrayList<>();
                                           for(int i = 0; i < list.size(); i++) {
                                               ChiTietHDB chiTietHDB = new ChiTietHDB(hdb, list.get(i).getMaThuoc(), tt.get(i), "10", tt.get(i) * list.get(i).getDonGiaBan());
                                               listCTHDB.add(chiTietHDB);
                                           }
//
                                           medicineInterface.insertChiTietHDB(listCTHDB).enqueue(new Callback<List<ChiTietHDB>>() {
                                               @Override
                                               public void onResponse(Call<List<ChiTietHDB>> call, Response<List<ChiTietHDB>> response) {
                                                   if(response.isSuccessful()){
                                                       Toast.makeText(getContext(), "Thanh Toán Thành Công", Toast.LENGTH_SHORT).show();
                                                   }
                                               }

                                               @Override
                                               public void onFailure(Call<List<ChiTietHDB>> call, Throwable t) {

                                               }
                                           });
                                           cartViewModel.clearCart();

                                           Intent intent = new Intent(getContext(), MainActivity.class);
                                           startActivity(intent);

                                       } else {

                                       }
                                   }

                                   @Override
                                   public void onFailure(Call<HoaDonBan> call, Throwable t) {

                                   }
                               });



                           }
                   });
                   builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           // Hủy thanh toán
                           Toast.makeText(getActivity().getApplicationContext(), "Thanh toán đã bị hủy", Toast.LENGTH_SHORT).show();
                       }
                   });
                   builder.show();

               }else{
                   Toast.makeText(getContext(), "Hãy Thêm Sản Phẩm Vào Giỏ Hàng", Toast.LENGTH_SHORT).show();
               }


            }
        });
    }


    public List<Integer> getQuantityListFromAdapter() {
        if (cartAdapter != null) {
            return cartAdapter.getQuantityList();
        } else {
            return new ArrayList<>(); // Trả về một danh sách trống nếu adapter chưa được khởi tạo
        }
    }
    public static String generateInvoiceCode() {
        // Định dạng cho mã hoá đơn là "HDB" + thời gian thực
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTime = sdf.format(new Date());
        String invoiceCode = "HDB" + currentTime;
        return invoiceCode;
    }



}


