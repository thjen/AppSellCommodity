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
import com.example.qthjen.appsellcommodity.Adapter.AdapterPhone;
import com.example.qthjen.appsellcommodity.Model.SanPham;
import com.example.qthjen.appsellcommodity.R;
import com.example.qthjen.appsellcommodity.Util.CheckConnected;
import com.example.qthjen.appsellcommodity.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmartPhone extends AppCompatActivity {

    ArrayList<SanPham> arrayPhone;
    AdapterPhone adapterPhone;

    Toolbar toolbar;
    View footerView;
    ListView listViewPhone;

    boolean isLoading = false; // đang load dữ liệu
    boolean limitData = false; // có dữ liệu = false ko có dữ liệu = true

    int idSp = 0;
    int page = 1; // page = 1 load dữ liệu 1-3, page = 2 load dữ liệu 4-6;

    int idPhone = 0;
    String namePhone = "";
    int giaPhone = 0;
    String imagePhone = "";
    String infoPhone = "";
    int idSanPham = 0;

    mHandler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_phone);

        FindViewPhone();
        arrayPhone = new ArrayList<>();

        adapterPhone = new AdapterPhone(getApplicationContext(), arrayPhone, R.layout.row_phone);

        listViewPhone.setAdapter(adapterPhone);

        if (CheckConnected.haveNetworkConnected(getApplicationContext())) { // kiểm tra internet
            ActionToolbar();
            GetDataPhone();
            GetData(page);
            LoadMoreData();
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Internet!", Toast.LENGTH_SHORT).show();
        }
        /** sự kiện click item list view chuyển sang activity info và truyền đối tượng sản phẩm **/
        listViewPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SmartPhone.this, Info.class);
                intent.putExtra("info", arrayPhone.get(i));
                startActivity(intent);
            }
        });

    }
    /** sự kiện kéo scroll view để load thêm sản phẩm **/
    private void LoadMoreData() {

        listViewPhone.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItem, int totalItem) {

                if ( (firstItem + visibleItem) == totalItem && totalItem != 0 && isLoading == false && limitData == false) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                    adapterPhone.notifyDataSetChanged();
                }

            }
        });

    }
    /** đổ dữ liệu ra list view **/
    private void GetData(final int Page) {

        String urlPhone = Server.url + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPhone, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if ( response != null && response.length() != 2) { /** vì response khi không có dữ liệu luôn cho 1 cặp ngoặc [] **/
                    listViewPhone.removeFooterView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for ( int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            idPhone = jsonObject.getInt("id");
                            namePhone = jsonObject.getString("tensp");
                            giaPhone = jsonObject.getInt("giasp");
                            imagePhone = jsonObject.getString("hinhsp");
                            infoPhone = jsonObject.getString("motasp");
                            idSanPham = jsonObject.getInt("idsanpham");

                            arrayPhone.add(new SanPham(idPhone, namePhone, giaPhone, imagePhone, infoPhone, idSanPham));
                            adapterPhone.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    limitData = true;
                    listViewPhone.removeFooterView(footerView);
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
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

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("idsanpham", String.valueOf(idSp));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlPhone, new Response.Listener<JSONArray>() {
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
//                            arrayPhone.add(new SanPham(idPhone, namePhone, giaPhone, imagePhone, infoPhone, idSanPham));
//                            adapterPhone.notifyDataSetChanged();
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


    }
    /** sự kiện back của action bar **/
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
    /** nhận idsanpham được truyền từ main **/
    public void GetDataPhone() {
        Intent intent = getIntent();
        idSp = intent.getIntExtra("idLoaisp", -1);
    }

    private void FindViewPhone() {
        toolbar = (Toolbar) findViewById(R.id.tbPhone);
        listViewPhone = (ListView) findViewById(R.id.listViewPhone);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = layoutInflater.inflate(R.layout.progressbar, null);

        mhandler = new mHandler();
    }
    /** sử lý tiến trình Handler và Thresd**/
    public class mHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch ( msg.what) {

                case 0: listViewPhone.addFooterView(footerView);
                    break;

                case 1: GetData(++page);
                    isLoading = false;
                    break;

            }

            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {

            mhandler.sendEmptyMessage(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message message = mhandler.obtainMessage(1);
            mhandler.sendMessage(message);

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
