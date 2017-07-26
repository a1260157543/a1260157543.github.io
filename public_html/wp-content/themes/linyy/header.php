<!DOCTYPE html>
<html lang="en" class="ace ace-card-on ace-tab-nav-on ace-main-nav-on ace-sidebar-on" data-theme-color="#c0e3e7" xmlns:wb="http://open.weibo.com/wb">
<head>
    <meta charset="utf-8">
    <meta name="description" content="喜爱阅读！读最经典最优秀最有趣的文章，让读书变得更有乐趣！给你一个阅读的生活！了解我的生活-做最优秀的个人博客网站。">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="baidu-site-verification" content="QikllgNqV1" />
    <meta property="wb:webmaster" content="5417cb2e59502260" />
    <title><?php if ( is_home() ) {
        bloginfo('name');echo " | "; bloginfo('description');
    } elseif ( is_category() ) {
        bloginfo('name');echo " | ";single_cat_title(); echo " - 归档";
    } elseif (is_single() || is_page() ) {
        bloginfo('name');echo " | ";single_post_title();
    } elseif (is_search() ) {
        bloginfo('name');echo " | 搜索结果"; 
    } elseif (is_archive() ) {
        bloginfo('name');echo " | ";single_cat_title(); echo " - 标签"; 
    } elseif (is_404() ) {
        bloginfo('name');echo ' | 页面未找到!';
    } else {
        wp_title('',true);
    } ?></title>
    <link rel="apple-touch-icon" href="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo.ico">
    <link rel="shortcut icon" href="<?php bloginfo('template_url'); ?>/img/uploads/avatar/lyy.ico">
    <link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/css/style.css">
    <link href="https://fonts.googleapis.com/css?family=Quicksand:400,700" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Pacifico" rel="stylesheet">
    <!-- Icon Fonts -->
    <link href="<?php bloginfo('template_url'); ?>/fonts/icomoon/style.css" rel="stylesheet">
	<link rel="alternate" type="application/rss+xml" title="RSS 2.0 - 所有文章" href="<?php echo get_bloginfo('rss2_url'); ?>" />
	<link rel="alternate" type="application/rss+xml" title="RSS 2.0 - 所有评论" href="<?php bloginfo('comments_rss2_url'); ?>" />
    <link href="<?php bloginfo('template_url'); ?>/js/plugins/highlight/solarized-light.css" rel="stylesheet">
    <link href="<?php bloginfo('template_url'); ?>/style.css" rel="stylesheet">
	<link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/css/one3.css">
	<link rel="pingback" href="<?php bloginfo('pingback_url'); ?>" />


	<script>
		if(/Android|Windows Phone|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent) && !/MI PAD/i.test(navigator.userAgent)){
			
		}else{
			
		}
	</script>
	<link rel="stylesheet" href="<?php bloginfo('template_url'); ?>/Public/css/common.css"/>
    <!-- Modernizer -->
    <script type="text/javascript" src="<?php bloginfo('template_url'); ?>/js/vendor/modernizr-3.3.1.min.js"></script>
<script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js" type="text/javascript" charset="utf-8"></script>
<script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=3855124295" type="text/javascript" charset="utf-8"></script>
<script> 
	function stop(){ return false; } 
	document.oncontextmenu=stop; 
</script>
<div id="shareImage" style="display: none;"><img src="http://linyy.name/wp-content/uploads/2017/07/avatar-195x195.jpg" /></div>
</head>

<?php flush(); ?>

<body class="category" onLoad="scrollTo(0,0)" >
	<a name="gotop"></a>
	
<!--<div id="loader" class="loader">
	<div class="loader-content">
		<a href="<?php echo get_option('home'); ?>/"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/loading.png">
<p style="color:#e2e2e2;">林洋洋 | 有故事的个人博客</p>
<p style="color:#e2e2e2;">努力加载中...</p>
	</div>
</div>-->
     <div class="ace-wrapper">
         <header id="ace-header" class="ace-container-shift ace-logo-in ace-head-boxed ace-nav-right">
             <div class="ace-head-inner">
                 <div class="ace-head-container ace-container">
                     <div class="ace-head-row">
                         <div id="ace-head-col1" class="ace-head-col text-left">
                             <a id="ace-logo" class="ace-logo" href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 我 的 标 志">
								<img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/main.png" alt="林洋洋博客 | Logo"><span>.LinYY</span>
                             </a>
                         </div>
                         
                         <div id="ace-head-col2" class="ace-head-col text-right">
                             <div class="ace-nav-container ace-container hidden-sm hidden-xs">
                                 <nav id="ace-main-nav">
                                    <ul class="clear-list">
										<li <?php if (is_home()) { echo 'class="current"';} ?>><a title="林洋洋博客 | 主 页"  href="<?php echo get_option('home'); ?>/">Home</a></li>
										<?php wp_list_pages('depth=1&title_li=0&sort_column=menu_order'); ?>
										<li><a href="wp-login.php" title="林洋洋博客 | 登录">登录</a></li>
				
									</ul>
                                 </nav>
                             </div>
                         </div>
                         
                         <div id="ace-head-col3" class="ace-head-col text-right">
                             <button id="ace-sidebar-btn" class="btn btn-icon btn-light btn-shade">
                                 <span class="ace-icon ace-icon-side-bar-icon"></span>
                             </button>
                         </div>
                     </div>
                 </div><!-- .ace-container -->
             </div><!-- .ace-head-inner -->
        </header><!-- #ace-header -->

