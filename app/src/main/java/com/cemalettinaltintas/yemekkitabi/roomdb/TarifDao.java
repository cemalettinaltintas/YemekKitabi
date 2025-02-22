package com.cemalettinaltintas.yemekkitabi.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cemalettinaltintas.yemekkitabi.model.Tarif;

import java.util.List;

@Dao
public interface TarifDao {
    @Query("SELECT * FROM Tarif")
    List<Tarif> getAll();

    @Query("SELECT * FROM Tarif WHERE id=:id")
    Tarif findById(int id);

    @Insert
    void insert(Tarif tarif);

    @Delete
    void delete(Tarif tarif);
}