package clientTest;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class ChatClient extends Frame {
    private TextField txt = new TextField();
    private TextArea cnt = new TextArea();
    DataOutputStream dos;
    DataInputStream dis;
    Socket s;

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.launchFram();
        client.connect();
    }

    public void launchFram() {
        this.setTitle("客户端");
        this.setBounds(300, 400, 200, 300);
        this.add(txt, BorderLayout.SOUTH);
        this.add(cnt, BorderLayout.NORTH);
        this.pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disConnect();
                System.exit(0);
            }
        });
        txt.addActionListener(new TFListener());
        this.setVisible(true);
    }

    public void connect() {
        try {
            s = new Socket("localhost", 8888);
            int port = s.getLocalPort();
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            this.setTitle("客户机：" + port);
            cnt.append("has connected! \r\n");
            while (true) {
                String returnMsg = dis.readUTF();
                String returnMsg1 = returnMsg.substring(returnMsg.lastIndexOf(':') + 1, returnMsg.lastIndexOf(']'));
                if ((port + "").equals(returnMsg1)) {
                    cnt.append("me:" + returnMsg.substring(returnMsg.lastIndexOf('-') + 1));
                } else {
                    cnt.append(returnMsg);
                }
            }
        } catch (IOException e) {
            System.exit(0);
        }
    }

    public void disConnect() {
        try {
            s.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TFListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                dos.writeUTF(txt.getText());
                dos.flush();
                txt.setText("");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
