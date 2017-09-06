package com.example.qthjen.appsellcommodity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qthjen.appsellcommodity.Model.LoaiSp;
import com.example.qthjen.appsellcommodity.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterLoaiSp extends BaseAdapter {

    Context context;
    List<LoaiSp> list;

    public AdapterLoaiSp(Context context, List<LoaiSp> list) {
        this.context = context;
        this.list = list;
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

        ImageView ivRowLoaiSp;
        TextView tvRowLoaiSp;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();

        if ( view == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_loaisp, null);
            viewHolder.ivRowLoaiSp = (ImageView) view.findViewById(R.id.ivRowLoaiSp);
            viewHolder.tvRowLoaiSp = (TextView) view.findViewById(R.id.tvRowLoaiSp);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvRowLoaiSp.setText(list.get(i).getTensp());
        Picasso.with(context).load(list.get(i).getHinhsp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.ivRowLoaiSp);

        return view;
    }

}
