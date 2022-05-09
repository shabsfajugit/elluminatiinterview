package com.interview.elluminati.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.interview.elluminati.R;
import com.interview.elluminati.activity.SelectSpecificationActivity;
import com.interview.elluminati.listners.ItemClickListner;
import com.interview.elluminati.model.ParentAssociateModel;

import java.util.List;

public class ParentAssociateAdapter extends RecyclerView.Adapter<ParentAssociateAdapter.MyViewHolder> {

    Context context;
    List<ParentAssociateModel> parentAssociateModelList;

    public ParentAssociateAdapter(Context context, List<ParentAssociateModel> parentAssociateModelList) {
        this.context = context;
        this.parentAssociateModelList = parentAssociateModelList;
    }

    public ParentAssociateAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_associate, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ParentAssociateModel pmodel = parentAssociateModelList.get(position);

        holder.tvTile.setText(pmodel.getName());
        holder.tvSubTitle.setText("Choose 1");

        holder.rvitem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ParentChildAdapter parentChildAdapter = new ParentChildAdapter(context, pmodel.getItemModelList(),pmodel.getName());
        parentChildAdapter.setItemClickListner(new ItemClickListner() {
            @Override
            public void clicked(int pos) {
                ((SelectSpecificationActivity)context).setChildAssociated(pmodel.getItemModelList().get(pos).getName());
            }
        });
        holder.rvitem.setAdapter(parentChildAdapter);


    }

    @Override
    public int getItemCount() {
        return parentAssociateModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvTile;
        AppCompatTextView tvSubTitle;
        RecyclerView rvitem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTile = itemView.findViewById(R.id.tvTile);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            rvitem = itemView.findViewById(R.id.rvitem);
        }
    }
}
