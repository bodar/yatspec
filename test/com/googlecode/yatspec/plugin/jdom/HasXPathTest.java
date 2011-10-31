package com.googlecode.yatspec.plugin.jdom;

import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static com.googlecode.yatspec.plugin.jdom.HasXPath.hasXPath;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jdom.Namespace.getNamespace;

public class HasXPathTest {
    private static final Namespace NAMESPACE = getNamespace("ns", "http://localhost");
    private static final String NAMESPACED_XML = "" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<ns:ParentElement xmlns:ns=\"http://localhost\">\n" +
            "    <ns:ParentElementHeader operatorId=\"operator\" operatorTransactionId=\"1234\"\n" +
            "                                  operatorIssuedDate=\"2005-01-01T00:00:02\"/>\n" +
            "    <ns:ParentElementBody>\n" +
            "        <ns:ChildElement attribute=\"provide\" addressReference=\"af12b345\"\n" +
            "                               homeWiringSolution=\"No OR Order OR Authorize\" managedInstallModules=\"BMwin/BMmac,X360,Wii\">\n" +
            "            <ns:Appointment date=\"2005-01-01T09:00:00\" timeSlot=\"am\"/>\n" +
            "        </ns:ChildElement>\n" +
            "    </ns:ParentElementBody>\n" +
            "</ns:ParentElement>";
    private static final String NO_NAMESPACE_XML = "" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<ParentElement>" +
            "    <ParentElementHeader operatorId=\"operator\" operatorTransactionId=\"1234\"\n" +
            "                                  operatorIssuedDate=\"2005-01-01T00:00:02\"/>\n" +
            "    <ParentElementBody>\n" +
            "        <ChildElement attribute=\"provide\" addressReference=\"af12b345\"\n" +
            "                               homeWiringSolution=\"No OR Order OR Authorize\" managedInstallModules=\"BMwin/BMmac,X360,Wii\">\n" +
            "            <Appointment date=\"2005-01-01T09:00:00\" timeSlot=\"am\"/>\n" +
            "        </ChildElement>\n" +
            "    </ParentElementBody>\n" +
            "</ParentElement>";


    @Test
    public void shouldMatchNamespacedAttribute() throws Exception {
        assertMatches(format("//ns:ChildElement[@attribute='%s']", "provide"), NAMESPACED_XML, true, NAMESPACE);
    }

    @Test
    public void shouldMatchNamespacedElement() throws Exception {
        assertMatches("//ns:ChildElement", NAMESPACED_XML, true, NAMESPACE);
    }

    @Test
    public void shouldNotMatchNamespacedAttribute() throws Exception {
        assertMatches(format("not(//ns:ChildElement[@attribute='%s'])", "provide"), NAMESPACED_XML, false, NAMESPACE);
    }

    @Test
    public void shouldNotMatchNamespacedElement() throws Exception {
        assertMatches("not(//ns:ChildElement)", NAMESPACED_XML, false, NAMESPACE);
    }

    @Test
    public void shouldNotMatchNamespacedNonExistentAttribute() throws Exception {
        assertMatches("/ns:ParentElement/@doesNotExist", NAMESPACED_XML, false, NAMESPACE);
    }

    @Test
    public void shouldNotMatchNamespacedNonExistentElement() throws Exception {
        assertMatches("//ns:ChildElement/doesNotExist", NAMESPACED_XML, false, NAMESPACE);
    }

    @Test
    public void shouldMatchNamespacedFunction() throws Exception {
        assertMatches("count(//ns:ChildElement) = 1", NAMESPACED_XML, true, NAMESPACE);
    }

    @Test
    public void shouldNotMatchNamespacedFunction() throws Exception {
        assertMatches("count(//ns:ChildElement) = 2", NAMESPACED_XML, false, NAMESPACE);
    }

    @Test
    public void shouldMatchNamespacedCustomMatcher() throws Exception {
        assertMatches("count(//ns:ChildElement) = 1", NAMESPACED_XML, true, NAMESPACE);
    }

    @Test
    public void shouldNotMatchNamespacedCustomMatcher() throws Exception {

    }

    @Test
    public void shouldMatchAttribute() throws Exception {
        assertMatches(format("//ChildElement[@attribute='%s']", "provide"), NO_NAMESPACE_XML, true);
    }

    @Test
    public void shouldMatchElement() throws Exception {
        assertMatches("//ChildElement", NO_NAMESPACE_XML, true);
    }

    @Test
    public void shouldNotMatchAttribute() throws Exception {
        assertMatches(format("not(//ChildElement[@attribute='%s'])", "provide"), NO_NAMESPACE_XML, false);
    }

    @Test
    public void shouldNotMatchElement() throws Exception {
        assertMatches("not(//ChildElement)", NO_NAMESPACE_XML, false);
    }

    @Test
    public void shouldNotMatchNonExistentAttribute() throws Exception {
        assertMatches("/ParentElement/@doesNotExist", NO_NAMESPACE_XML, false);
    }

    @Test
    public void shouldNotMatchNonExistentElement() throws Exception {
        assertMatches("//ChildElement/doesNotExist", NO_NAMESPACE_XML, false);
    }

    @Test
    public void shouldMatchFunction() throws Exception {
        assertMatches("count(//ChildElement) = 1", NO_NAMESPACE_XML, true);
    }
    
    @Test
    public void shouldNotMatchFunction() throws Exception {
        assertMatches("count(//ChildElement) = 2", NO_NAMESPACE_XML, false);
    }

    private static void assertMatches(String xpathQuery, String xml, boolean expected, Namespace... namespaces) throws Exception {
        assertThat(hasXPath(xpathQuery, namespaces).matches(toDocument(xml)), is(expected));
    }

    private static Document toDocument(String xml) throws Exception {
        return new SAXBuilder().build(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    }
}
