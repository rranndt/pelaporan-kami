package com.pelaporan.mandalajaticare.config;

public class Warga
{
    String nama, username, password, alamat, email, foto_profil;
    public Warga(){}

    public Warga(String nama, String username, String password, String alamat, String email, String foto_profil) {
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.alamat = alamat;
        this.email = email;
        this.foto_profil = foto_profil;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto_profil() {
        return foto_profil;
    }

    public void setFoto_profil(String foto_profil) {
        this.foto_profil = foto_profil;
    }
}
