/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        /** cordova demo begin */
        /*var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);*/
        /** cordova demo end */
        //执行初始化状态栏函数
        initState();
    }
};

app.initialize();





var maxHeight,startY,endY,dragLength= 0,translateY;
var height=$("#slide").height();
var HEIGHT=document.body.clientHeight;
$(function() {
	maxHeight = height * 1;
	translateY=0;
	$("#background").bind('touchstart',function(){console.log("touchstart!");});
	$("#state_wifi").bind('click', changeWifiState);
	$("#state_net").bind('click', changeNetState);
	$("#state_blue").bind('click', changeBlueState);
	$("#state_light").bind('click', changeLightState);
	$("#state_camera").bind('click', changeCameraState);
	$("#app .app_item img").bind('click', openSome);
	$("#app .app_item img").bind('touchstart',function(event){
		$(this).css({"-webkit-transform":"scale3d(1.1,1.1,1)"});
		event.stopPropagation();
	})
	$("#link .link_item img").bind('click', openSome);
	$("#link .link_item img").bind('touchstart',function(event){
		$(this).css({"-webkit-transform":"scale3d(1.1,1.1,1)"});
		event.stopPropagation();
	})
	$("#app .app_item img").bind('touchend',function(event){
		$(this).css({"-webkit-transform":"scale3d(1,1,1)"});
		event.stopPropagation();
	})
	$("#button_th").bind('touchstart', dragStart);
	$("#button_th").bind('touchmove', drag);
	$("#button_th").bind('touchend',dragEnd);
})

//初始化状态栏函数
function initState(){

	var index;
	var wifiImg=document.getElementById("state_wifi");
	var netImg=document.getElementById("state_net");
	var blueImg=document.getElementById("state_blue");
	var lightImg=document.getElementById("state_light");
	var cameraImg=document.getElementById("state_camera");
	if (isWifiOn()){
		index=wifiImg.src.lastIndexOf("off");
		if(index!=-1){
		    wifiImg.src=wifiImg.src.slice(0,index)+"on.png";
		}
	} else {
	    index=wifiImg.src.lastIndexOf("on");
	    if(index!=-1){
            wifiImg.src=wifiImg.src.slice(0,index)+"off.png";
        }
	}
	if (isNetOn()){
			index=netImg.src.lastIndexOf("off");
			netImg.src=netImg.src.slice(0,index)+"on.png";
	}
	if (isBlueOn()){
        index=blueImg.src.lastIndexOf("off");
        if(index!=-1){
            blueImg.src=blueImg.src.slice(0,index)+"on.png";
        }
	} else {
	    index=blueImg.src.lastIndexOf("on");
	    if(index!=-1){
    	    blueImg.src=blueImg.src.slice(0,index)+"off.png";
    	}
	}
	if (isLightOn()){
        index=lightImg.src.lastIndexOf("off");
        if(index!=-1){
            lightImg.src=lightImg.src.slice(0,index)+"on.png";
        }
	}
	if (isCameraOn()){
			index=cameraImg.src.lastIndexOf("off");
			if(index!=-1){
			    cameraImg.src=cameraImg.src.slice(0,index)+"on.png";
			}
	}
}

//判断wifi是否开启,若是返回true,否则返回false
function isWifiOn(){
    var rst;
    window.WifiWizard.isWifiEnabled(function(res){rst=res;}, function(){});

    return rst;
}


//判断流量是否开启,若是返回true,否则返回false
function isNetOn(){

}

//判断蓝牙是否开启,若是返回true,否则返回false
function isBlueOn(){
//    cordova.plugins.BluetoothStatus.initPlugin();
    return cordova.plugins.BluetoothStatus.BTenabled;
}

//判断手电是否开启,若是返回true,否则返回false
function isLightOn(){

}

//判断摄像头是否开启,若是返回true,否则返回false
function isCameraOn(){

}

//开启wifi
function setWifiOn(){
    window.WifiWizard.setWifiEnabled(true,function(res){},function(){});
}

//关闭wifi
function setWifiOff(){
    window.WifiWizard.setWifiEnabled(false,function(res){},function(){});
}

//开启流量
function setNetOn(){
    plugins.MobileDataWizard.clickMobileData();
}

//关闭流量
function setNetOff(){
    plugins.MobileDataWizard.clickMobileData();
}

//开启蓝牙
function setBlueOn(){
    cordova.plugins.BluetoothStatus.enableBT();
}

