package com.example.qthjen.appsellcommodity.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qthjen.appsellcommodity.Adapter.AdapterLaptop;
import com.example.qthjen.appsellcommodity.Model.SanPham;
import com.example.qthjen.appsellcommodity.R;
import com.example.qthjen.appsellcommodity.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Laptop extends AppCompatActivity {

    ArrayList<SanPham> arrayLaptop;
    AdapterLaptop adapterLaptop;

    Toolbar toolbar;
    ListView listViewLaptop;
    View progress;

    int idsp = 0;
    int page = 1; // page = 1 load dữ liệu 1-3, page = 2 load dữ liệu 4-6;

    boolean isLoading = false;
    boolean limitData = false;

    int idPhone = 0;
    String namePhone = "";
    int giaPhone = 0;
    String imagePhone = "";
    String infoPhone = "";
    int idSanPham = 0;

    mHandler1 mHandler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);

        toolbar = (Toolbar) findViewById(R.id.tbLaptop);
        listViewLaptop = (ListView) findViewById(R.id.listViewLaptop);

        mHandler1 = new mHandler1();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        progress = layoutInflater.inflate(R.layout.progressbar, null);

        ActionToolbar();

        Intent intent = getIntent();
        idsp = intent.getIntExtra("idLoaisp", -1);

        arrayLaptop = new ArrayList<>();

        adapterLaptop = new AdapterLaptop(Laptop.this, arrayLaptop, R.layout.row_laptop);

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                if ( response != null) {
//                    for ( int i = 0; i < response.length(); i++) {
//                        try {
//                            JSONObject jsonObject = response.getJSONObject(i);
//                            idPhone = jsonObject.getInt("id");
//                            namePhone = jsonObject.getString("tensp");
//                            giaPhone = jsonObject.getInt("giasp");
//                            imagePhone = jsonObject.getString("hinhsp");
//                            infoPhone = jsonObject.getString("motasp");
//                            idSanPham = jsonObject.getInt("idsanpham");
//
//                            arrayLaptop.add(new SanPham(idPhone, namePhone, giaPhone, imagePhone, infoPhone, idSanPham));
//                            adapterLaptop.notifyDataSetChanged();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(jsonArrayRequest);
        GetData(page);

        listViewLaptop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int first, int visible, int total) {

                if ( (first + visible) == total && total != 0 && isLoading == false && limitData == false ) {
                    isLoading = true;
                    mThreadData mThreadData = new mThreadData();
                    mThreadData.start();
                    adapterLaptop.notifyDataSetChanged();
                }

            }
        });

        listViewLaptop.setAdapter(adapterLaptop);

        listViewLaptop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Laptop.this, Info.class);
                intent.putExtra("info", arrayLaptop.get(i));
                startActivity(intent);
            }
        });

    }

    private void GetData(final int Page) {

        String urlLaptop = Server.url + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLaptop, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if ( response != null && response.length() != 2) {
                    listViewLaptop.removeFooterView(progress);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            idPhone = jsonObject.getInt("id");
                            namePhone = jsonObject.getString("tensp");
                            giaPhone = jsonObject.getInt("giasp");
                            imagePhone = jsonObject.getString("hinhsp");
                            infoPhone = jsonObject.getString("motasp");
                            idSanPham = jsonObject.getInt("idsanpham");

                            arrayLaptop.add(new SanPham(idPhone, namePhone, giaPhone, imagePhone, infoPhone, idSanPham));
                            adapterLaptop.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    limitData = true;
                    listViewLaptop.removeFooterView(progress);
                    Toast.makeText(Laptop.this, "Done", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put("idsanpham", String.valueOf(idsp));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class mHandler1 extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:listViewLaptop.addFooterView(progress);
                    break;
                case 1:GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class mThreadData extends Thread {

        @Override
        public void run() {

            mHandler1.sendEmptyMessage(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler1.obtainMessage(1);
            mHandler1.sendMessage(message);

            super.run();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.menuGioHang) {
            startActivity(new Intent(getApplicationContext(), com.example.qthjen.appsellcommodity.Activity.GioHang.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
