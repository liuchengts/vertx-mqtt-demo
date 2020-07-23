package com.pi.client.pi_client.communication;

import com.pi.client.pi_client.ApplicationContext;
import com.pi.client.pi_client.Config;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.ext.web.Router;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class SocketService {
  @Getter
  NetServer netServer;
  Config config;
  SelfSignedCertificate certificate = SelfSignedCertificate.create();

  public SocketService(ApplicationContext applicationContext) {
    this.config = applicationContext.getConfig();
    NetServerOptions options = new NetServerOptions()
      .setKeyCertOptions(certificate.keyCertOptions())
      .setTrustOptions(certificate.trustOptions())
      .setSsl(true);
//    .setClientAuth(ClientAuth.REQUIRED)
//      .setKeyStoreOptions(new JksOptions().
//        setPath("/path/to/your/server-keystore.jks").
//        setPassword("password-of-your-keystore")
//      );
    netServer = applicationContext.getVertx().createNetServer(options);
    netServer.listen(0, "localhost", res -> {
      if (res.succeeded()) {
        log.info("socket Server is now listening on actual port: " + netServer.actualPort());
      } else {
        log.info("Failed to bind!");
      }
    });
    netServer.connectHandler(socket -> {
      socket.handler(buffer -> {
        log.info("I received some bytes: " + buffer.toString());
        socket.write(buffer);
      });
      socket.closeHandler(v -> {
        log.info("The socket has been closed" + v);
      });
    });
  }

}
