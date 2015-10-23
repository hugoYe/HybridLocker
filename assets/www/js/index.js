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
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();
//为点击区域绑定click事件,点击后执行slideUp函数
$("#slide .touch").bind('click',slideUp);


function slideUp(event){
	//滑动块向上滑动
	$("#slide").addClass("up");
	//解除点击区域的事件绑定
	$("#slide .touch").unbind();
	//为点击区域绑定click事件,点击后执行slideDown函数
	$("#slide .touch").bind('click',slideDown);
	//关闭事件冒泡
	event.stopPropagation();
}
function slideDown(event){
	//滑动块向下滑动
	$("#slide").removeClass();
	//解除点击区域的事件绑定
	$("#slide .touch").unbind();
	//为点击区域绑定click事件,点击后执行slideUp函数
	$("#slide .touch").bind('click',slideUp);
	//关闭事件冒泡
	event.stopPropagation();
}

//为背景绑定click事件,点击后执行滑动块向下滑动
$("#background").click(function(){
	$("#slide").removeClass();
})

//点击滑动块上空白区域执行的函数
$("#slide").click(function(){
	console.log("click!");
	event.stopPropagation();
});

//点击滑动块上状态图标和app执行的函数
function openSome(event){
	console.log("open");
	event.stopPropagation();
}

