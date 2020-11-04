package com.passwordsave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseSql extends SQLiteOpenHelper
{
    private static String s_Database_global = "";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "saverDatabase.db";
    public DataBaseSql(Context context, String sName) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
        s_Database_global = sName;
    }

    public boolean createDatabase()
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        dbApplication.execSQL("create table if not exists '" + s_Database_global + "' ("
                + "id_ integer primary key autoincrement,"
                + "app_ text,"
                + "login_ text,"
                + "pass_ text,"
                + "info_ text" + ");");
        return this.checkTableExists(s_Database_global);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public int checkDB(String sApp, String sLogin) {
        SQLiteDatabase dbApplication = this.getReadableDatabase();
        String l_sApp;
        String l_sLogin;
        int l_dId = 0;
        Cursor cursor = dbApplication.query(s_Database_global, null, null, null, null, null, null);
        if (cursor == null)
        {
            return l_dId;
        }
        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex("id_");
            int nameColIndex = cursor.getColumnIndex("app_");
            int loginColIndex = cursor.getColumnIndex("login_");

            do {
                l_dId = cursor.getInt(idColIndex);
                l_sApp = cursor.getString(nameColIndex);
                l_sLogin = cursor.getString(loginColIndex);
                if (sApp.equals(l_sApp) && sLogin.equals(l_sLogin) )
                {
                    l_dId = -1;
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return l_dId;
    }


    public boolean addToDB(String sTarget, String sName, String sPass, String sAddit)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_", sTarget);
        contentValues.put("login_", sName);
        contentValues.put("pass_", sPass);
        contentValues.put("info_", sAddit);
        long rowID = dbApplication.insert(s_Database_global, null, contentValues);
        return true;
    }

    public boolean addToDBToId(String sTarget, String sName, String sPass, String sAddit, int dId)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_", sTarget);
        contentValues.put("login_", sName);
        contentValues.put("pass_", sPass);
        contentValues.put("info_", sAddit);
        long rowID = dbApplication.update(s_Database_global, contentValues, "id_="+dId,null);
        return true;
    }

    public int getSizeDB()
    {
        SQLiteDatabase dbApplication = this.getReadableDatabase();
        int dItr = 1;
        Cursor cursor = dbApplication.query(s_Database_global, null, null, null, null, null, null);
        if (cursor == null)
        {
            return dItr;
        }
        dItr = cursor.getCount();
        return dItr;
    }

    public boolean getFromDB(int[] dPos, String[] sApp, String[] sLogin, String[] sPass, String[] sAddit)
    {
        SQLiteDatabase dbApplication = this.getReadableDatabase();
        int itr = 0;
        Cursor cursor = null;

        try {
            cursor = dbApplication.query(s_Database_global, null, null, null, null, null, null);
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
            int additColIndex = cursor.getColumnIndex("info_");
            do {
                dPos[itr] = cursor.getInt(idColIndex);
                sApp[itr] = cursor.getString(appColIndex);
                sLogin[itr] = cursor.getString(loginColIndex);
                sPass[itr] = cursor.getString(passColIndex);
                sAddit[itr] = cursor.getString(additColIndex);
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

    public boolean checkTableExists(String sName)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        dbApplication.execSQL("drop table if exists '" + s_Database_global + "'");
        String sql = "SELECT * FROM sqlite_master WHERE type='table' AND name='"+sName+"'";

        Cursor mCursor = dbApplication.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }


    public boolean deleteDatabase(String sName)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        if (this.checkTableExists(sName) == false)
        {
            return false;
        }
        dbApplication.execSQL("drop table if exists '" + sName + "'");
        return this.checkTableExists(sName);
    }

    public void deleteItem(int dId)
    {
        SQLiteDatabase dbApplication = this.getWritableDatabase();
        dbApplication.delete(s_Database_global, "id_=?", new String[]{Integer.toString(dId)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
