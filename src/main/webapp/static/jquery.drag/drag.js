/**
 * 
 */
(function($){
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
	$.prototype.drag = funciton(opts){
			var me,defaults,options;
		    me = this;
		    defaults = {
		        target: '.card-container .item', //默认的可以放置的目标class
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
	            
		    });
		    return this;
		}
})(jQuery);