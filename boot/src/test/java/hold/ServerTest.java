package hold;

import com.essaid.jg.boot.JGBootSettings;
import com.essaid.jg.boot.JGServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServerTest {

    @Autowired
    JGServer server;

    @Autowired
    JGBootSettings serverConfig;

    @Test
    void runServer(){
        server.start().join();
    }

    @Test
    void viewSettings(){
        System.out.println(serverConfig);
    }


}
