$(function () {
    var options =
        {
            thumbBox: '.thumbBox',
            spinner: '.spinner',
            imgSrc: ''
        }
    var cropper = $('.imageBox').cropbox(options);
    $('#upload-file').on('change', function(){
        var reader = new FileReader();
        reader.onload = function(e) {
            options.imgSrc = e.target.result;
            cropper = $('.imageBox').cropbox(options);
        }
        reader.readAsDataURL(this.files[0]);
        this.files = [];
    })
    $('#btnCrop').on('click', function(){
        var img = cropper.getDataURL();
        $('.cropped').html('');
        $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:64px;margin-top:4px;border-radius:64px;box-shadow:0px 0px 12px #7E7E7E;" ><p>64px*64px</p>');
        $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:128px;margin-top:4px;border-radius:128px;box-shadow:0px 0px 12px #7E7E7E;"><p>128px*128px</p>');
        $('.cropped').append('<img src="'+img+'" align="absmiddle" style="width:180px;margin-top:4px;border-radius:180px;box-shadow:0px 0px 12px #7E7E7E;"><p>180px*180px</p>');
    })
    $('#btnZoomIn').on('click', function(){
        cropper.zoomIn();
    })
    $('#btnZoomOut').on('click', function(){
        cropper.zoomOut();
    })
    $(".head_img").click(function () {
        $(".gray_div").css("display","block");
    })
    $(".use_btn_close").click(function () {
        $(".gray_div").css("display","none");
    })
    $(".use_btn_use").click(function () {
       var img_base64 = $(".cropped img").eq(1).attr("src");
       if(img_base64 == undefined){
           layer.msg("请选择照片且裁切后再确认使用！！！")
       }
       else{
           var index = layer.load(1, {
               shade: [0.1,'#a2a2a2cc'] //0.1透明度的白色背景
           });
           $.post("/changeHeadImg",{imgSrc:img_base64},function (result) {
               if(result.data=""){
                   layer.msg("修改头像失败！请稍后再试！");
               }
               else{
                  window.location.reload();
               }
           })
       }

    })
})