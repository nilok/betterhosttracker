package org.opendaylight.controller.config.yang.config.task_consumer.impl;

import org.opendaylight.betterhosttracker.consumer.TaskConsumerImpl;
import org.opendaylight.betterhosttracker.consumer.TaskConsumerService;
import org.opendaylight.yang.gen.v1.opendaylight.sample.rev140407.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TaskConsumerModule extends org.opendaylight.controller.config.yang.config.task_consumer.impl.AbstractTaskConsumerModule {
    private static final Logger log = LoggerFactory.getLogger(TaskConsumerModule.class);

    public TaskConsumerModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public TaskConsumerModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.controller.config.yang.config.task_consumer.impl.TaskConsumerModule oldModule, AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public AutoCloseable createInstance() {
        TaskService service = getRpcRegistryDependency().getRpcService(TaskService.class);

        final TaskConsumerImpl consumerImpl = new TaskConsumerImpl(service);


        final class AutoCloseableService implements TaskConsumerService, AutoCloseable {

            @Override
            public void close() throws Exception {
                log.info("TaskConsumerService (instance {}) torn down.", this);
            }

            @Override
            public void createEntry(Map<String, String> data) {
                consumerImpl.createEntry(data);
            }
        }

        AutoCloseable ret = new AutoCloseableService();
        log.info("TaskConsumerService (instance {}) initialized.", ret );
        return ret;
    }

}
