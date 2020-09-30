package com.dpk.hammoq;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.dpk.hammoq.Model.User;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "UserDataBase121";

    public static final String TABLE_NAME = "UserTable121";

    public static final String TABLE_ID = "id";

    public static final String TABLE_USER_NAME = "name";

    public static final String TABLE_USER_EMAIL = "email";

    public static final String TABLE_USER_PASSWORD = "password";

    public static final String TABLE_USER_MOBILE = "mobile";


    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + TABLE_ID + " INTEGER PRIMARY KEY, " + TABLE_USER_NAME + " VARCHAR, " + TABLE_USER_EMAIL + " VARCHAR, " + TABLE_USER_PASSWORD + " VARCHAR, "+ TABLE_USER_MOBILE +" VARCHAR)";
        database.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_USER_NAME, user.getName());
        values.put(TABLE_USER_EMAIL, user.getEmail());
        values.put(TABLE_USER_PASSWORD, user.getPassword());
        values.put(TABLE_USER_MOBILE, user.getMobile());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<User> getAllUser() {
        String[] columns = {TABLE_ID, TABLE_USER_EMAIL, TABLE_USER_NAME, TABLE_USER_PASSWORD, TABLE_USER_MOBILE};
        String sortOrder = TABLE_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(TABLE_NAME, columns, null, null, null, null, sortOrder);

        if (mCursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(TABLE_ID))));
                user.setName(mCursor.getString(mCursor.getColumnIndex(TABLE_USER_NAME)));
                user.setEmail(mCursor.getString(mCursor.getColumnIndex(TABLE_USER_EMAIL)));
                user.setPassword(mCursor.getString(mCursor.getColumnIndex(TABLE_USER_PASSWORD)));
                user.setMobile(mCursor.getString(mCursor.getColumnIndex(TABLE_USER_MOBILE)));

                userList.add(user);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return userList;
    }

    public boolean updateData(String id, String name, String mobile, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_ID, id);
        values.put(TABLE_USER_NAME, name);
        values.put(TABLE_USER_MOBILE, mobile);
        values.put(TABLE_USER_EMAIL, email);
        values.put(TABLE_USER_PASSWORD, password);
        db.update(TABLE_NAME, values, "id = ?", new String[]{ id });
        return true;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_NAME, user.getName());
        values.put(TABLE_USER_EMAIL, user.getEmail());
        values.put(TABLE_USER_PASSWORD, user.getPassword());
        db.update(TABLE_NAME, values, TABLE_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{ id });
        return true;
    }

    public boolean checkUser(String email) {
        String[] columns = {TABLE_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = TABLE_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs,null,null,null);
        int mCursorCount = mCursor.getCount();
        mCursor.close();
        db.close();

        if (mCursorCount > 0)
        {
            return true;
        }

        return false;
    }

    public boolean checkUser(String email, String password) {

        String[] columns = {TABLE_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = TABLE_USER_EMAIL + " = ?" + " AND " + TABLE_USER_PASSWORD + " = ?";

        String[] selectionArgs = {email, password};

        Cursor mCursor = db.query(TABLE_NAME, columns, selection, selectionArgs,null,null,null);
        int mCursorCount = mCursor.getCount();
        mCursor.close();
        db.close();
        if (mCursorCount > 0)
        {
            return true;
        }
        return false;
    }
}