package com.labshigh.martinipool.core.inherits;

import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractRestService {

  protected URI makeUri(String host, String path, Object... args) {

    if (host == null) {
      throw new RuntimeException("%s : Service Instance NotFound.");
    }

    StringBuffer sbUri = new StringBuffer(host).append(path);
    for (int i = 0; i < args.length; i++) {
      sbUri.append(args[i]);
    }

    return this.makeUri(sbUri.toString());
  }

  protected URI makeUri(String host, String path) {

    if (host == null) {
      throw new RuntimeException("%s : Service Instance NotFound.");
    }

    return this.makeUri(host + path);
  }

  protected URI makeUri(String url) {
    try {
      return new URI(url);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
