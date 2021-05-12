package com.example.server.util.idGenerator;

import java.util.Random;

/**
 * @author 全鸿润
 */
public class IdGenerator {

    private static int ID = 0;

    /**
     * 生成全局唯一的id,可用作mid
     * @return 全局唯一的id
     */
    public synchronized static int getId() {
        int res = new Random().nextInt(1000000000);
        if (ID == res) {
            ID += 1;
            return ID;
        }
        ID = res;
        return ID;
    }

}