//关闭蓝牙
function setBlueOff(){
    cordova.plugins.BluetoothStatus.disableBT();
}

//开启手电
function setLightOn(){
    window.plugins.flashlight.switchOn();
}

//关闭手电
function setLightOff(){
    window.plugins.flashlight.switchOff();
}

//开启摄像头
function setCameraOn(){
    navigator.camera.getPicture(function(){},function(){},{ quality: 50,
                                                              destinationType: Camera.DestinationType.DATA_URL
                                                          });
}

//关闭摄像头
function setCameraOff(){

}
//开始拖动时执行的函数
function dragStart(event){
	event.preventDefault();
	$('#button_th').css({'opacity':0});
	startY=event.originalEvent.targetTouches[0].pageY;
	if (translateY!=maxHeight) {
		translateY=HEIGHT-event.originalEvent.targetTouches[0].pageY;
		$("#slide").css({
			"transition-duration":"50ms",
			"-webkit-transform": "translate3d(0,"+"-"+translateY+"px"+",0)"
		});
	}else {
		$("#slide").css({
			"transition-duration":"0ms",
			"-webkit-transform": "translate3d(0,"+"-"+translateY+"px"+",0)"
		});
	}
	event.stopPropagation();
}

//拖动时执行的函数
function drag(event){
	endY=event.originalEvent.targetTouches[0].pageY;
	$("#slide").css({"transition-duration":"0ms"});
	dragLength=startY-endY;
	var newTranslateY=translateY+dragLength;
	if (newTranslateY<=maxHeight){
		$("#slide").css({
			"-webkit-transform": "translate3d(0,"+"-"+newTranslateY+"px"+",0)"
		});
		translateY=newTranslateY;
	}
	startY=endY;
	event.stopPropagation();
}

//拖动结束时执行的函数
function dragEnd(event){
	$("#slide").css({
		"transition-duration":"300ms",
	});
	if(translateY<=height*0.25){
		slideDown();
	}else {
		slideUp();
	}
	event.stopPropagation();
}

//控制中心完全出来后重新为触摸区域绑定的拖动结束后执行的函数
function upDragEnd(event){
	$("#slide").css({
		"transition-duration":"300ms",
	});
	if (dragLength>0){
		slideUp();
	}else {
		slideDown();
	}
	event.stopPropagation();
}

//控制中心上移动画
function slideUp(){
	$('#button_th').css({'opacity':0});
	$("#button_th").unbind('touchstart');
	$("#button_th").unbind('touchmove');
	$("#button_th").unbind('touchend');
	$(".touch_item").unbind('touchstart');
	$(".touch_item").unbind('touchmove');
	$(".touch_item").unbind('touchend');
	$(".touch_item").bind('touchstart',dragStart);
	$(".touch_item").bind('touchmove',drag);
	$(".touch_item").bind('touchend',upDragEnd);
	$("#slide").css({
		"-webkit-transform":"translate3d(0,-100%,0)",
	});
	$("img.touch").css({"-webkit-transform":"rotateZ(0deg)"});
	translateY=maxHeight;
}

//控制中心下移动画
function slideDown(){
	window.setTimeout("$('#button_th').css({'opacity':1});",300);
	$(".touch_item").unbind('touchstart');
	$(".touch_item").unbind('touchmove');
	$(".touch_item").unbind('touchend');
	$("#button_th").unbind('touchstart');
	$("#button_th").unbind('touchmove');
	$("#button_th").unbind('touchend');
	$("#button_th").bind('touchstart',dragStart);
	$("#button_th").bind('touchmove',drag);
	$("#button_th").bind('touchend',dragEnd);
	$("#slide").css({
		"-webkit-transform":"translate3d(0,0%,0)",
	});
	$("img.touch").css({"-webkit-transform":"rotateZ(180deg)"});
	translateY=0;
}

//点击背景后将控制中心收起
$("#background").click(function(){
	slideDown();
})

//点击控制中心其他区域执行的函数
$("#slide").click(function(){
	console.log("click!");
	event.stopPropagation();
});

//点击app后执行的函数
function openSome(event){
	console.log("open");
	event.stopPropagation();
}

