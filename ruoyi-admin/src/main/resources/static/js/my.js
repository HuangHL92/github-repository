
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

//表单提交事件Handler(由ry-ui.js进行回调)
function submitHandler(f) {
    //第一个form的ID（提交第一个form,注意一个画面如果有多个form的情况下可能有问题）

    var formlength =f.contentWindow.document.forms.length;
    var formid="";

    console.log("页面有" + formlength +"个表单");

    if(formlength>1) {
        for(var i=0;i<formlength;i++){
            var form=f.contentWindow.document.forms[i];
            //console.log(form.id);
            if(form.id.indexOf("-MAIN")!=-1){
                formid = form.id;
                break;
            }
        }
        if(formid=="") {
            formid = f.contentWindow.document.forms[0].id;
        }
    } else {
        formid = f.contentWindow.document.forms[0].id;
    }

    if ($.validate.form()) {
        if(formid) {
            submitAction($("#"+formid));
            console.log("表单>" + formid +"<提交成功！");
        } else {
            $.modal.alertError("提交失败！（原因：没有获得表单对象）");
        }

    }
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

    //上传控件
    $("[data-toggle='fileupload']").each(function () {
        var _c=$(this);
        var up=new UploadFile(_c);
    })

    //选择控件
    $("[data-toggle='select']").each(function () {
        var _c=$(this);
        var up=new Select(_c);
    })

    layui.use('element', function(){
        var $ = layui.jquery
            ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块

        //触发事件
        var active = {
            tabAdd: function(){
                //新增一个Tab项
                element.tabAdd('demo', {
                    title: '新选项'+ (Math.random()*1000|0) //用于演示
                    ,content: '内容'+ (Math.random()*1000|0)
                    ,id: new Date().getTime() //实际使用一般是规定好的id，这里以时间戳模拟下
                })
            }
            ,tabDelete: function(othis){
                //删除指定Tab项
                element.tabDelete('demo', '44'); //删除：“商品管理”
                othis.addClass('layui-btn-disabled');
            }
            ,tabChange: function(){
                //切换到指定Tab项
                element.tabChange('demo', '22'); //切换到：用户管理
            }
        };

        $('.site-demo-active').on('click', function(){
            var othis = $(this), type = othis.data('type');
            active[type] ? active[type].call(this, othis) : '';
        });



    });

}

