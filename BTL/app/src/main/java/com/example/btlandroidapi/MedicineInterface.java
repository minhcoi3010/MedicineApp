package com.example.btlandroidapi;

import com.example.btlandroidapi.model.AccountKH;
import com.example.btlandroidapi.model.ChiTietHDB;
import com.example.btlandroidapi.model.Goods;
import com.example.btlandroidapi.model.HoaDonBan;
import com.example.btlandroidapi.model.KhachHang;
import com.example.btlandroidapi.model.Medicine;
import com.example.btlandroidapi.model.NhanVien;
import com.example.btlandroidapi.model.Product;
import com.example.btlandroidapi.model.SoHDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MedicineInterface {
    @POST("/customer/signUp")
    Call<KhachHang> insertKhachHang(@Body KhachHang khachhang);

    @POST("/customer/signUpAccountKH")
    Call<AccountKH> insertAccountKH(@Body AccountKH account);

    @GET("/customer/login/{TenTK}/{Pass}")
    Call<AccountKH> getAccountKH(@Path("TenTK") String TenTK, @Path("Pass") String Pass);

    @GET("/medicines/getAll")
    Call<ArrayList<Medicine>> getAllThuoc();

    @GET("/medicine/getAllLoaiThuoc")
    Call<ArrayList<Product>> getAllTenThuoc();

    @GET("/medicine/getThuocByMaLoai/{MaLoai}")
    Call<ArrayList<Medicine>> getThuocByMaLoai(@Path("MaLoai") String MaLoai);

    @GET("/staffs/getAllStaffs")
    Call<ArrayList<NhanVien>> getAllNhanVien();

    @POST("/bill/billOfSale")
    Call<HoaDonBan> insertHoaDonBan(@Body HoaDonBan hdb);

    @POST("/bill/salesInvoiceDetails")
    Call<List<ChiTietHDB>> insertChiTietHDB(@Body List<ChiTietHDB> cthdb);

    @GET("/customer/getInfoCustomer/{MaKH}")
    Call<KhachHang> getThongTinKhachHang(@Path("MaKH") String MaKH);

    @GET("/medicine/getThuocByTenThuoc/{TenThuoc}")
    Call<ArrayList<Medicine>> getThuocByTenThuoc(@Path("TenThuoc") String TenThuoc);

    @GET("/medicine/getThuocByMaThuoc/{MaThuoc}")
    Call<Medicine> getThuocByMaThuoc(@Path("MaThuoc") String MaThuoc);

    @GET("/medicine/medicineBestSeller")
    Call<ArrayList<Medicine>> getAllThuocBestSeller();

    @POST("/customer/updateInfoCustomer")
    Call<String> updateInfoKhachHang(@Body KhachHang kh);

    @GET("/goods/getHDBIdWithMaNVNull/{MaKH}")
    Call<List<SoHDB>> getHDBIdWithMaNVNull(@Path("MaKH") String MaKH);

    @GET("/goods/getHDBIdWithMaNVNotNull/{MaKH}")
    Call<List<SoHDB>> getHDBIdWithMaNVNotNull(@Path("MaKH") String MaKH);
    @GET("/goods/getHDBDetailWithMaNVNull/{SoHDB}")
    Call<List<Goods>> getHDBDetailWithMaNVNull(@Path("SoHDB") String SoHDB);

    @GET("/goods/getHDBDetailWithMaNVNotNull/{SoHDB}")
    Call<List<Goods>> getHDBDetailWithMaNVNotNull(@Path("SoHDB") String SoHDB);

}
