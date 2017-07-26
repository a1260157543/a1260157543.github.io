<?php
/**
 * fArt functions and definitions
 *
 * Set up the theme and provides some helper functions, which are used in the
 * theme as custom template tags. Others are attached to action and filter
 * hooks in WordPress to change core functionality.
 *
 * When using a child theme you can override certain functions (those wrapped
 * in a function_exists() call) by defining them first in your child theme's
 * functions.php file. The child theme's functions.php file is included before
 * the parent theme's file, so the child theme functions would be used.
 *
 * @link https://codex.wordpress.org/Theme_Development
 * @link https://codex.wordpress.org/Child_Themes
 *
 * Functions that are not pluggable (not wrapped in function_exists()) are
 * instead attached to a filter or action hook.
 *
 * For more information on hooks, actions, and filters,
 * {@link https://codex.wordpress.org/Plugin_API}
 *
 * @subpackage fArt
 * @author tishonator
 * @since fArt 1.0.0
 *
 */

require_once( trailingslashit( get_template_directory() ) . 'customize-pro/class-customize.php' );

if ( ! function_exists( 'fart_setup' ) ) :
/**
 * fArt setup.
 *
 * Set up theme defaults and registers support for various WordPress features.
 *
 * Note that this function is hooked into the after_setup_theme hook, which
 * runs before the init hook. The init hook is too late for some features, such
 * as indicating support post thumbnails.
 *
 */
function fart_setup() {

	load_theme_textdomain( 'fart', get_template_directory() . '/languages' );

	add_theme_support( "title-tag" );

	// add the visual editor to resemble the theme style
	add_editor_style( array( 'css/editor-style.css', get_template_directory_uri() . '/css/font-awesome.min.css' ) );

	// This theme uses wp_nav_menu() in two locations.
	register_nav_menus( array(
		'primary'   => __( 'Primary Menu', 'fart' ),
	) );

	// Add wp_enqueue_scripts actions
	add_action( 'wp_enqueue_scripts', 'fart_load_scripts' );

	add_action( 'widgets_init', 'fart_widgets_init' );

	// add Custom background				 
	add_theme_support( 'custom-background', 
				   array ('default-color'  => '#FFFFFF')
				 );

	add_theme_support( 'post-thumbnails' );
	set_post_thumbnail_size( 1200, 0, true );

	global $content_width;
	if ( ! isset( $content_width ) )
		$content_width = 900;

	add_theme_support( 'automatic-feed-links' );

	/*
	 * Switch default core markup for search form, comment form, and comments
	 * to output valid HTML5.
	 */
	add_theme_support( 'html5', array(
		'comment-form', 'comment-list',
	) );

	// add custom header
	add_theme_support( 'custom-header', array (
                       'default-image'          => '',
                       'random-default'         => '',
                       'flex-height'            => true,
                       'flex-width'             => true,
                       'uploads'                => true,
                       'width'                  => 900,
                       'height'                 => 100,
                       'default-text-color'        => '#000000',
                       'wp-head-callback'       => 'fart_header_style',
                    ) );

    // add custom logo
    add_theme_support( 'custom-logo', array (
                       'width'                  => 145,
                       'height'                 => 36,
                       'flex-height'            => true,
                       'flex-width'             => true,
                    ) );

	
}
endif; // fart_setup
add_action( 'after_setup_theme', 'fart_setup' );

/**
 * the main function to load scripts in the fArt theme
 * if you add a new load of script, style, etc. you can use that function
 * instead of adding a new wp_enqueue_scripts action for it.
 */
function fart_load_scripts() {

	// load main stylesheet.
	wp_enqueue_style( 'font-awesome', get_template_directory_uri() . '/css/font-awesome.min.css', array( ) );
	wp_enqueue_style( 'fart-style', get_stylesheet_uri(), array( ) );
	
	wp_enqueue_style( 'fart-fonts', fart_fonts_url(), array(), null );
	
	// Load thread comments reply script
	if ( is_singular() && comments_open() && get_option( 'thread_comments' ) ) {
        wp_enqueue_script( 'comment-reply' );
    }
	
	// Load Utilities JS Script
	wp_enqueue_script( 'fart-js', get_template_directory_uri() . '/js/fart.js', array( 'jquery' ) );
	
	wp_enqueue_script( 'jquery.mobile.customized', get_template_directory_uri() . '/js/jquery.mobile.customized.min.js', array( 'jquery' ) );
	wp_enqueue_script( 'jquery.easing.1.3', get_template_directory_uri() . '/js/jquery.easing.1.3.js', array( 'jquery' ) );
	wp_enqueue_script( 'camera', get_template_directory_uri() . '/js/camera.min.js', array( 'jquery' ) );
}

/**
 *	Load google font url used in the fArt theme
 */
