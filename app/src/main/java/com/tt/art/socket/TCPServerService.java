package com.tt.art.socket;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * @author T
 * @date 2019/10/18
 * @Description
 */
@SuppressLint("Registered")
public class TCPServerService extends Service {
    private boolean mIsServiceDestoryed = false;
    private String[] mDefinedMessages = new String[]{
            "你好啊",
            "你叫什么",
            "今天天气不错",
            "我是一个机器人，能同时服务多个客户端",
            "你真帅"
    };

    @Override
    public void onCreate() {
        new Thread( new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                //监听本地8868端口
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                Log.e("TcpServer", "开启Tcp服务失败，端口号：8868");
                e.printStackTrace();
                return;
            }
            while (!mIsServiceDestoryed) {
                try {
                    //接收客户端请求
                    final Socket client = serverSocket.accept();
                    Log.i("TcpServer", "accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        //用于接受客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!mIsServiceDestoryed) {
            String str = in.readLine();
            Log.i("responseClient", "来自客户端的消息" + str);
            if (str == null) {
                //客户端断开连接
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            Log.i("responseClient", "发送" + msg);
        }
        Log.i("responseClient", "退出");
        out.close();
        in.close();
        client.close();
    }
}
