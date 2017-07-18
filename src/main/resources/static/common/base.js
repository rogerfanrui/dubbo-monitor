/**
 * Created by HANZO on 2016/6/17.
 */

/**
 * 待改
 * 
 * @param url
 * @param params
 * @param callback
 * @returns {*}
 */
function loadPage(url){
	$("#mainDiv").load(url,function(response,status,xhr){
		if(status=="success"){
			if(response){
				try{
					var result = jQuery.parseJSON(response);
					if(result.code==100){ 
						$("#mainDiv").html("");
						alert(result.data);
					}
				}catch(e){
					return response;
				}
			}
		}
	});
}
 
/**
 * Load a url into a page
 */
var _old_load = jQuery.fn.load;
jQuery.fn.load = function( url, params, callback ) {
	//update for HANZO, 2016/12/22
	if (typeof url !== "string" && _old_load) {
		return _old_load.apply( this, arguments );
	}

	var selector, type, response,
		self = this,
		off = url.indexOf( " " );
	if ( off > -1 ) {
		selector = jQuery.trim( url.slice( off ) );
		url = url.slice( 0, off );
	}
	if ( jQuery.isFunction( params ) ) {
		callback = params;
		params = undefined;
	} else if ( params && typeof params === "object" ) {
		type = "POST";
	}
	if ( self.length > 0 ) {
		jQuery.ajax( {
			url: url,
			beforeSend: function( xhr ) {  
				    xhr.setRequestHeader('X-Requested-With', {toString: function(){ return ''; }});  
			},  
			type: type || "GET", 
			dataType: "html",
			data: params
		} ).done( function( responseText ) {
			response = arguments;
			self.html( selector ?
				jQuery( "<div>" ).append( jQuery.parseHTML( responseText ) ).find( selector ) :
				responseText );
		} ).always( callback && function( jqXHR, status ) {
			self.each( function() {
				callback.apply( this, response || [ jqXHR.responseText, status, jqXHR ] );
			} );
		} );
	}

	return this;
};



function ajaxPost(url, params, callback) {
	var result = null;
    var headers={};
    headers['CSRFToken']=$("#csrftoken").val();
    
	$.ajax({
		type : 'post',
		async : false,
		url : url,
		data : params,
		dataType : 'json',
		headers:headers,
		success : function(data, status) {
			result = data;
			if(data&&data.code&&data.code=='101'){
				modals.error("操作失败，请刷新重试，具体错误："+data.message);
				return false;
			}
			if (callback) { 
				callback.call(this, data, status);
			}
		},
		error : function(err, err1, err2) {
		    if(err && err.readyState && err.readyState == '4'){
                var responseBody = err.responseText;
                if(responseBody){   
                	 responseBody = "{'retData':"+responseBody;
                     var resJson = eval('(' + responseBody + ')');
                     $("#csrftoken").val(resJson.csrf.CSRFToken);
                     this.success(resJson.retData, 200);
                }
                return ;
            } 		    
			modals.error({
				text : JSON.stringify(err) + '<br/>err1:' + JSON.stringify(err1) + '<br/>err2:' + JSON.stringify(err2),
				large : true
			});
		}
	});

	return result;
}

function getServerTime(base_path, format) {
	var result = null;

	var sdate = new Date(ajaxPost(base_path+'/base/getServerTime'));
	if (sdate != 'Invalid Date') {
		result = formatDate(sdate, format||'yyyy/MM/dd');
	}

	return result;
}

/**
 * 格式化日期
 */
function formatDate(date, format) {
	if(!date)return date;
	date = (typeof date == "number") ? new Date(date) : date;
	return date.Format(format);
}

Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"H+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

/**
 * 将map类型[name,value]的数据转化为对象类型
 */
function getObjectFromMap(aData) {
	var map = {};
	for (var i = 0; i < aData.length; i++) {
		var item = aData[i];
		if (!map[item.name]) {
			map[item.name] = item.value;
		}
	}
	return map;
}

/**
 * 获取render,并转化为对象数组
 */
function getRenderObject(render) {
	var arr = render.split(",");
	var obj = new Object();
	for (var i = 0; i < arr.length; i++) {
		var strA = arr[i].split("=");
		obj[strA[0]] = strA[1];
	}
	if (!obj.type)
		obj.type = "eq";
	return obj;
}

/**
 * 获取下一个编码 000001，000001000006，6
 * 得到结果 000001000007
 */
function getNextCode(prefix,maxCode,length){
	if(maxCode==null){
		var str="";
		for(var i=0;i<length-1;i++){
			str+="0";
		}
		return prefix+str+1;
	}else{
		var str="";
		var sno = parseInt(maxCode.substring(prefix.length))+1;
		for(var i=0;i<length-sno.toString().length;i++){
			str+="0";
		}
		return prefix+str+sno;
	}
	
}

/**
 * 收缩左边栏时，触发markdown编辑的resize
 */
$("[data-toggle='offcanvas']").click(function(){
	if(editor){
		setTimeout(function(){editor.resize()},500);
	}
});


//获取布尔值
/*String.prototype.BoolValue=function(){
	if(this==undefined)
		return false;
	if(this=="false"||this=="0")
		return false;
	return true;
}*/

var HtmlUtil = {
	/*1.用浏览器内部转换器实现html转码*/
	htmlEncode:function (html){
		//1.首先动态创建一个容器标签元素，如DIV
		var temp = document.createElement ("div");
		//2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
		(temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
		//3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
		var output = temp.innerHTML;
		temp = null;
		return output;
	},
	/*2.用浏览器内部转换器实现html解码*/
	htmlDecode:function (text){
		//1.首先动态创建一个容器标签元素，如DIV
		var temp = document.createElement("div");
		//2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
		temp.innerHTML = text;
		//3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
		var output = temp.innerText || temp.textContent;
		temp = null;
		return output;
	}
};

String.prototype.startWith=function(s){
	if(s==null||s==""||this.length==0||s.length>this.length)
		return false;
	if(this.substr(0,s.length)==s)
		return true;
	else
		return false;
	return true;
}