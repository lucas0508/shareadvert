package com.qujiali.shareadvert.base.view;

import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * @author QiZai
 * @desc
 * @date 2018/7/24 9:25
 */

public abstract class ViewHolder<T> extends BaseViewHolder<T> {

    public ViewHolder(ViewGroup parent, int res) {
        super(parent, res);
        initView();
    }

	
    public abstract void initView();

}
