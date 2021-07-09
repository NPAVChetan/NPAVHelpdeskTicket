package com.npav.npavhelpdeskticket.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.adapter.TicketChatAdapter;
import com.npav.npavhelpdeskticket.api.APIInterface;
import com.npav.npavhelpdeskticket.api.RetrofitClient;
import com.npav.npavhelpdeskticket.pojo.PostMessage;
import com.npav.npavhelpdeskticket.pojo.PostNote;
import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.pojo.Tickets;
import com.npav.npavhelpdeskticket.util.CommonMethods;
import com.npav.npavhelpdeskticket.util.Constants;
import com.npav.npavhelpdeskticket.util.FileUtils;
import com.npav.npavhelpdeskticket.util.onClickInterface;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.npav.npavhelpdeskticket.util.CommonMethods.hideProgressDialog;
import static com.npav.npavhelpdeskticket.util.CommonMethods.showProgressDialog;

public class TicketChatActivity extends AppCompatActivity {
    TicketChatAdapter ticketChatAdapter;
    List<Ticket.Data.TicketDetails> mTicketChatList = new ArrayList<>();
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    EditText editTextPostMessage, editTextAddNote;
    ImageButton imgBtnSendMsg, imgBtnSendNote, imgBtnMsgAttchment, imgBtnNoteAttchment;
    String ticket_id, ticket_Type;
    LinearLayout layout_write_message, layout_add_note;
    private String imagepath = null;
    Uri selectedImageUri;
    Bitmap bitmap;
    File sourceFile;
    Context mContext;
    private onClickInterface onclickInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_chat);
        mContext = TicketChatActivity.this;
        getSupportActionBar().setTitle("Ticket Details");
        layout_write_message = findViewById(R.id.layout_write_message);
        editTextPostMessage = findViewById(R.id.editTextPostMessage);
        imgBtnSendMsg = findViewById(R.id.imgBtnSendMsg);
        imgBtnMsgAttchment = findViewById(R.id.imgBtnMsgAttchment);
        imgBtnNoteAttchment = findViewById(R.id.imgBtnNoteAttchment);

        layout_add_note = findViewById(R.id.layout_add_note);
        editTextAddNote = findViewById(R.id.editTextAddNote);
        imgBtnSendNote = findViewById(R.id.imgBtnSendNote);

        recyclerView = findViewById(R.id.rv_messages_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TicketChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        if (CommonMethods.CheckInternetConnection(TicketChatActivity.this)) {
            ticket_id = Constants.KEY_TICKET_ID;
            ticket_Type = Constants.KEY_TICKET_TYPE;
            callGetTicketDetailsAPI(ticket_id);
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
        imgBtnMsgAttchment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attchment_type;
//                launchGallery(attachment_type);
                Constants.KEY_ATTACHMENT_TYPE = "Message";
                Toast.makeText(TicketChatActivity.this, "Message Attachment", Toast.LENGTH_SHORT).show();
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
        imgBtnNoteAttchment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.KEY_ATTACHMENT_TYPE = "Note";
                Toast.makeText(TicketChatActivity.this, "Note Attachment", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
            }
        });
    }

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
            return true;
        }
    }

    private CharSequence menuIconWithText(Drawable r, String title) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("   " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                Toast.makeText(TicketChatActivity.this, "Cose Ticket", Toast.LENGTH_SHORT).show();
                callAPIChangeStatus("closed");
                return true;
            case 4:
                Toast.makeText(TicketChatActivity.this, "Delete Ticket", Toast.LENGTH_SHORT).show();
                callAPIChangeStatus("deleted");
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        showProgressDialog(TicketChatActivity.this, "Sending");
        sharedpreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        String agent_id = sharedpreferences.getString("agent_id", "");
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
                            hideProgressDialog();
                            editTextPostMessage.getText().clear();
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
            public void onFailure(Call<PostMessage> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(TicketChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callAPIAddNote(String note) {
        showProgressDialog(TicketChatActivity.this, "Adding");
        sharedpreferences = getSharedPreferences(Constants.LOGIN_FILE_NAME,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        String agent_id = sharedpreferences.getString("agent_id", "");
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

        Call<PostNote> call = RetrofitClient.getInstance().getMyApi().post_note1("bearer " + token, requestImage, ticketID, agentID, internalNote);
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
                    mTicketChatList = Arrays.asList(arr_data);
                }
                if (status != null) {
                    if (status.equals("true")) {
                        if (mTicketChatList != null) {
                            if (mTicketChatList.size() > 0) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
