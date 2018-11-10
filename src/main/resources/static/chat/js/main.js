window.flag = 1;
$(function () {
    chat_change()
    wait_agree()
    $('<audio id="chatAudio"><source src="/chat/notice/notify.ogg" type="audio/ogg"> <source src="/chat/notice/notify.mp3" type="audio/mpeg"><source src="/chat/notice/notify.wav" type="audio/wav"> </audio>').appendTo('body');
    $(".friendList .big_cate").click(function () {
        var $that = $(this)
        var $next = $(this).next('.detail_friend');
        if ($next != undefined) {
            if ($next.hasClass('on')) {
                $next.css('display', 'none');
                $that.find('.my_icon_right').addClass('layui-icon-right').removeClass('layui-icon-down')
                $next.removeClass('on').addClass('off')
            }
            else {
                $that.find('.my_icon_right').removeClass('layui-icon-right').addClass('layui-icon-down')
                $next.css('display', 'block');
                $next.removeClass('off').addClass('on')
            }
        }

    });
    $(".one_person").click(function () {
        $(this).css("background", "gainsboro").siblings().css("background", "");
        $(this).css("background", "gainsboro").parents(".all_person").siblings().find('.one_person').css("background", "")
        $(this).find('img').css({
            'width': '40px',
            'height': '40px'
        }).parents('.one_person').siblings().find('img').css({
            'width': '30px',
            'height': '30px'
        })
        $(this).parents(".all_person").siblings().find('.one_person').find('img').css({
            'width': '30px',
            'height': '30px'
        })
    });
    $(".one_person").dblclick(function () {
        $(".refresh_chat").children(".my_message_icon").addClass("my_refresh").addClass("layui-icon-loading").removeClass("layui-icon-face-cry");
        $(".refresh_chat").children("span").eq(1).text("点我加载更多历史消息");
        window.flag = 1;
        var oid = $(this).attr('oid');
        var uid =  $(".no_use_uid").text();

        var img_src = $(this).children('img').attr('src');
        var friend_name = $(this).children('.friend_name').text();
        var $li = $(".chat_list").find('li');
        var i = 0;
        if ($li.length > 0) {
            $li.each(function () {
                if ($(this).attr('oid') == oid) {
                    $(this).addClass('check_li').siblings().removeClass('check_li');
                    $(".main_send_name").children('a').text(friend_name).attr('title', friend_name).attr('oid', oid);
                    i++;
                }
            })
            if (i == 0) {
                $(".chat_list").children('li').removeClass('check_li')
            }
        }
        if (i == 0) {
            $(".box2").css('display', 'block');
            $(".main_send_name").children('a').text(friend_name).attr('title', friend_name).attr('oid', oid);
            var html = " <li oid='" + oid + "' class=\"layui-nav-item check_li chat_li\">\n" +
                "                <img src=\"" + img_src + "\">\n" +
                "                <span title='" + friend_name + "' class='friend_name'>" + friend_name + "</span>\n" +
                "                <span class=\"layui-icon layui-icon-close-fill small_close\"></span>\n" +
                "            </li>";
            $(".chat_list").append(html);
        }

        $.getJSON("/getChatContent","oid="+oid+"&uid="+uid,function (result) {
            $(".chat_content ul").empty();
            for(var i = 0;i < result.length;i++){
                var msg_type = "";
                if(result[i].uid == uid){
                    msg_type = "my_msg";
                }
                else{
                    msg_type="other_msg";
                }
                var html = "<li class=\""+msg_type+"\">\n" +
                    "                        <img src=\" "+result[i].headImg+"\">\n" +
                    "                        <div class=\"chat_detail\">\n" +
                    "                            <span>"+result[i].username+"</span><span>"+result[i].fdate+"</span>\n" +
                    "                            <p class=\"detail_msg\">\n" +
                    "                               "+result[i].message+"\n" +
                    "                            </p>\n" +
                    "                        </div>\n" +
                    "                    </li>"
                $(".chat_content ul").prepend(html);
            }
            $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
        })
        $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
        setHasRead(uid,oid);
    });
    $(".chat_list").delegate(".chat_li", 'click', function () {
        $(".refresh_chat").children(".my_message_icon").addClass("my_refresh").addClass("layui-icon-loading").removeClass("layui-icon-face-cry");
        $(".refresh_chat").children("span").eq(1).text("点我加载更多历史消息");
        window.flag = 1;
        $(".send_content").focus();
        var uid =  $(".no_use_uid").text();
        $(this).addClass('check_li').siblings().removeClass('check_li');
        var oid = $(this).attr('oid');
        var friend_name = $(this).children('.friend_name').text();
        $(".main_send_name").children('a').text(friend_name).attr('title', friend_name).attr('oid', oid);
        $.getJSON("/getChatContent","oid="+oid+"&uid="+uid,function (result) {
            $(".chat_content ul").empty();
            for(var i = 0;i < result.length;i++){
                var msg_type = "";
                if(result[i].uid == uid){
                    msg_type = "my_msg";
                }
                else{
                    msg_type="other_msg";
                }
                console.log(msg_type)
                var html = "<li class=\""+msg_type+"\">\n" +
                    "                        <img src=\" "+result[i].headImg+"\">\n" +
                    "                        <div class=\"chat_detail\">\n" +
                    "                            <span>"+result[i].username+"</span><span>"+result[i].fdate+"</span>\n" +
                    "                            <p class=\"detail_msg\">\n" +
                    "                               "+result[i].message+"\n" +
                    "                            </p>\n" +
                    "                        </div>\n" +
                    "                    </li>"
                $(".chat_content ul").prepend(html);
            }
            $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
        })
        $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
        setHasRead(uid,oid);
    })
    $(".chat_list").delegate('.small_close', 'click', function (event) {
        event.stopPropagation();
        $(".refresh_chat").children(".my_message_icon").addClass("my_refresh").addClass("layui-icon-loading").removeClass("layui-icon-face-cry");
        $(".refresh_chat").children("span").eq(1).text("点我加载更多历史消息");
        window.flag = 1;
        var $li = $(".chat_list li");
        if ($li.length <= 1) {
            $('.chat_list').empty();
            $(".box2").css('display', 'none');
        }
        else {
            var oid;
            var $that = $(this);
            if (!$that.parents('.chat_li ').hasClass('check_li')) {
                $that.parents('.chat_li ').remove();
            }
            else {
                var uid =  $(".no_use_uid").text();
                if ($that.parents('.chat_li').index() == $li.length) {
                    $that.parents('.chat_list').children('li').eq($li.length - 2).addClass('check_li').siblings().removeClass('check_li');
                    oid = $that.parents('.chat_list').children('li').eq($li.length - 2).attr('oid');
                    console.log($that.parents('.chat_list ').children('li').eq($li.length - 2)[0])
                    var friend_name = $that.parents('.chat_list ').children('li').eq($li.length - 2).find('.friend_name').text();
                    $(".main_send_name").children('a').text(friend_name).attr('title', friend_name).attr('oid', oid);
                    $that.parents('.chat_li').remove();
                }
                else {

                    console.log($that.parents('.chat_list').children('li').eq($li.length - 1)[0])
                    $that.parents('.chat_list').children('li').eq($li.length - 1).addClass('check_li').siblings().removeClass('check_li');
                    oid = $that.parents('.chat_list').children('li').eq($li.length - 1).attr('oid');
                    console.log(oid)
                    var friend_name = $that.parents('.chat_list ').children('li').eq($li.length - 1).find('.friend_name').text();
                    $(".main_send_name").children('a').text(friend_name).attr('title', friend_name).attr('oid', oid);
                    $that.parents('.chat_li').remove();
                }

            }
            $.getJSON("/getChatContent","oid="+oid+"&uid="+uid,function (result) {
                $(".chat_content ul").empty();
                for(var i = 0;i < result.length;i++){
                    var msg_type = "";
                    if(result[i].uid == uid){
                        msg_type = "my_msg";
                    }
                    else{
                        msg_type="other_msg";
                    }
                    console.log(msg_type)
                    var html = "<li class=\""+msg_type+"\">\n" +
                        "                        <img src=\" "+result[i].headImg+"\">\n" +
                        "                        <div class=\"chat_detail\">\n" +
                        "                            <span>"+result[i].username+"</span><span>"+result[i].fdate+"</span>\n" +
                        "                            <p class=\"detail_msg\">\n" +
                        "                               "+result[i].message+"\n" +
                        "                            </p>\n" +
                        "                        </div>\n" +
                        "                    </li>"
                    $(".chat_content ul").prepend(html);
                }
                $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
            })
            $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
        }
    })
    $(".close_all_frame").click(function () {
        layer.confirm('确定要关闭所有窗口吗?', {
            btn: ['全部关闭', '取消'] //按钮
        }, function () {
            $('.chat_list').empty();
            $(".box2").css('display', 'none');
            $(".refresh_chat").children(".my_message_icon").addClass("my_refresh").addClass("layui-icon-loading").removeClass("layui-icon-face-cry");
            $(".refresh_chat").children("span").eq(1).text("点我加载更多历史消息");
            window.flag = 1;
            layer.msg("关闭成功！", {
                time: 1000
            })
        }, function () {

        });
    })
    $(".close_one").click(function () {
        $(".chat_list .chat_li.check_li ").find('.small_close').click();
    })
    $(".chat_msg").delegate("li","click",function () {
        $(this).addClass('check_li ').siblings().removeClass('check_li ');
    })
    $(".chat_msg").delegate("li","dblclick",function () {
        window.flag = 1;
        $(this).find('.no_read').remove()
        var uid =  $(".no_use_uid").text();
        var oid = $(this).attr('oid');
        var img_src = $(this).find('img').attr('src');
        var friend_name = $(this).find('.sender_name').text();
        var $li = $(".chat_list").find('li');
        var i = 0;
        if ($li.length > 0) {
            $li.each(function () {
                if ($(this).attr('oid') == oid) {
                    $(this).addClass('check_li').siblings().removeClass('check_li');
                    $(".main_send_name").children('a').text(friend_name).attr('title', friend_name).attr('oid', oid);
                    i++;
                }
            })
            if (i == 0) {
                $(".chat_list").children('li').removeClass('check_li')
            }
        }
        if (i == 0) {
            $(".box2").css('display', 'block');
            $(".main_send_name").children('a').text(friend_name).attr('title', friend_name).attr('oid', oid);
            var html = " <li oid='" + oid + "' class=\"layui-nav-item check_li chat_li\">\n" +
                "                <img src=\"" + img_src + "\">\n" +
                "                <span title='" + friend_name + "' class='friend_name'>" + friend_name + "</span>\n" +
                "                <span class=\"layui-icon layui-icon-close-fill small_close\"></span>\n" +
                "            </li>";
            $(".chat_list").append(html);
        }
        $.getJSON("/getChatContent","oid="+oid+"&uid="+uid,function (result) {
            $(".chat_content ul").empty();
            for(var i = 0;i < result.length;i++){
                var msg_type = "";
                if(result[i].uid == uid){
                    msg_type = "my_msg";
                }
                else{
                    msg_type="other_msg";
                }
                console.log(msg_type)
                var html = "<li class=\""+msg_type+"\">\n" +
                    "                        <img src=\" "+result[i].headImg+"\">\n" +
                    "                        <div class=\"chat_detail\">\n" +
                    "                            <span>"+result[i].username+"</span><span>"+result[i].fdate+"</span>\n" +
                    "                            <p class=\"detail_msg\">\n" +
                    "                               "+result[i].message+"\n" +
                    "                            </p>\n" +
                    "                        </div>\n" +
                    "                    </li>"
                $(".chat_content ul").prepend(html);
            }
            $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
        })
        $('.chat_content').scrollTop( $('.chat_content')[0].scrollHeight+250);
        setHasRead(uid,oid);
    })
    $(".chat_content").scroll(function () {
        scrollTop =$(this).scrollTop();//滚动高度
        if(scrollTop == 0){ //到达底部100px时,加载新内容
           $(".refresh_chat").css("display","inline");
           $(".chat_view .view_right").css("height","96%");
        }else {
            $(".refresh_chat").css("display","none");
            $(".chat_view .view_right").css("height","100%");
        }
    })

    $(".refresh_chat").click(function () {
        window.flag++;
        var oid = $(".main_send_name").children("a").attr('oid');
        var uid =  $(".no_use_uid").text();

        $.getJSON("/getChatContent","oid="+oid+"&uid="+uid+"&counts="+window.flag,function (result) {
            if(result.length > 0){
                $(".refresh_chat").children(".my_message_icon").addClass("my_refresh").addClass("layui-icon-loading").removeClass("layui-icon-face-cry");
                $(".refresh_chat").children("span").eq(1).text("点我加载更多历史消息");
                for (var i = 0; i < result.length; i++) {
                    var msg_type = "";
                    if (result[i].uid == uid) {
                        msg_type = "my_msg";
                    }
                    else {
                        msg_type = "other_msg";
                    }
                    var html = "<li class=\"" + msg_type + "\">\n" +
                        "                        <img src=\" " + result[i].headImg + "\">\n" +
                        "                        <div class=\"chat_detail\">\n" +
                        "                            <span>" + result[i].username + "</span><span>" + result[i].fdate + "</span>\n" +
                        "                            <p class=\"detail_msg\">\n" +
                        "                               " + result[i].message + "\n" +
                        "                            </p>\n" +
                        "                        </div>\n" +
                        "                    </li>"
                    $(".chat_content ul").prepend(html);
                }
                $(".refresh_chat").css("display","none");
                $(".chat_view .view_right").css("height","100%");
            }
          else{
                $(".refresh_chat").children(".my_message_icon").removeClass("my_refresh").removeClass("layui-icon-loading").addClass("layui-icon-face-cry");
                $(".refresh_chat").children("span").eq(1).text("没有更多了~");
            }
        })
    })

    $("body").delegate(".add_friend_span","click",function () {
        var oid = $(this).attr('oid');
        $.getJSON("/addFriend","oid="+parseInt(oid),function (result) {
            layer.alert(result.msg);
        })
    })
    $(".wait_agree").click(function () {
        var html = "";
        if(window.wait_agree.length != 0){
            for(var i = 0;i < window.wait_agree.length;i++){
                html +="<div class=\"agree_friend_msg\"><img src=\""+window.wait_agree[i].headImg+"\"><div class=\"small_tip\"><span class=\"friend_name\">"+window.wait_agree[i].username+"</span><p title='"+window.wait_agree[i].myDesc+"' class=\"friend_desc\">"+window.wait_agree[i].myDesc+"</p><span oid=\""+window.wait_agree[i].id+"\" class=\"wait_friend_span\"><span class=\"layui-icon layui-icon-face-smile-b\"></span>同意</span><span class='refuse_agree'><span oid='"+window.wait_agree[i].id+"' class='layui-icon layui-icon-face-cry'></span>拒绝</span></div></div>";
            }
            layer.open({
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['420px', '240px'], //宽高
                content: html
            });
        }
        else{
            layer.alert("暂时没有需要同意的新朋友！")
        }
    })

    $("body").delegate(".wait_friend_span",'click',function () {
        var oid = $(this).attr('oid');
        $.getJSON("/getMyGroup",function (result) {
            var $li_list =""
            if(result.length != 0){
                $li_list =  "  <select><option class='wei'>请选择组别</option>\n";
                for(var i = 0;i< result.length;i++){
                    $li_list+="<option class='wei' value='"+result[i].id+"'>"+result[i].gname+"</option>\n";
                }
                $li_list += "  </select>\n" +
                    "</div><button class=\"layui-btn layui-btn-danger add_group1  add_tobtn\" oid='"+oid+"'>加入</button>";
            }
            var html = "<h3 class='frame_title'>请选择添加到哪个分组?</h3><div class=\"layui-form-item\">\n" +
                "    <label class=\"layui-form-label\"></label>\n" +
                "    <div class=\"layui-input-inline\">\n" +
                "      <input class='new_group' type=\"text\" name=\"groupName\" required lay-verify=\"required\" placeholder=\"新建组名\" autocomplete=\"off\" class=\"layui-input\">\n" +
                "    </div>\n" +
                "    <button class=\"add_group1 layui-btn add_group\" oid='"+oid+"'>新增并加入</button>\n" +
                "  </div><div class=\"select select_area\">\n"

            layer.open({
                type: 1,
                area: ['430px', '250px'], //宽高
                content: html+$li_list
            });
        })
    })

    $("body").delegate(".refuse_agree",'click',function () {
        var oid = $(".refuse_agree").find("span").attr("oid");
        $.getJSON("/refuseAdd","oid="+parseInt(oid),function () {
           window.location.reload();
        })
    })
    $("body").delegate("select",'click',function () {
        var val = $("option:selected").val();
       $(".add_tobtn").attr("select_val",val);
    })
    $("body").delegate(".add_group1","click",function () {
        var gname="";
        var flag = 0;
        var select_val = ""
        if($(this).hasClass('add_group')){
            gname = $(".new_group").val();
            if(gname.trim().length == 0){
                layer.msg("请先输入分组名称！");
                flag = 1;
            }
        }
        else{
            if(flag == 0){
                select_val = $(this).attr("select_val");
                if(select_val == "" || select_val == undefined || select_val == null){
                    layer.msg("请选择下拉框的组别！");
                    flag = 1;
                }
            }
        }
        var oid=$(this).attr("oid");
        if(flag == 0){
            $.post("/add_group",{oid:parseInt(oid),gname:gname,select_val:select_val},function (result) {
                if(result.status == 200){
                    window.location.reload();
                }
            })
        }
    })

    $(".qm_text").click(function () {
        var content = $(this).text();
        $(this).after("<input type='text' class='qm_text my_qm_text' value='"+content+"'>");
        $(this).remove();

    })

})

