package com.example.purenote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purenote.db.NotesDB;
import com.example.purenote.utils.ThingsReminder;

import java.lang.reflect.Method;

public class DetailActivity extends AppCompatActivity{
    //private Button del,back;
    private TextView textView;
    private NotesDB note;
    private SQLiteDatabase dbWriter;
    private Toolbar mtoolbar;
    private Context context;
    private String wordSizePrefs;
    private int checkedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //del = findViewById(R.id.delete);
        //back = findViewById(R.id.back);
        textView = findViewById(R.id.d_text);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mtoolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //back.setOnClickListener(this);
        //del.setOnClickListener(this);
        textView.setText(getIntent().getStringExtra(NotesDB.CONTENT));
        note = new NotesDB(this);
        dbWriter = note.getWritableDatabase();
    }

    /**
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete:
                delete();
                finish();
                break;
            case R.id.back:
                finish();
                break;
            default:
        }
    }
    */

    public void delete(){
        dbWriter.delete(NotesDB.TABLE_NAME,"_id=" + getIntent().getIntExtra(NotesDB.ID,0),null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_delete:
                delete();
                finish();
                break;

            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, textView.getText().toString().replaceAll("<img src='(.*?)'/>","[图片]").replaceAll("<voice src='(.*?)'/>","[语音]"));
                startActivity(Intent.createChooser(intent, "分享到"));
                break;

            case R.id.action_add_alarm:
                //Toast.makeText(DetailActivity.this,"开发中",Toast.LENGTH_SHORT).show();
                ThingsReminder.OpenCalendar(this, note.TABLE_NAME.toString());
                break;

            case R.id.action_text_size:
                final String[] wordSizes =  new String[]{"正常","大","超大"};
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                AlertDialog alertDialog = builder.setTitle("选择字体大小")
                        .setSingleChoiceItems(wordSizes, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                wordSizePrefs = wordSizes[i];
                                float wordSize = getWordSize(wordSizePrefs);
                                SharedPreferences prefs = getSharedPreferences("Setting",MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("WordSize",wordSizePrefs);
                                editor.apply();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                           runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //ScrollView scrollView = findViewById(R.id.scrollView);
                                                //scrollView.setVisibility(View.VISIBLE);
                                                textView.setTextSize(wordSize);
                                                //((MainActivity) getActivity()).setTouchEventFlag(true);
                                            }
                                        });
                                    }
                                }).start();
                            }
                        }).create();
                alertDialog.show();
                break;
            case R.id.action_box:
                Toast.makeText(DetailActivity.this,"开发中",Toast.LENGTH_SHORT).show();

                break;
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

    private float getWordSize(String str){
        switch (str) {
            case "正常":
                checkedItem = 0;
                return 20;
            case "大":
                checkedItem = 1;
                return 25;
            case "超大":
                checkedItem = 2;
                return 30;
        }
        return 20;
    }
}