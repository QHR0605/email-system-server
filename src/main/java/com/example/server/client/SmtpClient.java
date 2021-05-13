package com.example.server.client;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author 全鸿润
 */
public class SmtpClient extends Thread {

    public static void main(String[] args) {

        SmtpClient smtpClient = new SmtpClient();
        smtpClient.start();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 25);
            handle(socket);
        } catch (IOException e) {
            System.out.println("SMTP 服务器未开启，或请检查连接的端口号");
            e.printStackTrace();
        }
    }

    public void handle(Socket socket){

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    OutputStream out = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
                    Scanner scanner = new Scanner(System.in);
                    System.out.println(reader.readLine());
                    while (true){
                        // 接收用户 cmd 输入
                        String output = scanner.nextLine();
                        // 客户端发送的、用于启动邮件内容传输的命令
                        if ("DATA".equals(output)){
                            writer.println(output);
                            writer.flush();
                            System.out.println(reader.readLine());
                            // 拼接邮件
                            while (true){
                                String content = scanner.nextLine();
                                if (".".equals(content)){
                                    writer.append(content).append("\n"); // 感觉这个不用拼接
                                    writer.flush();
                                    break;
                                }
                                writer.append(content).append("\n");
                            }
                            System.out.println(reader.readLine());
                            continue;
                        }
                        writer.println(output);
                        writer.flush();
                        String s = reader.readLine(); // 接收服务器响应
                        System.out.println(s);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t1.start();
    }
}
