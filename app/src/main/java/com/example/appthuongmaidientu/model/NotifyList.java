package com.example.appthuongmaidientu.model;

public class NotifyList {
    String id,status,content,idKH,idTB,idSP;

    public NotifyList(String id, String status, String content, String idKH, String idTB, String idSP) {
        this.id = id;
        this.status = status;
        this.content = content;
        this.idKH = idKH;
        this.idTB = idTB;
        this.idSP = idSP;
    }

    public String getIdSP() {
        return idSP;
    }

    public void setIdSP(String idSP) {
        this.idSP = idSP;
    }


    public String getIdTB() {
        return idTB;
    }

    public void setIdTB(String idTB) {
        this.idTB = idTB;
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Nội dung: "+this.content;
    }
}
