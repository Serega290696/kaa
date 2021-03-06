/*
 * Copyright 2014 CyberVision, Inc.
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

package org.kaaproject.kaa.server.operations.service.cache;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Assert;
import org.junit.Test;
import org.kaaproject.kaa.server.operations.service.cache.AppVersionKey;

public class AppVersionKeyTest {

    @Test
    public void testHashCodeAndEquals(){
        EqualsVerifier.forClass(AppVersionKey.class).verify();
    }    
    
    @Test
    public void deltaSameCacheKeyTest() {
        AppVersionKey key1 = new AppVersionKey("appId1", 1);
        AppVersionKey key2 = new AppVersionKey("appId1", 1);
        Assert.assertEquals(key1, key2);
        key1 = new AppVersionKey(null, 1);
        key2 = new AppVersionKey(null, 1);
        Assert.assertEquals(key1, key2);
    }

    @Test
    public void deltaDifferentCacheKeyTest() {
        AppVersionKey key1 = new AppVersionKey("appId1", 1);
        AppVersionKey key2 = new AppVersionKey("appId1", 2);
        Assert.assertNotEquals(key1, key2);
        key1 = new AppVersionKey("appId1", 1);
        key2 = new AppVersionKey("appId2", 1);
        Assert.assertNotEquals(key1, key2);
    }
}
