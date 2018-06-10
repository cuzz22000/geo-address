package org.cjwilson.geoaddress.google;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

public class GeoCode<T extends GeoCodeLocation> {

  private String address;

  @SuppressWarnings("unchecked")
  private GeoCodeLocationFactory<T> locationFactory = (jsonObj) -> {
    final GeoCodeLocation location = new GeoCodeLocation() {

      @Override
      public JsonObject locationJsonObj() {
        return jsonObj;
      }

    };
    return (T) location;
  };

  private GeoCodeInputStream jsonInputStream = () -> {

    final String encAdd = URLEncoder.encode(address, "UTF-8");
    final String formatedURL =
        String.format("https://maps.googleapis.com/maps/api/geocode/json?&address=%s", encAdd);
    final URL httpsUrl = new URL(formatedURL);
    final HttpsURLConnection httpsConnection = (HttpsURLConnection) httpsUrl.openConnection();
    if (httpsConnection.getResponseCode() != 200) {
      return null;
    }
    return httpsConnection.getInputStream();

  };


  public GeoCode(final String address) {
    this.address = address;
  }

  public GeoCode<T> useLocationFactory(final GeoCodeLocationFactory<T> locationFactory) {
    this.locationFactory = locationFactory;
    return this;
  }

  public GeoCode<T> withEndpoint(final GeoCodeInputStream jsonInputStream) {
    this.jsonInputStream = jsonInputStream;
    return this;
  }

  public T locate() throws GeoCodeException {

    InputStream endPointInputStream;
    try {
      endPointInputStream = this.jsonInputStream.openInputStream();
    } catch (IOException e) {
      throw new GeoCodeException(e);
    }

    final JsonReader jsonReader = Json.createReader(endPointInputStream);
    final JsonObject jsonRoot = jsonReader.readObject();
    return this.locationFactory.build(jsonRoot);


  }

}
