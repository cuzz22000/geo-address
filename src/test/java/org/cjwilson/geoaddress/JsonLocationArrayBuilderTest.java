package org.cjwilson.geoaddress;

import java.util.Scanner;

import javax.json.JsonArray;

import org.junit.Assert;
import org.junit.Test;

public class JsonLocationArrayBuilderTest implements TestFixture {

  @Test
  public void testBuild() {

    final JsonLocationArrayBuilder jsonLocationArrayBuilder = JsonLocationArrayBuilder
        .newJsonLocactionArray().addLocation(LOCATION).addLocation(LOCATION);
    final JsonArray actual = jsonLocationArrayBuilder.build();
    final Scanner expectedJson =
        new Scanner(this.getClass().getResourceAsStream("/json-location-array.json"));
    final StringBuilder expected = new StringBuilder();
    while (expectedJson.hasNext()) {
      expected.append(expectedJson.nextLine());
    }
    expectedJson.close();
    Assert.assertEquals(actual.toString(), expected.toString());
  }

}
