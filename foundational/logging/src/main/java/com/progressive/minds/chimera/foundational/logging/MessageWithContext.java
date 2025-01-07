package com.progressive.minds.chimera.foundational.logging;

import com.google.common.base.Preconditions;
import com.progressive.minds.chimera.common.tags.annotation.Private;

import java.util.HashMap; /**
 * Wrapper class for log messages that include a logging context.
 * This is used as the return type of the string interpolator `LogStringContext`.
 */
@Private
public record MessageWithContext(String message, HashMap<String, String> context) {

    public MessageWithContext merge(MessageWithContext messageWithContext) {
        Preconditions.checkNotNull(messageWithContext);
        HashMap<String, String> resultMap = new HashMap<>(this.context);
        resultMap.putAll(messageWithContext.context);
        return new MessageWithContext(this.message + messageWithContext.message, resultMap);
    }

    public MessageWithContext stripMargin() {
        return new MessageWithContext(this.message.strip(), this.context);
    }
}
