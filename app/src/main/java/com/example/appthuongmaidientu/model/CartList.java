package com.example.appthuongmaidientu.model;

public class CartList {
    String maSP;
    String tenSP;
    String soLuongMua;
    String check;
    String gia;
    String uid;
    String hinhanh;
    String slb;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CartList(String maSP, String tenSP, String soLuongMua, String check, String gia, String uid, String hinhanh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuongMua = soLuongMua;
        this.check = check;
        this.gia = gia;
        this.uid = uid;
        this.hinhanh = hinhanh;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public String getSlb() {
        return slb;
    }

    public void setSlb(String slb) {
        this.slb = slb;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }


    public String getMaSP() {
        return maSP;
    }

    public String getUid() {
        return uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }



    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(String soLuongMua) {
        this.soLuongMua = soLuongMua;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

}
