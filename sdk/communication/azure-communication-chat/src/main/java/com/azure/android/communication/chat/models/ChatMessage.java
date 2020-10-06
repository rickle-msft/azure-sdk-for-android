// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat.models;

import com.azure.android.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.threeten.bp.OffsetDateTime;

/**
 * The ChatMessage model.
 */
@Fluent
public final class ChatMessage {
    /*
     * The id of the chat message. This id is server generated.
     */
    @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    /*
     * Type of the chat message.
     * 
     * Possible values:
     * - Text
     * - ThreadActivity/TopicUpdate
     * - ThreadActivity/AddMember
     * - ThreadActivity/DeleteMember
     */
    @JsonProperty(value = "type")
    private String type;

    /*
     * The chat message priority.
     */
    @JsonProperty(value = "priority")
    private ChatMessagePriority priority;

    /*
     * Version of the chat message.
     */
    @JsonProperty(value = "version", access = JsonProperty.Access.WRITE_ONLY)
    private String version;

    /*
     * Content of the chat message.
     */
    @JsonProperty(value = "content")
    private String content;

    /*
     * The display name of the chat message sender. This property is used to
     * populate sender name for push notifications.
     */
    @JsonProperty(value = "senderDisplayName")
    private String senderDisplayName;

    /*
     * The timestamp when the chat message arrived at the server. The timestamp
     * is in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     */
    @JsonProperty(value = "createdOn", access = JsonProperty.Access.WRITE_ONLY)
    private OffsetDateTime createdOn;

    /*
     * The id of the chat message sender.
     */
    @JsonProperty(value = "senderId", access = JsonProperty.Access.WRITE_ONLY)
    private String senderId;

    /*
     * The timestamp when the chat message was deleted. The timestamp is in
     * ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     */
    @JsonProperty(value = "deletedOn")
    private OffsetDateTime deletedOn;

    /*
     * The timestamp when the chat message was edited. The timestamp is in
     * ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     */
    @JsonProperty(value = "editedOn")
    private OffsetDateTime editedOn;

    /**
     * Get the id property: The id of the chat message. This id is server
     * generated.
     * 
     * @return the id value.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the type property: Type of the chat message.
     * 
     * Possible values:
     * - Text
     * - ThreadActivity/TopicUpdate
     * - ThreadActivity/AddMember
     * - ThreadActivity/DeleteMember.
     * 
     * @return the type value.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set the type property: Type of the chat message.
     * 
     * Possible values:
     * - Text
     * - ThreadActivity/TopicUpdate
     * - ThreadActivity/AddMember
     * - ThreadActivity/DeleteMember.
     * 
     * @param type the type value to set.
     * @return the ChatMessage object itself.
     */
    public ChatMessage setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get the priority property: The chat message priority.
     * 
     * @return the priority value.
     */
    public ChatMessagePriority getPriority() {
        return this.priority;
    }

    /**
     * Set the priority property: The chat message priority.
     * 
     * @param priority the priority value to set.
     * @return the ChatMessage object itself.
     */
    public ChatMessage setPriority(ChatMessagePriority priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Get the version property: Version of the chat message.
     * 
     * @return the version value.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Get the content property: Content of the chat message.
     * 
     * @return the content value.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Set the content property: Content of the chat message.
     * 
     * @param content the content value to set.
     * @return the ChatMessage object itself.
     */
    public ChatMessage setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Get the senderDisplayName property: The display name of the chat message
     * sender. This property is used to populate sender name for push
     * notifications.
     * 
     * @return the senderDisplayName value.
     */
    public String getSenderDisplayName() {
        return this.senderDisplayName;
    }

    /**
     * Set the senderDisplayName property: The display name of the chat message
     * sender. This property is used to populate sender name for push
     * notifications.
     * 
     * @param senderDisplayName the senderDisplayName value to set.
     * @return the ChatMessage object itself.
     */
    public ChatMessage setSenderDisplayName(String senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
        return this;
    }

    /**
     * Get the createdOn property: The timestamp when the chat message arrived
     * at the server. The timestamp is in ISO8601 format:
     * `yyyy-MM-ddTHH:mm:ssZ`.
     * 
     * @return the createdOn value.
     */
    public OffsetDateTime getCreatedOn() {
        return this.createdOn;
    }

    /**
     * Get the senderId property: The id of the chat message sender.
     * 
     * @return the senderId value.
     */
    public String getSenderId() {
        return this.senderId;
    }

    /**
     * Get the deletedOn property: The timestamp when the chat message was
     * deleted. The timestamp is in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * 
     * @return the deletedOn value.
     */
    public OffsetDateTime getDeletedOn() {
        return this.deletedOn;
    }

    /**
     * Set the deletedOn property: The timestamp when the chat message was
     * deleted. The timestamp is in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * 
     * @param deletedOn the deletedOn value to set.
     * @return the ChatMessage object itself.
     */
    public ChatMessage setDeletedOn(OffsetDateTime deletedOn) {
        this.deletedOn = deletedOn;
        return this;
    }

    /**
     * Get the editedOn property: The timestamp when the chat message was
     * edited. The timestamp is in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * 
     * @return the editedOn value.
     */
    public OffsetDateTime getEditedOn() {
        return this.editedOn;
    }

    /**
     * Set the editedOn property: The timestamp when the chat message was
     * edited. The timestamp is in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * 
     * @param editedOn the editedOn value to set.
     * @return the ChatMessage object itself.
     */
    public ChatMessage setEditedOn(OffsetDateTime editedOn) {
        this.editedOn = editedOn;
        return this;
    }
}
