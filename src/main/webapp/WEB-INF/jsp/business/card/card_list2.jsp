<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>

<!-- 下拉框 -->
<link rel="stylesheet" href="/static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="/static/ace/css/datepicker.css" />
<link rel="stylesheet" href="/static/jquery.dad/jquery.dad.css" />
<script src="/static/js/common/common.js"></script>
<style>
	.wrap { width: 1000px; height: 220px; margin: 0 auto; font-family: arial,SimSun; font-size: 0; overflow: hidden;}
	.jq22 { float: right; width: 400px;}
	.jq22 .item { display: inline-block; width: 180px; height: 100px; margin: 0 20px 20px 0;}
	.item1 { background-color: #1faeff;}
	.item2 { background-color: #ff2e12;}
	.item3 { background-color: #00c13f;}
	.item4 { background-color: #e1b700;}
	.jq22 span { display: block; height: 100px; line-height: 100px; font-size: 2rem; text-align: center; color: #fff;}
	
	.btns { width: 1000px; margin-left: auto; margin-right: auto;}
	
	.dropzone {float:left; height: 220px;width:400px; margin: 0 0 0 10px; background-color: #ccc;}
	.dropzone span { display: inline-block; width: 180px; height: 100px; margin: 10px 0 0 10px; line-height: 50px; font-size: 2rem; text-align: center; color: #fff; background-color: #7200ac;}

</style>


</head>
<body>
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">	
		<div class="dropzone"></div>	
		<div class="dropzone"></div>
		<div class="dropzone"></div>

		<div class="jq22">
			<div class="item item1"><span>1</span></div>
			<div class="item item2"><span>2</span></div>
			<div class="item item3"><span>3</span></div>
			<div class="item item4"><span>4</span></div>
		</div>
		
	</div>

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp"%>
<!-- 删除时确认窗口 -->
<script src="/static/ace/js/bootbox.js"></script>
<!-- ace scripts -->
<script src="/static/ace/js/ace/ace.js"></script>
<!-- 下拉框 -->
<script src="/static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="/static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
<!-- <script type="text/javascript" src="/static/jquery.dad/jquery.dad.js?ver=3"></script> -->
<script>

/*!
 * jquery.dad.js v1 (http://konsolestudio.com/dad)
 * Author William Lima
 */
(function($) {
    "use strict";
    function O_dad() {
        var self = this;
        this.x = 0;
        this.y = 0;
        this.target = false;
        this.clone = false;
        this.placeholder = false;
        this.cloneoffset = {
            x: 0,
            y: 0
        };
        this.move = function(e) {
            self.x = e.pageX;
            self.y = e.pageY;
            if (self.clone != false && self.target != false) {
                self.clone.css({
                    top: self.y - self.cloneoffset.y,
                    left: self.x - self.cloneoffset.x
                })
            } else {}
        };
        $(window).on('mousemove', function(e) {
            self.move(e)
        })
    }
    $.prototype.dad = function(opts) {
        var me,
            defaults,
            options;
        me = this;
        defaults = {
            target: '>div',
            draggable: false,
            placeholder: 'drop here',
            callback: false,
            containerClass: 'dad-container',
            childrenClass: 'dads-children',
            cloneClass: 'dads-children-clone',
            active: true
        };
        options = $.extend({}, defaults, opts);
        $(this).each(function() {
            var mouse,
                target,
                dragClass,
                active,
                callback,
                placeholder,
                daddy,
                childrenClass,
                jQclass,
                cloneClass;
            mouse = new O_dad();
            active = options.active;
            daddy = $(this);
            if (!daddy.hasClass('dad-active') && active == true)
                daddy.addClass('dad-active');
            childrenClass = options.childrenClass;
            cloneClass = options.cloneClass;
            jQclass = '.' + childrenClass;
            daddy.addClass(options.containerClass);
            target = daddy.find(options.target);
            placeholder = options.placeholder;
            callback = options.callback;
            dragClass = 'dad-draggable-area';
            me.addDropzone = function(selector, func) {
                $(selector).on('mouseenter', function(e) {
                    if (mouse.target != false) {
                    	//去掉虚线占位符
                        mouse.placeholder.css({
                            display: 'none'
                        });
                    	//去掉被拖动控件的占位符,
                        /* mouse.target.css({
                            display: 'none'
                        });  */
                        $(this).addClass('active');
                        /* $(e.target).css({
                        	background:'black'
                        }); */
                    }
                }).on('mouseup', function(e) {
                    if (mouse.target != false) {
                        mouse.placeholder.css({
                            display: 'block'
                        });
                        mouse.target.css({
                            display: 'block'
                        });
                        func(mouse.target);
                        dad_end()
                    }
                    $(this).removeClass('active')
                }).on('mouseleave', function(e) {
                    if (mouse.target != false) {
                        mouse.placeholder.css({
                            display: 'block'
                        });
                        mouse.target.css({
                            display: 'block'
                        })
                    }
                    $(this).removeClass('active')
                })
            };
            me.getPosition = function() {
                var positionArray = [];
                $(this).find(jQclass).each(function() {
                    positionArray[$(this).attr('data-dad-id')] = parseInt($(this).attr('data-dad-position'))
                });
                return positionArray
            };
            me.activate = function() {
                active = true;
                if (!daddy.hasClass('dad-active')) {
                    daddy.addClass('dad-active')
                }
                return me
            };
            me.deactivate = function() {
                active = false;
                daddy.removeClass('dad-active');
                return me
            };
            $(document).on('mouseup', function() {
                dad_end()
            });
            var order = 1;
            target.addClass(childrenClass).each(function() {
                if ($(this).data('dad-id') == undefined) {
                    $(this).attr('data-dad-id', order)
                }
                $(this).attr('data-dad-position', order);
                order++
            });
            function update_position(e) {
                var order = 1;
                e.find(jQclass).each(function() {
                    $(this).attr('data-dad-position', order);
                    order++
                })
            }
            function dad_end() {
                if (mouse.target != false && mouse.clone != false) {
                    if (callback != false) {
                        callback(mouse.target)
                    }
                    var appear = mouse.target;
                    var desapear = mouse.clone;
                    var holder = mouse.placeholder;
                    var bLeft = 0;
                    Math.floor(parseFloat(daddy.css('border-left-width')));
                    var bTop = 0;
                    Math.floor(parseFloat(daddy.css('border-top-width')));
                    if ($.contains(daddy[0], mouse.target[0])) {
                        mouse.clone.animate({
                            top: mouse.target.offset().top - daddy.offset().top - bTop,
                            left: mouse.target.offset().left - daddy.offset().left - bLeft
                        }, 300, function() {
                            appear.css({
                                visibility: 'visible'
                            }).removeClass('active');
                            desapear.remove()
                        })
                    } else {
                        mouse.clone.fadeOut(300, function() {
                            desapear.remove()
                        })
                    }
                    holder.remove();
                    mouse.clone = false;
                    mouse.placeholder = false;
                    mouse.target = false;
                    update_position(daddy)
                }
                $("html,body").removeClass('dad-noSelect')
            }
            function dad_update(obj) {
                if (mouse.target != false && mouse.clone != false) {
                    var newplace,
                        origin;
                    origin = $('<span style="display:none"></span>');
                    newplace = $('<span style="display:none"></span>');
                    if (obj.prevAll().hasClass('active')) {
                        obj.after(newplace)
                    } else {
                        obj.before(newplace)
                    }
                    mouse.target.before(origin);
                    newplace.before(mouse.target);
                    mouse.placeholder.css({
                        top: mouse.target.offset().top - daddy.offset().top,
                        left: mouse.target.offset().left - daddy.offset().left,
                        width: mouse.target.outerWidth() - 10,
                        height: mouse.target.outerHeight() - 10
                    });
                    origin.remove();
                    newplace.remove()
                }
            }
            var jq = (options.draggable != false) ? options.draggable : jQclass;
            daddy.find(jq).addClass(dragClass);
            daddy.find(jq).on('mousedown touchstart', function(e) {
                if (mouse.target == false && e.which == 1 && active == true) {
                    if (options.draggable != false) {
                        mouse.target = daddy.find(jQclass).has(this)
                    } else {
                        mouse.target = $(this)
                    }
                    mouse.clone = mouse.target.clone();
                    mouse.target.css({
                        visibility: 'hidden'
                    }).addClass('active');
                    mouse.clone.addClass(cloneClass);
                    daddy.append(mouse.clone);
                    mouse.placeholder = $('<div></div>');
                    mouse.placeholder.addClass('dads-children-placeholder');
                    mouse.placeholder.css({
                        top: mouse.target.offset().top - daddy.offset().top,
                        left: mouse.target.offset().left - daddy.offset().left,
                        width: mouse.target.outerWidth() - 10,
                        height: mouse.target.outerHeight() - 10,
                        lineHeight: mouse.target.height() - 18 + 'px',
                        textAlign: 'center'
                    }).text(placeholder);
                    daddy.append(mouse.placeholder);
                    var difx,
                        dify;
                    var bLeft = Math.floor(parseFloat(daddy.css('border-left-width')));
                    var bTop = Math.floor(parseFloat(daddy.css('border-top-width')));
                    difx = mouse.x - mouse.target.offset().left + daddy.offset().left + bLeft;
                    dify = mouse.y - mouse.target.offset().top + daddy.offset().top + bTop;
                    mouse.cloneoffset.x = difx;
                    mouse.cloneoffset.y = dify;
                    mouse.clone.removeClass(childrenClass).css({
                        position: 'absolute',
                        top: mouse.y - mouse.cloneoffset.y,
                        left: mouse.x - mouse.cloneoffset.x
                    });
                    $("html,body").addClass('dad-noSelect')
                }
            });
            $(jQclass).on('mouseenter', function() {
                dad_update($(this))
            })
        });
        return this
    }
}(jQuery));


</script>
<script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}//关闭加载状态
		
		
		
		$(function(){ 
			//工具列别
			var currentTool = $('.jq22').dad({'placeholder':'控件'});
			//可选的目标区域
			var availableDropZones = $('.dropzone');
			//实际的目标区域
			

			currentTool.addDropzone(availableDropZones, function(e){
				//把拖拽的工具显示到目标区域
				var span = e.find('span');	
				$('.dropzone.active').append(span.clone(true));
				
				//e.find('span').appendTo(target);
				//删除被拖动的工具
				//e.remove();
			});
		});


</script>


</body>
</html>