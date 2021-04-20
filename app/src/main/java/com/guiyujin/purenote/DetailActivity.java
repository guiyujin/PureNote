package com.guiyujin.purenote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.guiyujin.purenote.db.DBUtils;
import com.guiyujin.purenote.db.NotesDB;
import com.guiyujin.purenote.utils.ThingsReminder;

import java.lang.reflect.Method;

public class DetailActivity extends AppCompatActivity{
    //private Button del,back;
    private EditText editText;
    private NotesDB note;
    private SQLiteDatabase dbWriter;
    private Toolbar mtoolbar;
    private String wordSizePrefs;
    private int checkedItem;
    private byte[] bytes;
    private DBUtils dbUtils;
    private int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbUtils = new DBUtils(this);

        editText = findViewById(R.id.d_text);
        mtoolbar = findViewById(R.id.toolbar_detail);
        editText.setText(getIntent().getStringExtra(NotesDB.CONTENT));
        _id = getIntent().getIntExtra(NotesDB.ID,0);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dbUtils.update(editText.getText().toString(),_id);
                finish();
            }
        });

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
            case R.id.action_save:
                dbUtils.update(editText.getText().toString(),_id);
                finish();
                break;
            case R.id.action_delete:
                Intent intents = getIntent();
                dbUtils.delete(intents);
                //delete();
                finish();
                break;

            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString().replaceAll("<img src='(.*?)'/>","[图片]").replaceAll("<voice src='(.*?)'/>","[语音]"));
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
                                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getText());
                                           runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //ScrollView scrollView = findViewById(R.id.scrollView);
                                                //scrollView.setVisibility(View.VISIBLE);
                                                if (bytes == null) {
                                                    spannableStringBuilder.setSpan(new TypefaceSpan("serif"),0,spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                                    //设置字体前景色
                                                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                }
                                                editText.setText(spannableStringBuilder);
                                                editText.setTextSize(wordSize);
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