package org.cjwilson.geoaddress.google;

import javax.json.JsonObject;

public interface GeoCodeLocationFactory<T> {

  public T build(JsonObject status);

}
