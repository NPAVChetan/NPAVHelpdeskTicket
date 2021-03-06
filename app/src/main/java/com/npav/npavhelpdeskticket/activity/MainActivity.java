package com.npav.npavhelpdeskticket.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.fragment.ActiveTicketFragment;
import com.npav.npavhelpdeskticket.fragment.AssignedTicketFragment;
import com.npav.npavhelpdeskticket.fragment.ClosedTicketFragment;
import com.npav.npavhelpdeskticket.fragment.DeletedTicketFragment;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.SharedPref;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    public static SharedPreferences User_Login_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView txtName = (TextView) headerView.findViewById(R.id.txt_name);
        TextView txtEmail = (TextView) headerView.findViewById(R.id.txt_email);

        SharedPref.init(this);
        User_Login_Info = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);

        String name_first = User_Login_Info.getString("name_first", "");
        String name_last = User_Login_Info.getString("name_last", "");
        String name = name_first + " " + name_last;
        String email = User_Login_Info.getString("email", "");
        txtName.setText(name);
        txtEmail.setText(email);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_all_active, R.id.nav_assigned_to_me, R.id.nav_closed, R.id.nav_deleted, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Fragment fragment;
                if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("Assigned To Me")) {
                    fragment = new AssignedTicketFragment();
                    moveToFragment(fragment);
                } else if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("Closed")) {
                    fragment = new ClosedTicketFragment();
                    moveToFragment(fragment);
                } else if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("Deleted")) {
                    fragment = new DeletedTicketFragment();
                    moveToFragment(fragment);
                } else if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("All Active")) {
                    fragment = new ActiveTicketFragment();
                    moveToFragment(fragment);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment)
                .replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName()).commit();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Constants.KEY_SELECTED_MENU = destination.getLabel().toString();
                Log.e(TAG, "onDestinationChanged: " + destination.getLabel());
            }
        });
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
