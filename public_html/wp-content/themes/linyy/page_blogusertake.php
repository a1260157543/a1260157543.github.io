<?php
/*
Template Name: 用户体验
*/
?>
<?php get_header(); ?>

                <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">

                        <div class="ace-paper-cont clear-mrg">
						
						<!-- START: PAGE CONTENT -->
						<header class="page-header padd-box">
							<h1 class="title-lg text-upper">个人技术博客(研究)</h1>
						</header>
    <div class="padd-box clear-mrg">	
						    <center><div class="pf-filter padd-box">
											<a class="btn btn-sh btn-primary" href="?page_id=96" >全部</a>
											<a class="btn btn-sh btn-success" href="?page_id=277" >用户体验</a>
											<a class="btn btn-sh btn-primary" href="?page_id=156" >网络技术</a>
							</div></center>
<article class="post hentry">
					
						
<?php
	$limit = get_option('posts_per_page');
	$paged = (get_query_var('paged')) ? get_query_var('paged') : 1;
	query_posts('category_name=usertake&showposts=' . $limit=4 . '&paged=' . $paged);
	$wp_query->is_archive = true; $wp_query->is_home = false;
?>
<?php while (have_posts()) : the_post(); ?>
							<div class="post-media">
									<center><figure class="post-figure">
										<?php the_post_thumbnail(); ?>
									</figure></center>
							</div>
							<div class="padd-box-sm">							
								<header class="post-header text-center">
									<h2 class="post-title entry-title text-upper"><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h2>

									<div class="post-header-info">
										<span class="posted-on"><?php the_time('Y年n月j日') ?></span>												
										|&nbsp;<span class="post-author vcard">by <?php the_author_nickname(); ?></span>
									</div>
								</header>
								<div class="post-content entry-content editor clearfix clear-mrg">
									<?php the_excerpt(); ?>
								</div>
								<footer class="post-footer">
									<div class="post-footer-top brd-btm clearfix">
											<div class="post-footer-info">
											<?php $category = get_the_category();
											echo $category[0]->cat_name;?> | <?php the_tags('标签： ', ', ', ''); ?>
										</div>

										<div class="post-more">
											<a class="btn btn-sm btn-primary" href="<?php the_permalink() ?>" >阅读全文...</a>
										</div>
									</div>
								</footer><hr>
							</div>
<?php endwhile; ?>
						</article><!-- .post -->

						<!--<div class="wp_nav"><?php echo wp_nav(); ?></div>-->
<div class="wp_nav"><div class="page_navi"><?php par_pagenavi(9); ?></div>  
</div>
						<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>