import java.io.*;

public interface Servlet {
    public void init();
    public void Service(InputStream is, OutputStream ops) throws Exception;
    public void destroy();
}
