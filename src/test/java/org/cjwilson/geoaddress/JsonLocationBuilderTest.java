package org.cjwilson.geoaddress;

import java.util.Scanner;

import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Test;

public class JsonLocationBuilderTest implements TestFixture {

  @Test
  public void testBuild() {

    final JsonLocationBuilder jsonLocationBuilder =
        JsonLocationBuilder.newJsonLocation().withLocation(LOCATION);
    final JsonObject actual = jsonLocationBuilder.build();
    final Scanner expectedJson =
        new Scanner(this.getClass().getResourceAsStream("/json-location.json"));
    final StringBuilder expected = new StringBuilder();
    while (expectedJson.hasNext()) {
      expected.append(expectedJson.nextLine());
    }
    expectedJson.close();

    Assert.assertEquals(actual.toString(), expected.toString());
  }

}


