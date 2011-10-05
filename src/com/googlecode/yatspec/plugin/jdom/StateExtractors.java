package com.googlecode.yatspec.plugin.jdom;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.jdom.xpath.XPath.newInstance;

public class StateExtractors {
    private StateExtractors() {
    }

    public static <T> StateExtractor<T> getValue(final String key, final Class<T> klazz) {
        return new StateExtractor<T>() {
            public T execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return capturedInputAndOutputs.getType(key, klazz);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <I extends Iterable<T>, T> StateExtractor<I> getValues(final String key, final Class<T> klazz) {
        return new StateExtractor<I>() {
            public I execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return (I) capturedInputAndOutputs.getType(key, Iterable.class);
            }
        };
    }

    public static StateExtractor<String> getXpathValue(final String key, final String xpath) {
        return new StateExtractor<String>() {
            public String execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return getXpathValues(capturedInputAndOutputs, key, xpath).get(0);
            }
        };
    }

    public static StateExtractor<List<String>> getXpathValues(final String key, final String xpath) {
        return new StateExtractor<List<String>>() {
            public List<String> execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return getXpathValues(capturedInputAndOutputs, key, xpath);
            }
        };
    }

    private static List<String> getXpathValues(CapturedInputAndOutputs capturedInputAndOutputs, String key, String xpath) throws Exception {
        return sequence(getDocument(capturedInputAndOutputs, key)).map(toAttributeOrElementValues(xpath)).head();
    }

    private static Document getDocument(CapturedInputAndOutputs capturedInputAndOutputs, String key) throws Exception {
        return getValue(key, Document.class).execute(capturedInputAndOutputs);
    }

    @SuppressWarnings("unchecked")
    private static Callable1<? super Document, List<String>> toAttributeOrElementValues(final String xpath) {
        return new Callable1<Document, List<String>>() {
            public List<String> call(Document document) throws Exception {
                List result = newInstance(xpath).selectNodes(document);
                checkThatValuesAreFound(result, xpath);
                return sequence(result).map(toAttributeOrElement()).toList();
            }
        };
    }

    private static void checkThatValuesAreFound(List result, String xpath) {
        if (result == null || result.isEmpty()) {
            throw new IllegalStateException("Result not found at " + xpath);
        }
    }


    private static Callable1<Object, String> toAttributeOrElement() {
        return new Callable1<Object, String>() {
            public String call(Object result) throws Exception {
                return result instanceof Element ? ((Element) result).getText() : ((Attribute) result).getValue();
            }
        };
    }
}
