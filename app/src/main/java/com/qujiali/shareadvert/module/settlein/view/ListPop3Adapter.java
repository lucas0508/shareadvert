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

import java.util.HashMap;
import java.util.List;

public class ListPop3Adapter extends BaseAdapter {

    private List<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX> list;
    private Context mContext;
    public static HashMap<Integer, Boolean> isSelected;

    public ListPop3Adapter(List<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX> list, Context mContext) {
        this.mContext = mContext;
        this.list = list;
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
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
        MyViewHoler holder = null;
        if (convertView == null) {
            holder = new MyViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_list, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvAddressName);
            holder.tvCbx = (CheckBox) convertView.findViewById(R.id.ckb);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHoler) convertView.getTag();
        }
        String mod = list.get(position).getName();
        holder.tvName.setText(mod);
//        if (list.get(position).isIscheck()){
//            holder.tvCbx.setChecked(true);
//        }else {
//            holder.tvCbx.setChecked(false);
//        }
        holder.tvCbx.setChecked(isSelected.get(position));


//        if (mod.isSelceted) {
//            holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.black));
//        } else {
//            holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        }
        return convertView;
    }

    public class MyViewHoler {
        public TextView tvName;
        public CheckBox tvCbx;
    }
}
