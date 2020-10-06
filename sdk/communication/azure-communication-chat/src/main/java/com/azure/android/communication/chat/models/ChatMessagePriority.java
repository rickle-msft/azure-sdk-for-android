// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat.models;

import com.azure.android.core.util.ExpandableStringEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collection;

/**
 * Defines values for ChatMessagePriority.
 */
public final class ChatMessagePriority extends ExpandableStringEnum<ChatMessagePriority> {
    /**
     * Static value Normal for ChatMessagePriority.
     */
    public static final ChatMessagePriority NORMAL = fromString("Normal");

    /**
     * Static value High for ChatMessagePriority.
     */
    public static final ChatMessagePriority HIGH = fromString("High");

    /**
     * Creates or finds a ChatMessagePriority from its string representation.
     * 
     * @param name a name to look for.
     * @return the corresponding ChatMessagePriority.
     */
    @JsonCreator
    public static ChatMessagePriority fromString(String name) {
        return fromString(name, ChatMessagePriority.class);
    }

    /**
     * @return known ChatMessagePriority values.
     */
    public static Collection<ChatMessagePriority> values() {
        return values(ChatMessagePriority.class);
    }
}
