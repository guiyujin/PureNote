package com.guiyujin.purenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;;
import com.guiyujin.purenote.db.DBUtils;

public class AddContentActivity extends AppCompatActivity {
    private EditText et_text;
//    private NotesDB notesDB;
//    private SQLiteDatabase dbWriter;
    private Toolbar mtoolbar;
    private DBUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        dbUtils = new DBUtils(this);
        et_text = findViewById(R.id.et_text);
        mtoolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        notesDB = new NotesDB(this);
//        dbWriter = notesDB.getWritableDatabase();
    }

    /**
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:
                addDB();
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
        }
    }
    */

//
//    public void addDB()  {
//        ContentValues cv = new ContentValues();
//        cv.put(NotesDB.CONTENT, et_text.getText().toString());
//        cv.put(NotesDB.TIME,getTime());
//        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
//    }
//
//
//    public String getTime(){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//        Date curDate = new Date();
//        String str = format.format(curDate);
//        return str;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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
            case R.id.action_save:
                dbUtils.add(et_text.getText().toString());
                //addDB();
                finish();
                break;
            case R.id.action_clear:
                et_text.setText("");
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}