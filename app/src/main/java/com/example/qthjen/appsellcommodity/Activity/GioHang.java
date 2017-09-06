package com.example.qthjen.appsellcommodity.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qthjen.appsellcommodity.Adapter.AdapterGioHang;
import com.example.qthjen.appsellcommodity.R;
import com.example.qthjen.appsellcommodity.Util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class GioHang extends AppCompatActivity {

    TextView tvStatusGioHang, tvTongTien;
    static TextView tvGiaTri;
    ListView lvGioHang;
    Toolbar tbarGioHang;
    Button btThanhToan, btTiepTucMua;

    AdapterGioHang adapterGioHang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);

        FindViewGioHang();
        ActionToolbarGioHang();
        CheckData();
        GetData();
        RemoveItemGioHang();
        ClickButton();

    }

    private void ClickButton() {

        btTiepTucMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.qthjen.appsellcommodity.Activity.GioHang.this, MainActivity.class));
            }
        });

        btThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogThanhToan();
            }
        });

    }
    /** dialog thanh toán và sử lý sự kiện thanh toán**/
    private void DialogThanhToan() {

        final Dialog dialog = new Dialog(com.example.qthjen.appsellcommodity.Activity.GioHang.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_thanhtoan);

        final EditText etInputTenKhachHang = (EditText) dialog.findViewById(R.id.etInputTenKhachHang);
        final EditText etInputSdtKhachHang = (EditText) dialog.findViewById(R.id.etInputSdtKhachHang);
        final EditText etInputEmailKhachHang = (EditText) dialog.findViewById(R.id.etInputEmailKhachHang);

        Button btConfirm = (Button) dialog.findViewById(R.id.btConfirm);
        Button btBack = (Button) dialog.findViewById(R.id.btBack);

        /** sự kiện xác nhận thông tin khác hàng và gửi thông tin khách hàng và thông tin sản phẩm lên server **/
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( etInputTenKhachHang.getText().toString().trim().length() > 0 &&
                        etInputSdtKhachHang.getText().toString().trim().length() > 0 &&
                        etInputEmailKhachHang.getText().toString().trim().length() > 0 ) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Server.urlThongTin, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
                            Log.d("idkhachhang",madonhang);
                            final int code = Integer.parseInt(madonhang.toString().trim());
                            if ( code > 0) {
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.urlChiTietSanPham
                                        , new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        /** so sánh với kết quả của server trả về nếu là done thì clear mảng giỏ hàng **/
                                        if ( response.trim().equals("done")) { // lưu ý phải trim các response
                                            MainActivity.gioHang.clear();
                                            Toast.makeText(com.example.qthjen.appsellcommodity.Activity.GioHang.this, "Done", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(com.example.qthjen.appsellcommodity.Activity.GioHang.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(com.example.qthjen.appsellcommodity.Activity.GioHang.this, "Error", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(com.example.qthjen.appsellcommodity.Activity.GioHang.this, MainActivity.class));
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(com.example.qthjen.appsellcommodity.Activity.GioHang.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    }
                                }){ /** gửi thông tin (json) sản phẩm theo mã đơn hàng lên server **/
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        JSONArray jsonArray = new JSONArray();
                                        for ( int i = 0; i < MainActivity.gioHang.size(); i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("madonhang", String.valueOf(code));
                                                jsonObject.put("masanpham", MainActivity.gioHang.get(i).getIdSp());
                                                jsonObject.put("tensanpham", MainActivity.gioHang.get(i).getTenSp());
                                                jsonObject.put("giasanpham", MainActivity.gioHang.get(i).getGiaSp());
                                                jsonObject.put("soluongsanpham", MainActivity.gioHang.get(i).getSoLuongSp());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String, String> params = new HashMap<String, String>();
                                        params.put("json", jsonArray.toString());

                                        return params;
                                    }
                                };
                                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                requestQueue1.add(stringRequest1);

                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(com.example.qthjen.appsellcommodity.Activity.GioHang.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override /** gửi thông tin khách hàng lên server và nhận mã đơn hàng **/
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();

                            params.put("tenkhachhang", etInputTenKhachHang.getText().toString().trim());
                            params.put("sodienthoai", etInputSdtKhachHang.getText().toString().trim());
                            params.put("email", etInputEmailKhachHang.getText().toString().trim());

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    /** sự kiện xáo sản phẩm ra khỏi giỏ hàng **/
    private void RemoveItemGioHang() {

        lvGioHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(com.example.qthjen.appsellcommodity.Activity.GioHang.this);
                dialog.setTitle("Xác nhận xóa");
                dialog.setMessage("Bạn có muốn xáo sản phẩm này ra khỏi giỏ hàng");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ( MainActivity.gioHang.size() <= 0 ) {
                            tvStatusGioHang.setVisibility(View.VISIBLE);
                        } else {
                            MainActivity.gioHang.remove(position);
                            adapterGioHang.notifyDataSetChanged();
                            GetData();
                            if ( MainActivity.gioHang.size() <= 0) {
                                tvStatusGioHang.setVisibility(View.VISIBLE);
                            } else {
                                tvStatusGioHang.setVisibility(View.INVISIBLE);
                                adapterGioHang.notifyDataSetChanged();
                                GetData();
                            }
                        }
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapterGioHang.notifyDataSetChanged();
                        GetData();
                    }
                });
                dialog.show();
                return false;
            }
        });

    }

    /** hàm tính tiền và đổ dữ liệu ra textview và public static để có thể sử dụng tvGiaTri cho AdapterGioHang **/
    public static void GetData() {

        long thanhtien = 0;
        for ( int i = 0; i < MainActivity.gioHang.size(); i++) {
            thanhtien += MainActivity.gioHang.get(i).getGiaSp();
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGiaTri.setText(decimalFormat.format(thanhtien) + " Đ");

    }
    /** hàm kiểm tra dữ liệu để ẩn hiện text view và list view **/
    private void CheckData() {

        if ( MainActivity.gioHang.size() <= 0) {
            adapterGioHang.notifyDataSetChanged();
            lvGioHang.setVisibility(View.INVISIBLE);
            tvStatusGioHang.setVisibility(View.VISIBLE);
        } else {
            adapterGioHang.notifyDataSetChanged();
            lvGioHang.setVisibility(View.VISIBLE);
            tvStatusGioHang.setVisibility(View.INVISIBLE);
        }

    }

    private void ActionToolbarGioHang() {
        setSupportActionBar(tbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void FindViewGioHang() {

        tvStatusGioHang = (TextView) findViewById(R.id.tvStatusGioHang);
        tvTongTien = (TextView) findViewById(R.id.tvTongTien);
        tvGiaTri = (TextView) findViewById(R.id.tvGiaTri);
        lvGioHang = (ListView) findViewById(R.id.lvGioHang);
        tbarGioHang = (Toolbar) findViewById(R.id.tbarGioHang);
        btThanhToan = (Button) findViewById(R.id.btThanhToan);
        btTiepTucMua = (Button) findViewById(R.id.btTiepTucMua);

        adapterGioHang = new AdapterGioHang(getApplicationContext(), MainActivity.gioHang, R.layout.row_giohang);
        lvGioHang.setAdapter(adapterGioHang);

    }

}
