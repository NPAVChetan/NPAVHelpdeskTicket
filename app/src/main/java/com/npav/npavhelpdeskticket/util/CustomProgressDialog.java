package com.npav.npavhelpdeskticket.util;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.npav.npavhelpdeskticket.R;


public class CustomProgressDialog {

    public static Dialog progressDialog;

    public CustomProgressDialog() {
        // TODO Auto-generated constructor stub

    }

    public static void showProgressBar(Context context, String strMsg, boolean cancelable) {

        // Show progress Bar when Required
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(cancelable);

        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_progress_dialog, null);
        TextView txtSetMsg = (TextView) v.findViewById(R.id.txtProgressMessage);
        txtSetMsg.setText(strMsg);
        progressDialog.setContentView(v);
        progressDialog.show();

    }

    public static void dismissProgressBar() {
        // Close progress Bar After Use
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static boolean isInternetAvailable(Context context){
        // Check Internet connection is ON or OFF
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        int flag = 1;
        if (networkInfo != null) {
            if (!networkInfo.isAvailable()) {
                flag = 0;
            }
            if (!networkInfo.isConnected()) {
                flag = 0;
            }
        } else {
            flag = 0;
        }
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }


    public boolean isValidWord(String word) {

        return word.matches("[A-Za-z][^.]*");
    }

    boolean isNumber(String string) {
        try {
            int amount = Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

   /* private void loadImageThumbnailRequest() {
// setup Glide request without the into() method
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with( context )
                .load( yourData.getLowQualityLink() );

// pass the request as a a parameter to the thumbnail request
        Glide
                .with( context )
                .load( yourData.getHdUrl() )
                .thumbnail( thumbnailRequest )
                .into( imageView );
    }



    Transformation blurTransformation = new Transformation() {
    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap blurred = Blur.fastblur(LiveImageView.this.context, source, 10);
        source.recycle();
        return blurred;
    }

    @Override
    public String key() {
        return "blur()";
    }
    };

Picasso.with(context)
    .load(thumbUrl) // thumbnail url goes here
    .placeholder(R.drawable.placeholder)
    .resize(imageViewWidth, imageViewHeight)
    .transform(blurTransformation)
    .into(imageView, new Callback() {
        @Override
        public void onSuccess() {
            Picasso.with(context)
                    .load(url) // image url goes here
                    .resize(imageViewWidth, imageViewHeight)
                    .placeholder(imageView.getDrawable())
                    .into(imageView);
        }

        @Override
        public void onError() {
        }
    });


    File f = new File("./file_name");
if(f.exists()){
    System.out.println("success");
}
else{
    System.out.println("fail");
}
*/
}
