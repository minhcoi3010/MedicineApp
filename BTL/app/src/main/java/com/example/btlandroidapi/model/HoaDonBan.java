package com.example.btlandroidapi.model;

import java.util.Date;

public class HoaDonBan {
    private String SoHDB;
    private String MaNV;
    private Date NgayLap;
    private String MaKH;
    private float TongTien;

    public HoaDonBan(String soHDB, String maNV, Date ngayLap, String maKH, float tongTien) {
        SoHDB = soHDB;
        MaNV = maNV;
        NgayLap = ngayLap;
        MaKH = maKH;
        TongTien = tongTien;
    }

    public String getSoHDB() {
        return SoHDB;
    }

    public void setSoHDB(String soHDB) {
        SoHDB = soHDB;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public Date getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Date ngayLap) {
        NgayLap = ngayLap;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String maKH) {
        MaKH = maKH;
    }

    public float getTongTien() {
        return TongTien;
    }

    public void setTongTien(float tongTien) {
        TongTien = tongTien;
    }
}
