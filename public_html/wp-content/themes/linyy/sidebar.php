<div id="ace-sidebar">
		<button id="ace-sidebar-close" class="btn btn-icon btn-light btn-shade">
			<span class="ace-icon ace-icon-close"></span>
		</button>

		<div id="ace-sidebar-inner">
			<nav id="ace-main-nav-sm" class="visible-xs visible-sm text-center">
				<ul class="clear-list">
					<li <?php if (is_home()) { echo 'class="current"';} ?>><a title="<?php bloginfo('name'); ?>"  href="<?php echo get_option('home'); ?>/">Home</a></li>
					<?php wp_list_pages('depth=1&title_li=0&sort_column=menu_order'); ?>
										<li><a href="http://wpa.qq.com/msgrd?v=3&uin=1260157543&site=qq&menu=yes" target="_blank" title="林洋洋博客 | QQ联系我">联系我</a></li>
				</ul>
			</nav><!-- #ace-main-nav-sm -->
		<article class="ace-card bg-primary">
				<div class="ace-card-inner text-center">
<script type="text/javascript">
 var today = new Date(); //新建一个Date对象
  var date = today.getDate();//查询当月日期
  var day = today.getDay();//查询当前星期几
  var week="";                               
  if (day==0) week='<a href="<?php echo get_option('home'); ?>/"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo7.png" width="195" height="195" alt="logo"></a>';
  if (day==1) week='<a href="<?php echo get_option('home'); ?>/"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo1.png" width="195" height="195" alt="logo"></a>';
  if (day==2) week='<a href="<?php echo get_option('home'); ?>/"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo2.png" width="195" height="195" alt="logo"></a>';
  if (day==3) week='<a href="<?php echo get_option('home'); ?>/"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo3.png" width="195" height="195" alt="logo"></a>';
  if (day==4) week='<a href="<?php echo get_option('home'); ?>/"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo4.png" width="195" height="195" alt="logo"></a>';
  if (day==5) week='<a href="<?php echo get_option('home'); ?>/"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo5.png" width="195" height="195" alt="logo"></a>';
  if (day==6) week='<a href="<?php echo get_option('home'); ?>/"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo6.png" width="195" height="195" alt="logo"></a>';
  document.write(week);
</script>
				<h1><a href="?p=1138"><?php bloginfo('name'); ?></a></h1>
				<p class="text-muted"><?php echo floor((time()-strtotime("2017-04-01"))/86400); ?> 天 | <?php bloginfo('description'); ?></p>
					<ul class="ace-social clear-list">
		<li><a href="https://github.com/a1260157543" target="_blank" title="github"><span class="ace-icon ace-icon-github"></span></a></li>							
		<li><a href="http://wpa.qq.com/msgrd?v=3&uin=1260157543&site=qq&menu=yes" target="_blank" title="QQ联系我"><span class="ace-icon ace-icon-qq"></span></a></li>
		<li><a href="<?php bloginfo('rss2_url'); ?>" title="林洋洋博客 | RSS订阅" target="_blank"><span class="ace-icon ace-icon-rss"></span></a></li>
		<li><a href="?p=1395" title="下载简易测试APK"><span class="ace-icon ace-icon-android"></span></a></li>
		<li><a href="http://weibo.com/lyy250250" target="_blank" title="访问新浪微博"><span class="ace-icon ace-icon-weibo"></span></a></li>
					</ul>
				</div>				
			</article><!-- #ace-card -->
			<aside class="widget-area">
				<section class="widget widget_search">
					<form class="search-form" id="search" action="<?php bloginfo('url'); ?>/">
						<label>
							<span class="screen-reader-text">Search for:</span>
							<input type="search" class="search-field" placeholder="站内搜索(文章)" value="" id="s" name="s">
						</label>
						<button type="submit" class="search-submit" onClick="if(document.forms['search'].searchinput.value=='- Search -')document.forms['search'].searchinput.value=”;” alt=”Search”  >
							<span class="screen-reader-text"></span>
							<span class="ace-icon ace-icon-search"></span>
						</button>
					</form>
				</section>

				<section id="tag_cloud-2" class="widget widget_tag_cloud">
				<?php if ( !function_exists('dynamic_sidebar')
                            || !dynamic_sidebar('Third_sidebar') ) : ?>
					<h2 class="widget-title">标签云</h2>
					<div class="tagcloud">
						<?php wp_tag_cloud('smallest=12&largest=17&unit=px&number=20&orderby=count&order=DESC');?>
					</div>
				<?php endif; ?>
				</section>

	<section id="recent-posts-3" class="widget widget_recent_entries">
				<?php if ( !function_exists('dynamic_sidebar')
                            || !dynamic_sidebar('Second_sidebar') ) : ?> 
					<h4 class="widget-title">最新文章</h4>
					<ul>
						<?php
							$posts = get_posts('numberposts=6&orderby=post_date');
							foreach($posts as $post) {
								setup_postdata($post);
								echo '<li><a href="' . get_permalink() . '">' . get_the_title() . '</a></li>';
							}
							$post = $posts[0];
						?>
					</ul>
				<?php endif; ?>
				</section>



<!--<center><iframe name="weather_inc" src="http://i.tianqi.com/index.php?c=code&id=8"  width="225" height="80" frameborder="0" marginwidth="0" marginheight="0" scrolling="no"></iframe></center>-->
<!--<center><object type="application/x-shockwave-flash" style="outline:none;" data="http://cdn.abowman.com/widgets/fish/fish.swf?up_fishName=Fish&up_fishColor4=F45540&up_fishColor2=F45540&up_fishColor8=F45540&up_foodColor=FCB347&up_fishColor6=F45540&up_fishColor5=F45540&up_fishColor3=F45540&up_numFish=5&up_fishColor1=F45540&up_backgroundColor=FFFFFF&up_backgroundImage=http://&up_fishColor7=F45540&up_fishColor10=F45540&up_fishColor9=F45540&" width="300" height="200"><param name="movie" value="http://cdn.abowman.com/widgets/fish/fish.swf?up_fishName=Fish&up_fishColor4=F45540&up_fishColor2=F45540&up_fishColor8=F45540&up_foodColor=FCB347&up_fishColor6=F45540&up_fishColor5=F45540&up_fishColor3=F45540&up_numFish=5&up_fishColor1=F45540&up_backgroundColor=FFFFFF&up_backgroundImage=http://&up_fishColor7=F45540&up_fishColor10=F45540&up_fishColor9=F45540&"></param><param name="AllowScriptAccess" value="always"></param><param name="wmode" value="opaque"></param><param name="scale" value="noscale"/><param name="salign" value="tl"/></object></center>-->

			</aside>
		</div><!-- #ace-sidebar-inner -->
	</div><!-- #ace-sidebar -->