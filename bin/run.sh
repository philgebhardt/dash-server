#!/bin/bash

function get_iface() {
  #grep `hostname` /etc/hosts | awk '{print $1}'
  echo -n "192.168.0.109"
}
java -DSQLITE_CONFIG_MULTITHREAD -jar target/dash-0.0.1-SNAPSHOT-jar-with-dependencies.jar iface="$(get_iface)" filter=usedb 2>&1 | tee run.out
