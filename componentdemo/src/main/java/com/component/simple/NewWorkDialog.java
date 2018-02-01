package com.component.simple;

import android.content.Context;

import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.dialog.selector.IItemShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxin on 2017/3/16.
 */
public class NewWorkDialog extends BottomSelectorDialog<NewWorkDialog.ItemData> {
    public NewWorkDialog(Context context) {
        super(context);
    }

    @Override
    protected String getTitle() {
        return "模拟网络获取数据";
    }

    @Override
    protected void onLoad() {
        showLoading();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            List<ItemData> data = new ArrayList<>();
                            data.add(new ItemData(10, "小明"));
                            data.add(new ItemData(10, "小红"));
                            data.add(new ItemData(10, "小张"));
                            data.add(new ItemData(10, "小王"));
                            data.add(new ItemData(10, "小李"));
                            data.add(new ItemData(10, "小孙"));
                            data.add(new ItemData(10, "小程"));
                            data.add(new ItemData(10, "小胡"));
                            data.add(new ItemData(10, "小曾"));
                            showList(data,true);
                            hideLoading();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public static class ItemData implements IItemShow {
        int age;
        String name;

        public ItemData(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getValue() {
            return name;
        }
    }
}
