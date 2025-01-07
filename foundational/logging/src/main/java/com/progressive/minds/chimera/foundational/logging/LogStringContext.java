package com.progressive.minds.chimera.foundational.logging;

import java.util.HashMap;
import java.util.Map;

public class LogStringContext {

//    public LogStringContext(String... sc) {
//        this.sc = sc.
//    }
//
//    public MessageWithContext log(MDC... args) {
//        StringBuilder sb = new StringBuilder(StringContext.processEscapes(sc.parts().get(0)));
//        Map<String, String> context = new HashMap<>();
//
//        for (MDC mdc : args) {
//            String value = mdc.value() != null ? mdc.value().toString() : null;
//            sb.append(value);
//            if (isStructuredLoggingEnabled()) {
//                context.put(mdc.key().name(), value);
//            }
//            sb.append(StringContext.processEscapes(sc.parts().get(1)));
//        }
//
//        return new MessageWithContext(sb.toString(), context);
//    }
}
