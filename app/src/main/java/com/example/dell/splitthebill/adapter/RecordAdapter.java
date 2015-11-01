package com.example.dell.splitthebill.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.splitthebill.R;
import com.example.dell.splitthebill.model.BillDetails;

import java.util.ArrayList;

/**
 * Created by dell on 10/28/2015.
 */
public class RecordAdapter extends BaseAdapter {
    ArrayList<BillDetails> recordList;
    Context mContext;
    LayoutInflater mInflater;

    public RecordAdapter(ArrayList<BillDetails> recordList, Context mContext, LayoutInflater mInflater) {
        this.recordList = recordList;
        this.mContext = mContext;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_items, null);
            viewHolder = new ViewHolder();
            viewHolder.tvDates = (TextView) convertView.findViewById(R.id.tvDates);
            viewHolder.tvBills = (TextView) convertView.findViewById(R.id.tvBills);
            viewHolder.tvTips = (TextView) convertView.findViewById(R.id.tvTipPercents);
            viewHolder.tvPeoples = (TextView) convertView.findViewById(R.id.tvPeoples);
            viewHolder.tvTipAmounts = (TextView) convertView.findViewById(R.id.tvTipAmounts);
            viewHolder.tvTotalBills = (TextView) convertView.findViewById(R.id.tvTotalBills);
            viewHolder.tvPerPersons = (TextView) convertView.findViewById(R.id.tvPerPersons);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BillDetails bill = recordList.get(position);
        if (bill != null) {
            viewHolder.tvDates.setText(bill.getDate());
            viewHolder.tvBills.setText("" + bill.getBill());
            viewHolder.tvTips.setText(""+bill.getTipPercent() );
            viewHolder.tvPeoples.setText(""+bill.getPeople());
            viewHolder.tvTipAmounts.setText("" + bill.getTipAmount());
            viewHolder.tvTotalBills.setText("" + bill.getBillAmount());
            viewHolder.tvPerPersons.setText("" + bill.getAmtPerPerson());
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvDates, tvBills, tvTips, tvPeoples, tvTipAmounts, tvTotalBills, tvPerPersons;
    }
}
