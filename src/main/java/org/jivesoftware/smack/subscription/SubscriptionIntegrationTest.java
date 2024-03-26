/**
 *
 * Copyright 2021-2023 Guus der Kinderen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smack.subscription;

import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PresenceTypeFilter;
import org.jivesoftware.smack.filter.StanzaExtensionFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.StandardExtensionElement;

import org.igniterealtime.smack.inttest.AbstractSmackIntegrationTest;
import org.igniterealtime.smack.inttest.SmackIntegrationTestEnvironment;
import org.igniterealtime.smack.inttest.annotations.SmackIntegrationTest;
import org.igniterealtime.smack.inttest.util.IntegrationTestRosterUtil;
import org.igniterealtime.smack.inttest.util.SimpleResultSyncPoint;

/**
 * Integration tests that verify that sent presence subscription requests are received as intended.
 *
 * @author Guus der Kinderen, guus.der.kinderen@gmail.com
 */
public class SubscriptionIntegrationTest extends AbstractSmackIntegrationTest {

    public SubscriptionIntegrationTest(SmackIntegrationTestEnvironment environment) {
        super(environment);
    }

    /**
     * This test verifies that a subscription request is received.
     *
     * @throws Exception on anything unexpected or undesired.
     */
    @SmackIntegrationTest
    public void testSubscriptionRequest() throws Exception {
        IntegrationTestRosterUtil.ensureBothAccountsAreNotInEachOthersRoster(conOne, conTwo);

        final Presence subscriptionRequest = conTwo.getStanzaFactory().buildPresenceStanza()
                .ofType(Presence.Type.subscribe)
                .to(conOne.getUser())
                .build();

        final SimpleResultSyncPoint received = new SimpleResultSyncPoint();

        final StanzaFilter resultFilter = new AndFilter(
            PresenceTypeFilter.SUBSCRIBE,
            FromMatchesFilter.createBare(conTwo.getUser())
        );

        conOne.addAsyncStanzaListener(p -> received.signal(), resultFilter);

        conTwo.sendStanza(subscriptionRequest);
        received.waitForResult(timeout);
    }

    /**
     * When a subscription request is made, the stanza can have additional extension elements. This test verifies that
     * such extension elements are received.
     *
     * @throws Exception on anything unexpected or undesired.
     */
    @SmackIntegrationTest
    public void testSubscriptionRequestWithExtension() throws Exception {
        IntegrationTestRosterUtil.ensureBothAccountsAreNotInEachOthersRoster(conOne, conTwo);

        final Presence subscriptionRequest = conTwo.getStanzaFactory().buildPresenceStanza()
                .ofType(Presence.Type.subscribe)
                .to(conOne.getUser())
                .addExtension(new StandardExtensionElement("test", "org.example.test"))
                .build();

        final SimpleResultSyncPoint received = new SimpleResultSyncPoint();

        final StanzaFilter resultFilter = new AndFilter(
            PresenceTypeFilter.SUBSCRIBE,
            FromMatchesFilter.createBare(conTwo.getUser()),
            new StanzaExtensionFilter("test", "org.example.test")
        );

        conOne.addAsyncStanzaListener(p -> received.signal(), resultFilter);

        conTwo.sendStanza(subscriptionRequest);
        received.waitForResult(timeout);
    }
}
