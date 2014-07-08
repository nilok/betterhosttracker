package org.opendaylight.mdhosttracker.plugin.internal;

import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareConsumer;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
/*import org.opendaylight.yang.gen.v1.urn.opendaylight.address.tracker.rev140617.address.node.connector.Addresses;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanm
 * @version 0.0.1
 */
public class MdHostTrackerConsumer extends AbstractBindingAwareConsumer
        implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(MdHostTrackerConsumer.class);

    MdHostTrackerImpl mdHostTrackerImpl;

    public MdHostTrackerConsumer() {
    }

    @Override
    public void onSessionInitialized(ConsumerContext session) {
        DataBroker dataService = session.<DataBroker>getSALService(DataBroker.class);
        mdHostTrackerImpl = new MdHostTrackerImpl(dataService);
        mdHostTrackerImpl.registerAsDataChangeListener();
    }

    @Override
    public void close() throws Exception {
        if (mdHostTrackerImpl != null) {
            mdHostTrackerImpl.close();
        }
    }

}
