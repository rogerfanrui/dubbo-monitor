var config={
		url:'http://localhost/monitor/'
};

function print(data){
	var $tooltips = $('#js_tooltips');
	$tooltips.html(data);
	$tooltips.css('display', 'block');
	setTimeout(function () {
		$tooltips.css('display', 'none');
    }, 2000);
}
function doRequest(param,callback){
	type=param.type||'GET';
	$.ajax({ 
		url: param.url,
		type:type,
		async:param.async||true,
		data:param.data||"",
		contentType: param.isbody?"application/json; charset=utf-8":"application/x-www-form-urlencoded;charset=UTF-8", 
		timeout:5000,
		dataType:'json', 
		error:function(){
			alalert.error('交互错误');
		},success:function(data){
			if(!data.success){
				alalert.error(data.msg||'交互错误');
			}else{
				callback(data.data);
			}
		}
      });
}