<!--UploadFile开始-->
var UploadFile = function (input) {
    var that = this;
    var multi=input.data("multiple")||false
        ,accept=input.data("accept")||'file'
        ,btnText=input.data("btnText")||("image"!==accept?"上传文件":"上传图片")
    var control=new Array();
    if("image"!==accept){
        control.push('<div class="layui-upload">');
        control.push('<button type="button" class="layui-btn upload-btn" ><i class="layui-icon" th:inline="text"></i>'+btnText+'</button>');
        if(multi){
            control.push('<div class="layui-upload-list"><table class="layui-table"><tbody></tbody></table></div>');
        }else{
            control.push('<div class="layui-inline"></div>');
        }
        control.push('</div>');
    }else{
        control.push('<div><div class="jyui-upimgs layui-clear">');
        control.push('<section class="jyui-up-upload upload-btn">');
        control.push('<div class="con">');
        control.push('<i class="fa fa-plus"></i>');
        control.push(' <p>'+btnText+'</p>');
        control.push('</div></section>');
        control.push('</div></div>');
    }
    var _c=$(control.join(''));
    input.after(_c)
    var option={
        appendArea:_c.find("div:eq(0)")//需要append的区域
        ,accept:accept//接收的类型
        ,btn:_c.find(".upload-btn:eq(0)")//按钮
        ,multiple:multi//是否允许多文件
        ,input:input//隐藏域
        ,backType:input.data("backType")||'path'//返回类型 attachmentNo还是路径
    }
    that.config = $.extend({}, that.config, option)
    if(that.config.multiple&&"image"!==that.config.accept){
        that.config.appendArea=that.config.appendArea.find("tbody:eq(0)");
    }
    return that.init();
};
UploadFile.prototype.config = {}
UploadFile.prototype.genAppend = function (file) {
    var that = this
        , config = this.config
        ,appobj
        ,trid=file.index?"id=upload-"+file.index:"";
    if("image"===config.accept){
        appobj=$(['<section class="jyui-up-section" '+trid+'>'
            ,'<span class="jyui-up-span"></span>'
            ,'<span class="jyui-closeup  upload-delete"><i class="fa fa-close"></i></span>'
            ,'<img src="" alt="" class="jyui-up-img">'
            ,'</section>'].join(''));
    }else{
        if (config.multiple) {
            appobj=$(['<tr '+trid+'>'
                ,'<td><a href="javascript:;" target="_blank">'+ file.fileName +'</a></td>'
                ,'<td>'+ file.fileSize +'kb</td>'
                ,'<td>'
                ,'<i class="fa fa-spinner fa-pulse  layui-hide upload-loading"></i>'
                ,'<button class="layui-btn layui-btn-xs upload-reload layui-hide">重传</button>'
                ,'<button class="layui-btn layui-btn-xs layui-btn-danger upload-delete layui-hide">删除</button>'
                ,'</td>'
                ,'</tr>'].join(''));
        } else {
            appobj=$(['<div '+trid+'><a href="javascript:;" target="_blank"  class=" layui-word-aux">'+file.fileName+'</a>'
                ,'<i class="fa fa-spinner fa-pulse  layui-hide upload-loading"></i>'
                ,'<button class="layui-btn layui-btn-xs upload-reload layui-hide">重传</button>'
                ,'<button class="layui-btn layui-btn-xs layui-btn-danger upload-delete layui-hide">删除</button></div>'].join(''));
        }
    }
    return appobj;
}
UploadFile.prototype.init = function () {
    var guid = new GUID();
    var that = this
        ,config=this.config
        , initControl = function () {//初始化控件
        layui.use('upload', function () {
            var $ = layui.jquery
                , upload = layui.upload;

            var data=null;
            if(config.multiple||"no"===config.backType){
                var gid=config.input.val()||guid.newGUID();
                data={"attachmentNo":gid};
                if(!config.multiple){
                    data={"attachmentNo":gid,"single":"true"};
                }
            }
            upload.render({
                elem: config.btn
                ,url: ctx+'common/upload/uploadFile'
                ,accept: config.accept
                ,multiple: config.multiple
                ,data:data
                ,choose: function(obj){
                    var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                    if(!config.multiple){
                        if("image"===config.accept){
                            //config.appendArea.find(".upload-btn").prevAll().remove();
                            config.appendArea.find(".upload-btn").addClass("layui-hide");
                        }else{
                            config.appendArea.html("");
                        }
                    }
                    obj.preview(function(index, file,result){
                        var _f={fileName:file.name,fileSize:(file.size/1014).toFixed(1),index:index}
                        var op = that.genAppend(_f)
                        if("image"!==config.accept){
                            op.find('.upload-loading').removeClass('layui-hide');
                            config.appendArea.append(op)
                            op.find('.upload-reload').on('click', function () {
                                obj.upload(index, file);
                                op.find('.upload-loading').removeClass('layui-hide')
                                op.find('.upload-reload').addClass('layui-hide')
                                op.find('.upload-delete').addClass('layui-hide')
                                return false;
                            });
                        }else{
                            op.find('img').attr("src",result);
                            config.appendArea.prepend(op)
                        }

                    });
                }
                ,done: function(res, index, upload){
                    if(res.code == 0){ //上传成功
                        var tr = config.appendArea.find('#upload-'+ index)
                            ,deldata=null;
                        if(config.multiple||"no"===config.backType){
                            deldata={"id":res.attid};
                            download(tr,res.attid)
                            if(config.input.val()==""&&res.attachmentNo){
                                config.input.val(res.attachmentNo)
                            }
                        }else{
                            deldata={"path":res.filePath};
                            download(tr,res.filePath)
                            config.input.val(res.filePath)
                        }
                        tr.find('.upload-loading').addClass('layui-hide')
                        tr.find('.upload-delete').removeClass('layui-hide').on("click",function () {
                            delFile(deldata,tr);
                            return false;
                        });
                    	return delete this.files[index]; //删除文件队列已经上传成功的文件
                    }
                    this.error(index, upload);
                }
                ,error: function(index, upload){
                    delete this.files[index];
                    var tr = config.appendArea.find('#upload-'+ index)
                    tr.find('.upload-delete').removeClass('layui-hide').on("click",function () {
                        tr.remove();
                        return false;
                    });
                    tr.find('.upload-loading').addClass('layui-hide')
                    tr.find('.upload-reload').removeClass('layui-hide')
                }
            });
        })

    }
        ,getFileList=function(callback){//初始化值
        var data={"path":config.input.val()};
        if(config.multiple||"no"===config.backType){
            data={"attachmentNo":config.input.val()};
        }
        $.get(ctx+"common/upload/getFileList",data,function (res) {
            if(res.code==0){
                $.each(res.fileList,function (i,file) {
                    var ap=that.genAppend(file)
                        ,deldata=null;
                    if("image"!==config.accept){
                        config.appendArea.append(ap)
                    }else{
                        if(file.byte){
                        ap.find('img').attr("src","data:image/jpg;base64,"+file.byte)
                        }else{
                            ap.find('img').attr("src","/img/no.jpg")
                        }
                        config.appendArea.prepend(ap)
                        if(!config.multiple){config.appendArea.find('.upload-btn').addClass('layui-hide');}
                    }
                    if(config.multiple||"no"===config.backType){
                        deldata={"id":file.id};
                        download(ap,file.id)
                    }else{
                        deldata={"path":file.path};
                        download(ap,file.path)
                    }
                    ap.find(".upload-delete").removeClass("layui-hide").on("click",function () {
                        delFile(deldata,ap);
                        return false;
                    });
                })
                initControl();
            }
        })
    }
        ,delFile=function(data,obj){//删除文件
        layer.confirm('确定要删除？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                type: 'POST',
                url: '/common/upload/delFile',
                dataType: 'json',
                async: false,
                data: data,
                success: function (res) {
                    if (res.code == 0) {
                        obj.remove();
                        if("image"===config.accept&&!config.multiple){
                            config.appendArea.find('.upload-btn').removeClass('layui-hide');
                        }
                        return layer.msg('删除成功', {icon: 1});
                    }else{
                        return layer.msg("服务器发生异常，删除失败", {
                            icon: 2
                            ,shift: 6
                        })
                    }
                }
            });
        });

    }
        ,download=function(ob,idorpath){//下载
        if(config.multiple||"no"===config.backType){
            ob.find('a').attr("href", "/common/upload/downloadFile?id=" + idorpath).attr("target", "_blank");
        }else{
            ob.find('a').attr("href", "/common/upload/downloadFile?path=" + idorpath).attr("target", "_blank");
        }

    }
    getFileList();
}
<!--FileUpload结束-->


