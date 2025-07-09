package com.pasarku.model;

import javax.persistence.*;

@Entity
@Table(name = "produk")
public class Produk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String kategori; // beras, sayuran, buah, protein, bumbu, minyak_gula, lainnya

    @Column(nullable = false)
    private String satuan; // kg, liter, ikat, dst

    @Column(nullable = false)
    private String kualitas; // A, B, C

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public String getKualitas() { return kualitas; }
    public void setKualitas(String kualitas) { this.kualitas = kualitas; }
}
