package org.opendaylight.mdhosttracker.plugin.internal;

import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareProvider;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanm
 * @version 0.0.1
 */
public class MdHostTrackerProvider extends AbstractBindingAwareProvider
        implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(MdHostTrackerProvider.class);

    MdHostTrackerImpl mdHostTrackerImpl;

    public MdHostTrackerProvider() {
//        betterHostTrackerImpl = new BetterHostTrackerImpl();
    }

    @Override
    public void close() throws Exception {
        if (mdHostTrackerImpl != null) {
            mdHostTrackerImpl.close();
        }
    }

    @Override
    public void onSessionInitiated(ProviderContext session) {
//        session.addRpcImplementation(BetterHostTrackerService.class, betterHostTrackerImpl);
    }

}
