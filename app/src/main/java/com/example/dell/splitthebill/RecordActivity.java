package com.example.dell.splitthebill;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.splitthebill.database.TipDbHelper;
import com.example.dell.splitthebill.model.BillDetails;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dell on 10/27/2015.
 */
public class RecordActivity extends ActionBarActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    private static final String TAG = RecordActivity.class.getSimpleName();
    ListView recordList;
    LayoutInflater mInflater;
    boolean sortByAscending = true;
    ArrayList<BillDetails> records = new ArrayList<BillDetails>();
    TipDbHelper mTipDbHelper;
    Cursor mCursor;
    RecordAdapter recordAdapter;
    FloatingActionButton actionButton;
    FloatingActionMenu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list);
        initList();
        initFAB();
    }

    @SuppressLint("ResourceAsColor")
    private void initFAB() {
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_action_new);

        actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .setContentView(icon).build();


        ImageView itemCal = new ImageView(this); // Create an icon
        itemCal.setBackgroundColor(R.color.SeaGreen);
        itemCal.setImageResource(R.drawable.ic_action_calendar);

        ImageView itemStar = new ImageView(this); // Create an icon
        itemStar.setBackgroundColor(R.color.SeaGreen);
        itemStar.setImageResource(R.drawable.ic_action_important);


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        SubActionButton sortByDate = itemBuilder.setContentView(itemCal).build();
        SubActionButton sortByAmount = itemBuilder.setContentView(itemStar).build();

        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sortByDate)
                .addSubActionView(sortByAmount)
                .attachTo(actionButton)
                .build();
        sortByAmount.setTag("sortByAmount");
        sortByDate.setTag("sortByDate");

        sortByDate.setOnClickListener(this);
        sortByAmount.setOnClickListener(this);
    }

    private void initList() {
        mTipDbHelper = new TipDbHelper(this);
        recordList = (ListView) findViewById(R.id.recordList);
        mTipDbHelper = new TipDbHelper(this);
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCursor = mTipDbHelper.getBills();
        while (mCursor.moveToNext()) {
            BillDetails bill = new BillDetails();
            int id = mCursor.getInt(mCursor.getColumnIndex(TipDbHelper.KEY_ID));
            bill.setId(mCursor.getInt(mCursor.getColumnIndex(TipDbHelper.KEY_ID)));
            bill.setDate(mCursor.getString(mCursor.getColumnIndex(TipDbHelper.KEY_DATE)));
            bill.setBill(mCursor.getDouble(mCursor.getColumnIndex(TipDbHelper.KEY_BILL)));
            bill.setTipPercent(mCursor.getInt(mCursor.getColumnIndex(TipDbHelper.KEY_TIP_PERCENT)));
            bill.setPeople(mCursor.getInt(mCursor.getColumnIndex(TipDbHelper.KEY_PEOPLE)));
            bill.setTipAmount(mCursor.getDouble(mCursor.getColumnIndex(TipDbHelper.KEY_TIP_AMOUNT)));
            bill.setBillAmount(mCursor.getDouble(mCursor.getColumnIndex(TipDbHelper.KEY_BILL_AMOUNT)));
            bill.setAmtPerPerson(mCursor.getDouble(mCursor.getColumnIndex(TipDbHelper.KEY_AMOUNT_PER_PERSON)));
            Log.v(TAG, "id" + id + " : " + bill.toString());
            records.add(bill);
        }
        if (records != null) {
            recordAdapter = new RecordAdapter(records, this, mInflater);
            recordList.setAdapter(recordAdapter);
            recordList.setOnItemLongClickListener(this);
        } else {
            new TextView(this).setText("NO DATA");

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        confirmAndDeleteBill(position);
        return false;
    }

    private void confirmAndDeleteBill(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecord(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteRecord(int position) {
        BillDetails billToBeDeleted = records.get(position);
        Log.v(TAG, "billToBeDeleted" + billToBeDeleted.toString());
        records.remove(position);
        mTipDbHelper.deleteRecord(billToBeDeleted);
        recordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("sortByAmount")) {
            actionMenu.close(true);
            records = sortByAmount(records, sortByAscending);
            recordAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Sort Amount By Ascending " + sortByAscending, Toast.LENGTH_SHORT).show();
        }
        if (v.getTag().equals("sortByDate")) {
            actionMenu.close(true);
            records = sortByDate(records, sortByAscending);
            recordAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Sort Date By Ascending " + sortByAscending, Toast.LENGTH_SHORT).show();
        }
        sortByAscending = toggleSorter(sortByAscending);

    }

    private boolean toggleSorter(boolean sortByAscending) {
        return !sortByAscending;
    }

    private ArrayList<BillDetails> sortByAmount(ArrayList<BillDetails> records, final boolean sortByAscending) {
        Collections.sort(records, new Comparator<BillDetails>() {
            @Override
            public int compare(BillDetails lhs, BillDetails rhs) {
                if (sortByAscending) {
                    if (lhs.getBillAmount() > rhs.getBillAmount())
                        return 1;
                    else if (lhs.getBillAmount() < rhs.getBillAmount())
                        return -1;
                } else if (!sortByAscending) {
                    if (lhs.getBillAmount() < rhs.getBillAmount())
                        return 1;
                    else if (lhs.getBillAmount() > rhs.getBillAmount())
                        return -1;
                }

                return 0;
            }
        });
        return records;
    }

    private ArrayList<BillDetails> sortByDate(final ArrayList<BillDetails> records, final boolean sortByAscending) {
        Collections.sort(records, new Comparator<BillDetails>() {
            @Override
            public int compare(BillDetails lhs, BillDetails rhs) {
                if (sortByAscending) {
                    return lhs.getDate().compareTo(rhs.getDate());
                } else
                    return rhs.getDate().compareTo(lhs.getDate());
            }
        });
        return records;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.goBackAction: {
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
        }
        return false;
    }

}
