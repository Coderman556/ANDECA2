package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FinanceTransactionAdapter extends RecyclerView.Adapter<FinanceTransactionAdapter.MyViewHolder>{

    private ArrayList<FinanceTransaction> dataList;
    private MyRecyclerViewItemClickListener mItemClickListener;

    // Constructor
    public FinanceTransactionAdapter(ArrayList<FinanceTransaction> dataList, MyRecyclerViewItemClickListener itemClickListener) {
        this.dataList = dataList;
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(itemView);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(dataList.get(myViewHolder.getLayoutPosition()));
        }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FinanceTransaction data = dataList.get(position);

        holder.category.setText(data.getCategory());

        holder.description.setText(limitString(data.getDescription()));

        holder.amount.setText("$" + String.valueOf(data.getPrice()));
        // Set other views in the holder as needed
    }

    public String limitString(String original) {
        int maxLength = 13;
        if (original == null) {
            return null;
        }
        if (original.length() <= maxLength) {
            return original;
        } else {
            return original.substring(0, maxLength) + "...";
        }
    }

    public void updateData(ArrayList<FinanceTransaction> newDataList) {
        dataList.clear();
        dataList.addAll(newDataList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public TextView description;
        public TextView amount;
        // Other views in the item
        // category, description, amount

        public MyViewHolder(View view) {
            super(view);
            category = view.findViewById(R.id.category);
            description = view.findViewById(R.id.description);
            amount = view.findViewById(R.id.amount);
            // Initialize other views
        }
    }
    public interface MyRecyclerViewItemClickListener
    {
        void onItemClicked(FinanceTransaction transaction);
    }

}
