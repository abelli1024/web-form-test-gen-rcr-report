package mo.must.chat.common.util;


import mo.must.chat.common.framework.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class SystemUtil {
    public static long getId() {
        return SnowflakeIdWorker.generateId();
    }
}
