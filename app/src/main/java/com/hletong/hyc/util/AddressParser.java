package com.hletong.hyc.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.util.ArrayMap;
import android.util.JsonReader;

import com.hletong.hyc.model.AddressNode;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by dongdaqing on 16/4/29.
 */
public class AddressParser {
    /**
     * 地址一旦被查询过一次,就会被缓存下来,
     * 下一次再访问时就不用再去文件里面搜索了
     */
    private ArrayMap<String, ArrayList<AddressNode>> cached;
    private ArrayMap<String, String> ancestors;//保存父子关系
    private static AddressParser parser;

    private AddressParser() {
        cached = new ArrayMap<>();
        ancestors = new ArrayMap<>();
    }

    public static AddressParser getParser() {
        if (parser == null)
            parser = new AddressParser();
        return parser;
    }

    public void clean() {
        cached.clear();
        ancestors.clear();
    }

    /**
     * 获取所有的省
     *
     * @param context
     * @return
     */
    public ArrayList<AddressNode> getProvinces(Context context) {
        if (cached.get("provinces") == null) {
            cached.put("provinces", parseCityListWithParent(null, context));
        }
        return cached.get("provinces");
    }

    /**
     * 通过上一级的Code获取它的直接子集
     * 要获取所有的省,由于在json文件中省是第一级数据,没有上级,故而传null
     * 要获取某一个省下面的所有市,就传入省的Code
     * 要获取某一个市下面的所有区,就传入该市的Code
     *
     * @param parent
     * @param context
     * @return
     */
    public ArrayList<AddressNode> getCities(String parent, Context context) {
        final String cacheKey;

        if (parent == null) {
            cacheKey = "provinces";
        } else {
            cacheKey = parent;
        }

        if (cached.get(cacheKey) == null) {
            /**
             * 省是用“province”作为key来存储
             * 但是查询仍就是用null
             */
            cached.put(cacheKey, parseCityListWithParent(parent, context));
        }
        return cached.get(cacheKey);
    }

    /**
     * 跟上面的函数功能一样,只不过这里是具体的执行步骤,上面只是外部调用的方法
     *
     * @param parentCode
     * @param context
     * @return
     */
    private ArrayList<AddressNode> parseCityListWithParent(String parentCode, Context context) {
        ArrayList<AddressNode> data = new ArrayList<>();
        AssetManager manager = context.getAssets();
        JsonReader reader = null;
        try {
            InputStreamReader fileReader = new InputStreamReader(manager.open("address.json"));
            reader = new JsonReader(fileReader);
            reader.beginArray();
            ArrayList<String> parents = findAncestors(parentCode);
            boolean hasNext = true;
            for (int i = 0; i < parents.size(); i++) {
                hasNext = hasNext && findNodeInCurrentLevel(reader, parents.get(i));
            }

            if (!hasNext) {
                return data;
            }

            data = collectCity(reader, parentCode);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(e.getLocalizedMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    private ArrayList<String> findAncestors(String me) {
        ArrayList<String> ancestor = new ArrayList<>();

        if (me == null)
            return ancestor;

        ancestor.add(me);
        while (ancestors.get(me) != null) {
            final String parent = ancestors.get(me);
            ancestor.add(parent);
            me = parent;
        }
        Collections.reverse(ancestor);
        return ancestor;
    }

    private boolean findNodeInCurrentLevel(JsonReader reader, String node) {
        try {
            boolean find = false;
            while (reader.hasNext()) { //判断数组元素
                reader.beginObject();
                do {
                    final String name = reader.nextName();
                    if ("name".equals(name)) {
                        if (reader.nextString().equals(node)) {
                            find = true;
                        }
                    } else {
                        if (find && name.equals("data")) {
                            reader.beginArray();
                            return true;
                        } else {
                            reader.skipValue();
                        }
                    }
                } while (reader.hasNext());
                reader.endObject();
                if (find)//没有下级了
                    return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ArrayList<AddressNode> collectCity(JsonReader reader, String parent) {
        ArrayList<AddressNode> list = new ArrayList<>();

        try {
            while (reader.hasNext()) {
                reader.beginObject();
                AddressNode addressNode = new AddressNode();
                list.add(addressNode);
                while (reader.hasNext()) {
                    final String name = reader.nextName();
                    if ("data".equals(name)) {
                        addressNode.setChildCount(countChild(reader));
                    } else {
                        switch (name) {
                            case "name":
                                addressNode.setName(reader.nextString());
                                ancestors.put(addressNode.getName(), parent);//存储父子关系
                                break;
                            case "key":
                                addressNode.setKey(reader.nextString());
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }

                    if (!reader.hasNext()) {
                        reader.endObject();
                        break;
                    }
                }
//                Logger.d(addressNode.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private int countChild(JsonReader reader){
        int count = 0;
        try {
            reader.beginArray();
            while (reader.hasNext()){
                count++;
                reader.beginObject();
                while (reader.hasNext())
                {
                    reader.nextName();
                    reader.skipValue();
                }
                reader.endObject();
            }
            reader.endArray();
        } catch (IOException mE) {
            mE.printStackTrace();
        }
        return count;
    }
}
