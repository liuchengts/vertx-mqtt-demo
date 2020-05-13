#!/usr/bin/env bash
iptables -Z
var=`date "+%Y-%m-%d %H:%M:%S"`
echo "Flow set to zero at ${var}" > /root/${DEVICE_NO}/flow.txt
