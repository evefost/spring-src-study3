package com.xie.java.beans.processor;

import java.util.Random;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/6/13
 */

public class Target implements ITarget {

    @Override
    public void doSomething() {
        Random random = new Random();
        System.out.println("target 正在处理某些事情");
        if(random.nextBoolean()){
            System.out.println("target 执生失败");
          throw new RuntimeException("发生异常了");
        }else {

        }
        System.out.println("target 执生成功");

    }
}
