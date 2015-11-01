package com.example.dell.splitthebill.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.splitthebill.R;
import com.example.dell.splitthebill.database.AndroidDatabaseManager;
import com.example.dell.splitthebill.database.TipDbHelper;
import com.example.dell.splitthebill.model.BillDetails;
import com.example.dell.splitthebill.model.Participants;
import com.example.dell.splitthebill.preference.UserSettings;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("ALL")
public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvTipAmt, tvFinalTip, tvFinalBill, tvBill, tvPerPerson, tvFinalPP, tvTip, tvPpl;
    private EditText etBill, etPerson1, etPerson2, etPerson3, etPerson4, etPerson5, etPerson6;
    private SeekBar sbTip, sbPeople;

    private static final String DEFAULT_NAME = "";
    private static final int DEFAULT_TIP_PERCENT = 0;
    private static final int DEFAULT_PEOPLE = 1;
    private static final double DEFAULT_DOUBLE = 0;

    Date date = new Date();

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private int tipPrecent, people;
    private double billAmount, tipPercent, totalBillAmount, perPersonBillAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initSeekBar();
    }


    private void initSeekBar() {
        sbTip = (SeekBar) findViewById(R.id.sbtip);
        sbPeople = (SeekBar) findViewById(R.id.sbPeople);
        sbTip.setOnSeekBarChangeListener(this);
        sbPeople.setOnSeekBarChangeListener(this);
    }

    private void updateTextViews() {
        tvTipAmt.setVisibility(View.VISIBLE);
        tvBill.setVisibility(View.VISIBLE);
        tvPerPerson.setVisibility(View.VISIBLE);
        tvFinalTip.setVisibility(View.VISIBLE);
        tvFinalBill.setVisibility(View.VISIBLE);
        tvFinalPP.setVisibility(View.VISIBLE);
        tvFinalTip.setText("" + tipPercent);
        tvFinalBill.setText("" + totalBillAmount);
        tvFinalPP.setText("" + perPersonBillAmount);
    }

    private void initViews() {
        etBill = (EditText) findViewById(R.id.etBill);

        tvTipAmt = (TextView) findViewById(R.id.tvTipAmt);
        tvBill = (TextView) findViewById(R.id.tvBill);
        tvPerPerson = (TextView) findViewById(R.id.tvPerPerson);
        tvFinalTip = (TextView) findViewById(R.id.tvFinalTip);
        tvFinalBill = (TextView) findViewById(R.id.tvFinalBill);
        tvFinalPP = (TextView) findViewById(R.id.tvFinalPP);

        tvTip = (TextView) findViewById(R.id.tvTip);
        tvPpl = (TextView) findViewById(R.id.tvPpl);

        etBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    billAmount = Double.parseDouble(s.toString());
                    updateCalculation();
                } else {
                    billAmount = 0;
                    clearTextViews();
                    clearSeekbar();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void clearSeekbar() {
        sbTip.setProgress(0);
        sbPeople.setProgress(0);

    }

    private void clearTextViews() {
        tvTipAmt.setVisibility(View.GONE);
        tvBill.setVisibility(View.GONE);
        tvPerPerson.setVisibility(View.GONE);
        tvFinalTip.setVisibility(View.GONE);
        tvFinalBill.setVisibility(View.GONE);
        tvFinalPP.setVisibility(View.GONE);
    }

    private void updateCalculation() {
        setdefaultParameters();
        setDefaultResults();
        calculateBill();
        if (billAmount > 0)
            updateTextViews();
    }

    private void setDefaultResults() {
        double defaulTipAmount, defaultFinalBill, defaultPerPersonBill;
        defaulTipAmount = defaultFinalBill = defaultPerPersonBill = DEFAULT_DOUBLE;
        if (tipPercent == 0) {
            tipPercent = defaulTipAmount;
        }
        if (totalBillAmount == 0) {
            totalBillAmount = defaultFinalBill;
        }
        if (perPersonBillAmount == 0) {
            perPersonBillAmount = defaultPerPersonBill;
        }
    }

    private void calculateBill() {
        DecimalFormat df = new DecimalFormat("#.##");
        tipPercent = billAmount * tipPrecent / 100.00;
        tipPercent = Double.parseDouble(df.format(tipPercent));
        totalBillAmount = billAmount + tipPercent;
        totalBillAmount = Double.parseDouble(df.format(totalBillAmount));
        perPersonBillAmount = totalBillAmount / people;
        perPersonBillAmount = Double.parseDouble(df.format(Math.ceil(perPersonBillAmount)));
    }

    private void setdefaultParameters() {

        if (billAmount == 0 || etBill.getText().toString().matches(""))
            billAmount = DEFAULT_DOUBLE;
        /*else
            bills = Double.parseDouble(etBill.getText().toString());
*/
        if (tipPrecent == 0)
            tipPrecent = DEFAULT_TIP_PERCENT;

        if (people == 0 || people == 1) {
            int defaultPeople;
            defaultPeople = 1;
            people = DEFAULT_PEOPLE;
        }
    }

    private void saveToDb() {
        if (billAmount > 0) {
            TipDbHelper mTipDbHelper = new TipDbHelper(this);
            BillDetails mBillDetails = new BillDetails();
            mBillDetails.setDate(dateFormat.format(date));
            mBillDetails.setBill(billAmount);
            mBillDetails.setTipPercent(tipPrecent);
            mBillDetails.setPeople(people);
            mBillDetails.setTipAmount(tipPercent);
            mBillDetails.setBillAmount(totalBillAmount);
            mBillDetails.setAmtPerPerson(perPersonBillAmount);
            long rowid = mTipDbHelper.addBillEntry(mBillDetails);

            Participants participants = new Participants();
            String name1, name2, name3, name4, name5, name6;
            name1 = name2 = name3 = name4 = name5 = name6 = DEFAULT_NAME;
            if (!etPerson1.getText().toString().matches(""))
                name1 = etPerson1.getText().toString();
            if (!etPerson2.getText().toString().matches(""))
                name2 = etPerson2.getText().toString();
            if (!etPerson3.getText().toString().matches(""))
                name3 = etPerson3.getText().toString();
            if (!etPerson4.getText().toString().matches(""))
                name4 = etPerson4.getText().toString();
            if (!etPerson5.getText().toString().matches(""))
                name5 = etPerson5.getText().toString();
            if (!etPerson6.getText().toString().matches(""))
                name6 = etPerson6.getText().toString();
            participants.setP_id((int) rowid);
            participants.setPartictipants1(name1);
            participants.setPartictipants2(name2);
            participants.setPartictipants3(name3);
            participants.setPartictipants4(name4);
            participants.setPartictipants5(name5);
            participants.setPartictipants6(name6);

            //mBillDetails.setParticipants(participants);
            //if (participants != null) {
            mTipDbHelper.addParticipants(participants);
            //}
            setEditTextBlank();

        } else {
            Toast.makeText(this, "Enter bills value", Toast.LENGTH_SHORT).show();
        }
        clearSeekbar();

    }

    private void setEditTextBlank() {
        etBill.setText("");
        etPerson1.setText("");
        etPerson2.setText("");
        etPerson3.setText("");
        etPerson4.setText("");
        etPerson5.setText("");
        etPerson6.setText("");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbTip) {
            tipPrecent = progress;
            tvTip.setText("Tip " + String.valueOf(tipPrecent) + "%");
        } else if (seekBar == sbPeople) {
            people = progress;
            tvPpl.setText("Split among " + String.valueOf(people));
        }
        initPeopleEditText(people);
        updateCalculation();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void initPeopleEditText(int people) {
        Log.v(TAG, "people: " + people);
        etPerson1 = (EditText) findViewById(R.id.etPerson1);
        etPerson2 = (EditText) findViewById(R.id.etPerson2);
        etPerson3 = (EditText) findViewById(R.id.etPerson3);
        etPerson4 = (EditText) findViewById(R.id.etPerson4);
        etPerson5 = (EditText) findViewById(R.id.etPerson5);
        etPerson6 = (EditText) findViewById(R.id.etPerson6);
        makeEditTextVisible(people);
    }

    private void makeEditTextVisible(int people) {
        switch (people) {
            case 0: {
                etPerson1.setVisibility(View.GONE);
                etPerson2.setVisibility(View.GONE);
                etPerson3.setVisibility(View.GONE);
                etPerson4.setVisibility(View.GONE);
                etPerson5.setVisibility(View.GONE);
                etPerson6.setVisibility(View.GONE);
                break;
            }
            case 1: {
                etPerson1.setVisibility(View.VISIBLE);
                etPerson2.setVisibility(View.GONE);
                etPerson3.setVisibility(View.GONE);
                etPerson4.setVisibility(View.GONE);
                etPerson5.setVisibility(View.GONE);
                etPerson6.setVisibility(View.GONE);
                break;
            }
            case 2: {
                etPerson1.setVisibility(View.VISIBLE);
                etPerson2.setVisibility(View.VISIBLE);
                etPerson3.setVisibility(View.GONE);
                etPerson4.setVisibility(View.GONE);
                etPerson5.setVisibility(View.GONE);
                etPerson6.setVisibility(View.GONE);
                break;
            }
            case 3: {
                etPerson1.setVisibility(View.VISIBLE);
                etPerson2.setVisibility(View.VISIBLE);
                etPerson3.setVisibility(View.VISIBLE);
                etPerson4.setVisibility(View.GONE);
                etPerson5.setVisibility(View.GONE);
                etPerson6.setVisibility(View.GONE);
                break;
            }
            case 4: {
                etPerson1.setVisibility(View.VISIBLE);
                etPerson2.setVisibility(View.VISIBLE);
                etPerson3.setVisibility(View.VISIBLE);
                etPerson4.setVisibility(View.VISIBLE);
                etPerson5.setVisibility(View.GONE);
                etPerson6.setVisibility(View.GONE);
                break;
            }
            case 5: {
                etPerson1.setVisibility(View.VISIBLE);
                etPerson2.setVisibility(View.VISIBLE);
                etPerson3.setVisibility(View.VISIBLE);
                etPerson4.setVisibility(View.VISIBLE);
                etPerson5.setVisibility(View.VISIBLE);
                etPerson6.setVisibility(View.GONE);
                break;
            }
            case 6: {
                etPerson1.setVisibility(View.VISIBLE);
                etPerson2.setVisibility(View.VISIBLE);
                etPerson3.setVisibility(View.VISIBLE);
                etPerson4.setVisibility(View.VISIBLE);
                etPerson5.setVisibility(View.VISIBLE);
                etPerson6.setVisibility(View.VISIBLE);
                break;
            }
        }
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
        switch (id) {
            case R.id.action_settings: {
                actionIntent = new Intent(this, UserSettings.class);
                startActivity(actionIntent);
                break;
            }
            case R.id.action_view_database: {
                actionIntent = new Intent(this,
                        AndroidDatabaseManager.class);
                startActivity(actionIntent);
                break;
            }
            case R.id.save: {
                saveToDb();
                break;
            }
            case R.id.archieve: {
                actionIntent = new Intent(this, RecordActivity.class);
                startActivity(actionIntent);
                break;
            }
            case R.id.about: {
                opnAboutDialpg();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void opnAboutDialpg() {
        final Dialog aboutDialog = new Dialog(this);
        aboutDialog.setContentView(R.layout.about);
        aboutDialog.setTitle("About the App");
        String aboutApp = "Version 1.0.1" + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "What all this app can do :" + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + "1. Calculate tip" + System.getProperty("line.separator")
                + "2. Split bills" + System.getProperty("line.separator")
                + "3. Save your bills - click the save icon" + System.getProperty("line.separator")
                + "4. View detailed summary of bill -click on Folder icon " + System.getProperty("line.separator")
                + "5. Save time - set default tip, round off and split in Settings" + System.getProperty("line.separator")
                + "6. Save the Person name" + System.getProperty("line.separator")
                + "7. Single tap archive item to view each person name if saved" + System.getProperty("line.separator")
                + "9. Long press to delete history item" + System.getProperty("line.separator")
                + "" + System.getProperty("line.separator")
                + System.getProperty("line.separator");
        TextView tvABout = (TextView) aboutDialog.findViewById(R.id.tvAbout);
        tvABout.setGravity(Gravity.CENTER);
        tvABout.setText(aboutApp);

        aboutDialog.show();


    }

}
