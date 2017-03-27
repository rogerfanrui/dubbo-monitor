var config={
		url:'http://localhost/monitor/'
};

var isLoad=[0,0];

$(function() {
		$('li[role=presentation]').click(function() {

			$('li[role=presentation]').removeClass('active');
			$(this).addClass('active');

			$('.panel').hide();
			var _index=$(this).attr('index');
			$('#p' +_index ).show();
			if(_index==1&&!isLoad[_index-1]){
				showAllConsumers();
				isLoad[_index-1]=1;
			}else if (_index==2&&!isLoad[_index-1]){
				showAllProviders();
				isLoad[_index-1]=1;
			}
		});
		
		

	});

$(function(){
	
	$('#ctable').bootstrapTable({
		toolbar:"#c_toolbar",
	           search:"true",
	           showColumns:"true",
	           detailView:"true",
	           detailFormatter:"detailFormatter",
	           showFooter:"false",
	           data:[],
         columns: 
            [
                {
                    field: 'appName',
                    title:'appName'
                }, {
                	 field: 'serviceName',
                	 title:'serviceName'
                }, {
                 	 field: 'ctime',
                 	title:'ctime'
		        }, {
		           	 field: 'ip',
		           	title:'ip'
		        }, {
		       	   field: 'port',
		       	  title:'port'
		        },{
		        	 field: 'success',
			         title:'success',
			         formatter:'showSuccess'
		        },{
		        	 field: 'error',
			         title:'error',
			         formatter:'showError'
		        },{
		        	 field: 'oper',
			         title:'operator',
			         formatter:'showOperator'
		        }
            ]
        
    });
	
	$('#ptable').bootstrapTable({
		toolbar:"#p_toolbar",
	           search:"true",
	           showColumns:"true",
	           detailView:"true",
	           detailFormatter:"detailFormatter",
	           showFooter:"false",
	           data:[],
         columns: 
            [
                {
                    field: 'appName',
                    title:'appName'
                }, {
                	 field: 'serviceName',
                	 title:'serviceName'
                }, {
                 	 field: 'ctime',
                 	title:'ctime'
		        }, {
                	 field: 'group',
                  	title:'group'
 		        }, {
		           	 field: 'ip',
		           	title:'ip'
		        }, {
		       	   field: 'port',
		        	title:'port'
		        },{
		        	 field: 'success',
			         title:'success',
			         formatter:'showSuccess'
		        },{
		        	 field: 'error',
			         title:'error',
			         formatter:'showError'
		        }
            ]
        
    });
	$('li[role=presentation]:first').click();
	
});

$(function(){
	
	
});
//定义操作图标
function showOperator(value,row,index){
	
	 if( row.mockDown){
		 
		 return `<button type="button" class="btn btn-default" data-index="${index}" role="recoveBtn">恢复</button>`;
	 }else{
		 return `<button type="button" class="btn btn-default" data-index="${index}" role="mockDownBtn">降级</button>`;
	 }
	 
	
}

function showSuccess(value,row,index){
	var method =row.methods;
	var showSuccess=0;
	 $.each(method, function (key, d) {
		 showSuccess +=d.success;
	    });
	 return showSuccess;
}
function showError(value,row,index){
	var method =row.methods;
	var showSuccess=0;
	 $.each(method, function (key, d) {
		 showSuccess +=d.fail;
	    });
	 return showSuccess;
}
function detailFormatter(index, row){
	var method =row.methods;
	var html = '<table class="table"><thead><tr><th>methodName</th><th>success</th><th>fail</th><th>max excute time </th> <th>avg execute time</th>  </tr></thead>';
    $.each(method, function (key, d) {
    	html+=`  <tr>  <td>${d.mName}</td> <td>${d.success}</td><td>${d.fail}</td><td>${d.maxETime?d.maxETime:'0'}</td><td>${d.aveETime?d.aveETime:'0'}</td></tr> `;
    });
    html +='</table>'
  
    return html;
}

function detailFormatterPro(index, row){
	var method =row.methods;
	var html = [];
    $.each(method, function (key, d) {
        html.push(`<p><b>methodName:</b>${d.mName} <b>成功:</b>${d.success} <b>失败:</b>${d.fail}</p>`);
    });
    return html.join('');
}
function showAllConsumers(){
	
	doRequest({
		url:'consumer'
	},function(d){
		if(!d){
			return;
		}
		$('#ctable').bootstrapTable('load', d);

		$('button[role=recoveBtn]').click(function() {
			var _index=this.dataset.index;
			var _row =$('#ctable').bootstrapTable('getData')[_index];
			var services=_row.serviceName;
			var appName=_row.appName;
			var ip=_row.ip;
			doRequest({
				url:'recoveMock',
				type:'POST',
				data:{
					services:services,
					appName:appName,
					ip:ip
				}
			},function(d){
				print('执行成功');
				showAllConsumers();
			});
		});
		$('button[role=mockDownBtn]').click(function() {
			var _index=this.dataset.index;
			var _row =$('#ctable').bootstrapTable('getData')[_index];
			var services=_row.serviceName;
			var appName=_row.appName;
			var ip=_row.ip;
			doRequest({
				url:'doMock',
				type:'POST',
				data:{
					services:services,
					appName:appName,
					ip:ip
				}
			},function(d){
				print('执行成功');
				showAllConsumers();
			});
			
		});
	});
}




function showAllProviders(){
	doRequest({
		url:'provider'
	},function(d){
		if(!d){
			return;
		}
		$('#ptable').bootstrapTable('load', d);
		
	});
	
	
}

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
		url: config.url+param.url,
		type:type,
		async:true,
		data:param.data,
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
