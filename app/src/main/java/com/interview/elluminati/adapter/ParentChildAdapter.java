package com.interview.elluminati.adapter;

import static com.interview.elluminati.activity.SelectSpecificationActivity.descData;
import static com.interview.elluminati.activity.SelectSpecificationActivity.priceData;
import static com.interview.elluminati.activity.SelectSpecificationActivity.tvAddCart;
import static com.interview.elluminati.activity.SelectSpecificationActivity.tvQty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.interview.elluminati.R;
import com.interview.elluminati.activity.SelectSpecificationActivity;
import com.interview.elluminati.listners.ItemClickListner;
import com.interview.elluminati.model.ItemModel;
import com.interview.elluminati.model.ParentAssociateModel;
import com.interview.elluminati.utils.Utils;

import java.util.List;

public class ParentChildAdapter extends RecyclerView.Adapter<ParentChildAdapter.MyViewHolder> {

    Context context;
    List<ItemModel> itemModelList;
    ItemClickListner itemClickListner;
    String parentAssociate;


    public ParentChildAdapter(Context context, List<ItemModel> itemModelList, String parentAssociate) {
        this.context = context;
        this.itemModelList = itemModelList;
        this.parentAssociate = parentAssociate;
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public ParentChildAdapter(Context context, List<ItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    public ParentChildAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_sublist, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ItemModel model = itemModelList.get(position);

        holder.checkb.setChecked(model.isSelected);

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
        ItemModel va  =  itemModelList.get(pos);
        for (int i = 0; i < itemModelList.size(); i++) {
            itemModelList.get(i).isSelected = false;
        }
        va.isSelected = !va.isSelected;
        notifyDataSetChanged();
        itemClickListner.clicked(pos);
        priceData.clear();
        descData.clear();
        tvQty.setText("1");
        priceData.put(parentAssociate, Float.valueOf(va.getPrice()));
        descData.put(parentAssociate, va.getName());
        tvAddCart.setText("Add to Cart- ₹"+String.valueOf(Utils.getSumOfMap(priceData)));
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatCheckBox checkb;
        AppCompatTextView tvPrice;
        AppCompatTextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkb = itemView.findViewById(R.id.checkb);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
