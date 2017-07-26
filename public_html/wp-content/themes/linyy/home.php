<?php get_header(); ?>
<div id="loader" class="loader">
	<div class="loader-content">
		<a href="<?php echo get_option('home'); ?>/"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/loading.png">
<p style="color:#e2e2e2;">林洋洋 | 有故事的个人博客</p>
<p style="color:#e2e2e2;">努力加载中...</p>
	</div>
</div>
<div class="ace-paper-stock">
	<main class="ace-paper clearfix">
	<div class="ace-paper-cont clear-mrg">
						
    <div class="padd-box clear-mrg">

<div id="main-container" class="container">
	<div class="row frontpage">
		<div class="col-md-8">
			<div class="fp-one">
				<div id="carousel-one" class="carousel slide">
					<?php
						query_posts('showposts=1&category_name=jrtj'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
						while(have_posts()) : the_post(); ?>
					<div class="carousel-inner">
						<div class="item active">
							<a href="<?php the_permalink(); ?>"  title="林洋洋博客 | 生 活 荐 读 logo"><?php the_post_thumbnail(); ?></a>
							<div  class="fp-one-imagen-footer" title="林洋洋博客 | 生 活 荐 读"><a href="?cat=11">生 活 荐 读</a></div>
							<div class="fp-one-cita-wrapper">
								<div class="fp-one-titulo-pubdate">
<span class="posted-on">热 夏</span>
									<p class="dom" title="林洋洋博客 | 荐 读 七 月">7</p>
										<center><div class="post-header-info">
										<span class="posted-on"><!--<?php the_time('Y/n/j') ?>-->月</span>
										</div></center>
								</div>
								<div class="fp-one-cita">
									<p><a href="<?php the_permalink(); ?>" title="林洋洋博客 | 生 活 荐 读 短 文"><?php the_excerpt(); ?></a></p>
								</div>
								<div class="clearfix"></div>
							</div>
                        </div>
					</div>
					<?php endwhile; ?>
				</div>
			</div>
		</div>
    <div class="col-md-4">
        <div class="row">
            <div class="col-md-12">
                <div class="fp-one-articulo">
                    <?
						$args=array(
						'numberposts'=>1,
						'meta_key'=>'views',
						'cat'=>3
						);
						$rand_posts=get_posts($args);
						foreach($rand_posts as $post){
						setup_postdata($post);
						?><a href="<?php $category = get_the_category(); $category_id= get_cat_id($category[0]->cat_name); echo '?cat='.$category_id; ?>" title="<?php $category = get_the_category(); echo $category[0]->cat_name; ?>"><h4 title="林洋洋博客 | 公 告 消 息">公告 消息</h4></a><?}?>
                    <ul class="list-unstyled pasado">
<?
						$args=array(
						'numberposts'=>7,
						'meta_key'=>'views',
						'cat'=>3
						);
						$rand_posts=get_posts($args);
						foreach($rand_posts as $post){
						setup_postdata($post);
						?>
                        <li>
                            <span class="text-muted"><?php the_time('') ?>&nbsp;|&nbsp;</span>
                                <a href="<?php the_permalink(); ?>" title="<?php the_title(); ?>"><?php the_title(); ?></a>
                        </li><?}?>
                    </ul>						
                </div>
            </div>

<div class="slide-container">
			<?php
				query_posts('showposts=4&category_name=study'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
				while(have_posts()) : the_post(); ?>
  <div class="wrapper">
	<div class="clash-card barbarian">
	  <div class="clash-card__unit-name"><a href="<?php the_permalink(); ?>"><?php the_title(); ?></a></div>
	  <div class="clash-card__level clash-card__level--barbarian"><?php the_time('Y/n/j') ?></div>
	  <div class="clash-card__unit-description">
		<?php the_excerpt(); ?>
	  </div>
	  <div class="clash-card__unit-stats clash-card__unit-stats--barbarian clearfix">
		<div class="one-third">
		  <div class="stat"><a title="作者：<?php the_author_nickname(); ?>"><?php the_author_nickname(); ?></a></div>
		</div>
		<div class="one-third">
		  <div class="stat"><a href="<?php $category = get_the_category(); $category_id= get_cat_id($category[0]->cat_name); echo '?cat='.$category_id; ?>" title="归档：<?php $category = get_the_category(); echo $category[0]->cat_name; ?>"><?php $category = get_the_category();
echo $category[0]->cat_name;?></a></div>
		</div>
		<div class="one-third no-border">
		  <div class="stat"><a href="<?php the_permalink() ?>" title="查看全文">Click</a></div>
		</div>
	  </div>
	</div>
  </div>
<?php endwhile; ?>	  
</div>
            
        </div>
    </div>
</div>

<!--<div class="container">
    <div id="main">
        <div class="carousel">

    <div class="content">
        <ul><a class="carousel-prev" href="detail.html">Previous</a>
								<?php
							query_posts('showposts=4&category_name=book-myself'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
						while(have_posts()) : the_post(); ?>
            <li>
                <div class="image">
                    <a href="detail.html"></a>
                    <?php the_post_thumbnail(); ?>
                </div>
                <div class="title">
                    <h3><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h3>
                </div>
                <div class="location">Palo Alto CA</div>
                <div class="area">
                    <span class="key">Area:</span>
                    <span class="value">750m<sup>2</sup></span>
                </div>
                <div class="bathrooms"><div class="inner">3</div></div>
            </li>
			<?php endwhile; ?>	
        </ul>
    </div>
</div></div>
</div>-->

<!--<div class="pf-wrap">
    <div class="pf-grid">
        <div class="pf-grid-sizer"></div>
			<?php
				query_posts('showposts=4&category_name=story'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
				while(have_posts()) : the_post(); ?>
        <div class="pf-grid-item  Reading photography design">
            <div class="project">
                <figure class="portfolio-figure">
                    <?php the_post_thumbnail(); ?>
                </figure>
                <div class="portfolio-caption text-center">
                    <div class="valign-table">
                        <div class="valign-cell">
                            <h2 class="text-upper"><a href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h2>
                            <p><span class="posted-on"><?php the_time('Y年n月j日') ?></span>
										&nbsp;<span class="post-author vcard"> | &nbsp;<span class="ace-icon ace-icon-folder-open-o"></span>&nbsp;<a href="<?php $category = get_the_category(); $category_id= get_cat_id($category[0]->cat_name); echo '?cat='.$category_id; ?>" title="<?php $category = get_the_category(); echo $category[0]->cat_name; ?>"><?php $category = get_the_category();
echo $category[0]->cat_name;?></a></span></p>
                            <a href="<?php the_permalink(); ?>" class="pf-btn-view btn btn-primary">View</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
		<?php endwhile; ?>		
	
	</div>
</div>
<br>-->
<center><div class="pf-wrap">
    <div class="pf-filter padd-box">
        <button data-filter=""></button>
		<a href="?page_id=545"><button data-filter=".Reading " title="林洋洋博客 | 安静的看一本书！ 按目录学习更高效！">我  &nbsp;&nbsp;的  &nbsp;&nbsp;阅  &nbsp;&nbsp;读</button></a>
    </div>
</div></center>		
	<div class="pf-wrap">

    <div class="pf-grid">
        <div class="pf-grid-sizer"></div>
						<?php
							query_posts('showposts=4&category_name=book-myself'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
						while(have_posts()) : the_post(); ?>
        <div class="pf-grid-item Reading photography design">
            <div class="project">
                <figure class="portfolio-figure">
                    <?php the_post_thumbnail(); ?>
                </figure>

                <div class="portfolio-caption text-center">
                    <div class="valign-table">
                        <div class="valign-cell">
                            <h2 class="text-upper"><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h2>
                            <p><span class="posted-on"><?php the_time('Y年n月j日') ?></span>
										&nbsp;<span class="post-author vcard"> | &nbsp;<span class="ace-icon ace-icon-folder-open-o"></span>&nbsp;<a href="<?php $category = get_the_category(); $category_id= get_cat_id($category[0]->cat_name); echo '?cat='.$category_id; ?>" title="<?php $category = get_the_category(); echo $category[0]->cat_name; ?>"><?php $category = get_the_category();
echo $category[0]->cat_name;?></a></span></p>
                            <a href="<?php the_permalink(); ?>" class="pf-btn-view btn btn-primary">View</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
						<?php endwhile; ?>		
	</div>
</div>
</br>
<center>
<section class="section">
            <div class="row">
                <div class="col-sm-2 clear-mrg hidden-xs">
					<a href="?cat=24"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/book_lcj_logok.png" alt="林洋洋博客 | 定期专题" title="林洋洋博客 | 定期专题"></a>
                </div>
<div class="col-sm-2 clear-mrg hidden-xs">
					<a href="?cat=23"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/book_gdxs_logok.png" alt="林洋洋博客 | 定期专题" title="林洋洋博客 | 定期专题"></a>
                </div>
<div class="col-sm-2 clear-mrg hidden-xs">
					<a href="?cat=22"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/book_ys_logok.png" alt="林洋洋博客 | 定期专题" title="林洋洋博客 | 定期专题"></a>
                </div>
				<div class="col-sm-2 clear-mrg hidden-xs">
					<a href="?cat=18"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/book_cyyxs_logok.png"  alt="林洋洋博客 | 音视频分享" title="林洋洋博客 | 音视频分享"></a>
                </div>
				<div class="col-sm-2 clear-mrg hidden-xs">
					<a href=""><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/wdhyh_logok.png"  alt="林洋洋博客 | 我的回忆盒" title="林洋洋博客 | 我的回忆盒"></a>
                </div>
				<div class="col-sm-2 clear-mrg hidden-xs">
					<a href="?cat=12"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/wdjlxc_logok.png"  alt="林洋洋博客 | 我的记录相册" title="林洋洋博客 | 我的记录相册"></a>
                </div>
            </div>
        </section>
</center>


<!--<div class="archives">
<?php
$previous_year = $year = 0;
$previous_month = $month = 0;
$ul_open = false;
$myposts = get_posts('numberposts=-1&orderby=post_date&order=DESC');
foreach($myposts as $post) :
setup_postdata($post);
$year = mysql2date('Y', $post->post_date);
$month = mysql2date('n', $post->post_date);
$day = mysql2date('j', $post->post_date);
if($year != $previous_year || $month != $previous_month) :
if($ul_open == true) :
echo '</ul>';
endif;
echo '<h4 class="m-title">'; echo the_time('Y-m'); echo '</h4>';
echo '<ul class="archives-monthlisting">';
$ul_open = true;
endif;
$previous_year = $year; $previous_month = $month;
?>
<li>
<a href="<?php the_permalink(); ?>"><span><?php the_time('Y-m-j'); ?></span>
<div class="atitle"><?php the_title(); ?></div></a>
</li>
<?php endforeach; ?>
</ul>
</div>-->


<article class="post hentry">
				<div class="post-media">
					<div class="post-slider slider">
						<?php
							query_posts('showposts=2&category_name=imageindex');
							while(have_posts()) : the_post(); ?>
						<div>
								<?php the_post_thumbnail(); ?>

                </div>
            </div>


						</div>
						<?php endwhile; ?>
					</div>


<center><div class="post-header-info">
	<span class="posted-on">感谢ONe故事、OnE书、oNE歌、onE博客造就了我的sKy。</span>
<!--<div id="login-reg">
			<span data-sign="0" id="user-login" class="user-login"><?php _e(' 登录','tinection'); ?></span>
			<span data-sign="1" id="user-reg" class="user-reg"><?php _e('注册','tinection'); ?></span>
		</div>-->
<a title="林洋洋 | 登录"  href="wp-login.php"><登录></a>  <?php   
    global $current_user, $display_name , $user_email;  
    get_currentuserinfo();  
	echo $current_user->display_name . "\n";   
    ?>
</div></center><br/>
</div>

<!--<div id="sign" class="sign">
<div class="part loginPart">
    <form id="login" action="<?php echo get_option('home'); ?>/wp-login.php" method="post" novalidate="novalidate">
<div id="register-active" class="switch"><i class="fa fa-toggle-on"></i>切换注册</div>
<h3>登录
<p class="status"></p>
</h3>
<label class="icon" for="username"><i class="fa fa-user"></i></label>
            <input class="input-control" id="username" type="text" placeholder="请输入用户名" name="username" required="" aria-required="true">
 
<label class="icon" for="password"><i class="fa fa-lock"></i></label>
            <input class="input-control" id="password" type="password" placeholder="请输入密码" name="password" required="" aria-required="true">
<p class="safe">
            <label class="remembermetext" for="rememberme"><input name="rememberme" type="checkbox" checked="checked" id="rememberme" class="rememberme" value="forever">记住我的登录</label>
            <a class="lost" href="<?php echo get_option('home'); ?>/wp-login.php?action=lostpassword"><?php _e('忘记密码 ?','tinection'); ?></a>
 
<input class="submit" type="submit" value="登录" name="submit">
        <input type="hidden" id="security" name="security" value="<?php echo  wp_create_nonce( 'security_nonce' );?>">
		<input type="hidden" name="_wp_http_referer" value="<?php echo $_SERVER['REQUEST_URI']; ?>">
	</form></div>
<div class="part registerPart">
    <form id="register" action="<?php bloginfo('url'); ?>/wp-login.php?action=register" method="post" novalidate="novalidate">
<div id="login-active" class="switch"><i class="fa fa-toggle-off"></i>切换登录</div> 
<h3>注册
<p class="status"></p>
</h3>
<label class="icon" for="user_name"><i class="fa fa-user"></i></label>
            <input class="input-control" id="user_name" type="text" name="user_name" placeholder="输入英文用户名" required="" aria-required="true">
 
<label class="icon" for="user_email"><i class="fa fa-envelope"></i></label>
            <input class="input-control" id="user_email" type="text" name="user_email" placeholder="输入常用邮箱" required="" aria-required="true">
 
<label class="icon" for="user_pass"><i class="fa fa-lock"></i></label>
            <input class="input-control" id="user_pass" type="password" name="user_pass" placeholder="密码最小长度为6" required="" aria-required="true">
 
<label class="icon" for="user_pass2"><i class="fa fa-retweet"></i></label>
            <input class="input-control" type="password" id="user_pass2" name="user_pass2" placeholder="再次输入密码" required="" aria-required="true">
 
<input class="submit" type="submit" value="注册" name="submit">
 
<a class="close"><span class="ace-icon ace-icon-star"></span></a>  
        <input type="hidden" id="user_security" name="user_security" value="<?php echo  wp_create_nonce( 'user_security_nonce' );?>"><input type="hidden" name="_wp_http_referer" value="<?php echo $_SERVER['REQUEST_URI']; ?>"> 
    </form></div>
</div>-->

			</article>
</div>
		
	</div><!-- .pf-grid -->
</div><!-- .pf-wrap -->
					

    </div><!-- .padd-box -->
<!-- END: PAGE CONTENT -->

    </div><!-- .padd-box -->
<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>