// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.storage.blob.models;

import com.azure.android.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Blob tags.
 */
@JacksonXmlRootElement(localName = "Tags")
@Fluent
public final class BlobTags {
    private static final class TagSetWrapper {
        @JacksonXmlProperty(localName = "Tag")
        private final List<BlobTag> items;

        @JsonCreator
        private TagSetWrapper(@JacksonXmlProperty(localName = "Tag") List<BlobTag> items) {
            this.items = items;
        }
    }

    /*
     * The blobTagSet property.
     */
    @JsonProperty(value = "TagSet", required = true)
    private TagSetWrapper blobTagSet;

    /**
     * Get the blobTagSet property: The blobTagSet property.
     *
     * @return the blobTagSet value.
     */
    public List<BlobTag> getBlobTagSet() {
        if (this.blobTagSet == null) {
            this.blobTagSet = new TagSetWrapper(new ArrayList<BlobTag>());
        }
        return this.blobTagSet.items;
    }

    /**
     * Set the blobTagSet property: The blobTagSet property.
     *
     * @param blobTagSet the blobTagSet value to set.
     * @return the BlobTags object itself.
     */
    public BlobTags setBlobTagSet(List<BlobTag> blobTagSet) {
        this.blobTagSet = new TagSetWrapper(blobTagSet);
        return this;
    }
}
