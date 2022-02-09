import java.io.*;

public class Aservlet implements Servlet {
    @Override
    public void init() {
        System.out.println("Cantonese...init");
    }

    @Override
    public void Service(InputStream is, OutputStream ops) throws Exception {
        ops.write("I am A Servlet!".getBytes());
        ops.flush();
    }

    @Override
    public void destroy() {

    }
}
