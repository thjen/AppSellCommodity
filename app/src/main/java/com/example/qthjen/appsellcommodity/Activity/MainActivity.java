package com.example.qthjen.appsellcommodity.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.qthjen.appsellcommodity.Adapter.AdapterLoaiSp;
import com.example.qthjen.appsellcommodity.Adapter.AdapterSanPham;
import com.example.qthjen.appsellcommodity.Model.GioHang;
import com.example.qthjen.appsellcommodity.Model.LoaiSp;
import com.example.qthjen.appsellcommodity.Model.SanPham;
import com.example.qthjen.appsellcommodity.R;
import com.example.qthjen.appsellcommodity.Util.CheckConnected;
import com.example.qthjen.appsellcommodity.Util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar tbarHome;
    ViewFlipper vflipperHome;
    RecyclerView recyclerHome;
    NavigationView navigationHome;
    ListView lvNavigation;
    DrawerLayout drawerLayout;

    ArrayList<LoaiSp> arrayLoaiSp;
    AdapterLoaiSp adapterLoaiSp;

    ArrayList<SanPham> arraySp;
    AdapterSanPham adapterSp;

    public static ArrayList<GioHang> gioHang; // mảng giỏ hàng cho mọi activity đều có thể truy cập

    int id = 0;
    String tenSp = "";
    String hinhSp = "";

    int idSp = 0;
    String tenSanPham = "";
    int giaSp = 0;
    String hinhSanPham = "";
    String motaSp = "";
    int idSanPham = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindView();
        arrayLoaiSp = new ArrayList<>();
        adapterLoaiSp = new AdapterLoaiSp(getApplicationContext(), arrayLoaiSp);
        arrayLoaiSp.add(0, new LoaiSp(0, "Trang chính", "http://www.iconsdb.com/icons/preview/royal-blue/home-xxl.png"));
        lvNavigation.setAdapter(adapterLoaiSp);

        if (CheckConnected.haveNetworkConnected(getApplicationContext())) { // kiểm tra internet
            ActionBar();
            ShowFlipper();
            GetLoaiSp();
            GetSanPham();
            OnItemClickMenuListView();
        } else {    /** không có internet tự finish sau 2.5 giây **/
            Toast.makeText(MainActivity.this, "Please Check Internet!", Toast.LENGTH_SHORT).show();
            new CountDownTimer(2500,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                     finish();
                }
            }.start();

        }

    }
    /** bắt sự kiện item click listview trong navigation view **/
    private void OnItemClickMenuListView() {

        lvNavigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {

                    case 0:
                        if ( CheckConnected.haveNetworkConnected(getApplicationContext())) {
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Please Check Internet!", Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 1:
                        if ( CheckConnected.haveNetworkConnected(getApplicationContext())) {
                            Intent intentPhone = new Intent(MainActivity.this, SmartPhone.class);
                            intentPhone.putExtra("idLoaisp", arrayLoaiSp.get(i).getId()); // truyền id loại sp
                            startActivity(intentPhone);
                        } else {
                            Toast.makeText(MainActivity.this, "Please Check Internet!", Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 2:
                        if ( CheckConnected.haveNetworkConnected(getApplicationContext())) {
                            Intent intentLaptop = new Intent(MainActivity.this, Laptop.class);
                            intentLaptop.putExtra("idLoaisp", arrayLoaiSp.get(i).getId());
                            startActivity(intentLaptop);
                        } else {
                            Toast.makeText(MainActivity.this, "Please Check Internet!", Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 3:
                        if ( CheckConnected.haveNetworkConnected(getApplicationContext())) {
                            startActivity(new Intent(MainActivity.this, LienHe.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Please Check Internet!", Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case 4:
                        if ( CheckConnected.haveNetworkConnected(getApplicationContext())) {
                            startActivity(new Intent(MainActivity.this, MapsActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Please Check Internet!", Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }

            }
        });

    }
    /** đổ dữ liệu ra recycler view **/
    private void GetSanPham() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerHome);
        recyclerView.setHasFixedSize(true);

     //   LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
     //     recyclerView.setLayoutManager(layoutManager);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // 2 col
        arraySp = new ArrayList<SanPham>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlSanPham, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if ( response != null ) {
                    for ( int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            idSp = jsonObject.getInt("id");
                            tenSanPham = jsonObject.getString("tensp");
                            giaSp = jsonObject.getInt("giasp");
                            hinhSanPham = jsonObject.getString("hinhsp");
                            motaSp = jsonObject.getString("motasp");
                            idSanPham = jsonObject.getInt("idsanpham");

                            arraySp.add(new SanPham(idSp, tenSanPham, giaSp, hinhSanPham, motaSp, idSanPham));
                            adapterSp.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

        adapterSp = new AdapterSanPham(getApplicationContext(), arraySp, R.layout.row_sanpham);
        recyclerView.setAdapter(adapterSp);

    }
    /** đổ dữ liệu ra list view trong navigation view **/
    private void GetLoaiSp() {

        JsonArrayRequest jsonArray = new JsonArrayRequest(Server.urlLoaiSp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if( response != null) {
                    for ( int i = 0 ; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenSp = jsonObject.getString("tenloaisp");
                            hinhSp = jsonObject.getString("hinhloaisp");
                            arrayLoaiSp.add(new LoaiSp(id, tenSp, hinhSp));
                            adapterLoaiSp.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    arrayLoaiSp.add(3, new LoaiSp(0, "Liên hệ", "http://mamnonngoisaoviet.edu.vn/images/6.png"));
                    arrayLoaiSp.add(4, new LoaiSp(0, "Thông tin", "http://pgdvinhlinh.edu.vn/images/month.png"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArray);

    }
    /** sự kiện đóng mở navigation view **/
    private void ActionBar() {

        setSupportActionBar(tbarHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbarHome.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        tbarHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    /** hàm show quảng cáo (flipper view) **/
    private void ShowFlipper() {

        ArrayList<String> arrayAd = new ArrayList<>();
        arrayAd.add("http://tinhte.cdnforo.com/store/2014/08/2572609_Hinh_2.jpg");
        arrayAd.add("https://cdn.tgdd.vn/qcao/04_08_2017_08_44_32_Xperia-XA1-L1-800-300.png");
        arrayAd.add("https://cdn1.tgdd.vn/qcao/23_08_2017_16_04_26_Galaxy-Note-8-Gat-Gach-800-300-3.png");
        arrayAd.add("https://cdn3.tgdd.vn/qcao/14_08_2017_15_40_24_Oppo-Tang-Qua-Ngon-800-300.png");
        arrayAd.add("https://cdn2.tgdd.vn/qcao/31_07_2017_15_51_33_Big-T8-800-300.png");

        for ( int i = 0; i < arrayAd.size(); i++) {
            /** add link into imageView **/
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(arrayAd.get(i)).into(imageView);
            /** set size image = view flipper **/
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            /** add into flipper **/
            vflipperHome.addView(imageView);
        }
        vflipperHome.setFlipInterval(3000); // set run time = 5s;
        vflipperHome.setAutoStart(true); // enable auto run
        /** hiệu ứng chuyển quảng cáo **/
        Animation animationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        vflipperHome.setInAnimation(animationIn);
        vflipperHome.setOutAnimation(animationOut);

    }

    public void FindView() {

        tbarHome       = (Toolbar)        findViewById(R.id.tbarHome);
        vflipperHome   = (ViewFlipper)    findViewById(R.id.vflipperHome);
        recyclerHome   = (RecyclerView)   findViewById(R.id.recyclerHome);
        navigationHome = (NavigationView) findViewById(R.id.navigationHome);
        lvNavigation   = (ListView)       findViewById(R.id.lvNavigation);
        drawerLayout   = (DrawerLayout)   findViewById(R.id.drawerLayout);

        if ( gioHang != null) {

        } else {
            gioHang = new ArrayList<>();
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
