package clientTest;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer extends Frame {
    private TextArea cnt = new TextArea();
    List<Socket> socketList;

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.launchFram();
        server.startServer();
    }

    public void startServer() {
        try {
            ServerSocket ss = new ServerSocket(8888);
            socketList = new ArrayList<Socket>();
            Socket s;
            Thread t;
            while (true) {
                s = ss.accept();
                socketList.add(s);
                cnt.append("adress:" + s.getRemoteSocketAddress().toString() + " has connect ! \r\n");
                t = new Thread(new MyRunnable(s));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchFram() {
        this.setTitle("服务器端");
        this.setBounds(50, 50, 100, 300);
        this.add(cnt, BorderLayout.NORTH);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    class MyRunnable implements Runnable {
        private Socket ss;
        DataInputStream dis;

        public MyRunnable(Socket s) {
            this.ss = s;
        }

        @Override
        public void run() {
            String address = "";
            try {
                dis = new DataInputStream(ss.getInputStream());
                DataOutputStream dos;
                address = ss.getRemoteSocketAddress().toString();
                String msg = dis.readUTF();
                while (!"bye".equals(msg)) {
                    cnt.append("来自" + address + "的消息:" + msg + "\n");
                    for (Socket stmp : socketList) {
                        dos = new DataOutputStream(stmp.getOutputStream());
                        dos.writeUTF("来自[" + address + "]的消息-" + msg + "\n");
                        dos.flush();
                    }
                    msg = dis.readUTF();
                }
            } catch (IOException e) {
                try {
                    socketList.remove(ss);
                    ss.close();
                    cnt.append(address + "关闭连接\r\n");
                } catch (IOException e1) {
                }
            }
        }
    }
}
