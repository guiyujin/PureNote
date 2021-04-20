package com.guiyujin.purenote.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Adapter;

import com.guiyujin.purenote.adapter.MyAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils {
    private NotesDB note;
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReader;
    private Cursor cursor;
    private Context context;
    private MyAdapter adapter;
    private String string = getTime();

    public DBUtils(Context context) {
        this.context = context;
    }

    public void add(String content){
        note = new NotesDB(context);
        dbWriter = note.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesDB.CONTENT,content);
        contentValues.put(NotesDB.TIME,getTime());
        dbWriter.insert(NotesDB.TABLE_NAME, null, contentValues);
    }

    public void delete(Intent intent){
        note = new NotesDB(context);
        dbWriter = note.getWritableDatabase();
        dbWriter.delete(NotesDB.TABLE_NAME,"_id=" + intent.getIntExtra(NotesDB.ID,0),null);
    }

    public void update(String content, int id){
        note = new NotesDB(context);
        dbWriter = note.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesDB.CONTENT,content);
        contentValues.put(NotesDB.TIME,getTime());
        dbWriter.update(NotesDB.TABLE_NAME,contentValues,NotesDB.ID+"=?", new String[]{String.valueOf(id)});
        dbWriter.close();
    }


    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
}
