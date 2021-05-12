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
                        String output = scanner.nextLine();
                        if ("DATA".equals(output)){
                            writer.println(output);
                            writer.flush();
                            System.out.println(reader.readLine());
                            while (true){
                                String content = scanner.nextLine();
                                if (".".equals(content)){
                                    writer.append(content+"\n");
                                    writer.flush();
                                    break;
                                }
                                writer.append(content+"\n");
                            }
                            System.out.println(reader.readLine());
                            continue;
                        }
                        writer.println(output);
                        writer.flush();
                        String s = reader.readLine();
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