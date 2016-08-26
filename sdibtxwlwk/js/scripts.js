/*
* Created by		: Demente Design
* Email				: contact@demente-design.com
* URL				: http://demente-design.com
*/

/*-----------------------------------------------------------------------------------------------------------------
// Enhance Menu 
-----------------------------------------------------------------------------------------------------------------*/

(function(jQuery){

	jQuery.fn.enhanceMenu = function(options){
		settings = jQuery.extend({
			effect:'slide',		// slide or fade
			duration: 800,		// time for the animation to complete
			delayIn: 150,		// delay to show the sub menu on hover
			delayOut: 0,		// delay to hide the sub menu on blur
			siteWidth: 960		// with of the site
		},options );

		return this.each(function(){
		
			//get the menu
			var target = jQuery(this),
				menu = target.first('ul'),
				li = menu.find('li').children('ul').hide().end(),
				topLi = menu.find('> li'),
				subWidth = menu.find('> li').children('ul').outerWidth(),
				menuPosition = target.position().left;
				
			// See if there's enough room for a sub menu
			topLi.each(function(){
			
				var item = jQuery(this),
					position = item.position().left,
					children = item.find('ul').slice(1);
					
				if( 2 * subWidth + position + menuPosition > settings.siteWidth ){
				
					var paddingLeft = children.css('padding-left'),
						paddingRight = children.css('padding-right');
					
					children.css({
						'left': 'auto',
						'right': '100%',
						'padding-left': paddingRight,
						'padding-right': paddingLeft
					});
				}
				
			});
			
			//add the parent class
			li.has('ul').addClass('parent');
			
			//remove titles
			jQuery("a", menu).removeAttr('title');
			
			if(settings.effect == 'fade')
			{
				li.hover(function() {
					// mouseover
					jQuery(this).find('> ul').stop(true, true).css('z-index',11).delay(settings.delayIn).fadeIn(settings.duration);
				}, function() {
					// mouseout
					jQuery(this).find('> ul').stop(true, true).css('z-index',10).delay(settings.delayOut).fadeOut(settings.duration); 		
				});
			}	
			
			else if(settings.effect == 'slide')
			{

				li.hover(function() {
					
					// mouseover
					var item = jQuery(this);
					
					item.find('> ul').css('z-index',11).delay(settings.delayIn).slideDown(settings.duration,
					function()
					{
						jQuery(this).css({
							'overflow':'visible', 
							'display':'block' // Since jQuery 1.4.3 the CSS must be overwritten
						});
					}); 
				}, function() {
					
					// mouseout	
					var item = jQuery(this);
					
					item.find('> ul').stop(true, false).css('z-index',10).delay(settings.delayOut).slideUp(settings.duration, 
					function()
					{
						jQuery(this).css({
							'height':'auto', // This fixes the bug where the menu won't show completely after mouseout event
							'padding':'auto'
						});
					}); 		
				});
			}
			
			else{

				li.hover(function() {
					// mouseover
					var item = jQuery(this);
					item.find('> ul').show(); 
					
				}, function() {
					// mouseout	
					var item = jQuery(this);
					item.find('> ul').hide(); 		
				});
			}
			// Else the CSS will take care
		});
	};
})(jQuery);

/*-----------------------------------------------------------------------------------------------------------------
// Captions 
-----------------------------------------------------------------------------------------------------------------*/

var captions = function(){

	var images = jQuery('#content-block').find('img.caption');
	
	images.each(function(i){

		var img = images.eq(i),	
			imgWidth = img.attr('width') + 10,
			parentLink = img.parent('a'),
			caption = img.attr('title'),
			inlineStyle = img.attr('style');

		// if there's no title attribute get the alt attribute
		caption.length == 0? caption = img.attr('alt') : null;

		cssFloat = img.css('float');
		alignFloat = img.attr('align');
		
		// Determine the float if any
		if ( cssFloat == 'left' || alignFloat == 'left' ) var imgFloat = 'left';
		else if ( cssFloat == 'right' || alignFloat == 'right' ) var imgFloat = 'right';
		else var imgFloat = 'none', 
				 imgWidth = '100%';

				
		// If the image is a link, wrap everything
		if( parentLink.length ) parentLink.wrap('<div class="caption-wrap" />');
		
		else img.wrap('<div class="caption-wrap" />');
		
		// Style the wrapper adn add the caption text
		img.parents('.caption-wrap')
		.addClass('caption-wrap-' + imgFloat)
		.append('<span class="caption-text">' + caption + '</span>');
		
		// IE needs a width
		img.parents('.caption-wrap').css({'width': imgWidth});
	});
};

/*-----------------------------------------------------------------------------------------------------------------
// Style the tables
-----------------------------------------------------------------------------------------------------------------*/

