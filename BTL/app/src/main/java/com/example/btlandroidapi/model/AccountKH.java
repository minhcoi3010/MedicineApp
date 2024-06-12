package com.example.btlandroidapi.model;

public class AccountKH {
    private String MaKH;
    private String TenTK;
    private String Pass;

    public AccountKH(String maKH, String tenTK, String pass) {
        MaKH = maKH;
        TenTK = tenTK;
        Pass = pass;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String maKH) {
        MaKH = maKH;
    }

    public String getTenTK() {
        return TenTK;
    }

    public void setTenTK(String tenTK) {
        TenTK = tenTK;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