<article id="ace-card" class="ace-card bg-primary">
	<div class="ace-card-inner text-center"><script type="text/javascript">
	  var today = new Date(); //新建一个Date对象
	  var date = today.getDate();//查询当月日期
	  var day = today.getDay();//查询当前星期几
	  var week="";                               
	  if (day==0) week='<a href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 有故事的个人博客"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo7.png" width="195" height="195" alt="林洋洋博客-星期日"></a>';
	  if (day==1) week='<a href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 有故事的个人博客"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo1.png" width="195" height="195" alt="林洋洋博客-星期一"></a>';
	  if (day==2) week='<a href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 有故事的个人博客"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo2.png" width="195" height="195" alt="林洋洋博客-星期二"></a>';
	  if (day==3) week='<a href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 有故事的个人博客"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo3.png" width="195" height="195" alt="林洋洋博客-星期三"></a>';
	  if (day==4) week='<a href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 有故事的个人博客"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo4.png" width="195" height="195" alt="林洋洋博客-星期四"></a>';
	  if (day==5) week='<a href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 有故事的个人博客"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo5.png" width="195" height="195" alt="林洋洋博客-星期五"></a>';
	  if (day==6) week='<a href="<?php echo get_option('home'); ?>/" title="林洋洋博客 | 有故事的个人博客"><img class="avatar avatar-195" src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/logo6.png" width="195" height="195" alt="林洋洋博客-星期六"></a>';
	  document.write(week);
	</script>
	<h1><a href="?p=1138" title="林洋洋博客 | 我 的 名 字"><?php bloginfo('name'); ?></a></h1>
	<p class="text-muted" title="林洋洋博客 | 建 站 <?php echo floor((time()-strtotime("2017-04-01"))/86400); ?> 天"><?php echo floor((time()-strtotime("2017-04-01"))/86400); ?> 天 | <?php bloginfo('description'); ?></p>
	<ul class="ace-social clear-list">
		<li><a href="https://github.com/a1260157543" target="_blank" title="林洋洋博客 | github"><span class="ace-icon ace-icon-github"></span></a></li>							
		<li><a href="http://wpa.qq.com/msgrd?v=3&uin=1260157543&site=qq&menu=yes" target="_blank" title="林洋洋博客 | QQ联系我"><span class="ace-icon ace-icon-qq"></span></a></li>
		<li><a href="<?php bloginfo('rss2_url'); ?>" title="林洋洋博客 | RSS订阅" target="_blank"><span class="ace-icon ace-icon-rss"></span></a></li>
		<li><a href="?p=1395" title="林洋洋博客 | 下载简易测试APK"><span class="ace-icon ace-icon-android"></span></a></li>
		<li><a href="http://weibo.com/lyy250250" target="_blank" title="林洋洋博客 | 访问新浪微博"><span class="ace-icon ace-icon-weibo"></span></a></li>
		<div class="pf-grid-item design">
			<div class="project"  style="width:288px">
				<!--<center><a href="?p=1138" class="pf-btn-view btn btn-primary">了解  |  MORE</a></center>
<center><a href="?p=1138" class="pf-btn-view btn btn-primary">了解  |  MORE</a></center>-->
			</div>		

		</div>
	</ul>
	</div>

	<footer class="ace-card-footer">
		<form class="search-form" id="search" action="<?php bloginfo('url'); ?>/">
				<label>
					<span class="screen-reader-text">Search for:</span>
					<input type="search" class="search-field" placeholder="站内搜索(文章)" value="" id="s" name="s">
				</label>
				<button type="submit" class="search-submit" onClick="if(document.forms['search'].searchinput.value=='- Search -')document.forms['search'].searchinput.value=";" alt="Search"  >
					<span class="screen-reader-text"></span>
					<span class="ace-icon ace-icon-search"></span>
				</button>
		</form>

	</footer>
</article><!-- #ace-card -->

        <div id="ace-content" class="ace-container-shift">
            <div class="ace-container">
                    <div id="ace-nav-wrap" class="hidden-sm hidden-xs">
                        <div class="ace-nav-cont">
                            <div id="ace-nav-scroll">
                                <nav id="ace-nav" class="ace-nav">
                                    <ul class="clear-list">	
										<li>
											<a href="?page_id=346" data-tooltip="图 集"><span class="ace-icon ace-icon-portfolio"></span></a>
										</li>
										<li>
											<a href="?page_id=92" data-tooltip="人 物"><span class="ace-icon ace-icon-references"></span></a>
										</li>
										<li>
											<a href="?page_id=912" data-tooltip="情 感"><span class="ace-icon ace-icon-blog"></span></a>
										</li>
										<li>
											<a href="?page_id=347" data-tooltip="联系我"><span class="ace-icon ace-icon-contact"></span></a>
										</li>

									</ul>
                                </nav>
                            </div>

                            <div id="ace-nav-tools" class="hidden">
                                <span class="ace-icon ace-icon-dots-three-horizontal"></span>

                                <button id="ace-nav-arrow" class="clear-btn">
                                    <span class="ace-icon ace-icon-chevron-thin-down"></span>
                                </button>
                            </div>
                        </div>
                        <div class="ace-nav-btm"></div>
                    </div><!-- .ace-nav-wrap -->