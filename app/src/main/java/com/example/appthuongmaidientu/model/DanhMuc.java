package com.example.appthuongmaidientu.model;

public class DanhMuc {
    String name,mota,hinhanh,iddm;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public String getIddm() {
        return iddm;
    }

    public void setIddm(String iddm) {
        this.iddm = iddm;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public DanhMuc(String name, String hinhanh, String iddm) {
        this.name = name;
        this.hinhanh = hinhanh;
        this.iddm = iddm;
    }
}
