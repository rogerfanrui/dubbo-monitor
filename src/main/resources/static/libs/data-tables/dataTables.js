/**
 * 通用表格组件，基于jquery-DataTable组件的扩展
 * 
 * @param tableId
 *            table组件id
 * @param queryId
 *            query查询id
 * @param searchDiv
 *            查询条件div的id
 * @author bill1012 qq:475572229
 */
function CommonTable(tableId, queryId, searchDiv,config) {
	this.tableId = tableId;
	this.queryId = queryId;
	this.searchDiv = searchDiv;
	this.data = null;
	this.loaded = false;
	this.config=config;

	// 用作缓存一些数据
	var dataCache = $("#dataCache" + tableId);
	if (dataCache.length == 0) {
		dataCache = $("<div></div>");
		dataCache.attr("id", "dataCache" + tableId);
		$(document.body).append(dataCache);
	}
	this.dataCache = dataCache;
	this.dataCache.data("queryId", this.queryId);
	//
	var searchButton = $("#" + searchDiv + " button[data-btn-type='search']");
	this.searchButton = searchButton;
	var resetButton = $("#" + searchDiv + " button[data-btn-type='reset']");
	this.resetButton=resetButton;
	// 表格横向自适应 
	$("#" + this.tableId).css("width", "100%");
	// 初始化表格
	this.initTable(tableId, queryId, searchDiv);

}

/**
 * 初始化表格
 */
CommonTable.prototype.initTable = function(tableId, queryId, searchDiv) {
	this.data = this.getServerData(null, tableId);
	this.dataCache.data("data", this.data);
	// console.log(JSON.stringify(this.data));
	var that = this;
	var columns = [];
	for (var i = 0; i < this.data.query.columnList.length; i++) {
		var column = this.data.query.columnList[i];
		var obj = {};
		obj["data"] = column.key;
		obj["title"] = column.header;
		obj["name"] = column.key;
		obj["visible"] = !column.hidden;
		obj["sortable"] = column.allowSort;
		obj["class"] = "text-" + column.align;
		//obj["width"]=column.width+"px";   
		//obj["sWidthOrig"]=null;  
		if (column.fnRender) { 
			var fnRender=null;
			try {
				fnRender=eval(column.fnRender)
			} catch (e) {
				if (e instanceof ReferenceError) {
					modals.error(column.fnRender+" 未定义！");
				} 
			}
			obj["mRender"] =fnRender;
		}
		columns.push(obj);
	}
	// alert(JSON.stringify(columns));
	var allowPaging=this.data.query.allowPaging;
	this.table = $('#' + tableId).DataTable($.extend({ 
		"paging" : allowPaging, // 分页
		"lengthChange" : allowPaging, // 每页记录数可选项
		"lengthMenu": [[10, 20, 50, -1], [10, 20, 50, "全部"]],
		"searching" : false, // 过滤
		"ordering" : true, // 排序
		"rowId":"id",
	    //"dom": '<"top"iflp<"clear">>rt<"bottom"iflp<"clear">>',
		"info" : allowPaging, // 分页明细
		"autoWidth" : false,
		//"stateSave" : true,// 这样就可以在删除返回时，保留在同一页上
		"processing" : true,// 是否显示取数据时的那个等待提示
		"pagingType" : "full_numbers",// 分页样式 		 
		"language" : { // 中文支持
			"sUrl" : basePath + "/resources/common/json/zh_CN.json"
		},
		"displayLength" : that.data.pageInfo.pageSize,// 每页记录条数，默认为10
		"serverSide" : true,
		"ajaxDataProp" : "data",
		"ajaxSource" : basePath + "/user/loadData",
		"fnServerData" : $.proxy(that.fillDataTable,that), 
		"fnInitComplete" : $.proxy(that.fnInitComplete,that),
		"singleSelect":true,  //单选
		"aoColumns" : columns
		
	},that.config));  
	

	if(this.searchButton){
		this.searchButton.click(function() {
			that.table.page('first').draw(false);  
			// 执行查询的回调函数 		
			if (that.searchButton.attr("callback") != null && that.searchButton.attr("callback") != undefined) {
				eval(that.searchButton.attr("callback"));
			}
		});
	}
	
	if(this.resetButton){
		this.resetButton.click(function(){
			//清除查询条件
			that.clearSearchDiv(that.searchDiv);
			//清除排序、分页、重置初始长度
		    that.table.order([]).page.len(10).draw(); 
		});
	}
}

