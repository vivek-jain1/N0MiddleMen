package com.example.vivek.n0middlemen;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.vivek.n0middlemen.R.id.image;

/**
 * Created by vivek on 15/10/17.
 */

public class CustomAdaptor extends ArrayAdapter<TwoStrings> {

    private Activity context;
    List<TwoStrings> list;

    private final Integer[] imageId;

    public CustomAdaptor(Activity context, List<TwoStrings> list,Integer[] imageId){
        super(context,R.layout.crops_list_layout,list);
        this.context = context;
        this.list = list;
        this.imageId = imageId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.crops_list_layout,null,true);

        TwoStrings crops = list.get(position);
        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.crop_image);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.crop_price);
        textViewPrice.setText(crops.getPrice() + " Rs/kg");
        int val = 0;

        if(crops.getName().contentEquals("Maize")){
            val=0;
        }
        if(crops.getName().contentEquals("Rice")){
            val=1;
        }
        if(crops.getName().contentEquals("Sugarcane")){
            val=2;
        }
        if(crops.getName().contentEquals("Tobacco")){
            val=3;
        }
        if(crops.getName().contentEquals("Wheat")){
            val=4;
        }
        imageView.setImageResource(imageId[val]);
        return listViewItem;
    }
}
