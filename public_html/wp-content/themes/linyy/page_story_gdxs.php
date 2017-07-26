<?php
/*
Template Name: Page 古典小说
*/
?>
<?php get_header(); ?>

                <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">
                        <div class="ace-paper-cont clear-mrg">
						<header class="page-header padd-box">
							<h1 class="title-lg text-upper">古典小说系列</h1>
						</header>
						<!-- START: PAGE CONTENT -->
    <div class="padd-box clear-mrg">
	
<div class="pf-wrap">
    <div class="pf-grid">
        <div class="pf-grid-sizer"></div><!-- used for sizing -->
<?php
	$limit = get_option('posts_per_page');
	$paged = (get_query_var('paged')) ? get_query_var('paged') : 1;
	query_posts('cat=234&showposts=' . $limit=8 . '&paged=' . $paged);
	$wp_query->is_archive = true; $wp_query->is_home = false;
?>
<?php while (have_posts()) : the_post(); ?>
        <div class="pf-grid-item photography">
            <div class="project">
                <figure class="portfolio-figure">
                    <?php the_post_thumbnail(); ?>
                </figure>
                <div class="portfolio-caption text-center">
                    <div class="valign-table">
                        <div class="valign-cell">
                            <h2 class="text-upper"><a href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h2>
                            <p><span class="posted-on"><?php the_time('Y年n月j日') ?></span>
										&nbsp;<span class="post-author vcard"> | &nbsp;<?php the_tags('', ', ', ''); ?></span></p>
                            <a href="<?php the_permalink(); ?>" class="pf-btn-view btn btn-primary">View</a>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- .pf-grid-item -->
						<?php endwhile; ?>		
	</div><!-- .pf-grid -->
</div><!-- .pf-wrap -->	
						<div class="wp_nav"><div class="page_navi"><?php par_pagenavi(9); ?></div>  </div>
						<!--<div class="wp_nav"><?php echo wp_nav(); ?></div>-->
</br>	
	<center>
												<div class="post-header-info">
<span class="posted-on">感谢ONe故事、OnE书、oNE歌、onE博客造就了我的sKy。</span>
												</div>
</center>	
<!--
<section class="section brd-btm">
            <div class="row">
                <div class="col-sm-6 clear-mrg">
<center><div class="pf-wrap">
    <div class="pf-filter padd-box">
	    <button data-filter=""></button>
        <button data-filter="">Hot Books</button>
    </div>
</div></center>
<?
$args=array(
'numberposts'=>5,
'orderby'=>'meta_value_num',//按点击量排序
'meta_key'=>'views',
'cat'=>163
);
$rand_posts=get_posts($args);
foreach($rand_posts as $post){
setup_postdata($post);
?>
                    <div class="progress-line ace-animate" role="progressbar" aria-valuenow="<? echo the_views(true, '','','true');?>" aria-valuemin="0" aria-valuemax="1000" style="margin-bottom:24px">
                        <a href="<?php the_permalink(); ?>"><strong class="progress-title"><?php the_title(); ?></strong></a>
                        <div class="progress-bar" data-text="<? echo the_views(true, '','&nbsp;次','true');?>" data-value=""></div>
                    </div>
<?}?>
                </div>

                <div class="col-sm-6 clear-mrg">
<center><div class="pf-wrap">
    <div class="pf-filter padd-box">
	    <button data-filter=""></button>
        <button data-filter="">Hot Blogs</button>
    </div>
</div></center>
<?
$args=array(
'numberposts'=>5,
'orderby'=>'meta_value_num',//按点击量排序
'meta_key'=>'views',
'cat'=>141
);
$rand_posts=get_posts($args);
foreach($rand_posts as $post){
setup_postdata($post);
?>
                    <div class="progress-line ace-animate" role="progressbar" aria-valuenow="<? echo the_views(true, '','','true');?>" aria-valuemin="0" aria-valuemax="1000" style="margin-bottom:24px">
                        <a href="<?php the_permalink(); ?>"><strong class="progress-title"><?php the_title(); ?></strong></a>
                        <div class="progress-bar" data-text="<? echo the_views(true, '','&nbsp;次','true');?>" data-value=""></div>
                    </div>
<?}?>
                </div><!-- .col-sm-6 -->
            </div><!-- .row -->
        </section><!-- .section -->



	


						<!--<article class="post hentry">
							<div class="post-media">
								<div class="post-slider slider">
									<div>
<img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/main.gif" alt="">
									</div>
								</div>
							</div>
						</article>-->
		
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