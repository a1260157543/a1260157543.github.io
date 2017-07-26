<?php
    if (isset($_SERVER['SCRIPT_FILENAME']) && 'comments.php' == basename($_SERVER['SCRIPT_FILENAME']))
        die ('Please do not load this page directly. Thanks!');
?>
<br>

	<?php wp_list_comments('type=comment&callback=aurelius_comment');?>
<div id="cancel_comment_reply"><?php cancel_comment_reply_link() ?></div>
<?php if ( get_option('comment_registration') && !$user_ID ) : ?>
	<p><?php printf(__('You must be <a href="%s">logged in</a> to post a comment.'), get_option('siteurl')."/wp-login.php?redirect_to=".urlencode(get_permalink()));?></p>

<?php else : ?>
<div class="padd-box-sm">
        <form action="<?php echo get_option('siteurl'); ?>/wp-comments-post.php" method="post" id="commentform" name="commentform">
<?php if ( $user_ID ) : ?>
			<p><?php printf(__('用 (%s) 发表想法.'), '<a href="'.get_option('siteurl').'/wp-admin/profile.php">'.$user_identity.'</a>'); ?>
				<a href="<?php echo wp_logout_url(get_permalink()); ?>" title="<?php _e('Log out of this account') ?>"><?php _e(' Log out &raquo;'); ?></a>
			</p>
		<?php else : ?>
            <div class="form-group">
                <label class="form-label" for="author">你 的 昵 称</label>
                <div class="form-item-wrap">
                    <input name="author" id="author" class="form-item" type="text" value="小羊羔">
                </div>
            </div>

            <!--<div class="form-group">
                <label class="form-label" for="email">你 的 标 题</label>
                <div class="form-item-wrap">
                    <input name="url" id="url" class="form-item" type="text">
                </div>
            </div>-->

            <div class="form-group"> 
                <label class="form-label" for="url">你 的 邮 箱</label>
                <div class="form-item-wrap">
                    
					<input name="email" id="email" class="form-item" type="email" required="required">
                </div>
            </div>
				<?php endif; ?>
            <div class="form-group">
                <label class="form-label" for="comment">你 的 观 点</label>
                <div class="form-item-wrap">
                    <textarea name="comment" id="comment" rows="5" tabindex="4" class="form-item"></textarea>
                </div>
            </div>

            <div class="form-submit form-item-wrap">

				<input class="btn btn-primary btn-lg" type="submit" id="submit" value="提 交 评 论">
				<?php comment_id_fields(); ?>
				<?php do_action('comment_form', $post->ID); ?>
            </div>
        </form>
    </div>

<?php endif; ?>

