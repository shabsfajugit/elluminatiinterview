package com.interview.elluminati.activity;

import static com.interview.elluminati.activity.MainActivity.frmBottomCart;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.interview.elluminati.R;
import com.interview.elluminati.database.DatabaseHelper;
import com.interview.elluminati.database.MyCart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RelativeLayout llToolbar;
    private View view;
    private RecyclerView rvCart;
    private AppCompatTextView btnCheckOut;
    private AppCompatImageView ivBack;
    DatabaseHelper databaseHelper;
    List<MyCart> myCarts;
    StaggeredGridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        databaseHelper = new DatabaseHelper(CartActivity.this);
        myCarts = new ArrayList<>();
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        rvCart.setLayoutManager(gridLayoutManager);
        Cursor res = databaseHelper.getAllData();
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setId(res.getString(0));
            rModel.setId(res.getString(1));
            rModel.setName(res.getString(2));
            rModel.setDesc(res.getString(3));
            rModel.setQty(res.getString(4));
            rModel.setTotal(res.getString(5));
            myCarts.add(rModel);
        }

        CartAdapter itemAdp = new CartAdapter(CartActivity.this, myCarts);
        rvCart.setAdapter(itemAdp);
        updateItem();
        bindClick();
    }

    private void bindClick() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void initView() {
        llToolbar = (RelativeLayout) findViewById(R.id.llToolbar);
        view = (View) findViewById(R.id.view);
        rvCart = (RecyclerView) findViewById(R.id.rvCart);
        btnCheckOut = (AppCompatTextView) findViewById(R.id.btnCheckOut);
        ivBack = (AppCompatImageView) findViewById(R.id.ivBack);
    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();
        double totalRs = 0;
        double ress = 0;
        int totalItem = 0;
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setTotal(res.getString(5));
            rModel.setQty(res.getString(4));
            float sum = 0.0f;
            sum = Float.parseFloat(rModel.getTotal())*Integer.parseInt(rModel.getQty());
            totalRs = totalRs + sum;
        }
        btnCheckOut.setText("Checkout- ₹"+totalRs);

    }

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        final int[] count = {0};
        double[] totalAmount = {0};
        DatabaseHelper helper = new DatabaseHelper(CartActivity.this);
        private List<MyCart> mData;
        private LayoutInflater mInflater;
        Context mContext;

        public CartAdapter(Context context, List<MyCart> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            this.mContext = context;
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_cart, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            MyCart cart = mData.get(i);

            holder.tvName.setText( cart.getName());
            holder.tvDesc.setText( cart.getDesc());
            holder.tvPrice.setText("₹" + cart.getTotal());


            MyCart myCart = new MyCart();
            myCart.setId(cart.getId());
            myCart.setName(cart.getName());
            myCart.setDesc(cart.getDesc());
            myCart.setTotal(cart.getTotal());
            int qrt = helper.getCard(myCart.getId(), myCart.getTotal());
            if (qrt != -1) {
                count[0] = qrt;
                holder.tvQty.setText("" + count[0]);
                holder.tvQty.setVisibility(View.VISIBLE);
                holder.ivMinus.setVisibility(View.VISIBLE);

            } else {
                holder.tvQty.setVisibility(View.INVISIBLE);
                holder.ivMinus.setVisibility(View.INVISIBLE);
            }


            holder.ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count[0] = Integer.parseInt(holder.tvQty.getText().toString());
                    count[0] = count[0] - 1;
                    if (count[0] <= 0) {
                        holder.tvQty.setVisibility(View.INVISIBLE);
                        holder.ivMinus.setVisibility(View.INVISIBLE);
                        holder.tvQty.setText("" + count[0]);
                        helper.deleteRData(myCart.getId(), myCart.getTotal());
                        myCarts.remove(cart);
                        notifyDataSetChanged();
                        updateItem();
                        frmBottomCart.setVisibility(View.GONE);
                    } else {
                        holder.tvQty.setVisibility(View.VISIBLE);
                        holder.tvQty.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        helper.insertData(myCart);
                        notifyDataSetChanged();
                        updateItem();
                    }
                }
            });
            holder.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvQty.setVisibility(View.VISIBLE);
                    holder.ivMinus.setVisibility(View.VISIBLE);
                    count[0] = Integer.parseInt(holder.tvQty.getText().toString());
                    totalAmount[0] = totalAmount[0] + Double.parseDouble(myCart.getTotal());
                    count[0] = count[0] + 1;
                    holder.tvQty.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    helper.insertData(myCart);
                    updateItem();
                }
            });
            holder.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog myDelete = new AlertDialog.Builder(CartActivity.this)
                            .setTitle("Delete")
                            .setMessage("Do you want to Delete?")
                            .setIcon(R.drawable.ic_baseline_delete_24)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Log.d("sdj", "" + whichButton);
                                    dialog.dismiss();
                                    helper.deleteRData(myCart.getId(), myCart.getTotal());
                                    myCarts.remove(cart);
                                    updateItem();
                                    notifyDataSetChanged();
                                }

                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("sdj", "" + which);
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myDelete.show();
                    Button nbutton = myDelete.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setBackgroundColor(ContextCompat.getColor(CartActivity.this,R.color.teal_200));
                    Button pbutton = myDelete.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setBackgroundColor(ContextCompat.getColor(CartActivity.this,R.color.teal_200));
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            AppCompatTextView tvName;
            AppCompatTextView tvDesc;
            AppCompatTextView tvQty;
            AppCompatTextView tvPrice;
            AppCompatImageView ivClose;
            AppCompatImageView ivMinus;
            AppCompatImageView ivPlus;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                ivClose = itemView.findViewById(R.id.ivClose);
                ivMinus = itemView.findViewById(R.id.ivMinus);
                ivPlus = itemView.findViewById(R.id.ivPlus);
                tvDesc = itemView.findViewById(R.id.tvDesc);
                tvQty = itemView.findViewById(R.id.tvQty);
                tvPrice = itemView.findViewById(R.id.tvPrice);
            }

        }


    }
}