package org.opendaylight.controller.betterhosttracker.plugin.internal;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.opendaylight.controller.md.sal.common.api.data.DataChangeEvent;
import org.opendaylight.controller.sal.binding.api.data.DataBrokerService;
import org.opendaylight.controller.sal.binding.api.data.DataChangeListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.address.tracker.rev140617.AddressCapableNodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.address.tracker.rev140617.address.node.connector.Addresses;
import org.opendaylight.yang.gen.v1.urn.opendaylight.betterhosttracker.rev140624.Host;
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

    private Set<Host> hosts;

    BetterHostTrackerImpl(DataBrokerService dataService) {
        Preconditions.checkNotNull(dataService, "dataBrokerService should not be null.");
        this.dataService = dataService;
        this.hosts = Collections.synchronizedSet(new HashSet());
    }

    @Override
    public void onDataChanged(DataChangeEvent<InstanceIdentifier<?>, DataObject> change) {
        if (change == null) {
            log.info("In onDataChanged: No Processing done as dataChangeEvent is null.");
        }
        Map<InstanceIdentifier<?>, DataObject> linkOriginalData = change.getOriginalOperationalData();
        Map<InstanceIdentifier<?>, DataObject> linkUpdatedData = change.getUpdatedOperationalData();
        //aanm: we read from the updated operational data, right?
        for (Map.Entry<InstanceIdentifier<?>, DataObject> entrySet : linkUpdatedData.entrySet()) {
            InstanceIdentifier<?> key = entrySet.getKey();
            DataObject dataObject = entrySet.getValue();
            if (key.getClass().isInstance(InstanceIdentifier.class)
                    && dataObject.getClass().isInstance(Addresses.class)) {
                Addresses addrs = (Addresses) dataObject;
                InstanceIdentifier<Addresses> iia = (InstanceIdentifier<Addresses>) key;
                InstanceIdentifier<NodeConnector> iinc = iia.firstIdentifierOf(NodeConnector.class);
                NodeConnector nodeConnector = (NodeConnector) dataService.readOperationalData(iinc);
                //Host h = null;// = new Host();
                //HostBuilder hb = new HostBuilder();
                //hosts.add(hb);
            }
        }
        Iterator<DataObject> iterator = linkOriginalData.values().iterator();
        DataObject next = iterator.next();
        AddressCapableNodeConnector t = (AddressCapableNodeConnector) next;
        t.getAddresses().get(0).getIp();
    }

    void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void registerAsDataChangeListener() {
        InstanceIdentifier<Addresses> addrCapableNodeConnectors = //
                InstanceIdentifier.builder(Nodes.class) //
                .child(Node.class).child(NodeConnector.class) //
                .augmentation(AddressCapableNodeConnector.class)//
                .child(Addresses.class).build();

        dataService.registerDataChangeListener(addrCapableNodeConnectors, this);
    }
}
