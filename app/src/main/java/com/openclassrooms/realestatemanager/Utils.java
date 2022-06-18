package com.openclassrooms.realestatemanager;

import static android.content.ContentValues.TAG;

import static com.openclassrooms.realestatemanager.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * Conversion de la date en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getFormattedDate(Date date){
        DateFormat dateFormat = getSystemLanguage().equals("en") ? new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH) : new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
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
        String timeStamp = String.valueOf(System.currentTimeMillis());
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

    public static String urlRequest(String url) {
        StringBuilder placesBuilder = new StringBuilder();
        try {

            URL requestUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                if (inputStream != null) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        placesBuilder.append(line).append("\n");
                    }
                }
            } else {
                Log.i("test", "Unsuccessful HTTP Response Code: " + responseCode);
            }
        } catch (
                MalformedURLException e) {
            Log.e("test", "Error processing Places API URL", e);
        } catch (
                IOException e) {
            Log.e("test", "Error connecting to Places API", e);
        }
        return placesBuilder.toString();
    }

    public static String placeSearchNearBy(String lat, String lng){
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&rankby=distance"+
                "&types=point_of_interest"+
                "&key="+
                MAPS_API_KEY;
        return urlRequest(placesSearchStr);
    }

    public static String getPointsOfInterest(String lat, String lng, Context context) {
        String urlRequestResult = placeSearchNearBy(lat, lng);
        Set<String> pointsOfInterestSet = new HashSet<>();
        String pointsOfInterestString = null;
        try {
            JSONObject resultObject = new JSONObject(urlRequestResult);
            JSONArray results = resultObject.getJSONArray("results");
            for (int i = 0; results.length() > i; i++) {
                JSONObject place = new JSONObject(results.get(i).toString());
                JSONArray types = place.getJSONArray("types");
                for (int j = 0; types.length() > j; j++) {
                    String interest = types.get(j).toString();
                    switch (interest) {
                        case "airport":
                            pointsOfInterestSet.add(context.getString(R.string.airport));
                            break;
                        case "amusement_park":
                            pointsOfInterestSet.add(context.getString(R.string.amusement_park));
                            break;
                        case "aquarium":
                            pointsOfInterestSet.add(context.getString(R.string.aquarium));
                            break;
                        case "art_gallery":
                            pointsOfInterestSet.add(context.getString(R.string.art_gallery));
                            break;
                        case "atm":
                            pointsOfInterestSet.add(context.getString(R.string.atm));
                            break;
                        case "bakery":
                            pointsOfInterestSet.add(context.getString(R.string.bakery));
                            break;
                        case "bank":
                            pointsOfInterestSet.add(context.getString(R.string.bank));
                            break;
                        case "bar":
                            pointsOfInterestSet.add(context.getString(R.string.bar));
                            break;
                        case "beauty_salon":
                            pointsOfInterestSet.add(context.getString(R.string.beauty_salon));
                            break;
                        case "bus_station":
                            pointsOfInterestSet.add(context.getString(R.string.bus));
                            break;
                        case "cafe":
                            pointsOfInterestSet.add(context.getString(R.string.cafe));
                            break;
                        case "church":
                            pointsOfInterestSet.add(context.getString(R.string.church));
                            break;
                        case "city_hall":
                            pointsOfInterestSet.add(context.getString(R.string.city_hall));
                            break;
                        case "dentist":
                            pointsOfInterestSet.add(context.getString(R.string.dentist));
                            break;
                        case "doctor":
                            pointsOfInterestSet.add(context.getString(R.string.doctor));
                            break;
                        case "drugstore":
                            pointsOfInterestSet.add(context.getString(R.string.drugstore));
                            break;
                        case "florist":
                            pointsOfInterestSet.add(context.getString(R.string.florist));
                            break;
                        case "gas_station":
                            pointsOfInterestSet.add(context.getString(R.string.gas_station));
                            break;
                        case "gym":
                            pointsOfInterestSet.add(context.getString(R.string.gym));
                            break;
                        case "hair_care":
                            pointsOfInterestSet.add(context.getString(R.string.hair_care));
                            break;
                        case "hospital":
                            pointsOfInterestSet.add(context.getString(R.string.hospital));
                            break;
                        case "laundry":
                            pointsOfInterestSet.add(context.getString(R.string.laundry));
                            break;
                        case "library":
                            pointsOfInterestSet.add(context.getString(R.string.library));
                            break;
                        case "movie_theater":
                            pointsOfInterestSet.add(context.getString(R.string.movie_theater));
                            break;
                        case "museum":
                            pointsOfInterestSet.add(context.getString(R.string.museum));
                            break;
                        case "park":
                            pointsOfInterestSet.add(context.getString(R.string.park));
                            break;
                        case "pharmacy":
                            pointsOfInterestSet.add(context.getString(R.string.pharmacy));
                            break;
                        case "post_office":
                            pointsOfInterestSet.add(context.getString(R.string.post_office));
                            break;
                        case "primary_school":
                            pointsOfInterestSet.add(context.getString(R.string.primary_school));
                            break;
                        case "restaurant":
                            pointsOfInterestSet.add(context.getString(R.string.restaurant));
                            break;
                        case "school":
                            pointsOfInterestSet.add(context.getString(R.string.school));
                            break;
                        case "secondary_school":
                            pointsOfInterestSet.add(context.getString(R.string.secondary_school));
                            break;
                        case "shopping_mall":
                            pointsOfInterestSet.add(context.getString(R.string.shopping_mall));
                            break;
                        case "spa":
                            pointsOfInterestSet.add(context.getString(R.string.spa));
                            break;
                        case "store":
                            pointsOfInterestSet.add(context.getString(R.string.store));
                            break;
                        case "subway_station":
                            pointsOfInterestSet.add(context.getString(R.string.subway_station));
                            break;
                        case "supermarket":
                            pointsOfInterestSet.add(context.getString(R.string.supermarket));
                            break;
                        case "taxi_stand":
                            pointsOfInterestSet.add(context.getString(R.string.taxi_stand));
                            break;
                        case "train_station":
                            pointsOfInterestSet.add(context.getString(R.string.train_station));
                            break;
                        case "university":
                            pointsOfInterestSet.add(context.getString(R.string.university));
                            break;
                        case "veterinary_care":
                            pointsOfInterestSet.add(context.getString(R.string.veterinary));
                            break;
                        case "zoo":
                            pointsOfInterestSet.add(context.getString(R.string.zoo));
                            break;
                    }
                }
            }
            pointsOfInterestString = pointsOfInterestSet.toString().substring(1, pointsOfInterestSet.toString().length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointsOfInterestString;
    }

    public static Bitmap getStaticMap(String lat, String lng) {
        String url = "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=15&size=500x500&format=jpg&markers="+lat+","+lng+"&key="+MAPS_API_KEY;
        //execute query
        Bitmap result = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Bitmap> future = executor.submit(() -> {
            try {
                URL requestUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)requestUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = new URL(url).openStream();
                    return BitmapFactory.decodeStream(in);
                }
                else {
                    Log.i("test", "Unsuccessful HTTP Response Code: " + responseCode);
                }
            } catch (MalformedURLException e) {
                Log.e("test", "Error processing Places API URL", e);
            } catch (IOException e) {
                Log.e("test", "Error connecting to Places API", e);
            }
            return null;
        });
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static BitmapDescriptor vectorToBitmap(Resources resources, @DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable( resources, id, null);
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, Color.BLACK);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void sendNotification(Context context, String notificationText, boolean showBigText) {
        String channelId = "326";
        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_home_24)
                        .setContentText(notificationText)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        if(showBigText) {
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "RealEstateManager Messages";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        int NOTIFICATION_ID = 7;
        String NOTIFICATION_TAG = "REALESTATEMANAGERNOTIF";
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
