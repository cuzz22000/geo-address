package org.cjwilson.geoaddress;

import javax.json.JsonObject;

public class GeoAddressLocationBuilder {


  private final JsonObject geoCodeLocation;
  private final JsonObject locationObj;
  private final String geoCodeStatus;
  private final String formattedAddress;
  private String address;
  private String status;


  public GeoAddressLocationBuilder(final JsonObject geoCodeLocation) {
    this.geoCodeLocation = geoCodeLocation;
    this.geoCodeStatus = this.geoCodeLocation.getString("status");
    this.locationObj = geoCodeStatus.equals("OK")
        ? geoCodeLocation.get("results").asJsonArray().get(0).asJsonObject().get("geometry")
            .asJsonObject().get("location").asJsonObject()
        : null;
    this.formattedAddress = geoCodeStatus.equals("OK") ? geoCodeLocation.get("results")
        .asJsonArray().get(0).asJsonObject().getString("formatted_address") : null;
  }

  public static GeoAddressLocationBuilder newBuilder(final JsonObject geoCodeLocation) {
    return new GeoAddressLocationBuilder(geoCodeLocation);
  }

  public GeoAddressLocationBuilder withStatus(final String status) {
    this.status = status;
    return this;
  }

  public GeoAddressLocationBuilder withAddress(final String address) {
    this.address = address;
    return this;
  }

  public GeoAddressLocation build() {
    if (this.formattedAddress == null && this.address == null) {
      throw new IllegalStateException("Addresses cannot be null");
    }
    return new GeoAddressLocation() {

      @Override
      public JsonObject locationJsonObj() {
        return this.locationJsonObj();
      }

      @Override
      public String status() {
        if (status != null) {
          return status;
        }
        if (geoCodeStatus.equals("OK")) {
          return "FOUND";
        } else if (geoCodeStatus.equals("OVER_QUERY_LIMIT")
            || geoCodeStatus.equals("UNKNOWN_ERROR")) {
          return geoCodeStatus;
        }
        return "NOT_FOUND";
      }

      @Override
      public Double lng() {
        return locationObj != null ? locationObj.getJsonNumber("lat").doubleValue() : 0d;
      }

      @Override
      public Double lat() {
        return locationObj != null ? locationObj.getJsonNumber("lng").doubleValue() : 0d;
      }

      @Override
      public String address() {
        return formattedAddress != null ? formattedAddress : address;
      }
    };
  }

}
