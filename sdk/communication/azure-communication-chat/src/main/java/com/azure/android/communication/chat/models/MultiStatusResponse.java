// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat.models;

import com.azure.android.core.annotation.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The MultiStatusResponse model.
 */
@Immutable
public final class MultiStatusResponse {
    /*
     * The list of status information for each resource in the request.
     */
    @JsonProperty(value = "multipleStatus", access = JsonProperty.Access.WRITE_ONLY)
    private List<IndividualStatusResponse> multipleStatus;

    /**
     * Get the multipleStatus property: The list of status information for each
     * resource in the request.
     * 
     * @return the multipleStatus value.
     */
    public List<IndividualStatusResponse> getMultipleStatus() {
        return this.multipleStatus;
    }
}
