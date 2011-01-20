package com.googlecode.yatspec.state.givenwhenthen;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

public final class StateExtractors {

    private StateExtractors() {}

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
                return (I) capturedInputAndOutputs.get(key);
            }
        };
    }

    public static StateExtractor<String> getXpathValue(final String key, final String xpath) {
        return new StateExtractor<String>() {
            public String execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                Document document = getValue(key, Document.class).execute(capturedInputAndOutputs);
                return getAttributeOrElementValue(document, xpath);
            }

            private String getAttributeOrElementValue(Document document, String xpath) throws JDOMException {
                XPath xpather = XPath.newInstance(xpath);
                Object result = xpather.selectSingleNode(document);
                return result instanceof Element ? ((Element) result).getText() : ((Attribute) result).getValue();
            }
        };
    }

}
