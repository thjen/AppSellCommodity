package com.example.qthjen.appsellcommodity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qthjen.appsellcommodity.Model.SanPham;
import com.example.qthjen.appsellcommodity.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterLaptop extends BaseAdapter {

    Context context;
    List<SanPham> list;
    int layout;

    public AdapterLaptop(Context context, List<SanPham> list, int layout) {
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

        ImageView ivRowLaptop;
        TextView tvLaptopRowLaptop;
        TextView tvGiaRowLaptop;
        TextView tvInfoRowLaptop;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();
        if ( view == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);
            viewHolder.ivRowLaptop = (ImageView) view.findViewById(R.id.ivRowLaptop);
            viewHolder.tvLaptopRowLaptop = (TextView) view.findViewById(R.id.tvLaptopRowLaptop);
            viewHolder.tvGiaRowLaptop = (TextView) view.findViewById(R.id.tvGiaRowLaptop);
            viewHolder.tvInfoRowLaptop = (TextView) view.findViewById(R.id.tvInfoRowLaptop);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvLaptopRowLaptop.setText(list.get(i).getTenSp());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvGiaRowLaptop.setText("Giá: " + decimalFormat.format(list.get(i).getGiaSp()) + " Đ");

        viewHolder.tvInfoRowLaptop.setMaxLines(2); // sét số dòng tối đa = 2;
        viewHolder.tvInfoRowLaptop.setEllipsize(TextUtils.TruncateAt.END); // sét dấu ... có text view
        viewHolder.tvInfoRowLaptop.setText(list.get(i).getMotaSp());

        Picasso.with(context).load(list.get(i).getHinhSp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.ivRowLaptop);

        return view;
    }

}
