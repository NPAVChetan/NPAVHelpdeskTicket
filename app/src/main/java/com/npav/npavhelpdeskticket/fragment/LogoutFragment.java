package com.npav.npavhelpdeskticket.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.activity.LoginActivity;
import com.npav.npavhelpdeskticket.activity.MainActivity;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.SharedPref;

public class LogoutFragment extends Fragment {

    private static SharedPreferences User_Login_Info;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        final TextView textView = root.findViewById(R.id.text_logout);
        textView.setText("Logout");

        SharedPref.init(getActivity());
        User_Login_Info = requireActivity().getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure want to Log Out?")
                .setCancelable(false)
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    Constants.KEY_SELECTED_MENU = "Assigned To Me";
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    requireActivity().finish();
                })
                .setPositiveButton("Yes", (dialog, id) -> {
                    SharedPreferences.Editor editor = User_Login_Info.edit();
                    editor.putString("isLoggedIn", "");
                    editor.apply();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    requireActivity().finish();
                });

        AlertDialog alert = builder.create();
        alert.show();
        return root;
    }
}
