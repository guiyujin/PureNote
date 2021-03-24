package com.example.purenote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private FloatingActionButton fab;
    private ListView lv;
    private Intent intent;
    private MyAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
        initView();
    }

    public void initView(){
        lv = findViewById(R.id.list);
        fab = findViewById(R.id.add);
        fab.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra(NotesDB.ID,cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                intent.putExtra(NotesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                intent.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        intent = new Intent(this, AddContent.class);
        switch (view.getId()){
            case R.id.add:
                startActivity(intent);
                break;
            default:
        }
    }

    public void selectDB(){
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null,
                null, null ,null);
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}