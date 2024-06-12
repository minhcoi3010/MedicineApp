package com.example.btlandroidapi.model;

public class Goods {
    private String SoHDB;
    private String MaThuoc;
    private String TenThuoc;
    private String SLBan;
    private String DonGiaBan;
    private String ThanhTien;

    private String Anh;

    public Goods(String soHDB, String maThuoc, String tenThuoc, String SLBan, String donGiaBan, String thanhTien, String anh) {
        SoHDB = soHDB;
        MaThuoc = maThuoc;
        TenThuoc = tenThuoc;
        this.SLBan = SLBan;
        DonGiaBan = donGiaBan;
        ThanhTien = thanhTien;
        Anh = anh;
    }

    public String getSoHDB() {
        return SoHDB;
    }

    public void setSoHDB(String soHDB) {
        SoHDB = soHDB;
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

    public String getSLBan() {
        return SLBan;
    }

    public void setSLBan(String SLBan) {
        this.SLBan = SLBan;
    }

    public String getDonGiaBan() {
        return DonGiaBan;
    }

    public void setDonGiaBan(String donGiaBan) {
        DonGiaBan = donGiaBan;
    }

    public String getThanhTien() {
        return ThanhTien;
    }

    public void setThanhTien(String thanhTien) {
        ThanhTien = thanhTien;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }
}

