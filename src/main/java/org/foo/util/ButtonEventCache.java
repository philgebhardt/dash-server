package org.foo.util;

import org.foo.button.model.ButtonEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phil on 3/6/16.
 */
public class ButtonEventCache {

    Map<String,ButtonEventCacheEntry> entries;
    private long expirationTime;

    public ButtonEventCache() {
        entries = new HashMap<>();
    }
    public int put(ButtonEvent event) {
        String id = event.getId();
        int newCount = 1;
        if(contains(id)) {
            ButtonEventCacheEntry entry = entries.get(id);
            newCount = entry.getCount()+1;
            entry.setCount(newCount);
            entry.setDttm(event.getDtmOccured().getTime());
        } else {
            entries.put(id, new ButtonEventCacheEntry()
                    .withCount(newCount)
                    .withDttm(event.getDtmOccured().getTime()));
        }
        return newCount;
    }

    public boolean contains(String id) {
        if (!entries.containsKey(id)) {
            return false;
        } else {
            ButtonEventCacheEntry entry = entries.get(id);
            return (entry.getDttm() - System.currentTimeMillis()) < getExpirationTime();
        }
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
