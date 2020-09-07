package com.qujiali.shareadvert.module.settlein.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.qujiali.shareadvert.R;
import com.qujiali.shareadvert.module.settlein.model.AdressDataEntity_oldddd;

import java.util.List;

public class ListPop2Adapter extends BaseAdapter {

    private List<AdressDataEntity_oldddd.RowsBean.ChildBeanXX> list;
    private Context mContext;

    public ListPop2Adapter(List<AdressDataEntity_oldddd.RowsBean.ChildBeanXX> list, Context mContext) {
        this.mContext=mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_list, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvAddressName);
            holder.tvCbx = (CheckBox) convertView.findViewById(R.id.ckb);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvCbx.setVisibility(View.GONE);
        String mod = list.get(position).getName();
        holder.tvName.setText(mod);
//        if (mod.isSelceted) {
//            holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.black));
//        } else {
//            holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        }
        return convertView;
    }

    class Holder {
        TextView tvName;
        CheckBox tvCbx;
    }
}
