#!/usr/bin/env bash
# 分割线
echo "*******************************************************************" >> /tmp/flow.txt
# 时间戳
var=`date "+%Y-%m-%d %H:%M:%S"`
echo "Flow write at ${var}" >> /tmp/flow.txt
# 写入流量数据
iptables -n -v -t filter -L INPUT >> /tmp/flow.txt
iptables -n -v -t filter -L OUTPUT >> /tmp/flow.txt
