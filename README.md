# ICEWall 
a lightweight application firewall

- Support defense sql injection attacks

- Supports defense against xxs attacks

- Support for defense MongoDB injection attacks

- Support Gzip

- White list support

- No need to modify the original code, no invasion

Load in maven:
```xml
    <dependency>
        <groupId>com.cybermkd</groupId>
        <artifactId>ICEWall</artifactId>
        <version>1.0.0.0</version>
    </dependency>
```

How to use it (configured on web.xml):

```xml
    <filter>
        <filter-name>ICEWall</filter-name>
        <filter-class>com.cybermkd.icewall.ICEWallFilter</filter-class>
        <init-param>
            <param-name>whitelists</param-name>
            <param-value>/test/a.html;</param-value>
        </init-param>
        <init-param>
            <param-name>xxs</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>sql</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>mongo</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>gzip</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>


    <filter-mapping>
        <filter-name>ICEWall</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```