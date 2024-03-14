package technology.innovate.haziremployee.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getUniqueID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int progressPercent(String total, String joined) {
        double totalp = Double.valueOf(total);
        double joinedp = Double.valueOf(joined);

        double tot = (100 / totalp);
        double value = tot * joinedp;
        if (totalp <= joinedp) {
            value = 100;
        }
        String val = String.valueOf(value);
        if (val.contains(".")){
            val = val.substring(0,val.indexOf("."));
        }
        int progress = Integer.valueOf(val);
        return progress;
    }

    public static String spotsLeft(String total, String joined) {
        double totalp = Double.valueOf(total);
        double joinedp = Double.valueOf(joined);
        double value = totalp - joinedp;

        return decimalFormal(value);
    }

    public static String decimalFormal(double val) {
        return new DecimalFormat("##.##").format(val);
    }



    public static String encodeToBase64(Context context, Uri imgUri, Bitmap.CompressFormat compressFormat, int quality) {
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }




    public static String parseDate(String time, String input, String output) {
        String convertedDate = time;
        String inputPattern = input;
        String outputPattern = output;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(convertedDate);
            str = outputFormat.format(date);
        } catch (ParseException p) {
            p.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.format(cal.getTime());
    }



    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    public static boolean checkIfAlreadyhavePermission(Activity activity, String permission) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static void requestForSpecificPermission(Activity activity, String[] granted_permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(granted_permission, 101);
        }
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void createAlertDialog(Activity activity, Context context, String title, String message, String buttonPositive, String buttonNegative
            , final DialogSelectListener listener) {
     /*   final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.layout_dialog_alert, null);
        alertDialog.setView(dialogView);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(false);
        final TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        final TextView tv_message = (TextView) dialogView.findViewById(R.id.tv_message);
        final TextView tv_negative = (TextView) dialogView.findViewById(R.id.tv_negative);
        final TextView tv_btn = (TextView) dialogView.findViewById(R.id.tv_btn);
        final LinearLayout btn_dialog_positive = (LinearLayout) dialogView.findViewById(R.id.btn_dialog_positive);
        final LinearLayout btn_dialog_negative = (LinearLayout) dialogView.findViewById(R.id.btn_dialog_negative);

        tv_message.setText(message);
        tv_negative.setText(buttonNegative);
        tv_btn.setText(buttonPositive);

        if (!TextUtils.isEmpty(buttonNegative)) {
            tv_negative.setVisibility(View.VISIBLE);
            tv_negative.setText(buttonNegative);
        } else {
            tv_negative.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(View.GONE);
        }

        btn_dialog_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_dialog_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClicked("");
            }
        });

        dialog.show();*/
    }

    public static void createAlertDialog(Activity activity, String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog = builder.create();
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void setupUI(final View view, final Activity activity, final boolean flag) {

        try {
            if (flag) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            } else {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        view.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
        }
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (flag) {
                        hideSoftKeyboard(activity, view);
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, activity, flag);
            }
        }
    }

    public static void setupDialogUI(final View view, final Activity activity, final boolean flag) {

        try {
            if (flag) {
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        view.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
        }
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (flag) {
                        hideSoftKeyboard(activity, view);
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupDialogUI(innerView, activity, flag);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity, View v) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                v.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }


    public static String setNameCaps(String myString) {
        if (myString == null || TextUtils.isEmpty(myString)) {
            return "";
        }
        myString = myString.trim().replace(" ", ":");
        String nameText = "";
        if (myString.contains(":")) {
            String[] array = myString.split(":");
            for (int i = 0; i < array.length; i++) {
                String text = array[i];
                if (i > 0) {
                    nameText = nameText + " " + text.substring(0, 1).toUpperCase() + text.substring(1);
                } else {
                    nameText = text.substring(0, 1).toUpperCase() + text.substring(1);
                }
            }


        } else {
            nameText = myString.substring(0, 1).toUpperCase() + myString.substring(1);
        }


        return nameText;
    }

    public static String setFirstLetterCaps(String myString) {
        String letter = "";
        letter = myString.substring(0, 1).toUpperCase();
        return letter;
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String getCurrentTime(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String formattedTime = tf.format(c);
        return formattedTime;
    }
}
