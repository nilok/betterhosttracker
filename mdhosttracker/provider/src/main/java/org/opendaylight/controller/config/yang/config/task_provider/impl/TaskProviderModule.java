package org.opendaylight.controller.config.yang.config.task_provider.impl;

import org.opendaylight.betterhosttracker.provider.TaskProvider;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yang.gen.v1.opendaylight.sample.rev140407.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskProviderModule extends org.opendaylight.controller.config.yang.config.task_provider.impl.AbstractTaskProviderModule {
    private static final Logger log = LoggerFactory.getLogger(TaskProviderModule.class);

    public TaskProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public TaskProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.controller.config.yang.config.task_provider.impl.TaskProviderModule oldModule, AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public AutoCloseable createInstance() {
        final TaskProvider appProvider = new TaskProvider();

        DataBroker dataBrokerService = getDataBrokerDependency();
        appProvider.setDataService(dataBrokerService);
        
        RpcProviderRegistry rpcRegistryDependency = getRpcRegistryDependency();
        final BindingAwareBroker.RpcRegistration<TaskService> rpcRegistration = 
                                rpcRegistryDependency
                                    .addRpcImplementation(TaskService.class, appProvider);

        //retrieves the notification service for publishing notifications
        NotificationProviderService notificationService = getNotificationServiceDependency();
        
                
        // Wrap toaster as AutoCloseable and close registrations to md-sal at
        // close()
        final class CloseResources implements AutoCloseable {

            @Override
            public void close() throws Exception {
                rpcRegistration.close();
                appProvider.close();
                log.info("TaskProvider (instance {}) torn down.", this);
            }
        }

        AutoCloseable ret = new CloseResources();
        log.info("TaskProvider (instance {}) initialized.", ret);
        return ret;
    }


}
