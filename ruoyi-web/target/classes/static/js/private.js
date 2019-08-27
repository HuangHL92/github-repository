//region 图片预览
function videoAndPic(){
    $("[data-toggle='videoAndPic']").each(function () {
        var obj=$(this);
        var noname = obj.data("noname") || "";
        var attachmentNo =obj.val();
        $.get(ctx+"comm/videoAndPic?",{attachmentNo:attachmentNo,noname:noname}, function (result) {
            if(result != null && result != ''){
                obj.after(result);
            }
        })
    })
}
videoAndPic();
//endregion

// 格式化日期函数
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};

// 弹出层指定宽度
function openSpecifySize(title, url, width, height, isDetail) {
    var btns = ['确定', '关闭'];
    //如果是移动端，就使用自适应大小弹窗
    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
        width = 'auto';
        height = 'auto';
    }
    if ($.common.isEmpty(title)) {
        title = false;
    }
    if ($.common.isEmpty(url)) {
        url = "/404.html";
    }
    if (!((width + "").indexOf("%") >= 0 || (height + "").indexOf("%") >= 0) && !((width + "").indexOf("px") >= 0 || (height + "").indexOf("px") >= 0)) {
        width = width + "px";
        height = height + "px";
    }
    if ($.common.isEmpty(width)) {
        width = 800 + "px";
    }
    if ($.common.isEmpty(height)) {
        height = ($(window).height() - 100) + "px";
    }
    if (isDetail) {
        btns=['关闭'];
    }
    layer.open({
        type: 2,
        area: [width , height ],
        fix: false,
        //不固定
        maxmin: true,
        shade: 0.3,
        title: title,
        content: url,
        btn: btns,
        // 弹层外区域关闭
        shadeClose: true,
        btn1: function(index, layero) {
            if(isDetail) {
                layer.close(index);
            } else {
                var iframeWin = layero.find('iframe')[0];
                if(iframeWin.contentWindow.submitHandler) {
                    iframeWin.contentWindow.submitHandler(iframeWin);
                } else {
                    layer.close(index);
                }
            }
        },
        cancel: function(index) {
            return true;
        },
        success:function (layero,index) {
            var body = layer.getChildFrame('body', index);
            layero.find(".layui-layer-btn").prepend(body.find(".toolbar-btn"));
        }
    });
}

/**
 * 选择行政区划树(只显示当前用户下的)
 */
function selectAreaTree_Action() {
    var areaId = $.common.isEmpty($("#areaId_Hide_Tree").val()) ? "100" : $("#areaId_Hide_Tree").val();
    var url = ctx + "basic/area/selectAreaTree/" + areaId +"?currentuser=1";
    var options = {
        title: '选择行政区域',
        width: "380",
        url: url,
        callBack: doAreaSubmit_Action
    };
    $.modal.openOptions(options);
}

function doAreaSubmit_Action(index, layero){
    var body = layer.getChildFrame('body', index);
    $("#areaId_Hide_Tree").val(body.find('#treeId').val());
    $("#areaName_Hide_Tree").val(body.find('#treeName').val());
    layer.close(index);
}