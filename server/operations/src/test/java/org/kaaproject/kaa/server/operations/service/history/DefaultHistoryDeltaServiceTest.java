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

package org.kaaproject.kaa.server.operations.service.history;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kaaproject.kaa.common.dto.ChangeDto;
import org.kaaproject.kaa.common.dto.ChangeType;
import org.kaaproject.kaa.common.dto.EndpointGroupStateDto;
import org.kaaproject.kaa.common.dto.EndpointProfileDto;
import org.kaaproject.kaa.common.dto.HistoryDto;
import org.kaaproject.kaa.common.dto.ProfileFilterDto;
import org.kaaproject.kaa.server.operations.service.cache.AppVersionKey;
import org.kaaproject.kaa.server.operations.service.cache.CacheService;
import org.kaaproject.kaa.server.operations.service.cache.ConfigurationIdKey;
import org.kaaproject.kaa.server.operations.service.cache.HistoryKey;
import org.kaaproject.kaa.server.operations.service.cache.HistorySubject;
import org.kaaproject.kaa.server.operations.service.delta.HistoryDelta;
import org.kaaproject.kaa.server.operations.service.filter.FilterService;
import org.kaaproject.kaa.server.operations.service.history.DefaultHistoryDeltaService;
import org.kaaproject.kaa.server.operations.service.history.HistoryDeltaService;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class DefaultHistoryDeltaServiceTest {
    private static final String PROFILE_BODY = "dummy";
    private static final int PROFILE_VERSION = 3;
    private static final int CONF_VERSION = 2;
    private static final String APP1_ID = "APP1_ID";
    private static final String APP1_TOKEN = "APP1_TOKEN";
    private static final String EG1_ID = "EG1_ID";
    private static final String EG2_ID = "EG2_ID";
    private static final String PF1_ID = "PF1_ID";
    private static final String PF2_ID = "PF2_ID";
    private static final String CF1_ID = "CF1_ID";
    private static final String CF2_ID = "CF2_ID";
    private static final String CACHE_SERVICE = "cacheService";
    private static final String FILTER_SERVICE = "filterService";

    private HistoryDeltaService historyDeltaService;
    private CacheService cacheService;
    private FilterService filterService;

    private EndpointProfileDto profile;

    @Before
    public void before() {
        historyDeltaService = new DefaultHistoryDeltaService();
        cacheService = mock(CacheService.class);
        filterService = mock(FilterService.class);

        ReflectionTestUtils.setField(historyDeltaService, CACHE_SERVICE, cacheService);
        ReflectionTestUtils.setField(historyDeltaService, FILTER_SERVICE, filterService);

        profile = new EndpointProfileDto();
        profile.setApplicationId(APP1_ID);
        profile.setConfigurationVersion(CONF_VERSION);
        profile.setProfileVersion(PROFILE_VERSION);
        profile.setProfile(PROFILE_BODY);
    };

    @After
    public void after() {
        historyDeltaService = null;
    };

    @Test
    public void testInitial() {
        List<ProfileFilterDto> allFilters = new ArrayList<>();
        ProfileFilterDto filter = new ProfileFilterDto();
        filter.setApplicationId(APP1_ID);
        filter.setEndpointGroupId(EG1_ID);
        filter.setId(PF1_ID);
        allFilters.add(filter);

        Mockito.when(filterService.getAllMatchingFilters(Mockito.any(AppVersionKey.class), Mockito.any(String.class))).thenReturn(allFilters);
        Mockito.when(cacheService.getConfIdByKey(Mockito.any(ConfigurationIdKey.class))).thenReturn(CF1_ID);

        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, APP1_TOKEN, 0);

        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
        EndpointGroupStateDto egs = historyDelta.getEndpointGroupStates().get(0);
        Assert.assertNotNull(egs);
        Assert.assertEquals(CF1_ID, egs.getConfigurationId());
        Assert.assertEquals(PF1_ID, egs.getProfileFilterId());
        Assert.assertEquals(EG1_ID, egs.getEndpointGroupId());
    }

    @Test
    public void testDeltaNoChanges() {
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.CONFIGURATION, APP1_TOKEN, 101, 101);
        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(0, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaOldGroupRemove() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.REMOVE_GROUP, EG1_ID)));

        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.CONFIGURATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(0, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaNewGroupRemove() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.REMOVE_GROUP, EG2_ID)));

        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaOldGroupAddTopic() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_TOPIC, EG1_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaOldGroupRemoveTopic() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.REMOVE_TOPIC, EG1_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.CONFIGURATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaOldGroupAddConf() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_CONF, EG1_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }


    @Test
    public void testDeltaOldGroupRemoveConf() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.REMOVE_CONF, EG1_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaOldGroupRemoveProf() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.REMOVE_PROF, EG1_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(0, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaOldGroupAddMatchingProf() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_PROF, EG1_ID, PF2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
        Assert.assertEquals(PF2_ID, historyDelta.getEndpointGroupStates().get(0).getProfileFilterId());
    }

    @Test
    public void testDeltaOldGroupAddNotMatchingProf() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_PROF, EG1_ID, PF2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.CONFIGURATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(0, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaOldGroupWrongChange() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.UPDATE, EG1_ID, PF2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaNewGroupAddTopic() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_TOPIC, EG2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.CONFIGURATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaNewGroupRemoveTopic() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.REMOVE_TOPIC, EG2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaNewGroupMatchingPFWithoutCF() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_PROF, EG2_ID, PF2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.CONFIGURATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(2, historyDelta.getEndpointGroupStates().size());
    }

    @Test
    public void testDeltaNewGroupMatchingPFWithCf() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_PROF, EG2_ID, PF2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(cacheService.getConfIdByKey(Mockito.any(ConfigurationIdKey.class))).thenReturn(CF2_ID);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.NOTIFICATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertTrue(historyDelta.isConfigurationChanged());
        Assert.assertTrue(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(2, historyDelta.getEndpointGroupStates().size());
        Assert.assertEquals(CF1_ID, historyDelta.getEndpointGroupStates().get(0).getConfigurationId());
        Assert.assertEquals(CF2_ID, historyDelta.getEndpointGroupStates().get(1).getConfigurationId());
    }

    @Test
    public void testDeltaNewGroupNotMatchingPF() {
        profile.setCfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        profile.setNfGroupStates(toList(new EndpointGroupStateDto(EG1_ID, PF1_ID, CF1_ID)));
        Mockito.when(cacheService.getHistory(Mockito.any(HistoryKey.class))).thenReturn(toList(toDto(ChangeType.ADD_PROF, EG2_ID, PF2_ID)));
        Mockito.when(filterService.matches(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        HistoryDelta historyDelta = historyDeltaService.getDelta(profile, HistorySubject.CONFIGURATION, APP1_TOKEN, 101, 102);

        Assert.assertNotNull(historyDelta);
        Assert.assertFalse(historyDelta.isConfigurationChanged());
        Assert.assertFalse(historyDelta.isTopicListChanged());
        Assert.assertNotNull(historyDelta.getEndpointGroupStates());
        Assert.assertEquals(1, historyDelta.getEndpointGroupStates().size());
    }

    public List<EndpointGroupStateDto> toList(EndpointGroupStateDto... dtos) {
        List<EndpointGroupStateDto> result = new ArrayList<>();
        for (EndpointGroupStateDto dto : dtos) {
            result.add(dto);
        }
        return result;
    }

    public List<HistoryDto> toList(HistoryDto... dtos) {
        List<HistoryDto> result = new ArrayList<>();
        for (HistoryDto dto : dtos) {
            result.add(dto);
        }
        return result;
    }

    public HistoryDto toDto(ChangeType changeType, String egId) {
        return toDto(changeType, egId, null);
    }

    public HistoryDto toDto(ChangeType changeType, String egId, String pfId) {
        HistoryDto dto = new HistoryDto();
        ChangeDto change = new ChangeDto();
        change.setType(changeType);
        change.setEndpointGroupId(egId);
        change.setProfileFilterId(pfId);
        dto.setChange(change);
        return dto;
    }

}
