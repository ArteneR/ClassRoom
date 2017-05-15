package com.xdot.classroom.list_views.schedules_activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.current_schedule.CurrentScheduleActivity;
import java.util.ArrayList;



public class SchedulesRecyclerViewAdapter extends RecyclerView.Adapter<SchedulesRecyclerViewAdapter.DataObjectHolder> {
        private static String LOG_TAG = "SchedulesRecyclerViewAdapter";
        private ArrayList<SchedulesListData> schedulesDataset;
        private static MyClickListener myClickListener;


        public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView tvScheduleName;
                private final Context mContext;


                public DataObjectHolder(View itemView) {
                        super(itemView);
                        mContext =itemView.getContext();
                        tvScheduleName = (TextView) itemView.findViewById(R.id.tvScheduleName);
                        Log.d(LOG_TAG, "Adding Listener...");
                        itemView.setOnClickListener(this);
                }


                @Override
                public void onClick(View v) {
                        myClickListener.onItemClick(getAdapterPosition(), v);
                        Log.d(LOG_TAG, "Clicked: " + getAdapterPosition());
                        openScheduleActivity(getAdapterPosition());
                }


                private void openScheduleActivity(int selectedScheduleIndex) {
                        Intent intent = new Intent(mContext, CurrentScheduleActivity.class);
                        intent.putExtra("SELECTED_SCHEDULE_INDEX", selectedScheduleIndex);
                        mContext.startActivity(intent);
                }
        }


        public void setOnItemClickListener(MyClickListener myClickListener) {
                this.myClickListener = myClickListener;
        }


        public SchedulesRecyclerViewAdapter(ArrayList<SchedulesListData> schedulesDataset) {
                this.schedulesDataset = schedulesDataset;
        }


        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row_schedules_list, parent, false);

                DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
                return dataObjectHolder;
        }


        @Override
        public void onBindViewHolder(DataObjectHolder holder, int position) {
                holder.tvScheduleName.setText(schedulesDataset.get(position).getScheduleName());
        }


        public void addItem(SchedulesListData dataObj, int index) {
                schedulesDataset.add(index, dataObj);
                notifyItemInserted(index);
        }

        public void deleteItem(int index) {
                schedulesDataset.remove(index);
                notifyItemRemoved(index);
        }


        @Override
        public int getItemCount() {
                return schedulesDataset.size();
        }


        public interface MyClickListener {
                public void onItemClick(int position, View v);
        }
}