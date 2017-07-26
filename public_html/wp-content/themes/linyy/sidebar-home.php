<?php
/**
 * The sidebar containing the main home columns widget areas
 *
 * @subpackage fart
 * @author tishonator
 * @since fart 1.0.0
 *
 */
?>

<div id="home-cols">

	<div id="home-cols-inner">

		<?php 
			/**
			 * Display widgets dragged in 'Homepage Columns 1' widget areas
			 */
		?>
		<div class="col4a">

			<?php if ( !dynamic_sidebar( 'homepage-column-1-widget-area' ) ) : ?>

						<h2 class="sidebar-title">
							<?php _e('Home Col Widget 1', 'fart'); ?>
						</h2><!-- .sidebar-title -->
						
						<div class="sidebar-after-title">
						</div><!-- .sidebar-after-title -->
						
						<div class="textwidget">
							<?php _e('This is first homepage widget area. To customize it, please navigate to Admin Panel -> Appearance -> Widgets and add widgets to Homepage Column #1.', 'fart'); ?>
						</div><!-- .textwidget -->
			
			<?php endif; // end of ! dynamic_sidebar( 'homepage-column-1-widget-area' )
				  ?>

		</div><!-- .col4a -->
		
		<?php 
			/**
			 * Display widgets dragged in 'Homepage Columns 2' widget areas
			 */
		?>
		<div class="col4b">
			<?php if ( !dynamic_sidebar( 'homepage-column-2-widget-area' ) ) : ?>
			
					<h2 class="sidebar-title">
						<?php _e('Home Col Widget 2', 'fart'); ?>
					</h2><!-- .sidebar-title -->
					
					<div class="sidebar-after-title">
					</div><!-- .sidebar-after-title -->
					
					<div class="textwidget">
						<?php _e('This is second homepage widget area. To customize it, please navigate to Admin Panel -> Appearance -> Widgets and add widgets to Homepage Column #2.', 'fart'); ?>
					</div><!-- .textwidget -->
						
			<?php endif; // end of ! dynamic_sidebar( 'homepage-column-2-widget-area' )
				  ?>
			
		</div><!-- .col4b -->
		
		<?php 
			/**
			 * Display widgets dragged in 'Homepage Columns 3' widget areas
			 */
		?>
		<div class="col4c">
			<?php if ( !dynamic_sidebar( 'homepage-column-3-widget-area' ) ) : ?>
			
					<h2 class="sidebar-title">
						<?php _e('Home Col Widget 3', 'fart'); ?>
					</h2><!-- .sidebar-title -->
					
					<div class="sidebar-after-title">
					</div><!-- .sidebar-after-title -->
					
					<div class="textwidget">
						<?php _e('This is third homepage widget area. To customize it, please navigate to Admin Panel -> Appearance -> Widgets and add widgets to Homepage Column #3.', 'fart'); ?>
					</div><!-- .textwidget -->
						
			<?php endif; // end of ! dynamic_sidebar( 'homepage-column-3-widget-area' )
				  ?>
			
		</div><!-- .col4c -->
		
		<?php 
			/**
			 * Display widgets dragged in 'Homepage Columns 4' widget areas
			 */
		?>
		<div class="col4d">
			<?php if ( !dynamic_sidebar( 'homepage-column-4-widget-area' ) ) : ?>
			
					<h2 class="sidebar-title">
						<?php _e('Home Col Widget 4', 'fart'); ?>
					</h2><!-- .sidebar-title -->
					
					<div class="sidebar-after-title">
					</div><!-- .sidebar-after-title -->
					
					<div class="textwidget">
						<?php _e('This is fourth homepage widget area. To customize it, please navigate to Admin Panel -> Appearance -> Widgets and add widgets to Homepage Column #4.', 'fart'); ?>
					</div><!-- .textwidget -->
						
			<?php endif; // end of ! dynamic_sidebar( 'homepage-column-4-widget-area' )
				  ?>
			
		</div><!-- .col4c -->
		
		<div class="clear">
		</div><!-- .clear -->

	</div><!-- #home-cols-inner -->

</div><!-- #home-cols -->