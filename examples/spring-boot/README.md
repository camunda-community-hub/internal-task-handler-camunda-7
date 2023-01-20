# Internal Task Handler Spring Boot Example

This example contains a simple document handling process with one pitfall:

The DMS the document should be archived to is very old, so saving a document there must not happen in parallel AND it takes between 5 and 10 seconds to archive a document.

The rest of the process should not be impacted by this problem.

## Solution

The internal task handler is configured with 2 different `ExecutorService` instances:

* The general one which overrides the default. It has a special bean name `internalTaskClientExecutor`. This executor will execute all internal task handlers that have no dedicated executor service assigned
* A special one only for the `DocumentArchiving` handler. They are bound to each other via bean name `dmsExecutor`. This executor will only execute internal task handlers that have the bean name assigned as `executorBeanName`.
