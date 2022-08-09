package com.kakaologin_sample;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UseagerecordAdapter extends RecyclerView.Adapter<UseagerecordAdapter.CustomViewHolder> {

        private ArrayList<UseageRecord> arrayList = new ArrayList<>();

        //생성자 선언
        public UseagerecordAdapter(ArrayList<UseageRecord> arrayList ) {
            this.arrayList = arrayList;
        }
        public UseagerecordAdapter() {

        }
        @NonNull
        @Override
        //처음으로 생성될 때
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
            CustomViewHolder holder = new CustomViewHolder(view);
            return holder;
        }


        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

            holder.date = (TextView)holder.itemView.findViewById(R.id.date);
            holder.location= (TextView)holder.itemView.findViewById(R.id.location);
            holder.machine_type = (TextView)holder.itemView.findViewById(R.id.machine_type);
            holder.operation_time = (TextView)holder.itemView.findViewById(R.id.operation_time);

            String dates = arrayList.get(position).getDate();
            StringBuilder datesb = new StringBuilder(dates);
            datesb = datesb.deleteCharAt(10).insert(10, " ");
            datesb = datesb.deleteCharAt(13).insert(13, ":");
            datesb = datesb.deleteCharAt(16).insert(16, ":");
//            holder.date.setText(arrayList.get(position).getDate());
            holder.date.setText(datesb.toString());

            holder.location.setText(arrayList.get(position).getLocation());

            switch (arrayList.get(position).getMachine_type()){
                case "big_washer": holder.machine_type.setText("대형 세탁기"); break;
                case "washer": holder.machine_type.setText("중형 세탁기"); break;
                case "big_dryer": holder.machine_type.setText("대형 건조기"); break;
                case "dryer": holder.machine_type.setText("중형 건조기"); break;
                default : holder.machine_type.setText("기타 기기"); break;
            }

            holder.operation_time.setText(arrayList.get(position).getOperation_time() + "분");

        }

        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView date;
            protected TextView location;
            protected TextView machine_type ;
            protected TextView operation_time ;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                this.date = (TextView)itemView.findViewById(R.id.date);
                this.location = (TextView)itemView.findViewById(R.id.location);
                this.machine_type = (TextView)itemView.findViewById(R.id.machine_type);
                this.operation_time = (TextView)itemView.findViewById(R.id.operation_time);
            }
        }
    }
