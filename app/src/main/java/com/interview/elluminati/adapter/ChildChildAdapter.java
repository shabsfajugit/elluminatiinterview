package com.interview.elluminati.adapter;

import static com.interview.elluminati.activity.SelectSpecificationActivity.descData;
import static com.interview.elluminati.activity.SelectSpecificationActivity.priceData;
import static com.interview.elluminati.activity.SelectSpecificationActivity.tvAddCart;
import static com.interview.elluminati.activity.SelectSpecificationActivity.tvQty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.interview.elluminati.R;
import com.interview.elluminati.model.ItemModel;
import com.interview.elluminati.utils.Utils;

import java.util.List;

public class ChildChildAdapter extends RecyclerView.Adapter<ChildChildAdapter.MyViewHolder> {

    Context context;
    List<ItemModel> itemModelList;
    String associateName;
    final int[] countqtys = {0};


    public ChildChildAdapter(Context context, List<ItemModel> itemModelList, String associateName) {
        this.context = context;
        this.itemModelList = itemModelList;
        this.associateName = associateName;
    }


    public ChildChildAdapter(Context context, List<ItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    public ChildChildAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_sublist, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ItemModel model = itemModelList.get(position);

        holder.checkb.setChecked(model.isSelected);

        if (model.isSelected){
            holder.ivQty.setText("1");
            countqtys[0]=1;
            holder.llCounter.setVisibility(View.VISIBLE);
        }else {
            holder.llCounter.setVisibility(View.GONE);
        }

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countqtys[0] = Integer.parseInt(holder.ivQty.getText().toString());
                countqtys[0] = countqtys[0] - 1;
                if (countqtys[0] <= 0) {

                } else {
                    holder.ivQty.setText("" + countqtys[0]);
                    priceData.put(associateName, Float.parseFloat(model.getPrice()) * countqtys[0]);
                    float sum = 0.0f;
                    sum = Utils.getSumOfMap(priceData);
                    tvAddCart.setText("Add to Cart- ₹"+sum);
                }
            }
        });


        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countqtys[0] = Integer.parseInt(holder.ivQty.getText().toString());
                countqtys[0] = countqtys[0] + 1;
                holder.ivQty.setText("" + countqtys[0]);
                priceData.put(associateName, Float.parseFloat(model.getPrice()) * countqtys[0]);
                float sum = 0.0f;
                sum = Utils.getSumOfMap(priceData);
                tvAddCart.setText("Add to Cart- ₹"+sum);
            }
        });

        holder.checkb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMultiple(position);
            }
        });

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMultiple(position);
            }
        });

        holder.tvPrice.setText("₹" + model.getPrice());
        holder.tvName.setText(model.getName());

    }

    private void checkMultiple(int pos) {
        ItemModel va = itemModelList.get(pos);
        for (int i = 0; i < itemModelList.size(); i++) {
            itemModelList.get(i).isSelected = false;
        }
        va.isSelected = !va.isSelected;
        notifyDataSetChanged();
        priceData.put(associateName, Float.valueOf(va.getPrice()));
        descData.put(associateName,va.getName());
        float sum = 0.0f;
        sum = Utils.getSumOfMap(priceData) * Integer.parseInt(tvQty.getText().toString());
        tvAddCart.setText("Add to Cart- ₹" + sum);
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatCheckBox checkb;
        AppCompatTextView tvPrice;
        AppCompatTextView tvName;
        AppCompatTextView ivQty;
        LinearLayoutCompat llCounter;
        AppCompatImageView ivMinus;
        AppCompatImageView ivPlus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkb = itemView.findViewById(R.id.checkb);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvName = itemView.findViewById(R.id.tvName);
            llCounter = itemView.findViewById(R.id.llCounter);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivQty = itemView.findViewById(R.id.ivQty);
            ivPlus = itemView.findViewById(R.id.ivPlus);
        }
    }
}
