package com.npav.npavhelpdeskticket.pojo;

import java.util.Arrays;

public class Tickets {
    public String status;
    public String info;
    public String ticketStatus;
    public Tickets.Data[] data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public static class Data implements Comparable, Cloneable {
        public String count;
        public String latest_message_time_stamp;
        public String time_stamp_latest_message;
        public String incoming_message_count;
        public String outgoing_message_count;
        public int message_id;
        public String message_type;
        public String message_text;
        public String time_stamp;
        public int ticket_id;
        public int agent_id;
        public String ticket_type;
        public String department;
        public String ticket_status;
        public String time_stamp_ticket_generated;
        public String time_stamp_ticket_resolved;
        public String[] tags;
        public int customer_id;
        public String name_first;
        public String name_last;
        public String city;
        public String phone;
        public String email;
        public String comment;
        public String tag;
        public String telegram_username;
        public String time_stamp_customer_created;
        public String attachment_id;
        public String url_download;
        public String mime_type;
        public String name_message_sender;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLatest_message_time_stamp() {
            return latest_message_time_stamp;
        }

        public void setLatest_message_time_stamp(String latest_message_time_stamp) {
            this.latest_message_time_stamp = latest_message_time_stamp;
        }

        public String getTime_stamp_latest_message() {
            return time_stamp_latest_message;
        }

        public void setTime_stamp_latest_message(String time_stamp_latest_message) {
            this.time_stamp_latest_message = time_stamp_latest_message;
        }

        public String getIncoming_message_count() {
            return incoming_message_count;
        }

        public void setIncoming_message_count(String incoming_message_count) {
            this.incoming_message_count = incoming_message_count;
        }

        public String getOutgoing_message_count() {
            return outgoing_message_count;
        }

        public void setOutgoing_message_count(String outgoing_message_count) {
            this.outgoing_message_count = outgoing_message_count;
        }

        public int getMessage_id() {
            return message_id;
        }

        public void setMessage_id(int message_id) {
            this.message_id = message_id;
        }

        public String getMessage_type() {
            return message_type;
        }

        public void setMessage_type(String message_type) {
            this.message_type = message_type;
        }

        public String getMessage_text() {
            return message_text;
        }

        public void setMessage_text(String message_text) {
            this.message_text = message_text;
        }

        public String getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(String time_stamp) {
            this.time_stamp = time_stamp;
        }

        public int getTicket_id() {
            return ticket_id;
        }

        public void setTicket_id(int ticket_id) {
            this.ticket_id = ticket_id;
        }

        public int getAgent_id() {
            return agent_id;
        }

        public void setAgent_id(int agent_id) {
            this.agent_id = agent_id;
        }

        public String getTicket_type() {
            return ticket_type;
        }

        public void setTicket_type(String ticket_type) {
            this.ticket_type = ticket_type;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getTicket_status() {
            return ticket_status;
        }

        public void setTicket_status(String ticket_status) {
            this.ticket_status = ticket_status;
        }

        public String getTime_stamp_ticket_generated() {
            return time_stamp_ticket_generated;
        }

        public void setTime_stamp_ticket_generated(String time_stamp_ticket_generated) {
            this.time_stamp_ticket_generated = time_stamp_ticket_generated;
        }

        public String getTime_stamp_ticket_resolved() {
            return time_stamp_ticket_resolved;
        }

        public void setTime_stamp_ticket_resolved(String time_stamp_ticket_resolved) {
            this.time_stamp_ticket_resolved = time_stamp_ticket_resolved;
        }

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public String getName_first() {
            return name_first;
        }

        public void setName_first(String name_first) {
            this.name_first = name_first;
        }

        public String getName_last() {
            return name_last;
        }

        public void setName_last(String name_last) {
            this.name_last = name_last;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTelegram_username() {
            return telegram_username;
        }

        public void setTelegram_username(String telegram_username) {
            this.telegram_username = telegram_username;
        }

        public String getTime_stamp_customer_created() {
            return time_stamp_customer_created;
        }

        public void setTime_stamp_customer_created(String time_stamp_customer_created) {
            this.time_stamp_customer_created = time_stamp_customer_created;
        }

        public String getAttachment_id() {
            return attachment_id;
        }

        public void setAttachment_id(String attachment_id) {
            this.attachment_id = attachment_id;
        }

        public String getUrl_download() {
            return url_download;
        }

        public void setUrl_download(String url_download) {
            this.url_download = url_download;
        }

        public String getMime_type() {
            return mime_type;
        }

        public void setMime_type(String mime_type) {
            this.mime_type = mime_type;
        }

        public String getName_message_sender() {
            return name_message_sender;
        }

        public void setName_message_sender(String name_message_sender) {
            this.name_message_sender = name_message_sender;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "count='" + count + '\'' +
                    ", latest_message_time_stamp='" + latest_message_time_stamp + '\'' +
                    ", incoming_message_count='" + incoming_message_count + '\'' +
                    ", outgoing_message_count='" + outgoing_message_count + '\'' +
                    ", message_id=" + message_id +
                    ", message_type='" + message_type + '\'' +
                    ", message_text='" + message_text + '\'' +
                    ", time_stamp='" + time_stamp + '\'' +
                    ", ticket_id=" + ticket_id +
                    ", agent_id=" + agent_id +
                    ", ticket_type='" + ticket_type + '\'' +
                    ", department='" + department + '\'' +
                    ", ticket_status='" + ticket_status + '\'' +
                    ", time_stamp_ticket_generated='" + time_stamp_ticket_generated + '\'' +
                    ", time_stamp_ticket_resolved='" + time_stamp_ticket_resolved + '\'' +
                    ", tags=" + Arrays.toString(tags) +
                    ", customer_id=" + customer_id +
                    ", name_first='" + name_first + '\'' +
                    ", name_last='" + name_last + '\'' +
                    ", city='" + city + '\'' +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", comment='" + comment + '\'' +
                    ", tag='" + tag + '\'' +
                    ", telegram_username='" + telegram_username + '\'' +
                    ", time_stamp_customer_created='" + time_stamp_customer_created + '\'' +
                    ", attachment_id='" + attachment_id + '\'' +
                    ", url_download='" + url_download + '\'' +
                    ", mime_type='" + mime_type + '\'' +
                    ", name_message_sender='" + name_message_sender + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Object o) {
            Data compare = (Data) o;
            if (compare.ticket_id == this.ticket_id && compare.getMessage_text().equals(this.getMessage_text()) && compare.time_stamp_latest_message.equals(this.time_stamp_latest_message)) {
                return 0;
            }
            return 1;
        }

        @Override
        public Data clone() {
            Data clone;
            try {
                clone = (Data) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e); //should not happen
            }
            return clone;
        }
    }
}
