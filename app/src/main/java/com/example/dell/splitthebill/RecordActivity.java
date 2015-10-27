package com.example.dell.splitthebill;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.dell.splitthebill.database.TipDbHelper;

/**
 * Created by dell on 10/27/2015.
 */
public class RecordActivity extends ActionBarActivity {
    ListView recordList;
    //List<BillDetails> billList;
    String[] from = {
            TipDbHelper.KEY_DATE,
            TipDbHelper.KEY_BILL,
            TipDbHelper.KEY_TIP_PERCENT,
            TipDbHelper.KEY_PEOPLE,
            TipDbHelper.KEY_TIP_AMOUNT,
            TipDbHelper.KEY_BILL_AMOUNT,
            TipDbHelper.KEY_AMOUNT_PER_PERSON,
            };
    TipDbHelper mTipDbHelper;
    int[] to = {R.id.tvDates, R.id.tvBills, R.id.tvTips, R.id.tvPeoples, R.id.tvTipAmounts,
            R.id.tvTotalBills};
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        initList();
    }

    private void initList() {
        recordList = (ListView) findViewById(R.id.recordList);
        mTipDbHelper = new TipDbHelper(this);
        mCursor = mTipDbHelper.getBills();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_items,
                mCursor,
                from,
                to);
        recordList.setAdapter(adapter);
    }
}
