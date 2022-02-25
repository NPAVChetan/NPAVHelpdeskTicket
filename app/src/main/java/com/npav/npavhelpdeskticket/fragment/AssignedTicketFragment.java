package com.npav.npavhelpdeskticket.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.pojo.Tickets;
import com.npav.npavhelpdeskticket.util.CommonMethods;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.SharedPref;
import com.npav.npavhelpdeskticket.util.onClickInterface;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
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
    private static final String TAG = AssignedTicketFragment.class.getName();
    TicketListAdapter ticketListAdapter;
    private onClickInterface onclickInterface;
    List<Tickets.Data> ticketList = new ArrayList<>();
    RecyclerView recyclerView;
    private SharedPreferences sharedpreferences;
    DatabaseHandler db;
    TextView empty_view;
    private WebSocketClient mWebSocketClient;
    private static URI mServerUri;
    private static boolean tryReconnecting = false;
    private int mCountForReconnect = 0;
    private String agent_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_assigned_ticket, container, false);
        empty_view = root.findViewById(R.id.empty_view);
        recyclerView = root.findViewById(R.id.rv_ticket_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        SharedPref.init(getActivity());
        sharedpreferences = requireActivity().getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        agent_id = sharedpreferences.getString("agent_id", "");

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {
                if (mWebSocketClient.isOpen()) {
                    mWebSocketClient.close(1000, "TL WS"); // TL Ws -> Ticket list Web socket
                    Log.e("TL Websocket", "Closed at " + System.currentTimeMillis());
                }
                Tickets.Data obj = ticketList.get(abc);
                String ticket_id = String.valueOf(obj.getTicket_id());
                String ticket_type = obj.getTicket_type();
                Constants.KEY_TICKET_ID = ticket_id;
                Constants.KEY_TICKET_TYPE = ticket_type;
                ticketListAdapter.notifyDataSetChanged();
            }
        };

        if (CommonMethods.CheckInternetConnection(requireActivity())) {
            callGetTicketAPI();
            initSocket("wss://messaging.support.test.netprotector.net/agent/android/" + agent_id);
        } else {
            Log.d(TAG, "Retrieve data from SQLITE");
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

    private void callGetTicketAPI() {
        showProgressDialog(getActivity(), "");
        sharedpreferences = getActivity().getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date pre_date = new Date();
        pre_date.setDate(pre_date.getDate() - 200);
        String start = formatter.format(pre_date);
        Date post_date = new Date();
        post_date.setDate(post_date.getDate() + 2);
        String end = formatter.format(post_date);

        String url = APIInterface.BASE_URL + "api/ticket/web/list/active/" + start + "/" + end;
        Call<Tickets> call = RetrofitClient.getInstance().getMyApi().getticketlist("bearer " + token, url);
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
                    if (arr_data != null) {
                        ticketList = new ArrayList<>(Arrays.asList(arr_data));
                    }
                    //                    ticketList = Arrays.asList(arr_data);
                }
                if (status != null) {
                    if (status.equals("true")) {
                        if (ticketList.size() > 0) {
                            // Add or Update all tickets to database
                            empty_view.setVisibility(View.GONE);
//                            db = new DatabaseHandler(getActivity());
//                            db.add_or_update_Ticket1(ticketList);
                            ticketListAdapter = new TicketListAdapter(getActivity(), ticketList, onclickInterface, TAG);
                            recyclerView.setAdapter(ticketListAdapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            empty_view.setVisibility(View.VISIBLE);
                        }

                    } else if (status.equals("false")) {
                        if (info.equalsIgnoreCase("TokenExpiredError")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
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
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("isLoggedIn", "");
                            editor.apply();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        } else if (info.equalsIgnoreCase("invalid signature")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
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

    private void initSocket(String url) {
        if (mWebSocketClient == null) {
            URI uri = null;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mServerUri = uri;
            if (mServerUri != null) {
                createWebSocket();
            }
        } else {
            reconnectWebSocket();
        }
    }

    private synchronized void reconnectWebSocket() {
        Log.e("Websocket", "reconnectWebSocket at " + System.currentTimeMillis());
        if (tryReconnecting) {
            return;
        }
        if (mWebSocketClient == null) {
            createWebSocket();
        } else if (!mWebSocketClient.isOpen()) {
            tryReconnecting = true;
            invocarDentroDelTimerEspecial();
        }
    }

    private void invocarDentroDelTimerEspecial() {
        int delay;
        if (mWebSocketClient == null) {
            createWebSocket();
            delay = 1;
            mCountForReconnect = 1;
        } else if (mWebSocketClient.isClosed()) {
            mWebSocketClient.reconnect();
            mCountForReconnect++;
            delay = CommonMethods.getCurrentFibonacci(mCountForReconnect);
        } else if (mWebSocketClient.isOpen()) {
            tryReconnecting = false;
            mCountForReconnect = 1;
            delay = 0;
        } else {
            mCountForReconnect++;
            delay = CommonMethods.getCurrentFibonacci(mCountForReconnect);
        }
        if (delay != 0) {
            handler.postDelayed(runnableTimer, delay * 1000);
        }
    }

    private void createWebSocket() {
        mWebSocketClient = new WebSocketClient(mServerUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.e("Websocket", "Opened at " + System.currentTimeMillis());
            }

            @Override
            public void onMessage(String message) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("Websocket", "onMessage " + message);
                    try {
                        Ticket.Data.TicketDetails obj = new Ticket.Data.TicketDetails();
                        JSONArray jsonArray = new JSONArray(message);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int ticket_ID = Integer.parseInt(jsonObject.getString("ticket_id"));
                        obj.setTicket_id(ticket_ID);
                        int agent_id = Integer.parseInt(jsonObject.getString("agent_id"));
                        obj.setAgent_id(agent_id);
                        String ticket_type = jsonObject.getString("ticket_type");
                        obj.setTicket_type(ticket_type);
                        String department = jsonObject.getString("department");
                        obj.setDepartment(department);
                        String ticket_status = jsonObject.getString("ticket_status");
                        obj.setTicket_status(ticket_status);
                        String time_stamp_ticket_generated = jsonObject.getString("time_stamp_ticket_generated");
                        obj.setTime_stamp_ticket_generated(time_stamp_ticket_generated);
                        String time_stamp_ticket_resolved = jsonObject.getString("time_stamp_ticket_resolved");
                        obj.setTime_stamp_ticket_resolved(time_stamp_ticket_resolved);
                        String tags = jsonObject.getString("tags");
                        String[] tg = new String[1];
                        tg[0] = tags;
                        obj.setTags(tg);
                        int customer_id = Integer.parseInt(jsonObject.getString("customer_id"));
                        obj.setCustomer_id(customer_id);
                        String name_first = jsonObject.getString("name_first");
                        obj.setName_first(name_first);
                        String name_last = jsonObject.getString("name_last");
                        obj.setName_last(name_last);
                        String city = jsonObject.getString("city");
                        obj.setCity(city);
                        String phone = jsonObject.getString("phone");
                        obj.setPhone(phone);
                        String email = jsonObject.getString("email");
                        obj.setEmail(email);
                        String comment = jsonObject.getString("comment");
                        obj.setComment(comment);
                        String tag = jsonObject.getString("tag");
                        obj.setTag(tag);
                        String telegram_username = jsonObject.getString("telegram_username");
                        obj.setTelegram_username(telegram_username);
                        String time_stamp_customer_created = jsonObject.getString("time_stamp_customer_created");
                        obj.setTime_stamp_customer_created(time_stamp_customer_created);
                        int message_id = Integer.parseInt(jsonObject.getString("message_id"));
                        obj.setMessage_id(message_id);
                        String message_type = jsonObject.getString("message_type");
                        obj.setMessage_type(message_type);
                        String message_text = jsonObject.getString("message_text");
                        obj.setMessage_text(message_text);
                        String time_stamp = jsonObject.getString("time_stamp");
                        obj.setTime_stamp(time_stamp);
                        String name_message_sender = jsonObject.getString("name_message_sender");
                        obj.setName_message_sender(name_message_sender);
                        String attachment_id = jsonObject.getString("attachment_id");
                        obj.setAttachment_id(attachment_id);
                        String url_download = jsonObject.getString("url_download");
                        obj.setUrl_download(url_download);
                        String mime_type = jsonObject.getString("mime_type");
                        obj.setMime_type(mime_type);
                        List<Tickets.Data> newTicketList = new ArrayList<>();
                        for (Tickets.Data data : ticketList) {
                            newTicketList.add(data.clone());
                        }
                        for (Tickets.Data data : newTicketList) {
                            if (data.ticket_id == ticket_ID) {
                                data.message_text = message_text;
                                data.time_stamp_latest_message = time_stamp;
                            }
                        }
                        ticketListAdapter.setUpdatedTicketList(newTicketList);
                    } catch (Exception err) {
                        Log.d("Error", err.toString());
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e("Websocket", "Closed at " + System.currentTimeMillis());
                if (reason.equalsIgnoreCase("TL WS")) {
                    mWebSocketClient = null;
                } else {
                    reconnectWebSocket();
                }
            }

            @Override
            public void onError(Exception ex) {
                Log.e("Websocket", "Error " + ex.getMessage());
                reconnectWebSocket();
            }
        };
        mWebSocketClient.connect();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume: " + System.currentTimeMillis());
        if (CommonMethods.CheckInternetConnection(requireActivity())) {
//            callGetTicketAPI();
//            initSocket("wss://support.test.netprotector.net/android/" + agent_id);
            initSocket("wss://messaging.support.test.netprotector.net/agent/android/" + agent_id);
        }
        super.onResume();
    }


    private final Handler handler = new Handler();
    private final Runnable runnableTimer = this::invocarDentroDelTimerEspecial;
}
