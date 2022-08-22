package org.abc.wiki.util;

import java.io.Serializable;

/**
 * RequestContext：请求过程的上下文
 * 线程本地变量：用于在整个线程执行过程中都可以获取远程IP
 */
public class RequestContext implements Serializable {

    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