<!--Select开始-->
var Select = function (obj) {
    var that = this;
    var id= obj.data("id")
        ,skin=obj.data("skin")||"primary"
        ,radio=obj.data("radio")||"false"
        ,callback=obj.data("click")||""
        ,search=obj.data("search")||""
        ,type=obj.data("type")||""  //user,user-tree,dept,dept-tree
        ,pid=obj.data("pid")||""  // 父ID，解决只显示某个层级下数据问题
        ,value=obj.data("value")||""  // 选中的项
    var option={
        id:id
        ,callback:callback //回调方法
        ,type:type
        ,pid:pid
        ,value:value
    }
    that.config = $.extend({}, that.config, option)

    //添加属性
    obj.attr("xm-select",id);
    obj.attr("xm-select-skin",skin);  //样式 default，primary，normal，warm，danger
    obj.attr("xm-select-search",search); //支持搜索
    if(radio==true) {
        obj.attr("xm-select-radio","");  //单选
    }

    return that.init();

}
Select.prototype.init=function () {

    var config=this.config

    var url = "";
    if(config.type=="user") {
        url =ctx + 'system/user/getList4Select/';
    } else if (config.type=="user-tree") {
        url = "";
    } else if(config.type=="dept") {
        url = ctx + 'system/dept/getTree4Select?deptid='+ config.pid;
    }else if(config.type=="dept-group") {
        url = ctx + 'system/dept/getList4Select/';
    }  else {
        layui.formSelects.data(config.id, 'local', {
                arr: [
                    // {name: '分组1', type: 'optgroup'},
                    // {name: '北京', value: 1,  children: [{name: '朝阳', disabled: true, value: 11}, {name: '海淀', value: 12}]},
                    // {name: '分组2', type: 'optgroup'},
                    // {name: '深圳', value: 2, children: [{name: '龙岗', value: 21}]},
                    {
                        "name": "北京",
                        "value": 1,
                        "children": [
                            {
                                "name": "朝阳",
                                "value": 11,
                                "children": [
                                    {"name": "酒仙桥", "value": 111},
                                    {"name": "望京东", "value": 121}
                                ]
                            },
                            {"name": "海淀", "value": 12}
                        ]
                    },
                    {
                        "name": "深圳",
                        "value": 2,
                        "children": [
                            {"name": "龙岗", "value": 21}
                        ]
                    }
                ],
                tree: {
                    //在点击节点的时候, 如果没有子级数据, 会触发此事件
                    nextClick: function(id, item, callback){
                        //需要在callback中给定一个数组结构, 用来展示子级数据
                        // callback([
                        //     {name: 'test1', value: Math.random()},
                        //     {name: 'test2', value: Math.random()}
                        // ])
                    },
                }
            });
    }

    //动态绑定
    if(url) {
        layui.formSelects.config(config.id, {
            searchUrl: url,
            // type: 'get',                //请求方式: post, get, put, delete...
            // header: {},                 //自定义请求头
            // data: {},                   //自定义除搜索内容外的其他数据
            // searchUrl: '',              //搜索地址, 默认使用xm-select-search的值, 此参数优先级高
            // searchName: 'keyword',      //自定义搜索内容的key值
            // searchVal: '',              //自定义搜索内容, 搜素一次后失效, 优先级高于搜索框中的值
            // keyName: 'name',            //自定义返回数据中name的key, 默认 name
            // keyVal: 'value',            //自定义返回数据中value的key, 默认 value
            // keySel: 'selected',         //自定义返回数据中selected的key, 默认 selected
            // keyDis: 'disabled',         //自定义返回数据中disabled的key, 默认 disabled
            // keyChildren: 'children',    //联动多选自定义children
            // delay: 500,                 //搜索延迟时间, 默认停止输入500m

            success: function(id, url, searchVal, result){

                //绑定之前选中的
                if(config.value) {
                    var selected = ("" + config.value).split(",");
                    layui.formSelects.value(config.id, selected,true); //绑定已选中的值

                    // layui.formSelects.value('example2_2', []);          //赋值空数组, 清空所有
                    // layui.formSelects.value('example2_2', [1, 2]);      //赋值 北京,上海
                    // layui.formSelects.value('example2_2', [5], true);   //追加赋值 天津
                    // layui.formSelects.value('example2_2', [1], false);  //删除 已选择 [北京]
                }

            },
            // error: function(id, url, searchVal, err){           //使用远程方式的error回调
            //     // //同上
            //     // console.log(err);   //err对象
            // },
            // beforeSuccess: function(id, url, searchVal, result){        //success之前的回调, 干嘛呢? 处理数据的, 如果后台不想修改数据, 你也不想修改源码, 那就用这种方式处理下数据结构吧
            //     // console.log(id);        //组件ID xm-select
            //     // console.log(url);       //URL
            //     // console.log(searchVal); //搜索的value
            //     // console.log(result);    //返回的结果
            //     //
            //     // return result;  //必须return一个结果, 这个结果要符合对应的数据结构
            // },
            // beforeSearch: function(id, url, searchVal){         //搜索前调用此方法, return true将触发搜索, 否则不触发
            //     // if(!searchVal){//如果搜索内容为空,就不触发搜索
            //     //     return false;
            //     // }
            //     // return true;
            // },
            // clearInput: false          //当有搜索内容时, 点击选项是否清空搜索内容, 默认不清空
        });
    }

    //模板定义
    if(config.type=="user-tree" || config.type=="user" )
    {


        layui.formSelects.render(config.id, {
            // init: ["100", "109"],           //默认值
            // skin: "danger",                 //多选皮肤
            // height: "auto",                 //是否固定高度, 38px | auto
            // radio: false,                   //是否设置为单选模式
            // direction: "auto",
            // create: function(id, name){
            //     console.log(id);    //多选id
            //     console.log(name);  //创建的标签名称
            //     return Date.now();  //返回该标签对应的val
            // },
            // filter: fun...,         //同formSelects.filter
            // max: 3,                 //多选最多选择量
            // maxTips: fun...,        //同formSelects.maxTips
            // on: fun...,             //同formSelects.on
            // searchType: "title",    //搜索框的位置

            template: function(name, value, selected, disabled){
                return value.name + '<span style="position: absolute; right: 0; color: #A0A0A0; font-size: 14px;">' + value.deptname + '</span>';
            }
        });
    }


    //点击事件
    layui.formSelects.on(config.id, function(id, vals, val, isAdd, isDisabled){
        //id:           点击select的id
        //vals:         当前select已选中的值
        //val:          当前select点击的值
        //isAdd:        当前操作选中or取消
        //isDisabled:   当前选项是否是disabled
        //alert("选择了: " + val.value);
        if(config.callback) {
            var f = eval(config.callback);
            f(val);
        }

    });

}
<!--Select结束-->

