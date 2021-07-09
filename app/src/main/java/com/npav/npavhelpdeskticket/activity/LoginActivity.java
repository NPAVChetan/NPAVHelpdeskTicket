package com.npav.npavhelpdeskticket.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.api.RetrofitClient;
import com.npav.npavhelpdeskticket.pojo.User;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.CustomProgressDialog;
import com.npav.npavhelpdeskticket.util.SharedPref;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.npav.npavhelpdeskticket.util.CommonMethods.hideProgressDialog;
import static com.npav.npavhelpdeskticket.util.CommonMethods.showProgressDialog;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText txtUserName, txtPassword;
    Context context = this;
    String Username = "";
    String Password = "";
    public static SharedPreferences User_Login_Info;
    TextView txt_help;
    ImageView help_wApp;
    Spinner spinner;
    String prefix;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        SharedPref.init(this);
        User_Login_Info = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);

        String isLoggedIn = User_Login_Info.getString("isLoggedIn", "");

        /*this.finish();
        Intent in1 = new Intent(context, TicketChatActivity.class);
        startActivity(in1);*/

        if (isLoggedIn.equalsIgnoreCase("yes")) {
            this.finish();
            Intent in = new Intent(context, MainActivity.class);
            startActivity(in);
        }
        findViewAllById();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUserName.getText().toString().equals(""))
                    txtUserName.setError("required");
                else if (txtUserName.getText().toString().length() < 10)
                    txtUserName.setError("10 digit required");
                else if (txtPassword.getText().toString().equals(""))
                    txtPassword.setError("required");
                else if (CustomProgressDialog.isInternetAvailable(context)) {
                    try {
                        getFields();
                        try {
                            User userLoginDetails = new User();
                            userLoginDetails.setPhone(Username);
                            userLoginDetails.setPassword(Password);
                            callLoginAPI(userLoginDetails);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Internet Is Not Available.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        txt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=" + "+918669617057" + "&text=";
                sendMsg.setPackage("com.whatsapp");
                sendMsg.putExtra(Intent.EXTRA_TEXT, "Hi! Welcome to Net Protector support");
                sendMsg.setData(Uri.parse(url));
                if (sendMsg.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(sendMsg);
                }
            }
        });

        help_wApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=" + "+918669617057" + "&text=";
                sendMsg.setPackage("com.whatsapp");
                sendMsg.putExtra(Intent.EXTRA_TEXT, "Hi! Welcome to Net Protector support");
                sendMsg.setData(Uri.parse(url));
                if (sendMsg.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(sendMsg);
                }
            }
        });

        spinner.setSelection(98);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryCode = parent.getItemAtPosition(position).toString();
                String[] strArray = countryCode.split("\\+");
                prefix = strArray[1].trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPermissions()) {
            // permissions granted.
        } else {
            // show dialog informing them that we lack certain permissions
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void callLoginAPI(User userLoginDetails) {
        showProgressDialog(context, "");
        Call<User> call = RetrofitClient.getInstance().getMyApi().login(userLoginDetails);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User userdata = response.body();
                String status = null;
                String info = null;
                User.Data data = null;

                if (userdata != null) {
                    status = userdata.getStatus();
                    info = userdata.getInfo();
                    Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();
                    data = userdata.getData();
                }
                if (status != null) {
                    if (status.equals("true")) {
                        String token = data.getAccessToken();
                        JWT jwt = new JWT(token);
                        String key, value;
                        Map<String, Claim> allClaims = jwt.getClaims();

                        for (Map.Entry<String, Claim> entry : allClaims.entrySet()) {
                            key = entry.getKey();
                            value = entry.getValue().asString();
                            SharedPreferences.Editor editor = User_Login_Info.edit();
                            editor.putString(key, value);
                            editor.apply();
                        }

                        SharedPreferences.Editor editor = User_Login_Info.edit();
                        editor.putString("token", token);
                        editor.putString("Mobile", prefix + Username);
                        editor.putString("Password", Password);
                        editor.putString("isLoggedIn", "yes");
                        editor.apply();
                        startActivity(new Intent(context, MainActivity.class));
                        finish();

                    } else if (status.equals("false")) {
                        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("info"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void findViewAllById() {
        btn_login = (Button) findViewById(R.id.btn_login);
        txtUserName = (EditText) findViewById(R.id.txt_Username);
        txtPassword = (EditText) findViewById(R.id.txt_Password);
        txt_help = (TextView) findViewById(R.id.txt_help);
        help_wApp = (ImageView) findViewById(R.id.help_wApp);
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    public void getFields() {
        Username = txtUserName.getText().toString().trim();
        Username = prefix + Username;
        Password = txtPassword.getText().toString().trim();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    // no permissions granted.
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to Exit App?")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
