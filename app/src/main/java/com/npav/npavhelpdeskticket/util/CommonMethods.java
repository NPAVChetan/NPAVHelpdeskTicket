package com.npav.npavhelpdeskticket.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class CommonMethods {

    private static final int CONST_NO_NETWORK = 525138;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static HashMap<Integer, String> map = new HashMap<Integer, String>();
    private static int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static ProgressDialog progressDialog;

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity
                        .INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                            .getWindowToken(), 0);
                }
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        }
    }

    public static Status getConnectivityStatus(Context context) {
        ConnectivityManager cm = ContextCompat.getSystemService(context, ConnectivityManager.class);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        switch (activeNetwork != null ? activeNetwork.getType() : CONST_NO_NETWORK) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_ETHERNET:
                if (cm.isActiveNetworkMetered())
                    return Status.MOBILE; // respect metered wifi networks as mobile
                return Status.WIFI;
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_BLUETOOTH:
            case ConnectivityManager.TYPE_WIMAX:
                return Status.MOBILE;
            default:
                return Status.NONE;
        }
    }

    public enum Status {
        /**
         * Operating on a wireless connection
         */
        WIFI,
        /**
         * Operating on 3G, 4G, 4G LTE, etc.
         */
        MOBILE,
        /**
         * No connection present
         */
        NONE
    }

    public static boolean CheckInternetConnection(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void setMap(Integer key, String value) {
        map.put(key, value);
    }

    public static HashMap<Integer, String> getMapKeyValue() {
        return map;
    }

    public static void showOKAlertDialog(Context context, String title, String message) {
        new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK",
                        (dialog, which) -> {
                        }).show();
    }

    public static Bitmap getCircleBitmap(Bitmap bm) {
        int sice = Math.min((bm.getWidth()), (bm.getHeight()));
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, sice, sice);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static void hideProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public static void showProgressDialog(Context mContext, String msg) {
        progressDialog = new ProgressDialog(mContext);
        if (msg != null && !msg.isEmpty()) {
            progressDialog.setMessage(msg);
        } else {
            progressDialog.setMessage("Loading...");
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /*private fun setErrorOnSearchView(searchView: SearchView, errorMessage:
            String) {
        val id = searchView.context
                .resources
                .getIdentifier("android:id/search_src_text", null, null)
        val editText = searchView.find<EditText>(id)

                val errorColor = ContextCompat.getColor(this, R.color.red)
        val fgcspan = ForegroundColorSpan(errorColor)
        val builder = SpannableStringBuilder(errorMessage)
        builder.setSpan(fgcspan, 0, errorMessage.length, 0)
        editText.error = builder
    }*/

    public static String friendlyTimeDiff(long timeDifferenceMilliseconds) {
        long diffSeconds = timeDifferenceMilliseconds / 1000;
        long diffMinutes = timeDifferenceMilliseconds / (60 * 1000);
        long diffHours = timeDifferenceMilliseconds / (60 * 60 * 1000);
        long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
        long diffWeeks = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 7);
        long diffMonths = (long) (timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 30.41666666));
        long diffYears = timeDifferenceMilliseconds / ((long) 60 * 60 * 1000 * 24 * 365);

        if (diffSeconds < 1) {
            return "just now"; // less than a second
        } else if (diffMinutes < 1) {
            return "just now";
//            return diffSeconds + " seconds ago";
        } else if (diffHours < 1) {
            return diffMinutes + " minutes ago";
        } else if (diffDays < 1) {
            return diffHours + " hours ago";
        } else if (diffWeeks < 1) {
            return diffDays + " days ago";
        } else if (diffMonths < 1) {
            return diffWeeks + " weeks";
        } else if (diffYears < 1) {
            return diffMonths + " months";
        } else {
            return diffYears + " years";
        }
    }

}