function fart_fonts_url() {

    $fonts_url = '';
 
    /* Translators: If there are characters in your language that are not
    * supported by Questrial, translate this to 'off'. Do not translate
    * into your own language.
    */
    $cantarell = _x( 'on', 'Questrial font: on or off', 'fart' );

    if ( 'off' !== $cantarell ) {
        $font_families = array();
 
        $font_families[] = 'Questrial';
 
        $query_args = array(
            'family' => urlencode( implode( '|', $font_families ) ),
            'subset' => urlencode( 'latin,latin-ext' ),
        );
 
        $fonts_url = add_query_arg( $query_args, '//fonts.googleapis.com/css' );
    }
 
    return $fonts_url;
}

function fart_display_social_sites() {

	echo '<ul class="header-social-widget">';

	$socialURL = get_theme_mod('fart_social_facebook', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Facebook', 'fart') . '" class="facebook16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_google', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Google+', 'fart') . '" class="google16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_twitter', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Twitter', 'fart') . '" class="twitter16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_linkedin', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on LinkeIn', 'fart') . '" class="linkedin16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_instagram', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Instagram', 'fart') . '" class="instagram16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_rss', get_bloginfo( 'rss2_url' ));
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow our RSS Feeds', 'fart') . '" class="rss16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_tumblr', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Tumblr', 'fart') . '" class="tumblr16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_youtube', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Youtube', 'fart') . '" class="youtube16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_pinterest', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Pinterest', 'fart') . '" class="pinterest16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_vk', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on VK', 'fart') . '" class="vk16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_flickr', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Flickr', 'fart') . '" class="flickr16"></a>';
	}

	$socialURL = get_theme_mod('fart_social_vine', '#');
	if ( !empty($socialURL) ) {

		echo '<li><a href="' . esc_url( $socialURL ) . '" title="' . __('Follow us on Vine', 'fart') . '" class="vine16"></a>';
	}

	echo '</ul>';
}

/**
 * Display website's logo image
 */
function fart_show_website_logo_image_and_title() {

	if ( has_custom_logo() ) {

        the_custom_logo();
    }

    $header_text_color = get_header_textcolor();

    if ( 'blank' !== $header_text_color ) {
    
        echo '<div id="site-identity">';
        echo '<a href="' . esc_url( home_url('/') ) . '" title="' . esc_attr( get_bloginfo('name') ) . '">';
        echo '<h1>'.get_bloginfo('name').'</h1>';
        echo '</a>';
        echo '<strong>'.get_bloginfo('description').'</strong>';
        echo '</div>';
    }
}

/**
 *	Displays the copyright text.
 */
function fart_show_copyright_text() {

	$footerText = get_theme_mod('fart_footer_copyright', null);

	if ( !empty( $footerText ) ) {

		echo esc_html( $footerText ) . ' | ';		
	}
}

/**
 *	widgets-init action handler. Used to register widgets and register widget areas
 */
