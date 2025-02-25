package com.cemalettinaltintas.yemekkitabi.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cemalettinaltintas.yemekkitabi.model.Tarif;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TarifDao {
    @Query("SELECT * FROM Tarif")
    Flowable<List<Tarif>> getAll();

    @Query("SELECT * FROM Tarif WHERE id=:id")
    Flowable<Tarif> findById(int id);

    @Insert
    Completable insert(Tarif tarif);

    @Delete
    Completable delete(Tarif tarif);
}