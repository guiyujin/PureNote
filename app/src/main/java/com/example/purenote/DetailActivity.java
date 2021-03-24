package com.example.purenote;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button del,back;
    private TextView tv;
    private NotesDB note;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        del = findViewById(R.id.delete);
        back = findViewById(R.id.back);
        tv = findViewById(R.id.d_text);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        back.setOnClickListener(this);
        del.setOnClickListener(this);
        tv.setText(getIntent().getStringExtra(NotesDB.CONTENT));
        note = new NotesDB(this);
        dbWriter = note.getWritableDatabase();
    }

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

    public void delete(){
        dbWriter.delete(NotesDB.TABLE_NAME,"_id=" + getIntent().getIntExtra(NotesDB.ID,0),null);
    }
}