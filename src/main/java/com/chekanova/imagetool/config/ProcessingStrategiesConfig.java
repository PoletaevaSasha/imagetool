package com.chekanova.imagetool.config;

import com.chekanova.imagetool.enums.MultithreadingStrategy;
import com.chekanova.imagetool.service.strategy.ProcessingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ExecutorServiceProcessingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ForkJoinPoolStrategy;
import com.chekanova.imagetool.service.strategy.impl.SingleProcessingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ThreadProcessingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

import static com.chekanova.imagetool.enums.MultithreadingStrategy.EXECUTOR_SERVICE;
import static com.chekanova.imagetool.enums.MultithreadingStrategy.FORK_JOIN;
import static com.chekanova.imagetool.enums.MultithreadingStrategy.SINGLE;
import static com.chekanova.imagetool.enums.MultithreadingStrategy.THREADS;

@Configuration
@RequiredArgsConstructor
public class ProcessingStrategiesConfig {
    private final SingleProcessingStrategy singleProcessingStrategy;
    private final ThreadProcessingStrategy threadProcessingStrategy;
    private final ExecutorServiceProcessingStrategy executorServiceProcessingStrategy;
    private final ForkJoinPoolStrategy forkJoinPoolStrategy;

    @Bean
    public Map<MultithreadingStrategy, ProcessingStrategy> processingStrategies() {
        EnumMap<MultithreadingStrategy, ProcessingStrategy> strategies = new EnumMap<>(MultithreadingStrategy.class);
        strategies.put(SINGLE, singleProcessingStrategy);
        strategies.put(THREADS, threadProcessingStrategy);
        strategies.put(EXECUTOR_SERVICE, executorServiceProcessingStrategy);
        strategies.put(FORK_JOIN, forkJoinPoolStrategy);
        return strategies;
    }
}
