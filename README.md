# Open Liberty Embedded Message Server Demo

A simple repository demonstrating how to use the [embedded message server](https://www.ibm.com/docs/en/was-liberty/base?topic=providers-embedded-jms-messaging-provider) in the [Open Liberty](https://openliberty.io/) web application server for basic [async](https://eclipse-ee4j.github.io/jakartaee-tutorial/#using-message-driven-beans-to-receive-messages-asynchronously) [point-to-point](https://eclipse-ee4j.github.io/jakartaee-tutorial/#point-to-point-messaging-style) messaging.

## Requirements
1. A Java 8+ JDK (if using a version higher than 8, you might want to tweak the POM)
2. Apache Maven

## Running

Spin up a dev server using:

`mvn liberty:dev`

This is something that anyone who's used Open Liberty before should already be familiar with

This application has a single [Servlet 3.0+](learn/main) based endpoint: `GET http://localhost:9080/learn/main`. The response of that endpoint is insignificant. What's important is what happens behind the scenes.

The application is configured to have a single JMS Queue that the servlet writes to on each request. The message written to that queue is a map containing two fields:
1. endpoint: the url of the endpoint being hit
2. requestCount: the number of times the endpoint was hit

The request count is recorded by means of a `RequestCountUtil` object - a Dependent [CDI bean](https://eclipse-ee4j.github.io/jakartaee-tutorial/#about-beans). It's not Java code if it's not prematurely over-engineered.

These messages are asynchronously processed by a [Message-Driven Bean](https://eclipse-ee4j.github.io/jakartaee-tutorial/#receiving-messages-asynchronously-using-a-message-driven-bean) - `RequestCountReporter` which merely formats and prints out the data. In a real application the message sent and the action performed on the message wouldn't be as trivial, but this is a demo of how to configure everything (see `server.xml` as well as the annotations used in the source code) - so the simpler the better.