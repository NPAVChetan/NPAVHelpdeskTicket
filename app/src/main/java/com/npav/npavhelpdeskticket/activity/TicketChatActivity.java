package com.npav.npavhelpdeskticket.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.adapter.TicketChatAdapter;
import com.npav.npavhelpdeskticket.api.APIInterface;
import com.npav.npavhelpdeskticket.api.RetrofitClient;
import com.npav.npavhelpdeskticket.pojo.Assign;
import com.npav.npavhelpdeskticket.pojo.PostMessage;
import com.npav.npavhelpdeskticket.pojo.PostNote;
import com.npav.npavhelpdeskticket.pojo.Tag;
import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.pojo.Tickets;
import com.npav.npavhelpdeskticket.util.CommonMethods;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.FileUtils;
import com.npav.npavhelpdeskticket.util.onClickInterface;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.npav.npavhelpdeskticket.util.CommonMethods.hideProgressDialog;
import static com.npav.npavhelpdeskticket.util.CommonMethods.showProgressDialog;

public class TicketChatActivity extends AppCompatActivity {
    private static final String TAG = TicketChatActivity.class.getSimpleName();
    TicketChatAdapter ticketChatAdapter;
    List<Ticket.Data.TicketDetails> mTicketChatList = new ArrayList<>();
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    EditText editTextPostMessage, editTextAddNote;
    ImageButton imgBtnSendMsg, imgBtnSendNote, imgBtnMsgAttachment, imgBtnNoteAttachment;
    String ticket_id, ticket_Type;
    LinearLayout layout_write_message, layout_add_note;
    private String imagepath = null;
    Uri selectedImageUri;
    Bitmap bitmap;
    File sourceFile;
    Context mContext;
    String curr_time;
    private onClickInterface onclickInterface;
    int current_agent_id = 0;
    private WebSocketClient mWebSocketClient;
    private static URI mServerUri;
    private static boolean tryReconnecting = false;
    private int mCountForReconnect = 0;
    String agent_id;
    String token;
    private TextView txt_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_chat);
        mContext = TicketChatActivity.this;
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ticket Details");
        layout_write_message = findViewById(R.id.layout_create_tag);
        editTextPostMessage = findViewById(R.id.editTextPostMessage);
        imgBtnSendMsg = findViewById(R.id.imgBtnSendMsg);
        imgBtnMsgAttachment = findViewById(R.id.imgBtnMsgAttchment);
        imgBtnNoteAttachment = findViewById(R.id.imgBtnNoteAttchment);

        layout_add_note = findViewById(R.id.layout_add_note);
        editTextAddNote = findViewById(R.id.editTextAddNote);
        imgBtnSendNote = findViewById(R.id.imgBtnSendNote);

        recyclerView = findViewById(R.id.rv_messages_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TicketChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        txt_status = findViewById(R.id.txt_status);
        sharedpreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        agent_id = sharedpreferences.getString("agent_id", "");
        token = sharedpreferences.getString("token", "");

        if (CommonMethods.CheckInternetConnection(TicketChatActivity.this)) {
            ticket_id = Constants.KEY_TICKET_ID;
            ticket_Type = Constants.KEY_TICKET_TYPE;
            callGetTicketDetailsAPI(ticket_id);
//            initSocket("wss://support.test.netprotector.net/android/" + agent_id);
            initSocket("wss://messaging.support.test.netprotector.net/agent/android/" + agent_id);
        } else {
            Toast.makeText(getApplicationContext(), "Internet Is Not Available.", Toast.LENGTH_SHORT).show();
        }

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {
            }
        };

        // Message text and attachment.
        imgBtnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postMessage = editTextPostMessage.getText().toString();
                if (postMessage.length() > 0) {
                    if (CommonMethods.CheckInternetConnection(TicketChatActivity.this)) {
                        callAPIPostMessage(postMessage);
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Is Not Available.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Message should not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgBtnMsgAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectImage();
//                Constants.KEY_ATTACHMENT_TYPE = "Message";
                Constants.KEY_ATTACHMENT_TYPE = "Message";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
            }
        });

        // Note text and attachment.
        imgBtnSendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postNote = editTextAddNote.getText().toString();
                if (postNote.length() > 0) {
                    if (CommonMethods.CheckInternetConnection(TicketChatActivity.this)) {
                        callAPIAddNote(postNote);
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Is Not Available.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Note should not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgBtnNoteAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.KEY_ATTACHMENT_TYPE = "Note";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
            }
        });
    }

    private void initSocket(String url) {
        if (mWebSocketClient == null) {
            URI uri = null;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                Log.d(TAG, "initSocket: ");
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
        Log.e("TicketChat Websocket", "reconnectWebSocket at " + System.currentTimeMillis());
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
                Log.e("TicketChat Websocket", "Opened at " + System.currentTimeMillis());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_status.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onMessage(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TicketChat Websocket", "onMessage " + s);
                        if (mTicketChatList.size() > 0) {
                            try {
                                Ticket.Data.TicketDetails obj = new Ticket.Data.TicketDetails();
                                JSONArray jsonArray = new JSONArray(s);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                int ticket_ID = Integer.parseInt(jsonObject.getString("ticket_id"));
                                obj.setTicket_id(ticket_ID);
                                int openedChatTicketId = Integer.parseInt(ticket_id);
                                if (openedChatTicketId == ticket_ID) {
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
                                    List<Ticket.Data.TicketDetails> mTicketChatLastMsg = new ArrayList<>();
                                    mTicketChatLastMsg.add(obj);
                                    ticketChatAdapter.addAll(mTicketChatLastMsg);
                                    recyclerView.smoothScrollToPosition(mTicketChatList.size() - 1);
                                }
                            } catch (Exception err) {
                                Log.d(TAG, "run: " + err.toString());
                            }
                        }
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_status.setVisibility(View.VISIBLE);
                        }
                    });
                    Log.e("TicketChat Websocket", "Closed at " + System.currentTimeMillis());
                    if (reason.equalsIgnoreCase("CT WS")) {
                        mWebSocketClient = null;
                    } else {
                        reconnectWebSocket();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_status.setVisibility(View.VISIBLE);
                    }
                });
                Log.e("TicketChat Websocket", "Error " + ex.getMessage());
                reconnectWebSocket();
            }
        };
        mWebSocketClient.connect();
    }

    private final Handler handler = new Handler();
    private final Runnable runnableTimer = this::invocarDentroDelTimerEspecial;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String selected_menu = Constants.KEY_SELECTED_MENU;
        if (selected_menu.equalsIgnoreCase("Closed")) {
            menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.ic_delete_24), getResources().getString(R.string.delete_ticket)));
            return true;
        } else if (selected_menu.equalsIgnoreCase("Deleted")) {
            return true;
        } else {
            menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_send_black_24dp), getResources().getString(R.string.write_message)));
            menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_pencil_24), getResources().getString(R.string.add_note)));
            menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.ic_close_24), getResources().getString(R.string.close_ticket)));
            menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.ic_delete_24), getResources().getString(R.string.delete_ticket)));
            menu.add(0, 5, 5, menuIconWithText(getResources().getDrawable(R.drawable.ic_add_tag_24), getResources().getString(R.string.add_tags)));
            menu.add(0, 6, 6, menuIconWithText(getResources().getDrawable(R.drawable.ic_assign_ticket_24), getResources().getString(R.string.assign_ticket)));
            return true;
        }
    }

    private CharSequence menuIconWithText(Drawable r, String title) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
