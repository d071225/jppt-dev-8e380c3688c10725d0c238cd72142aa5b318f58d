package com.hletong.mob.base;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class HLBaseAdapter<T> extends BaseAdapter {
    protected Activity mActivity;
    protected LayoutInflater inflater;
    protected Resources res;
    protected List<T> mData;
    /**
     * 报价展示保留两位小数
     **/
    public static final DecimalFormat df2 = new DecimalFormat("0.00");

    public HLBaseAdapter(Activity activity, List<T> data) {
        // TODO Auto-generated constructor stub
        if (data == null) {
            data = new ArrayList<T>();
        }
        mData = data;
        mActivity = activity;
        inflater = mActivity.getLayoutInflater();
        res = mActivity.getResources();
    }

    protected Drawable getDrawable(int drawableId) {
        Drawable drawable = res.getDrawable(
                drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        return drawable;
    }

    public List<T> getData() {
        return mData;
    }

    /****
     * 记忆排序需要的方法
     ***/
    public void remove(T t) {
        if (mData.remove(t)) {
            notifyDataSetChanged();
        }
    }

    public void removeAll(List<T> list) {
        mData.removeAll(list);
        notifyDataSetChanged();
    }

    public void insert(T t, int to) {
        mData.add(to, t);
        notifyDataSetChanged();
    }
    /**
     * 刷新数据
     */
    public void refreash(List<T> newData) {
        // TODO Auto-generated method stub
        mData.clear();
        if (newData != null) {
            /** 此时表示清空列表数据 */
            mData.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public final View getView(int position, View convertView, ViewGroup parent){
        BaseViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=createViewHolder(position,parent);
            if(viewHolder.mConvertView==null){
                throw new NullPointerException("mConvertView in BaseViewHolder can not be null");
            }
            viewHolder.mConvertView.setTag(viewHolder);
        }else{
            viewHolder= (BaseViewHolder) convertView.getTag();
        }
        bindViewHolder(position,viewHolder);
        return viewHolder.mConvertView;
    }
    /**第一次创建的时候获取item
     * @return
     */
    protected abstract BaseViewHolder createViewHolder(int position, ViewGroup parent);

    protected abstract void bindViewHolder(int position,BaseViewHolder viewHolder);

    /**
     * 封装的Base类
     */
    public static class BaseViewHolder{
        public View mConvertView;
        public BaseViewHolder(View convertView){
            this.mConvertView=convertView;
        }
    }
}
