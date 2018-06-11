## Geo Address

Exercise creating an API for looking up a street address's geo location using [Googles GeoCode API](https://developers.google.com/maps/documentation/geocoding/intro). 

### Usage

```bash
Usage:
	-h , --help : this munu
	-a , --address : address to lookup
	-f , --file: file path of addresses
```

The implemented `main` method accepts arguments with the following format `"1600 Amphitheatre Parkway, Mountain View, CA"` in singular form or a line delimited list file format.

eg:

```text
777 Brockton Avenue, Abington MA 2351
30 Memorial Drive, Avon MA 2322
250 Hartford Avenue, Bellingham MA 2019
```

### Building

Maven is being utilized as the build tool with minimum dependancies.

```bash
$> mvn package

```

### Running 

The maven build created a runnable jar without dependancies. However dependancies were copied as part of the build to `${project.build.directory}/libs` and referenced in the jars `META-INF/MANIFEST.MF`file.

```bash
# Usage
$> java -jar target/geo-address-0.0.1.jar
Usage:
	-h , --help : this munu
	-a , --address : address to lookup
	-f , --file: file path of addresses

# Lookup address 
$> java -jar target/geo-address-0.0.1.jar -a "777 Brockton Avenue, Abington MA 2351"
{"address":"777 Brockton Avenue, Abington MA 2351","status":"FOUND","location":{"lat":-70.9686115,"lng":42.0963462}}

# List of address
$> java -jar target/geo-address-0.0.1.jar -f "path to file"

....

$>

```

### Test Cases

There is fairly good coverage, of course there can alway be more. Special note should be taken the most tests in `org.cjwilson.geoaddress.AppTest` are being ignored. As these are live tests calling the actual service. Future version may introduce Mokito for better coverage.

### Annoyances and Shortcomings

#### Limits

Although the GeoCode api usage limit documents [2,500 requests per day](https://developers.google.com/maps/documentation/geocoding/usage-limits) the service consistently reports its status as `OVER_QUERY_LIMIT` when it is not. This is handled by trapping the status and sleeping for three seconds, resetting the count and repeating the request. One potential issue with this strategy is if the service is in fact `OVER_QUERY_LIMIT` the application will loop gracefully FOREVER!

#### Multi-Threading

Of course there are always some advanced functionality and implementing multi threaded requests is on the list. The application currently runs synchronously, for better performance an asynchronous approach would improve execution time.

#### API Key

There was consideration to include a switch in the `GeoCode` api to include a Google API key. Although one was available the functionality was NOT implemented.. Perhaps on the next version. :smirk:

#### Address Resolution
It was observed that the service will respond with a status of "OK" and provide a geo location if the address is wrong. The service apparently, once the address is parsed, will return results for any segment of the address that is resolvable. For instance if the street address is incorrect is will revert to the City and Zip. If any of the those two segments are wrong it will return results for one of those two. Only when all segments of the request are unresolvable will it return zero results.

Example of Not Found:

```
$> java -jar target/geo-address-0.0.1.jar -a "1313 Mockingbird Lane, The Moon MA 2350"
{"address":"1313 Mockingbird Lane, The Moon MA 2350","status":"NOT_FOUND","location":{"lat":0.0,"lng":0.0}}
```

