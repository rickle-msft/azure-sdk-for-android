package com.azure.android.storage.blob;

import com.azure.android.storage.blob.models.AccessTier;
import com.azure.android.storage.blob.models.BlobDeleteHeaders;
import com.azure.android.storage.blob.models.BlobDeleteResponse;
import com.azure.android.storage.blob.models.BlobDownloadHeaders;
import com.azure.android.storage.blob.models.BlobDownloadResponse;
import com.azure.android.storage.blob.models.BlobGetPropertiesHeaders;
import com.azure.android.storage.blob.models.BlobGetPropertiesResponse;
import com.azure.android.storage.blob.models.BlobStorageException;
import com.azure.android.storage.blob.models.BlobType;
import com.azure.android.storage.blob.models.LeaseStateType;
import com.azure.android.storage.blob.models.LeaseStatusType;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

import static com.azure.android.storage.blob.TestUtils.enableFiddler;
import static com.azure.android.storage.blob.TestUtils.generateBlockID;
import static com.azure.android.storage.blob.TestUtils.generateResourceName;
import static com.azure.android.storage.blob.TestUtils.getDefaultData;
import static com.azure.android.storage.blob.TestUtils.initializeDefaultAsyncBlobClientBuilder;
import static com.azure.android.storage.blob.TestUtils.initializeDefaultSyncBlobClientBuilder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BlobTest {
    private String containerName;
    private String blobName;
    private static StorageBlobAsyncClient asyncClient;
    private static StorageBlobClient syncClient;

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
    public void getPropertiesValues() {
        // When
        BlobGetPropertiesHeaders response = syncClient.getBlobProperties(containerName, blobName);

        // Then
        assertNotNull(response.getETag());
//        assertFalse(response.getETag().contains("\"")); // Quotes should be scrubbed from etag header values
        assertNotNull(response.getLastModified());
        assertNotNull(response.getRequestId());
        assertNotNull(response.getVersion());
        assertNotNull(response.getDateProperty());
        assertTrue(response.getMetadata() == null || response.getMetadata().isEmpty());
        assertEquals(BlobType.BLOCK_BLOB, response.getBlobType());
        assertNull(response.getCopyCompletionTime()); // tested in "copy"
        assertNull(response.getCopyStatusDescription()); // only returned when the service has errors; cannot validate.
        assertNull(response.getCopyId()); // tested in "abort copy"
        assertNull(response.getCopyProgress()); // tested in "copy"
        assertNull(response.getCopySource()); // tested in "copy"
        assertNull(response.getCopyStatus()); // tested in "copy"
        assertTrue(response.isIncrementalCopy() == null || !response.isIncrementalCopy()); // tested in PageBlob."start incremental copy"
        assertNull(response.getDestinationSnapshot()); // tested in PageBlob."start incremental copy"
        assertNull(response.getLeaseDuration()); // tested in "acquire lease"
        assertEquals(LeaseStateType.AVAILABLE, response.getLeaseState());
        assertEquals(LeaseStatusType.UNLOCKED, response.getLeaseStatus());
        assertTrue(response.getContentLength() >= 0);
        assertNotNull(response.getContentType());
        assertNull(response.getContentMD5());
        assertNull(response.getContentEncoding()); // tested in "set HTTP headers"
        assertNull(response.getContentDisposition()); // tested in "set HTTP headers"
        assertNull(response.getContentLanguage()); // tested in "set HTTP headers"
        assertNull(response.getCacheControl()); // tested in "set HTTP headers"
        assertNull(response.getBlobSequenceNumber()); // tested in PageBlob."create sequence number"
        assertEquals("bytes", response.getAcceptRanges());
        assertNull(response.getBlobCommittedBlockCount()); // tested in AppendBlob."append block"
        assertTrue(response.isServerEncrypted());
        assertEquals(AccessTier.HOT.toString(), response.getAccessTier());
        assertTrue(response.isAccessTierInferred());
        assertNull(response.getArchiveStatus());
        assertNotNull(response.getCreationTime());
        // Tag Count not in BlobProperties.
        // Rehydrate priority not in BlobProperties
        // Is Sealed not in BlobProperties
        // Last Access Time is not in BlobProperties
    }

    @Test
    public void getProperties() {
        // When
        BlobGetPropertiesResponse response = syncClient.getBlobPropertiesWithRestResponse(containerName, blobName,
            null, null, null, null, null, null, null);

        // Then
        assertEquals(200, response.getStatusCode());
    }

    // Get properties AC

    // Get properties AC fail

    @Test
    public void getPropertiesError() {
        // Setup
        String blobName = generateResourceName(); // Blob that does not exist.

        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.getBlobProperties(containerName, blobName));

        // Then
        assertEquals(404, ex.getStatusCode());
    }

    @Test
    public void rawDownloadMin() throws IOException {
        // When
        ResponseBody response = syncClient.rawDownload(containerName, blobName);

        // Then
        assertArrayEquals(getDefaultData(), response.bytes());
    }

    @Test
    public void rawDownloadValues() throws IOException {
        // When
        BlobDownloadResponse response = syncClient.rawDownloadWithRestResponse(containerName, blobName, null, null, null, null, null, null, null, null, null, null);

        // Then
        assertArrayEquals(getDefaultData(), response.getValue().bytes());
        assertEquals(200, response.getStatusCode());
        BlobDownloadHeaders headers = response.getDeserializedHeaders();
        assertNotNull(headers.getETag());
//        assertFalse(response.getETag().contains("\"")); // Quotes should be scrubbed from etag header values
        assertNotNull(headers.getLastModified());
        assertNotNull(headers.getRequestId());
        assertNotNull(headers.getVersion());
        assertNotNull(headers.getDateProperty());
        assertTrue(headers.getMetadata() == null || headers.getMetadata().isEmpty());
        assertEquals(BlobType.BLOCK_BLOB, headers.getBlobType());
        assertNull(headers.getCopyCompletionTime()); // tested in "copy"
        assertNull(headers.getCopyStatusDescription()); // only returned when the service has errors; cannot validate.
        assertNull(headers.getCopyId()); // tested in "abort copy"
        assertNull(headers.getCopyProgress()); // tested in "copy"
        assertNull(headers.getCopySource()); // tested in "copy"
        assertNull(headers.getCopyStatus()); // tested in "copy"
        assertNull(headers.getLeaseDuration()); // tested in "acquire lease"
        assertEquals(LeaseStateType.AVAILABLE, headers.getLeaseState());
        assertEquals(LeaseStatusType.UNLOCKED, headers.getLeaseStatus());
        assertTrue(headers.getContentLength() >= 0);
        assertNotNull(headers.getContentType());
        assertNull(headers.getContentMd5());
        assertNull(headers.getContentEncoding()); // tested in "set HTTP headers"
        assertNull(headers.getContentDisposition()); // tested in "set HTTP headers"
        assertNull(headers.getContentLanguage()); // tested in "set HTTP headers"
        assertNull(headers.getCacheControl()); // tested in "set HTTP headers"
        assertNull(headers.getBlobSequenceNumber()); // tested in PageBlob."create sequence number"
        assertEquals("bytes", headers.getAcceptRanges());
        assertNull(headers.getBlobCommittedBlockCount()); // tested in AppendBlob."append block"
        assertTrue(headers.isServerEncrypted());
    }

    @Test
    public void rawDownloadEmptyBlob() throws IOException {
        // Setup
        String blobName = generateResourceName(); // Blob that does not exist.
        syncClient.commitBlockList(containerName, blobName, new ArrayList<>(), false);

        // When
        BlobDownloadResponse response = syncClient.rawDownloadWithRestResponse(containerName, blobName, null, null, null, null, null, null, null, null, null, null);

        // Then
        assertEquals("", response.getValue().string());
        assertEquals(200, response.getStatusCode());
        BlobDownloadHeaders headers = response.getDeserializedHeaders();
        assertEquals(0, (long) headers.getContentLength());
    }

    // Download range
    // Download AC
    // Download AC fail
    // Download md5
    // Download snapshot

    @Test
    public void rawDownloadError() {
        // Setup
        String blobName = generateResourceName(); // Blob that does not exist.

        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.rawDownload(containerName, blobName));

        // Then
        assertEquals(404, ex.getStatusCode());
    }

    @Test
    public void deleteMin() {
        // When
        syncClient.deleteBlob(containerName, blobName);

        // Then
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.getBlobProperties(containerName, blobName));
        assertEquals(404, ex.getStatusCode());
        assertEquals("BlobNotFound", ex.getErrorCode().toString());
    }

    @Test
    public void delete() {
        // When
        BlobDeleteResponse response = syncClient.deleteBlobWithRestResponse(containerName, blobName, null, null, null, null, null, null, null, null, null, null, null);

        // Then
        assertEquals(202, response.getStatusCode());
        BlobDeleteHeaders headers = response.getDeserializedHeaders();
        assertNotNull(headers.getRequestId());
        assertNotNull(headers.getVersion());
        assertNotNull(headers.getDateProperty());
    }

    // Delete options

    // Delete AC

    // Delete AC fail

    @Test
    public void deleteError() {
        // Setup
        String blobName = generateResourceName(); // Blob that does not exist.

        // When
        BlobStorageException ex = assertThrows(BlobStorageException.class,
            () -> syncClient.deleteBlob(containerName, blobName));

        // Then
        assertEquals(404, ex.getStatusCode());
    }


}
