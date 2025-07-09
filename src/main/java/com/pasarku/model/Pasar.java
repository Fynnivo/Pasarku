package com.pasarku.model;

import javax.persistence.*;

@Entity
@Table(name = "pasar")
public class Pasar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String alamat;

    @Column(nullable = false)
    private String kota;

    @Column(nullable = false)
    private String jamOperasional;

    @Column(nullable = false)
    private String hariPasar;

    @Column(nullable = false)
    private String kategori; // harian, mingguan, khusus

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getKota() { return kota; }
    public void setKota(String kota) { this.kota = kota; }
    public String getJamOperasional() { return jamOperasional; }
    public void setJamOperasional(String jamOperasional) { this.jamOperasional = jamOperasional; }
    public String getHariPasar() { return hariPasar; }
    public void setHariPasar(String hariPasar) { this.hariPasar = hariPasar; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
}
