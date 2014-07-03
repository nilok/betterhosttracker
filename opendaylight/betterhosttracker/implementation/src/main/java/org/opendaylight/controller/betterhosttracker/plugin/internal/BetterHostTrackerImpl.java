package org.opendaylight.controller.betterhosttracker.plugin.internal;

import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import org.opendaylight.controller.md.sal.common.api.data.DataChangeEvent;
import org.opendaylight.controller.sal.binding.api.data.DataBrokerService;
import org.opendaylight.controller.sal.binding.api.data.DataChangeListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.address.tracker.rev140617.AddressCapableNodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aanm
 * @version 0.0.1
 */
public class BetterHostTrackerImpl implements DataChangeListener {

    private static final Logger log = LoggerFactory.getLogger(BetterHostTrackerImpl.class);

    private DataBrokerService dataService;

    BetterHostTrackerImpl(DataBrokerService dataService) {
        Preconditions.checkNotNull(dataService, "dataBrokerService should not be null.");
        this.dataService = dataService;
    }

    @Override
    public void onDataChanged(DataChangeEvent<InstanceIdentifier<?>, DataObject> change) {
        if (change == null) {
            log.info("In onDataChanged: No Processing done as dataChangeEvent is null.");
        }
        Map<InstanceIdentifier<?>, DataObject> linkOriginalData = change.getOriginalOperationalData();
        Map<InstanceIdentifier<?>, DataObject> linkUpdatedData = change.getUpdatedOperationalData();
        Iterator<DataObject> iterator = linkOriginalData.values().iterator();
        DataObject next = iterator.next();
        AddressCapableNodeConnector t = (AddressCapableNodeConnector) next;
        t.getAddresses().get(0).getIp();
        //aanm: How to read an IP address from the AddressCapableNodeConnector, is the DataObject?
    }

    void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void registerAsDataChangeListener() {
        InstanceIdentifier<AddressCapableNodeConnector> addrCapableNodeConnectors = //
                InstanceIdentifier.builder(Nodes.class) //
                .child(Node.class).child(NodeConnector.class) //
                .augmentation(AddressCapableNodeConnector.class).build();

        dataService.registerDataChangeListener(addrCapableNodeConnectors, this);
    }
}
