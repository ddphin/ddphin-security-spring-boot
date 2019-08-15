package com.ddphin.security.demo.system.id;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * EnvironmentConfigurer
 *
 * @Date 2019/8/11 下午3:32
 * @Author ddphin
 */
@Slf4j
@Configuration
public class NodeEnvironmentConfigurer {
    @Bean
    public NodeEnvironment nodeEnvironment(Environment environment) {
        NodeEnvironment env = new NodeEnvironment();
        Integer dataCenterId = Integer.valueOf(environment.getProperty("DATA_CENTER_ID", "0"));
        Integer workerId = Integer.valueOf(environment.getProperty("WORKER_ID", "0"));
        log.info("NodeEnvironment\n" +
                "    DATA_CENTER_ID={}\n" +
                "         WORKER_ID={}",
                dataCenterId,
                workerId);
        env.setDataCenterId(dataCenterId);
        env.setWorkerId(workerId);
        return env;
    }

    @Bean
    public SnowFlakeIDWorkerFactory snowFlakeIDWorkerFactory(NodeEnvironment nodeEnvironment) {
        return new SnowFlakeIDWorkerFactory(nodeEnvironment.getWorkerId(), nodeEnvironment.getDataCenterId());
    }
}
