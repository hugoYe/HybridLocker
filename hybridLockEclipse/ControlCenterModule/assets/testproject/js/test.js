//为点击区域绑定click事件,点击后执行slideUp函数
$("#slide .touch").bind('click',slideUp);

//document.addEventListener('touchstart', touchStart, false);

function bindWebFavoriteApp(data){
    var app = data.app[0];
    var intent = app.intent;
    var bitmap = app.bitmap;
    if(bitmap != null) {
        var appimg = document.getElementById("app1");
        appimg.src = "data:image/gif;base64,"+bitmap;
    }
}

function bindapp(){
}

function slideUp(event){
	//滑动块向上滑动
	$("#slide").animate({bottom:"-5%"},'fast');
	//解除点击区域的事件绑定
	$("#slide .touch").unbind();
	//为点击区域绑定click事件,点击后执行slideDown函数
	$("#slide .touch").bind('click',slideDown);
	//关闭事件冒泡
	event.stopPropagation();
}
function slideDown(event){
	//滑动块向下滑动
	$("#slide").animate({bottom:"-55%"},'fast');
	//解除点击区域的事件绑定
	$("#slide .touch").unbind();
	//为点击区域绑定click事件,点击后执行slideUp函数
	$("#slide .touch").bind('click',slideUp);
	//关闭事件冒泡
	event.stopPropagation();
}

//为背景绑定click事件,点击后执行滑动块向下滑动
//$("#background").click(function(){
//	$("#slide").animate({bottom:"-55%"},'fast');
//})
$("#background").bind('touchstart',touchStart);

function touchStart(){
    console.log("touchstart!");
    touchHelper.preventEvent();
}

//点击滑动块上空白区域执行的函数
$("#slide").click(function(){
	console.log("click!");
	event.stopPropagation();
});

//点击滑动块上状态图标和app执行的函数
function openSome(event){
	console.log("shortcut open");
	control.toastMessage("调用java");
	control.bindFavoriteApp();

	event.stopPropagation();
}