CommonTable.prototype.clearSearchDiv=function(selector){
	var sel=$(selector).length>0?$(selector):$("#"+this.searchDiv);
	sel.find(':input[name]:not(:radio)').val('');
	sel.find(':radio').attr('checked', false);
	sel.find(':radio[data-flag]').iCheck('update');
	sel.find(':checkbox').attr('checked', false);
	sel.find(':checkbox[data-flag]').iCheck('update');
	sel.find('select:not(.select2)').val("");
	sel.find("select.select2").select2().val("").trigger("change");
}

// 表格初始化后移动查询组件位置 oSettings=配置；json=数据记录；
CommonTable.prototype.fnInitComplete = function(oSettings, json) {
	// 移动查询框的位置 与记录/页同行
	var _this=this;
	if(!$('.col-sm-9:eq(0)', this.table.table().container() ).html()){ 
		$("#" +this.searchDiv).appendTo($('.col-sm-9:eq(0)', this.table.table().container() ) );
	}
	 
	// 列头文本居中 
	//this.tableId=oSettings.sTableId
	$("#" + this.tableId + " thead tr th").removeClass("text-left").removeClass("text-right").addClass("text-center");
	
	//行单选 	
	if(oSettings.oInit.singleSelect==true){
	    $('#'+this.tableId+' tbody').on( 'click', 'tr', function () {
			//HNAZO modify
	        /*if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	        } else {
	            _this.table.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	        } */
			if (!$(this).hasClass('selected')) {
				_this.table.$('tr.selected').removeClass('selected');
				$(this).addClass('selected');

				if(oSettings.oInit.rowClick){
					oSettings.oInit.rowClick.call(this,_this.getSelectedRowData(),$(this).hasClass('selected'));
				}
			}
	     });
	}else if(oSettings.oInit.singleSelect==false){
		$('#'+this.tableId+' tbody').on( 'click', 'tr', function (){
			 $(this).toggleClass('selected');
		})
	}
	
    //如果分页不可选 则空出位置 让条件区域更宽
    if(!oSettings.oInit.lengthChange){
    	$("#"+this.tableId+"_wrapper div.row").eq(0).find("div.col-sm-3").remove();
		$("#"+this.tableId+"_wrapper div.row").eq(0).find("div.col-sm-9").removeClass("col-sm-9").addClass("col-sm-12");
    }
    
    //Y轴滚动时，设置列头自适应
    if(oSettings.oInit.scrollY){
    	setTimeout(function(){_this.table.columns.adjust();},200); 
    	//setTimeout(function(){_this.fixHeaderWidth()},100);
    }   
} 

CommonTable.prototype.fixHeaderWidth=function(){
	var _this=this;
	var width=$("#"+this.tableId).find("tbody tr:first").width();  
	console.log(width)
	if(width>0){
		//$("#"+_this.tableId+"_wrapper div.dataTables_scrollHeadInner table").css("width",width).parent().css("width",width);
		$("#"+this.tableId).find("tbody tr:first td").each(function(index,item){ 
			console.log($("#"+_this.tableId+"_wrapper div.dataTables_scrollHeadInner table").find("thead").length);  
			console.log("width"+index+":"+$("#"+_this.tableId).find("thead tr:first th").eq(index).css('width'));
			var thwidth=$("#"+_this.tableId).find("thead tr:first th").eq(index).css('width')
			//if(thwidth=="0px"){  
				$("#"+_this.tableId).find("thead tr:first th").eq(index).css("width",$(item).width());
				$("#"+_this.tableId+"_wrapper div.dataTables_scrollHeadInner table").css("width",width).parent().css("width",width);
				$("#"+_this.tableId+"_wrapper div.dataTables_scrollHeadInner table").find("thead tr:first th").eq(index).css("width",$(item).width());
			//}
			console.log($(item).width());    
		}) 
	}  
	else{    
		//console.log("this.fixHeaderWidth();");
		this.fixHeaderWidth();
	}
		
}

