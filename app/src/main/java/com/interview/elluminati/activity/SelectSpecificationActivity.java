package com.interview.elluminati.activity;

import static android.widget.Toast.LENGTH_LONG;
import static com.interview.elluminati.activity.MainActivity.frmBottomCart;
import static com.interview.elluminati.activity.MainActivity.tvCountCart;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.interview.elluminati.R;
import com.interview.elluminati.adapter.ChildAssociateAdapter;
import com.interview.elluminati.adapter.ParentAssociateAdapter;
import com.interview.elluminati.database.DatabaseHelper;
import com.interview.elluminati.database.MyCart;
import com.interview.elluminati.model.ChildAssociateModel;
import com.interview.elluminati.model.ItemModel;
import com.interview.elluminati.model.ParentAssociateModel;
import com.interview.elluminati.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSpecificationActivity extends AppCompatActivity {

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppCompatImageView imageview;
    private AppCompatImageView ivBack;
    private RecyclerView rvParentAssociate;
    List<ParentAssociateModel> parentAssociateModelList = new ArrayList<>();
    List<ChildAssociateModel> childAssociateModelList = new ArrayList<>();
    ParentAssociateAdapter parentAssociateAdapter;
    ChildAssociateAdapter childAssociateAdapter;
    private RecyclerView rvChilsAssociate;
    private LinearLayoutCompat llCounter;
    private AppCompatImageView ivMinus;
    public static AppCompatTextView tvQty;
    private AppCompatImageView ivPlus;
    public static AppCompatTextView tvAddCart;
    public static HashMap<String, Float> priceData = new HashMap<String, Float>();
    public static HashMap<String, String> descData = new HashMap<String, String>();
    final int[] countqty = {0};
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_specification);
        initView();
        databaseHelper = new DatabaseHelper(SelectSpecificationActivity.this);
        setParentAssociated();
        bindClick();
    }

    private void bindClick() {

        tvAddCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                List<String> desccrip = new ArrayList<>();
                for ( Map.Entry<String, String> entry : descData.entrySet()) {
                    String values = entry.getValue();
                    desccrip.add(values);
                }
                MyCart myCartRepeat= new MyCart();
                myCartRepeat.setId(parentAssociateModelList.get(0).getId());
                myCartRepeat.setName(parentAssociateModelList.get(0).getName());
                myCartRepeat.setDesc(String.valueOf(desccrip));
                myCartRepeat.setQty(tvQty.getText().toString());
                float sum = 0.0f;
                String[] temp = tvAddCart.getText().toString().split("₹");
                sum = Float.parseFloat(temp[1]) /Integer.parseInt(tvQty.getText().toString());
                myCartRepeat.setTotal(String.valueOf(sum));
                Log.e("INsert", "--> " + databaseHelper.insertData(myCartRepeat));
                //databaseHelper.insertData(myCart);
                onBackPressed();
                SelectSpecificationActivity.this.finish();
                Toast.makeText(SelectSpecificationActivity.this,"Added Success!", LENGTH_LONG);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countqty[0] = Integer.parseInt(tvQty.getText().toString());
                countqty[0] = countqty[0] - 1;
                if (countqty[0] <= 0) {

                } else {
                    tvQty.setText("" + countqty[0]);
                    float sum = 0.0f;
                    sum = Utils.getSumOfMap(priceData) * countqty[0];
                    tvAddCart.setText("Add to Cart- ₹"+sum);
                }
            }
        });


        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countqty[0] = Integer.parseInt(tvQty.getText().toString());
                countqty[0] = countqty[0] + 1;
                tvQty.setText("" + countqty[0]);
                float sum = 0.0f;
                sum = Utils.getSumOfMap(priceData) * countqty[0];
                tvAddCart.setText("Add to Cart- ₹"+sum);
            }
        });

    }

    public void setChildAssociated(String modifier) {

        childAssociateModelList.clear();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray specifications = obj.getJSONArray("specifications");
            for (int i = 0; i < specifications.length(); i++) {
                JSONObject objdata = specifications.getJSONObject(i);
                if (objdata.has("isAssociated") && objdata.getBoolean("isAssociated")) {
                    ChildAssociateModel pm = new ChildAssociateModel();
                    pm.setId(objdata.getString("_id"));
                    pm.setModifierName(objdata.getString("modifierName"));
                    pm.setName(objdata.getJSONArray("name").getString(0));
                    if (objdata.has("list") && !objdata.isNull("list")) {
                        JSONArray list = objdata.getJSONArray("list");
                        if (list.length() > 0) {
                            List<ItemModel> itemModelList = new ArrayList<>();
                            for (int j = 0; j < list.length(); j++) {
                                JSONObject listobj = list.getJSONObject(j);
                                ItemModel im = new ItemModel();
                                im.setId(listobj.getString("_id"));
                                im.setName(listobj.getJSONArray("name").getString(0));
                                im.setPrice(String.valueOf(listobj.getInt("price")));
                                itemModelList.add(im);
                            }
                            pm.setItemModelList(itemModelList);
                        }
                    }
                    childAssociateModelList.add(pm);
                }
            }

            showChildData(modifier);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showChildData(String modifier) {

        List<ChildAssociateModel> childAssociateModelListFinal = new ArrayList<>();
        for (int i = 0; i < childAssociateModelList.size(); i++) {
            if (childAssociateModelList.get(i).getModifierName().equals(modifier)) {
                childAssociateModelListFinal.add(childAssociateModelList.get(i));
            }
        }
        rvChilsAssociate.setLayoutManager(new LinearLayoutManager(SelectSpecificationActivity.this, LinearLayoutManager.VERTICAL, false));
        childAssociateAdapter = new ChildAssociateAdapter(SelectSpecificationActivity.this, childAssociateModelListFinal);
        rvChilsAssociate.setAdapter(childAssociateAdapter);
        rvChilsAssociate.setNestedScrollingEnabled(false);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("item_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void setParentAssociated() {

        parentAssociateModelList.clear();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray specifications = obj.getJSONArray("specifications");
            for (int i = 0; i < specifications.length(); i++) {
                JSONObject objdata = specifications.getJSONObject(i);
                if (objdata.has("isParentAssociate") && objdata.getBoolean("isParentAssociate")) {
                    ParentAssociateModel pm = new ParentAssociateModel();
                    pm.setId(objdata.getString("_id"));
                    pm.setName(objdata.getJSONArray("name").getString(0));
                    if (objdata.has("list") && !objdata.isNull("list")) {
                        JSONArray list = objdata.getJSONArray("list");
                        if (list.length() > 0) {
                            List<ItemModel> itemModelList = new ArrayList<>();
                            for (int j = 0; j < list.length(); j++) {
                                JSONObject listobj = list.getJSONObject(j);
                                ItemModel im = new ItemModel();
                                im.setId(listobj.getString("_id"));
                                im.setName(listobj.getJSONArray("name").getString(0));
                                im.setPrice(String.valueOf(listobj.getInt("price")));
                                if (j == 0) {
                                    im.isSelected = true;
                                }
                                itemModelList.add(im);
                            }
                            pm.setItemModelList(itemModelList);
                        }
                    }
                    parentAssociateModelList.add(pm);
                }
            }
            showParentData();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showParentData() {
        rvParentAssociate.setLayoutManager(new LinearLayoutManager(SelectSpecificationActivity.this, LinearLayoutManager.VERTICAL, false));
        parentAssociateAdapter = new ParentAssociateAdapter(SelectSpecificationActivity.this, parentAssociateModelList);
        rvParentAssociate.setAdapter(parentAssociateAdapter);
        rvParentAssociate.setNestedScrollingEnabled(false);
        priceData.clear();
        descData.clear();
        descData.put(parentAssociateModelList.get(0).getName(), parentAssociateModelList.get(0).getItemModelList().get(0).getName());
        priceData.put(parentAssociateModelList.get(0).getName(), Float.valueOf(parentAssociateModelList.get(0).getItemModelList().get(0).getPrice()));
        tvAddCart.setText("Add to Cart- ₹"+String.valueOf(Utils.getSumOfMap(priceData)));
        setChildAssociated(parentAssociateModelList.get(0).getItemModelList().get(0).getName());
    }

    private void initView() {
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageview = (AppCompatImageView) findViewById(R.id.imageview);
        ivBack = (AppCompatImageView) findViewById(R.id.ivBack);
        rvParentAssociate = (RecyclerView) findViewById(R.id.rvParentAssociate);
        rvChilsAssociate = (RecyclerView) findViewById(R.id.rvChilsAssociate);
        llCounter = (LinearLayoutCompat) findViewById(R.id.llCounter);
        ivMinus = (AppCompatImageView) findViewById(R.id.ivMinus);
        tvQty = (AppCompatTextView) findViewById(R.id.tvQty);
        ivPlus = (AppCompatImageView) findViewById(R.id.ivPlus);
        tvAddCart = (AppCompatTextView) findViewById(R.id.tvAddCart);
    }


}