package org.opendaylight.mdhosttracker.plugin.internal;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareConsumer;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.sal.utils.GlobalConstants;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.opendaylight.controller.topologymanager.ITopologyManager;
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
        log.trace("onSessionInitialized");
        DataBroker dataService = session.<DataBroker>getSALService(DataBroker.class);
        ITopologyManager topologyManager = (ITopologyManager) ServiceHelper.getInstance(ITopologyManager.class, GlobalConstants.DEFAULT.toString(), this);
        mdHostTrackerImpl = new MdHostTrackerImpl(dataService, topologyManager);
        mdHostTrackerImpl.registerAsDataChangeListener();
    }

    @Override
    public void close() throws Exception {
        if (mdHostTrackerImpl != null) {
            mdHostTrackerImpl.close();
        }
    }

}
