### Backend Specifications

Add Maven dependencies:

```xml
<dependency>
  <groupId>io.vertx</groupId>
  <artifactId>vertx-service-discovery</artifactId>
  <!--<version>${vertx.version}</version>-->
</dependency>

<dependency>
    <groupId>org.dizitart</groupId>
    <artifactId>potassium-nitrite</artifactId>
    <version>{version}</version>
</dependency>
```

Implement `ServiceDiscoveryBackend` with `LocalStorageBackendService`


Add this file `io.vertx.servicediscovery.spi.ServiceDiscoveryBackend` to `resources/META-INF/services` with this content `garden.bots.scarlet.backend.LocalStorageBackendService`


<hr>
draft
val adminToken = System.getenv("SCARLET_ADMIN_TOKEN") ?: ""
