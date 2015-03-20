# ![Logo](https://raw.githubusercontent.com/wiki/bodar/yatspec/logo.jpg) YatSpec #

This library replaces test tools like Concordion and Fit by allowing your tests to stay maintainable (i.e refactoring support in you favourite IDE) while still producing human readable documentation.

Currently this library supports 3 levels of usage:

  * TableRowTest support for JUnit Methods (like in MBUnit / NUnit)
  * Specification support for Junit. Produces readable [Html](https://github.com/bodar/yatspec/raw/wiki/example.html) from your [test](/test/com/googlecode/yatspec/junit/SpecificationExampleTest.java)
  * "Given / When / Then" support with automatic capturing and display of "givens" and captured inputs and output.

### Example ###

The simplest example would be to add the JUnit [@RunWith](http://junit.sourceforge.net/javadoc/org/junit/runner/RunWith.html) attribute specifying the [SpecRunner](/src/com/googlecode/yatspec/junit/SpecRunner.java) class to your Test class.

```
@RunWith(SpecRunner.class)
public class ExampleTest {
    @Test
    public void reallySimpleExample() throws Exception {
        assertThat("The quick brown fox".contains("fox"), is(true));
    }
}
```

If you ran this test you would see the following in STOUT:

```
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



---


Maven repo  => http://repo.bodar.com/


### Java Support ###
Version 1.1 requires Java 7 or higher. Version 217 is the last build that supports Java 6.
