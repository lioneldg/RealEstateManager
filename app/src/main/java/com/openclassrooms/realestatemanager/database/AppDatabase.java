package com.openclassrooms.realestatemanager.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.openclassrooms.realestatemanager.model.Estate;


@Database(entities = {Estate.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // SINGLETON
    private static volatile AppDatabase INSTANCE;

    // DAO
    public abstract EstateDao estateDao();


    // INSTANCE
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "MyDatabase.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
