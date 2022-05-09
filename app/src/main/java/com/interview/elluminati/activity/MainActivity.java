package com.interview.elluminati.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.interview.elluminati.R;
import com.interview.elluminati.database.DatabaseHelper;
import com.interview.elluminati.database.MyCart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppCompatImageView imageview;
    private AppCompatImageView ivBack;
    private AppCompatImageView ivLikee;
    private AppCompatImageView ivSetting;
    private AppCompatTextView tvCustomize;
    private LinearLayoutCompat llCounter;
    private AppCompatImageView ivMinus;
    private AppCompatTextView ivQty;
    private AppCompatImageView ivPlus;
    public static FrameLayout frmBottomCart;
    public static AppCompatTextView tvCountCart;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindClick();

    }

    private void bindClick() {

        tvCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SelectSpecificationActivity.class));
            }
        });

        frmBottomCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });


        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Attention")
                        .setMessage("This item has multiple customizations added. Remove the correct item from the cart.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_200));

            }
        });


        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog myDelete = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Repeat Last")
                        .setMessage("Customization:")
                        .setPositiveButton("Repeat", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                addRepear();

                            }

                        })
                        .setNegativeButton("Customize", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(MainActivity.this, SelectSpecificationActivity.class));
                            }
                        })
                        .create();
                myDelete.show();
                Button nbutton = myDelete.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_200));
                Button pbutton = myDelete.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_200));
            }
        });


    }

    private void addRepear() {
        List<MyCart> myCarts = new ArrayList<>();
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

        MyCart myCartRepeat = myCarts.get(myCarts.size() - 1);
        myCartRepeat.setQty(String.valueOf(Integer.parseInt(myCartRepeat.getQty())+1));
        Log.e("INsert", "--> " + databaseHelper.insertData(myCartRepeat));
        onResume();
    }

    private void initView() {
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageview = (AppCompatImageView) findViewById(R.id.imageview);
        ivBack = (AppCompatImageView) findViewById(R.id.ivBack);
        ivLikee = (AppCompatImageView) findViewById(R.id.ivLikee);
        ivSetting = (AppCompatImageView) findViewById(R.id.ivSetting);
        tvCustomize = (AppCompatTextView) findViewById(R.id.tvCustomize);
        llCounter = (LinearLayoutCompat) findViewById(R.id.llCounter);
        ivMinus = (AppCompatImageView) findViewById(R.id.ivMinus);
        ivQty = (AppCompatTextView) findViewById(R.id.ivQty);
        ivPlus = (AppCompatImageView) findViewById(R.id.ivPlus);
        frmBottomCart = (FrameLayout) findViewById(R.id.frmBottomCart);
        tvCountCart = (AppCompatTextView) findViewById(R.id.tvCountCart);


        databaseHelper = new DatabaseHelper(MainActivity.this);
        Cursor resw = databaseHelper.getAllData();
        if (resw.getCount() != 0) {
            frmBottomCart.setVisibility(View.VISIBLE);
            tvCountCart.setText("" + resw.getCount());
            tvCustomize.setVisibility(View.GONE);
            llCounter.setVisibility(View.VISIBLE);
            showqty();
        } else {
            frmBottomCart.setVisibility(View.GONE);
            tvCustomize.setVisibility(View.VISIBLE);
            llCounter.setVisibility(View.GONE);
        }
    }

    private void showqty() {
        int fqty = 0;
        Cursor res = databaseHelper.getAllData();
        while (res.moveToNext()) {
            fqty = Integer.parseInt(res.getString(4)) + fqty;
        }
        ivQty.setText("" + fqty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor resw = databaseHelper.getAllData();
        if (resw.getCount() != 0) {
            frmBottomCart.setVisibility(View.VISIBLE);
            tvCountCart.setText("" + resw.getCount());
            tvCustomize.setVisibility(View.GONE);
            llCounter.setVisibility(View.VISIBLE);
            showqty();
        } else {
            frmBottomCart.setVisibility(View.GONE);
            tvCustomize.setVisibility(View.VISIBLE);
            llCounter.setVisibility(View.GONE);
        }
    }
}