//改变wifi状态
function changeWifiState(event){
//	var index;
//	if (isWifiOn()) {
//		setWifiOff();
//		if (this.src.lastIndexOf("on")!=-1){
//			index=this.src.lastIndexOf("on");
//			this.src=this.src.slice(0,index)+"off.png";
//		}
//	}else {
//		setWifiOn();
//		if (this.src.lastIndexOf("off")!=-1)
//			index=this.src.lastIndexOf("off");
//		this.src=this.src.slice(0,index)+"on.png";
//	}
//	event.stopPropagation();

        var index;
		if (this.src.lastIndexOf("on")!=-1){
			index=this.src.lastIndexOf("on");
			this.src=this.src.slice(0,index)+"off.png";
			setWifiOff();
		}else {
			index=this.src.lastIndexOf("off");
		    this.src=this.src.slice(0,index)+"on.png";
		    setWifiOn();
	    }
	event.stopPropagation();
}

//改变流量状态
function changeNetState(event){
//	var index;
//	if (isNetOn()) {
//		setNetOff();
//		if (this.src.lastIndexOf("on")!=-1){
//			index=this.src.lastIndexOf("on");
//			this.src=this.src.slice(0,index)+"off.png";
//		}
//	}else {
//		setNetOn();
//		if (this.src.lastIndexOf("off")!=-1)
//			index=this.src.lastIndexOf("off");
//		this.src=this.src.slice(0,index)+"on.png";
//	}
//	event.stopPropagation();

    var index;

		setNetOff();
		if (this.src.lastIndexOf("on")!=-1){
			index=this.src.lastIndexOf("on");
			this.src=this.src.slice(0,index)+"off.png";
		} else {
		    setNetOn();
			index=this.src.lastIndexOf("off");
		    this.src=this.src.slice(0,index)+"on.png";
	    }
	event.stopPropagation();
}

//改变蓝牙状态
function changeBlueState(event){
//	var index;
//	if (isBlueOn()) {
//		setBlueOff();
//		if (this.src.lastIndexOf("on")!=-1){
//			index=this.src.lastIndexOf("on");
//			this.src=this.src.slice(0,index)+"off.png";
//			console.log("changeBlueState 111");
//		}
//	}else {
//		setBlueOn();
//		if (this.src.lastIndexOf("off")!=-1){
//			index=this.src.lastIndexOf("off");
//		    this.src=this.src.slice(0,index)+"on.png";
//		    console.log("changeBlueState 222");
//		}
//	}
//	event.stopPropagation();
var index;
		setBlueOff();
		if (this.src.lastIndexOf("on")!=-1){
			index=this.src.lastIndexOf("on");
			this.src=this.src.slice(0,index)+"off.png";
			console.log("changeBlueState 111");
		} else {
		setBlueOn();
		if (this.src.lastIndexOf("off")!=-1){
			index=this.src.lastIndexOf("off");
		    this.src=this.src.slice(0,index)+"on.png";
		    console.log("changeBlueState 222");
		}
	}
	event.stopPropagation();
}

//改变手电状态
function changeLightState(event){
//	var index;
//	if (isLightOn()) {
//		setLightOff();
//		if (this.src.lastIndexOf("on")!=-1){
//			index=this.src.lastIndexOf("on");
//			this.src=this.src.slice(0,index)+"off.png";
//		}
//	}else {
//		setLightOn();
//		if (this.src.lastIndexOf("off")!=-1)
//			index=this.src.lastIndexOf("off");
//		this.src=this.src.slice(0,index)+"on.png";
//	}
//	event.stopPropagation();
var index;
		setLightOff();
		if (this.src.lastIndexOf("on")!=-1){
			index=this.src.lastIndexOf("on");
			this.src=this.src.slice(0,index)+"off.png";
		} else {
		setLightOn();
		if (this.src.lastIndexOf("off")!=-1)
			index=this.src.lastIndexOf("off");
		this.src=this.src.slice(0,index)+"on.png";
	}
	event.stopPropagation();
}

//改变相机状态
function changeCameraState(event){
//	var index;
//	if (isCameraOn()) {
//		setCameraOff();
//		if (this.src.lastIndexOf("on")!=-1){
//			index=this.src.lastIndexOf("on");
//			this.src=this.src.slice(0,index)+"off.png";
//		}
//	}else {
//		setCameraOn();
//		if (this.src.lastIndexOf("off")!=-1)
//			index=this.src.lastIndexOf("off");
//		this.src=this.src.slice(0,index)+"on.png";
//	}
//	event.stopPropagation();

		setCameraOn();

	event.stopPropagation();
}
