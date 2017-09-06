package com.example.qthjen.appsellcommodity.Model;

import java.io.Serializable;

public class SanPham implements Serializable {

    public int ID;
    public String TenSp;
    public Integer GiaSp;
    public String HinhSp;
    public String MotaSp;
    public int IDSanPham;

    public SanPham(int ID, String tenSp, Integer giaSp, String hinhSp, String motaSp, int IDSanPham) {
        this.ID = ID;
        TenSp = tenSp;
        GiaSp = giaSp;
        HinhSp = hinhSp;
        MotaSp = motaSp;
        this.IDSanPham = IDSanPham;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenSp() {
        return TenSp;
    }

    public void setTenSp(String tenSp) {
        TenSp = tenSp;
    }

    public Integer getGiaSp() {
        return GiaSp;
    }

    public void setGiaSp(Integer giaSp) {
        GiaSp = giaSp;
    }

    public String getHinhSp() {
        return HinhSp;
    }

    public void setHinhSp(String hinhSp) {
        HinhSp = hinhSp;
    }

    public String getMotaSp() {
        return MotaSp;
    }

    public void setMotaSp(String motaSp) {
        MotaSp = motaSp;
    }

    public int getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(int IDSanPham) {
        this.IDSanPham = IDSanPham;
    }
}
