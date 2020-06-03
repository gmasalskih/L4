package com.example.l4.api;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.l4.entity.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
abstract public class DB extends RoomDatabase {

    private static volatile DB INSTANCE;

    public abstract DAO getDao();

    public static DB getInstance(Context context) {

        synchronized (DB.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        DB.class,
                        "db.db"
                ).build();
            }
            return INSTANCE;
        }
    }
}