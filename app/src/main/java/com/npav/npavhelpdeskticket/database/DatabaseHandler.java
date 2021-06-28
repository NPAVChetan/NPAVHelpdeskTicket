package com.npav.npavhelpdeskticket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.npav.npavhelpdeskticket.pojo.Ticket;
import com.npav.npavhelpdeskticket.pojo.Tickets;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "npav_helpdesk";
    private static final String TABLE_TICKET = "ticket";

    // ticket table fields
    private static final String KEY_ID = "id";
    private static final String KEY_COUNT = "count";
    private static final String KEY_LATEST_MESSAGE_TIME_STAMP = "latest_message_time_stamp";
    private static final String KEY_INCOMING_MESSAGE_COUNT = "incoming_message_count";
    private static final String KEY_OUTGOING_MESSAGE_COUNT = "outgoing_message_count";
    private static final String KEY_MESSAGE_ID = "message_id";
    private static final String KEY_MESSAGE_TYPE = "message_type";
    private static final String KEY_MESSAGE_TEXT = "message_text";
    private static final String KEY_TIME_STAMP = "time_stamp";
    private static final String KEY_TICKET_ID = "ticket_id";
    private static final String KEY_AGENT_ID = "agent_id";
    private static final String KEY_TICKET_TYPE = "ticket_type";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_TICKET_STATUS = "ticket_status";
    private static final String KEY_TIME_STAMP_TICKET_GENERATED = "time_stamp_ticket_generated";
    private static final String KEY_TIME_STAMP_TICKET_RESOLVED = "time_stamp_ticket_resolved";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_NAME_FIRST = "name_first";
    private static final String KEY_NAME_LAST = "name_last";
    private static final String KEY_CITY = "city";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TELEGRAM_USERNAME = "telegram_username";
    private static final String KEY_TIME_STAMP_CUSTOMER_CREATED = "time_stamp_customer_created";
    private static final String KEY_ATTACHMENT_ID = "attachment_id";
    private static final String KEY_URL_DOWNLOAD = "url_download";
    private static final String KEY_MIME_TYPE = "mime_type";


    /*private static final String KEY_TICKET_ID = "ticket_id"; // ok
    private static final String KEY_CUST_MOBILE = "cust_mobile"; // ok
    private static final String KEY_CUST_NAME = "cust_name"; // ok
    private static final String KEY_CUST_CITY = "cust_city"; // ok
    private static final String KEY_TICKET_CREATED_DATE_TIME = "ticket_created_date_time"; // ok
    private static final String KEY_TICKET_MODIFIED_DATE = "ticket_modified_date"; //1
    private static final String KEY_AGENT_REPLY_COUNT = "agent_reply_count"; // ok
    private static final String KEY_CUST_REPLY_COUNT = "cust_reply_count"; // ok
    private static final String KEY_LAST_REPLY_BY_WHOM = "last_reply_by_whom"; //2
    private static final String KEY_TICKET_ASSIGNEE = "ticket_assignee";// ok
    private static final String KEY_LAST_REPLY_DATE_TIME = "last_reply_date_time"; //3*/

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TICKET_TABLE = "CREATE TABLE " + TABLE_TICKET + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TICKET_ID + " INTEGER,"
                + KEY_INCOMING_MESSAGE_COUNT + " TEXT,"
                + KEY_OUTGOING_MESSAGE_COUNT + " TEXT,"
                + KEY_MESSAGE_ID + " INTEGER,"
                + KEY_MESSAGE_TYPE + " TEXT,"
                + KEY_MESSAGE_TEXT + " TEXT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_AGENT_ID + " INTEGER,"
                + KEY_TICKET_TYPE + " TEXT,"
                + KEY_DEPARTMENT + " TEXT,"
                + KEY_TICKET_STATUS + " TEXT,"
                + KEY_TIME_STAMP_TICKET_GENERATED + " TEXT,"
                + KEY_TIME_STAMP_TICKET_RESOLVED + " TEXT,"
                + KEY_TAGS + " TEXT,"
                + KEY_CUSTOMER_ID + " INTEGER,"
                + KEY_NAME_FIRST + " TEXT,"
                + KEY_NAME_LAST + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_TELEGRAM_USERNAME + " TEXT,"
                + KEY_TIME_STAMP_CUSTOMER_CREATED + " TEXT,"
                + KEY_ATTACHMENT_ID + " TEXT,"
                + KEY_URL_DOWNLOAD + " TEXT,"
                + KEY_MIME_TYPE + " TEXT,"
                + KEY_LATEST_MESSAGE_TIME_STAMP + " TEXT" + ")";
        db.execSQL(CREATE_TICKET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
        // Create tables again
        onCreate(db);
    }

    // code to add the new Customer
    public void add_or_update_Ticket(List<Ticket.Data> ticketList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < ticketList.size(); i++) {
            Ticket.Data ticket = ticketList.get(i);
            String ticketSingleId = String.valueOf(ticket.getTicket_id());

            boolean status = Check_Is_Ticket_ID_Present_or_Not(TABLE_TICKET, KEY_TICKET_ID, ticketSingleId);

            // if status true -> update and if status false -> add ticket.
            if (status) { // update ticket
                try {
                    SQLiteDatabase update_db = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(KEY_TICKET_ID, ticket.getTicket_id());
                    values.put(KEY_INCOMING_MESSAGE_COUNT, ticket.getIncoming_message_count());
                    values.put(KEY_OUTGOING_MESSAGE_COUNT, ticket.getOutgoing_message_count());
                    values.put(KEY_MESSAGE_ID, ticket.getMessage_id());
                    values.put(KEY_MESSAGE_TYPE, ticket.getMessage_type());
                    values.put(KEY_MESSAGE_TEXT, ticket.getMessage_text());
                    values.put(KEY_TIME_STAMP, ticket.getTime_stamp());
                    values.put(KEY_AGENT_ID, ticket.getAgent_id());
                    values.put(KEY_TICKET_TYPE, ticket.getTicket_type());
                    values.put(KEY_DEPARTMENT, ticket.getDepartment());
                    values.put(KEY_TICKET_STATUS, ticket.getTicket_status());
                    values.put(KEY_TIME_STAMP_TICKET_GENERATED, ticket.getTime_stamp_ticket_generated());
                    values.put(KEY_TIME_STAMP_TICKET_RESOLVED, ticket.getTime_stamp_ticket_resolved());
                    values.put(KEY_TAGS, ticket.getTags().toString());
                    values.put(KEY_CUSTOMER_ID, ticket.getCustomer_id());
                    values.put(KEY_NAME_FIRST, ticket.getName_first());
                    values.put(KEY_NAME_LAST, ticket.getName_last());
                    values.put(KEY_CITY, ticket.getCity());
                    values.put(KEY_PHONE, ticket.getPhone());
                    values.put(KEY_EMAIL, ticket.getEmail());
                    values.put(KEY_TELEGRAM_USERNAME, ticket.getTelegram_username());
                    values.put(KEY_TIME_STAMP_CUSTOMER_CREATED, ticket.getTime_stamp_customer_created());
                    values.put(KEY_ATTACHMENT_ID, ticket.getAttachment_id());
                    values.put(KEY_URL_DOWNLOAD, ticket.getUrl_download());
                    values.put(KEY_MIME_TYPE, ticket.getMime_type());
                    values.put(KEY_LATEST_MESSAGE_TIME_STAMP, ticket.getLatest_message_time_stamp());
                    int update_status = update_db.update(TABLE_TICKET, values, KEY_TICKET_ID + " = ?", new String[]{ticketSingleId});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { // add ticket
                try {
                    ContentValues values = new ContentValues();
                    values.put(KEY_TICKET_ID, ticket.getTicket_id());
                    values.put(KEY_INCOMING_MESSAGE_COUNT, ticket.getIncoming_message_count());
                    values.put(KEY_OUTGOING_MESSAGE_COUNT, ticket.getOutgoing_message_count());
                    values.put(KEY_MESSAGE_ID, ticket.getMessage_id());
                    values.put(KEY_MESSAGE_TYPE, ticket.getMessage_type());
                    values.put(KEY_MESSAGE_TEXT, ticket.getMessage_text());
                    values.put(KEY_TIME_STAMP, ticket.getTime_stamp());
                    values.put(KEY_AGENT_ID, ticket.getAgent_id());
                    values.put(KEY_TICKET_TYPE, ticket.getTicket_type());
                    values.put(KEY_DEPARTMENT, ticket.getDepartment());
                    values.put(KEY_TICKET_STATUS, ticket.getTicket_status());
                    values.put(KEY_TIME_STAMP_TICKET_GENERATED, ticket.getTime_stamp_ticket_generated());
                    values.put(KEY_TIME_STAMP_TICKET_RESOLVED, ticket.getTime_stamp_ticket_resolved());
                    values.put(KEY_TAGS, ticket.getTags().toString());
                    values.put(KEY_CUSTOMER_ID, ticket.getCustomer_id());
                    values.put(KEY_NAME_FIRST, ticket.getName_first());
                    values.put(KEY_NAME_LAST, ticket.getName_last());
                    values.put(KEY_CITY, ticket.getCity());
                    values.put(KEY_PHONE, ticket.getPhone());
                    values.put(KEY_EMAIL, ticket.getEmail());
                    values.put(KEY_TELEGRAM_USERNAME, ticket.getTelegram_username());
                    values.put(KEY_TIME_STAMP_CUSTOMER_CREATED, ticket.getTime_stamp_customer_created());
                    values.put(KEY_ATTACHMENT_ID, ticket.getAttachment_id());
                    values.put(KEY_URL_DOWNLOAD, ticket.getUrl_download());
                    values.put(KEY_MIME_TYPE, ticket.getMime_type());
                    values.put(KEY_LATEST_MESSAGE_TIME_STAMP, ticket.getLatest_message_time_stamp());
                    long id = db.insert(TABLE_TICKET, null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void add_or_update_Ticket1(List<Tickets.Data> ticketList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < ticketList.size(); i++) {
            Tickets.Data ticket = ticketList.get(i);
            String ticketSingleId = String.valueOf(ticket.getTicket_id());

            boolean status = Check_Is_Ticket_ID_Present_or_Not(TABLE_TICKET, KEY_TICKET_ID, ticketSingleId);

            // if status true -> update and if status false -> add ticket.
            if (status) { // update ticket
                try {
                    SQLiteDatabase update_db = this.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(KEY_TICKET_ID, ticket.getTicket_id());
                    values.put(KEY_INCOMING_MESSAGE_COUNT, ticket.getIncoming_message_count());
                    values.put(KEY_OUTGOING_MESSAGE_COUNT, ticket.getOutgoing_message_count());
                    values.put(KEY_MESSAGE_ID, ticket.getMessage_id());
                    values.put(KEY_MESSAGE_TYPE, ticket.getMessage_type());
                    values.put(KEY_MESSAGE_TEXT, ticket.getMessage_text());
                    values.put(KEY_TIME_STAMP, ticket.getTime_stamp());
                    values.put(KEY_AGENT_ID, ticket.getAgent_id());
                    values.put(KEY_TICKET_TYPE, ticket.getTicket_type());
                    values.put(KEY_DEPARTMENT, ticket.getDepartment());
                    values.put(KEY_TICKET_STATUS, ticket.getTicket_status());
                    values.put(KEY_TIME_STAMP_TICKET_GENERATED, ticket.getTime_stamp_ticket_generated());
                    values.put(KEY_TIME_STAMP_TICKET_RESOLVED, ticket.getTime_stamp_ticket_resolved());
                    String[] tags = ticket.getTags();
                    String strTags = "";
                    if (tags != null && tags.length > 0) {
                        for (String str : tags) {
                            strTags = strTags + " " + str;
                        }
                    }
                    values.put(KEY_TAGS, strTags);
                    values.put(KEY_CUSTOMER_ID, ticket.getCustomer_id());
                    values.put(KEY_NAME_FIRST, ticket.getName_first());
                    values.put(KEY_NAME_LAST, ticket.getName_last());
                    values.put(KEY_CITY, ticket.getCity());
                    values.put(KEY_PHONE, ticket.getPhone());
                    values.put(KEY_EMAIL, ticket.getEmail());
                    values.put(KEY_TELEGRAM_USERNAME, ticket.getTelegram_username());
                    values.put(KEY_TIME_STAMP_CUSTOMER_CREATED, ticket.getTime_stamp_customer_created());
                    values.put(KEY_ATTACHMENT_ID, ticket.getAttachment_id());
                    values.put(KEY_URL_DOWNLOAD, ticket.getUrl_download());
                    values.put(KEY_MIME_TYPE, ticket.getMime_type());
                    values.put(KEY_LATEST_MESSAGE_TIME_STAMP, ticket.getLatest_message_time_stamp());
                    int update_status = update_db.update(TABLE_TICKET, values, KEY_TICKET_ID + " = ?", new String[]{ticketSingleId});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { // add ticket
                try {
                    ContentValues values = new ContentValues();
                    values.put(KEY_TICKET_ID, ticket.getTicket_id());
                    values.put(KEY_INCOMING_MESSAGE_COUNT, ticket.getIncoming_message_count());
                    values.put(KEY_OUTGOING_MESSAGE_COUNT, ticket.getOutgoing_message_count());
                    values.put(KEY_MESSAGE_ID, ticket.getMessage_id());
                    values.put(KEY_MESSAGE_TYPE, ticket.getMessage_type());
                    values.put(KEY_MESSAGE_TEXT, ticket.getMessage_text());
                    values.put(KEY_TIME_STAMP, ticket.getTime_stamp());
                    values.put(KEY_AGENT_ID, ticket.getAgent_id());
                    values.put(KEY_TICKET_TYPE, ticket.getTicket_type());
                    values.put(KEY_DEPARTMENT, ticket.getDepartment());
                    values.put(KEY_TICKET_STATUS, ticket.getTicket_status());
                    values.put(KEY_TIME_STAMP_TICKET_GENERATED, ticket.getTime_stamp_ticket_generated());
                    values.put(KEY_TIME_STAMP_TICKET_RESOLVED, ticket.getTime_stamp_ticket_resolved());
                    String[] tags = ticket.getTags();
                    String strTags = "";
                    if (tags != null && tags.length > 0) {
                        for (String str : tags) {
                            strTags = strTags + " " + str;
                        }
                    }
                    values.put(KEY_TAGS, strTags);
                    values.put(KEY_CUSTOMER_ID, ticket.getCustomer_id());
                    values.put(KEY_NAME_FIRST, ticket.getName_first());
                    values.put(KEY_NAME_LAST, ticket.getName_last());
                    values.put(KEY_CITY, ticket.getCity());
                    values.put(KEY_PHONE, ticket.getPhone());
                    values.put(KEY_EMAIL, ticket.getEmail());
                    values.put(KEY_TELEGRAM_USERNAME, ticket.getTelegram_username());
                    values.put(KEY_TIME_STAMP_CUSTOMER_CREATED, ticket.getTime_stamp_customer_created());
                    values.put(KEY_ATTACHMENT_ID, ticket.getAttachment_id());
                    values.put(KEY_URL_DOWNLOAD, ticket.getUrl_download());
                    values.put(KEY_MIME_TYPE, ticket.getMime_type());
                    values.put(KEY_LATEST_MESSAGE_TIME_STAMP, ticket.getLatest_message_time_stamp());
                    long id = db.insert(TABLE_TICKET, null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean Check_Is_Ticket_ID_Present_or_Not(String TableName,
                                                     String dbfield, String fieldValue) {
        SQLiteDatabase read_db = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = read_db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // code to get single Customer
    /*public Customer getCustomer(String id) {
        Cursor cursor = null;
        Customer customer = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.query(TABLE_TICKET, new String[]{KEY_ID, KEY_TICKET_ID,
                            KEY_CUST_MOBILE, KEY_CUST_NAME, KEY_CUST_CITY,
                            KEY_TICKET_CREATED_DATE_TIME, KEY_TICKET_MODIFIED_DATE, KEY_AGENT_REPLY_COUNT, KEY_CUST_REPLY_COUNT,
                            KEY_LAST_REPLY_BY_WHOM, KEY_TICKET_ASSIGNEE, KEY_LAST_REPLY_DATE_TIME}, KEY_ID + "=?", new String[]{id},
                    null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();
            customer = new Customer(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11));
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return customer;
    }*/

    /*public int updateCustomer(Customer customer, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TICKET_CREATED_DATE_TIME, customer.getEngineer_name());
        values.put(KEY_TICKET_MODIFIED_DATE, customer.getEngineer_mobile());
        values.put(KEY_CUST_CITY, customer.getEngineer_department());
        values.put(KEY_TICKET_ASSIGNEE, customer.getSync_status());
        int status = db.update(TABLE_TICKET, values, KEY_ID + " = ?", new String[]{id});
        return status;
    }*/


    // code to get all customer issue from sqlite
    public List<Ticket.Data> getAllTickets() {
        List<Ticket.Data> allTicketList = new ArrayList<Ticket.Data>();
        String selectQuery = "SELECT  * FROM " + TABLE_TICKET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Ticket.Data ticket = new Ticket.Data();
                    ticket.setTicket_id(cursor.getInt(1));
                    ticket.setIncoming_message_count(cursor.getString(2));
                    ticket.setOutgoing_message_count(cursor.getString(3));
                    ticket.setMessage_id(cursor.getInt(4));
                    ticket.setMessage_type(cursor.getString(5));
                    ticket.setMessage_text(cursor.getString(6));
                    ticket.setTime_stamp(cursor.getString(7));
                    ticket.setAgent_id(cursor.getInt(8));
                    ticket.setTicket_type(cursor.getString(9));
                    ticket.setDepartment(cursor.getString(10));
                    ticket.setTicket_status(cursor.getString(11));
                    ticket.setTime_stamp_ticket_generated(cursor.getString(12));
                    ticket.setTime_stamp_ticket_resolved(cursor.getString(13));
//                    ticket.setTags(cursor.getString(14));
                    ticket.setCustomer_id(cursor.getInt(15));
                    ticket.setName_first(cursor.getString(16));
                    ticket.setName_last(cursor.getString(17));
                    ticket.setCity(cursor.getString(18));
                    ticket.setPhone(cursor.getString(19));
                    ticket.setEmail(cursor.getString(20));
                    ticket.setTelegram_username(cursor.getString(21));
                    ticket.setTime_stamp_customer_created(cursor.getString(22));
                    ticket.setAttachment_id(cursor.getString(23));
                    ticket.setUrl_download(cursor.getString(24));
                    ticket.setMime_type(cursor.getString(25));
                    ticket.setLatest_message_time_stamp(cursor.getString(26));
                    /*ticket.setCust_mobile(cursor.getString(1));
                    ticket.setCust_name(cursor.getString(2));
                    ticket.setCust_city(cursor.getString(3));
                    ticket.setTicket_created_date_time(cursor.getString(4));
                    ticket.setTicket_modified_date(cursor.getString(5));
                    ticket.setAgent_reply_count(cursor.getInt(6));
                    ticket.setCust_reply_count(cursor.getInt(7));
                    ticket.setLast_reply_by_whom(cursor.getString(8));
                    ticket.setTicket_assignee(cursor.getString(9));
                    ticket.setLast_reply_date_time(cursor.getString(10));*/
                    allTicketList.add(ticket);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allTicketList;
    }

    public List<Tickets.Data> getAllTickets1() {
        List<Tickets.Data> allTicketList = new ArrayList<Tickets.Data>();
        String selectQuery = "SELECT  * FROM " + TABLE_TICKET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Tickets.Data ticket = new Tickets.Data();
                    ticket.setTicket_id(cursor.getInt(1));
                    ticket.setIncoming_message_count(cursor.getString(2));
                    ticket.setOutgoing_message_count(cursor.getString(3));
                    ticket.setMessage_id(cursor.getInt(4));
                    ticket.setMessage_type(cursor.getString(5));
                    ticket.setMessage_text(cursor.getString(6));
                    ticket.setTime_stamp(cursor.getString(7));
                    ticket.setAgent_id(cursor.getInt(8));
                    ticket.setTicket_type(cursor.getString(9));
                    ticket.setDepartment(cursor.getString(10));
                    ticket.setTicket_status(cursor.getString(11));
                    ticket.setTime_stamp_ticket_generated(cursor.getString(12));
                    ticket.setTime_stamp_ticket_resolved(cursor.getString(13));
//                    ticket.setTags(cursor.getString(14));
                    ticket.setCustomer_id(cursor.getInt(15));
                    ticket.setName_first(cursor.getString(16));
                    ticket.setName_last(cursor.getString(17));
                    ticket.setCity(cursor.getString(18));
                    ticket.setPhone(cursor.getString(19));
                    ticket.setEmail(cursor.getString(20));
                    ticket.setTelegram_username(cursor.getString(21));
                    ticket.setTime_stamp_customer_created(cursor.getString(22));
                    ticket.setAttachment_id(cursor.getString(23));
                    ticket.setUrl_download(cursor.getString(24));
                    ticket.setMime_type(cursor.getString(25));
                    ticket.setLatest_message_time_stamp(cursor.getString(26));
                    /*ticket.setCust_mobile(cursor.getString(1));
                    ticket.setCust_name(cursor.getString(2));
                    ticket.setCust_city(cursor.getString(3));
                    ticket.setTicket_created_date_time(cursor.getString(4));
                    ticket.setTicket_modified_date(cursor.getString(5));
                    ticket.setAgent_reply_count(cursor.getInt(6));
                    ticket.setCust_reply_count(cursor.getInt(7));
                    ticket.setLast_reply_by_whom(cursor.getString(8));
                    ticket.setTicket_assignee(cursor.getString(9));
                    ticket.setLast_reply_date_time(cursor.getString(10));*/
                    allTicketList.add(ticket);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allTicketList;
    }

    // code to get all local not synced customer issue from sqlite
    /*public List<Customer> getNotSyncedCustomerIssue() {
        List<Customer> customerIssueList = new ArrayList<Customer>();
        // Select All Query
        String selectQuery = "SELECT * FROM  " + TABLE_TICKET + " WHERE " + KEY_CUST_REPLY_COUNT + " IS NULL AND " + KEY_LAST_REPLY_BY_WHOM + " IS NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getString(0));
                customer.setCustomer_mobile(cursor.getString(1));
                customer.setCustomer_name(cursor.getString(2));
                customer.setCustomer_issue(cursor.getString(3));
                customer.setEngineer_department(cursor.getString(4));
                customer.setEngineer_name(cursor.getString(5));
                customer.setEngineer_mobile(cursor.getString(6));
                customer.setDate_time(cursor.getString(7));
                customer.setYt_id(cursor.getString(8));
                customer.setCt_id(cursor.getString(9));
                customer.setSync_status(cursor.getString(10));
                customer.setRandom_id(cursor.getString(11));
                customerIssueList.add(customer);
            } while (cursor.moveToNext());
        }
        return customerIssueList;
    }*/
}


