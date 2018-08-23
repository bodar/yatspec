<h1><img src="https://raw.githubusercontent.com/wiki/bodar/yatspec/logo.jpg" align="absmiddle"/> YatSpec</h1>

YatSpec is a [BDD framework](https://en.wikipedia.org/wiki/Behavior-driven_development). YatSpec runs your JUnit tests and generates human-readable HTML reports.
 
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

### Basic Example ###

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

### Real Project Example ###

---


Maven repo  => http://repo.bodar.com/


### Java Support ###
Version 1.1 requires Java 7 or higher. Version 217 is the last build that supports Java 6.
