package org.cjwilson.geoaddress.google;

import java.io.IOException;
import java.io.InputStream;

public interface GeoCodeInputStream {

  public InputStream openInputStream() throws IOException;

}
