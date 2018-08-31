<h1><img src="https://raw.githubusercontent.com/wiki/bodar/yatspec/logo.jpg" align="absmiddle"/> YatSpec</h1>

YatSpec is a [BDD test framework](https://en.wikipedia.org/wiki/Behavior-driven_development). YatSpec runs your JUnit tests and generates human-readable HTML reports.
 
YatSpec replaces test tools like Concordion and Fit by allowing your tests to stay maintainable (i.e refactoring support 
in you favourite IDE) while still producing human readable documentation.
If you are doing [three amigos](https://www.agilealliance.org/glossary/three-amigos/) and only developers modify the 
tests there is no need to maintain a set of HTML or text specification files, just use pure Java to write your tests and use YatSpec to generate the HTML report.

Alternative tools:
* [Cucumber](https://cucumber.io/) - YatSpec is different because you write your tests in pure Java and JUnit not in plain text .feature files.
* [Concordion](https://concordion.org/) - YatSpec is different because you write your tests in pure Java and JUnit instead of HTML. 
* [Fit](http://fit.c2.com/) - YatSpec is different because you write your tests in pure Java and JUnit instead of HTML.  

Currently this library supports several features:
  * Generate [HTML](https://github.com/bodar/yatspec/raw/wiki/example.html) based on a [JUnit Java](/test/com/googlecode/yatspec/junit/SpecificationExampleTest.java) file
  * BDD "Given / When / Then" support with automatic capturing and display of "givens" and captured inputs and output.
  * Tabular data tests by using [@TableRunner](src/com/googlecode/yatspec/junit/TableRunner.java) 
  with [@Table](src/com/googlecode/yatspec/junit/Table.java) and [@Row](src/com/googlecode/yatspec/junit/Row.java) support for JUnit Methods (similar to in MBUnit / NUnit)
  * [@Notes](src/com/googlecode/yatspec/junit/Notes.java) to add comments to tests
  * [@LinkingNote](/src/com/googlecode/yatspec/junit/LinkingNote.java) to [generate links between tests](https://www.youtube.com/watch?v=CFMkD-t363c)  

### Quick Start ###

The simplest example would be to add the JUnit [@RunWith](http://junit.sourceforge.net/javadoc/org/junit/runner/RunWith.html) attribute specifying the [SpecRunner](/src/com/googlecode/yatspec/junit/SpecRunner.java) class to your Test class.

```java
@RunWith(SpecRunner.class)
public class ExampleTest {
    @Test
    public void reallySimpleExample() throws Exception {
        assertThat("The quick brown fox".contains("fox"), is(true));
    }
}
```

If you ran this test you would see the following in standard output (console):

```
(...)
Html output:
/tmp/com/googlecode/yatspec/junit/ExampleTest.html
```

If you opened the HTML file you would see:

![Screenshot](https://raw.githubusercontent.com/wiki/bodar/yatspec/yatspec.png)

So lets quickly explain what has happened:

  * The fully qualified class name is turned into the directory structure and filename. i.e. `com.company.ExampleTest` -> `com/company/ExampleTest.html`
    * The root output folder can be [configured](Configuration.md)
  * A table of contents is generated for every test method
  * The method body is interpreted into a text specification
  * Tests are colour according to the following scheme
    * Green == Passed
    * Red == Failed
    * Orange == Not Run

### YatSpec Tutorial ###

Let us have a look at an example application that connects to a third party API to fetch data and process it.
In this example, Weather Application connects DarkSky APIs to fetch the weather forecast and extract the current wind speed in London.
We will test that Weather Application API using YatSpec. 

The whole example is available at https://github.com/wojciechbulaty/examples/tree/master/weather-yatspec-example

##### YatSpec Tutorial Step 0: Set up the project #####
Start with adding YatSpec to your project. If you use maven, add a dependency to `pom.xml`:
````xml
<dependency>
    <groupId>com.googlecode.yatspec</groupId>
    <artifactId>yatspec</artifactId>
    <version>1.27</version>
    <scope>test</scope>
</dependency>
````
You will also need a repository in your `pom.xml`:
````xml
<repositories>
    <repository>
        <id>http://repo.bodar.com/</id>
        <url>http://repo.bodar.com/</url>
    </repository>
</repositories>
````

##### YatSpec Tutorial Step 1: Create a test #####
Create your first YatSpec test. Its just a JUnit test written in a BDD-style. The YatSpec `SpecRunner` will pick it up and generate a HTML output based on the test method body.
````java
@RunWith(SpecRunner.class)
public class WeatherApplicationTest extends TestState {
    private final WeatherApplication weatherApplication = new WeatherApplication();

    @Rule
    public WireMockRule darkSkyAPIStub = new WireMockRule();

    private HttpResponse httpResponse;
    private String responseBody;

    @Before
    public void setUp() {
        weatherApplication.start();
    }

    @After
    public void tearDown() {
        weatherApplication.stop();
    }

    @Test
    public void servesWindSpeedBasedOnDarkSkyResponse() throws IOException {
        givenDarkSkyForecastForLondonContainsWindSpeed("12.34");
        whenIRequestForecast();
        thenTheWindSpeedIs("12.34mph");
    }

    private void whenIRequestForecast() throws IOException {
        Request get = Request.Get("http://localhost:" + weatherApplication.port() + "/wind-speed");
        Response response = get.execute();
        httpResponse = response.returnResponse();
        responseBody = EntityUtils.toString(httpResponse.getEntity());
    }

    private void thenTheWindSpeedIs(String expected) throws IOException {
        assertEquals(expected, responseBody);
    }

    private void givenDarkSkyForecastForLondonContainsWindSpeed(String windSpeed) throws IOException {
        darkSkyAPIStub.stubFor(get(urlEqualTo("/forecast/e67b0e3784104669340c3cb089412b67/51.507253,-0.127755"))
                .willReturn(aResponse().withBody(darkSkyResponseBody(windSpeed))));
    }

    private String darkSkyResponseBody(String windSpeed) throws IOException {
        return format(IOUtils.toString(getClass().getClassLoader().getResourceAsStream("darksky-response-body.json")), windSpeed);
    }
}
````
##### YatSpec Tutorial Step 2: Run the test and inspect the HTML output #####
Run the test (in IntelliJ or Eclipse) and inspect the standard output.
It will point you to the location of the HTML report
````
(...)
Yatspec output:
C:\Users\Wojtek\AppData\Local\Temp\com\wbsoftwareconsutlancy\WeatherApplicationTest.html
```` 
Open that file in a browser and you will see the test results.
![Screenshot](https://raw.githubusercontent.com/wiki/bodar/yatspec/tutorial/yatspec_tutorial_basic_test.PNG)

##### YatSpec Tutorial Step 3: Storing test state #####
As you can see in the example test, if you would like to store the result of a "when" method and save it to be asserted on in the "then"
method, the recommended way is to use a class attribute, for example `httpResponse` or `responseBody` in this case.

If your tests became complex and you have many scenarios per tests class, you might end up having many class attributes used by different tests.
In that case, we recommend splitting the test class into multiple test classes. That should result in easier to follow test scenarios.
##### YatSpec Tutorial Step 4: Use captured inputs and outputs #####
You can use the `log(String, Object)` method to add request and response payloads to "captured inputs and outputs", which will result in adding them the HTML test report.
For example, you can save the request and response body by calling the `log` method:
```java
private void whenIRequestForecast() throws IOException {
    Request get = Request.Get("http://localhost:" + weatherApplication.port() + "/wind-speed");
    log("Request from client to " + WEATHER_APPLICATION, get);
    Response response = get.execute();
    httpResponse = response.returnResponse();
    responseBody = EntityUtils.toString(httpResponse.getEntity());
    log("Response from " + WEATHER_APPLICATION + " to client", toString(httpResponse, responseBody));
}
```
And they will show in the generated HTML report:
![Screenshot](https://raw.githubusercontent.com/wiki/bodar/yatspec/tutorial/yatspec_tutorial_captured_inputs_outputs.PNG)

##### YatSpec Tutorial Step 5: Use interesting givens #####
Once you have used the `log` method to save requests and responses in the captured inputs and outputs, you can use `interestingGivens`
to highlight interesting bits in those payloads, in out case it will be the wind speed:
````java
private void givenDarkSkyForecastForLondonContainsWindSpeed(String windSpeed) throws IOException {
    interestingGivens.add("Wind speed", windSpeed);
    darkSkyAPIStub.stubFor(get(urlEqualTo("/forecast/e67b0e3784104669340c3cb089412b67/51.507253,-0.127755"))
            .willReturn(aResponse().withBody(darkSkyResponseBody(windSpeed))));
}
````
That results it highlighting it in yellow on the HTML report:
![Screenshot](https://raw.githubusercontent.com/wiki/bodar/yatspec/tutorial/yatspec_tutorial_interesting_givens.PNG)

##### YatSpec Tutorial Step 6: Generate a sequence diagram #####
We can now proceed to generate a sequence diagram that will visualise how.
It is an especially useful technique in microservice architectures where there are many components communicating with each other.
You do it by adding some extra code, so that the whole test looks like this:
````java
@RunWith(SpecRunner.class)
public class WeatherApplicationTest extends TestState implements WithCustomResultListeners {
    private static final String WEATHER_APPLICATION = "WeatherApplication";

    private final WeatherApplication weatherApplication = new WeatherApplication();

    @Rule
    public WireMockRule darkSkyAPIStub = new WireMockRule();

    private HttpResponse httpResponse;
    private String responseBody;

    @Before
    public void setUp() {
        weatherApplication.start();
        darkSkyAPIStub.addMockServiceRequestListener(new LogWiremockInYatspecRequest(this, WEATHER_APPLICATION, "DarkSky"));
    }

    @After
    public void tearDown() {
        weatherApplication.stop();
        addSequenceDiagram();
    }

    @Test
    public void servesWindSpeedBasedOnDarkSkyResponse() throws IOException {
        givenDarkSkyForecastForLondonContainsWindSpeed("12.34");
        whenIRequestForecast();
        thenTheWindSpeedIs("12.34mph");
    }

    private void addSequenceDiagram() {
        super.log("Sequence diagram", new SequenceDiagramGenerator()
                .generateSequenceDiagram(new ByNamingConventionMessageProducer().messages(capturedInputAndOutputs)));
    }

    private void whenIRequestForecast() throws IOException {
        Request get = Request.Get("http://localhost:" + weatherApplication.port() + "/wind-speed");
        log("Request from client to " + WEATHER_APPLICATION, get);
        Response response = get.execute();
        httpResponse = response.returnResponse();
        responseBody = EntityUtils.toString(httpResponse.getEntity());
        log("Response from " + WEATHER_APPLICATION + " to client", toString(httpResponse, responseBody));
    }

    private String toString(HttpResponse response, String responseBody) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("HTTP").append(" ").append(response.getStatusLine().getStatusCode()).append("\n");
        if (response.getAllHeaders() != null) {
            Arrays.stream(response.getAllHeaders()).forEach(h -> result.append(h.getName()).append(": ").append(h.getValue()).append("\n"));
        }
        result.append("\n").append("\n").append(responseBody);
        return result.toString();
    }

    private void thenTheWindSpeedIs(String expected) throws IOException {
        assertEquals(expected, responseBody);
    }

    private void givenDarkSkyForecastForLondonContainsWindSpeed(String windSpeed) throws IOException {
        interestingGivens.add("Wind speed", windSpeed);
        darkSkyAPIStub.stubFor(get(urlEqualTo("/forecast/e67b0e3784104669340c3cb089412b67/51.507253,-0.127755"))
                .willReturn(aResponse().withBody(darkSkyResponseBody(windSpeed))));
    }

    private String darkSkyResponseBody(String windSpeed) throws IOException {
        return format(IOUtils.toString(getClass().getClassLoader().getResourceAsStream("darksky-response-body.json")), windSpeed);
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return ImmutableSet.of(new HtmlResultRenderer().withCustomHeaderContent(getHeaderContentForModalWindows()).withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()));
    }
}
````
You can actually click on the arrows and see the payloads.
![Screenshot](https://raw.githubusercontent.com/wiki/bodar/yatspec/tutorial/yatspec_tutorial_sequence_diagram.PNG)

##### YatSpec Tutorial Step 7: Sad path test #####
Quite often you will want to also test more than one scenario per test. For example, a hypothetitcal situation where you wanna 
see what happens if the third party returns an error:
````java
@Test
public void reportsErrorWhenDarkSkyReturnsANonSuccessfulResponse() throws IOException {
    givenDarkSkyReturnsAnError("500");
    whenIRequestForecast();
    thenTheResponseContains("Error while fetching data from DarkSky APIs");
}
````
![Screenshot](https://raw.githubusercontent.com/wiki/bodar/yatspec/tutorial/yatspec_tutorial_sad_path.PNG)
##### YatSpec Tutorial Step 8: Use notes #####
You can use a @Notes or a [@LinkingNote](https://www.youtube.com/watch?v=CFMkD-t363c) to add notes to the test output.
````java
@Test
@Notes("The DarkSky response is quite big and complex, out weather application extracts one attribute from it")
public void servesWindSpeedBasedOnDarkSkyResponse() throws IOException {
    givenDarkSkyForecastForLondonContainsWindSpeed("12.34");
    whenIRequestForecast();
    thenTheWindSpeedIs("12.34mph");
}
````
##### YatSpec Tutorial Step 9: Create a table test #####
Sometimes you would like to test the same scenario for with different types test data. You can create a table test for that:
````java
@Test
@Table({@Row("500"), @Row("503")})
public void reportsErrorWhenDarkSkyReturnsANonSuccessfulResponse(String darkSkyResponseCode) throws IOException {
    givenDarkSkyReturnsAnError(darkSkyResponseCode);
    whenIRequestForecast();
    thenTheResponseContains("Error while fetching data from DarkSky APIs");
}
```` 
##### YatSpec Tutorial Step 10: Tutorial code #####
The whole example is available at https://github.com/wojciechbulaty/examples/tree/master/weather-yatspec-example 

---

Maven repo  => http://repo.bodar.com/

### Java Support ###
Version 1.1 requires Java 7 or higher. Version 217 is the last build that supports Java 6.
