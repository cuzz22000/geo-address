package org.cjwilson.geoaddress;

import org.cjwilson.geoaddress.google.GeoCodeLocation;

public interface GeoAddressLocation extends GeoCodeLocation {

  public String address();

  public String status();

  public Double lat();

  public Double lng();

}
