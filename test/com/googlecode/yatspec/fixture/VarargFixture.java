package com.googlecode.yatspec.fixture;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class VarargFixture {

    public Map<String, Object> arrayOnly(String[] lotsOfParams) {
        Map<String, Object> recordedParams = new HashMap<String, Object>();
        recordedParams.put("lotsOfParams", lotsOfParams);
        return recordedParams;
    }

    public Map<String, Object> varargsOnly(String... lotsOfParams) {
        Map<String, Object> recordedParams = new HashMap<String, Object>();
        recordedParams.put("lotsOfParams", lotsOfParams);
        return recordedParams;
    }

    public Map<String, Object> oneParamAndVarargs(String someParam, String... lotsOfParams) {
        Map<String, Object> recordedParams = new HashMap<String, Object>();
        recordedParams.put("someParam", someParam);
        recordedParams.put("lotsOfParams", lotsOfParams);
        return recordedParams;
    }

    public Map<String, Object> someParamsAndVarargs(String someParam, String anotherParam, String... lotsOfParams) {
        Map<String, Object> recordedParams = new HashMap<String, Object>();
        recordedParams.put("someParam", someParam);
        recordedParams.put("anotherParam", anotherParam);
        recordedParams.put("lotsOfParams", lotsOfParams);
        return recordedParams;
    }
}
