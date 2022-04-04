package com.journey.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.journey.R;
import com.journey.entity.Record;

import java.util.ArrayList;

public class RecordAdapter extends ArrayAdapter<Record> {

    public RecordAdapter(Context context, ArrayList<Record> records){
        super(context,0,records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Record record = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_item,parent,false);
        }
        TextView departure = (TextView) convertView.findViewById(R.id.departure);
        TextView arrival = (TextView) convertView.findViewById(R.id.arrival);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        Button btn = (Button) convertView.findViewById(R.id.detailBtn);
        departure.setText(record.getDeparture());
        arrival.setText(record.getArrival());
        date.setText(record.getCreateDateString());
        btn.setTag(record.getDocId());

        return convertView;
    }

}
