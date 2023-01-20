# Internal Task Handler

This project provides you with artifacts that allow the embedded usage of the external task pattern.

## Why?

Camunda Platform 8 is coming up, and it does not provide an embedded way of handling service tasks. Everything is a *job*, like an external task.

In preparation to migrate to Camunda 8, this project provides you with embedded handling of external tasks:

* Create an embedded application, fully capable of handling everything internally
* Model and execute processes using the external task pattern, along with all its benefits

## How?

Currently, this project provides you with 2 modules:

* [Core](./extension/core): It contains the basic functionality and allows manual bootstrapping of client and worker.
* [Spring Boot Starter](./extension/spring-boot-starter): Built on the core module, it relies on Spring Boot to manage client and worker lifecycle and allows annotation-based worker registration.

There are examples in place for each of the implementations:

* [Spring Boot](./examples/spring-boot): It uses the `camunda-bpm-spring-boot-starter` and `internal-task-handler-spring-boot-starter` to bootstrap the process engine and the internal task client.

## Getting started

Just like the usual way of creating a process application with the external task pattern, create a process with external service tasks.

Then, instead of bootstrap a remote client, create the internal task client and connect it to the engine java api.

Using the internal task client, you can create internal task workers.
