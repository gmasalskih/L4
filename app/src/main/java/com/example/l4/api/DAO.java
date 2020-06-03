package com.example.l4.api;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.l4.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface DAO {

    @Query("SELECT * FROM user")
    public Single<List<User>> getAllUser();

    @Query("DELETE FROM user")
    public Completable clearAllUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertUser(List<User> users);
}
