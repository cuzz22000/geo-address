package org.cjwilson.geoaddress;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class JsonLocationArrayBuilder {

  private List<JsonObject> locations;

  public static JsonLocationArrayBuilder newJsonLocactionArray() {
    return new JsonLocationArrayBuilder();
  }

  public JsonLocationArrayBuilder addLocation(final GeoAddressLocation location) {
    if (locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(JsonLocationBuilder.newJsonLocation().withLocation(location).build());
    return this;
  }

  public JsonArray build() {
    final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    this.locations.forEach(l -> {
      arrayBuilder.add(l);
    });
    return arrayBuilder.build();
  }

}