function GUID() {
    this.date = new Date();   /* 判断是否初始化过，如果初始化过以下代码，则以下代码将不再执行，实际中只执行一次 */
    if (typeof this.newGUID != 'function') {   /* 生成GUID码 */
        GUID.prototype.newGUID = function () {
            this.date = new Date(); var guidStr = '';
            sexadecimalDate = this.hexadecimal(this.getGUIDDate(), 16);
            sexadecimalTime = this.hexadecimal(this.getGUIDTime(), 16);
            for (var i = 0; i < 9; i++) {
                guidStr += Math.floor(Math.random() * 16).toString(16);
            }
            guidStr += sexadecimalDate;
            guidStr += sexadecimalTime;
            while (guidStr.length < 32) {
                guidStr += Math.floor(Math.random() * 16).toString(16);
            }
            return this.formatGUID(guidStr);
        }
        /* * 功能：获取当前日期的GUID格式，即8位数的日期：19700101 * 返回值：返回GUID日期格式的字条串 */
        GUID.prototype.getGUIDDate = function () {
            return this.date.getFullYear() + this.addZero(this.date.getMonth() + 1) + this.addZero(this.date.getDay());
        }
        /* * 功能：获取当前时间的GUID格式，即8位数的时间，包括毫秒，毫秒为2位数：12300933 * 返回值：返回GUID日期格式的字条串 */
        GUID.prototype.getGUIDTime = function () {
            return this.addZero(this.date.getHours()) + this.addZero(this.date.getMinutes()) + this.addZero(this.date.getSeconds()) + this.addZero(parseInt(this.date.getMilliseconds() / 10));
        }
        /* * 功能: 为一位数的正整数前面添加0，如果是可以转成非NaN数字的字符串也可以实现 * 参数: 参数表示准备再前面添加0的数字或可以转换成数字的字符串 * 返回值: 如果符合条件，返回添加0后的字条串类型，否则返回自身的字符串 */
        GUID.prototype.addZero = function (num) {
            if (Number(num).toString() != 'NaN' && num >= 0 && num < 10) {
                return '0' + Math.floor(num);
            } else {
                return num.toString();
            }
        }
        /*  * 功能：将y进制的数值，转换为x进制的数值 * 参数：第1个参数表示欲转换的数值；第2个参数表示欲转换的进制；第3个参数可选，表示当前的进制数，如不写则为10 * 返回值：返回转换后的字符串 */GUID.prototype.hexadecimal = function (num, x, y) {
            if (y != undefined) { return parseInt(num.toString(), y).toString(x); }
            else { return parseInt(num.toString()).toString(x); }
        }
        /* * 功能：格式化32位的字符串为GUID模式的字符串 * 参数：第1个参数表示32位的字符串 * 返回值：标准GUID格式的字符串 */
        GUID.prototype.formatGUID = function (guidStr) {
            var str1 = guidStr.slice(0, 8) + '-', str2 = guidStr.slice(8, 12) + '-', str3 = guidStr.slice(12, 16) + '-', str4 = guidStr.slice(16, 20) + '-', str5 = guidStr.slice(20);
            return str1 + str2 + str3 + str4 + str5;
        }
    }
}

// 页面控件初始化
initCtrl();