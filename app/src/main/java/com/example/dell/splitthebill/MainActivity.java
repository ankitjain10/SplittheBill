package com.example.dell.splitthebill;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dell.splitthebill.database.TipDbHelper;


public class MainActivity extends ActionBarActivity {
    TipDbHelper mTipDbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTipDbHelper=new TipDbHelper(this);
        db=mTipDbHelper.getWritableDatabase();
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
        Intent actionIntent;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            actionIntent=new Intent(this,UserSettings.class);
            startActivity(actionIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
