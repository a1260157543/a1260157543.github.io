<?php
/*
Template Name: Page Gallery2
*/
?>

<?php get_header(); ?>

             <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">
                        <div class="ace-paper-cont clear-mrg">
						
						<!-- START: PAGE CONTENT -->

<div class="padd-box">
    <!--<h1 class="title-lg text-upper">有故事的个人画廊</h1>-->
</div>


<div class="pf-wrap">
    <div class="pf-filter padd-box">
        <button data-filter="*">全部</button>
        <button data-filter=".photography">生 活</button>
        <button data-filter=".design">其 他</button>
    </div><!-- .pf-filter -->

    <div class="pf-grid">
        <div class="pf-grid-sizer"></div><!-- used for sizing -->

        <div class="pf-grid-item">
			<?php
				query_posts('cat=49'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
					while(have_posts()) : the_post(); ?>
            <div class="project">
                <figure class="portfolio-figure">
                    <img src="<?php echo catch_that_image() ?>" alt="">
                </figure>

                <div class="portfolio-caption text-center">
                    <div class="valign-table">
                        <div class="valign-cell">
                            <h2 class="text-upper"><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h2>
                            <p><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_time('Y年n月j日') ?> | <?php the_tags('标签：', ', ', ''); ?></a></p></p>
                            <a href="#pf-popup-1" class="pf-btn-view btn btn-primary">View More</a>
                        </div>
                    </div>
                </div>
            </div>

            <div id="pf-popup-1" class="pf-popup clearfix">
                <div class="pf-popup-col1">
                    <div class="pf-popup-media">

                    </div>
                </div><!-- .pf-popup-col1 -->

                <div class="pf-popup-col2">
                    <div class="pf-popup-info clear-mrg">
                        <h2 class="text-upper"><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h2>
                        <p class="text-muted"><strong><?php the_author_nickname(); ?></strong></p>
                        <dl class="dl-horizontal">
                            <dt>Date:</dt>
                            <dd><?php the_time('Y年n月j日') ?></dd>
                        </dl>
                        <p><?php the_content(); ?></p>
                    </div><!-- .pf-popup-info -->

                </div><!-- .pf-popup-col2 -->
            </div><!-- .pf-popup -->
			<?php endwhile; ?>
        </div><!-- .pf-grid-item -->


        <div class="pf-grid-item design">
			<?php
				query_posts('cat=63'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
					while(have_posts()) : the_post(); ?>
            <div class="project">
                <figure class="portfolio-figure">
                    <img src="<?php echo catch_that_image() ?>" alt="">
                </figure>

                <div class="portfolio-caption text-center">
                    <div class="valign-table">
                        <div class="valign-cell">
                            <h2 class="text-upper"><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h2>
                            <p><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_time('Y年n月j日') ?> | <?php the_tags('标签：', ', ', ''); ?></a></p></p>
                            <a href="#pf-popup-1" class="pf-btn-view btn btn-primary">View More</a>
                        </div>
                    </div>
                </div>
            </div>

            <div id="pf-popup-6" class="pf-popup clearfix">
                <div class="pf-popup-col1">
                    <div class="pf-popup-media">

                    </div>
                </div><!-- .pf-popup-col1 -->

                <div class="pf-popup-col2">
                    <div class="pf-popup-info clear-mrg">
                        <h2 class="text-upper">2222</h2>
                        <p class="text-muted"><strong>design / development</strong></p>
                        <dl class="dl-horizontal">
                            <dt>Date:</dt>
                            <dd>11 Jan 2012</dd>

                            <dt>Site link:</dt>
                            <dd><a href="www.sitedomen.com">www.sitedomen.com</a></dd>

                            <dt>Client:</dt>
                            <dd>11 Jan 2012</dd>
                        </dl>
                        <p>xxxxx</p>
                    </div><!-- .pf-popup-info -->

                </div><!-- .pf-popup-col2 -->
            </div><!-- .pf-popup -->
						<?php endwhile; ?>
        </div><!-- .pf-grid-item -->
    
		<div class="pf-grid-item photography">
								<?php
				query_posts('cat=62'); //cat=-1为排除ID为1的分类下文章showposts=3&cat=33
					while(have_posts()) : the_post(); ?>
			<div class="project">
                <figure class="portfolio-figure">
                    <img src="<?php echo catch_that_image() ?>" alt="">
                </figure>

                <div class="portfolio-caption text-center">
                    <div class="valign-table">
                        <div class="valign-cell">
                            <h2 class="text-upper"><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_title(); ?></a></h2>
                            <p><a href="<?php the_permalink(); ?>" rel="bookmark"><?php the_time('Y年n月j日') ?> | <?php the_tags('标签：', ', ', ''); ?></a></p></p>
                            <a href="#pf-popup-1" class="pf-btn-view btn btn-primary">View More</a>
                        </div>
                    </div>
                </div>
            </div>


            <div id="pf-popup-7" class="pf-popup clearfix">
                <div class="pf-popup-col1">
                    <div class="pf-popup-media">

                    </div>
                </div><!-- .pf-popup-col1 -->

                <div class="pf-popup-col2">
                    <div class="pf-popup-info clear-mrg">
                        <h2 class="text-upper">333</h2>
                        <p class="text-muted"><strong>design / development</strong></p>
                        <dl class="dl-horizontal">
                            <dt>Date:</dt>
                            <dd>11 Jan 2012</dd>

                            <dt>Site link:</dt>
                            <dd><a href="www.sitedomen.com">www.sitedomen.com</a></dd>

                            <dt>Client:</dt>
                            <dd>11 Jan 2012</dd>
                        </dl>
                        <p>About 64% of all on-line teens say that do things online that they wouldn’t want their
                            parents to know about. 11% of all adult internet users visit dating websites and spend their
                            time in chatrooms. Some of the classify their behavior as “cyber affair” More then 60% of
                            employees use company PC for the personal needs during their work hours as long as 80
                            minutes per day. </p>
                    </div><!-- .pf-popup-info -->

                </div><!-- .pf-popup-col2 -->
            </div><!-- .pf-popup --><?php endwhile; ?>
        </div><!-- .pf-grid-item -->
	</div><!-- .pf-grid -->
</div><!-- .pf-wrap -->

<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>

