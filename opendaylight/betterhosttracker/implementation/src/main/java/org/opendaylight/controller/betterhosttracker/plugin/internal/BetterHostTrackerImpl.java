package org.opendaylight.controller.betterhosttracker.plugin.internal;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.opendaylight.controller.md.sal.common.api.data.DataChangeEvent;
import org.opendaylight.controller.sal.binding.api.data.DataBrokerService;
import org.opendaylight.controller.sal.binding.api.data.DataChangeListener;
import org.opendaylight.controller.sal.binding.api.data.DataModificationTransaction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.address.tracker.rev140617.AddressCapableNodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.address.tracker.rev140617.address.node.connector.Addresses;
import org.opendaylight.yang.gen.v1.urn.opendaylight.betterhosttracker.rev140624.hosts.Host;
import org.opendaylight.yang.gen.v1.urn.opendaylight.betterhosttracker.rev140624.Hosts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.betterhosttracker.rev140624.HostsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.betterhosttracker.rev140624.hosts.HostBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.betterhosttracker.rev140624.HostId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.betterhosttracker.rev140624.hosts.HostKey;
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
        //aanm: we read from the updated operational data, right?
        for (Map.Entry<InstanceIdentifier<?>, DataObject> entrySet : linkUpdatedData.entrySet()) {
            InstanceIdentifier<?> key = entrySet.getKey();
            DataObject dataObject = entrySet.getValue();
            if (dataObject instanceof Addresses) {
                Addresses addrs = (Addresses) dataObject;
                InstanceIdentifier<NodeConnector> iinc = key.firstIdentifierOf(NodeConnector.class);
                NodeConnector nodeConnector = (NodeConnector) dataService.readOperationalData(iinc);
                //Host h = null;// = new Host();
                //HostBuilder hb = new HostBuilder();
                //hosts.add(hb);

                Host host = new HostBuilder().setAddresses(Arrays.asList(addrs)).setId(new HostId(addrs.getMac().getValue())).build();
                InstanceIdentifier<Host> hostId = InstanceIdentifier.builder(Hosts.class).child(Host.class, host.getKey()).build();
                DataModificationTransaction txn = dataService.beginTransaction();
                txn.putOperationalData(hostId, host);
                txn.commit();
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

        InstanceIdentifier<Hosts> hostsId = InstanceIdentifier.builder(Hosts.class).build();
        DataModificationTransaction tx = dataService.beginTransaction();
        Hosts hostsContainer = new HostsBuilder().build();
        tx.putOperationalData(hostsId, hostsContainer);
        try {
            tx.commit().get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        dataService.registerDataChangeListener(addrCapableNodeConnectors, this);
    }
}
