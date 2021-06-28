package com.npav.npavhelpdeskticket.adapter;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.util.CommonMethods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TicketChatAdapter extends RecyclerView.Adapter<TicketChatAdapter.MyViewHolder> {
    private List<Ticket.Data.TicketDetails> mTicketChatList;

    public TicketChatAdapter(List<Ticket.Data.TicketDetails> pcList) {
        mTicketChatList = pcList;
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
            String name_sender = mTicketChatList.get(position).getName_message_sender();
            String msg_time = mTicketChatList.get(position).getTime_stamp();
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            DateFormat outputFormat = new SimpleDateFormat("'Date : 'dd-MM-yyyy\n'Time : 'KK:mm a");
            DateFormat outputFormat = new SimpleDateFormat("'Date : 'd MMM\n'Time : 'KK:mm a");
            System.out.println(outputFormat.format(inputFormat.parse(msg_time)));
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
                System.out.println(time_am_pm);
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
//                holder.tv_their_message.setText(msg_text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Spanned st = Html.fromHtml(msg_text, Html.FROM_HTML_MODE_COMPACT);
                    CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                    holder.tv_their_message.setText(str);
                } else {
                    Spanned st1 = Html.fromHtml(msg_text);
                    CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                    holder.tv_their_message.setText(str1);
                }
//                holder.tv_cust_name.setText(name_sender);
                holder.tv_incoming_msg_time.setText(strMsgDetails);
//                holder.tv_incoming_msg_time_ago.setText(time_ago);
            } else if (msg_type.equalsIgnoreCase("outgoing")) {
                holder.relative_layout_my_message.setVisibility(View.VISIBLE);
                holder.relative_layout_their_message.setVisibility(View.GONE);
                holder.rl_note.setVisibility(View.GONE);
//                holder.tv_my_message.setText(msg_text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Spanned st = Html.fromHtml(msg_text, Html.FROM_HTML_MODE_COMPACT);
                    CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                    holder.tv_my_message.setText(str);
                } else {
                    Spanned st1 = Html.fromHtml(msg_text);
                    CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                    holder.tv_my_message.setText(str1);
                }
                holder.tv_out_msg_actual_time.setText(strMsgDetails);
//                holder.tv_agent_msg_ago.setText(time_ago);
            } else if (msg_type.equalsIgnoreCase("note")) {
                holder.relative_layout_my_message.setVisibility(View.GONE);
                holder.relative_layout_their_message.setVisibility(View.GONE);
                holder.rl_note.setVisibility(View.VISIBLE);
//                holder.tv_note.setText(msg_text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Spanned st = Html.fromHtml(msg_text, Html.FROM_HTML_MODE_COMPACT);
                    CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                    holder.tv_note.setText(str);
                } else {
                    Spanned st1 = Html.fromHtml(msg_text);
                    CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                    holder.tv_note.setText(str1);
                }
                holder.tv_agent_name.setText(strMsgDetails);
//                holder.tv_note_ago.setText(time_ago);
            } else {
                holder.relative_layout_my_message.setVisibility(View.GONE);
                holder.relative_layout_their_message.setVisibility(View.GONE);
                holder.rl_note.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mTicketChatList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_my_message, tv_their_message, tv_note, tv_agent_name, tv_note_ago, tv_cust_name, tv_incoming_msg_time,
                tv_incoming_msg_time_ago, tv_agent_sender_name, tv_out_msg_actual_time, tv_agent_msg_ago;
        RelativeLayout relative_layout_my_message, relative_layout_their_message, rl_note;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            relative_layout_my_message = itemView.findViewById(R.id.rl_my_msg);
            relative_layout_their_message = itemView.findViewById(R.id.rl_tm_msg);
            rl_note = itemView.findViewById(R.id.rl_note);
            tv_my_message = itemView.findViewById(R.id.tv_mm_message_body);
            tv_their_message = itemView.findViewById(R.id.tv_tm_message_body);
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_agent_name = itemView.findViewById(R.id.txt_agent_name);
//            tv_note_ago = itemView.findViewById(R.id.txt_note_ago);
//            tv_cust_name = itemView.findViewById(R.id.txt_cust_name);
            tv_incoming_msg_time = itemView.findViewById(R.id.txt_actual_time);
//            tv_incoming_msg_time_ago = itemView.findViewById(R.id.tv_cust_msg_ago);
//            tv_agent_sender_name = itemView.findViewById(R.id.txt_agent_sender_name);
            tv_out_msg_actual_time = itemView.findViewById(R.id.txt_out_msg_actual_time);
//            tv_agent_msg_ago = itemView.findViewById(R.id.tv_agent_msg_ago);
        }
    }
}
