<?php
/*
Template Name: Page myself
*/
?>
<?php get_header(); ?>

                <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">

                        <div class="ace-paper-cont clear-mrg">
						
						<!-- START: PAGE CONTENT -->
						<header class="page-header padd-box">
							<h1 class="title-lg text-upper">生 活 动 态</h1>
						</header>
						    <center><div class="pf-filter padd-box">
											<!--<a class="btn btn-sh btn-success" href="?page_id=348" >全部</a>-->
											<a class="btn btn-sh btn-primary" href="?page_id=543" >随口说说</a>
											<a class="btn btn-sh btn-primary" href="?page_id=545" >书籍</a>
							</div></center>
<div class="padd-box">						
<section class="section clear-mrg">
<div class="padd-box-sm clear-mrg">					
<?php
	$limit = get_option('posts_per_page');
	$paged = (get_query_var('paged')) ? get_query_var('paged') : 1;
	query_posts('cat=162&showposts=' . $limit=6 . '&paged=' . $paged);
	$wp_query->is_archive = true; $wp_query->is_home = false;
?>
<?php while (have_posts()) : the_post(); ?>
                <div class="ref-box brd-btm hreview">
                    <div class="ref-avatar">
                        <img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/avatar-195x195.png" alt=""  height="54" width="54">
                    </div>

                    <div class="ref-info">
                        <div class="ref-author">
                            <strong><?php the_title(); ?></strong>
                            <span><?php the_time('Y年n月j日') ?></span>
                        </div>
                            <p>
								<?php the_excerpt(); ?>
                            </p>
                    </div>
					<hr>
                </div><!-- .ref-box -->
			<?php endwhile; ?>

            </div><!-- .padd-box-sm -->
        </section><!-- .section -->
		
<section class="section clear-mrg">
<div class="padd-box-sm clear-mrg">					
<?php
	$limit = get_option('posts_per_page');
	$paged = (get_query_var('paged')) ? get_query_var('paged') : 1;
	query_posts('category_name=myself&showposts=' . $limit=8 . '&paged=' . $paged);
	$wp_query->is_archive = true; $wp_query->is_home = false;
?>
<?php while (have_posts()) : the_post(); ?>
                <div class="ref-box brd-btm hreview">
                    <div class="ref-avatar">
                        <a href="<?php the_permalink(); ?>"><img src="<?php bloginfo('template_url'); ?>/img/uploads/avatar/book.jpg" alt=""  height="54" width="54"></a>
                    </div>

                    <div class="ref-info">
                        <div class="ref-author">
                            <a href="<?php the_permalink(); ?>"><strong><?php the_title(); ?></strong></a>
                            <span><?php the_time('Y年n月j日') ?></span>
                        </div>
                            <p>
								<a href="<?php the_permalink(); ?>"><?php the_excerpt(); ?></a>
                            </p>
                    </div>
					<hr>
                </div><!-- .ref-box -->
			<?php endwhile; ?>

            </div><!-- .padd-box-sm -->
        </section><!-- .section -->
</div>			
						
						<div class="wp_nav"><div class="page_navi"><?php par_pagenavi(9); ?></div>  </div>
						<!--<div class="wp_nav"><?php echo wp_nav(); ?></div>-->
						    

						<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>