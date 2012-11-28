/**
 * Copyright 2012 Microsoft Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microsoft.windowsazure.services.media.models;

import static org.junit.Assert.*;

import java.net.URLEncoder;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.microsoft.windowsazure.services.media.implementation.content.ContentKeyRestType;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityCreationOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityDeleteOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityListOperation;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Tests for the ContentKey entity
 * 
 */
public class ContentKeyEntityTest {
    private final String testContentKeyId = "nb:kid:UUID:c82307be-1a81-4554-ba7d-cf6dfa735a5a";
    private final ContentKeyType testContentKeyType = ContentKeyType.StorageEncryption;
    private final String testEncryptedContentKey = "testEncryptedContentKey";
    private final String testExpectedContentKeyUri = String.format("ContentKeys('%s')",
            URLEncoder.encode(testContentKeyId, "UTF-8"));

    public ContentKeyEntityTest() throws Exception {

    }

    @Test
    public void createContentKeyHasCorrectUrl() throws Exception {
        EntityCreationOperation<ContentKeyInfo> creator = ContentKey.create(testContentKeyId, testContentKeyType,
                testEncryptedContentKey);

        assertEquals("ContentKeys", creator.getUri());
    }

    @Test
    public void createContentKeyHasCorrectPayload() throws Exception {
        ContentKeyRestType contentKeyRestType = (ContentKeyRestType) ContentKey.create(testContentKeyId,
                testContentKeyType, testEncryptedContentKey).getRequestContents();

        assertEquals(testContentKeyId, contentKeyRestType.getId());
        assertEquals(testContentKeyType.getCode(), contentKeyRestType.getContentKeyType().intValue());
        assertEquals(testEncryptedContentKey, contentKeyRestType.getEncryptedContentKey());
        assertNull(contentKeyRestType.getChecksum());
        assertNull(contentKeyRestType.getCreated());
        assertNull(contentKeyRestType.getLastModified());
        assertNull(contentKeyRestType.getName());
        assertNull(contentKeyRestType.getProtectionKeyId());
        assertNull(contentKeyRestType.getProtectionKeyType());
    }

    @Test
    public void getContentKeyGivesExpectedUri() throws Exception {
        assertEquals(testExpectedContentKeyUri, ContentKey.get(testContentKeyId).getUri());
    }

    @Test
    public void listContentKeyReturnsExpectedUri() {
        EntityListOperation<ContentKeyInfo> lister = ContentKey.list();

        assertEquals("ContentKeys", lister.getUri());
        assertNotNull(lister.getQueryParameters());
        assertEquals(0, lister.getQueryParameters().size());
    }

    @Test
    public void listContentKeyCanTakeQueryParameters() {
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("$top", "10");
        queryParams.add("$skip", "2");

        EntityListOperation<ContentKeyInfo> lister = ContentKey.list(queryParams);

        assertEquals("10", lister.getQueryParameters().getFirst("$top"));
        assertEquals("2", lister.getQueryParameters().getFirst("$skip"));
        assertEquals(2, lister.getQueryParameters().size());
    }

    @Test
    public void ContentKeyDeleteReturnsExpectedUri() throws Exception {
        EntityDeleteOperation deleter = ContentKey.delete(testContentKeyId);
        assertEquals(testExpectedContentKeyUri, deleter.getUri());
    }
}
