package com.example.dell.splitthebill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dell.splitthebill.database.TipDbHelper;
import com.example.dell.splitthebill.model.BillDetails;

import java.util.ArrayList;

public class BillAdapter extends ArrayAdapter<BillDetails>{
	
	private TipDbHelper dbHandler;

	public BillAdapter(Context context, ArrayList<BillDetails> bills, TipDbHelper dbHandler) {
		super(context, R.layout.item_bill, bills);
		this.dbHandler = dbHandler;
	}
	
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
		 final BillDetails bill = getItem(position);
		 
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bill, null);
	       }
	       // Lookup view for data population
	       TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
	       TextView tvPp = (TextView) convertView.findViewById(R.id.tvPerPersonList);
	       TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
	       if(bill == null){
	    	   tvDate.setText("     Date   ");
	    	   //tvBill.setText("       Bill");
	    	   //tvTip.setText("    Tip   ");
	    	   tvPp.setText("   You Paid");
	    	   //ibDel.setVisibility(View.GONE);
	       }else{
	           tvName.setText(bill.getBillName());
	    	   tvDate.setText(bill.getDate());
		       tvPp.setText("$"+bill.getAmtPerPerson());
	       }
	       // Return the completed view to render on screen
	       return convertView;
	   }
}


