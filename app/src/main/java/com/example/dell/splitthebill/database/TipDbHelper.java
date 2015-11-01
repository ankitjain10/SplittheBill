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
import com.example.dell.splitthebill.model.Participants;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

/**
 * Created by dell on 10/26/2015.
 */
public class TipDbHelper extends SQLiteOpenHelper {
    private static final String TAG = TipDbHelper.class.getSimpleName();
    private static final String DB_NAME = "tip_db";
    private static final String BILL_TABLE = "tip_table";
    private static final String PARTICIPANTS_TABLE = "Participants_table";
    private static final int DB_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_BILL = "bill";
    public static final String KEY_TIP_PERCENT = "tip_percent";
    public static final String KEY_PEOPLE = "people";
    public static final String KEY_TIP_AMOUNT = "tip_amount";
    public static final String KEY_BILL_AMOUNT = "bill_amount";
    public static final String KEY_AMOUNT_PER_PERSON = "amount_per_person";

    public static final String KEY_REFERENCE_ID = "reference_id";
    public static final String KEY_PERSON_1 = "p_1";
    public static final String KEY_PERSON_2 = "p_2";
    public static final String KEY_PERSON_3 = "p_3";
    public static final String KEY_PERSON_4 = "p_4";
    public static final String KEY_PERSON_5 = "p_5";
    public static final String KEY_PERSON_6 = "p_6";
    Context mContext;

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }

    private static final String create_bill_table = String.format(
            "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s REAL,%s INTEGER," +
                    "%s INTEGER,%s REAL,%s REAL,%s REAL)", BILL_TABLE, KEY_ID, KEY_DATE, KEY_BILL,
            KEY_TIP_PERCENT, KEY_PEOPLE, KEY_TIP_AMOUNT, KEY_BILL_AMOUNT, KEY_AMOUNT_PER_PERSON);


    private static final String create_person_table = String.format(
            "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s TEXT," +
                    "%s TEXT,%s TEXT,%s TEXT,FOREIGN KEY (%s) REFERENCES %s (%s))",
            PARTICIPANTS_TABLE,
            KEY_REFERENCE_ID,
            KEY_PERSON_1,
            KEY_PERSON_2,
            KEY_PERSON_3,
            KEY_PERSON_4,
            KEY_PERSON_5,
            KEY_PERSON_6,
            KEY_REFERENCE_ID,
            BILL_TABLE,
            KEY_ID
    );

    public TipDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, create_bill_table);
        Log.v(TAG, create_person_table);
        try {
            db.execSQL(create_bill_table);
            db.execSQL(create_person_table);
            Log.v(TAG, "Table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + BILL_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PARTICIPANTS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "DROP TABLE IF EXISTS " + BILL_TABLE);
        onCreate(db);
    }

    public long addParticipants(Participants participants) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_REFERENCE_ID, participants.getP_id());
        cv.put(KEY_PERSON_1, participants.getPartictipants1());
        cv.put(KEY_PERSON_2, participants.getPartictipants2());
        cv.put(KEY_PERSON_3, participants.getPartictipants3());
        cv.put(KEY_PERSON_4, participants.getPartictipants4());
        cv.put(KEY_PERSON_5, participants.getPartictipants5());
        cv.put(KEY_PERSON_6, participants.getPartictipants6());
        long id = db.insertWithOnConflict(PARTICIPANTS_TABLE, null, cv, CONFLICT_IGNORE);
        db.close();
        return id;
    }

    public long addBillEntry(BillDetails billDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE, billDetails.getDate());
        cv.put(KEY_BILL, billDetails.getBill());
        cv.put(KEY_TIP_PERCENT, billDetails.getTipPercent());
        cv.put(KEY_PEOPLE, billDetails.getPeople());
        cv.put(KEY_TIP_AMOUNT, billDetails.getTipAmount());
        cv.put(KEY_BILL_AMOUNT, billDetails.getBillAmount());
        cv.put(KEY_AMOUNT_PER_PERSON, billDetails.getAmtPerPerson());
        long rowid = db.insertWithOnConflict(BILL_TABLE, null, cv, CONFLICT_IGNORE);
        //Toast.makeText(mContext,"Bill saved "+cv.toString()+" : "+rowid,Toast.LENGTH_LONG).show();
        db.close();
        return rowid;
    }

    public Cursor getBills() {
        String selectQuery = "SELECT  * FROM " + BILL_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getParticipants() {
        String selectQuery = "SELECT  * FROM " + PARTICIPANTS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getEachParticipants(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + PARTICIPANTS_TABLE + " where " + KEY_REFERENCE_ID + " = " + id;
        return db.rawQuery(selectQuery, null);
    }

    public void deleteRecord(BillDetails bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BILL_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(bill.getId())});
        Toast.makeText(mContext,"Record Deleted "+bill.toString(), Toast.LENGTH_LONG).show();
        db.close();
    }


    public void deleteParticipatnts(Participants participantToBeDeleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PARTICIPANTS_TABLE, KEY_REFERENCE_ID + " = ?",
                new String[]{String.valueOf(participantToBeDeleted.getP_id())});
        Toast.makeText(mContext,"Participants Deleted "+participantToBeDeleted.toString(),Toast.LENGTH_LONG).show();
        db.close();
    }
}

