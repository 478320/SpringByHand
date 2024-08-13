package org.springframework.core.metrics;

/**
 *
 */
public class DefaultApplicationStartup implements ApplicationStartup{
    private static final DefaultStartupStep DEFAULT_STARTUP_STEP = new DefaultStartupStep();

    @Override
    public DefaultStartupStep start(String name) {
        return DEFAULT_STARTUP_STEP;
    }


    static class DefaultStartupStep implements StartupStep {
        @Override
        public void end() {
        }

    }
}
