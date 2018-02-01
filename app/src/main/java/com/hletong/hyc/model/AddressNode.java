package com.hletong.hyc.model;

/**
 * Created by dongdaqing on 16/4/29.
 */
public class AddressNode {
    private String key;
    private String name;//-1 => 全国， -2 => 全省，-3 => 全市
    private String shortName;
    private int childCount;
    private static final String[] ems = {
            "省", "市", "市辖区", "壮族自治区", "苗族土家族自治县", "土家族苗族自治县",
            "布依族苗族自治州", "苗族侗族自治州", "回族自治州", "彝族回族自治县", "回族自治县", "朝鲜族自治州", "蒙古族藏族自治州", "藏族自治州", "藏族羌族自治州", "哈尼族彝族自治州", "彝族自治州",
            "土家族苗族自治州", "哈萨克族自治县", "满族蒙古族自治县", "蒙古族自治县", "回族土族自治县", "土族自治县", "撒拉族自治县", "羌族自治县", "藏族自治县",
            "土家族自治县", "壮族瑶族自治县", "瑶族自治县", "回族自治区", "维吾尔自治区", "保安族东乡族撒拉族自治县", "黎族自治县", "黎族苗族自治县",
            "哈尼族彝族自治县", "傣族彝族自治县", "回族彝族自治县", "彝族自治县",
            "毛南族自治县", "仫佬族自治县", "彝族苗族自治县", "苗族自治县", "侗族自治县", "各族自治县", "满族自治县", "回族区", "林区", "白族自治州", "傣族景颇族自治州",
            "傈僳族自治州", "壮族苗族自治州", "傣族自治州", "傈僳族自治县", "哈尼族彝族傣族自治县", "彝族傣族自治县", "拉祜族佤族布朗族傣族自治县", "苗族瑶族傣族自治县", "纳西族自治县",
            "傣族拉祜族佤族自治县", "傣族佤族自治县", "佤族自治县","畲族自治县",
            "独龙族怒族自治县", "白族普米族自治县", "彝族哈尼族拉祜族自治县", "拉祜族自治县", "哈尼族自治县",
            "自治州", "自治县", "自治区", "特别行政区", "地区", "新区", "区", "區", "县", "乡", "镇"
    };

    public AddressNode() {
        childCount = 0;
    }

    public AddressNode(String mName) {
        name = mName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String mKey) {
        key = mKey;
    }

    public void setName(String mName) {
        name = mName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        if (shortName == null && name.length() > 2) {
            //情况比较特殊，特殊处理
            if ("碑林区".equals(name)){
                shortName = "碑林";
                return shortName;
            }

            for (String string : ems) {
                if (name.endsWith(string)) {
                    shortName = name.substring(0, name.length() - string.length());
                    break;
                }
            }
        }

        if (shortName == null){
            if ("-1".equals(name))
                shortName = "全国";
            else if ("-2".equals(name))
                shortName = "全省";
            else if ("-3".equals(name))
                shortName = "全市";
            else
                shortName = name;
        }

        return shortName;
    }

    public void setChildCount(int mChildCount) {
        childCount = mChildCount;
    }

    public boolean haveChild(){
        return childCount > 0;
    }

    @Override
    public String toString() {
        return "i'm " + name + ", i have " + childCount + " child(ren)";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AddressNode) {
            AddressNode addressNode = (AddressNode) o;
            return addressNode.getName().equals(name);
        }
        return false;
    }
}
