package com.zh.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zh.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 批量模式
 *
 * @author zhangqiang
 * @date 2019-04-01
 */

public class Provider2 {

    private static final String QUEUE_NAME = "test_queue_confirm_multiple";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//         生产者调用将channel设置为confirm模式,若已经设置为事务模式，则不能再设置为confirm模式
        channel.confirmSelect();
        String message = "hello confirm";
//        批量发送
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }
//        确认
        if (!channel.waitForConfirms()) {
            System.out.println("message publish failed");
        } else {
            System.out.println("message publish success");
        }
        channel.close();
        connection.close();
    }

}


