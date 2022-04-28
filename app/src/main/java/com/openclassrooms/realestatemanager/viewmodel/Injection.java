package com.openclassrooms.realestatemanager.viewmodel;

import android.content.Context;
import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {
    public static EstateDataRepository provideEstateDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return new EstateDataRepository(database.estateDao());
    }

    public static Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        EstateDataRepository dataSourceEstate = provideEstateDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceEstate, executor);
    }
}
