package com.googlecode.yatspec.plugin.jdom;

import org.hamcrest.*;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyIterable.emptyIterable;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.AllOf.allOf;
import static org.jdom.xpath.XPath.newInstance;

public final class HasXPath extends TypeSafeDiagnosingMatcher<Document> {
    private final XPath xpath;
    private final Matcher<Iterable<?>> valueMatcher;

    private HasXPath(String xpathExpression, Namespace ... namespaces) {
        this.xpath = xpath(xpathExpression, namespaces);
        this.valueMatcher = new ResultMatcher();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(format("an XML document with XPath %s", xpath.getXPath()));
    }

    @Override
    protected boolean matchesSafely(Document document, Description mismatchDescription) {
        List result = evaluate(xpath, document);
        mismatchDescription.appendText(format("xpath result %s", result));
        return valueMatcher.matches(result);
    }

    private static List evaluate(XPath xpath, Document document) {
        try {
            return xpath.selectNodes(document);
        } catch (JDOMException e) {
            return emptyList();
        }
    }

    private static XPath xpath(String xpath) {
        try {
            return newInstance(xpath);
        } catch (JDOMException e) {
            throw new IllegalStateException(e);
        }
    }

    private static XPath xpath(String xpath, Namespace ... namespaces) {
        XPath xPath = xpath(xpath);
        for (Namespace namespace : namespaces) {
            xPath.addNamespace(namespace);
        }
        return xPath;
    }

    @Factory
    public static Matcher<Document> hasXPath(String xpathExpression, Namespace... namespaces) {
        return new HasXPath(xpathExpression, namespaces);
    }

    private static final class ResultMatcher extends TypeSafeMatcher<Iterable<?>> {
        private static final Object XPATH_FUNCTION_UNSUCCESSFUL = Boolean.FALSE;

        @Override
        protected boolean matchesSafely(Iterable<?> result) {
            return allOf(not(emptyIterable()), not(contains(XPATH_FUNCTION_UNSUCCESSFUL))).matches(result);
        }

        @Override
        public void describeTo(Description description) {
        }
    }
}