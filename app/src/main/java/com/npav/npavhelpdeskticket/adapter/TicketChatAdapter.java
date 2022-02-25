package com.npav.npavhelpdeskticket.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.util.CommonMethods;
import com.npav.npavhelpdeskticket.util.onClickInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TicketChatAdapter extends RecyclerView.Adapter<TicketChatAdapter.MyViewHolder> {
    private List<Ticket.Data.TicketDetails> mTicketChatList;
    private Context mContext;
    private onClickInterface mOnClickInterface;

    public TicketChatAdapter(Context context, List<Ticket.Data.TicketDetails> pcList, onClickInterface onClickInterface) {
        mContext = context;
        mTicketChatList = pcList;
        mOnClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        return new TicketChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            String msg_type = mTicketChatList.get(position).getMessage_type();
            String msg_text = mTicketChatList.get(position).getMessage_text();
            String url_download = mTicketChatList.get(position).getUrl_download();
            String name_sender = mTicketChatList.get(position).getName_message_sender();
            String msg_time = mTicketChatList.get(position).getTime_stamp();
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            DateFormat outputFormat = new SimpleDateFormat("'Date : 'd MMM\n'Time : 'KK:mm a");
//            System.out.println(outputFormat.format(inputFormat.parse(msg_time)));
            String time = outputFormat.format(inputFormat.parse(msg_time));
            String[] strArr = time.split("\n");
            String time_am_pm = "00:00 am";
            String date_time = "00:00 am";
            if (strArr.length > 0) {
                String date = strArr[0];
                String formatted_date = date.substring(7);
                String actual_time = strArr[1];
                time_am_pm = actual_time.substring(7);
                date_time = formatted_date + ", " + time_am_pm;
//                System.out.println(time_am_pm);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setLenient(false);
            Date oldDate = formatter.parse(msg_time);
            long oldMillis = 0;
            if (oldDate != null)
                oldMillis = oldDate.getTime();
            Date curDate = new Date();
            long curMillis = curDate.getTime();
            long diff = curMillis - oldMillis;
            String time_ago = CommonMethods.friendlyTimeDiff(diff);

            String strMsgDetails = name_sender + ", " + date_time;

            if (msg_type.equalsIgnoreCase("incoming")) {
                holder.relative_layout_my_message.setVisibility(View.GONE);
                holder.relative_layout_their_message.setVisibility(View.VISIBLE);
                holder.rl_note.setVisibility(View.GONE);
                holder.rl_assign.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (url_download != null) {
                        holder.iv_tm_msg_img.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(url_download)
                                .into(holder.iv_tm_msg_img);
                    } else {
                        holder.iv_tm_msg_img.setVisibility(View.GONE);
                    }
                    Spanned st = Html.fromHtml(msg_text, Html.FROM_HTML_MODE_COMPACT);
                    CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                    if (str.length() > 0) {
                        holder.tv_their_message.setText(str);
                    } else {
                        holder.tv_their_message.setVisibility(View.GONE);
                    }
                } else {
                    if (url_download != null) {
                        holder.iv_tm_msg_img.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(url_download)
                                .into(holder.iv_tm_msg_img);
                    } else {
                        holder.iv_tm_msg_img.setVisibility(View.GONE);
                    }
                    Spanned st1 = Html.fromHtml(msg_text);
                    CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                    if (str1.length() > 0) {
                        holder.tv_their_message.setText(str1);
                    } else {
                        holder.tv_their_message.setVisibility(View.GONE);
                    }
                }
                holder.tv_incoming_msg_time.setText(strMsgDetails);
            } else if (msg_type.equalsIgnoreCase("outgoing")) {
                holder.relative_layout_my_message.setVisibility(View.VISIBLE);
                holder.relative_layout_their_message.setVisibility(View.GONE);
                holder.rl_note.setVisibility(View.GONE);
                holder.rl_assign.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (url_download != null) {
                        holder.iv_my_msg_img.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(url_download)
                                .into(holder.iv_my_msg_img);
                    } else {
                        holder.iv_my_msg_img.setVisibility(View.GONE);
                    }
                    Spanned st = Html.fromHtml(msg_text, Html.FROM_HTML_MODE_COMPACT);
                    CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                    holder.tv_my_message.setText(str);
                } else {
                    if (url_download != null) {
                        holder.iv_my_msg_img.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(url_download)
                                .into(holder.iv_my_msg_img);
                    } else {
                        holder.iv_my_msg_img.setVisibility(View.GONE);
                    }
                    Spanned st1 = Html.fromHtml(msg_text);
                    CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                    holder.tv_my_message.setText(str1);
                }
                holder.tv_out_msg_actual_time.setText(strMsgDetails);
            } else if (msg_type.equalsIgnoreCase("note")) {
                holder.relative_layout_my_message.setVisibility(View.GONE);
                holder.relative_layout_their_message.setVisibility(View.GONE);
                holder.rl_assign.setVisibility(View.GONE);
                holder.rl_note.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (url_download != null) {
                        holder.iv_my_note_img.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(url_download)
                                .into(holder.iv_my_note_img);
                    } else {
                        holder.iv_my_note_img.setVisibility(View.GONE);
                    }
                    Spanned st = Html.fromHtml(msg_text, Html.FROM_HTML_MODE_COMPACT);
                    CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                    holder.tv_note.setText(str);
                } else {
                    if (url_download != null) {
                        holder.iv_my_note_img.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(url_download)
                                .into(holder.iv_my_note_img);
                    } else {
                        holder.iv_my_note_img.setVisibility(View.GONE);
                    }
                    Spanned st1 = Html.fromHtml(msg_text);
                    CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                    holder.tv_note.setText(str1);
                }
                holder.tv_agent_name.setText(strMsgDetails);
            } else if (msg_type.equalsIgnoreCase("assigning")) {
                holder.relative_layout_my_message.setVisibility(View.GONE);
                holder.relative_layout_their_message.setVisibility(View.GONE);
                holder.rl_assign.setVisibility(View.VISIBLE);
                holder.rl_note.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Spanned st = Html.fromHtml(msg_text, Html.FROM_HTML_MODE_COMPACT);
                    CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                    holder.tv_assign_msg.setText(str);
                } else {
                    Spanned st1 = Html.fromHtml(msg_text);
                    CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                    holder.tv_assign_msg.setText(str1);
                }
                holder.txt_assign_agent_name.setText(strMsgDetails);
            } else {
                holder.relative_layout_my_message.setVisibility(View.GONE);
                holder.relative_layout_their_message.setVisibility(View.GONE);
                holder.rl_note.setVisibility(View.GONE);
                holder.rl_assign.setVisibility(View.GONE);
            }
            holder.iv_my_msg_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImageInBrowser(position);
                }
            });
            holder.iv_tm_msg_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImageInBrowser(position);
                }
            });
            holder.iv_my_note_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImageInBrowser(position);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void openImageInBrowser(int position) {
        mOnClickInterface.setClick(position);
        String url_download = mTicketChatList.get(position).getUrl_download();
        if (url_download != null) {
            Uri uri = Uri.parse(url_download);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            }
        }
    }

    public void add(Ticket.Data.TicketDetails lastMsg) {
        mTicketChatList.add(lastMsg);
        notifyItemInserted(mTicketChatList.size() - 1);
    }

    public void addAll(List<Ticket.Data.TicketDetails> moveResults) {
        for (Ticket.Data.TicketDetails result : moveResults) {
            add(result);
        }
    }

    @Override
    public int getItemCount() {
        return mTicketChatList == null ? 0 : mTicketChatList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_my_message, tv_their_message, tv_note, tv_agent_name, tv_assign_msg, txt_assign_agent_name, tv_incoming_msg_time,
                tv_out_msg_actual_time;
        RelativeLayout relative_layout_my_message, relative_layout_their_message, rl_note, rl_assign;
        ImageView iv_my_msg_img, iv_my_note_img, iv_tm_msg_img;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            relative_layout_my_message = itemView.findViewById(R.id.rl_my_msg);
            tv_my_message = itemView.findViewById(R.id.tv_mm_message_body);
            tv_out_msg_actual_time = itemView.findViewById(R.id.txt_out_msg_actual_time);
            iv_my_msg_img = itemView.findViewById(R.id.iv_my_msg_img);

            relative_layout_their_message = itemView.findViewById(R.id.rl_tm_msg);
            tv_their_message = itemView.findViewById(R.id.tv_tm_message_body);
            tv_incoming_msg_time = itemView.findViewById(R.id.txt_actual_time);
            iv_tm_msg_img = itemView.findViewById(R.id.iv_tm_msg_img);

            rl_note = itemView.findViewById(R.id.rl_note);
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_agent_name = itemView.findViewById(R.id.txt_agent_name);
            iv_my_note_img = itemView.findViewById(R.id.iv_my_note_img);

            rl_assign = itemView.findViewById(R.id.rl_assign);
            tv_assign_msg = itemView.findViewById(R.id.tv_assign_msg);
            txt_assign_agent_name = itemView.findViewById(R.id.txt_assign_agent_name);

        }
    }
}
