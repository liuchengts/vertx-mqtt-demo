#!/usr/bin/env bash
# 分割线
echo "*******************************************************************" >> /root/${DEVICE_NO}/flow.txt
# 时间戳
var=`date "+%Y-%m-%d %H:%M:%S"`
echo "Flow write at ${var}" >> /root/${DEVICE_NO}/flow.txt
# 写入流量数据
iptables -n -v -t filter -L INPUT >> /root/${DEVICE_NO}/flow.txt
iptables -n -v -t filter -L OUTPUT >> /root/${DEVICE_NO}/flow.txt