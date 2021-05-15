package com.example.server.server;

import com.example.server.dto.Pop3Session;
import com.example.server.service.Pop3Service;
import com.example.server.service.impl.Pop3ServiceImpl;
import com.example.server.util.command.CommandParse;
import com.example.server.util.json.Pop3StateCode;

import java.io.*;
import java.net.Socket;

import static com.example.server.util.command.CommandConstant.*;

public class Pop3ServerThread extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    /**
     * 具体的POP3指令的处理函数实现
     */
    private Pop3Service pop3Service;

    public Pop3ServerThread(Socket socket) {
        this.socket = socket;
        pop3Service = new Pop3ServiceImpl(socket, new Pop3Session());
        System.out.println("开启 POP3 服务线程");
    }

    @Override
    public void run() {
        try {
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String command = reader.readLine();
                System.out.println("客户端：" + command);
                String[] args = CommandParse.parseCommand(command);
                // 输入空格或回车
                if (args == null) {
                    //writer.println(Pop3StateCode.ERR + Pop3StateCode.STNTAX);
                    int a = 1;
                } else {
                    if (USER.equals(args[0])) {
                        System.out.println("执行" + USER);
                        pop3Service.handleUserCommand(args);
                    } else if (PASS.equals(args[0])) {
                        System.out.println("执行" + PASS);
                        pop3Service.handlePassCommand(args);
                    } else if (STAT.equals(args[0])) {
                        System.out.println("执行" + STAT);
                        pop3Service.handleStatCommand(args);
                    } else if (LIST.equals(args[0])) {
                        System.out.println("执行" + LIST);
                        pop3Service.handleListCommand(args);
                    } else if (RETR.equals(args[0])) {
                        System.out.println("执行" + RETR);
                        pop3Service.handleRetrCommand(args);
                    } else if (DELE.equals(args[0])) {
                        System.out.println("执行" + DELE);
                        pop3Service.handleDeleCommand(args);
                    } else if (REST.equals(args[0])) {
                        System.out.println("执行" + REST);
                        pop3Service.handleRestCommand(args);
                    } else if (QUIT.equals(args[0])) { // 退出时关闭IO流和套接字
                        System.out.println("执行" + QUIT);
                        pop3Service.handleQuitCommand(args);
                        reader.close();
                        writer.close();
                        socket.close();
                        break;
                    } else {
                        System.out.println("POP3 没有命令" + args[0]);
                        writer.println(Pop3StateCode.ERR + Pop3StateCode.STNTAX + '\n' + "#end#");
                    }
                }
            }
            this.interrupt();
        } catch (IOException e) {
            System.out.println(socket.getInetAddress() + "断开连接");
            e.printStackTrace();
        }
    }

}
