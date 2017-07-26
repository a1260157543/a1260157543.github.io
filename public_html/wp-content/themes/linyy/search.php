<?php get_header(); ?>

                <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">
                        <div class="ace-paper-cont clear-mrg">
						
						<!-- START: PAGE CONTENT -->
    <div class="padd-box clear-mrg">
        <section class="section brd-btm">
            <div class="row">
                <div class="col-sm-12 clear-mrg text-box">
						<header class="search-for">
							<h1 class="search-title">搜&nbsp;&nbsp;索&nbsp;&nbsp;结&nbsp;&nbsp;果:</h1>
						</header>				
						

						<?php if (have_posts()) : while (have_posts()) : the_post(); ?>
						<article class="post hentry">
							<div class="padd-box-sm">
								<header class="post-header text-center">
									<h2 class="post-title entry-title text-upper"><a rel="bookmark" href="<?php the_permalink(); ?>"><?php the_title(); ?></a></h2>

									<div class="post-header-info">
										<span class="posted-on"><span class="screen-reader-text">Posted on </span>											
												<time class="post-date published"><?php the_time('Y年n月j日') ?></time></span>
										|&nbsp;<span class="post-author vcard">by <?php the_author_nickname(); ?></span>
										</br><span class="post-author vcard"><?php the_tags('标签： ', ', ', ''); ?></span>
										
									</div>
									<hr style="margin-left:150px;margin-right:150px;">
								</header>
							</div>
					<?php endwhile; ?>
					<?php else : ?>
					<center><strong class="title-lg text-upper">找不到相关文章(nothing found)</strong></center>
					<?php endif; ?>
       						<div class="wp_nav"><div class="page_navi"><?php par_pagenavi(9); ?></div>  </div>
						</article><!-- .post -->
						
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