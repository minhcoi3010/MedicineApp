package com.example.btlandroidapi.model;

import java.io.Serializable;
import java.util.Date;

public class Medicine implements Serializable {
    private String MaThuoc;
    private String TenThuoc;
    private String ThanhPhan;
    private Date NgaySX;
    private Date NgayHH;
    private String MaLoai;
    private float DonGiaBan;
    private float DonGiaNhap;
    private int SoLuong;
    private float TrongLuong;
    private String Anh;
    private String XuatXu;
    private String MoTa;
    private String isDeleted;

    public Medicine(String maThuoc, String tenThuoc, String thanhPhan, Date ngaySX, Date ngayHH, String maLoai, float donGiaBan, float donGiaNhap, int soLuong, float trongLuong, String anh, String xuatXu, String moTa, String isdeleted) {
        MaThuoc = maThuoc;
        TenThuoc = tenThuoc;
        ThanhPhan = thanhPhan;
        NgaySX = ngaySX;
        NgayHH = ngayHH;
        MaLoai = maLoai;
        DonGiaBan = donGiaBan;
        DonGiaNhap = donGiaNhap;
        SoLuong = soLuong;
        TrongLuong = trongLuong;
        Anh = anh;
        XuatXu = xuatXu;
        MoTa = moTa;
        isDeleted = isdeleted;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMaThuoc() {
        return MaThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        MaThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return TenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        TenThuoc = tenThuoc;
    }

    public String getThanhPhan() {
        return ThanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        ThanhPhan = thanhPhan;
    }

    public Date getNgaySX() {
        return NgaySX;
    }

    public void setNgaySX(Date ngaySX) {
        NgaySX = ngaySX;
    }

    public Date getNgayHH() {
        return NgayHH;
    }

    public void setNgayHH(Date ngayHH) {
        NgayHH = ngayHH;
    }

    public String getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(String maLoai) {
        MaLoai = maLoai;
    }

    public float getDonGiaBan() {
        return DonGiaBan;
    }

    public void setDonGiaBan(float donGiaBan) {
        DonGiaBan = donGiaBan;
    }

    public float getDonGiaNhap() {
        return DonGiaNhap;
    }

    public void setDonGiaNhap(float donGiaNhap) {
        DonGiaNhap = donGiaNhap;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public float getTrongLuong() {
        return TrongLuong;
    }

    public void setTrongLuong(float trongLuong) {
        TrongLuong = trongLuong;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    public String getXuatXu() {
        return XuatXu;
    }

    public void setXuatXu(String xuatXu) {
        XuatXu = xuatXu;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }
}
