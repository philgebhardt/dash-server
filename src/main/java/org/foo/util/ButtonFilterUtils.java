package org.foo.util;

import org.foo.button.model.Button;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by phil on 2/15/16.
 */
public class ButtonFilterUtils {

    private static final String NOT = " not ";
    private static final String AND = " and ";
    private static final String ARP = "arp";
    private static final String OR = " or ";
    private static final String DEST_GATEWAY = "dst host 192.168.0.1";

    public static String arpIncludeButtons(Collection<Button> buttons) {
        String filterStr=null;
        if(buttons!=null && !buttons.isEmpty()) {
            Iterator<Button> iter = buttons.iterator();
            StringBuilder sb = new StringBuilder(DEST_GATEWAY+AND+ARP+AND+"(ether src ");
            do {
                sb.append(iter.next().getId());
                if(iter.hasNext()) {
                    sb.append(OR);
                }
            } while(iter.hasNext());
            sb.append(")");
            filterStr=sb.toString();
        }
        return filterStr;
    }

    public static String arpExcludeButtons(Collection<Button> buttons) {
        String filterStr=null;
        if(buttons!=null && !buttons.isEmpty()) {
            Iterator<Button> iter = buttons.iterator();
            StringBuilder sb = new StringBuilder(DEST_GATEWAY+AND+ARP+AND+NOT+"(ether src ");
            do {
                sb.append(iter.next().getId());
                if(iter.hasNext()) {
                    sb.append(OR);
                }
            } while(iter.hasNext());
            sb.append(")");
            filterStr=sb.toString();
        }
        return filterStr;
    }
}
