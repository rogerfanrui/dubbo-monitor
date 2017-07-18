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
		timeout:5000,
		dataType:'json', 
		error:function(){
			print('交互错误');
		},success:function(data){
			if(!data.success){
				print(data.msg||'交互错误');
			}else{
				callback(data.data);
			}
		}
      });
}