CommonTable.prototype.getSelectedRowId=function(){
	if(this.table.row('.selected').length>0)
	   return this.table.row('.selected').id();
	return null; 
}
/**
 * 获取当前选中行的数据 单选
 */
CommonTable.prototype.getSelectedRowData=function(){
	if(this.table.row('.selected').length>0)
	   return this.table.row('.selected').data();
	return null;
}

/**
 * 获取当前选中行的数据 多选
 */
CommonTable.prototype.getSelectedRowsData=function(){
	var datas=null;
    var rows=this.table.rows('.selected').data();
    if(rows.length==0)
    	return datas;
    datas=[];
    for(var i=0;i<rows.length;i++){
    	datas.push(rows[i]);
    }
    return datas;
}


//新增，刷新界面
CommonTable.prototype.reloadData=function(){
	this.table.page('first').draw(false);  
}

//刷新当前页面，并定位到行
CommonTable.prototype.reloadRowData=function(rowId){
	var dataCache=$("#dataCache"+this.tableId);
	var pageInfo=dataCache.data("pageInfo");
	var pageIndex=pageInfo==null?"first":pageInfo.pageNum-1;
	this.table.page(pageIndex).draw(false);   
	if(rowId){//定位选中到当前行
		 this.selectRow(rowId);
	}
} 

//选中行
CommonTable.prototype.selectRow=function(rowId,triggerEvent){
	if(rowId){
		this.selectRowWithSelector("#"+rowId, triggerEvent)
	}
}

//选中第一行
CommonTable.prototype.selectFirstRow=function(triggerEvent){
	this.selectRowWithSelector("tr:first",triggerEvent);
}

//通用选择
CommonTable.prototype.selectRowWithSelector=function(selector,triggerEvent){
	if(selector){
		if(triggerEvent){
			this.table.$(selector).click();
		}else{ 
		    this.table.$('tr.selected').removeClass('selected');
	        this.table.$(selector).addClass('selected');
		}
	}
}






/**
 * 清除行选中
 */
CommonTable.prototype.clearSelection=function(){
	this.table.row('.selected').remove().draw(false);
}

// 获取查询框中的查询数据
// isCondition默认为true;likeOption默认false 即不拼接%
CommonTable.prototype.fnGetConditions=function(searchDiv) {
	var searchDiv = $("#" + searchDiv);
	var conditions = [];
	if (searchDiv !== null && searchDiv.length > 0) {
		var ele = searchDiv.find(':input[name]');
		ele.each(function(i) {
			if ($(this).attr("readonly") == "readonly" || $(this).attr("disabled") == "disabled")
				return;
			var map = {};
			var key = $(this).attr("name");
			// alert("key:"+key+" id:"+$(this).attr("id"));
			var isExist = false;
			for (var j = 0; j < conditions.length; j++) {
				if (key == conditions[j].key) {
					isExist = true;
					map = conditions[j];
					break;
				}
			}
			var value = $(this).val();
			var type = $(this).attr("type");

			var likeOption = $(this).attr("likeOption"); // 是否进行模糊查询，默认值为false
			var isCondition = $(this).attr("isCondition"); // 在前台条件div中是否作为1=1条件的一部分
			var operator = $(this).attr("operator"); // 操作符
			if (!isCondition)
				isCondition = "true";
			if (!likeOption)
				likeOption = "false";
			if (!operator)
				operator = "";
			if ((type && (type.toLowerCase() == 'checkbox' || type.toLowerCase() == 'radio'))) {
				if ($(this).attr("checked") != "checked") {
					value = "";
				}
			}
			if ((type && (type == 'select-one'))) {
				if (!value) {
					value = "";
				}
			}
			// alert(key+" "+type+" "+likeOption);
			if ((type && (type == "text" || type == "search") && likeOption == "true")) { // 如果是用户手动输入项，需要在前后各加一个百分号
				if (value && value != "") {
					value = "%" + value + "%";
				}
			}
			if (isExist) {
				map.value += "," + value;
			} else {
				map.key = key;
				map.value = value;
				map.isCondition = isCondition;
				map.operator = operator;
				conditions.push(map);
				// alert("key:"+key+" value:"+value);
			}
		});
	} else {
		// no search conditions found.
	}
	// alert("conditons:"+JSON.stringify(conditions));
	return conditions;
}