var zebratables = function(){
	jQuery('table.zebratable').find('tr:nth-child(odd)').addClass('tr-odd');
};

/*-----------------------------------------------------------------------------------------------------------------
// Show/hide the text of the inputs
-----------------------------------------------------------------------------------------------------------------*/

var inputValues = function(){

	var form = jQuery('form'),
		inputs = jQuery('input.hidevalue, textarea.hidevalue', form);
		
	inputs.each(function()
	{
		var input = jQuery(this),
			value = input.val();
		
		//If it's a password field a trick needs to be done
		if( input.attr('type') == 'password' ){
			input.after('<input class="password-mask" type="text" value="' + value + '" />');
			
			var mask = jQuery('input.password-mask', form);
			
			//Hide the actual password input to show a text
			input.hide();
			mask.show();
			
			//Exchange the inputs and focus the new one
			mask.focus(function(){
				mask.hide();
				input.show();
				input.focus();
			});
		}
		
		//If the default text is there, remove it
		input.focus(function(){
			if( jQuery(this).val() == value ){
				jQuery(this).val('');
			}
		});
		
		//If the field was left empty, show the text
		input.blur(function(){
			if( jQuery(this).val() == '' ){
				//If no password was enteres, show the fake input again
				if( input.attr('type') == 'password' ){
					input.hide();
					mask.show();
				}
				else jQuery(this).val(value);
			}
		});
		
	});
};

/*-----------------------------------------------------------------------------------------------------------------
// Pullquote
-----------------------------------------------------------------------------------------------------------------*/

var pullquote = function()
{
	var quotes = jQuery('#content-block').find('.article-body .pullquote'),
		parents = quotes.parent(),
		n = quotes.length;
		
		for( var i=0; i<n; i++){
			var quote = quotes[i],
				parent = parents[i],
				text = quote.innerHTML,
				box = document.createElement('quote');
				
			box.className = 'pullquote-box';
			box.innerHTML = text;

			parent.parentNode.insertBefore(box ,parent);
		}
};

/*-----------------------------------------------------------------------------------------------------------------
// Image preloader
-----------------------------------------------------------------------------------------------------------------*/

jQuery.fn.preloader = function(options){
	
	var defaults = {
		             delay: 200,
					 preload_parent: "a",
					 check_timer: 300,
					 ondone: function(){ },
					 oneachload: function(image){  },
					 fadein: 500 ,
					 widthLimit: 32
					};
	
	var options = jQuery.extend(defaults, options),
		root = jQuery(this), 
		images = root.find("img").filter(function(){
			if( jQuery(this).width() > options.widthLimit ) return true;
			else return false;
		}).css({"visibility":"hidden",opacity:0}),  
		timer,  
		counter = 0, 
		i=0, 
		checkFlag = [], 
		delaySum = options.delay,
	 
	init = function(){
		
		timer = setInterval(function(){
			
			if(counter>=checkFlag.length){
				clearInterval(timer);
				options.ondone();
				return;
			}
		
			for(i=0;i<images.length;i++){
			
				if(images[i].complete == true){
				
					if(checkFlag[i] == false){
						checkFlag[i] = true;
						options.oneachload(images[i]);
						counter++;
						
						delaySum = delaySum + options.delay;
					}
					
					jQuery(images[i]).css("visibility","visible").delay(delaySum).animate({opacity:1},options.fadein,
					function(){ jQuery(this).parent().removeClass("preloader");   });
				}
			}
		},options.check_timer) 
	};
	
	images.each(function(){
		
		if(jQuery(this).parent(options.preload_parent).length==0)
		jQuery(this).wrap("<a class='preloader' />");
		else
		jQuery(this).parent().addClass("preloader");
		
		checkFlag[i++] = false;
		
		}); 
	images = jQuery.makeArray(images); 

	timer = setInterval(function(){
		clearInterval(timer);
		init();
		return;
	},100);
}

/*-----------------------------------------------------------------------------------------------------------------
// Table hover link effect
-----------------------------------------------------------------------------------------------------------------*/

var hoverTables = function(){
	var tables = jQuery('table.hovertable'),
		rows = tables.find('tbody tr');
	
	rows.click(function(){
		// Get the first link
		var link = jQuery(this).find('a').eq(0).attr('href');
		if(link){
			location.href=link;
		}
	});
};

/*-----------------------------------------------------------------------------------------------------------------
// Fades
-----------------------------------------------------------------------------------------------------------------*/

var fades = function(){
	var parents = jQuery('.fade'),
		links = parents.find('a');
	
	links.css('opacity', .6);
	links.hover(function(){
		jQuery(this).stop().fadeTo(200, 1);
	},function(){
		jQuery(this).stop().fadeTo(200, .8);
	});
};