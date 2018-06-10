package org.cjwilson.geoaddress;

import javax.json.Json;
import javax.json.JsonObject;

public class JsonLocationBuilder {

  private GeoAddressLocation location;

  public static JsonLocationBuilder newJsonLocation() {
    return new JsonLocationBuilder();
  }

  public JsonLocationBuilder withLocation(final GeoAddressLocation location) {
    this.location = location;
    return this;
  }

  public JsonObject build() {
    final JsonObject latLng = Json.createObjectBuilder().add("lat", this.location.lat())
        .add("lng", this.location.lng()).build();
    return Json.createObjectBuilder().add("address", this.location.address())
        .add("status", this.location.status()).add("location", latLng).build();

  }

}
