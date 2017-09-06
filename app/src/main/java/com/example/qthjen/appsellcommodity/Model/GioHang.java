package com.example.qthjen.appsellcommodity.Model;

/**
 * Created by qthjen on 8/31/17.
 */

public class GioHang {

    int idSp;
    String tenSp;
    long giaSp;
    String hinhSp;
    int soLuongSp;

    public GioHang(int idSp, String tenSp, long giaSp, String hinhSp, int soLuongSp) {
        this.idSp = idSp;
        this.tenSp = tenSp;
        this.giaSp = giaSp;
        this.hinhSp = hinhSp;
        this.soLuongSp = soLuongSp;
    }

    public int getIdSp() {
        return idSp;
    }

    public void setIdSp(int idSp) {
        this.idSp = idSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public long getGiaSp() {
        return giaSp;
    }

    public void setGiaSp(long giaSp) {
        this.giaSp = giaSp;
    }

    public String getHinhSp() {
        return hinhSp;
    }

    public void setHinhSp(String hinhSp) {
        this.hinhSp = hinhSp;
    }

    public int getSoLuongSp() {
        return soLuongSp;
    }

    public void setSoLuongSp(int soLuongSp) {
        this.soLuongSp = soLuongSp;
    }
}
