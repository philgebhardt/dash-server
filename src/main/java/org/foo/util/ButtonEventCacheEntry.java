package org.foo.util;

/**
 * Created by phil on 3/6/16.
 */
public class ButtonEventCacheEntry {
    private long dttm;
    private int count;

    public ButtonEventCacheEntry() {}
    public ButtonEventCacheEntry(long dttm, int count) {
        this.dttm = dttm;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getDttm() {
        return dttm;
    }

    public void setDttm(long dttm) {
        this.dttm = dttm;
    }

    public ButtonEventCacheEntry withDttm(long dttm) {
        ButtonEventCacheEntry entry = clone();
        entry.setDttm(dttm);
        return entry;
    }

    public ButtonEventCacheEntry withCount(int count) {
        ButtonEventCacheEntry entry = clone();
        entry.setCount(count);
        return entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ButtonEventCacheEntry that = (ButtonEventCacheEntry) o;

        if (getDttm() != that.getDttm()) return false;
        return getCount() == that.getCount();

    }

    @Override
    public int hashCode() {
        int result = (int) (getDttm() ^ (getDttm() >>> 32));
        result = 31 * result + getCount();
        return result;
    }

    @Override
    public ButtonEventCacheEntry clone() {
        return new ButtonEventCacheEntry(getDttm(), getCount());
    }

    @Override
    public String toString() {
        return String.format("ButtonEventCacheEntry[dttm=%d,count=%d]", getDttm(), getCount());
    }
}
