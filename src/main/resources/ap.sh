#!/usr/bin/env bash
# !/bin/sh
set -m
SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
AP_DIR=${SHELL_FOLDER}"/create_ap"
function main() {
    ap_stop
    isok=`ping -c 4 www.baidu.com | awk -F, '/received/{print $2}' | awk '{print $1}'`
    if [[ ${isok} -gt 0 ]];then
        echo "网络正常"
    else
        echo "网络异常，运行ap"
        ap_start
    fi
}
function ap_stop() {
    echo "关闭ap,应用wifi"
    create_ap --stop wlan0 &
}
function ap_start() {
    create_ap --stop wlan0 &
    create_ap wlan0 eth0 ap &
}

main