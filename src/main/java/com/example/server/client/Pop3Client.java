package com.example.server.client;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Pop3Client extends Thread {
    public static void main(String[] args) {
        Pop3Client pop3Client = new Pop3Client();
        pop3Client.start();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 110);
            handle(socket);
        } catch (IOException e) {
            System.out.println("POP3 服务器未开启，或请检查连接的端口号");
            e.printStackTrace();
        }
    }

    public void handle(Socket socket) {
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
                    while (true) {
                        // 接收用户 cmd 输入
                        String output = scanner.nextLine().trim();
                        writer.println(output);
                        writer.flush();
//                        String line = reader.readLine(); // 接收服务器响应
//                        System.out.println(line);
                        String line = "";
                        while(!(line = reader.readLine()).equals("#end#")) {
                            System.out.println(line);
                        }
                        if("QUIT".equals(output.trim())) { // 退出时关闭IO流和套接字
                            writer.close();
                            reader.close();
                            socket.close();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }
}
