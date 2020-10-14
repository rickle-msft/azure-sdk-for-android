package com.azure.android.storage.blob;

import com.azure.android.storage.blob.models.BlobErrorCode;
import com.azure.android.storage.blob.models.BlobGetPropertiesHeaders;
import com.azure.android.storage.blob.models.BlobGetPropertiesResponse;
import com.azure.android.storage.blob.models.BlobHttpHeaders;
import com.azure.android.storage.blob.models.BlobRequestConditions;
import com.azure.android.storage.blob.models.BlobStorageException;
import com.azure.android.storage.blob.models.BlockBlobCommitBlockListHeaders;
import com.azure.android.storage.blob.models.BlockBlobItem;
import com.azure.android.storage.blob.models.BlockBlobStageBlockHeaders;
import com.azure.android.storage.blob.models.BlockBlobsCommitBlockListResponse;
import com.azure.android.storage.blob.models.BlockBlobsStageBlockResponse;
import com.azure.android.storage.blob.models.ContainerGetPropertiesHeaders;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.OffsetDateTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.azure.android.storage.blob.TestUtils.enableFiddler;
import static com.azure.android.storage.blob.TestUtils.garbageEtag;
import static com.azure.android.storage.blob.TestUtils.generateBlockID;
import static com.azure.android.storage.blob.TestUtils.generateResourceName;
import static com.azure.android.storage.blob.TestUtils.getDefaultData;
import static com.azure.android.storage.blob.TestUtils.initializeDefaultAsyncBlobClientBuilder;
import static com.azure.android.storage.blob.TestUtils.initializeDefaultSyncBlobClientBuilder;
import static com.azure.android.storage.blob.TestUtils.newDate;
import static com.azure.android.storage.blob.TestUtils.oldDate;
import static com.azure.android.storage.blob.TestUtils.receivedEtag;
import static com.azure.android.storage.blob.TestUtils.setupMatchCondition;
import static com.azure.android.storage.blob.TestUtils.validateBlobProperties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class BlockBlobTest {
    private String containerName;
    private String blobName;
    private static StorageBlobAsyncClient asyncClient;
    private static StorageBlobClient syncClient;

    @DataProvider
    public static Object[][] headers() throws NoSuchAlgorithmException {
        return new Object[][] {
            {null,      null,          null,       null,       null,                                                       null},    // 0
            {"control", "disposition", "encoding", "language", MessageDigest.getInstance("MD5").digest(getDefaultData()), "type"},   // 1
        };
    }

    @DataProvider
    public static Object[][] accessConditionsSuccess() {
        return new Object[][] {
            {null,    null,    null,         null},       // 0
            {oldDate, null,    null,         null},       // 1
            {null,    newDate, null,         null},       // 2
            {null,    null,    receivedEtag, null},       // 3
            {null,    null,    null,         garbageEtag} // 4
        };
    }

    @DataProvider
    public static Object[][] accessConditionsFail() {
        return new Object[][] {
            {newDate, null,    null,        null},        // 0
            {null,    oldDate, null,        null},        // 1
            {null,    null,    garbageEtag, null},        // 2
            {null,    null,    null,        receivedEtag} // 3
        };
    }

    @BeforeClass
    public static void setupClass() {
        asyncClient = initializeDefaultAsyncBlobClientBuilder(enableFiddler()).build();
        syncClient = initializeDefaultSyncBlobClientBuilder(enableFiddler()).build();
    }

    @Before
    public void setupTest() {
        // Create container
        containerName = generateResourceName();
        syncClient.createContainer(containerName);

        // Create blob
        blobName = generateResourceName();
        String blockId = generateBlockID();
        syncClient.stageBlock(containerName, blobName, blockId, getDefaultData(), null);
        List<String> blockIds = new ArrayList<>();
        blockIds.add(blockId);
        syncClient.commitBlockList(containerName, blobName, blockIds, false);
    }

    @After
    public void teardownTest() {
        syncClient.deleteContainer(containerName);
    }

    @Test
    public void stageBlock() {
        // Setup
        String blockId = generateBlockID();

        // When
        BlockBlobsStageBlockResponse response = syncClient.stageBlockWithRestResponse(containerName, blobName, blockId, getDefaultData(), null, null, null ,null ,null ,null, null);

        // Then
        assertEquals(201, response.getStatusCode());
        BlockBlobStageBlockHeaders headers = response.getDeserializedHeaders();
        assertNotNull(headers.getXMsContentCrc64());
        assertNotNull(headers.getRequestId());
        assertNotNull(headers.getVersion());
        assertNotNull(headers.getDateProperty());
        assertTrue(headers.isServerEncrypted() != null && headers.isServerEncrypted());
    }

    @Test
    public void stageBlockMin() {
        // Setup
        String blockId = generateBlockID();

        // When
        Void response = syncClient.stageBlock(containerName, blobName, blockId, getDefaultData(), null);

        // Then
        // When list block support added, check blockBlobClient.listBlocks(BlockListType.ALL).getUncommittedBlocks().size() == 1
    }

    @Test
    public void stageBlockIABlockId() {
        // Setup
        String nullBlockId = null;

        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.stageBlock(containerName, blobName, nullBlockId, getDefaultData(), null));

        // Then
        assertEquals(400, ex.getStatusCode());

        // Setup
        String wrongBlockId = "id";

        // When
        ex = assertThrows(BlobStorageException.class,
            () -> syncClient.stageBlock(containerName, blobName, wrongBlockId, getDefaultData(), null));

        // Then
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    public void stageBlockIAData() {
        // Setup
        byte[] nullData = null;

        // Expect
        assertThrows(NullPointerException.class,
            () -> syncClient.stageBlock(containerName, blobName, generateBlockID(), nullData, null));

        // Setup
        byte[] data = new byte[0];

        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.stageBlock(containerName, blobName, generateBlockID(), data, null));

        // Then
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    public void stageBlockTransactionalMd5() throws NoSuchAlgorithmException {
        // Setup
        byte[] correctMd5 = MessageDigest.getInstance("MD5").digest(getDefaultData());

        // When
        BlockBlobsStageBlockResponse response = syncClient.stageBlockWithRestResponse(containerName, blobName, generateBlockID(), getDefaultData(), correctMd5, null, null, null, null, null, null);

        // Then
        assertEquals(201, response.getStatusCode());

        // Setup
        byte[] garbageMd5 = MessageDigest.getInstance("MD5").digest("garbage".getBytes());

        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.stageBlock(containerName, blobName, generateBlockID(), getDefaultData(), garbageMd5));

        // Then
        assertEquals(400, ex.getStatusCode());
        assertEquals(BlobErrorCode.MD5MISMATCH, ex.getErrorCode());
    }

    @Test
    public void commitBlockList() {
        // Setup
        String blockId = generateBlockID();
        syncClient.stageBlock(containerName, blobName, blockId, getDefaultData(), null);
        List<String> blockIds = new ArrayList<>();
        blockIds.add(blockId);

        // When
        BlockBlobsCommitBlockListResponse response = syncClient.commitBlockListWithRestResponse(containerName, blobName, blockIds, null ,null,  null, null, null, null, null, null, null, null);

        // Then
        assertEquals(201, response.getStatusCode());
        BlockBlobCommitBlockListHeaders headers = response.getDeserializedHeaders();
        assertNotNull(headers.getETag());
//        assertFalse(headers.getETag().contains("\"")); // Quotes should be scrubbed from etag header values
        assertNotNull(headers.getLastModified());
        assertNotNull(headers.getRequestId());
        assertNotNull(headers.getVersion());
        assertNotNull(headers.getDateProperty());
        assertNotNull(headers.getXMsContentCrc64());
        assertTrue(headers.isServerEncrypted() != null && headers.isServerEncrypted());
    }

    @Test
    public void commitBlockListMinOverwrite() {
        // Setup
        String blockId = generateBlockID();
        syncClient.stageBlock(containerName, blobName, blockId, getDefaultData(), null);
        List<String> blockIds = new ArrayList<>();
        blockIds.add(blockId);

        // When
        BlockBlobItem response = syncClient.commitBlockList(containerName, blobName, blockIds, true);

        // then
        assertNotNull(response);
    }

    @Test
    public void commitBlockListMinNoOverwrite() {
        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.commitBlockList(containerName, blobName, new ArrayList<>(), false));

        // then
        assertEquals(409, ex.getStatusCode());
        assertEquals(BlobErrorCode.BLOB_ALREADY_EXISTS, ex.getErrorCode());
    }

    @Test
    public void commitBlockListNull() {
        // When
        BlockBlobItem response = syncClient.commitBlockList(containerName, blobName, null, true);

        // then
        assertNotNull(response);
    }

    @Test
    @UseDataProvider("headers")
    public void commitBlockListHeaders(String cacheControl, String contentDisposition, String contentEncoding, String contentLanguage, byte[] contentMd5, String contentType) {
        // Setup
        BlobHttpHeaders headers = new BlobHttpHeaders()
            .setCacheControl(cacheControl)
            .setContentDisposition(contentDisposition)
            .setContentEncoding(contentEncoding)
            .setContentLanguage(contentLanguage)
            .setContentMd5(contentMd5)
            .setContentType(contentType);

        // When
        BlockBlobsCommitBlockListResponse response = syncClient.commitBlockListWithRestResponse(containerName, blobName, null, null ,null,  null, headers, null, null, null, null, null, null);

        // Then
        assertEquals(201, response.getStatusCode());

        // If the value isn't set the service will automatically set it
        contentType = (contentType == null) ? "application/octet-stream" : contentType;
        BlobGetPropertiesHeaders getPropertiesHeaders = syncClient.getBlobProperties(containerName, blobName);
        validateBlobProperties(getPropertiesHeaders, cacheControl, contentDisposition, contentEncoding, contentLanguage, contentMd5, contentType);
    }


    @Test
    public void commitBlockListMetadata() {
        // Setup
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key1", "value1");
        metadata.put("key2", "value2");

        // When
        BlockBlobsCommitBlockListResponse response = syncClient.commitBlockListWithRestResponse(containerName, blobName, null, null ,null,  null, null, metadata, null, null, null, null, null);

        // Then
        assertEquals(201, response.getStatusCode());

        BlobGetPropertiesHeaders headers = syncClient.getBlobProperties(containerName, blobName);
        assertEquals("value1", headers.getMetadata().get("key1"));
        assertEquals("value2", headers.getMetadata().get("key2"));
    }

    // TODO: (gapra) Commit block list tags

    @Test
    @UseDataProvider("accessConditionsSuccess")
    public void commitBlockListAC(OffsetDateTime modified, OffsetDateTime unmodified, String ifMatch, String ifNoneMatch) {
        // Setup
        ifMatch = setupMatchCondition(syncClient, containerName, blobName, ifMatch);
        BlobRequestConditions requestConditions = new BlobRequestConditions()
            .setIfModifiedSince(modified)
            .setIfUnmodifiedSince(unmodified)
            .setIfMatch(ifMatch)
            .setIfNoneMatch(ifNoneMatch);

        // When
        BlockBlobsCommitBlockListResponse response = syncClient.commitBlockListWithRestResponse(containerName, blobName, null, null ,null,  null, null, null, requestConditions, null, null, null, null);

        // Then
        assertEquals(201, response.getStatusCode());
    }

    @Test
    @UseDataProvider("accessConditionsFail")
    public void commitBlockListACFail(OffsetDateTime modified, OffsetDateTime unmodified, String ifMatch, String ifNoneMatch) {
        // Setup
        ifNoneMatch = setupMatchCondition(syncClient, containerName, blobName, ifNoneMatch);
        BlobRequestConditions requestConditions = new BlobRequestConditions()
            .setIfModifiedSince(modified)
            .setIfUnmodifiedSince(unmodified)
            .setIfMatch(ifMatch)
            .setIfNoneMatch(ifNoneMatch);

        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.commitBlockListWithRestResponse(containerName, blobName, null, null ,null,  null, null, null, requestConditions, null, null, null, null));

        // Then
        assertEquals(412, ex.getStatusCode());
    }
}
