$(function () {
    dragPanelMove(".top1",".box");
    dragPanelMove(".top_common1",".box2");
    function dragPanelMove(downDiv,moveDiv){
        $(downDiv).mousedown(function (e) {
            var isMove = true;
            var div_x = e.pageX - $(moveDiv).offset().left;
            var div_y = e.pageY - $(moveDiv).offset().top;
            $(document).mousemove(function (e) {
                if (isMove) {
                    var obj = $(moveDiv);
                    obj.css({"left":e.pageX - div_x, "top":e.pageY - div_y});
                }
            }).mouseup(
                function () {
                    isMove = false;
                });
        });

    }

});
