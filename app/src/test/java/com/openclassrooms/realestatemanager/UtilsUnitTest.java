package com.openclassrooms.realestatemanager;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
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
    public void getFormattedDate(){
        Date date = new Date(1650623208492L);
        assertEquals("04/22/2022", Utils.getFormattedDate(date));
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

    @Test
    public void isLetterHyphenAndSpace(){
        String notMatchable1 = "I don't match_123";
        String notMatchable2 = "A1(@}";
        String matchable = "I match_-";
        assertFalse(Utils.isLetterHyphenAndSpace(notMatchable1));
        assertFalse(Utils.isLetterHyphenAndSpace(notMatchable2));
        assertTrue(Utils.isLetterHyphenAndSpace(matchable));
    }

    @Test
    public void isAlphanumHyphenAndSpace(){
        String notMatchable1 = "I {don't} match_123";
        String notMatchable2 = "A1(@}";
        String matchable = "I match_123-";
        assertFalse(Utils.isAlphanumHyphenAndSpace(notMatchable1));
        assertFalse(Utils.isAlphanumHyphenAndSpace(notMatchable2));
        assertTrue(Utils.isAlphanumHyphenAndSpace(matchable));
    }

    @Test
    public void isNumber(){
        String notMatchable1 = "don't match 123";
        String notMatchable2 = "A1(@}";
        String matchable = "123";
        assertFalse(Utils.isNumber(notMatchable1));
        assertFalse(Utils.isNumber(notMatchable2));
        assertTrue(Utils.isNumber(matchable));
    }

    @Test
    public void fromArrayListStringToStringList(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("one");
        arrayList.add("two");
        arrayList.add("three");
        arrayList.add("four");
        String stringList = "one,two,three,four";
        assertEquals(stringList, Utils.fromArrayListStringToStringList(arrayList));
    }

    @Test
    public void fromStringListToArrayList(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("one");
        arrayList.add("two");
        arrayList.add("three");
        arrayList.add("four");
        String stringList = "one,two,three,four";
        ArrayList<String> arrayListFromUtils = Utils.fromStringListToArrayList(stringList);
        assertEquals(arrayList.get(0), arrayListFromUtils.get(0));
        assertEquals(arrayList.get(1), arrayListFromUtils.get(1));
        assertEquals(arrayList.get(2), arrayListFromUtils.get(2));
        assertEquals(arrayList.get(3), arrayListFromUtils.get(3));
    }

    @Test
    public void getTimestampXMonthsAgo(){
        long timestampNow = 1655890463000L;
        long timestamp3MonthAgo = 1647945263000L;
        long timestamp12MonthAgo = 1624354463000L;
        long timestamp36MonthAgo = 1561196063000L;
        Date dateNow = new Date(timestampNow);
        long _3MonthAgo = Utils.getTimestampXMonthsAgo(dateNow,3);
        long _12MonthAgo = Utils.getTimestampXMonthsAgo(dateNow,12);
        long _36MonthAgo = Utils.getTimestampXMonthsAgo(dateNow,36);
        assertEquals(timestamp3MonthAgo, _3MonthAgo);
        assertEquals(timestamp12MonthAgo, _12MonthAgo);
        assertEquals(timestamp36MonthAgo, _36MonthAgo);
    }

    @Test
    public void getFinanceMonthlyPayment(){
        int resultMonthlyPayment = 2000;
        int totalAmount = 350000;
        int contribution = 50000;
        double interestRate = 2.5;
        int duration = 180;
        int resultMonthlyPaymentFromUtils = (int) Utils.getFinanceMonthlyPayment(totalAmount,contribution,interestRate, duration);
        assertEquals(resultMonthlyPayment, resultMonthlyPaymentFromUtils);
    }

    @Test
    public void getCostOfFinancing(){
        int resultCostOfFinancing = 60066;
        int totalAmount = 350000;
        int contribution = 50000;
        double interestRate = 2.5;
        int duration = 180;
        int resultCostOfFinancingFromUtils = (int) Utils.getCostOfFinancing(totalAmount,contribution,interestRate, duration);
        assertEquals(resultCostOfFinancing, resultCostOfFinancingFromUtils);
    }
}