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
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.SharedPref;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    public static SharedPreferences sharedPreferences;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView txtName = headerView.findViewById(R.id.txt_name);
        TextView txtEmail = headerView.findViewById(R.id.txt_email);

        SharedPref.init(this);
        sharedPreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);

        String name_first = sharedPreferences.getString("name_first", "");
        String name_last = sharedPreferences.getString("name_last", "");
        String name = name_first + " " + name_last;
        String email = sharedPreferences.getString("email", "");
        txtName.setText(name);
        txtEmail.setText(email);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_assigned_to_me, R.id.nav_all_active, R.id.nav_closed, R.id.nav_deleted, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            assert navHostFragment != null;
            NavController navController = navHostFragment.getNavController();
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
            if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("Assigned To Me")) {
                navGraph.setStartDestination(R.id.nav_assigned_to_me);
                navController.setGraph(navGraph);

            } else if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("Closed")) {
                navGraph.setStartDestination(R.id.nav_closed);
                navController.setGraph(navGraph);

            } else if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("Deleted")) {
                navGraph.setStartDestination(R.id.nav_deleted);
                navController.setGraph(navGraph);

            } else if (Constants.KEY_SELECTED_MENU.equalsIgnoreCase("All Active")) {
                ((NavGraph) navGraph).setStartDestination(R.id.nav_all_active);
                navController.setGraph(navGraph);

            }
            NavigationUI.setupWithNavController(navigationView, navController);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getLabel() == null) throw new AssertionError();
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
//                        System.exit(0);
                        finishAffinity();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
