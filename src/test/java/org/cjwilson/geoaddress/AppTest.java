package org.cjwilson.geoaddress;

import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {


  @Test
  @Ignore
  public void appAddressTest() {
    App.main(new String[] {"-a", ADDRESS});
    Assert.assertTrue(true);
  }

  @Test
  @Ignore
  public void appAddressesTest() {
    final URL addresses = this.getClass().getResource("/addresses.txt");
    System.out.println(addresses.toString());
    System.out.println(addresses.getPath());
    App.main(new String[] {"-f", addresses.getPath()});
    Assert.assertTrue(true);

  }

  @Test
  public void testAppUsage() {
    App.main(new String[] {});
    Assert.assertTrue(true);
  }

  private final String ADDRESS = "777 Brockton Avenue, Abington MA 2351";
}
