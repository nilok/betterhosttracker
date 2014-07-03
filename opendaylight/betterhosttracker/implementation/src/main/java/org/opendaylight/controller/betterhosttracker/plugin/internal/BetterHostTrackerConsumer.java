package org.opendaylight.controller.betterhosttracker.plugin.internal;

import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareConsumer;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.sal.binding.api.data.DataBrokerService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.address.tracker.rev140617.address.node.connector.Addresses;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanm
 * @version 0.0.1
 */
public class BetterHostTrackerConsumer extends AbstractBindingAwareConsumer
        implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(BetterHostTrackerConsumer.class);

    BetterHostTrackerImpl betterHostTrackerImpl;

    public BetterHostTrackerConsumer() {
    }

    @Override
    public void onSessionInitialized(ConsumerContext session) {
        DataBrokerService dataService = session.<DataBrokerService>getSALService(DataBrokerService.class);
        betterHostTrackerImpl = new BetterHostTrackerImpl(dataService);
        betterHostTrackerImpl.registerAsDataChangeListener();
    }

    @Override
    public void close() throws Exception {
        if (betterHostTrackerImpl != null) {
            betterHostTrackerImpl.close();
        }
    }

}
