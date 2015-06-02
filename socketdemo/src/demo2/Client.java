package demo2;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * Created by Lichao.W At 2015/5/28 21:43
 * wanglichao@163.com
 */
public class Client {

    public static void main(String args[]) throws Exception {
        //Ϊ�˼���������е��쳣��ֱ��������
        String host = "127.0.0.1";  //Ҫ���ӵķ����IP��ַ
        int port = 8899;   //Ҫ���ӵķ���˶�Ӧ�ļ����˿�
        //�����˽�������
        Socket client = new Socket(host, port);
        //�������Ӻ�Ϳ����������д������
        Writer writer = new OutputStreamWriter(client.getOutputStream());
        writer.write("Hello Server.");
        writer.write("eof");
        writer.flush();
        //д���Ժ���ж�����
        Reader reader = new InputStreamReader(client.getInputStream());
        char chars[] = new char[64];
        int len;
        StringBuffer sb = new StringBuffer();
        String temp;
        int index;
        while ((len=reader.read(chars)) != -1) {
            temp = new String(chars, 0, len);
            if ((index = temp.indexOf("eof")) != -1) {
                sb.append(temp.substring(0, index));
                break;
            }
            sb.append(new String(chars, 0, len));
        }
        System.out.println("from server: " + sb);
        writer.close();
        reader.close();
        client.close();
    }

}



