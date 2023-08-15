module laokou.auth.domain {
    requires spring.security.oauth2.core;
    requires com.fasterxml.jackson.annotation;
    requires spring.security.core;
    requires lombok;
    exports org.laokou.auth.domain.user;
    exports org.laokou.auth.domain.gateway;
    exports org.laokou.auth.domain.log;
    exports org.laokou.auth.domain.auth;
}