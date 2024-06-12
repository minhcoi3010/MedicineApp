package com.example.btlandroidapi.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.btlandroidapi.MedicineInterface;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.activity.SearcMedicineActivity;
import com.example.btlandroidapi.adapter.BestSellerAdapter;
import com.example.btlandroidapi.adapter.GoiYHomNayAdapter;
import com.example.btlandroidapi.model.Medicine;
import com.example.btlandroidapi.retrofit.MedicineUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    View inflater;
    List<Medicine> list, list_bestseller;

    ImageView img_cart;
    ImageSlider imgslider;
    LinearLayout ll_search_medicine;
    MedicineInterface medicineInterface;
    RecyclerView recyclerView_goiyhomnay,recycleview_bestseller ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize widgets here
        InitWidgets(rootView);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider4, ScaleTypes.FIT));

        imgslider.setImageList(slideModels);
        list = new ArrayList<>();
        list_bestseller = new ArrayList<>();
        medicineInterface = MedicineUtils.getBookService();
        medicineInterface.getAllThuocBestSeller().enqueue(new Callback<ArrayList<Medicine>>() {
            @Override
            public void onResponse(Call<ArrayList<Medicine>> call, Response<ArrayList<Medicine>> response) {
                if(response.isSuccessful()){
                    list_bestseller = response.body();
                    recycleview_bestseller = rootView.findViewById(R.id.recycleview_bestseller);

                    recycleview_bestseller.setLayoutManager(new LinearLayoutManager(getActivity()));
                    BestSellerAdapter bestSellerAdapter = new BestSellerAdapter(getContext(), list_bestseller, R.layout.item_main_bestseller);
                    recycleview_bestseller.setAdapter(bestSellerAdapter);

                    medicineInterface.getAllThuoc().enqueue(new Callback<ArrayList<Medicine>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Medicine>> call, Response<ArrayList<Medicine>> response) {
                            if(response.isSuccessful()){
                                list = response.body();

                                recyclerView_goiyhomnay = rootView.findViewById(R.id.recyclerView_goiyhomnay);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                recyclerView_goiyhomnay.setLayoutManager(layoutManager);

                                GoiYHomNayAdapter adapter = new GoiYHomNayAdapter(getActivity(), list, R.layout.item_listview_buy);
                                recyclerView_goiyhomnay.setAdapter(adapter);

                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Medicine>> call, Throwable t) {
                            Toast.makeText(getContext(), "Them du lieu that bai", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Medicine>> call, Throwable t) {

            }
        });




        return rootView;
    }



    private void InitWidgets(View rootView) {



        ll_search_medicine = rootView.findViewById(R.id.ll_search_medicine);
        imgslider = rootView.findViewById(R.id.imgslider);
        ll_search_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearcMedicineActivity.class);
                startActivity(intent);
            }
        });

    }


}