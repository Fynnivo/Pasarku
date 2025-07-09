package com.pasarku.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "harga")
public class Harga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "produk_id", nullable = false)
    private Produk produk;

    @ManyToOne
    @JoinColumn(name = "pasar_id", nullable = false)
    private Pasar pasar;

    @Column(nullable = false)
    private double harga;

    @Column(nullable = false)
    private LocalDateTime waktuUpdate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Produk getProduk() { return produk; }
    public void setProduk(Produk produk) { this.produk = produk; }
    public Pasar getPasar() { return pasar; }
    public void setPasar(Pasar pasar) { this.pasar = pasar; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    public LocalDateTime getWaktuUpdate() { return waktuUpdate; }
    public void setWaktuUpdate(LocalDateTime waktuUpdate) { this.waktuUpdate = waktuUpdate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
