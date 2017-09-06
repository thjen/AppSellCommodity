package com.example.qthjen.appsellcommodity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.qthjen.appsellcommodity.Model.GioHang;
import com.example.qthjen.appsellcommodity.Model.SanPham;
import com.example.qthjen.appsellcommodity.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class Info extends AppCompatActivity {

    Toolbar tbarInfo;
    ImageView ivInfo;
    TextView tvSpInfo, tvGiaInfo, tvInfo;
    Spinner spinner;
    Button btAddInfo;

    int idInfo = 0;
    String nameInfo = "";
    int giaInfo = 0;
    String imageInfo = "";
    String info = "";
    int idSanPhamInfo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        FindViewInfo();
        ActionToolbarInfo();
        GetInfo();
        EventSpinner();
        EventButton();

    }

    private void EventButton() {
        /** thêm dữ liệu vào giỏ hàng **/
        btAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( MainActivity.gioHang.size() > 0 ) { // size >0 tức là có dữ liệu
                    boolean exits = false;
                    int sl = Integer.parseInt(spinner.getSelectedItem().toString());
                    for ( int i = 0; i < MainActivity.gioHang.size(); i++) { // Để kiểm tra hết dữ liệu trong mảng
                        if ( MainActivity.gioHang.get(i).getIdSp() == idInfo) { // trùng id tức là có sản phẩm đang chuẩn bị thanh toán
                            MainActivity.gioHang.get(i).setSoLuongSp(MainActivity.gioHang.get(i).getSoLuongSp() + sl);
                            if ( MainActivity.gioHang.get(i).getSoLuongSp() >= 10) {
                                MainActivity.gioHang.get(i).setSoLuongSp(10);
                            }
                            MainActivity.gioHang.get(i).setGiaSp(giaInfo*MainActivity.gioHang.get(i).getSoLuongSp());
                            exits = true;
                        }
                    }
                    /** kiểm tra không có item trùng id thì thêm dữ liệu mới **/
                    if ( exits == false) {
                        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long thanhtien = soluong * giaInfo;
                        MainActivity.gioHang.add(new GioHang(idInfo, nameInfo, thanhtien, imageInfo, soluong));
                    }

                } else { /** thêm dữ liệu mới khi không có dữ liệu **/
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long thanhtien = soluong * giaInfo;
                    MainActivity.gioHang.add(new GioHang(idInfo, nameInfo, thanhtien, imageInfo, soluong));
                }

                Intent intent = new Intent(Info.this, com.example.qthjen.appsellcommodity.Activity.GioHang.class);
                startActivity(intent);
            }
        });

    }
    /** tạo spinner **/
    private void EventSpinner() {
        Integer[] array = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, array);
        spinner.setAdapter(adapter);
    }
    /** đổ dữ liệu **/
    private void GetInfo() {

        SanPham sanPham = (SanPham) getIntent().getSerializableExtra("info");
        idInfo = sanPham.getID();
        nameInfo = sanPham.getTenSp();
        giaInfo = sanPham.getGiaSp();
        imageInfo = sanPham.getHinhSp();
        info = sanPham.getMotaSp();
        idSanPhamInfo = sanPham.getIDSanPham();

        tvSpInfo.setText(nameInfo);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGiaInfo.setText("Giá: " + decimalFormat.format(giaInfo) + " Đ");
        Picasso.with(getApplicationContext())
                .load(imageInfo)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(ivInfo);

        tvInfo.setText(info);

    }
    /** sự kiện back của action bar **/
    private void ActionToolbarInfo() {

        setSupportActionBar(tbarInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbarInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void FindViewInfo() {

        tbarInfo = (Toolbar) findViewById(R.id.tbarInfo);
        ivInfo = (ImageView) findViewById(R.id.ivInfo);
        tvSpInfo = (TextView) findViewById(R.id.tvSpInfo);
        tvGiaInfo = (TextView) findViewById(R.id.tvGiaInfo);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        spinner = (Spinner) findViewById(R.id.spinner);
        btAddInfo = (Button) findViewById(R.id.btAddInfo);

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
