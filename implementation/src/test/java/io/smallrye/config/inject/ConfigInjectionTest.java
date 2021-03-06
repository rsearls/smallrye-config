package io.smallrye.config.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;

public class ConfigInjectionTest extends InjectionTest {
    @Rule
    public WeldInitiator weld = WeldInitiator.from(ConfigProducer.class, ConfigBean.class)
            .addBeans()
            .activate(ApplicationScoped.class)
            .inject(this)
            .build();

    @Inject
    private ConfigBean configBean;

    @Test
    public void inject() {
        assertEquals("1234", configBean.getMyProp());
        assertEquals("1234", configBean.getExpansion());
        assertEquals("12345678", configBean.getSecret());

        assertThrows("Not allowed to access secret key secret", SecurityException.class,
                () -> configBean.getConfig().getValue("secret", String.class));
    }

    @ApplicationScoped
    public static class ConfigBean {
        @Inject
        @ConfigProperty(name = "my.prop")
        private String myProp;
        @Inject
        @ConfigProperty(name = "expansion")
        private String expansion;
        @Inject
        @ConfigProperty(name = "secret")
        private String secret;
        @Inject
        private Config config;

        String getMyProp() {
            return myProp;
        }

        String getExpansion() {
            return expansion;
        }

        String getSecret() {
            return secret;
        }

        Config getConfig() {
            return config;
        }
    }
}
