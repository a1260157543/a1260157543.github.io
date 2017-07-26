<?php
/**
 * WordPress基础配置文件。
 *
 * 这个文件被安装程序用于自动生成wp-config.php配置文件，
 * 您可以不使用网站，您需要手动复制这个文件，
 * 并重命名为“wp-config.php”，然后填入相关信息。
 *
 * 本文件包含以下配置选项：
 *
 * * MySQL设置
 * * 密钥
 * * 数据库表名前缀
 * * ABSPATH
 *
 * @link https://codex.wordpress.org/zh-cn:%E7%BC%96%E8%BE%91_wp-config.php
 *
 * @package WordPress
 */

// ** MySQL 设置 - 具体信息来自您正在使用的主机 ** //
/** WordPress数据库的名称 */
define('DB_NAME', 'linyynam_1');

/** MySQL数据库用户名 */
define('DB_USER', 'linyynam_1');

/** MySQL数据库密码 */
define('DB_PASSWORD', 'ZiK6LoCj');

/** MySQL主机 */
define('DB_HOST', 'localhost');

/** 创建数据表时默认的文字编码 */
define('DB_CHARSET', 'utf8mb4');

/** 数据库整理类型。如不确定请勿更改 */
define('DB_COLLATE', '');

/**#@+
 * 身份认证密钥与盐。
 *
 * 修改为任意独一无二的字串！
 * 或者直接访问{@link https://api.wordpress.org/secret-key/1.1/salt/
 * WordPress.org密钥生成服务}
 * 任何修改都会导致所有cookies失效，所有用户将必须重新登录。
 *
 * @since 2.6.0
 */
define('AUTH_KEY',         'rhy,4_@95S4|,HI1w|l`;&X%qWvK-Z]4xXC_kk1n*mj}VgQVFn35JHLD8Ef %OET');
define('SECURE_AUTH_KEY',  'NZVGp(4mvZhOZgWBnfgN|(44Jcn!U|QhA$~} cM?7tr,:rls-OqWipE70Z}l(R{s');
define('LOGGED_IN_KEY',    '!>3BUEo[X3x.7Tj/(o$v13^v.Y3dW4Fmr_N#_6N>/B8q.E$~OSdt58l=gMU&(Q:F');
define('NONCE_KEY',        ';*lo(8a8co1Y,KLhTxOnXrLjYLhX$^!Y*V.VCr pf5ww&sP_Lo;(kRv&.,3yV??e');
define('AUTH_SALT',        'ygXNDxMn6h06}Y.|oKEn!;Bg+WLEA5.@(|4S{8%4F!<Sa9(zFfdsS[Gu.xTKy)16');
define('SECURE_AUTH_SALT', 'gA(@f%{TMj7&5YQ1B[RS={G;dw,VTfO$n>VQNg7]_Y4tA2nB]+[;?Og/qSu4oDUz');
define('LOGGED_IN_SALT',   'Z6#,PH^dOogZ*?{Ie!5iaJ!3~O~C6ND|M,/>F<}0!!sWIBefcT}OlM,P6?LS`(~D');
define('NONCE_SALT',       'P{RrFW}Tn yEAc45KngF6|Zm^yat/A^4L:r<s,NecfIdr}Ui^wCHtg#p2H|XA*_#');

/**#@-*/

/**
 * WordPress数据表前缀。
 *
 * 如果您有在同一数据库内安装多个WordPress的需求，请为每个WordPress设置
 * 不同的数据表前缀。前缀名只能为数字、字母加下划线。
 */
$table_prefix  = 'wp_';

/**
 * 开发者专用：WordPress调试模式。
 *
 * 将这个值改为true，WordPress将显示所有用于开发的提示。
 * 强烈建议插件开发者在开发环境中启用WP_DEBUG。
 *
 * 要获取其他能用于调试的信息，请访问Codex。
 *
 * @link https://codex.wordpress.org/Debugging_in_WordPress
 */
define('WP_DEBUG', false);

/**
 * zh_CN本地化设置：启用ICP备案号显示
 *
 * 可在设置→常规中修改。
 * 如需禁用，请移除或注释掉本行。
 */
define('WP_ZH_CN_ICP_NUM', true);

/* 好了！请不要再继续编辑。请保存本文件。使用愉快！ */

/** WordPress目录的绝对路径。 */
if ( !defined('ABSPATH') )
	define('ABSPATH', dirname(__FILE__) . '/');

/** 设置WordPress变量和包含文件。 */
require_once(ABSPATH . 'wp-settings.php');
