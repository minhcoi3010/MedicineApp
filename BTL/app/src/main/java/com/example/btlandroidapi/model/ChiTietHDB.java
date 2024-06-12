package com.example.btlandroidapi.model;

public class ChiTietHDB {
    private String SoHDB;
    private String MaThuoc;
    private int SLBan;
    private String KhuyenMai;
    private float ThanhTien;

    public ChiTietHDB(String soHDB, String maThuoc, int SLBan, String khuyenMai, float thanhTien) {
        SoHDB = soHDB;
        MaThuoc = maThuoc;
        this.SLBan = SLBan;
        KhuyenMai = khuyenMai;
        ThanhTien = thanhTien;
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

    public int getSLBan() {
        return SLBan;
    }

    public void setSLBan(int SLBan) {
        this.SLBan = SLBan;
    }

    public String getKhuyenMai() {
        return KhuyenMai;
    }

    public void setKhuyenMai(String khuyenMai) {
        KhuyenMai = khuyenMai;
    }

    public float getThanhTien() {
        return ThanhTien;
    }

    public void setThanhTien(float thanhTien) {
        ThanhTien = thanhTien;
    }
}
