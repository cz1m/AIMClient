package com.like4u.aim.domain;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 16:57
 */
public interface MessageType {

    Integer MESSAGE_LOGIN_SUCCESS=200;
    Integer MESSAGE_LOGIN_FAIL=401;
    /**
     * 普通信息
     * */
    Integer MESSAGE_COMMON=100;
    /**
     * 该信息保存了在线用户列表
     * */
    Integer MESSAGE_USER_LIST=101;
    /**
     * 请求获取在线用户
     * */
    Integer MESSAGE_GET_USER_LIST=102;
    /**
     * 客户端请求退出
     * */

    Integer MESSAGE_CLIENT_EXIT=103;

    Integer MESSAGE_SEND_TO_ALL=104;
    Integer MESSAGE_FILE=105;

}
