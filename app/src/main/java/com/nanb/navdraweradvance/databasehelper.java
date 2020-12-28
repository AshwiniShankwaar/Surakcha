package com.nanb.navdraweradvance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class databasehelper extends SQLiteOpenHelper {


    public static final String TITLE = "TITLE";
    public static final String MESSAGE = "MESSAGE";
    public static final String TABLE_NAME = "MESSAGE_TABLE";
    public static final String contact_TABLE_NAME = "CONTACTS" ;
    public static final String CONTACT_NAME = "CONTACT_NAME" ;
    public static final String PHONE_NUMBER = "PHONE_NUMBER" ;
    public static final String PROFILETABLE = "PROFILE_TABLE";
    public static final String USERSNAME = "NAME";
    public static final String USERPHONENUMBER = "PHONE_NUMBER";
    public static final String USEREMAILID = "EMAIL_ID";
    public static final String PERMANENTADDRESS = "PERMANENT_ADDRESS";
    public static final String CURRENTADDRESS = "CURRENT_ADDRESS";
    public static final String SETTINGTABLE = "SETTING_TABLE";
    public static final String LOCATIONSERVICE = "LOCATION_SERVICE";
    public static final String MESSAGE_ID = "SELECT_MESSAGE_ID";

    public databasehelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null,5);
        //databasehelper(context,example.db,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtablestatement = "CREATE TABLE " + TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT," + MESSAGE + " TEXT)";
        String createcontacttablestatement = "CREATE TABLE " + contact_TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + CONTACT_NAME + " TEXT," + PHONE_NUMBER + " TEXT)";
        String createprofiletable = "CREATE TABLE " + PROFILETABLE+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USERSNAME + " TEXT," + USERPHONENUMBER + " TEXT,"+USEREMAILID+" TEXT,"+PERMANENTADDRESS+" TEXT,"+CURRENTADDRESS+" TEXT)";
        String createsettingtable = "CREATE TABLE " + SETTINGTABLE+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + LOCATIONSERVICE + " INTEGER," + MESSAGE_ID + " INTEGER)";
        db.execSQL(createcontacttablestatement);
        db.execSQL(createtablestatement);
        db.execSQL(createprofiletable);
        db.execSQL(createsettingtable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String addsettingdata(int message_id,int location_service){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOCATIONSERVICE,location_service);
        values.put(MESSAGE_ID,message_id);
        Long insert = db.insert(SETTINGTABLE,null,values);
        if(insert == -1){
            return "Error occurs";
        }else{
            return "Saved";
        }
    }
    public boolean updatesettingdata(int message_id,int location_service){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "UPDATE "+ SETTINGTABLE+" SET "+MESSAGE_ID+" = "+message_id+", "+LOCATIONSERVICE+"="+location_service+" WHERE ID = 1";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.moveToNext()){
            return false;
        }else{
            return true;
        }

    }
    public String addOne(msgmodel msgmodel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        //CheckIsDataAlreadyInDBorNot(TABLE_NAME,TITLE,msgmodel.getTitle());

        if(!CheckIsDataAlreadyInDBorNot(TABLE_NAME,TITLE,msgmodel.getTitle()))
        {
        values.put(TITLE,msgmodel.getTitle());
        values.put(MESSAGE,msgmodel.getMsg());
        Long insert = db.insert(TABLE_NAME,null,values);
        if(insert == -1){
            return "Error occurs";
        }else{
            return "Saved";
        }
        }else{
            //Toast.makeText()
            return "Title not available";
        }
    }

    public String addprofile(String name,String email,String phone,String permanentaddress,String currentaddress){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!CheckIsDataAlreadyprofiletable(name,email,phone,permanentaddress,currentaddress)){
            values.put(USERSNAME,name);
            values.put(USERPHONENUMBER,phone);
            values.put(USEREMAILID,email);
            values.put(PERMANENTADDRESS,permanentaddress);
            values.put(CURRENTADDRESS,currentaddress);
            Long insert = db.insert(PROFILETABLE,null,values);
            if(insert == -1){
                return "Error occurs";
            }else{
                return "Saved";
            }
        }else {
            return "Data available";
        }
    }
    public List<msgmodel> getmsgdata(){
        List<msgmodel> resultdata = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                int messageid = cursor.getInt(0);
                String msgtitle = cursor.getString(1);
                String msgdata = cursor.getString(2);

                msgmodel msgmodel = new msgmodel(messageid,msgtitle,msgdata);
                resultdata.add(msgmodel);
            }while (cursor.moveToNext());

        }else{
            // no data
        }
        cursor.close();
        db.close();
        return resultdata;
    }
    public List<msgmodel> getsellectedmsgdata(int msgid){
        List<msgmodel> resultdata = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE ID = \""+ msgid+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                int messageid = cursor.getInt(0);
                String msgtitle = cursor.getString(1);
                String msgdata = cursor.getString(2);

                msgmodel msgmodel = new msgmodel(messageid,msgtitle,msgdata);
                resultdata.add(msgmodel);
            }while (cursor.moveToNext());

        }else{
            // no data
        }
        cursor.close();
        db.close();
        return resultdata;
    }
   public String getselectedmessage(int messageid){
        String resultdata ="";
        if(!CheckIsDataAlreadyInsettingDBorNot(TABLE_NAME,"ID",messageid)){
            resultdata = "";
            return resultdata;
        }else{
            //resultdata = "available";
            String query = "SELECT * FROM "+TABLE_NAME+" WHERE ID = \""+ messageid+"\"";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do {
                    String msg = cursor.getString(2);
                    resultdata = msg;
                }while (cursor.moveToNext());

            }else{
                // no data
            }
            cursor.close();
            //Log.d("Query",query);
            return resultdata;
        }


   }
    public List<profilemodel> getprofiledata(){
        List<profilemodel> resultdata = new ArrayList<>();
        String Query = "SELECT * FROM "+PROFILETABLE+" ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.moveToFirst()){
            do {

                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);
                String pradd = cursor.getString(4);
                String curradd = cursor.getString(5);

                profilemodel profilemodel = new profilemodel(name,phone,email,pradd,curradd);
                resultdata.add(profilemodel);
            }while (cursor.moveToNext());

        }else{
            // no data
        }
        cursor.close();
        db.close();
        return resultdata;

    }
    public List<contactmodel> getsavedcontactdata(){
        List<contactmodel> resultdata = new ArrayList<>();
        String query = "SELECT * FROM "+contact_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                String contactid = cursor.getString(0);
                String contactname = cursor.getString(1);
                String contactphonenbr = cursor.getString(2);

               contactmodel contactmodel = new contactmodel(contactname,contactphonenbr,contactid);
                resultdata.add(contactmodel);
            }while (cursor.moveToNext());

        }else{
            // no data
        }
        cursor.close();
        db.close();
        return resultdata;
    }
    public List<settingmodel> getsettingdata(){
        List<settingmodel> resultdata = new ArrayList<>();
        String query = "SELECT * FROM "+SETTINGTABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                int msgid = cursor.getInt(2);
                int locationservice = cursor.getInt(1);
               // String contactphonenbr = cursor.getString(2);

                settingmodel settingmodel = new settingmodel(msgid,locationservice);
                resultdata.add(settingmodel);
            }while (cursor.moveToNext());

        }else{
            // no data
        }
        cursor.close();
        db.close();
        return resultdata;
    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                               String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + "\""+fieldValue+"\"";
       // Log.d("sql",Query);
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        sqldb.close();
        return true;
    }
    public boolean CheckIsDataAlreadyInsettingDBorNot(String TableName,
                                               String dbfield, int fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + "\""+fieldValue+"\"";
        Log.d("sql",Query);
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        sqldb.close();
        return true;
    }
    public boolean CheckIsDataAlreadyprofiletable(String name,String email,String phone,String permanentaddress,String currentaddress) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + PROFILETABLE + " where " + USERSNAME + " = " + "\""+name+"\" AND "+ USEREMAILID + " = " + "\""+email+"\" AND "
                + USERPHONENUMBER + " = " + "\""+phone+"\" AND "+ PERMANENTADDRESS + " = " + "\""+permanentaddress+"\" AND "+ CURRENTADDRESS + " = " + "\""+currentaddress+"\"";
        Log.d("sql",Query);
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        sqldb.close();
        return true;
    }
    public boolean CheckifTableIsEmptyOrNot(String TableName) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TableName;
        //Log.d("sql",Query);
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        sqldb.close();
        return true;
    }
    public boolean deleteone(msgmodel msgmodel){
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "DELETE FROM "+TABLE_NAME+" WHERE ID = "+msgmodel.getId();
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.moveToNext()){
            return false;
        }else{
            return true;
        }

    }
    public boolean deletecontactone(contactmodel contactmodel){
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "DELETE FROM "+contact_TABLE_NAME+" WHERE ID = "+contactmodel.getId();
        Log.d("Query",Query);
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.moveToNext()){
            return false;
        }else{
            return true;
        }

    }
    public String addcontactOne(String name,String phoneNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //CheckIsDataAlreadyInDBorNot(TABLE_NAME,TITLE,msgmodel.getTitle());
        if(!CheckIsDataAlreadyInDBorNot(contact_TABLE_NAME,PHONE_NUMBER,phoneNumber))
        {
            values.put(CONTACT_NAME,name);
            values.put(PHONE_NUMBER,phoneNumber);
            long insert = db.insert(contact_TABLE_NAME,null,values);
            if(insert == -1){
                return "Error occurs";
            }else{
                return "Saved";
            }
        }else{
            return "Contact already saved";
        }
    }
}
