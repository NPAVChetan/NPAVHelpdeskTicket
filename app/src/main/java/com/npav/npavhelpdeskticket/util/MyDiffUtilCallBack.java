package com.npav.npavhelpdeskticket.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.npav.npavhelpdeskticket.pojo.Tickets;

import java.util.List;

public class MyDiffUtilCallBack extends DiffUtil.Callback {
    private List<Tickets.Data> newTicketList;
    private List<Tickets.Data> oldTicketList;

    public MyDiffUtilCallBack(List<Tickets.Data> newTicketList, List<Tickets.Data> oldTicketList) {
        this.newTicketList = newTicketList;
        this.oldTicketList = oldTicketList;
    }

    @Override
    public int getOldListSize() {
        return oldTicketList != null ? oldTicketList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newTicketList != null ? newTicketList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newTicketList.get(newItemPosition).ticket_id == oldTicketList.get(oldItemPosition).ticket_id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newTicketList.get(newItemPosition).compareTo(oldTicketList.get(oldItemPosition));
        return result == 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Tickets.Data newListData = newTicketList.get(newItemPosition);
        Tickets.Data oldListData = oldTicketList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (!newListData.getMessage_text().equals(oldListData.getMessage_text())) {
            diff.putString("message_text", newListData.getMessage_text());
        }
        if (!newListData.getTime_stamp_latest_message().equals(oldListData.getTime_stamp_latest_message())) {
            diff.putString("time_stamp_latest_message", newListData.getTime_stamp_latest_message());
        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;
        //return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
