package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;


public class EarthArrayAdapter extends ArrayAdapter<Earthquake> {
    public EarthArrayAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context, 0, earthquakes);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Earthquake earthquake = getItem(position);
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView magTextView = (TextView)listItemView.findViewById(R.id.magnitude);
        magTextView.setText(String.valueOf(earthquake.getmMagnitude()));

        TextView locationTextView = (TextView)listItemView.findViewById(R.id.place);
        locationTextView.setText(earthquake.getmPlace());

        TextView timeTextView = (TextView)listItemView.findViewById(R.id.time);
        timeTextView.setText(String.valueOf(earthquake.getmTime()));


        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri earthQuakeuri = Uri.parse(earthquake.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthQuakeuri);
                if (websiteIntent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(websiteIntent);
            }}
        });

        return listItemView;
    }

}
