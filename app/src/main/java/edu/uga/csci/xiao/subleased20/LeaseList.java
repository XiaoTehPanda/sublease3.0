package edu.uga.csci.xiao.subleased20;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class LeaseList extends ArrayAdapter<Sublease> {
    private Activity context;
    private List<Sublease> subleaseList;

    public LeaseList(Activity context, List<Sublease> subleaseList) {
        super(context,R.layout.list_layout, subleaseList);
        this.context = context;
        this.subleaseList = subleaseList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewAddress = (TextView) listViewItem.findViewById(R.id.leaseAddressText);
        TextView textViewPrice   = (TextView) listViewItem.findViewById(R.id.leasePriceText);
        Sublease sublease = subleaseList.get(position);

        textViewAddress.setText(sublease.getAddress());
        textViewPrice.setText("$" + sublease.getPrice() + " - " + sublease.getDuration());

        return listViewItem;
    }
}
