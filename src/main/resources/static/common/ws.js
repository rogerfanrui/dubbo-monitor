


function StompWSClient(path){
	this.path =path;
	this.client=null;
	this.isConnection=false;
	var _this = this;
	this.sublists=[];
	this.subScirtObjs=new Array();
	connectWs =function () {
        var socket = new SockJS(path);
        socket.onclose = function() {
        	_this.isConnection=false;
        	console.log("连接已关闭");
        	
        };
        socket.onopen = function() { 
            console.log('open'); 
        }; 
        _this.client = Stomp.over(socket);
        
        _this.client.connect({}, function (frame) {
            console.log('Connected:' + frame);
            alalert.info("已连接websocket");
            _this.isConnection=true;
            
        },function(){
        	console.log("disconnection");
        	alalert.warn("连接websocket失败，请检查服务器endpoint配置");
        	_this.isConnection=false;
        });
    }();
    
    this.contanins =function (val)  {  
        for (var i = 0; i < this.sublists.length; i++)  
       {  
          if (this.sublists[i] == val)  
         {  
          return true;  
         }  
       }  
        return false;  
   } ;
	
}
StompWSClient.prototype.clear=function(){
	if(!this.isConnection || this.subScirtObjs.length==0){
		return;
	}
	for(si in this.subScirtObjs){
		var subObj = this.subScirtObjs[si];
		if(subObj){
			subObj.unsubscribe();
		}
	}
	this.subObj=new Array();
	this.sublists=new Array();
}
StompWSClient.prototype.subscribe=function(path,callback,error){
	if(this.isConnection ){
		if(this.contanins(path)){
			console.log(path+',已订阅');
			return;
		}
		var subObj =this.client.subscribe(path,function(msg){
			callback(msg.body);
		});
		this.sublists.push(path);
		this.subScirtObjs.push(subObj);
		console.log(path+"订阅成功");
	}else{
		console.log(path+"订阅失败");
		if(error)
			error('未建立连接');
	}
}