package com.npav.npavhelpdeskticket.pojo;

public class Ticket {
    public int ticket_id;
    public String cust_mobile;
    public String cust_name;
    public String cust_city;
    public String ticket_created_date_time;
    public String ticket_modified_date;
    public int agent_reply_count;
    public int cust_reply_count;
    public String last_reply_by_whom;
    public String ticket_assignee;
    public String last_reply_date_time;
    public String status;
    public String info;

    public Data data;
    public Data[] arr_data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data[] getArr_data() {
        return arr_data;
    }

    public void setArr_data(Data[] arr_data) {
        this.arr_data = arr_data;
    }

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

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getCust_mobile() {
        return cust_mobile;
    }

    public void setCust_mobile(String cust_mobile) {
        this.cust_mobile = cust_mobile;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_city() {
        return cust_city;
    }

    public void setCust_city(String cust_city) {
        this.cust_city = cust_city;
    }

    public String getTicket_created_date_time() {
        return ticket_created_date_time;
    }

    public void setTicket_created_date_time(String ticket_created_date_time) {
        this.ticket_created_date_time = ticket_created_date_time;
    }

    public String getTicket_modified_date() {
        return ticket_modified_date;
    }

    public void setTicket_modified_date(String ticket_modified_date) {
        this.ticket_modified_date = ticket_modified_date;
    }

    public int getAgent_reply_count() {
        return agent_reply_count;
    }

    public void setAgent_reply_count(int agent_reply_count) {
        this.agent_reply_count = agent_reply_count;
    }

    public int getCust_reply_count() {
        return cust_reply_count;
    }

    public void setCust_reply_count(int cust_reply_count) {
        this.cust_reply_count = cust_reply_count;
    }

    public String getLast_reply_by_whom() {
        return last_reply_by_whom;
    }

    public void setLast_reply_by_whom(String last_reply_by_whom) {
        this.last_reply_by_whom = last_reply_by_whom;
    }

    public String getTicket_assignee() {
        return ticket_assignee;
    }

    public void setTicket_assignee(String ticket_assignee) {
        this.ticket_assignee = ticket_assignee;
    }

    public String getLast_reply_date_time() {
        return last_reply_date_time;
    }

    public void setLast_reply_date_time(String last_reply_date_time) {
        this.last_reply_date_time = last_reply_date_time;
    }

    public static class Data {
        public String totalRecords;
        public String totalPages;
        public List[] list;
        public TicketDetails[] ticketDetails;
        public TicketAssignmentHistory[] ticketAssignmentHistory;
        public PreviousAndNextIDs previousAndNextIDs;
        public String count;
        public String latest_message_time_stamp;
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

        public String getName_message_sender() {
            return name_message_sender;
        }

        public void setName_message_sender(String name_message_sender) {
            this.name_message_sender = name_message_sender;
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

        public String getLatest_message_time_stamp() {
            return latest_message_time_stamp;
        }

        public void setLatest_message_time_stamp(String latest_message_time_stamp) {
            this.latest_message_time_stamp = latest_message_time_stamp;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
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

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public static class Tags {

        }

        public TicketDetails[] getTicketDetails() {
            return ticketDetails;
        }

        public void setTicketDetails(TicketDetails[] ticketDetails) {
            this.ticketDetails = ticketDetails;
        }

        public TicketAssignmentHistory[] getTicketAssignmentHistory() {
            return ticketAssignmentHistory;
        }

        public void setTicketAssignmentHistory(TicketAssignmentHistory[] ticketAssignmentHistory) {
            this.ticketAssignmentHistory = ticketAssignmentHistory;
        }

        public PreviousAndNextIDs getPreviousAndNextIDs() {
            return previousAndNextIDs;
        }

        public void setPreviousAndNextIDs(PreviousAndNextIDs previousAndNextIDs) {
            this.previousAndNextIDs = previousAndNextIDs;
        }

        public String getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(String totalRecords) {
            this.totalRecords = totalRecords;
        }

        public String getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(String totalPages) {
            this.totalPages = totalPages;
        }

        public List[] getList() {
            return list;
        }

        public void setList(List[] list) {
            this.list = list;
        }

        public static class List {

            public String count;
            public String latest_message_time_stamp;
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

            public List() {

            }

            public String getName_message_sender() {
                return name_message_sender;
            }

            public void setName_message_sender(String name_message_sender) {
                this.name_message_sender = name_message_sender;
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

            public String getLatest_message_time_stamp() {
                return latest_message_time_stamp;
            }

            public void setLatest_message_time_stamp(String latest_message_time_stamp) {
                this.latest_message_time_stamp = latest_message_time_stamp;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
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

            public String[] getTags() {
                return tags;
            }

            public void setTags(String[] tags) {
                this.tags = tags;
            }

            public static class Tags {

            }
        }

        public static class TicketDetails {
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
            public int message_id;
            public String message_type;
            public String message_text;
            public String time_stamp;
            public String name_message_sender;
            public String attachment_id;
            public String url_download;
            public String mime_type;

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

            public String getName_message_sender() {
                return name_message_sender;
            }

            public void setName_message_sender(String name_message_sender) {
                this.name_message_sender = name_message_sender;
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
        }

        public static class TicketAssignmentHistory {

        }

        public static class PreviousAndNextIDs {
            public int next_ticket_id;
            public int previous_ticket_id;

            public int getNext_ticket_id() {
                return next_ticket_id;
            }

            public void setNext_ticket_id(int next_ticket_id) {
                this.next_ticket_id = next_ticket_id;
            }

            public int getPrevious_ticket_id() {
                return previous_ticket_id;
            }

            public void setPrevious_ticket_id(int previous_ticket_id) {
                this.previous_ticket_id = previous_ticket_id;
            }
        }
    }
}
