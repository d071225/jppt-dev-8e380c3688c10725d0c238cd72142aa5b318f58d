package com.hletong.mob.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ResourceFinder {
    public static final String ATTR = "attr";
    public static final String ARRAY = "array";
    public static final String ANIM = "anim";
    public static final String BOOL = "bool";
    public static final String COLOR = "color";
    public static final String DIMEN = "dimen";
    public static final String DRAWABLE = "drawable";
    public static final String ID = "id";
    public static final String INTEGER = "integer";
    public static final String LAYOUT = "layout";
    public static final String MENU = "menu";
    public static final String MIPMAP = "mipmap";
    public static final String RAW = "raw";
    public static final String STRING = "string";
    public static final String STYLE = "style";
    public static final String STYLEABLE = "styleable";

    @StringDef({ATTR, ARRAY, ANIM, BOOL, COLOR, DIMEN, DRAWABLE, ID, INTEGER, LAYOUT, MENU, MIPMAP, RAW, STRING, STYLE, STYLEABLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResType {
    }

    /**
     * 根据资源名称和类型,得到资源ID
     *
     * @param context
     * @param resourceName
     * @param type
     * @return
     */
    public static int findIdByName(Context context, String resourceName, @ResType String type) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resourceName, type, context.getPackageName());
    }

}
