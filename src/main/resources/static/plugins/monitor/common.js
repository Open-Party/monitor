/**
 * Created by soarpenguin on 17/10/8.
 */

/*
 *  映射dataTable中的英文显示为对应中文
 */
$(function () {
    $.extend(true, $.fn.dataTable.defaults, {
        "oLanguage": {
            "sProcessing": "正在加载中......",
            "sSearch": "搜索：",
            "sLengthMenu": "每页显示 _MENU_ 条",
            "sZeroRecords": "对不起，查询不到相关数据！",
            "sEmptyTable": "表中无数据存在！",
            "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
            "sInfoFiltered": "数据表中共为 _MAX_ 条记录",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上一页",
                "sNext": "下一页",
                "sLast": "末页"
            }
        }
    });

});

window.Monitor = {};

Monitor.confirm = function (title, content, callback, closeCall) {

    if (window.location != window.parent.location) {
        jQuery = window.parent.jQuery;
    }

    var confirmModal =
        jQuery('<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myConfirmLabel" aria-hidden="true">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
            '<h4 class="modal-title" id="myConfirmLabel">' + title + '</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>' + content + '</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button id="closeButton" class="btn btn-default">取消</button>' +
            '<button id="okButton" type="submit" class="btn btn-success">确认</button>' +
            '</div>' +
            '</div><!-- /.modal-content -->' +
            '</div><!-- /.modal-dialog -->' +
            '</div>');

    confirmModal.find('#okButton').click(function (event) {
        callback();
        confirmModal.modal('hide');
    });

    confirmModal.find('#closeButton').click(function (event) {
        if (closeCall) {
            closeCall();
        }
        confirmModal.modal('hide');
    });


    confirmModal.modal('show');
};

Monitor.alert = function (title, content) {

    if (window.location != window.parent.location) {
        jQuery = window.parent.jQuery;
    }

    var alertModal =
        jQuery('<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myAlertLabel" aria-hidden="true">' +
            '<div class="modal-dialog">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
            '<h4 class="modal-title" id="myAlertLabel">' + title + '</h4>' +
            '</div>' +
            '<div class="modal-body">' +
            '<p>' + content + '</p>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-default" data-dismiss="modal">好的，我知道了</button>' +
            '</div>' +
            '</div><!-- /.modal-content -->' +
            '</div><!-- /.modal-dialog -->' +
            '</div>');

    alertModal.modal('show');
};
jQuery.ajaxSetup({
    timeout: 300000 //5 second timeout
});

Monitor.success = function(title, content, html) {
    sweetAlert({
        title:title,
        text:content,
        type:'success',
        timer:1500,
        html:html,
        showConfirmButton:false,
    });
};

Monitor.warning = function(title, content, html) {
    sweetAlert({
        title:title,
        text:content,
        type:'warning',
        html:html,
        confirmButtonText: "好的，我知道了",
    });
};

Monitor.error = function(title, content, html) {
    sweetAlert({
        title:title,
        text:content,
        type:'error',
        html:html,
        confirmButtonText: "好的，我知道了",
    });
};

Monitor.loading = function(message) {
    var loadingIcon = '<div class="loader-inner pacman" style="left:40%"><div></div><div></div><div></div><div></div><div></div></div>';
    var loadingMsg = '<p>'+message+'</p>';
    $.blockUI({
        css:{
            'background-color':'rgba(38, 198, 218, 0.6)',
            'color':'#fff',
            'min-height':'90px',
            'width':'20%',
            'left':'40%',
            'padding-top':'30px',
            'border':'none'
        },
        message:loadingIcon + loadingMsg
    });
}

Monitor.unLoading = function() {
    $.unblockUI();
}

Monitor.get = function (action, callback) {
    jQuery.get(action, function (data, status) {
        if (status != "success") {
            Psp.alert("请求失败, 状态: " + status, action);
            return;
        }
        callback(data, status);
    });
};
Monitor.canOperation = function (appname) {
    var Data = {
        "appName": appname
    };
    var canOperation = 0;
    jQuery.ajax({
        type: "POST",
        url: "/admin/canOperation",
        data: Data,
        async: false,
        dataType: "json",
        contentType: "application/x-www-form-urlencoded",
        success: function (res) {
            if (res.success) {
                canOperation = res.data;
            }
        },
    });
    return canOperation;
}



