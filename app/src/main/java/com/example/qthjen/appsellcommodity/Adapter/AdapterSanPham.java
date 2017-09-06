package com.example.qthjen.appsellcommodity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qthjen.appsellcommodity.Activity.Info;
import com.example.qthjen.appsellcommodity.Model.SanPham;
import com.example.qthjen.appsellcommodity.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterSanPham extends RecyclerView.Adapter<AdapterSanPham.ViewHolder> {

    Context context;
    ArrayList<SanPham> list;
    int layout;

    public AdapterSanPham(Context context, ArrayList<SanPham> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvRowSp.setText(list.get(position).getTenSp());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvRowGiaSp.setText("Giá: " + decimalFormat.format(list.get(position).getGiaSp()) + " Đ");

        Picasso.with(context).load(list.get(position).getHinhSp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(holder.ivRowSp);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRowSp;
        TextView tvRowSp;
        TextView tvRowGiaSp;

        public ViewHolder(View itemView) {
            super(itemView);
            ivRowSp = (ImageView) itemView.findViewById(R.id.ivRowSp);
            tvRowSp = (TextView) itemView.findViewById(R.id.tvRowSp);
            tvRowGiaSp = (TextView) itemView.findViewById(R.id.tvRowGiaSp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Info.class);
                    intent.putExtra("info", list.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }

    }

}
