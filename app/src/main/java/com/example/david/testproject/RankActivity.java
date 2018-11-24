package com.example.david.testproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class RankActivity extends AppCompatActivity {

    private Record record;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the app full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        record = record.getInstant(this);

        setContentView(R.layout.activity_rank);

        listView = findViewById(R.id.list_view);
        listView.setAdapter(new RecordListAdapter(this, record.getRecords()));
    }
}
