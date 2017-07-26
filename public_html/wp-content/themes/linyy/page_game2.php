<?php
/*
Template Name: Page游戏2
*/
?>

 <?php get_header(); ?>
	<style>

	.main{position:relative;margin:10px auto;max-width:380px;height:600px;border:1px solid #ccc;overflow:hidden;}
	.ph-main{width: 100%;height: 100%;position: relative;border: none; margin: auto;overflow: hidden;}
	.container{position:absolute;top:-150px;width:100%;height:auto;}
	.row{width:100%;height:150px;}
	.cell{float:left;width:25%;height:100%;background-color: #fff;}
	.block{background:#3B5998;cursor:pointer;}
	.mark{ position: absolute; width: 40px; height: 20px; background-color: #e8e8e8;
		border-radius: 50%; top: 10px; left: 50%; margin-left: -20px; text-align: center; line-height: 20px;z-index: 1;}
	.mask , .again-mask{position: absolute;top: 0;left: 0;width: 100%;height: 100%;background: rgba(0,0,0,.3);text-align: center;z-index: 2;}
	.mask h1 , .again-mask h1{ color: #fff;height: 50px;line-height: 50px;font-family: '微软雅黑';margin-top: 35%; }
	.mask span , .again-mask span{ display: block; width: 100px;height: 50px;font-size: 20px; text-align: center; line-height: 50px;margin: 50px auto; background: #3B5998;color: #fff;border-radius: 6px; cursor: pointer; -webkit-box-shadow: 1px 1px 1px #999;box-shadow: 1px 1px 1px #999;text-shadow: 1px 1px 1px #fff; }
	.again-mask h2{ color: #fff;height: 45px;line-height: 45px;font-family: '微软雅黑';}
	</style>
                <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">
                        <div class="ace-paper-cont clear-mrg">
						
						<!-- START: PAGE CONTENT -->
    <div class="padd-box clear-mrg">
        <section class="section brd-btm">
            <div class="row">
                <div class="col-sm-12 clear-mrg text-box">
						<?php if (have_posts()) : while (have_posts()) : the_post(); ?>
						<article class="post hentry">
							<div class="padd-box-sm">
								<header class="post-header text-center">
									<h2 class="post-title entry-title text-upper"><a rel="bookmark" href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h2>

									<div class="post-header-info">
										<span class="posted-on"><?php the_time('Y年n月j日') ?></span>												
										|&nbsp;<span class="post-author vcard">by <?php the_author_nickname(); ?> </span>
										|&nbsp;<a href="?page_id=278"><span class="post-author vcard">手机版点我<span></a>
									</div>
								</header>
<div class="main" id="main">
		<div class="container" id="container">
		</div>
		<div class="mask" id="mask">
			<h1>别踩白块儿</h1>
			<span id="start">开始</span>
		</div>
	</div>

	<script src="<?php bloginfo('template_url'); ?>/js/Block.js"></script>
	<script>
		var oContainer = document.getElementById('container');
		var block = new Block(oContainer);
		block.init();

		var mask = document.getElementById('mask');
		var start = document.getElementById('start');
		start.onclick = function(){
			block.start();
			mask.style.display = 'none';
		}
	</script>
								</br>
<?php 
									 comments_template(); ?>
								<?php
									// global $withcomments; 
									// $withcomments = true;
									//包含评论模板文件，
									// comments_template("/inline-comments.php");
								// ?>
							</div>
					<?php endwhile; ?>
					<?php else : ?>
					输出找不到文章提示
					<?php endif; ?>

						</article><!-- .post -->
                </div>
            </div>
        </section><!-- .section -->


<div id="login-reg">
			<span data-sign="0" id="user-login" class="user-login"><?php _e(' 登录','tinection'); ?></span>
			<span data-sign="1" id="user-reg" class="user-reg"><?php _e('注册','tinection'); ?></span>
		</div>

<div id="sign" class="sign">
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
 
<a class="close"><i class="fa fa-times"></i></a>
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
 
<a class="close"><i class="fa fa-times"></i></a>  
        <input type="hidden" id="user_security" name="user_security" value="<?php echo  wp_create_nonce( 'user_security_nonce' );?>"><input type="hidden" name="_wp_http_referer" value="<?php echo $_SERVER['REQUEST_URI']; ?>"> 
    </form></div>
</div>					
       
    </div><!-- .padd-box -->
<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>