function chat_change() {
    var uid = $(".no_use_uid").text();
    $.getJSON("/getChatRecord","uid=" + uid,function(result){
        if (result.length > 0) {
            $(".chat_msg").empty()
        }
        else {
            $(".show_null").children("i").addClass('layui-icon-face-cry').removeClass("layui-icon-loading")
            $(".show_null").children("p").text("这里什么都没有")

        }

        var html = "";
        for (var i = 0; i < result.length; i++) {
            var add_str= "";
            if(result[i].isRead == 1 && result[i].mid != uid){
                add_str="<span class='no_read'>未读消息</span>";
            }
            html += "  <li oid='"+result[i].otherUser.id+"'>\n" +
                "                            <a href=\"javascript:;\">\n" +
                "                                <img src=\"" + result[i].otherUser.headImg + "\">\n" +
                "                                <div class=\"sender_area\">\n" +
                "                                    <span title='"+result[i].otherUser.username+"' class=\"sender_name\">" + result[i].otherUser.username + "</span>"+add_str+"\n" +
                "                                    <p class=\"sender_msg\" title=\"" + result[i].lastText + "\">" + result[i].lastText + "</p>\n" +
                "                                    <span class=\"sender_time\">" + result[i].lastDate + "</span>\n" +
                "                                </div>\n" +
                "                            </a>\n" +
                "                        </li>"
        }
        $(".chat_msg").append(html);
    })
}
function wait_agree() {
    $.getJSON("/wait_agree",function (result) {
        var length = result.length;
        window.wait_agree = result;
        $(".wait_agree").after("<span style='color: #00FF00'>("+length+")</span>");
    })
}
function setHasRead(uid,oid) {
    console.log(uid)
    console.log(oid)

    $.getJSON("/setHasRead","uid="+oid+"&oid="+uid,function () {
        $(".chat_msg li[oid="+uid+"]").find('.no_read').remove();

    })
}