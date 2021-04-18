package com.guiyujin.purenote.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public DBUtils(Context context) {
        this.context = context;
    }

    public void add(String content){
        note = new NotesDB(context);
        dbWriter = note.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT,content);
        cv.put(NotesDB.TIME,getTime());
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    public void delete(Intent intent){
        note = new NotesDB(context);
        dbWriter = note.getWritableDatabase();
        dbWriter.delete(NotesDB.TABLE_NAME,"_id=" + intent.getIntExtra(NotesDB.ID,0),null);
    }


    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
}
