//表单提交事件
function submitAction(formobj) {

    $.ajax({
        cache : true,
        type : "POST",
        url : ctx + formobj.attr("action"),
        data : formobj.serialize(),
        async : false,
        error : function(request) {
            $.modal.alertError("系统错误");
        },
        success : function(data) {
            $.operate.successCallback(data);
        }
    });
}

//页面控件初始化入口方法
function initCtrl() {

    //折叠控件初始化
    $(".modal").appendTo("body"), $("[data-toggle=popover]").popover(), $(".collapse-link").click(function () {
        var div_ibox = $(this).closest("div.ibox"),
            e = $(this).find("i"),
            i = div_ibox.find("div.ibox-content");
        i.slideToggle(200),
            e.toggleClass("fa-chevron-up").toggleClass("fa-chevron-down"),
            div_ibox.toggleClass("").toggleClass("border-bottom"),
            setTimeout(function () {
                div_ibox.resize();
            }, 50);
    }), $(".close-link").click(function () {
        var div_ibox = $(this).closest("div.ibox");
        div_ibox.remove()
    });

    //icheck控件初始化
    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });

}
