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
						
						<!-- START: PAGE CONTENT -->
    <div class="padd-box clear-mrg">
        <section class="section brd-btm">
            <div class="row">
                <div class="col-sm-12 clear-mrg text-box">
						<?php if (have_posts()) : while (have_posts()) : the_post(); ?>
						<article class="post hentry">
							<div class="padd-box-sm">
								<header class="post-header text-center">
									<h2 class="post-title entry-title text-upper"><a href="<?php the_permalink(); ?>"  title="林洋洋博客"><?php the_title(); ?></a></h2>

									<div class="post-header-info">
										<span class="posted-on"><?php the_time('Y年n月j日') ?>&nbsp;|&nbsp;<span class="ace-icon ace-icon-folder-open-o"></span>&nbsp;<a href="<?php $category = get_the_category(); $category_id= get_cat_id($category[0]->cat_name); echo '?cat='.$category_id; ?>" title="<?php $category = get_the_category(); echo $category[0]->cat_name; ?>"><?php $category = get_the_category();
echo $category[0]->cat_name;?></a><!--<?php if(function_exists('custom_the_views') ) custom_the_views($post->ID); ?>-->&nbsp;|&nbsp;<span class="ace-icon ace-icon-bookmark-o"></span>&nbsp;<?php the_tags('', ', ', ''); ?></span>							
									</div>
								</header>

								<div class="post-content entry-content editor clearfix clear-mrg" style="margin-top:20px;font-size:14px">
									<p><span><?php the_content(); ?>
<script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"0","bdSize":"16"},"image":{"viewList":["qzone","tsina","tqq","renren","weixin"],"viewText":"分享到：","viewSize":"16"}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script>
</span></p>
<center><?php
     wp_link_pages('before=<div id="fenye">&after=&next_or_number=next&previouspagelink=上&nextpagelink=&nbsp;&nbsp;');
     wp_link_pages('before=&after=&next_or_number=number');
     wp_link_pages('before=&after=</div>&next_or_number=next&previouspagelink=&nbsp;&nbsp;&nextpagelink=下');
?>
</center>	
								</div>
<footer class="post-footer">
<div class="post-like">
	<a href="javascript:;" data-action="ding" data-id="<?php the_ID(); ?>" class="favorite<?php if(isset($_COOKIE['bigfa_ding_'.$post->ID])) echo ' done';?>">喜欢 <span class="count">
	<?php 
		if( get_post_meta($post->ID,'bigfa_ding',true) ){            
			echo get_post_meta($post->ID,'bigfa_ding',true);
		} else {
			echo '0';
		}
	?></span>
	</a>
 </div>

<!--<?php if (get_previous_post()) { previous_post_link('上一篇: %link');} else {echo "已是最后文章";} ?> / 
<?php if (get_next_post()) { next_post_link('下一篇: %link');} else {echo "已是最新文章";} ?>		-->	</footer>									<!--<hr style="margin-left:150px;margin-right:150px;">

<!--
<div id="SOHUCS" sid="<?php the_ID();?>" ></div> 
<script type="text/javascript"> 
(function(){ 
var appid = 'cyt1N5Ndj'; 
var conf = 'prod_84980d7355125b152b0a4e77e6ea0c0e'; 
var width = window.innerWidth || document.documentElement.clientWidth; 
if (width < 960) { 
window.document.write('<script id="changyan_mobile_js" charset="utf-8" type="text/javascript" src="http://changyan.sohu.com/upload/mobile/wap-js/changyan_mobile.js?client_id=' + appid + '&conf=' + conf + '"><\/script>'); } else { var loadJs=function(d,a){var c=document.getElementsByTagName("head")[0]||document.head||document.documentElement;var b=document.createElement("script");b.setAttribute("type","text/javascript");b.setAttribute("charset","UTF-8");b.setAttribute("src",d);if(typeof a==="function"){if(window.attachEvent){b.onreadystatechange=function(){var e=b.readyState;if(e==="loaded"||e==="complete"){b.onreadystatechange=null;a()}}}else{b.onload=a}}c.appendChild(b)};loadJs("http://changyan.sohu.com/upload/changyan.js",function(){window.changyan.api.config({appid:appid,conf:conf})}); } })(); </script>-->

								<?php 
									 comments_template(); ?><br/>
								<?php
									// global $withcomments; 
									// $withcomments = true;
									//包含评论模板文件，
									// comments_template("/comments-other.php");
								// ?>
							</div>
					<?php endwhile; ?>
					<?php else : ?>
					输出找不到文章提示
					<?php endif; ?>

						</article><!-- .post -->


 <!--<center>
    <?php
    $data = wp_get_post_categories($post->ID);
    if ($data) {
    $related = $wpdb->get_results("
    SELECT post_title, ID
    FROM {$wpdb->prefix}posts, {$wpdb->prefix}term_relationships, {$wpdb->prefix}term_taxonomy
    WHERE {$wpdb->prefix}posts.ID = {$wpdb->prefix}term_relationships.object_id
    AND {$wpdb->prefix}term_taxonomy.taxonomy = 'category'
    AND {$wpdb->prefix}term_taxonomy.term_taxonomy_id = {$wpdb->prefix}term_relationships.term_taxonomy_id
    AND {$wpdb->prefix}posts.post_status = 'publish'
    AND {$wpdb->prefix}posts.post_type = 'post'
    AND {$wpdb->prefix}term_taxonomy.term_id = '" . $data[0] . "'
    AND {$wpdb->prefix}posts.ID != '" . $post->ID . "'
    ORDER BY RAND()
    LIMIT 2");

    if ( $related ) {
    foreach ($related as $related_post) {
    ?>
    <a href="<?php echo get_permalink($related_post->ID); ?>" rel="bookmark" title="<?php echo $related_post->post_title; ?>">
    <img src="<?php bloginfo('template_url'); ?>/img/d2.png"><?php echo $related_post->post_title; ?></a>
    <?php } } else { ?>
    <?php } }?></center>-->
                </div>
            </div>
        </section><!-- .section -->
       
    </div><!-- .padd-box -->
<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>