function fart_widgets_init() {
	
	// Register Sidebar Widget.
	register_sidebar( array (
						'name'	 		 =>	 __( 'Sidebar Widget Area', 'fart'),
						'id'		 	 =>	 'sidebar-widget-area',
						'description'	 =>  __( 'The sidebar widget area', 'fart'),
						'before_widget'	 =>  '',
						'after_widget'	 =>  '',
						'before_title'	 =>  '<div class="sidebar-before-title"></div><h3 class="sidebar-title">',
						'after_title'	 =>  '</h3><div class="sidebar-after-title"></div>',
					) );
					
	/**
	 * Add Homepage Columns Widget areas
	 */
	register_sidebar( array (
							'name'			 =>  __( 'Homepage Column #1', 'fart' ),
							'id' 			 =>  'homepage-column-1-widget-area',
							'description'	 =>  __( 'The Homepage Column #1 widget area', 'fart' ),
							'before_widget'  =>  '',
							'after_widget'	 =>  '',
							'before_title'	 =>  '<h2 class="sidebar-title">',
							'after_title'	 =>  '</h2><div class="sidebar-after-title"></div>',
						) );
						
	register_sidebar( array (
							'name'			 =>  __( 'Homepage Column #2', 'fart' ),
							'id' 			 =>  'homepage-column-2-widget-area',
							'description'	 =>  __( 'The Homepage Column #2 widget area', 'fart' ),
							'before_widget'  =>  '',
							'after_widget'	 =>  '',
							'before_title'	 =>  '<h2 class="sidebar-title">',
							'after_title'	 =>  '</h2><div class="sidebar-after-title"></div>',
						) );
						
	register_sidebar( array (
							'name'			 =>  __( 'Homepage Column #3', 'fart' ),
							'id' 			 =>  'homepage-column-3-widget-area',
							'description'	 =>  __( 'The Homepage Column #3 widget area', 'fart' ),
							'before_widget'  =>  '',
							'after_widget'	 =>  '',
							'before_title'	 =>  '<h2 class="sidebar-title">',
							'after_title'	 =>  '</h2><div class="sidebar-after-title"></div>',
						) );
	
	register_sidebar( array (
							'name'			 =>  __( 'Homepage Column #4', 'fart' ),
							'id' 			 =>  'homepage-column-4-widget-area',
							'description'	 =>  __( 'The Homepage Column #4 widget area', 'fart' ),
							'before_widget'  =>  '',
							'after_widget'	 =>  '',
							'before_title'	 =>  '<h2 class="sidebar-title">',
							'after_title'	 =>  '</h2><div class="sidebar-after-title"></div>',
						) );
	
	// Register Footer Column #1
	register_sidebar( array (
							'name'			 =>  __( 'Footer Column #1', 'fart' ),
							'id' 			 =>  'footer-column-1-widget-area',
							'description'	 =>  __( 'The Footer Column #1 widget area', 'fart' ),
							'before_widget'  =>  '',
							'after_widget'	 =>  '',
							'before_title'	 =>  '<h2 class="footer-title">',
							'after_title'	 =>  '</h2><div class="footer-after-title"></div>',
						) );
	
	// Register Footer Column #2
	register_sidebar( array (
							'name'			 =>  __( 'Footer Column #2', 'fart' ),
							'id' 			 =>  'footer-column-2-widget-area',
							'description'	 =>  __( 'The Footer Column #2 widget area', 'fart' ),
							'before_widget'  =>  '',
							'after_widget'	 =>  '',
							'before_title'	 =>  '<h2 class="footer-title">',
							'after_title'	 =>  '</h2><div class="footer-after-title"></div>',
						) );
	
	// Register Footer Column #3
	register_sidebar( array (
							'name'			 =>  __( 'Footer Column #3', 'fart' ),
							'id' 			 =>  'footer-column-3-widget-area',
							'description'	 =>  __( 'The Footer Column #3 widget area', 'fart' ),
							'before_widget'  =>  '',
							'after_widget'	 =>  '',
							'before_title'	 =>  '<h2 class="footer-title">',
							'after_title'	 =>  '</h2><div class="footer-after-title"></div>',
						) );
}

/**
 * Displays the slider
 */
function fart_display_slider() { ?>
	 
	 <div class="camera_wrap camera_emboss" id="camera_wrap">
	 
		<?php
			// display slides
			for ( $i = 1; $i <= 3; ++$i ) {
			
					$defaultSlideContent = __( '<h2>Lorem ipsum dolor</h2><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><a class="btn" title="Read more" href="#">Read more</a>', 'fart' );
					
					$defaultSlideImage = get_template_directory_uri().'/images/slider/' . $i .'.jpg';

					$slideContent = get_theme_mod( 'fart_slide'.$i.'_content', html_entity_decode( $defaultSlideContent ) );
					$slideImage = get_theme_mod( 'fart_slide'.$i.'_image', $defaultSlideImage );
				?>
					<div data-thumb="<?php echo esc_attr( $slideImage ); ?>" data-src="<?php echo esc_attr( $slideImage ); ?>">
						<div class="camera_caption fadeFromBottom">
							<?php echo $slideContent; ?>
						</div>
					</div>
<?php		} ?>
	</div><!-- #camera_wrap -->
<?php 
}

/**
 *	Used to load the content for posts and pages.
 */
function fart_the_content() {

	// Display Thumbnails if thumbnail is set for the post
	if ( has_post_thumbnail() ) {
?>

		<a href="<?php the_permalink(); ?>" title="<?php the_title_attribute(); ?>">
			<?php the_post_thumbnail(); ?>
		</a>
								
<?php
	}

	the_content( __( 'Read More', 'fart') );
}

/**
 *	Displays the single content.
 */
function fart_the_content_single() {

	// Display Thumbnails if thumbnail is set for the post
	if ( has_post_thumbnail() ) {

		the_post_thumbnail();
	}
	the_content( __( 'Read More...', 'fart') );
}

/**
 * Register theme settings in the customizer
 */
