package com.example.appthuongmaidientu.model;

public class DMSP {
    String name, hinhanh, id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DMSP(String name, String hinhanh, String id) {
        this.name = name;
        this.hinhanh = hinhanh;
        this.id = id;
    }
}
