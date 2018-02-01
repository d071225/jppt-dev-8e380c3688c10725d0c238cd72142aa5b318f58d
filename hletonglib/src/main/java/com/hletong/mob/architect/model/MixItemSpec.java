package com.hletong.mob.architect.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by ddq on 2016/12/14.
 * 用于处理混合类型列表，eg：返回的列表里面存在多种数据类型，比如消息里面，服务端返回的列表
 * 里面包含Upcoming和Receipt两种数据类型，要解析列表，就要把两种类型的特有字段找出来，
 * 其中Upcoming有type是receipt没有的，而Receipt有todoType是Upcoming没有的，所以可以像这样构造
 * <code>
 * ArrayList<MixItemSpec> mixItemSpecs = new ArrayList<>();
 * mixItemSpecs.add(new MixItemSpec("\"type\":", new TypeToken<List<Upcoming>>() {}.getType()));
 * mixItemSpecs.add(new MixItemSpec("\"todoType\":", new TypeToken<List<Receipt>>() {}.getType()));
 * </code>
 * 这个mixItemSpecs就可以用来解析同时包含Upcoming和Receipt两种数据类型的列表
 */
public class MixItemSpec {
    private String spec;//自己有而其他人没有的特征
    private Type type;//具体的类型
    private TypeToken<?> typeToken;//具体的类型

    public MixItemSpec(String spec, TypeToken<?> typeToken) {
        this.spec = spec;
        this.typeToken = typeToken;
        this.type = typeToken.getType();
    }

    public String getSpec() {
        return spec;
    }

    public Type getType() {
        return type;
    }

    public TypeToken<?> getTypeToken() {
        return typeToken;
    }
}
