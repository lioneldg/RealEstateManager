package com.openclassrooms.realestatemanager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.provider.EstatesContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)

public class EstateContentProviderTest {
    private ContentResolver mContentResolver;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), AppDatabase.class).allowMainThreadQueries().build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getEstates() {
        Uri uri = EstatesContentProvider.URI_GET_ESTATES;
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(uri, 1), null, null, null, null);
        assertThat(cursor, notNullValue());
        cursor.close();

    }
}
