package com.component.simple;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.widget.ShapeBinder;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class ShapeActivity extends BaseActivity {
    @BindView(R.id.btn_rectangle_shape)
    Button btnRectangleShape;
    @BindView(R.id.tv_oval_shape)
    TextView tvOvalShape;
    @BindView(R.id.tv_circle_shape)
    TextView tvCircleShape;
    private int defaultColor;
    @Override
    public int getLayoutId() {
        return R.layout.activity_shape;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        defaultColor=ContextCompat.getColor(this,R.color.colorAccent);
        ShapeBinder.with(defaultColor).drawableTo(btnRectangleShape);
        ShapeBinder.with(defaultColor).shape(GradientDrawable.OVAL).drawableTo(tvOvalShape);
        ShapeBinder.with(defaultColor).shape(GradientDrawable.OVAL).strokeWidth(3).drawableTo(tvCircleShape);

    }
    public void onConnerClick(View v) {
        int color=ContextCompat.getColor(this,R.color.colorAccent);
        int radius= Integer.parseInt((String) v.getTag());
        ShapeBinder.with(defaultColor).radius(radius).drawableTo(btnRectangleShape);
        ShapeBinder.with(defaultColor).shape(GradientDrawable.OVAL).stroke(color).size(100+5*radius,80).drawableTo(tvOvalShape);

    }
    public void onBorderClick(View v) {
        int color=Color.parseColor((String) v.getTag());
        ShapeBinder.with(Color.GRAY).radius(8).stroke(color).strokeWidth(2).drawableTo(btnRectangleShape);
        ShapeBinder.with(Color.GRAY).shape(GradientDrawable.OVAL).stroke(color).strokeWidth(2).drawableTo(tvOvalShape);
        ShapeBinder.with(Color.GRAY).shape(GradientDrawable.OVAL).stroke(color).strokeWidth(3).drawableTo(tvCircleShape);

    }
    public void onColorClick(View v) {
        int color=Color.parseColor((String) v.getTag());
        ShapeBinder.with(color).radius(8).drawableTo(btnRectangleShape);
        ShapeBinder.with(color).shape(GradientDrawable.OVAL).drawableTo(tvOvalShape);
        ShapeBinder.with(color).shape(GradientDrawable.OVAL).drawableTo(tvCircleShape);

    }
    public void onStrokeClick(View v) {
        String color= (String) v.getTag();

    }
}
