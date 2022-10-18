# fics-api-client
This project contains a SOAP client library that can be used to call FICS web services. WSDLs and XSDs are located in: src/main/resources

This project can be utilized in your Backbase integration services as a Maven dependency. Simply include the following maven coordinates in the dependency section of your service's pom.xml

    <groupId>com.backbase.accelerators</groupId>
    <artifactId>fics-api-client</artifactId>
    <version>1.0.0</version>

# Build this project
From the root directory of this project, run:

mvn clean install

This will compile the project and generate Java classes from the WSDLs and XSDs the resources folder. The generated classes can be found in: target/generated-sources/cxf

### Example usage - Defining `application.yml` configuration:

Properties should be defined in your Backbase integration service as follows. Please obtain actual configuration values
from your customer.

```yaml
fics:
  client:
    baseUrl: http://fics-webservice-host/wsFICS/wsfics.asmx
```

```java

@Data
@Configuration
@ConfigurationProperties("fics.client")
public class FicsClientProperties {

    private String baseUrl;
}
```

### Example usage - Defining a Spring Bean in Your Integration Service:

The below example wires up the `FicsClient` as a Spring bean.

```java

@Configuration
public class FicsClientConfiguration {

    @Bean
    @SneakyThrows
    public FicsClient ficsClient(FicsClientProperties ficsClientProperties) {
        WsFICS wsFics = new WsFICS(new URL(ficsClientProperties.getBaseUrl())).getWsFICSSoap();
        return new FicsClient(wsFics);
    }
}
```