pi-client
---------------------------

### lot分布式客户端
## 注意
* 启动时必须指定设备号，否则不能启动
```
pi-client-1.0.0-SNAPSHOT-fat.jar -DdeviceNo=D23772839521171173

kill -9 `jps -l | grep pi-client-1.0.0-SNAPSHOT-fat.jar`
```
* 关于触发v2的mqtt报文示例
```
{"type":"OK","serviceType":"FLOW","t":"{\n    \"log\": {\n      \"access\": \"/var/log/v2ray/access.log\",\n      \"error\": \"/var/log/v2ray/error.log\",\n      \"loglevel\": \"info\"\n    },\n    \"inbounds\": [\n {\n      \"listen\": \"0.0.0.0\",\n      \"port\": 12028,\n      \"protocol\": \"shadowsocks\",\n      \"domainOverride\": [\n        \"tls\",\n        \"http\"\n      ],\n      \"settings\": {\n        \"method\": \"aes-256-gcm\",\n        \"password\": \"pwd272394782703465\"\n      }\n    },\n {\n      \"listen\": \"0.0.0.0\",\n      \"port\": 33,\n      \"protocol\": \"shadowsocks\",\n      \"domainOverride\": [\n        \"tls\",\n        \"http\"\n      ],\n      \"settings\": {\n        \"method\": \"aes-256-gcm\",\n        \"password\": \"pwd256094161791315\"\n      }\n    },\n      {\n        \"listen\": \"127.0.0.1\",\n        \"port\": 1086,\n        \"protocol\": \"socks\",\n        \"settings\": {\n          \"udp\": true,\n          \"userLevel\": 0\n        }\n      }\n    ],\n    \"outbounds\": [\n      {\n        \"protocol\": \"socks\",\n        \"settings\": {\n          \"servers\": [\n            {\n              \"address\": \"127.0.0.1\",\n              \"port\": 2080\n            }\n          ],\n          \"tag\": \"trojan\"\n        }\n      }\n    ],\n    \"dns\": {\n      \"servers\": [\n        \"\"\n      ]\n    },\n    \"routing\": {\n      \"strategy\": \"rules\",\n      \"settings\": {\n        \"domainStrategy\": \"IPIfNonMatch\",\n        \"rules\": [\n          {\n            \"outboundTag\": \"direct\",\n            \"type\": \"field\",\n            \"network\": [\n              \"ws\",\n              \"kcp\",\n              \"tcp\"\n            ],\n            \"ip\": [\n              \"geoip:cn\",\n              \"geoip:private\"\n            ],\n            \"domain\": [\n              \"geosite:cn\",\n              \"geosite:speedtest\"\n            ]\n          }\n        ]\n      }\n    },\n    \"transport\": {\n      \"tcpSettings\": {\n        \"mtu\": 1350,\n        \"tti\": 20,\n        \"uplinkCapacity\": 100,\n        \"downlinkCapacity\": 100,\n        \"congestion\": true,\n        \"readBufferSize\": 2,\n        \"writeBufferSize\": 2,\n        \"header\": {\n          \"type\": \"none\"\n        }\n      },\n      \"wsSettings\": {\n        \"mtu\": 1350,\n        \"tti\": 20,\n        \"uplinkCapacity\": 100,\n        \"downlinkCapacity\": 100,\n        \"congestion\": true,\n        \"readBufferSize\": 2,\n        \"writeBufferSize\": 2,\n        \"header\": {\n          \"type\": \"none\"\n        }\n      },\n      \"kcpSettings\": {\n        \"mtu\": 1350,\n        \"tti\": 20,\n        \"uplinkCapacity\": 100,\n        \"downlinkCapacity\": 100,\n        \"congestion\": true,\n        \"readBufferSize\": 2,\n        \"writeBufferSize\": 2,\n        \"header\": {\n          \"type\": \"utp\"\n        }\n      }\n    }\n  }","msg":"","deviceNo":"D23772839521171173"}
```
