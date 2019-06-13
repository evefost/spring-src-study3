package com.xie.java.beans.processor;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/6/13
 */

public class TestProcessor implements ITestProcessor {

    @Override
    public void doSomething() {
        System.out.println("正在处理某些事情");
    }
}
