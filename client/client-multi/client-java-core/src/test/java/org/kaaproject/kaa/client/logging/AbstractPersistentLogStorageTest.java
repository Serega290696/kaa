/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.client.logging;

import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractPersistentLogStorageTest extends AbstractLogStorageTest {
    private static final int BUCKET_SIZE = 3;
    private static final int RECORD_COUNT = 2;

    @Test
    public void testPersistDBState() {
        LogStorage storage = (LogStorage) getStorage(BUCKET_SIZE, RECORD_COUNT);

        LogRecord record = new LogRecord();
        int insertionCount = 7;
        /*
         * Size of each record is 3B
         */
        int iter = insertionCount;
        while (iter-- > 0) {
            storage.addLogRecord(record);
        }
        LogBlock beforePersist = storage.getRecordBlock(15, 2);
        storage.close();

        storage = (LogStorage) getStorage(BUCKET_SIZE, RECORD_COUNT);
        LogStorageStatus storageStatus = (LogStorageStatus) storage;
        Assert.assertEquals(insertionCount, storageStatus.getRecordCount());
        Assert.assertEquals(insertionCount * 3, storageStatus.getConsumedVolume());
        LogBlock afterPersist = storage.getRecordBlock(15, 2);

        Assert.assertEquals(beforePersist.getRecords().size(), afterPersist.getRecords().size());

        storage.close();
    }

    @Test
    public void testGetBigRecordBlock() {
        LogStorage storage = (LogStorage) getStorage(BUCKET_SIZE, RECORD_COUNT);

        LogRecord record = new LogRecord();
        int insertionCount = 7;
        /*
         * Size of each record is 3B
         */
        int iter = insertionCount;
        while (iter-- > 0) {
            storage.addLogRecord(record);
        }

        LogBlock logBlock = storage.getRecordBlock(8192, 1000);
        Assert.assertEquals(insertionCount, logBlock.getRecords().size());
        storage.close();
    }


}
