package com.hletong.hyc.enums;

import android.support.annotation.NonNull;

import com.hletong.mob.net.Certificate;

/**
 * 多渠道打包的配置
 * Created by cc on 2016/12/29.
 */
public enum Config {
    DEV("https://test.hletong.com/jppt/", Certificate.CER_HY_DEV_HTTPS),
//    DEV("http://192.168.1.100:20000/jppt/", null), //刘冬财-测试环境
//    DEV("http://192.168.1.100:20002/jppt/", null), //王丹-测试环境
    //DEV("http://192.168.1.100:20003/jppt/", null), //吴昊-测试环境

    //DEV("http://192.168.1.100:28080/jppt/", null), //曾孝-测试环境
    //DEV("http://192.168.4.16:20080/jppt/", null), //潘海涛-测试环境
    BUSI("https://test.hletong.com/jppt-test/", Certificate.CER_HY_BUSI_HTTPS),
    PRODUCT("https://jp.hletong.com/jppt/", Certificate.CER_HY_PRODUCT_HTTPS);
    private String host;
    private String cer;

    Config(String host, String cer) {
        this.host = host;
        this.cer = cer;
    }

    public String getHost() {
        return host;
    }

    public String getCer() {
        return cer;
    }

    /**
     * 根据 枚举的名字获取枚举对象
     *
     * @param name enum.name()
     * @return 枚举配置对象
     */
    @NonNull
    public static Config getConfig(String name) {
        Config configs[] = Config.values();
        for (Config config : configs) {
            if (config.name().equals(name)) {
                return config;
            }
        }
        throw new IllegalArgumentException("can not find Config with name:" + name);
    }
}
