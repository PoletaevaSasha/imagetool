package com.chekanova.imagetool.config;

import com.chekanova.imagetool.enums.ParallelingStrategyType;
import com.chekanova.imagetool.service.strategy.ParallelingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ExecutorServiceParallelingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ForkJoinPoolParallelingStrategy;
import com.chekanova.imagetool.service.strategy.impl.SingleThreadParallelingStrategy;
import com.chekanova.imagetool.service.strategy.impl.MultipleThreadParallelingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

import static com.chekanova.imagetool.enums.ParallelingStrategyType.EXECUTOR_SERVICE;
import static com.chekanova.imagetool.enums.ParallelingStrategyType.FORK_JOIN;
import static com.chekanova.imagetool.enums.ParallelingStrategyType.SINGLE;
import static com.chekanova.imagetool.enums.ParallelingStrategyType.THREADS;

@Configuration
@RequiredArgsConstructor
public class ProcessingStrategiesConfig {
    private final SingleThreadParallelingStrategy singleProcessingStrategy;
    private final MultipleThreadParallelingStrategy threadProcessingStrategy;
    private final ExecutorServiceParallelingStrategy executorServiceProcessingStrategy;
    private final ForkJoinPoolParallelingStrategy forkJoinPoolParallelingStrategy;

    @Bean
    public Map<ParallelingStrategyType, ParallelingStrategy> processingStrategies() {
        EnumMap<ParallelingStrategyType, ParallelingStrategy> strategies = new EnumMap<>(ParallelingStrategyType.class);
        strategies.put(SINGLE, singleProcessingStrategy);
        strategies.put(THREADS, threadProcessingStrategy);
        strategies.put(EXECUTOR_SERVICE, executorServiceProcessingStrategy);
        strategies.put(FORK_JOIN, forkJoinPoolParallelingStrategy);
        return strategies;
    }
}