function fart_customize_register( $wp_customize ) {

    /**
	 * Add Social Sites Section
	 */
	$wp_customize->add_section(
		'fart_social_section',
		array(
			'title'       => __( 'Social Sites', 'fart' ),
			'capability'  => 'edit_theme_options',
		)
	);
	
	// Add facebook url
	$wp_customize->add_setting(
		'fart_social_facebook',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_facebook',
        array(
            'label'          => __( 'Facebook Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_facebook',
            'type'           => 'text',
            )
        )
	);

	// Add google+ url
	$wp_customize->add_setting(
		'fart_social_google',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_google',
        array(
            'label'          => __( 'Google+ Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_google',
            'type'           => 'text',
            )
        )
	);

	// Add twitter url
	$wp_customize->add_setting(
		'fart_social_twitter',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_twitter',
        array(
            'label'          => __( 'Twitter Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_twitter',
            'type'           => 'text',
            )
        )
	);

	// Add LinkedIn url
	$wp_customize->add_setting(
		'fart_social_linkedin',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_linkedin',
        array(
            'label'          => __( 'LinkedIn Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_linkedin',
            'type'           => 'text',
            )
        )
	);

	// Add Instagram url
	$wp_customize->add_setting(
		'fart_social_instagram',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_instagram',
        array(
            'label'          => __( 'instagram Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_instagram',
            'type'           => 'text',
            )
        )
	);

	// Add RSS Feeds url
	$wp_customize->add_setting(
		'fart_social_rss',
		array(
		    'default'           => get_bloginfo( 'rss2_url' ),
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_rss',
        array(
            'label'          => __( 'RSS Feeds URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_rss',
            'type'           => 'text',
            )
        )
	);

	// Add Tumblr url
	$wp_customize->add_setting(
		'fart_social_tumblr',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_tumblr',
        array(
            'label'          => __( 'Tumblr Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_tumblr',
            'type'           => 'text',
            )
        )
	);

	// Add YouTube channel url
	$wp_customize->add_setting(
		'fart_social_youtube',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_youtube',
        array(
            'label'          => __( 'YouTube channel URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_youtube',
            'type'           => 'text',
            )
        )
	);

	// Add Pinterest page url
	$wp_customize->add_setting(
		'fart_social_pinterest',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_pinterest',
        array(
            'label'          => __( 'Pinterest Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_pinterest',
            'type'           => 'text',
            )
        )
	);

	// Add VK page url
	$wp_customize->add_setting(
		'fart_social_vk',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_vk',
        array(
            'label'          => __( 'VK Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_vk',
            'type'           => 'text',
            )
        )
	);

	// Add Flickr page url
	$wp_customize->add_setting(
		'fart_social_flickr',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_flickr',
        array(
            'label'          => __( 'Flickr Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_flickr',
            'type'           => 'text',
            )
        )
	);

	// Add Vine page url
	$wp_customize->add_setting(
		'fart_social_vine',
		array(
		    'default'           => '#',
		    'sanitize_callback' => 'esc_url_raw',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_social_vine',
        array(
            'label'          => __( 'Vine Page URL', 'fart' ),
            'section'        => 'fart_social_section',
            'settings'       => 'fart_social_vine',
            'type'           => 'text',
            )
        )
	);
	
	/**
	 * Add Slider Section
	 */
	$wp_customize->add_section(
		'fart_slider_section',
		array(
			'title'       => __( 'Slider', 'fart' ),
			'capability'  => 'edit_theme_options',
		)
	);
	
	// Add slide 1 content
	$wp_customize->add_setting(
		'fart_slide1_content',
		array(
		    'default'           => __( '<h2>Lorem ipsum dolor</h2><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><a class="btn" title="Read more" href="#">Read more</a>', 'fart' ),
		    'sanitize_callback' => 'force_balance_tags',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_slide1_content',
        array(
            'label'          => __( 'Slide #1 Content', 'fart' ),
            'section'        => 'fart_slider_section',
            'settings'       => 'fart_slide1_content',
            'type'           => 'textarea',
            )
        )
	);
	
	// Add slide 1 background image
	$wp_customize->add_setting( 'fart_slide1_image',
		array(
			'default' => get_template_directory_uri().'/images/slider/' . '1.jpg',
    		'sanitize_callback' => 'esc_url_raw'
		)
	);

    $wp_customize->add_control( new WP_Customize_Image_Control( $wp_customize, 'fart_slide1_image',
			array(
				'label'   	 => __( 'Slide 1 Image', 'fart' ),
				'section' 	 => 'fart_slider_section',
				'settings'   => 'fart_slide1_image',
			) 
		)
	);
	
	// Add slide 2 content
	$wp_customize->add_setting(
		'fart_slide2_content',
		array(
		    'default'           => __( '<h2>Lorem ipsum dolor</h2><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><a class="btn" title="Read more" href="#">Read more</a>', 'fart' ),
		    'sanitize_callback' => 'force_balance_tags',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_slide2_content',
        array(
            'label'          => __( 'Slide #2 Content', 'fart' ),
            'section'        => 'fart_slider_section',
            'settings'       => 'fart_slide2_content',
            'type'           => 'textarea',
            )
        )
	);
	
	// Add slide 2 background image
	$wp_customize->add_setting( 'fart_slide2_image',
		array(
			'default' => get_template_directory_uri().'/images/slider/' . '2.jpg',
    		'sanitize_callback' => 'esc_url_raw'
		)
	);

    $wp_customize->add_control( new WP_Customize_Image_Control( $wp_customize, 'fart_slide2_image',
			array(
				'label'   	 => __( 'Slide 2 Image', 'fart' ),
				'section' 	 => 'fart_slider_section',
				'settings'   => 'fart_slide2_image',
			) 
		)
	);
	
	// Add slide 3 content
	$wp_customize->add_setting(
		'fart_slide3_content',
		array(
		    'default'           => __( '<h2>Lorem ipsum dolor</h2><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p><a class="btn" title="Read more" href="#">Read more</a>', 'fart' ),
		    'sanitize_callback' => 'force_balance_tags',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_slide3_content',
        array(
            'label'          => __( 'Slide #3 Content', 'fart' ),
            'section'        => 'fart_slider_section',
            'settings'       => 'fart_slide3_content',
            'type'           => 'textarea',
            )
        )
	);
	
	// Add slide 3 background image
	$wp_customize->add_setting( 'fart_slide3_image',
		array(
			'default' => get_template_directory_uri().'/images/slider/' . '3.jpg',
    		'sanitize_callback' => 'esc_url_raw'
		)
	);

    $wp_customize->add_control( new WP_Customize_Image_Control( $wp_customize, 'fart_slide3_image',
			array(
				'label'   	 => __( 'Slide 3 Image', 'fart' ),
				'section' 	 => 'fart_slider_section',
				'settings'   => 'fart_slide3_image',
			) 
		)
	);

	/**
	 * Add Footer Section
	 */
	$wp_customize->add_section(
		'fart_footer_section',
		array(
			'title'       => __( 'Footer', 'fart' ),
			'capability'  => 'edit_theme_options',
		)
	);
	
	// Add footer copyright text
	$wp_customize->add_setting(
		'fart_footer_copyright',
		array(
		    'default'           => '',
		    'sanitize_callback' => 'sanitize_text_field',
		)
	);

	$wp_customize->add_control( new WP_Customize_Control( $wp_customize, 'fart_footer_copyright',
        array(
            'label'          => __( 'Copyright Text', 'fart' ),
            'section'        => 'fart_footer_section',
            'settings'       => 'fart_footer_copyright',
            'type'           => 'text',
            )
        )
	);
}

add_action('customize_register', 'fart_customize_register');

function fart_header_style() {

    $header_text_color = get_header_textcolor();

    if ( ! has_header_image()
        && ( get_theme_support( 'custom-header', 'default-text-color' ) === $header_text_color
             || 'blank' === $header_text_color ) ) {

        return;
    }

    $headerImage = get_header_image();
?>
    <style type="text/css">
        <?php if ( has_header_image() ) : ?>

                #header-main-fixed {background-image: url("<?php echo esc_url( $headerImage ); ?>");}

        <?php endif; ?>


        <?php if ( get_theme_support( 'custom-header', 'default-text-color' ) !== $header_text_color
                    && 'blank' !== $header_text_color ) : ?>

                #header-main-fixed {color: #<?php echo esc_attr( $header_text_color ); ?>;}

        <?php endif; ?>
    </style>
<?php
}

if( function_exists('register_sidebar') ) {
	register_sidebar(array(
		'name' => 'First_sidebar',
		'before_widget' => '',
		'after_widget' => '',
		'before_title' => '<h4>',
		'after_title' => '</h4>'
	));
	register_sidebar(array(
		'name' => 'Second_sidebar',
		'before_widget' => '',
		'after_widget' => '',
		'before_title' => '<h4>',
		'after_title' => '</h4>'
	));
	register_sidebar(array(
		'name' => 'Third_sidebar',
		'before_widget' => '',
		'after_widget' => '',
		'before_title' => '<h4>',
		'after_title' => '</h4>'
	));
	register_sidebar(array(
		'name' => 'Fourth_sidebar',
		'before_widget' => '',
		'after_widget' => '',
		'before_title' => '<h4>',
		'after_title' => '</h4>'
	));
}

/**
 * 让 WordPress 只搜索文章的标题
 */
function __search_by_title_only( $search, &$wp_query )
{
	global $wpdb;
 
	if ( empty( $search ) )
        return $search; // skip processing - no search term in query
 
    $q = $wp_query->query_vars;    
    $n = ! empty( $q['exact'] ) ? '' : '%';
 
    $search =
    $searchand = '';
 
    foreach ( (array) $q['search_terms'] as $term ) {
    	$term = esc_sql( like_escape( $term ) );
    	$search .= "{$searchand}($wpdb->posts.post_title LIKE '{$n}{$term}{$n}')";
    	$searchand = ' AND ';
    }
 
    if ( ! empty( $search ) ) {
    	$search = " AND ({$search}) ";
    	if ( ! is_user_logged_in() )
    		$search .= " AND ($wpdb->posts.post_password = '') ";
    }
 
    return $search;
}
add_filter( 'posts_search', '__search_by_title_only', 500, 2 );

add_filter('pre_get_posts','SearchFilter');
function SearchFilter($query) {
if ($query->is_search) {
$query->set('post_type', 'post');
}
return $query;
}

function search_word_replace($buffer){
    if(is_search()){
        $arr = explode(" ", get_search_query());
        $arr = array_unique($arr);
        foreach($arr as $v)
            if($v)
                $buffer = preg_replace("/(".$v.")/i", "<span style=\"background-color:#ff0;\"><strong>$1</strong></span>", $buffer);
    }
    return $buffer;
}
add_filter("the_title", "search_word_replace", 200);
add_filter("the_excerpt", "search_word_replace", 200);
add_filter("the_content", "search_word_replace", 200);


function catch_that_image() {
      global $post, $posts;
      $first_img = '';
      ob_start();
      ob_end_clean();
      $output = preg_match_all('/<img.+src=[\'"]([^\'"]+)[\'"].*>/i', $post->post_content, $matches);
      $first_img = $matches [1] [0];
      if(empty($first_img)){ //Defines a default image
        $first_img = "/img/uploads/avatar/avatar-195x195.png";
      }
      return $first_img;
    }
	
//pagenavi

function wp_nav( $p = 2 ){  if ( is_singular() ) return;  global $wp_query, $paged;  $max_page = $wp_query->max_num_pages;  if ( $max_page == 1 ) return;  if ( empty( $paged ) ) $paged = 1;  echo '<span class="page-numbers">' . $paged . ' / ' . $max_page . ' </span> ';  if ( $paged > 1 ) p_link( $paged - 1, __('&laquo; Previous'),__('&laquo; Previous') );  if ( $paged > $p + 1 ) p_link( 1, 'First page' );  if ( $paged > $p + 2 ) echo '<span class="page-numbers">...</span>';  for( $i = $paged - $p; $i <= $paged + $p; $i++ ) {    if ( $i > 0 && $i <= $max_page ) $i == $paged ? print "<span class='page-numbers current'>{$i}</span> " : p_link( $i );  }  if ( $paged < $max_page - $p - 1 ) echo '<span class="page-numbers">...</span>';  if ( $paged < $max_page - $p ) p_link( $max_page, 'Last page' );  if ( $paged < $max_page ) p_link( $paged + 1, __('Next &raquo;'), __('Next &raquo;') );}function p_link( $i, $title = '', $linktype = '' ){  if ( $title == '' ) $title = "The {$i} page";  if ( $linktype == '' ) { $linktext = $i; } else { $linktext = $linktype; }  echo "<a class='page-numbers' href='", esc_html( get_pagenum_link( $i ) ), "' title='{$title}'>{$linktext}</a> ";}
    function par_pagenavi($range = 9){  
        global $paged, $wp_query;  
        if ( !$max_page ) {$max_page = $wp_query->max_num_pages;}  
        if($max_page > 1){if(!$paged){$paged = 1;}  
        if($paged != 1){echo "<a href='" . get_pagenum_link(1) . "' class='extend' title='跳转到首页'> 返回首页 </a>";}  
        previous_posts_link(' 上一页 ');  
        if($max_page > $range){  
            if($paged < $range){for($i = 1; $i <= ($range + 1); $i++){echo "<a href='" . get_pagenum_link($i) ."'";  
            if($i==$paged)echo " class='current'";echo ">$i</a>";}}  
        elseif($paged >= ($max_page - ceil(($range/2)))){  
            for($i = $max_page - $range; $i <= $max_page; $i++){echo "<a href='" . get_pagenum_link($i) ."'";  
            if($i==$paged)echo " class='current'";echo ">$i</a>";}}  
        elseif($paged >= $range && $paged < ($max_page - ceil(($range/2)))){  
            for($i = ($paged - ceil($range/2)); $i <= ($paged + ceil(($range/2))); $i++){echo "<a href='" . get_pagenum_link($i) ."'";if($i==$paged) echo " class='current'";echo ">$i</a>";}}}  
        else{for($i = 1; $i <= $max_page; $i++){echo "<a href='" . get_pagenum_link($i) ."'";  
        if($i==$paged)echo " class='current'";echo ">$i</a>";}}  
        next_posts_link(' 下一页 ');  
        if($paged != $max_page){echo "<a href='" . get_pagenum_link($max_page) . "' class='extend' title='跳转到最后一页'> 最后一页 </a>";}}  
    }  
	
	

function custom_the_views($post_id, $echo=true, $views='人阅读') {
        $count_key = 'views';  
        $count = get_post_meta($post_id, $count_key, true);  
        if ($count == '') {  
            delete_post_meta($post_id, $count_key);  
            add_post_meta($post_id, $count_key, '0');  
            $count = '0';  
        }  
        if ($echo)  
            echo number_format_i18n($count) . $views;  
        else  
            return number_format_i18n($count) . $views;  
    }  
    function set_post_views() {  
        global $post;  
        $post_id = $post->ID;  
        $count_key = 'views';  
        $count = get_post_meta($post_id, $count_key, true);  
        if (is_single() || is_page()) {  
            if ($count == '') {  
                delete_post_meta($post_id, $count_key);  
                add_post_meta($post_id, $count_key, '0');  
            } else {  
                update_post_meta($post_id, $count_key, $count + 1);  
            }  
        }  
    }  
    add_action('get_header', 'set_post_views');  


function my_login_redirect($redirect_to, $request){
if( empty( $redirect_to ) || $redirect_to == 'wp-admin/' || $redirect_to == admin_url() )
return home_url("");
else
return $redirect_to;
}
add_filter("login_redirect", "my_login_redirect", 10, 3);

	
//隐藏版本号
function wpbeginner_remove_version() {
return '';
}
add_filter('the_generator', 'wpbeginner_remove_version');

add_action('wp_ajax_nopriv_bigfa_like', 'bigfa_like');
add_action('wp_ajax_bigfa_like', 'bigfa_like');
function bigfa_like(){
    global $wpdb,$post;
    $id = $_POST["um_id"];
    $action = $_POST["um_action"];
    if ( $action == 'ding'){
		$bigfa_raters = get_post_meta($id,'bigfa_ding',true);
		$expire = time() + 99999999;
		$domain = ($_SERVER['HTTP_HOST'] != 'localhost') ? $_SERVER['HTTP_HOST'] : false; // make cookies work with localhost
		setcookie('bigfa_ding_'.$id,$id,$expire,'/',$domain,false);
		if (!$bigfa_raters || !is_numeric($bigfa_raters)) {
			update_post_meta($id, 'bigfa_ding', 1);
		}else {
			update_post_meta($id, 'bigfa_ding', ($bigfa_raters + 1));
		}   
		echo get_post_meta($id,'bigfa_ding',true);    
    }     
    die;
}
add_theme_support('post-thumbnails');		


//在24小时以内发布的显示为几分钟前或几小时前 
function timeago() { 
 global $post; 
 $date = $post->post_date; 
 $time = get_post_time('G', true, $post); 
 $time_diff = time() - $time; 
 if ( $time_diff > 0 && $time_diff < 24*60*60 ) 
 $display = sprintf( __('%s 之前'), human_time_diff( $time ) ); 
 else 
 $display = date(get_option('date_format'), strtotime($date) ); 
 
 return $display; 
} 
 
add_filter('the_time', 'timeago');


//搜索结果排除某些分类的文章
function Bing_search_filter_category( $query) {
	if ( !$query->is_admin && $query->is_search) {
		$query->set('cat','-11,-3,-5,-6'); //分类的ID，前面加负号表示排除；如果直接写ID，则表示只在该ID中搜索
	}
	return $query;
}
add_filter('pre_get_posts','Bing_search_filter_category');




function aurelius_comment($comment, $args, $depth) 
{
   $GLOBALS['comment'] = $comment; ?>

            <div class="padd-box-sm clear-mrg">
                <div class="ref-box brd-btm hreview" id="comment-<?php comment_ID(); ?>">
                    <div class="ref-avatar" id="li-comment-<?php comment_ID(); ?>">
                        <?php if (function_exists('get_avatar') && get_option('show_avatars')) { echo get_avatar($comment, 48); } ?>
                    </div>

                    <div class="ref-info" id="li-comment-<?php comment_ID(); ?>">
                        <div class="ref-author" id="li-comment-<?php comment_ID(); ?>">
                            <strong><?php echo get_comment_author(); ?></strong>
                            <span><?php echo get_comment_time('Y-m-d H:i'); ?>  （<?php comment_reply_link(array_merge( $args, array('reply_text' => '回复','depth' => $depth, 'max_depth' => $args['max_depth']))) ?>）</span>
                        </div>
                            <p>
				<?php comment_text(); ?>
                            </p><hr>
                    </div>
					
                </div><!-- .ref-box -->

            </div><!-- .padd-box-sm -->
<?php } 	

// 禁用修订版本，2015年3月5日更新
add_filter( 'wp_revisions_to_keep', 'specs_wp_revisions_to_keep', 10, 2 );
function specs_wp_revisions_to_keep( $num, $post ) {
   if ( 'post_type' == $post->post_type )
      $num = 0;

   return $num;
}

// 后台禁用Google Open Sans字体，加速网站
add_filter( 'gettext_with_context', 'wpdx_disable_open_sans', 888, 4 );
function wpdx_disable_open_sans( $translations, $text, $context, $domain ) {
  if ( 'Open Sans font: on or off' == $context && 'on' == $text ) {
    $translations = 'off';
  }
  return $translations;
}

/* 获取当前页面url
/* ---------------- */
function tin_get_current_page_url(){
	$ssl = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] == 'on') ? true:false;
    $sp = strtolower($_SERVER['SERVER_PROTOCOL']);
    $protocol = substr($sp, 0, strpos($sp, '/')) . (($ssl) ? 's' : '');
    $port  = $_SERVER['SERVER_PORT'];
    $port = ((!$ssl && $port=='80') || ($ssl && $port=='443')) ? '' : ':'.$port;
    $host = isset($_SERVER['HTTP_X_FORWARDED_HOST']) ? $_SERVER['HTTP_X_FORWARDED_HOST'] : isset($_SERVER['HTTP_HOST']) ? $_SERVER['HTTP_HOST'] : $_SERVER['SERVER_NAME'];
    return $protocol . '://' . $host . $port . $_SERVER['REQUEST_URI'];
}
 
