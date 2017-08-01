
(function() {
	window.alalert = window.alalert || {};
	var _alalertConfig = {
		index : 2000,
		pattern:{
			info:{
				title:'提示',
				outerStyle:"alert-info",
				iconStyle:"fa-info"
			},
			correct:{
				title:'成功',
				outerStyle:"alert-success",
				iconStyle:"fa-check"
			},warn:{
				title:'警告',
				outerStyle:"alert-warning",
				iconStyle:"fa-warning"
			},error:{
				title:'错误',
				outerStyle:"alert-danger",
				iconStyle:"fa-ban"
			}
		}
	};
	
	function createAlert(msg, type) {
		type = type || "info" ;
		msg = msg || '';
		var pattern = _alalertConfig.pattern[type];
		var modal = document.createElement('DIV');
		var zindex=config.index++;
		modal
				.setAttribute('style',
						'z-index:'+(zindex)+';width:250px;position:fixed;top:60px;right:-100px');
		 modal.setAttribute('label-tyle', 'alalert');
		
		var html = '<div class="alert '+pattern.outerStyle+' alert-dismissible">'
				+ '  <h4><i class="icon fa '+pattern.iconStyle+'"></i> ' + pattern.title
				+ '</h4><div style="text-align:center">' + msg + "</div> </div>";
		var jqModal=$(modal);
		jqModal.append(html);
		document.body.appendChild(modal);
		jqModal.animate({right:'100px'},'slow');
		setTimeout(function(){
			jqModal.animate({right:'-100px'},'slow',function(){
				$(this).remove();
			});
				
		
		},3000)
		return modal;

	}
	
	
	alalert.info = function(msg) {
        return createAlert(msg,'info');
    };
    alalert.succ = function(msg) {
        return createAlert(msg,'correct');
    };
    alalert.warn = function(msg) {
        return createAlert(msg,'warn');
    };
    alalert.error = function(msg) {
        return createAlert(msg,'error');
    };
})();