/**
 * 获取服务器中的数据
 * 
 * @param pageInfo
 *            分页信息
 * @param tableId
 *            table的ID
 */
CommonTable.prototype.getServerData = function(pageInfo, tableId) {
	var dataCache = $("#dataCache" + tableId);
	var reqParam = {
		queryId : dataCache.data("queryId"),
		pageName : window.document.location.pathname,
		pageInfo : pageInfo,
		query : null,
		sortInfo : dataCache.data("sortInfo"),
		conditions : this.fnGetConditions(this.searchDiv)
	};
	dataCache.data("pageInfo",pageInfo);
	var retData = null;
	$.ajax({
		url : basePath + "/query/loadData",// 这个就是请求地址对应sAjaxSource
		data : {
			"reqObj" : JSON.stringify(reqParam)
		},// 这个是把datatable的一些基本数据传给后台,比如起始位置,每页显示的行数
		type : 'post',
		dataType : 'json',
		async : false,
		success : function(result) {
			retData = result;
		},
		error : function(msg) {
			alert("状态码:" + msg.status + "   错误信息:" + msg.statusText);
		}
	});
	var start = 0;
	if (pageInfo) {
		start = pageInfo.pageSize * (pageInfo.pageNum - 1)
	}
	var columns = retData.query.columnList;
	for (var j = 0; j < columns.length; j++) {
		var column = columns[j];
		for (var i = 0; i < retData.rows.length; i++) {
			retData.rows[i]["rowIndex"] = start + i + 1;
			// 格式化日期
			if (column.dateFormat) {
				retData.rows[i][column.key] = formatDate(retData.rows[i][column.key], column.dateFormat);
			}
			// 格式化数字
			if (column.numberFormat) {
				// TODO format the number,like 0,000,000.00;
			}
			// 格式化render
			if (column.render) {
				var obj = getRenderObject(column.render);
				var value = retData.rows[i][column.key];
				if (value!=null) {
					if (obj.type == "eq") {
						retData.rows[i][column.key] = obj[retData.rows[i][column.key]];
					} else if (obj.type == "window") {
						// TODO 弹出窗口

					} else if (obj.type == "link") {
						// TODO 超链接
					}
				}
			}
		}
	}
	return retData;
}

/**
 * 换页、排序、查询按钮调用此方法
 * 
 * @param sSource
 *            服务器请求方法
 * @param aoData
 *            基本信息
 * @param fnCallback
 *            重绘dataTable的回调函数
 * @param oSettings
 *            dataTable全局配置
 */
CommonTable.prototype.fillDataTable = function(sSource, aoData, fnCallback, oSettings) {
	var result=this.data;
	var map = oSettings.oAjaxData;
	var dataCache = $("#dataCache" + oSettings.sTableId);
	if (this.loaded) {// 换页
		var pageInfo = {};
		pageInfo.pageSize = map.iDisplayLength;
		pageInfo.pageNum = map.iDisplayStart % map.iDisplayLength == 0 ? map.iDisplayStart / map.iDisplayLength + 1
				: map.iDisplayStart / map.iDisplayLength;
		// console.log(dataCache.data("getServerData"));
		// 构造排序
		var columnNames = map.sColumns.split(',');
		var sortArr = [];
		for (var i = 0; i < map.iSortingCols; i++) {
			if (map["iSortCol_" + i] != 0)// 过滤掉rowIndex的排序
				sortArr.push(columnNames[map["iSortCol_" + i]] + " " + map["sSortDir_" + i]);
		}
		dataCache.data("sortInfo", sortArr.join());
		result = this.getServerData(pageInfo, oSettings.sTableId);
		this.data=result;

	} else {// 首次加载
	    result = this.data;
		this.loaded=true;
	}
	var obj = {};
	obj['data'] = result.rows;
	obj["iTotalRecords"] = result.pageInfo.count;
	obj["iTotalDisplayRecords"] = result.pageInfo.count;
	fnCallback(obj);
	//序号排序  
	$("table.table thead tr").each(function(){
		$(this).find("th").eq(0).removeClass("sorting_asc").addClass("sorting_disabled");
	});

}  