/* AJAX登录变量
/* -------------- */
function ajax_sign_object(){
	$object = array();
	$object[redirecturl] = tin_get_current_page_url();
	$object[ajaxurl] = admin_url( '/admin-ajax.php' );
	$object[loadingmessage] = '正在请求中，请稍等...';
	$object_json = json_encode($object);
	return $object_json;
}
 
/* AJAX登录验证
/* ------------- */
function tin_ajax_login(){
	$result	= array();
	if(isset($_POST['security']) && wp_verify_nonce( $_POST['security'], 'security_nonce' ) ){
		$creds = array();
		$creds['user_login'] = $_POST['username'];
		$creds['user_password'] = $_POST['password'];
		$creds['remember'] = ( isset( $_POST['remember'] ) ) ? $_POST['remember'] : false;
		$login = wp_signon($creds, false);
		if ( ! is_wp_error( $login ) ){
			$result['loggedin']	= 1;
		}else{
			$result['message']	= ( $login->errors ) ? strip_tags( $login->get_error_message() ) : '<strong>ERROR</strong>: ' . esc_html__( '请输入正确用户名和密码以登录', 'tinection' );
		}
	}else{
		$result['message'] = __('安全认证失败，请重试！','tinection');
	}
	header( 'content-type: application/json; charset=utf-8' );
	echo json_encode( $result );
	exit;
 
}
add_action( 'wp_ajax_ajaxlogin', 'tin_ajax_login' );
add_action( 'wp_ajax_nopriv_ajaxlogin', 'tin_ajax_login' );
 
