<?php
/*
Template Name: Page Gallery 其他
*/
?>

<?php get_header(); ?>

                <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">
                        <div class="ace-paper-cont clear-mrg">
						
						<!-- START: PAGE CONTENT -->

<div class="padd-box">
    <h1 class="title-lg text-upper">有故事的个人画廊</h1>
</div>


<div class="pf-wrap">
    <div class="pf-filter padd-box">
	<center><div class="pf-filter padd-box">
		<a class="btn btn-sh btn-primary" href="?page_id=346" >全部</a>&nbsp;&nbsp;
		<a class="btn btn-sh btn-primary" href="?page_id=349" >旅游记录</a>&nbsp;&nbsp;
		<a class="btn btn-sh btn-primary" href="?page_id=251" >Me</a>&nbsp;&nbsp;
		<a class="btn btn-sh btn-success" href="?page_id=253" >其 他</a>
	</center>
		<article class="post hentry">
						<?php
							query_posts('cat=145'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
						while(have_posts()) : the_post(); ?>
							<div class="padd-box-sm">
							<hr>								
								<header class="post-header text-center">
								<div class="post-media">
								<h2 class="post-title entry-title text-upper"><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h2>
								<h4 class="post-title entry-title text-upper"></h4>
															<center><p><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_time('Y年n月j日') ?> | <?php the_tags('标签：', ', ', ''); ?></a></p></center>
									<a href="<?php the_permalink(); ?>" rel="bookmark">
										<center><figure class="post-figure">
											<img src="<?php echo catch_that_image() ?>" alt="">
										</figure></center>
									</a>
								</div>

									<!--<div class="post-header-info">
										<span class="posted-on"><?php the_time('Y年n月j日') ?></span>												
										|&nbsp;<span class="post-author vcard">by <?php the_author_nickname(); ?></span>
									</div>
								</header>
								<div class="post-content entry-content editor clearfix clear-mrg">
									<p><?php the_excerpt(); ?></p>
								</div>
								<footer class="post-footer">
									<div class="post-footer-top brd-btm clearfix">
										<div class="post-footer-info">
									<?php the_tags('标签： ', ', ', ''); ?>
												</span><span class="post-line">|</span><a
												href="" class="post-comments-count"><?php comments_popup_link('0 条评论', '1 条评论', '% 条评论', '', '评论已关闭'); ?></a>
										</div>

										<div class="post-more">
											<a class="btn btn-sm btn-primary" href="<?php the_permalink() ?>" >阅读全文...</a>
										</div>
									</div>
								</footer>-->
							</div>
<?php endwhile; ?>
						</article><!-- .post -->

						<!--<div class="wp_nav"><?php echo wp_nav(); ?></div>-->

						<!-- END: PAGE CONTENT -->
    </div><!-- .pf-filter -->
</div><!-- .pf-wrap -->
<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>

