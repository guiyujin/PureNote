package com.guiyujin.purenote.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guiyujin.purenote.R;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view =  inflater.inflate(R.layout.note_item, null);
        TextView contenttv = view.findViewById(R.id.list_content);
        TextView timetv = view.findViewById(R.id.list_time);
        cursor.moveToPosition(i);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        contenttv.setText(content);
        timetv.setText(time);
        return view;
    }
}
