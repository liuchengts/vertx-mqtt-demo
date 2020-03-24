#!/usr/bin/env bash
# !/bin/sh
ipps=`route | grep "default"`
IP=`echo ${ipps} | awk '{print $2}'`
echo "ip:$IP"
macps=`arp -a ${IP}`
MAC=`echo ${macps} | awk '{print $4}'`
echo "mac:$MAC"