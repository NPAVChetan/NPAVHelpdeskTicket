package com.npav.npavhelpdeskticket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.npav.npavhelpdeskticket.R;
import com.npav.npavhelpdeskticket.activity.TicketChatActivity;
import com.npav.npavhelpdeskticket.pojo.Tickets;
import com.npav.npavhelpdeskticket.util.CommonMethods;
import com.npav.npavhelpdeskticket.util.onClickInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Tickets.Data> mTicketList;
    private onClickInterface mOnClickInterface;

    public TicketListAdapter(Context context, List<Tickets.Data> pcList, onClickInterface onClickInterface) {
        mContext = context;
        mTicketList = pcList;
        mOnClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list, parent, false);
        return new TicketListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            String ticketId = String.valueOf(mTicketList.get(position).getTicket_id());
            holder.ticket_id.setText(String.format("#%s", ticketId));
            String ticket_type = mTicketList.get(position).getTicket_type();
            if (ticket_type.equalsIgnoreCase("whatsapp")) {
                holder.ticket_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_whatsapp));
            } else if (ticket_type.equalsIgnoreCase("email")) {
                holder.ticket_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.gmail));
            } else if (ticket_type.equalsIgnoreCase("telegram")) {
                holder.ticket_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.telegram));
            }
            String first_name = mTicketList.get(position).getName_first();
            String last_name = mTicketList.get(position).getName_last();
            String full_name = first_name + " " + last_name;
            holder.cust_name.setText(full_name);
            holder.cust_mobile.setText(mTicketList.get(position).getPhone());
            holder.cust_city.setText(mTicketList.get(position).getCity());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Spanned st = Html.fromHtml(mTicketList.get(position).getMessage_text(), Html.FROM_HTML_MODE_COMPACT);
                CharSequence str = CommonMethods.trimTrailingWhitespace(st);
                holder.message_text.setText(str);
            } else {
                Spanned st1 = Html.fromHtml(mTicketList.get(position).getMessage_text());
                CharSequence str1 = CommonMethods.trimTrailingWhitespace(st1);
                holder.message_text.setText(str1);
            }

//            holder.message_text.setText(mTicketList.get(position).getMessage_text());
            try {
                String[] strArr = mTicketList.get(position).getTags();
                String tags = "";
                if (strArr != null) {
                    if (strArr.length > 0) {
                        for (String str : strArr) {
                            tags = String.format("%s %s", tags, str);
                        }
                        holder.txt_tag.setText(tags);
                    }
                } else {
                    holder.txt_tag.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String last_msg_time = mTicketList.get(position).time_stamp_latest_message;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setLenient(false);
            Date oldDate = formatter.parse(last_msg_time);
            long oldMillis = 0;
            if (oldDate != null)
                oldMillis = oldDate.getTime();
            Date curDate = new Date();
            long curMillis = curDate.getTime();
            long diff = curMillis - oldMillis;
            String time_ago = friendlyTimeDiff(diff);

            holder.time_ago.setText(time_ago);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.parent_layout.setOnClickListener(v -> {
            mOnClickInterface.setClick(position);
            Intent intent = new Intent(mContext, TicketChatActivity.class);
//            intent.putExtra(Constants.KEY_TICKET_ID, TicketId);
            mContext.startActivity(intent);
//            ((Activity) mContext).finish();
        });
    }

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }

    private String friendlyTimeDiff(long timeDifferenceMilliseconds) {
        long diffSeconds = timeDifferenceMilliseconds / 1000;
        long diffMinutes = timeDifferenceMilliseconds / (60 * 1000);
        long diffHours = timeDifferenceMilliseconds / (60 * 60 * 1000);
        long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);
        long diffWeeks = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 7);
        long diffMonths = (long) (timeDifferenceMilliseconds / (60 * 60 * 1000 * 24 * 30.41666666));
        long diffYears = timeDifferenceMilliseconds / ((long) 60 * 60 * 1000 * 24 * 365);

        if (diffSeconds < 1) {
            return "just now"; // less than a second
        } else if (diffMinutes < 1) {
            return "just now";
//            return diffSeconds + " seconds ago";
        } else if (diffHours < 1) {
            return diffMinutes + " minutes ago";
        } else if (diffDays < 1) {
            return diffHours + " hours ago";
        } else if (diffWeeks < 1) {
            if (diffDays == 1) {
                return diffDays + " day ago";
            }
            return diffDays + " days ago";
        } else if (diffMonths < 1) {
            return diffWeeks + " weeks";
        } else if (diffYears < 1) {
            return diffMonths + " months";
        } else {
            return diffYears + " years";
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cust_name, cust_mobile, cust_city, ticket_id, message_text, time_ago, txt_tag;
        LinearLayout parent_layout;
        ImageView ticket_type;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            cust_name = itemView.findViewById(R.id.cust_name);
            cust_mobile = itemView.findViewById(R.id.cust_mobile);
            cust_city = itemView.findViewById(R.id.cust_city);
            ticket_id = itemView.findViewById(R.id.ticket_id);
            message_text = itemView.findViewById(R.id.message_text);
            time_ago = itemView.findViewById(R.id.time_ago);
            txt_tag = itemView.findViewById(R.id.txt_tag);
            ticket_type = itemView.findViewById(R.id.ticket_type);
        }
    }
}

