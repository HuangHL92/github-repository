/*1、代码最前面的分号，可以防止多个文件压缩合并以为其他文件最后一行语句没加分号，而引起合并后的语法错误。2、匿名函数(function(){})();：由于Javascript执行表达式是从圆括号里面到外面，所以可以用圆括号强制执行声明的函数。避免函数体内和外部的变量冲突。3、$实参:$是jquery的简写，很多方法和类库也使用$,这里$接受jQuery对象，也是为了避免$变量冲突，保证插件可以正常运行。4、window, document实参分别接受window, document对象，window, document对象都是全局环境下的，而在函数体内的window, document其实是局部变量，不是全局的window, document对象。这样做有个好处就是可以提高性能，减少作用域链的查询时间，如果你在函数体内需要多次调用window 或 document对象，这样把window 或 document对象当作参数传进去，这样做是非常有必要的。当然如果你的插件用不到这两个对象，那么就不用传递这两个参数了。5、最后剩下一个undefined形参了，那么这个形参是干什么用的呢，看起来是有点多余。undefined在老一辈的浏览器是不被支持的，直接使用会报错，js框架要考虑到兼容性，因此增加一个形参undefined*/;(function ($, window, document, undefined) {    var oTree = function (ele, options) {        this.$element = $(ele); //主元素        this.defaults = {            all: false,     //人物组织都开启            area: null,     //弹窗框宽高            search: true,    //支持搜索            cls_disp: "",            selectedids: "",            saveVal: []        };        //将一个新的空对象做为$.extend的第一个参数，defaults和用户传递的参数对象紧随其后，        //这样做的好处是所有值被合并到这个空对象上，保护了插件里面的默认值。        this.settings = $.extend({}, this.defaults, options);    };    // var saveVal = []; //操作以保存的值， 数组形式    var allJson;      //从后台获取原始组织架构数据，防止多次加载    var filterDataList;  //数据结构进行处理    var orgZTree; //可以多方位操作ztree    oTree.prototype = {        dialog: function () {            var contentsHTML, $wrapper, _this, $treeWrapper, allLoadData;            _this = this;            contentsHTML = '<div class="multiPickerDlg clearfix"><div class="multiPickerDlg_left"><div class="multiPickerDlg_left_cnt"><!--loading--><div class="ww_loading loading_left_wrap js_search_loading"><span class="ww_loadingIconSmall"></span><span class="ww_loading_text">正在搜索，请稍候…</span></div><!--jstree--><div id="partyTree" class="multiPickerDlg_left_cnt_list"><div class="ww_loading js_search_loading"><span class="ww_loadingIconSmall"></span><span class="ww_loading_text">正在搜索，请稍候…</span></div><div class="ztree_wrapper"><ul id="ztreeArea" class="ztree"></ul></div></div><div class="multiPickerDlg_left_mask js_left_mask"></div></div></div><div class="multiPickerDlg_right"><div class="clearfix"><div class="multiPickerDlg_right_title" style="float: left;">已选择的用户[0]</div><div class="cancel_all_selected" style="float: right;color: #1E9FFF;cursor: pointer;">删除全部</div></div><div class="multiPickerDlg_right_cnt"><div class="js_right_col"><ul id="saveNode"></ul></div><div class="multiPickerDlg_right_no_result"><i></i>未选择用户</div></div></div></div>';            // var ctx = $("#ctx").val();            //获取全部组织架构包括人物            $.getJSON('/system/user/orgTree?random=' + new Date().getTime()).done(function (data) {                allJson = data;                filterDataList = _this.filterDataSloveList(data);                // 页面进入渲染                _this.getValue();                if (_this.settings.all) {                    //全部开启                    allLoadData = _this.dataSolve(data);  //进行数据处理                }                ;            });            // 新增/修改按钮点击使事件            _this.$element.on('click', '.'+ this.settings.cls_disp, function () {                if (!allJson || allJson == '') {                    layer.msg('获取组织架构失败', {icon: 5});                    return;                }                layer.confirm(contentsHTML, {                    skin: 'org-skin',                    title: '选择用户',                    // scrollbar: false,                    area: _this.settings.area, //优化，使用配置项中的area                    resize: false, //禁止弹出窗拉伸                    btn: ['确认', '取消'], //按钮                    cancel: function (index, layero) {  //右上角关闭按钮                        var oldValue = _this.savedJson();                        _this.settings.saveVal = oldValue;                    }                }, function (index) {                    _this.renderOut(_this.settings.saveVal);                    layer.close(index);                }, function () {                    var oldValue = _this.savedJson();                    _this.settings.saveVal = oldValue;                });                //第一个loading隐藏                $('.ww_loading').css('display', 'none');                $leftWrap = $('.multiPickerDlg_left_cnt');                $treeWrapper = $('.multiPickerDlg').find('.multiPickerDlg_left_cnt_list');                $zTreeWrap = $treeWrapper.find('#ztreeArea');                //判断搜索是否开启                if (_this.settings.search) {                    _this.searchFuc(filterDataList);                }                ;                if (_this.settings.all) {                    //全部开启                    _this.loadAll($zTreeWrap, allLoadData);                }                ;            });        },        filterDataSloveList: function (data) {            var _this = this;            var treeNode = _this.dataSolve(data);            var filterResult = [];            function filterTree(fNode, dataNode) {                dataNode.forEach(function (aDate) {                    fNode.push({                        id: aDate.id,                        name: aDate.name,                        type: aDate.type,                        crumbs: aDate.crumbs,                        spellName: aDate.spellName,                        children: aDate.children                    });                    if (aDate.children) {                        filterTree(fNode, aDate.children)                    }                });            };            filterTree(filterResult, treeNode);            function unique(arr) {                var ret = [];                for (var i = 0, j = arr.length; i < j; i++) {                    if (ret.indexOf(arr[i].id) === -1) {                        ret.push(arr[i]);                    }                    ;                }                ;                return ret;            }            var nodeResult = unique(filterResult);            return nodeResult;        },        loadAll: function (ele, data) {            var _this = this;            var $ele = $(ele);            orgZTree = $.fn.zTree.init($ele, {                view: {                    dblClickExpand: true,                    selectedMulti: false,                    showLine: false,                    addHoverDom: addHoverDom,                    removeHoverDom: removeHoverDom,                    addDiyDom: addDiyDom                },                callback: {                    onClick: function (event, treeId, treeNode, clickFlag) {                        // 选项本身只作为打开和关闭组织用                        // 点击后默认展开该节点                        // orgZTree.expandNode(treeNode);                        // orgZTree.cancelSelectedNode(treeNode);                    }                }            }, data);            // 展开全部            orgZTree.expandAll(true);            //ztree 鼠标经过添加&删除 效果            function addHoverDom(treeId, treeNode) {                var pObj = $('#' + treeNode.tId);                pObj.children('.jstree-wholerow').addClass('jstree-wholerow-hover');            };            function removeHoverDom(treeId, treeNode) {                $('.ztree .jstree-wholerow').removeClass('jstree-wholerow-hover');            };            //添加自定义div            function addDiyDom(treeId, treeNode) {                var aObj = $('#' + treeNode.tId + '_a');                if ($('#diyBtn_' + treeNode.id).length > 0) return;                var selectedStr = '<div class="jstree-checked jstree-custom-checked"></div>';                var unSelectedStr = '<div class="jstree-checked jstree-un-checked"></div>';                var halfSelectedStr = '<div class="jstree-checked jstree-half-checked"></div>';                var hoverStr = '<div class="jstree-wholerow"></div>';                var pObj = $('#' + treeNode.tId);                pObj.prepend(hoverStr);                var status = _this.nodeStatus(treeNode);                if (status.status == 0) { // 不存在                    // 此处解决没有用户的部门不添加选择按钮                    if (status.hasUser || treeNode.type == 'user') {                        aObj.append(unSelectedStr);                    }                } else if (status.status == 1) { // 全部存在                    aObj.append(selectedStr);                } else { // 部分存在                    aObj.append(halfSelectedStr);                }                // 列表的整行点击事件                pObj.find('.jstree-checked').eq(0).bind('click', function () {                    var aObj = $("#" + treeNode.tId + "_a");                    var checkIcon = aObj.find('.jstree-checked');                    var status;                    if (checkIcon.hasClass('jstree-custom-checked')) {// 已选中                        status = 1;                    } else {// 未选中/半选中                        status = 0;                    }                    if (treeNode.isParent) { // 父级的场合，更新所有子节点                        var array = [];                        _this.getNodeChildren(array, treeNode);                        for (var i = 0; i < array.length; i++) {                            // if (treeNode.id != array[i].id) {                                refreshNodeSaved(array[i], status);                            // }                        }                    } else {                        refreshNodeSaved(treeNode, status); // 更新自身节点                    }                    // 更新自身及所有子节点选中状态                    _this.refreshNodeChecked(treeNode.tId, status);                    // 更新所有父节点选中状态                    _this.refreshParentChecked(treeNode)                    //以数组形式返回选种值                    _this.renderNode(_this.settings.saveVal);                    _this.deleteNode(_this.settings.saveVal);                });                /*for(var i=0; i<_this.settings.saveVal.length; i++) {                    if (treeNode.id == _this.settings.saveVal[i].id) {                        aObj.find('.jstree-custom-checked').addClass('selected');                    }                };*/            };            function refreshNodeSaved(treeNode, status) {                if (treeNode.type != 'user') {                    return;                }                //获取对应的id或者name值，只需treeNode.id 或者 treeNode.name 即可                var curId = treeNode.id;                var curName = treeNode.name;                var curCrumbs = treeNode.crumbs;                var repIndex = -1;  //获取重复数组的索引                if (_this.settings.saveVal.length > 0) {                    for (var i = 0; i < _this.settings.saveVal.length; i++) {                        if (_this.settings.saveVal[i].id == curId) {                            repIndex = i;                        }                    }                }                if (status == 1 && repIndex >= 0) { // 本来选中状态，且已选中存在：删除                    _this.settings.saveVal.splice(repIndex, 1);                } else if (status == 0 && repIndex < 0) { // 本来未选中，且已选中不存在：添加                    _this.settings.saveVal.push({                        id: curId,                        name: curName,                        crumbs: curCrumbs                    });                }            };            //渲染已选中部门            if (_this.settings.saveVal && _this.settings.saveVal.length > 0) {                _this.renderNode(_this.settings.saveVal);                _this.deleteNode(_this.settings.saveVal);            }            ;            $('.cancel_all_selected').on('click', function () {                _this.settings.saveVal = [];                _this.renderNode(_this.settings.saveVal);                $.each($('#ztreeArea').find('.jstree-checked'), function (index, item) {                    $(item).removeClass('jstree-custom-checked').removeClass('jstree-half-checked').removeClass('jstree-un-checked');                    $(item).addClass("jstree-un-checked");                    // $(item).addClass("jstree-custom-checked");                })            })        },        // 获取所有子节点        getNodeChildren: function (array, treeNode) {            var _this = this;            var children = treeNode.children;            if (children) {                for (var i = 0; i < children.length; i++) {                    _this.getNodeChildren(array, children[i])                }            } else {                if (treeNode.type == 'user') {                    array.push({                        tId: treeNode.tId,                        id: treeNode.id,                        name: treeNode.name,                        type: treeNode.type,                        crumbs: treeNode.crumbs                    });                }            }        },        // 判断节点的状态（所有子节点的选中状态）        nodeStatus: function (treeNode) {            var _this = this;            var array = [];            var hasUser = false;            _this.getNodeChildren(array, treeNode);            var count = 0;            for (var i = 0; i < array.length; i++) {                for (var j = 0; j < _this.settings.saveVal.length; j++) {                    if (array[i].id == _this.settings.saveVal[j].id) {                        count += 1;                        break;                    }                }                if (array[i].type == 'user' && !hasUser) {                    hasUser = true;                }                if ((count > 0) && (count - i != 1) && hasUser)                    break;            }            if (count == 0)  // 不存在                return {                    status: 0,                    hasUser: hasUser                };            else if (count == array.length) // 全部存在                return {                    status: 1,                    hasUser: hasUser                };            else  // 部分存在                return {                    status: -1,                    hasUser: hasUser                };        },        refreshNodeChecked: function (tId, status) {            var pObj = $("#" + tId);            $.each(pObj.find('.jstree-checked'), function (index, item) {                $(item).removeClass('jstree-custom-checked').removeClass('jstree-half-checked')                    .removeClass('jstree-un-checked');                if (status == 1) {                    $(item).addClass("jstree-un-checked");                } else {                    $(item).addClass("jstree-custom-checked");                }            })        },        refreshParentChecked: function (treeNode) {            var _this = this;            var parent = treeNode.getParentNode();            if (parent) {                var pObj = $("#" + parent.tId);                var cObj = $("#" + parent.tId + "_a").find('.jstree-checked');                var ckFlag = cObj.hasClass('jstree-custom-checked') ? 1 : 0;                // 此处-1为了去除父节点自身的图标                var total = pObj.find('.jstree-checked').length - 1;                var checked = pObj.find('.jstree-custom-checked').length - ckFlag;                if (checked == 0) {                    cObj.removeClass('jstree-custom-checked').removeClass('jstree-half-checked')                        .removeClass('jstree-un-checked').addClass("jstree-un-checked");                } else if (checked > 0 && checked != total) {                    cObj.removeClass('jstree-custom-checked').removeClass('jstree-half-checked')                        .removeClass('jstree-un-checked').addClass("jstree-half-checked");                } else {                    cObj.removeClass('jstree-custom-checked').removeClass('jstree-half-checked')                        .removeClass('jstree-un-checked').addClass("jstree-custom-checked");                }                _this.refreshParentChecked(parent);            }        },        // 查询事件        searchFuc: function (data) {            var _this = this;            $('.multiPickerDlg').addClass('multiPickerDlg_WithSearch');            var sdom = '<div class="multiPickerDlg_search_wrapper">'                + '<div class="multiPickerDlg_search">'                + '<span class="ww_searchInput">'                + '<span class="ww_commonImg ww_commonImg_Search ww_searchInput_icon"href="javascript:;"></span>'                + '<span class="ww_commonImg ww_commonImg_ClearText ww_searchInput_delete" id="clearMemberSearchInput"></span>'                + '<input type="text"id="memberSearchInput"class="qui_inputText ww_inputText ww_searchInput_text" placeholder="点击回车键搜索单位、部门、姓名"></span>'                + '</div>'                + '<div id="searchResult"class="ww_searchResult"style="display: none">'                + '<div id="search_member_list_title"class="ww_searchResult_title ww_searchResult_title_First">搜索结果</div>'                + '<div class="search_member_none">无搜索结果...</div>'                + '<ul id="search_member_list"></ul>'                + '</div>'                + '</div>';            $('.multiPickerDlg_left_cnt').prepend(sdom);            var $input = $('#memberSearchInput');            //聚焦失焦样式            $input.focus(function () {                $(this).addClass('onfocus');            }).blur(function () {                $(this).removeClass('onfocus');            });            $input.on('keyup', function (event) {                event.stopPropagation();            }).on('keydown', function (event) {                if (!event) event = window.event;                if ((event.keyCode || event.which) == 13) {                    var inputValue = $(this).val();                    // 未输入点击回车无效                    if (inputValue == null || inputValue == '')                        return false;                    inputValue = inputValue.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&");                    var matcher = new RegExp(inputValue, "i");                    var filterTxt = $.grep(data, function (value) {                        return matcher.test(value.name);                    });                    var totalFilterTxt = [];                    // 循环查询结果找出结果下的用户（查找结果会是单位/部门，此处忽略但是需要找到下面对应的用户显示）                    filterTxt.forEach(function (data) {                        if (data.children) {                            var array = [];                            var selectedTreeNode = orgZTree.getNodeByParam("id", data.id);                            _this.getNodeChildren(array, selectedTreeNode);                            array.forEach(function (value) {                                if (findElem(totalFilterTxt, 'id', value.id) == -1 && value.type == 'user') {                                    totalFilterTxt.push({                                        id: value.id,                                        name: value.name,                                        crumbs: value.crumbs                                    });                                }                            })                        } else {                            if (findElem(totalFilterTxt, 'id', data.id) == -1 && data.type == 'user') {                                totalFilterTxt.push({                                    id: data.id,                                    name: data.name,                                    crumbs: data.crumbs                                });                            }                        }                    });                    if (inputValue) {                        $('#clearMemberSearchInput').show();                        $('#searchResult').show();                        $('#partyTree').hide();                        $('#search_member_list').empty();                        var resultList = '';                        totalFilterTxt.forEach(function (data) {                            var index = data.crumbs.lastIndexOf('/' + data.name);                            var ck = findElem(_this.settings.saveVal, 'id', data.id);                            var ckIcon;                            if (ck == -1)                                ckIcon = '<div class="jstree-checked jstree-un-checked"></div>';                            else                                ckIcon = '<div class="jstree-checked jstree-custom-checked"></div>';                            resultList += '<li id="search_result_' + data.id + '" data-id="' + data.id + '" data-name="' + data.name + '" title="' + data.crumbs + '"><i class="ww_commonImg ww_commonImg_TreeMenuThumb"></i>' + data.name + '<span style="color: orange;">(' + data.crumbs.substring(1, index) + ')</span>' + ckIcon + '</li>'                        });                        //判断筛选之后有没有值                        if (resultList) {                            $('.search_member_none').hide();                        } else {                            $('.search_member_none').show();                        }                        $('#search_member_list').append(resultList);                        //清空（查询框清空按钮点击事件）                        $('.ww_searchInput_delete').show();                        $('.ww_searchInput_delete').on('click', function () {                            $('#memberSearchInput').val(null);                            $('#searchResult').hide();                            $('#partyTree').show();                            $('#search_member_list').empty();                            $('.search_member_none').show();                            $(this).hide();                        });                        // 查询结果列表点击事件                        $('#search_member_list li').on('click', function () {                            var selectId = $(this).attr('data-id');                            var selectName = $(this).attr('data-name');                            var selectCrumbs = $(this).attr('title');                            var repInx;  //获取重复数组的索引                            if (_this.settings.saveVal.length > 0) {                                for (var i = 0; i < _this.settings.saveVal.length; i++) {                                    if (_this.settings.saveVal[i].id == selectId) {                                        repInx = i;                                    }                                    ;                                }                                ;                            }                            ;                            //ztree进行操作，点击li之后 展开树                            var selectedTreeNode = orgZTree.getNodesByFilter(function(node) {                                return (node.id == selectId && node.type == 'user');                            }, true);                            if (repInx >= 0) {                                _this.settings.saveVal.splice(repInx, 1);                                $(this).find('.jstree-checked').removeClass('jstree-custom-checked').addClass('jstree-un-checked')                            } else {                                _this.settings.saveVal.push({                                    id: selectId,                                    name: selectName,                                    crumbs: selectCrumbs                                });                                $(this).find('.jstree-checked').removeClass('jstree-un-checked').addClass('jstree-custom-checked');                            }                            //处理ztree数 的选中图标                            // 更新自身                            _this.refreshNodeChecked(selectedTreeNode.tId, repInx >= 0 ? 1 : 0);                            // 更新所有父节点选中状态                            _this.refreshParentChecked(selectedTreeNode)                            // $('#searchResult').hide();                            // $('#partyTree').show();                            // $('#search_member_list').empty();                            // $('#memberSearchInput').val(null);                            // $('#clearMemberSearchInput').hide();                            if (_this.settings.saveVal.length > 0) {                                $('.multiPickerDlg_right_no_result').hide();                            } else {                                $('.multiPickerDlg_right_no_result').show();                            }                            ;                            //以数组形式返回选种值                            _this.renderNode(_this.settings.saveVal);                            _this.deleteNode(_this.settings.saveVal);                            //ztree进行操作，点击li之后 展开树                            // var selectedTreeNode = orgZTree.getNodeByParam("id", selectId);                            /*//处理ztree数 的选中图标                            var aObj = $("#" + selectedTreeNode.tId + "_a");                            var checkIcon = aObj.find('.jstree-custom-checked');                            if(checkIcon.hasClass('selected')) {                                checkIcon.removeClass('selected');                            }else {                                checkIcon.addClass('selected');                            };*/                            // orgZTree.selectNode(selectedTreeNode, true);//指定选中ID的节点                            // orgZTree.expandNode(selectedTreeNode, true, false);//指定选中ID节点展开                            // orgZTree.cancelSelectedNode();  ztree取消选中                        });                    } else {                        $('#searchResult').hide();                        $('#partyTree').show();                        $('#search_member_list').empty();                        $('#clearMemberSearchInput').hide();                        $('.search_member_none').show();                    }                }            });            /*按照属性值，查找对象*/            function findElem(arrayToSearch, attr, val) {                for (var i = 0; i < arrayToSearch.length; i++) {                    if (arrayToSearch[i][attr] == val) {                        return i;                    }                }                return -1;            }        },        dataSolve: function (data) {            var memberData = data.data;            var zTreeNode = [];            //浏览器不支持中文转拼音            function fillNode(zNode, dataNode, presentName) {                dataNode.forEach(function (aDate) {                    var thisNode = {                        id: aDate.id,                        name: aDate.deptname,                        type: aDate.type,                        isParent: aDate.isParent,                        parentId: aDate.parent_id,                        crumbs: presentName + '/' + aDate.deptname,                        children: []                    };                    var noChildNode = {                        id: aDate.id,                        name: aDate.deptname,                        type: aDate.type,                        isParent: aDate.isParent,                        parentId: aDate.parent_id,                        crumbs: presentName + '/' + aDate.deptname,                    };                    if (aDate.children.length > 0) {                        zNode.unshift(thisNode);                    } else {                        zNode.push(noChildNode);                    }                    ;                    fillNode(thisNode.children, aDate.children, thisNode.crumbs);                });            };            fillNode(zTreeNode, memberData, '');            return zTreeNode;        },        getValue: function () {            var _this = this;            var savedVal = $('#'+ this.settings.selectedids).val().split(',');            var filterJson = [];            for (var i = 0; i < savedVal.length; i++) {                (function (i) {                    filterDataList.forEach(function (item, index) {                        if (item.id == savedVal[i] && item.type == 'user') {                            return filterJson.push(item);                        }                        ;                    });                })(i);            }            ;            _this.settings.saveVal = filterJson;            _this.renderOut(_this.settings.saveVal);            return _this.settings.saveVal;        },        // 查找带回页面的删除事件        outRepair: function () {            var _this = this;            $(document).on('click', '.js_disselect_party', function () {                $(this).parent().remove();                _this.settings.saveVal = _this.savedJson();                /*if(_this.settings.saveVal.length>0) {                    $('.js_show_party_selector').html('修改');                }else {                    $('.js_show_party_selector').html('添加');                };*/                //处理ztree数 的选中图标                // var curId = $(this).siblings('.parydata').attr('data-id');                // var selectedTreeNode = orgZTree.getNodeByParam('id', curId);                // var aObj = $('#' + selectedTreeNode.tId + "_a");                // var checkIcon = aObj.find('.jstree-custom-checked');                // checkIcon.removeClass('selected');            });        },        savedJson: function () {            var itemList = $('.'+ this.settings.cls_disp).children('div');            var saveLength = itemList.length;            var jsonList = [];            var outInputVal = '';            if (saveLength > 0) {                itemList.each(function (index, ele) {                    jsonList.push({                        name: $(ele).find('.parydata').attr('data-name'),                        id: $(ele).find('.parydata').attr('data-id'),                        crumbs: $(ele).find('.parydata').attr('data-crumbs')                    });                    outInputVal += $(ele).find('.parydata').attr('data-id') + ',';                });            }            ;            //渲染外部input的值            $('#'+ this.settings.selectedids).val(outInputVal);            return jsonList;        },        // 弹出框确定按钮事件        renderOut: function (arr) {            $('.'+ this.settings.cls_disp).html('');            var wrap = '';            var perVal = '';            var perName = '';            if (arr.length > 0) {                arr.map(function (ele, index) {                    perVal += ele.id + ',';                    perName += ele.name + ',';                    wrap += '<div class="ww_groupSelBtn_item">'                        + '<span class="ww_commonImg ww_commonImg_TreeMenuThumb"></span>'                        + '<span class="ww_groupSelBtn_item_text">'                        + ele.name                        + '</span>'                        + '<span class="ww_commonImg ww_commonImg_GroupDelSel js_disselect_party">x</span>'                        + '<input type="hidden" class="parydata" name="partyid" data-name="'                        + ele.name                        + '" data-id="'                        + ele.id                        + '" data-crumbs="'                        + ele.crumbs                        + '" value="'                        + ele.id                        + '">'                        + '</div>';                });                // $('.js_show_party_selector').html('修改');            } else {                // $('.js_show_party_selector').html('添加');            }            $('.'+ this.settings.cls_disp).append(wrap);            // $('.js_party_select_result_show').html('共选择<span style="color: orange;">(' + arr.length + ')</span>人');            $('.'+ this.settings.cls_disp).val(perName.length > 0 ? perName.substring(0, perName.length - 1) : '');            //渲染外部input的值            $('#'+ this.settings.selectedids).val(perVal.length > 0 ? perVal.substring(0, perVal.length - 1) : '');        },        // 渲染右侧已选择人员        renderNode: function (arr) {            var _this = this;            var liHtml = '';            $('#saveNode').html('');            // 更新已选择用户数            $('.multiPickerDlg_right_title').html('已选择的用户<span style="color: orange;">(' + arr.length + ')</span>');            if (arr.length > 0) {                $('.multiPickerDlg_right_no_result').hide();            } else {                $('.multiPickerDlg_right_no_result').show();            }            arr.map(function (item, index) {                var index = item.crumbs.lastIndexOf('/' + item.name);                var additional = '<span style="color: orange;">(' + item.crumbs.substring(1, index) + ')</span>';                liHtml = '<li title=' + item.crumbs + ' data-name=' + item.name + ' data-id=' + item.id + ' class="ww_menuDialog_DualCols_colRight_cnt_item token-input-token js_list_item">'                    + '<span class="ww_commonImg icon icon_folder_blue"></span>'                    + '<span class="ww_treeMenu_item_text" title=' + item.crumbs + '>' + item.name + additional + '</span>'                    + '<a href="javascript:" class="ww_menuDialog_DualCols_colRight_cnt_item_delete"><span class="ww_commonImg ww_commonImg_DeleteItem js_delete"></span></a>'                    + '</li>';                $('#saveNode').append(liHtml);            });        },        // 删除选中节点事件        deleteNode: function (arr) {            var _this = this;            var arrNode = arr;            $('.js_list_item').on('click', function () {                var curId = $(this).attr('data-id');  //获取当前ID                for (var i = 0; i < arrNode.length; i++) {                    if (arrNode[i].id == curId) {                        arrNode.splice(i, 1);                    }                    ;                }                ;                //删除选中树节点的选中图标                var selectedTreeNode = orgZTree.getNodesByFilter(function(node) {                    return (node.id == curId && node.type == 'user');                }, true);                var pObj = $("#" + selectedTreeNode.tId);                // 此处需要根据节点是否已加载来做操作                if (pObj.length > 0) {                    _this.refreshNodeChecked(selectedTreeNode.tId, 1);                    _this.refreshParentChecked(selectedTreeNode);                } else {                    parentStatus(selectedTreeNode);                }                // 更新查询结果的选中状态                var searchTreeNode = $('#search_result_' + curId);                if (searchTreeNode.length > 0) {                    searchTreeNode.find('.jstree-checked').removeClass('jstree-custom-checked').addClass('jstree-un-checked')                }                //操作接下来的逻辑                $(this).remove();                _this.settings.saveVal = arrNode;                // 更新已选择用户数                $('.multiPickerDlg_right_title').html('已选择的用户<span style="color: orange;">(' + _this.settings.saveVal.length + ')</span>');                if (_this.settings.saveVal.length > 0) {                    $('.multiPickerDlg_right_no_result').hide();                } else {                    $('.multiPickerDlg_right_no_result').show();                }                ;                return _this.settings.saveVal;            });            function parentStatus(treeNode) {                var parent = treeNode.getParentNode();                if (parent) {                    var status = _this.nodeStatus(parent);                    var cObj = $("#" + parent.tId + "_a").find('.jstree-checked');                    if (status.status == 0) { // 不存在                        cObj.removeClass('jstree-custom-checked').removeClass('jstree-half-checked')                            .removeClass('jstree-un-checked').addClass("jstree-un-checked");                    } else if (status.status == 1) { // 全部存在                        cObj.removeClass('jstree-custom-checked').removeClass('jstree-half-checked')                            .removeClass('jstree-un-checked').addClass("jstree-custom-checked");                    } else { // 部分存在                        cObj.removeClass('jstree-custom-checked').removeClass('jstree-half-checked')                            .removeClass('jstree-un-checked').addClass("jstree-half-checked");                    }                    parentStatus(parent);                }            };        },        init: function () {            this.dialog();            this.outRepair();        }    };    $.fn.orgTree = function (opts) {        var ot = new oTree(this, opts); //接收两个参数，主元素 + 设置参数 this: 使用这个插件的容器  opts: 外部配置        return ot.init();    };})(jQuery, window, document);