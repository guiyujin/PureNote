package com.guiyujin.purenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guiyujin.purenote.adapter.MyAdapter;
import com.guiyujin.purenote.db.DBUtils;
import com.guiyujin.purenote.db.NotesDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private FloatingActionButton fab;
    private ListView lv;
    private Intent intent;
    private MyAdapter adapter;
    private Cursor cursor;
    private Toolbar mtoolbar;
    private SearchView mSearchView;

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
        mtoolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mtoolbar);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

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
        intent = new Intent(this, AddContentActivity.class);
        switch (view.getId()){
            case R.id.add:
                startActivity(intent);
                break;
            default:
        }
    }

    public void showItem(){
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null,
                null, null ,null);
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
//            case R.id.action_about:
//                Toast.makeText(MainActivity.this,"开发中",Toast.LENGTH_SHORT).show();
//
//                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}