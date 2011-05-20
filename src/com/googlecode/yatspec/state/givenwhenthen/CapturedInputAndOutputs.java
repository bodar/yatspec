package com.googlecode.yatspec.state.givenwhenthen;

import java.util.List;
import java.util.Map;

public class CapturedInputAndOutputs extends NiceMap<CapturedInputAndOutputs>{
    public CapturedInputAndOutputs(Object... instances) {
        super(instances);
    }

    public CapturedInputAndOutputs(List<Map.Entry<String, Object>> entries) {
        super(entries);
    }
}
