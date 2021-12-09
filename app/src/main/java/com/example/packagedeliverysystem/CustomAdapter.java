package com.example.packagedeliverysystem;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList;

    public CustomAdapter(Context context, ArrayList<JSONObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.text_row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        JSONObject jsonObject = arrayList.get(position);
        try {
            holder.textViewName.setText(jsonObject.getString("status")+" at "+jsonObject.getString("transit"));
            switch (jsonObject.getInt("severity")) {
                case 1:
                    holder.imageViewName.setImageResource(R.mipmap.ic_grey_check_foreground);
                    break;
                case 2:
                case 3:
                    holder.imageViewName.setImageResource(R.mipmap.ic_green_check_foreground);
                    break;
                case 5:
                    holder.imageViewName.setImageResource(R.mipmap.ic_warning_yellow_foreground);
                    break;
                default:
                    holder.imageViewName.setImageResource(R.mipmap.ic_red_cross_foreground);
                    break;
            }
            holder.arrivalDate.setText(jsonObject.getString("arrival_date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView arrivalDate;
        ImageView imageViewName;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.listitemname);
            arrivalDate = itemView.findViewById(R.id.arrivalDate);
            imageViewName = itemView.findViewById(R.id.listimage);
            linearLayout = itemView.findViewById(R.id.mylist);

        }
    }
}
