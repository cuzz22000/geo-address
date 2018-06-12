package org.cjwilson.geoaddress;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import org.cjwilson.geoaddress.google.GeoCode;
import org.cjwilson.geoaddress.google.GeoCodeException;
import org.cjwilson.geoaddress.google.GeoCodeLocationFactory;

/**
 * Hello world!
 *
 */
public class App {


  public static void main(String[] args) {

    if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
      System.out.println(usage());
    } else if (args[0].equals("--file") || args[0].equals("-f")) {

      try {
        final FileInputStream fios = new FileInputStream(args[1]);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(fios));
        final JsonLocationArrayBuilder jsonLocationArrayBuilder =
            JsonLocationArrayBuilder.newJsonLocactionArray();
        reader.lines().forEach(address -> {
          try {
            final Optional<GeoAddressLocation> locationOpt = resolveAddress(address);
            if (locationOpt.isPresent()) {
              jsonLocationArrayBuilder.addLocation(locationOpt.get());
            }
          } catch (GeoCodeException e) {
            e.printStackTrace();
            System.exit(1);
          }
        });
        reader.close();
        System.out.println(jsonLocationArrayBuilder.build().toString());
      } catch (FileNotFoundException e) {
        System.out.printf("File %s not found", args[1]);
        System.exit(1);
      } catch (IOException e) {
        System.out.println("Application IO ERROR");
        e.printStackTrace();
        System.exit(1);
      }

    } else if (args[0].equals("--address") || args[0].equals("-a")) {
      final String address = args[1];
      try {
        final Optional<GeoAddressLocation> locationOpt = resolveAddress(address);
        if (locationOpt.isPresent()) {
          System.out.println(JsonLocationBuilder.newJsonLocation().withLocation(locationOpt.get())
              .build().toString());
        }
      } catch (GeoCodeException e) {
        e.printStackTrace();
        System.exit(1);
      }
    } else {
      System.out.println(usage());
    }

  }

  private static String usage() {
    return new StringBuilder("Usage:\n").append("\t-h , --help : this munu\n")
        .append("\t-a , --address : address to lookup\n")
        .append("\t-f , --file: file path of addresses\n").toString();
  }

  private static Optional<GeoAddressLocation> resolveAddress(String address)
      throws GeoCodeException {

    final GeoCodeLocationFactory<GeoAddressLocation> locationFactory = (jsonObj) -> {
      return GeoAddressLocationBuilder.newBuilder(jsonObj).withAddress(address).build();

    };

    final GeoCode<GeoAddressLocation> geoCode =
        new GeoCode<GeoAddressLocation>(address).useLocationFactory(locationFactory);
    for (int i = 0; i < 5; i++) {
      final GeoAddressLocation location = geoCode.locate();
      if (!location.status().equals("FOUND") && !location.status().equals("NOT_FOUND")) {
        System.out.printf("Attempt %d resolving address %s failed with status %s... retrying\n",
            i + 1, address, location.status());
        if (i + 1 == 5) {
          return Optional.of(GeoAddressLocationBuilder.newBuilder(location.locationJsonObj())
              .withAddress(location.address()).withStatus("NOT_FOUND").build());
        } else if (location.status().equals("OVER_QUERY_LIMIT")) {
          System.out.println("Over Query Limit resetting count.. sleeping it off!");
          i = 0;
          try {
            Thread.sleep(3000);
          } catch (InterruptedException e) {
            // not gonna happen
          }
        }
        continue;
      }
      return Optional.of(location);
    }
    return Optional.empty();
  }

}
