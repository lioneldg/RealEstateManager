package com.openclassrooms.realestatemanager;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {
    static double dollarEuroRate = 0.812;
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * dollarEuroRate);
    }

    public static int convertEuroToDollar(int euro){
        return (int) Math.round(euro * (1/dollarEuroRate));
    }

    public static Date getTodayDate(){
        return new Date();
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getFormattedDate(Date date){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static NetworkInfo getActiveNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    
    public static Boolean isInternetAvailable(NetworkInfo activeNetwork) {
        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isLetterHyphenAndSpace(String str){
        return str.matches("[A-Za-z -_]+");
    }

    public static boolean isAlphanumHyphenAndSpace(String str){
        return str.matches("[A-Za-z0-9 -_]+");
    }

    public static boolean isNumber(String str){
        return str.matches("[0-9]+");
    };

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscapeOrientation(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean hasCamera(Context context){
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_PICK = 2;

    public static void takePhoto(Activity activity){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public static void pickPhoto(Activity activity){
        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICK);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public static Bitmap loadFromUri(Uri photoUri, Context context) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap getBitmapFromFileName(String fileName, int maxSize, Context context) {
        File file = new File(context.getFilesDir(), fileName);
        return getResizedBitmap(BitmapFactory.decodeFile(file.getPath()), maxSize);
    }

    private static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return new File(context.getFilesDir(), imageFileName);
    }

    public static String storeBitmap(Context context, Bitmap imageBitmap) {
        File file = null;
        try {
            file = createImageFile(context);
            FileOutputStream fos = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        assert file != null;
        return file.getName();
    }

    public static String[] getFilesNamesList(Context context){
        return context.fileList();
    }

    public static String fromArrayListStringToStringList(ArrayList<String> list) {
        StringBuilder string= new StringBuilder();
        for(int i = 0; i < list.size(); i++) {
            if(string.toString().isEmpty()){
                string.append(list.get(i));
            } else {
                string.append(',').append(list.get(i));
            }
        }
        return string.toString();
    }

    public static  ArrayList<String> fromStringListToArrayList(String string) {
        String[] parts = string.split(",");
        return new ArrayList<>(Arrays.asList(parts));
    }
}