/* AJAX注册验证
/* ------------- */
function tin_ajax_register(){
	$result	= array();
	if(isset($_POST['security']) && wp_verify_nonce( $_POST['security'], 'user_security_nonce' ) ){
		$user_login = sanitize_user($_POST['username']);
		$user_pass = $_POST['password'];
		$user_email	= apply_filters( 'user_registration_email', $_POST['email'] );
		$errors	= new WP_Error();
		if( ! validate_username( $user_login ) ){
			$errors->add( 'invalid_username', __( '请输入一个有效用户名','tinection' ) );
		}elseif(username_exists( $user_login )){
			$errors->add( 'username_exists', __( '此用户名已被注册','tinection' ) );
		}elseif(email_exists( $user_email )){
			$errors->add( 'email_exists', __( '此邮箱已被注册','tinection' ) );
		}
		do_action( 'register_post', $user_login, $user_email, $errors );
		$errors = apply_filters( 'registration_errors', $errors, $user_login, $user_email );
		if ( $errors->get_error_code() ){
			$result['success']	= 0;
			$result['message'] 	= $errors->get_error_message();
 
		} else {
			$user_id = wp_create_user( $user_login, $user_pass, $user_email );
			if ( ! $user_id ) {
				$errors->add( 'registerfail', sprintf( __( '无法注册，请联系管理员','tinection' ), get_option( 'admin_email' ) ) );
				$result['success']	= 0;
				$result['message'] 	= $errors->get_error_message();		
			} else{
				update_user_option( $user_id, 'default_password_nag', true, true ); //Set up the Password change nag.
				wp_new_user_notification( $user_id, $user_pass );	
				$result['success']	= 1;
				$result['message']	= esc_html__( '注册成功','tinection' );
				//自动登录
				wp_set_current_user($user_id);
  				wp_set_auth_cookie($user_id);
  				$result['loggedin']	= 1;
			}
 
		}	
	}else{
		$result['message'] = __('安全认证失败，请重试！','tinection');
	}
	header( 'content-type: application/json; charset=utf-8' );
	echo json_encode( $result );
	exit;	
}
add_action( 'wp_ajax_ajaxregister', 'tin_ajax_register' );
add_action( 'wp_ajax_nopriv_ajaxregister', 'tin_ajax_register' );


function custom_loginlogo() {
echo '<style type="text/css">
h1 a {background-image: url('.get_bloginfo('template_directory').'/img/uploads/avatar/avatar-195x195.png) !important; }
</style>';
}
add_action('login_head', 'custom_loginlogo');

?>