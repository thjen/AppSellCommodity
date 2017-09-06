package com.example.qthjen.appsellcommodity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qthjen.appsellcommodity.Activity.MainActivity;
import com.example.qthjen.appsellcommodity.Model.GioHang;
import com.example.qthjen.appsellcommodity.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterGioHang extends BaseAdapter {

    Context context;
    List<GioHang> list;
    int layout;

    public AdapterGioHang(Context context, List<GioHang> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {

        ImageView ivRowGioHang;
        TextView tvNameRowGioHang, tvGiaRowGioHang;
        Button btCongRowGioHang, btTruRowGioHang, btSoLuongRowGioHang;

    }

    ViewHolder viewHolder;

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        viewHolder = new ViewHolder();
        if ( view == null ) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);
            viewHolder.ivRowGioHang = view.findViewById(R.id.ivRowGioHang);
            viewHolder.tvNameRowGioHang = view.findViewById(R.id.tvNameRowGioHang);
            viewHolder.tvGiaRowGioHang = view.findViewById(R.id.tvGiaRowGioHang);
            viewHolder.btTruRowGioHang = view.findViewById(R.id.btTruRowGioHang);
            viewHolder.btCongRowGioHang = view.findViewById(R.id.btCongRowGioHang);
            viewHolder.btSoLuongRowGioHang = view.findViewById(R.id.btSoLuongRowGioHang);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvNameRowGioHang.setText(list.get(i).getTenSp());
        DecimalFormat format = new DecimalFormat("###,###,###");
        viewHolder.tvGiaRowGioHang.setText("Giá: " + format.format(list.get(i).getGiaSp()) + " Đ");
        Picasso.with(context).load(list.get(i).getHinhSp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.ivRowGioHang);

        viewHolder.btSoLuongRowGioHang.setText(list.get(i).getSoLuongSp() + "");

        /** sét ẩn biện button cộng trừ khi số lượng = 1 thì ẩn button trù số lượng = 10 thì ẩn button cộng...**/
        int sl = Integer.parseInt(viewHolder.btSoLuongRowGioHang.getText().toString());
        if ( sl >= 10) {
            viewHolder.btCongRowGioHang.setVisibility(View.INVISIBLE);
            viewHolder.btTruRowGioHang.setVisibility(View.VISIBLE);
        } else if( sl <= 1) {
            viewHolder.btCongRowGioHang.setVisibility(View.VISIBLE);
            viewHolder.btTruRowGioHang.setVisibility(View.INVISIBLE);
        } else if ( sl >= 1){
            viewHolder.btCongRowGioHang.setVisibility(View.VISIBLE);
            viewHolder.btTruRowGioHang.setVisibility(View.VISIBLE);
        }

        /** sự kiện thêm số lượng khi ấn button cộng **/
        viewHolder.btCongRowGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int slUpdate = Integer.parseInt(viewHolder.btSoLuongRowGioHang.getText().toString()) + 1;
                int slHienTai = MainActivity.gioHang.get(i).getSoLuongSp();
                long giaHienTai = MainActivity.gioHang.get(i).getGiaSp();

                MainActivity.gioHang.get(i).setSoLuongSp(slUpdate);
                long giaUpdate = (giaHienTai*slUpdate)/slHienTai;
                MainActivity.gioHang.get(i).setGiaSp(giaUpdate);
                /** set lại dữ liệu vào text view **/
                DecimalFormat format = new DecimalFormat("###,###,###");
                viewHolder.tvGiaRowGioHang.setText("Giá: " + format.format(giaUpdate) + " Đ");

                com.example.qthjen.appsellcommodity.Activity.GioHang.GetData(); // gọi text view tổng thành tiền
                /** sét ẩn hiện button cộng và trừ khi số lượng đến 10 thì không thể cộng và số lượng =1 thì không thể trừ **/
                if ( slUpdate >= 9) {
                    viewHolder.btCongRowGioHang.setVisibility(View.INVISIBLE);
                    viewHolder.btTruRowGioHang.setVisibility(View.VISIBLE);
                    viewHolder.btSoLuongRowGioHang.setText(String.valueOf(slUpdate));
                } else {
                    viewHolder.btTruRowGioHang.setVisibility(View.VISIBLE);
                    viewHolder.btCongRowGioHang.setVisibility(View.VISIBLE);
                    viewHolder.btSoLuongRowGioHang.setText(String.valueOf(slUpdate));
                }

            }
        });
        /** sự kiện trù số lượng khi ấn button cộng **/
        viewHolder.btTruRowGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slUpdateTru = Integer.parseInt(viewHolder.btSoLuongRowGioHang.getText().toString()) - 1;
                int slHientai = MainActivity.gioHang.get(i).getSoLuongSp();
                long giaHientai = MainActivity.gioHang.get(i).getGiaSp();

                MainActivity.gioHang.get(i).setSoLuongSp(slUpdateTru);
                long GiaUpdate = (giaHientai*slUpdateTru)/slHientai;
                MainActivity.gioHang.get(i).setGiaSp(GiaUpdate);

                DecimalFormat format = new DecimalFormat("###,###,###");
                viewHolder.tvGiaRowGioHang.setText("Giá: " + format.format(GiaUpdate) + " Đ");

                com.example.qthjen.appsellcommodity.Activity.GioHang.GetData();
                if ( slUpdateTru <= 2) {
                    viewHolder.btCongRowGioHang.setVisibility(View.VISIBLE);
                    viewHolder.btTruRowGioHang.setVisibility(View.INVISIBLE);
                    viewHolder.btSoLuongRowGioHang.setText(String.valueOf(slUpdateTru));
                } else {
                    viewHolder.btTruRowGioHang.setVisibility(View.VISIBLE);
                    viewHolder.btCongRowGioHang.setVisibility(View.VISIBLE);
                    viewHolder.btSoLuongRowGioHang.setText(String.valueOf(slUpdateTru));
                }

            }
        });


        return view;
    }
}