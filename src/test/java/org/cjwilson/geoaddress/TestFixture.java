package org.cjwilson.geoaddress;

import javax.json.JsonObject;

public interface TestFixture {
  public static final GeoAddressLocation LOCATION = new GeoAddressLocation() {

    @Override
    public String address() {
      return "address";
    }

    @Override
    public String status() {
      return "found";
    }

    @Override
    public Double lat() {
      return 12345.67;
    }

    @Override
    public Double lng() {
      return 12345.67;
    }

    @Override
    public JsonObject locationJsonObj() {
      return null;
    }

  };
}

