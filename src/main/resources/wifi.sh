#!/usr/bin/env bash
# !/bin/sh
set -m
echo "关闭ap,应用wifi"
create_ap --stop wlan0 &
#/etc/init.d/networking restart
ifup wlan0