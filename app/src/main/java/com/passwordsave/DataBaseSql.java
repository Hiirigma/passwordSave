package com.passwordsave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseSql extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private final String DATABASE_PASS = "pass";
    private final String DATABASE_APP = "app";
    private static final String DATABASE_NAME = "passDatabase.db";
    public DataBaseSql(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean createDatabase(String s_name)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        if (s_name.equals(DATABASE_PASS))
        {
            dbApplication.execSQL("create table if not exists '" + s_name + "' ("
                    + "id_ integer primary key autoincrement,"
                    + "pass_ text" + ");");
        } else {
            dbApplication.execSQL("create table if not exists '" + s_name + "' ("
                    + "id_ integer primary key autoincrement,"
                    + "app_ text,"
                    + "login_ text,"
                    + "pass_ text,"
                    + "info_ text" + ");");
        }
        return this.checkTableExists(s_name);
    }

    public void importPassword(String sPass)
    {
        try {
            CryptoLib _crypt = new CryptoLib();
            String output= "";
            String plainText = "This is the text to be encrypted.";
            String key = CryptoLib.SHA256(sPass, 32); //32 bytes = 256 bit
            output = _crypt.encrypt(plainText, sPass); //encrypt
            System.out.println("encrypted text=" + output);
            output = _crypt.decrypt(output, sPass); //decrypt
            System.out.println("decrypted text=" + output);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public int verifyPassword(String s_name, String s_password) {
        SQLiteDatabase dbApplication = this.getReadableDatabase();
        String l_s_pass;
        int l_d_id = -1;
        Cursor cursor = dbApplication.query(s_name, null, null, null, null, null, null);
        if (cursor == null)
        {
            return l_d_id;
        }
        if (cursor.moveToFirst()) {

            if (s_name.equals(DATABASE_PASS)) {
                int idColIndex = cursor.getColumnIndex("id_");
                int passColIndex = cursor.getColumnIndex("pass_");

                do {
                    l_s_pass = cursor.getString(passColIndex);
                    if (s_password.equals(l_s_pass)) {
                        l_d_id = cursor.getInt(idColIndex);
                        break;
                    }
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        return l_d_id;
    }

    public long verifyData(String s_name, String [] s_data) {
        SQLiteDatabase dbApplication = this.getReadableDatabase();
        String l_s_pass;
        String l_s_login;
        String l_s_app;
        long l_d_id = -1;
        Cursor cursor = dbApplication.query(s_name, null, null, null, null, null, null);
        if (cursor == null)
        {
            return l_d_id;
        }
        if (cursor.moveToFirst()) {

            if (s_name.equals(DATABASE_APP)) {
                int idColIndex = cursor.getColumnIndex("id_");
                int passColIndex = cursor.getColumnIndex("pass_");
                int appColIndex = cursor.getColumnIndex("app_");
                int loginColIndex = cursor.getColumnIndex("login_");

                if (s_data.length < 6) {
                    return l_d_id;
                }

                do {
                    l_s_pass = cursor.getString(passColIndex);
                    l_s_login = cursor.getString(loginColIndex);
                    l_s_app = cursor.getString(appColIndex);
                    if (s_data[0].equals(l_s_app) && s_data[1].equals(l_s_login) && s_data[2].equals(l_s_pass)) {
                        l_d_id = cursor.getInt(idColIndex);
                        break;
                    }
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        return l_d_id;
    }


    public boolean addToDatabase(String s_name, String [] sData)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (sData.length < 4)
        {
            return false;
        }
        contentValues.put("app_", sData[0]);
        contentValues.put("login_", sData[1]);
        contentValues.put("pass_", sData[2]);
        contentValues.put("info_", sData[3]);
        long rowID = dbApplication.insert(s_name, null, contentValues);
        return true;
    }

    public long addPasswordToDatabase(String s_name, String s_password)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (s_password.equals(""))
        {
            return -1;
        }
        contentValues.put("pass_", s_password);
        dbApplication.insert(s_name, null, contentValues);
        return this.verifyPassword(s_name,s_password);
    }

    public boolean addToToDatabaseById(String s_name, String [] sData, int d_id)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_", sData[0]);
        contentValues.put("login_", sData[1]);
        contentValues.put("pass_", sData[2]);
        contentValues.put("info_", sData[3]);
        long rowID = dbApplication.update(s_name, contentValues, "id_="+d_id,null);
        return true;
    }

    public boolean addPasswordToDBToId(String s_name, String s_pass, long d_id)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pass_", s_pass);
        long rowID = dbApplication.update(s_name, contentValues, "id_="+d_id,null);
        return true;
    }

    public int getSizeDB(String s_name)
    {
        SQLiteDatabase dbApplication = this.getReadableDatabase();
        int dItr = 1;
        Cursor cursor = dbApplication.query(s_name, null, null, null, null, null, null);
        if (cursor == null)
        {
            return dItr;
        }
        dItr = cursor.getCount();
        return dItr;
    }

    public boolean getFromDB(String s_name,int[] dPos, String[][] sData)
    {
        if (sData.length < 6)
        {
            return false;
        }

        SQLiteDatabase dbApplication = this.getReadableDatabase();
        int itr = 0;
        Cursor cursor = null;

        try {
            cursor = dbApplication.query(s_name, null, null, null, null, null, null);
        }
        catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){
                return false;
            }
        }

        if (cursor == null)
        {
            return false;
        }

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex("id_");
            int appColIndex = cursor.getColumnIndex("app_");
            int loginColIndex = cursor.getColumnIndex("login_");
            int passColIndex = cursor.getColumnIndex("pass_");
            int infoColIndex = cursor.getColumnIndex("info_");
            do {
                dPos[itr] = cursor.getInt(idColIndex);
                sData[0][itr] = cursor.getString(appColIndex);
                sData[1][itr] = cursor.getString(loginColIndex);
                sData[2][itr] = cursor.getString(passColIndex);
                sData[3][itr] = cursor.getString(infoColIndex);
                itr +=1;
            } while (cursor.moveToNext());
        }
        else
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkTableExists(String s_name)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();

        Cursor mCursor = dbApplication.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + s_name + "'", null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }


    public boolean deleteDatabase(String s_name)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        if (this.checkTableExists(s_name) == false)
        {
            return false;
        }
        dbApplication.execSQL("drop table if exists '" + s_name + "'");
        return this.checkTableExists(s_name);
    }

    public void deleteItem(String s_name, long d_id)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        dbApplication.delete(s_name, "id_=?", new String[]{Long.toString(d_id)});
    }

    public long deletePasswordFromDatabase(String s_name, String s_pass)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        long d_id = this.verifyPassword(s_name,s_pass);

        if (d_id == -1)
        {
            return -1;
        }

        dbApplication.delete(s_name, "id_=?", new String[]{Long.toString(d_id)});
        return d_id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
