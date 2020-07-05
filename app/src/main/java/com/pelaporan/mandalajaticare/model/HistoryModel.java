package com.pelaporan.mandalajaticare.model;

public class HistoryModel {

    public String getPelapor() {
        return pelapor;
    }

    public void setPelapor(String pelapor) {
        this.pelapor = pelapor;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    private String pelapor, jenis, detail, alamat, aksi, imgURL;

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String judul) {
        this.jenis = judul;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String detail) {
        this.alamat = detail;
    }

    public String getAksi() {
        return aksi;
    }

    public void setAksi(String tindak) {
        this.aksi = tindak;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