//            bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            if (data.getData() == null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (requestCode == 1 && resultCode == RESULT_OK) {
                selectedImageUri = data.getData();
                imagepath = FileUtils.getPath(this, selectedImageUri);
                bitmap = BitmapFactory.decodeFile(imagepath);
                sourceFile = new File(imagepath);
                String attachment_type = Constants.KEY_ATTACHMENT_TYPE;
                if (attachment_type.equalsIgnoreCase("Message")) {
                    showMessageAttachmentDialog();
                } else if (attachment_type.equalsIgnoreCase("Note")) {
                    showNoteAttachmentDialog();
                }
            } else if (requestCode == 2 && resultCode == RESULT_OK) {
                Toast.makeText(mContext, "Camera action.....", Toast.LENGTH_SHORT).show();
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals(curr_time)) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    sourceFile = f;
                    imagepath = sourceFile.getAbsolutePath();
                    String attachment_type = Constants.KEY_ATTACHMENT_TYPE;
                    if (attachment_type.equalsIgnoreCase("Message")) {
                        showMessageAttachmentDialog();
                    } else if (attachment_type.equalsIgnoreCase("Note")) {
                        showNoteAttachmentDialog();
                    }
                } catch (Exception e) {
                    Log.d(TAG, "onActivityResult: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "onActivityResult: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showMessageAttachmentDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.attachment_custom_dialog, viewGroup, false);
        ImageView iv = dialogView.findViewById(R.id.iv_attach_img);
        EditText et = dialogView.findViewById(R.id.editTextPostMessage);
        ImageButton iBtn = dialogView.findViewById(R.id.imgBtnSendMsg);
        iv.setImageBitmap(bitmap);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        iBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et.getText().toString();
                if (msg.length() > 0) {
                    if (CommonMethods.CheckInternetConnection(TicketChatActivity.this)) {
                        et.getText().clear();
                        alertDialog.dismiss();
                        callAPIPostMessage(msg);
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Is Not Available.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Message should not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showNoteAttachmentDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.attachment_note_dialog, viewGroup, false);
        ImageView iv = dialogView.findViewById(R.id.iv_attach_img_note);
        EditText et = dialogView.findViewById(R.id.editTextAddNote);
        ImageButton iBtn = dialogView.findViewById(R.id.imgBtnSendNote);
        iv.setImageBitmap(bitmap);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        iBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = et.getText().toString();
                if (note.length() > 0) {
                    if (CommonMethods.CheckInternetConnection(TicketChatActivity.this)) {
                        et.getText().clear();
                        alertDialog.dismiss();
                        callAPIAddNote(note);
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Is Not Available.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Note should not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAddTagDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_tag_dialog, viewGroup, false);
        EditText et = dialogView.findViewById(R.id.editTextTag);
        Button btnSave = dialogView.findViewById(R.id.btnSaveTag);
        Button btnCloseDialog = dialogView.findViewById(R.id.btnCloseDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.getText().clear();
                alertDialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTags = et.getText().toString();
                if (strTags.length() > 0) {
                    if (CommonMethods.CheckInternetConnection(TicketChatActivity.this)) {
                        String[] arr_tags = strTags.split(",");
                        et.getText().clear();
                        alertDialog.dismiss();
                        Tag tag = null;
                        try {
                            List<String> allTags = new ArrayList<>();
                            for (int i = 0; i < arr_tags.length; i++) {
                                String st = arr_tags[i];
                                if (st != null)
                                    allTags.add(st);
                            }
                            tag = new Tag();
                            tag.setTicketID(Integer.parseInt(ticket_id));
                            tag.setTags(allTags);
                        } catch (NumberFormatException e) {
                            Log.d(TAG, "onClick: " + e.getMessage());
                            e.printStackTrace();
                        }
                        callAPIAddTags(tag);
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Is Not Available.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Note should not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAssignTicketDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.assign_ticket_dialog, viewGroup, false);
        TextView assignToMe = dialogView.findViewById(R.id.txt_assignToMe);
        TextView technical = dialogView.findViewById(R.id.txt_technical);
        TextView activation = dialogView.findViewById(R.id.txt_activation);
        TextView online = dialogView.findViewById(R.id.txt_online);
        TextView dealer = dialogView.findViewById(R.id.txt_dealer);
        TextView corporate = dialogView.findViewById(R.id.txt_corporate);
        TextView sales = dialogView.findViewById(R.id.txt_sales);
        Button btnCloseAssignDialog = dialogView.findViewById(R.id.btnCloseAssignDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        btnCloseAssignDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        assignToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.KEY_ASSIGN_TO = "Me";
                Assign asssignTicket = new Assign();
                if (agent_id.length() > 0) {
                    asssignTicket.setToAgentID(Integer.parseInt(agent_id));
                    if (current_agent_id > 0) {
                        asssignTicket.setFromAgentID(current_agent_id);
                        alertDialog.dismiss();
                        callAPIAssignTicket(asssignTicket);
                    } else {
                        Toast.makeText(mContext, "Ticket current agent id not found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Logged In agent id not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setDepartment("sales");
            }
        });

        corporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setDepartment("corporate");
            }
        });

        dealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setDepartment("dealer");
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setDepartment("online");
            }
        });

        activation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setDepartment("activation");
            }
        });

        technical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setDepartment("technical");
            }
        });
    }

    private void setDepartment(String dept) {
        Constants.KEY_ASSIGN_TO = "Department";
        Assign asssignTicket = new Assign();
        asssignTicket.setToDepartment(dept);
        callAPIAssignTicket(asssignTicket);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                layout_add_note.setVisibility(View.GONE);
                layout_write_message.setVisibility(View.VISIBLE);
                return true;
            case 2:
                layout_add_note.setVisibility(View.VISIBLE);
                layout_write_message.setVisibility(View.GONE);
                return true;
            case 3:
                callAPIChangeStatus("closed");
                return true;
            case 4:
                callAPIChangeStatus("deleted");
                return true;
            case 5:
                showAddTagDialog();
                return true;
            case 6:
                showAssignTicketDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callAPIAssignTicket(Assign assignTicket) {
        showProgressDialog(TicketChatActivity.this, "");
        sharedpreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        String url = null;
        if (Constants.KEY_ASSIGN_TO.equalsIgnoreCase("Me")) {
            url = APIInterface.BASE_URL + "api/ticket/web/assignment/agent/" + ticket_id;
        } else if (Constants.KEY_ASSIGN_TO.equalsIgnoreCase("Department")) {
            url = APIInterface.BASE_URL + "api/ticket/web/assignment/department/" + ticket_id;
        }
        Call<Assign> call = RetrofitClient.getInstance().getMyApi().assign_ticket("bearer " + token, assignTicket, url);
        call.enqueue(new Callback<Assign>() {
            @Override
            public void onResponse(Call<Assign> call, Response<Assign> response) {
                Assign assigndata = response.body();
                String status = null;
                String info = null;
                if (assigndata != null) {
                    status = assigndata.getStatus();
                    info = assigndata.getInfo();
                }
                if (status != null) {
                    if (status.equals("true")) {
                        hideProgressDialog();
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("false")) {
                        hideProgressDialog();
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TicketChatActivity.this, jObjError.getString("info"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                        Toast.makeText(TicketChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<Assign> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(TicketChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callAPIAddTags(Tag tags) {
        showProgressDialog(TicketChatActivity.this, "");
        sharedpreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        Call<Tag> call = RetrofitClient.getInstance().getMyApi().add_tags("bearer " + token, tags);
        call.enqueue(new Callback<Tag>() {
            @Override
            public void onResponse(Call<Tag> call, Response<Tag> response) {
                Tag tagdata = response.body();
                String status = null;
                String info = null;
                if (tagdata != null) {
                    status = tagdata.getStatus();
                    info = tagdata.getInfo();
                }
                if (status != null) {
                    if (status.equals("true")) {
                        hideProgressDialog();
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("false")) {
                        hideProgressDialog();
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TicketChatActivity.this, jObjError.getString("info"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                        Toast.makeText(TicketChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<Tag> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(TicketChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callAPIChangeStatus(String status) {
        showProgressDialog(TicketChatActivity.this, "");
        sharedpreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        Tickets tickets = new Tickets();
        tickets.setTicketStatus(status);
        String token = sharedpreferences.getString("token", "");
        String url = APIInterface.BASE_URL + "api/ticket/web/status/" + ticket_id;
        Call<Tickets> call = RetrofitClient.getInstance().getMyApi().updateTicketStatus("bearer " + token, tickets, url);
        call.enqueue(new Callback<Tickets>() {
            @Override
            public void onResponse(Call<Tickets> call, Response<Tickets> response) {
                Tickets ticketdata = response.body();
                String status = null;
                String info = null;

                if (ticketdata != null) {
                    status = ticketdata.getStatus();
                    info = ticketdata.getInfo();
                }
                if (status != null) {
                    if (status.equals("true")) {
                        hideProgressDialog();
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("false")) {
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TicketChatActivity.this, jObjError.getString("info"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                        Toast.makeText(TicketChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<Tickets> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(TicketChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callAPIPostMessage(String msg) {
//        showProgressDialog(TicketChatActivity.this, "Sending");
        RequestBody ticketID = null;
        RequestBody agentID = null;
        RequestBody ticketType = null;
        RequestBody messageText = null;
        MultipartBody.Part requestImage = null;
        if (ticket_id != null) {
            ticketID = RequestBody.create(MediaType.parse("multipart/form-data"), ticket_id);
            agentID = RequestBody.create(MediaType.parse("multipart/form-data"), agent_id);
            ticketType = RequestBody.create(MediaType.parse("multipart/form-data"), ticket_Type);
            messageText = RequestBody.create(MediaType.parse("multipart/form-data"), msg);

            if (sourceFile != null) {
                String mimetype = null;
                String extension = MimeTypeMap.getFileExtensionFromUrl(imagepath);
                if (extension != null) {
                    mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimetype), sourceFile);
                requestImage = MultipartBody.Part.createFormData("attachment", sourceFile.getName(), requestFile);
                sourceFile = null;
            }
        }

        Call<PostMessage> call = RetrofitClient.getInstance().getMyApi().post_message("bearer " + token, requestImage, ticketID, agentID, ticketType, messageText);
        call.enqueue(new Callback<PostMessage>() {
            @Override
            public void onResponse(Call<PostMessage> call, Response<PostMessage> response) {
                PostMessage messagedata = response.body();
                String status = null;
                String info = null;

                if (messagedata != null) {
                    info = messagedata.getInfo();
                    status = messagedata.getStatus();
                }
                if (status != null) {
                    if (status.equals("true")) {
                        if (info.equalsIgnoreCase("Message Sent!")) {
//                            hideProgressDialog();
                            editTextPostMessage.getText().clear();
                            CommonMethods.hideKeyboard(TicketChatActivity.this);
//                            callGetTicketDetailsAPI(ticket_id);
                            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            String name_first = sharedpreferences.getString("name_first", "");
                            String name_last = sharedpreferences.getString("name_last", "");
                            String name_sender = name_first + " " + name_last;
                            Ticket.Data.TicketDetails obj = new Ticket.Data.TicketDetails();
                            Date curr_time_stamp = new Date();
                            String ts = outputFormat.format(curr_time_stamp);
                            obj.setMessage_text(msg);
                            obj.setMessage_type("outgoing");
                            obj.setTime_stamp(ts);
                            obj.setName_message_sender(name_sender);
                            obj.setUrl_download("");
                            List<Ticket.Data.TicketDetails> mTicketChatLastOutgoingMsg = new ArrayList<>();
                            mTicketChatLastOutgoingMsg.add(obj);
                            ticketChatAdapter.addAll(mTicketChatLastOutgoingMsg);
                            recyclerView.smoothScrollToPosition(mTicketChatList.size() - 1);
                        } else {
                            Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("false")) {
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TicketChatActivity.this, jObjError.getString("info"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TicketChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
//                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<PostMessage> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(TicketChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callAPIAddNote(String note) {
        showProgressDialog(TicketChatActivity.this, "Adding");
        RequestBody ticketID = null;
        RequestBody agentID = null;
        RequestBody internalNote = null;
        MultipartBody.Part requestImage = null;
        if (ticket_id != null) {
            ticketID = RequestBody.create(MediaType.parse("multipart/form-data"), ticket_id);
            agentID = RequestBody.create(MediaType.parse("multipart/form-data"), agent_id);
            internalNote = RequestBody.create(MediaType.parse("multipart/form-data"), note);

            if (sourceFile != null) {
                String mimetype = null;
                String extension = MimeTypeMap.getFileExtensionFromUrl(imagepath);
                if (extension != null) {
                    mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimetype), sourceFile);
                requestImage = MultipartBody.Part.createFormData("attachment", sourceFile.getName(), requestFile);
                sourceFile = null;
            }
        }

        Call<PostNote> call = RetrofitClient.getInstance().getMyApi().post_note("bearer " + token, requestImage, ticketID, agentID, internalNote);
        call.enqueue(new Callback<PostNote>() {
            @Override
            public void onResponse(Call<PostNote> call, Response<PostNote> response) {
                PostNote messagedata = response.body();
                String status = null;
                String info = null;

                if (messagedata != null) {
                    info = messagedata.getInfo();
                    status = messagedata.getStatus();
                }
                if (status != null) {
                    if (status.equals("true")) {
                        if (info.equalsIgnoreCase("Added new internal note to the ticket!")) {
                            hideProgressDialog();
                            editTextAddNote.getText().clear();
                            CommonMethods.hideKeyboard(TicketChatActivity.this);
                            callGetTicketDetailsAPI(ticket_id);
                        } else {
                            Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_SHORT).show();
                        }

                    } else if (status.equals("false")) {
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TicketChatActivity.this, jObjError.getString("info"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TicketChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<PostNote> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(TicketChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callGetTicketDetailsAPI(String ticket_id) {
        showProgressDialog(TicketChatActivity.this, "");
        sharedpreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        String url = APIInterface.BASE_URL + "api/ticket/web/single/" + ticket_id;
        Call<Ticket> call = RetrofitClient.getInstance().getMyApi().getticketdetails("bearer " + token, url);
        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                Ticket ticketdata = response.body();
                String status = null;
                String info = null;

                if (ticketdata != null) {
                    info = ticketdata.getInfo();
                    status = ticketdata.getStatus();
                    Ticket.Data.TicketDetails[] arr_data = ticketdata.getData().getTicketDetails();
//                    mTicketChatList = Arrays.asList(arr_data);
                    mTicketChatList = new ArrayList<>(Arrays.asList(arr_data));
                }
                if (status != null) {
                    if (status.equals("true")) {
                        if (mTicketChatList != null) {
                            if (mTicketChatList.size() > 0) {
                                current_agent_id = mTicketChatList.get(0).agent_id;
                                Collections.reverse(mTicketChatList);
                                ticketChatAdapter = new TicketChatAdapter(mContext, mTicketChatList, onclickInterface);
                                recyclerView.setAdapter(ticketChatAdapter);
                                recyclerView.smoothScrollToPosition(mTicketChatList.size() - 1);
                            }
                        }
                    } else if (status.equals("false")) {
                        Toast.makeText(TicketChatActivity.this, info, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TicketChatActivity.this, jObjError.getString("info"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(TicketChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(TicketChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TicketChatActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    curr_time = System.currentTimeMillis() + ".jpg";
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), curr_time);
                    Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", f);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Choose from Gallery")) {
                    /*Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (mWebSocketClient.isOpen()) {
            mWebSocketClient.close(1000, "CT WS"); // CT WS -> Chat Web socket
            Log.e("CT TicketChat Websocket", "Closed at " + System.currentTimeMillis());
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
