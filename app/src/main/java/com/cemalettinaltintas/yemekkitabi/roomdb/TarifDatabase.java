package com.cemalettinaltintas.yemekkitabi.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cemalettinaltintas.yemekkitabi.model.Tarif;

@Database(entities = {Tarif.class}, version = 1)
public abstract class   TarifDatabase extends RoomDatabase {
    public abstract TarifDao tarifDao();
}