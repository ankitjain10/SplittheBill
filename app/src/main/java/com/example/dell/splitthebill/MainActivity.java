package com.example.dell.splitthebill;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.splitthebill.database.TipDbHelper;
import com.example.dell.splitthebill.model.BillDetails;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("ALL")
public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private TipDbHelper mTipDbHelper;
    private SeekBar sbTip, sbPeople;
    double bill, tip, totalBill, perPersonBill;
    double defaultBill = 0;
    double defaulTipAmount = 0;
    double defaultFinalBill = 0;
    double defaultPerPersonBill = 0;
    BillDetails mBillDetails;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private EditText etBill;
    private int tipPrecent, people;
    private TextView tvTipAmt, tvFinalTip, tvFinalBill, tvBill, tvPerPerson, tvFinalPP, tvTip, tvPpl;

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
        tvFinalTip.setText("" + tip);
        tvFinalBill.setText("" + totalBill);
        tvFinalPP.setText("" + perPersonBill);
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
                    bill = Double.parseDouble(s.toString());
                    updateCalculation();
                } else {
                    etBill.setError("Enter bill");
                    bill = 0;
                    clearTextViews();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                clearTextViews();
            }
        });
    }

    private void clearTextViews() {
        tvTipAmt.setVisibility(View.GONE);
        tvBill.setVisibility(View.GONE);
        tvPerPerson.setVisibility(View.GONE);
        tvFinalTip.setVisibility(View.GONE);
        tvFinalBill.setVisibility(View.GONE);
        tvFinalPP.setVisibility(View.GONE);
        sbTip.setProgress(0);
        sbPeople.setProgress(0);
    }

    private void updateCalculation() {
        setdefaultParameters();
        setDefaultResults();
        calculateBill();
        if (bill > 0)
            updateTextViews();
    }

    private void setDefaultResults() {
        if (tip == 0) {
            tip = defaulTipAmount;
        }
        if (totalBill == 0) {
            totalBill = defaultFinalBill;
        }
        if (perPersonBill == 0) {
            perPersonBill = defaultPerPersonBill;
        }
    }

    private void calculateBill() {
        DecimalFormat df = new DecimalFormat("#.##");
        tip = bill * tipPrecent / 100.00;
        tip= Double.parseDouble(df.format(tip));
        totalBill = bill + tip;
        totalBill= Double.parseDouble(df.format(totalBill));
        perPersonBill = totalBill / people;
        perPersonBill= Double.parseDouble(df.format(perPersonBill));
    }

    private void setdefaultParameters() {
        if (bill == 0 || etBill.getText().toString().matches(""))
            bill = defaultBill;
        else
            bill = Double.parseDouble(etBill.getText().toString());

        int defaultTip = 0;
        if (tipPrecent == 0)
            tipPrecent = defaultTip;

        if (people == 0 || people == 1) {
            int defaultPeople;
            defaultPeople = 1;
            people = defaultPeople;
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
        switch (id){
            case R.id.action_settings:{
                actionIntent = new Intent(this, UserSettings.class);
                startActivity(actionIntent);
                break;
            }
            case R.id.action_view_database:{
                actionIntent = new Intent(this,
                        AndroidDatabaseManager.class);
                startActivity(actionIntent);

                break;
            }
            case R.id.save:{
                saveToDb();
                break;
            }
            case R.id.archieve:{
                actionIntent = new Intent(this, RecordActivity.class);
                startActivity(actionIntent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDb() {
        if (bill > 0) {
            mBillDetails = new BillDetails();
            mBillDetails.setDate(dateFormat.format(date));
            mBillDetails.setBill(bill);
            mBillDetails.setTipPercent(tipPrecent);
            mBillDetails.setPeople(people);
            mBillDetails.setTipAmount(tip);
            mBillDetails.setBillAmount(totalBill);
            mBillDetails.setAmtPerPerson(perPersonBill);
            mTipDbHelper = new TipDbHelper(this);
            mTipDbHelper.addBillEntry(mBillDetails);
        } else {
            Toast.makeText(this,"Enter bill value",Toast.LENGTH_SHORT).show();
        }
        etBill.setText("");
        sbTip.setProgress(0);
        sbPeople.setProgress(0);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbTip) {
            tipPrecent = progress;
            tvTip.setText("Tip " + String.valueOf(tipPrecent) + " %");
        } else if (seekBar == sbPeople) {
            people = progress;
            tvPpl.setText("Split among " + String.valueOf(people));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        updateCalculation();
    }
}
