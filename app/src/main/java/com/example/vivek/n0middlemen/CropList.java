package com.example.vivek.n0middlemen;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vivek on 11/10/17.
 */

public class CropList extends ArrayAdapter<Crop> {

    private Activity context;

    private List<Crop> cropList;

    public  CropList(Activity context,List<Crop> cropList){

        super(context,R.layout.list_layout,cropList);
        this.context=context;
        this.cropList=cropList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView textViewMobile =  (TextView)  listViewItem.findViewById(R.id.textViewMobile);
        TextView textViewPrice = (TextView)    listViewItem.findViewById(R.id.textViewPrice);

        TextView textViewName =  (TextView) listViewItem.findViewById(R.id.textViewName);

        Crop crop = cropList.get(position);

        textViewMobile.setText(crop.getMobile());
        textViewPrice.setText(crop.getPrice() + " Rs/kg");
        textViewName.setText(crop.getName());
        return listViewItem;
    }
}