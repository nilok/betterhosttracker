package org.opendaylight.betterhosttracker.consumer;

import java.util.Map;

public interface TaskConsumerService {
    public void createEntry(Map<String, String> data);

}
