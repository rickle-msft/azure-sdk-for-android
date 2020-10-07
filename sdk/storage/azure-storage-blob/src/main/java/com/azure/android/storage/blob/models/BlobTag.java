// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.storage.blob.models;

import com.azure.android.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The BlobTag model.
 */
@JacksonXmlRootElement(localName = "Tag")
@Fluent
public final class BlobTag {
    /*
     * The key property.
     */
    @JsonProperty(value = "Key", required = true)
    private String key;

    /*
     * The value property.
     */
    @JsonProperty(value = "Value", required = true)
    private String value;

    /**
     * Get the key property: The key property.
     *
     * @return the key value.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Set the key property: The key property.
     *
     * @param key the key value to set.
     * @return the BlobTag object itself.
     */
    public BlobTag setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Get the value property: The value property.
     *
     * @return the value value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set the value property: The value property.
     *
     * @param value the value value to set.
     * @return the BlobTag object itself.
     */
    public BlobTag setValue(String value) {
        this.value = value;
        return this;
    }
}
