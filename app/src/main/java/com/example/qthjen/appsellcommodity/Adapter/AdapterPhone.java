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

public class AdapterPhone extends BaseAdapter {

    Context contextPhone;
    List<SanPham> listPhone;
    int layoutPhone;

    public AdapterPhone(Context contextPhone, List<SanPham> listPhone, int layoutPhone) {
        this.contextPhone = contextPhone;
        this.listPhone = listPhone;
        this.layoutPhone = layoutPhone;
    }

    @Override
    public int getCount() {
        return listPhone.size();
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

        ImageView ivRowPhone;
        TextView tvPhoneRowPhone;
        TextView tvGiaRowPhone;
        TextView tvInfoRowPhone;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();
        if ( view == null) {

            LayoutInflater layoutInflater = (LayoutInflater) contextPhone.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layoutPhone, null);
            viewHolder.ivRowPhone = (ImageView) view.findViewById(R.id.ivRowPhone);
            viewHolder.tvPhoneRowPhone = (TextView) view.findViewById(R.id.tvPhoneRowPhone);
            viewHolder.tvGiaRowPhone = (TextView) view.findViewById(R.id.tvGiaRowPhone);
            viewHolder.tvInfoRowPhone = (TextView) view.findViewById(R.id.tvInfoRowPhone);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvPhoneRowPhone.setText(listPhone.get(i).getTenSp());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvGiaRowPhone.setText("Giá: " + decimalFormat.format(listPhone.get(i).getGiaSp()) + " Đ");

        viewHolder.tvInfoRowPhone.setMaxLines(2); // sét số dòng tối đa = 2;
        viewHolder.tvInfoRowPhone.setEllipsize(TextUtils.TruncateAt.END); // sét dấu ... có text view
        viewHolder.tvInfoRowPhone.setText(listPhone.get(i).getMotaSp());

        Picasso.with(contextPhone).load(listPhone.get(i).getHinhSp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.ivRowPhone);

        return view;
    }

}
