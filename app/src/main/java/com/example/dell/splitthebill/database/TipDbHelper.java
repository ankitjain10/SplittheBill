package com.example.dell.splitthebill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.splitthebill.model.BillDetails;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

/**
 * Created by dell on 10/26/2015.
 */
public class TipDbHelper extends SQLiteOpenHelper {
    private static final String TAG = TipDbHelper.class.getSimpleName();
    private static final String DB_NAME = "tip_db";
    private static final String BILL_TABLE = "tip_table";
    private static final int DB_VERSION = 1;
    private static final String KEY_ID = "_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_BILL = "bill";
    private static final String KEY_TIP_PERCENT = "tip_percent";
    private static final String KEY_PEOPLE = "people";
    private static final String KEY_TIP_AMOUNT = "tip_amount";
    private static final String KEY_BILl_AMOUNT = "bill_amount";
    private static final String KEY_AMOUNT_PER_PERSON = "amount_per_person";
    private static final String KEY_BILL_NAME = "bill_name";
    Context mContext;

    private static final String create_table = String.format(
            "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s REAL,%s INTEGER," +
                    "%s INTEGER,%s REAL,%s REAL,%s REAL,%s TEXT)", BILL_TABLE, KEY_ID, KEY_DATE, KEY_BILL,
            KEY_TIP_PERCENT, KEY_PEOPLE, KEY_TIP_AMOUNT, KEY_BILl_AMOUNT, KEY_AMOUNT_PER_PERSON, KEY_BILL_NAME);
    private static final String create_table1 = "CREATE TABLE " + BILL_TABLE + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DATE + " TEXT," + KEY_BILL + " REAL," + KEY_TIP_PERCENT + " INTEGER," + KEY_PEOPLE + " INTEGER," +
            KEY_TIP_AMOUNT + " REAL," + KEY_BILl_AMOUNT + " REAL," + KEY_AMOUNT_PER_PERSON + " REAL," + KEY_BILL_NAME + " TEXT)";

    public TipDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, create_table);
        Log.v(TAG, create_table1);
        try {
            db.execSQL(create_table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + BILL_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "DROP TABLE IF EXISTS " + BILL_TABLE);
        onCreate(db);
    }

    public long addBillEntry(BillDetails billDetails){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(KEY_DATE,billDetails.getDate());
        cv.put(KEY_BILL,billDetails.getBill());
        cv.put(KEY_TIP_PERCENT,billDetails.getTipPercent());
        cv.put(KEY_PEOPLE,billDetails.getPeople());
        cv.put(KEY_TIP_AMOUNT,billDetails.getTipAmount());
        cv.put(KEY_BILl_AMOUNT,billDetails.getBillAmount());
        cv.put(KEY_AMOUNT_PER_PERSON,billDetails.getAmtPerPerson());
        cv.put(KEY_BILL_NAME, billDetails.getBillName());
        long rowid=db.insertWithOnConflict(BILL_TABLE,null,cv, CONFLICT_IGNORE);
        Toast.makeText(mContext,"Bill saved "+cv.toString()+" : "+rowid,Toast.LENGTH_LONG).show();
        db.close();
        return rowid;
    }
}
