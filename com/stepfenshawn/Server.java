import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static String WEB_ROOT = System.getProperty("user.dir") + "\\" + "WebContent";
    private static String url = "";
    private static Map<String, String> map = new HashMap<String, String>();

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(WEB_ROOT + "\\conf.properties"));
            Set set = prop.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                String key = (String)iter.next();
                String value = prop.getProperty(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serversocket;
        Socket socket = null;
        InputStream is = null;
        OutputStream ops = null;
        try {
            serversocket = new ServerSocket(8080);
            while (true) {
                socket = serversocket.accept();
                is = socket.getInputStream();
                ops = socket.getOutputStream();
                parse(is);
                if (url != null) {
                    if (url.indexOf(".") != -1) {
                        sendStaticResource(ops);
                    }
                    else {
                        sendDynamiResource(is, ops);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
                is = null;
            }
            if (ops != null) {
                ops.close();
                ops = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        }
    }
    private static void parse(InputStream is) throws Exception {
        StringBuffer content = new StringBuffer(2048);
        byte[] buffer = new byte[2048];
        int i = -1;
        i = is.read(buffer);
        for(int j = 0; j < i; j++) {
            content.append((char)buffer[j]);
        }
        ParseUrl(content.toString());
    }

    private static void ParseUrl(String content) {
        /* Get the res */
        int index1, index2;
        index1 = content.indexOf(" ");
        if (index1 != -1) {
            index2 = content.indexOf(" ", index1 + 1);
            if (index2 > index1) {
                url = content.substring(index1 + 2, index2);
            }
        }
    }

    private static void sendStaticResource(OutputStream ops) throws Exception {
        byte[] bytes = new byte[2048];
        FileInputStream fis = null;
        try {
            File file = new File(WEB_ROOT, url);
            if (file.exists()) {
                ops.write("HTTP/1.1 200 OK\n".getBytes());
                ops.write("Server:apache-Coyote/1.1\n".getBytes());
                ops.write("Content-type:text/html;charset=utf-8\n".getBytes());
                ops.write("\n".getBytes());
                fis = new FileInputStream(file);
                int ch = fis.read(bytes);
                while (ch != -1) {
                    ops.write(bytes, 0, ch);
                    ch = fis.read(bytes);
                }
            } else {
                ops.write("HTTP/1.1 404 not found\n".getBytes());
                ops.write("Server:apache-Coyote/1.1\n".getBytes());
                ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                ops.write("\n".getBytes());
                ops.write("File not found".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                fis.close();
                fis = null;
            }
        }
    }

    private static void sendDynamiResource(InputStream is, OutputStream ops) throws Exception {
        ops.write("HTTP/1.1 200 OK\n".getBytes());
        ops.write("Server:Apache\n".getBytes());
        ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
        ops.write("\n".getBytes());
        if (map.containsKey(url)) {
            String value = map.get(url);
            Class clazz = Class.forName(value);
            Servlet servlet = (Servlet)clazz.newInstance();
            servlet.init();
            servlet.Service(is, ops);
        }        
    }
}
