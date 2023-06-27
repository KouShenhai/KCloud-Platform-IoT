package org.laokou.common.shardingsphere.config.auto;

import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereDriverURLProvider;

public class Test implements ShardingSphereDriverURLProvider {
    @Override
    public boolean accept(String s) {
        return true;
    }

    @Override
    public byte[] getContent(String s) {
        System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222");
        return new byte[0];
    }
}
