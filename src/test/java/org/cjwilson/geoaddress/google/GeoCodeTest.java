package org.cjwilson.geoaddress.google;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;

public class GeoCodeTest {

  private GeoCodeInputStream geoCodeInputStream = () -> {
    return this.getClass().getResourceAsStream("/actual-result.json");
  };

  @Test
  public void testResolveLocation() throws GeoCodeException {

    final GeoCodeLocationFactory<GeoCodeLocation> geoCodeFactory = (jsonObj) -> {
      return new GeoCodeLocation() {

        @Override
        public JsonObject locationJsonObj() {
          return jsonObj;
        }
      };
    };

    final GeoCode<GeoCodeLocation> geoCode =
        new GeoCode<GeoCodeLocation>("mocked location").useLocationFactory(geoCodeFactory);
    geoCode.withEndpoint(geoCodeInputStream);
    final GeoCodeLocation geoCodeLocation = geoCode.locate();
    final JsonObject expectedJson =
        Json.createReader(this.getClass().getResourceAsStream("/actual-result.json")).readObject();
    final String actual = geoCodeLocation.locationJsonObj().toString();;
    Assert.assertEquals(actual, expectedJson.toString());
  }

  @Test
  public void testResolveLocationIOException() throws GeoCodeException {
    try {
      final GeoCode<GeoCodeLocation> geoCode = new GeoCode<>("mocked location");
      GeoCodeInputStream nullGeoCodeInputStream = () -> {
        throw new IOException();
      };
      geoCode.withEndpoint(nullGeoCodeInputStream);
      geoCode.locate();
    } catch (GeoCodeException e) {
      Assert.assertTrue(e.getCause() instanceof IOException);
    }

  }


}
