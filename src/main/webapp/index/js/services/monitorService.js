"use strict";!function(t,e){void 0!==t&&t.factory("$domeMonitor",["$http","$q","$util","dialog",function(t,e,n,i){var o=function(){return t.get("/api/global/monitor/info")},r=function(e){return t.post("/api/monitor/target",angular.toJson(e))},a=function(e){return t.get("/api/monitor/target/"+e)},u=function(e,n,i,o,r){return t.get("/api/monitor/data/"+e+"?start="+n+"&end="+i+"&dataSpec="+o+"&cid="+r)},s=function(t,e,n){var o=window.open("","_blank"),a=n.targetType;r(n).then(function(n){var r=n.data.result;if(void 0===r)return void i.error("警告","请求错误！");setTimeout(function(){o.location="/monitor/monitor.html?cid="+t+"&cname="+e+"&id="+r+"&type="+a},0)})};return{getMonitorInfo:o,getMonitorStatistical:function(t,i,o,a){function s(t,e,n){return"[object Null]"===Object.prototype.toString.call(t)||isNaN(t)?"——":(n||(n=""),t=+t.toFixed(e),n&&(t+=n),t)}function c(t,e){return t=n.formartBytesData(t,e),"[object Null]"===Object.prototype.toString.call(t)||void 0===t?"——":+t.toFixed(2)}var d=e.defer();return r({clusterId:i,targetType:t,targetInfos:o}).then(function(t){return u(t.data.result,(new Date).getTime()-3e5,(new Date).getTime(),"AVERAGE",i)}).then(function(e){function n(t,e,n,i){var o=t+"Data",r=t+"Count";angular.forEach(n,function(t,n){for(var a in t)if(t.hasOwnProperty(a)&&"timeStamp"!==a&&f[a]){var u;if(void 0!==i&&(u="%"==i?s(t[a],2,i):c(t[a],i)),f[a][o].push({item:n,value:u}),null===f[a][r]&&!t[a])continue;"MAX"==e?t[a]>f[a][r]&&(f[a][r]=t[a]):"SUM"==e&&(f[a][r]+=t[a])}}),void 0!==i&&angular.forEach(f,function(t){t[r]="%"==i?s(t[r],2):c(t[r],i)})}var i,o,r,u,l=e.data.result,f={},p={};if("node"==t){for(i=l.counterResults["cpu.busy"].slice(-3,-2)[0],o=l.counterResults["mem.memused.percent"].slice(-3,-2)[0],p={diskUsedMap:{},diskReadMap:{},diskWriteMap:{},netInMap:{},netOutMap:{}},r=0,u=a.length;r<u;r++)f[a[r]]={diskUsedData:[],diskUsedCount:0,diskReadData:[],diskReadCount:0,diskWriteData:[],diskWriteCount:0,netInData:[],netInCount:0,netOutData:[],netOutCount:0,cpuBusyCount:s(i[a[r]],2),memPercentCount:s(o[a[r]],2)};angular.forEach(l.counterResults,function(t,e){var n=e.split("=")[1];e.indexOf("df.bytes.used.percent")!==-1?p.diskUsedMap[n]=t.slice(-3,-2)[0]:e.indexOf("disk.io.read_bytes")!==-1?p.diskReadMap[n]=t.slice(-3,-2)[0]:e.indexOf("disk.io.write_bytes")!==-1?p.diskWriteMap[n]=t.slice(-3,-2)[0]:e.indexOf("net.if.in.bytes")!==-1?p.netInMap[n]=t.slice(-3,-2)[0]:e.indexOf("net.if.out.bytes")!==-1&&(p.netOutMap[n]=t.slice(-3,-2)[0])}),n("diskUsed","MAX",p.diskUsedMap,"%"),n("diskRead","SUM",p.diskReadMap,"KB"),n("diskWrite","SUM",p.diskWriteMap,"KB"),n("netIn","SUM",p.netInMap,"KB"),n("netOut","SUM",p.netOutMap,"KB")}else if("pod"==t||"container"==t)for(i=l.counterResults["container.cpu.usage.busy"].slice(-3,-2)[0],o=l.counterResults["container.mem.usage.percent"].slice(-3,-2)[0],p={netIn:l.counterResults["container.net.if.in.bytes"].slice(-3,-2)[0],netOut:l.counterResults["container.net.if.out.bytes"].slice(-3,-2)[0]},r=0,u=a.length;r<u;r++)f[a[r]]={netInCount:c(p.netIn[a[r]],"KB"),netOutCount:c(p.netOut[a[r]],"KB"),cpuBusyCount:s(i[a[r]],2),memPercentCount:s(o[a[r]],2)};d.resolve(f)},function(){d.reject()}),d.promise},storeMonitorTarget:r,getMonitorTarget:a,getMonitorData:u,toMonitorPage:s}}])}(angular.module("domeApp"));