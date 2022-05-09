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
import com.interview.elluminati.model.ChildAssociateModel;
import com.interview.elluminati.model.ParentAssociateModel;

import java.util.List;

public class ChildAssociateAdapter extends RecyclerView.Adapter<ChildAssociateAdapter.MyViewHolder> {

    Context context;
    List<ChildAssociateModel> parentAssociateModelList;

    public ChildAssociateAdapter(Context context, List<ChildAssociateModel> parentAssociateModelList) {
        this.context = context;
        this.parentAssociateModelList = parentAssociateModelList;
    }

    public ChildAssociateAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_associate,parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ChildAssociateModel pmodel =  parentAssociateModelList.get(position);

       holder.tvTile.setText(pmodel.getName());

       holder.rvitem.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
       holder.rvitem.setAdapter(new ChildChildAdapter(context,pmodel.getItemModelList(),pmodel.getName()));


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
