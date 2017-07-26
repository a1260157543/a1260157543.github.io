<?php
/*
Template Name: Page Search
*/
?>

<?php get_header(); ?>

                <div class="ace-paper-stock">
                    <main class="ace-paper clearfix">
                        <div class="ace-paper-cont clear-mrg">
						
						<!-- START: PAGE CONTENT -->

    <div class="padd-box-sm">
        <header class="search-for">
            <h1 class="search-title">搜&nbsp;&nbsp;索&nbsp;&nbsp;结&nbsp;&nbsp;果:</h1>
        </header>
        <div class="search-result">
        
				<!--<a class="btn btn-lg btn-block btn-thin btn-upper " href="">了解  |  MORE</a>-->
								<section class="widget widget_search">
					<form class="search-form" id="search" action="<?php bloginfo('url'); ?>/">
						<label>
							<span class="screen-reader-text">Search for:</span>
							<input type="search" class="search-field" placeholder="站内搜索(文章)" value="" id="s" name="s">
						</label>
						<button type="submit" class="search-submit" onClick="if(document.forms['search'].searchinput.value=='- Search -')document.forms['search'].searchinput.value=”;” alt=”Search”  >
							<span class="screen-reader-text"></span>
							<span class="ace-icon ace-icon-search"></span>
						</button>
					</form>
					<?php
						get_search_form();
					?>
        </div>
    </div>

<!-- END: PAGE CONTENT -->
						
                </div><!-- .ace-paper-cont -->
            </main><!-- .ace-paper -->
        </div><!-- .ace-paper-stock -->

        </div><!-- .ace-container -->
    </div><!-- #ace-content -->

	<?php get_sidebar(); ?>
<?php get_footer(); ?>
