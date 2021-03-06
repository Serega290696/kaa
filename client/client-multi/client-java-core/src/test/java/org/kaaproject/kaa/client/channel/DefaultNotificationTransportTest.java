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

package org.kaaproject.kaa.client.channel;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.kaaproject.kaa.client.channel.impl.ChannelRuntimeException;
import org.kaaproject.kaa.client.channel.impl.transports.DefaultNotificationTransport;
import org.kaaproject.kaa.client.notification.NotificationProcessor;
import org.kaaproject.kaa.client.persistence.KaaClientState;
import org.kaaproject.kaa.common.TransportType;
import org.kaaproject.kaa.common.endpoint.gen.Notification;
import org.kaaproject.kaa.common.endpoint.gen.NotificationSyncRequest;
import org.kaaproject.kaa.common.endpoint.gen.NotificationSyncResponse;
import org.kaaproject.kaa.common.endpoint.gen.NotificationType;
import org.kaaproject.kaa.common.endpoint.gen.SubscriptionType;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseStatus;
import org.kaaproject.kaa.common.endpoint.gen.Topic;
import org.mockito.Mockito;

public class DefaultNotificationTransportTest {

    @Test(expected = ChannelRuntimeException.class)
    public void testSyncNegative() {
        KaaClientState clientState = Mockito.mock(KaaClientState.class);
        NotificationTransport transport = new DefaultNotificationTransport();
        transport.setClientState(clientState);
        transport.sync();
    }

    @Test
    public void testSync() {
        KaaChannelManager channelManager = Mockito.mock(KaaChannelManager.class);
        KaaClientState clientState = Mockito.mock(KaaClientState.class);
        
        NotificationTransport transport = new DefaultNotificationTransport();
        transport.setChannelManager(channelManager);
        transport.setClientState(clientState);
        transport.sync();

        Mockito.verify(channelManager, Mockito.times(1)).sync(TransportType.NOTIFICATION);
    }

    @Test
    public void testCreateEmptyRequest() {
        NotificationTransport transport1 = new DefaultNotificationTransport();

        Assert.assertNull(transport1.createEmptyNotificationRequest());

        KaaClientState clientState = Mockito.mock(KaaClientState.class);

        NotificationTransport transport2 = new DefaultNotificationTransport();

        Assert.assertNull(transport2.createEmptyNotificationRequest());
        transport2.setClientState(clientState);

        NotificationSyncRequest request = transport2.createEmptyNotificationRequest();

        Assert.assertNull(request.getAcceptedUnicastNotifications());
        Assert.assertNull(request.getSubscriptionCommands());
        Assert.assertNull(request.getTopicListHash());
    }

    @Test
    public void testCreateRequest() {
        KaaClientState clientState = Mockito.mock(KaaClientState.class);
        Mockito.when(clientState.getNotificationSeqNumber()).thenReturn(new Integer(5));

        NotificationTransport transport = new DefaultNotificationTransport();
        transport.createNotificationRequest();
        transport.setClientState(clientState);
        transport.createNotificationRequest();

        NotificationSyncRequest request = transport.createNotificationRequest();
        Assert.assertEquals(new Integer(5), request.getAppStateSeqNumber());
    }

    @Test
    public void testAcceptedUnicastNotification() throws Exception  {
        KaaClientState clientState = Mockito.mock(KaaClientState.class);
        NotificationProcessor notificationProcessor = Mockito.mock(NotificationProcessor.class);

        NotificationSyncResponse response1 = new NotificationSyncResponse();
        response1.setAppStateSeqNumber(3);
        response1.setResponseStatus(SyncResponseStatus.DELTA);

        KaaChannelManager channelManagerMock = Mockito.mock(KaaChannelManager.class);

        NotificationTransport transport = new DefaultNotificationTransport();
        transport.setChannelManager(channelManagerMock);
        transport.setNotificationProcessor(notificationProcessor);
        transport.setClientState(clientState);

        Notification nf1 = new Notification("u_id1", NotificationType.CUSTOM, "uid_1", 5, ByteBuffer.wrap(new byte [] { 1, 2, 3}));
        Notification nf2 = new Notification("m_id1", NotificationType.CUSTOM, "uid_2", 3, ByteBuffer.wrap(new byte [] { 1, 2, 3}));
        Notification nf3 = new Notification("u_id2", NotificationType.CUSTOM, "uid_2", 5, ByteBuffer.wrap(new byte [] { 1, 2, 3}));

        response1.setNotifications(Arrays.asList(nf1, nf2, nf3));
        transport.onNotificationResponse(response1);

        NotificationSyncRequest request1 = transport.createNotificationRequest();
        Assert.assertTrue(request1.getAcceptedUnicastNotifications().size() == 2);

        NotificationSyncResponse response2 = new NotificationSyncResponse();
        response2.setAppStateSeqNumber(3);
        response2.setResponseStatus(SyncResponseStatus.NO_DELTA);

        transport.onNotificationResponse(response2);

        NotificationSyncRequest request2 = transport.createNotificationRequest();
        Assert.assertNull(request2.getAcceptedUnicastNotifications());
    }

