package com.example.dell.splitthebill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.splitthebill.model.BillDetails;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

/**
 * Created by dell on 10/26/2015.
 */
public class TipDbHelper extends SQLiteOpenHelper {
    private static final String TAG = TipDbHelper.class.getSimpleName();
    private static final String DB_NAME = "tip_db";
    private static final String BILL_TABLE = "tip_table";
    private static final int DB_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_BILL = "bill";
    public static final String KEY_TIP_PERCENT = "tip_percent";
    public static final String KEY_PEOPLE = "people";
    public static final String KEY_TIP_AMOUNT = "tip_amount";
    public static final String KEY_BILL_AMOUNT = "bill_amount";
    public static final String KEY_AMOUNT_PER_PERSON = "amount_per_person";
    public static final String KEY_BILL_NAME = "bill_name";
    Context mContext;

    private static final String create_table = String.format(
            "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s REAL,%s INTEGER," +
                    "%s INTEGER,%s REAL,%s REAL,%s REAL,%s TEXT)", BILL_TABLE, KEY_ID, KEY_DATE, KEY_BILL,
            KEY_TIP_PERCENT, KEY_PEOPLE, KEY_TIP_AMOUNT, KEY_BILL_AMOUNT, KEY_AMOUNT_PER_PERSON, KEY_BILL_NAME);
    private static final String create_table1 = "CREATE TABLE " + BILL_TABLE + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DATE + " TEXT," + KEY_BILL + " REAL," + KEY_TIP_PERCENT + " INTEGER," + KEY_PEOPLE + " INTEGER," +
            KEY_TIP_AMOUNT + " REAL," + KEY_BILL_AMOUNT + " REAL," + KEY_AMOUNT_PER_PERSON + " REAL," + KEY_BILL_NAME + " TEXT)";

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

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1, Cursor2);
            return alc;
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
        cv.put(KEY_BILL_AMOUNT,billDetails.getBillAmount());
        cv.put(KEY_AMOUNT_PER_PERSON,billDetails.getAmtPerPerson());
        cv.put(KEY_BILL_NAME, billDetails.getBillName());
        long rowid=db.insertWithOnConflict(BILL_TABLE,null,cv, CONFLICT_IGNORE);
        Toast.makeText(mContext,"Bill saved "+cv.toString()+" : "+rowid,Toast.LENGTH_LONG).show();
        db.close();
        return rowid;
    }

    public int getBillsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_BILL;
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        List<BillDetails> allBills = getAllBills();

        return allBills.size();
    }

    public Cursor getBills(){
        String selectQuery = "SELECT  * FROM " + BILL_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public ArrayList<BillDetails> getAllBills() {
        ArrayList<BillDetails> billList = new ArrayList<BillDetails>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + BILL_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BillDetails bill = new BillDetails();
                bill.setId(cursor.getColumnIndex(KEY_ID));
                bill.setDate(String.valueOf(cursor.getColumnIndex(KEY_DATE)));
                bill.setBill(cursor.getColumnIndex(KEY_BILL));
                bill.setTipPercent(cursor.getColumnIndex(KEY_TIP_PERCENT));
                bill.setPeople(cursor.getColumnIndex(KEY_PEOPLE));
                bill.setTipAmount(cursor.getColumnIndex(KEY_TIP_AMOUNT));
                bill.setBillAmount(cursor.getColumnIndex(KEY_BILL_AMOUNT));
                bill.setAmtPerPerson(cursor.getColumnIndex(KEY_AMOUNT_PER_PERSON));
                bill.setBillName(String.valueOf(cursor.getColumnIndex(KEY_BILL_NAME)));
                // Adding contact to list
                billList.add(bill);
                Log.v(TAG,billList.toString());
            } while (cursor.moveToNext());
        }

        // return contact list
        return billList;
    }

    public void deleteContact(BillDetails bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BILL_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(bill.getId())});
        db.close();
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.beginTransaction();
            int del = db.delete(BILL_TABLE, KEY_ID + " = ?",
                    new String[] { String.valueOf(id) });
            db.setTransactionSuccessful();
            db.endTransaction();
            //Toast.makeText(ctx, ""+del,Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            //Toast.makeText(ctx, "Exception :"+ ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

}

