package com.example.server.client;

import com.example.server.util.json.SmtpStateCode;
import org.springframework.web.socket.WebSocketSession;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author 全鸿润
 */
public class SmtpClient implements Runnable {

//    public static void main(String[] args) {
//
//        SmtpClient smtpClient = new SmtpClient();
//        smtpClient.start();
//    }

    private WebSocketSession socketSession;
    private String message;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 25);
            handle(socket);
        } catch (IOException e) {
            System.out.println("SMTP 服务器未开启，或请检查连接的端口号");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("服务不可用");
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
                        String output = scanner.nextLine().trim();
                        if ("DATA".equals(output.trim())) {
                            writer.println(output);
                            writer.flush();
                            String line = reader.readLine();
                            System.out.println(line);
                            if (line != null && line.startsWith(Integer.toString(SmtpStateCode.START_EMAIL_INPUT))) {
                                while (true) {
                                    String content = scanner.nextLine();
                                    if (".".equals(content)) {
                                        writer.append(content + "\n");
                                        writer.flush();
                                        break;
                                    }
                                    writer.append(content + "\n");
                                }
                                System.out.println(reader.readLine());
                            }
                            continue;
                        }
                        writer.println(output);
                        writer.flush();
                        String s = reader.readLine();
                        System.out.println(s);
                        if ("QUIT".equals(output.trim())) { // 退出时关闭IO流和套接字
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
