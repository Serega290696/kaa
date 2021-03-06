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

package org.kaaproject.kaa.server.appenders.flume.appender;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.flume.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kaaproject.kaa.common.dto.logs.LogSchemaDto;
import org.kaaproject.kaa.server.appenders.flume.config.gen.FlumeConfig;
import org.kaaproject.kaa.server.appenders.flume.config.gen.FlumeEventFormat;
import org.kaaproject.kaa.server.common.log.shared.appender.LogEvent;
import org.kaaproject.kaa.server.common.log.shared.appender.LogEventPack;
import org.kaaproject.kaa.server.common.log.shared.appender.LogSchema;

public class FlumeAvroEventBuilderTest {

    private static final int EVENTS_COUNT = 5;

    private static final int SCHEMA_VERSION = 10;

    private LogEventPack eventPack;
    private FlumeEventBuilder eventBuilder;
    private String appToken;

    @Before
    public void init() {
        appToken = RandomStringUtils.randomNumeric(15);
        eventPack = new LogEventPack();
        List<LogEvent> list = generateEvents();

        eventPack.setEvents(list);
        LogSchemaDto schemaDto = new LogSchemaDto();
        schemaDto.setMajorVersion(SCHEMA_VERSION);
        LogSchema logSchema = new LogSchema(schemaDto);
        eventPack.setLogSchema(logSchema);
        eventPack.setLogSchemaVersion(SCHEMA_VERSION);
    }

    @Test
    public void generateFromAvroEventWithEmptyLogSchemaTest() {
        eventBuilder = new FlumeAvroEventBuilder();
        FlumeConfig flumeConfig = new FlumeConfig();
        flumeConfig.setFlumeEventFormat(FlumeEventFormat.RECORDS_CONTAINER);
        eventBuilder.init(flumeConfig);
        eventPack.setLogSchema(null);
        List<Event> events = eventBuilder.generateEvents(eventPack, null, appToken);
        Assert.assertNull(events);
    }

    @Test
    public void generateFromAvroEventWithEmptyLogEventsTest() {
        eventBuilder = new FlumeAvroEventBuilder();
        FlumeConfig flumeConfig = new FlumeConfig();
        flumeConfig.setFlumeEventFormat(FlumeEventFormat.RECORDS_CONTAINER);
        eventBuilder.init(flumeConfig);
        eventPack.setEvents(new ArrayList<LogEvent>());
        List<Event> events = eventBuilder.generateEvents(eventPack, null, appToken);
        Assert.assertNull(events);
    }

    @Test
    public void generateFromAvroEventRecordsContainerTest() {
        eventBuilder = new FlumeAvroEventBuilder();
        FlumeConfig flumeConfig = new FlumeConfig();
        flumeConfig.setFlumeEventFormat(FlumeEventFormat.RECORDS_CONTAINER);
        eventBuilder.init(flumeConfig);
        List<Event> events = eventBuilder.generateEvents(eventPack, null, appToken);
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
    }
    
    @Test
    public void generateFromAvroEventGenericTest() {
        eventBuilder = new FlumeAvroEventBuilder();
        FlumeConfig flumeConfig = new FlumeConfig();
        flumeConfig.setFlumeEventFormat(FlumeEventFormat.GENERIC);
        eventBuilder.init(flumeConfig);
        List<Event> events = eventBuilder.generateEvents(eventPack, null, appToken);
        Assert.assertNotNull(events);
        Assert.assertEquals(EVENTS_COUNT, events.size());
    }

    private List<LogEvent> generateEvents() {
        List<LogEvent> events = new ArrayList<>();
        for (int i = 0; i < EVENTS_COUNT; i++) {
            LogEvent logEvent = new LogEvent();
            logEvent.setLogData(new byte[] { 1, 2, 3, 4 });
            events.add(logEvent);
        }
        return events;
    }
}