    @Test
    public void onNotificationResponse() throws Exception {
        KaaClientState clientState = Mockito.mock(KaaClientState.class);
        NotificationProcessor notificationProcessor = Mockito.mock(NotificationProcessor.class);
        Mockito.when(clientState.getNotificationSeqNumber()).thenReturn(new Integer(2));
        Mockito.when(clientState.updateTopicSubscriptionInfo(Mockito.anyString(), Mockito.anyInt())).thenReturn(Boolean.TRUE);

        NotificationSyncResponse response = new NotificationSyncResponse();
        response.setAppStateSeqNumber(3);
        response.setResponseStatus(SyncResponseStatus.DELTA);

        String topicId1 = "topicId1";
        String topicId2 = "topicId2";

        KaaChannelManager channelManagerMock = Mockito.mock(KaaChannelManager.class);

        NotificationTransport transport = new DefaultNotificationTransport();
        transport.setChannelManager(channelManagerMock);
        transport.onNotificationResponse(response);
        transport.onNotificationResponse(response);
        transport.setNotificationProcessor(notificationProcessor);
        transport.onNotificationResponse(response);
        transport.setClientState(clientState);
        transport.onNotificationResponse(response);

        List<Topic> topicList = new ArrayList<>(1);
        topicList.add(new Topic(topicId1, null, SubscriptionType.MANDATORY_SUBSCRIPTION));
        topicList.add(new Topic(topicId2, null, SubscriptionType.OPTIONAL_SUBSCRIPTION));
        response.setAvailableTopics(topicList);

        Notification nf1 = new Notification(topicId2, NotificationType.CUSTOM, "uid", 5, ByteBuffer.wrap(new byte [] { 1, 2, 3}));
        Notification nf2 = new Notification(topicId1, NotificationType.CUSTOM, null, 3, ByteBuffer.wrap(new byte [] { 1, 2, 3}));
        Notification nf3 = new Notification(topicId1, NotificationType.CUSTOM, null, 6, ByteBuffer.wrap(new byte [] { 1, 2, 3}));

        response.setNotifications(Arrays.asList(nf3, nf1, nf2));

        List<Notification> expectedNotifications = Arrays.asList(nf1, nf2, nf3);

        transport.onNotificationResponse(response);

        Mockito.verify(notificationProcessor, Mockito.times(1)).notificationReceived(expectedNotifications);
        Mockito.verify(notificationProcessor, Mockito.times(1)).topicsListUpdated(topicList);
        Mockito.verify(clientState, Mockito.times(1)).updateTopicSubscriptionInfo(Mockito.eq(topicId1), Mockito.eq(3));
        Mockito.verify(clientState, Mockito.times(1)).updateTopicSubscriptionInfo(Mockito.eq(topicId1), Mockito.eq(6));

        Assert.assertEquals("uid", transport.createNotificationRequest().getAcceptedUnicastNotifications().get(0));
    }

    @Test
    public void testFilterStaleNotification() throws Exception {
        KaaClientState clientState = Mockito.mock(KaaClientState.class);
        NotificationProcessor notificationProcessor = Mockito.mock(NotificationProcessor.class);
        Mockito.when(clientState.updateTopicSubscriptionInfo(Mockito.anyString(), Mockito.anyInt())).thenReturn(Boolean.FALSE);

        NotificationSyncResponse response = new NotificationSyncResponse();
        response.setAppStateSeqNumber(3);
        response.setResponseStatus(SyncResponseStatus.DELTA);

        KaaChannelManager channelManagerMock = Mockito.mock(KaaChannelManager.class);

        NotificationTransport transport = new DefaultNotificationTransport();
        transport.setChannelManager(channelManagerMock);
        transport.setNotificationProcessor(notificationProcessor);
        transport.setClientState(clientState);

        Notification nf1 = new Notification("u_id1", NotificationType.CUSTOM, null, 3, ByteBuffer.wrap(new byte [] { 1, 2, 3}));
        Notification nf2 = new Notification("u_id1", NotificationType.CUSTOM, null, 3, ByteBuffer.wrap(new byte [] { 1, 2, 3}));

        response.setNotifications(Arrays.asList(nf1, nf2));
        transport.onNotificationResponse(response);

        List<Notification> expectedNotifications = Collections.emptyList();
        Mockito.verify(notificationProcessor, Mockito.times(1)).notificationReceived(expectedNotifications);
    }

    @Test
    public void testTopicState() {
        KaaClientState clientState = Mockito.mock(KaaClientState.class);

        Map<String, Integer> nfSubscriptions = new HashMap<String, Integer>();
        nfSubscriptions.put("topic1", 10);
        nfSubscriptions.put("topic2", 3);

        Mockito.when(clientState.getNfSubscriptions()).thenReturn(nfSubscriptions);

        NotificationTransport transport = new DefaultNotificationTransport();

        Assert.assertNull(transport.createEmptyNotificationRequest());

        transport.setClientState(clientState);

        NotificationSyncRequest request = transport.createEmptyNotificationRequest();

        Assert.assertTrue(request.getTopicStates().size() == 2);
    }
}
