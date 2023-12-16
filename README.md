# smack-sint-server-extensions
Additional tests that are server-oriented to add to those in [Smack's Integration Test Framework](https://download.igniterealtime.org/smack/dailybuilds/sinttest-javadoc/org/igniterealtime/smack/inttest/package-summary.html).

# Run tests
To run tests, edit the pom.xml to match your settings, then run `mvn exec:java`

This is an example Run/Debug configuration (which you can use in Intellij):

- *Main class*: `org.igniterealtime.smack.inttest.SmackIntegrationTestFramework`
- *VM options*: `-Dsinttest.service=example.org -Dsinttest.adminAccountUsername=admin -Dsinttest.adminAccountPassword=admin -Dsinttest.securityMode=disabled -Dsinttest.enabledTests=PubSubIntegrationTest`

Running a configuration like this will make the PubSubIntegrationTest tests run against an XMPP server running on the XMPP domain 'example.org', using an administrator account that has as username/password: admin/admin.

This is the most basic configuration that you can use to run tests against a locally installed Openfire server that is running in 'demoboot' mode.
