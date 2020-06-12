#!/usr/bin/env bash
WORK="/etc/v2ray"
CONFIG_NAME="config.json"
TMP_CONFIG_NAME=".tmp"
CUR_TIMESTAMP=$((`date '+%s'`*1000+`date '+%N'`/1000000))
cp ${WORK}/${CONFIG_NAME} ${WORK}/${CUR_TIMESTAMP}"-"${CONFIG_NAME}
# 重置配置文件
mv ${WORK}/${CONFIG_NAME}${TMP_CONFIG_NAME} ${WORK}/${CONFIG_NAME}
kill -s 9 `ps -aux | grep v2ray | awk '{ if($11=="/usr/bin/v2ray/v2ray") { print $2}}'`
echo "清理 v2ray 完成"
#systemctl restart v2ray
nohup /usr/bin/v2ray/v2ray -config /etc/v2ray/config.json &> /dev/null &
echo " v2ray 启动完成 "
