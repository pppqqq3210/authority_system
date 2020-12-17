layui.use('form', function() {
	// console.log("haha");
	var form = layui.form;
	
	//通用弹出层表单提交方法
	form.on('submit(demo1)', function(data){
		// console.log(data.field);
		// console.log(data);
		let action = $('form').attr("action");
		// if(action.indexOf('user') != -1 && !checkPhone()){
		// 	return false;
		// }
		$.post($('form').attr("action"),data.field, function (e){
			// var data = JSON.parse(e);
			// console.log(e);
			if (e.result==true) {
				parent.closeLayer(e.msg);
			}else {
				layer.msg('操作失败：' + e.msg, {icon: 2, time: 2000});
			}
		})
		return false;
	})
	
});