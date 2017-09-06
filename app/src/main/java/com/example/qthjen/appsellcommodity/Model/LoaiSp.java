package com.example.qthjen.appsellcommodity.Model;

public class LoaiSp {

    private int id;
    private String tensp;
    private String hinhsp;

    public LoaiSp(int id, String tensp, String hinhsp) {
        this.id = id;
        this.tensp = tensp;
        this.hinhsp = hinhsp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getHinhsp() {
        return hinhsp;
    }

    public void setHinhsp(String hinhsp) {
        this.hinhsp = hinhsp;
    }
}
