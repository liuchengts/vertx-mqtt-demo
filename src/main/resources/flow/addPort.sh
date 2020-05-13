#!/usr/bin/env bash
for ((i = 12028; i <= 12040; i++))
do
    iptables -I INPUT -p tcp --dport $i
    iptables -I INPUT -p udp --dport $i
    iptables -I OUTPUT -p tcp --sport $i
    iptables -I OUTPUT -p udp --sport $i
done