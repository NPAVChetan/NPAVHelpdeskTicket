package com.npav.npavhelpdeskticket.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.activity.LoginActivity;
import com.npav.npavhelpdeskticket.adapter.TicketListAdapter;
import com.npav.npavhelpdeskticket.api.APIInterface;
import com.npav.npavhelpdeskticket.api.RetrofitClient;
import com.npav.npavhelpdeskticket.database.DatabaseHandler;
import com.npav.npavhelpdeskticket.pojo.Tickets;
import com.npav.npavhelpdeskticket.util.CommonMethods;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.SharedPref;
import com.npav.npavhelpdeskticket.util.onClickInterface;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.npav.npavhelpdeskticket.util.CommonMethods.hideProgressDialog;
import static com.npav.npavhelpdeskticket.util.CommonMethods.showProgressDialog;

public class AssignedTicketFragment extends Fragment {

    TicketListAdapter ticketListAdapter;
    private onClickInterface onclickInterface;
    List<Tickets.Data> ticketList = new ArrayList<>();
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    DatabaseHandler db;
    TextView empty_view;
    private static SharedPreferences User_Login_Info;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_assigned_ticket, container, false);
        empty_view = root.findViewById(R.id.empty_view);
        recyclerView = root.findViewById(R.id.rv_ticket_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        SharedPref.init(getActivity());
        User_Login_Info = getActivity().getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {
//                Toast.makeText(getActivity(), "Position is" + abc, Toast.LENGTH_LONG).show();
                Tickets.Data obj = ticketList.get(abc);
                String ticket_id = String.valueOf(obj.getTicket_id());
                String ticket_type = obj.getTicket_type();
                Constants.KEY_TICKET_ID = ticket_id;
                Constants.KEY_TICKET_TYPE = ticket_type;
                ticketListAdapter.notifyDataSetChanged();
            }
        };

        if (CommonMethods.CheckInternetConnection(getActivity())) {
            callGetTicketAPI();
        } else {
            /*db = new DatabaseHandler(getActivity());
            ticketList = db.getAllTickets1();
            if (ticketList.size() > 0) {
                ticketListAdapter = new TicketListAdapter(getActivity(), ticketList, onclickInterface);
                recyclerView.setAdapter(ticketListAdapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                empty_view.setVisibility(View.VISIBLE);
            }*/
        }
        return root;
    }

    public void callGetTicketAPI() {
//        Toast.makeText(getActivity(), "Assigned To Me", Toast.LENGTH_SHORT).show();
        showProgressDialog(getActivity(), "");
        sharedpreferences = getActivity().getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date pre_date = new Date();
        pre_date.setDate(pre_date.getDate() - 10);
        String start = formatter.format(pre_date);
        Date post_date = new Date();
        post_date.setDate(post_date.getDate() + 2);
        String end = formatter.format(post_date);

        String url = APIInterface.BASE_URL + "api/ticket/web/list/active/" + start + "/" + end;
        Call<Tickets> call = RetrofitClient.getInstance().getMyApi().getticketlist1("bearer " + token, url);
        call.enqueue(new Callback<Tickets>() {
            @Override
            public void onResponse(Call<Tickets> call, Response<Tickets> response) {
                Tickets ticketdata = response.body();
                String status = null;
                String info = null;

                if (ticketdata != null) {
                    status = ticketdata.getStatus();
                    info = ticketdata.getInfo();
                    Tickets.Data[] arr_data = ticketdata.getData();
                    ticketList = Arrays.asList(arr_data);
                }
                if (status != null) {
                    if (status.equals("true")) {
                        if (ticketList.size() > 0) {
                            // Add or Update all tickets to database
                            empty_view.setVisibility(View.GONE);
                            db = new DatabaseHandler(getActivity());
                            db.add_or_update_Ticket1(ticketList);
                            ticketListAdapter = new TicketListAdapter(getActivity(), ticketList, onclickInterface);
                            recyclerView.setAdapter(ticketListAdapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            empty_view.setVisibility(View.VISIBLE);
                        }

                    } else if (status.equals("false")) {
                        if (info.equalsIgnoreCase("TokenExpiredError")) {
                            SharedPreferences.Editor editor = User_Login_Info.edit();
                            editor.putString("isLoggedIn", "");
                            editor.apply();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        info = jObjError.getString("info");
                        if (info.equalsIgnoreCase("TokenExpiredError")) {
                            SharedPreferences.Editor editor = User_Login_Info.edit();
                            editor.putString("isLoggedIn", "");
                            editor.apply();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), jObjError.getString("info"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<Tickets> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
