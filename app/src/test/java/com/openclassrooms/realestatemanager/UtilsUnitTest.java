package com.openclassrooms.realestatemanager;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import static org.mockito.Mockito.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class UtilsUnitTest {
    @Test
    public void convertDollarToEuro(){
        assertEquals(2, Utils.convertDollarToEuro(2), 0);
        assertEquals(10, Utils.convertDollarToEuro(12), 0);
        assertEquals(28420, Utils.convertDollarToEuro(35000), 0);
    }

    @Test
    public void convertEuroToDollar(){
        assertEquals(2, Utils.convertEuroToDollar(2), 0);
        assertEquals(12, Utils.convertEuroToDollar(10), 0);
        assertEquals(35000, Utils.convertEuroToDollar(28420), 0);
    }

    @Test
    public void getTodayDate(){
        Date date = new Date(1650623208492l);
        assertEquals("22/04/2022", Utils.getFormattedDate(date));
    }

    @Test
    public void isInternetAvailable(){
        NetworkInfo mockedNetworkInfo = mock(NetworkInfo.class);
        assertFalse(Utils.isInternetAvailable(null));
        when(mockedNetworkInfo.getType()).thenReturn(ConnectivityManager.TYPE_MOBILE);
        assertTrue(Utils.isInternetAvailable(mockedNetworkInfo));
        when(mockedNetworkInfo.getType()).thenReturn(ConnectivityManager.TYPE_WIFI);
        assertTrue(Utils.isInternetAvailable(mockedNetworkInfo));
    }
}