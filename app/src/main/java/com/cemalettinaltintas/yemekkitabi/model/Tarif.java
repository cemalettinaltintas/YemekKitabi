package com.cemalettinaltintas.yemekkitabi.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tarif {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "isim")
    public  String isim;
    @ColumnInfo(name = "malzemem")
    public  String malzeme;
    @ColumnInfo(name = "gorsel")
    public  byte[] gorsel;

    public Tarif(String isim, String malzeme, byte[] gorsel) {
        this.isim = isim;
        this.malzeme = malzeme;
        this.gorsel = gorsel;
    }
}