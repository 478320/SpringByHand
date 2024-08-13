package org.springframework.core.metrics;

import java.util.function.Supplier;

public interface StartupStep {

    void end();

}