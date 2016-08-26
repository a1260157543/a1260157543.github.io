<?php
$link =mysql_connect("localhost","root","root")or die ("mysql链接失败"); 
mysql_select_db("myadd", $link);
$exec="insert into php_user(id,name,num,pw,pwqr,zy,phone,email,copy) values (null,'".$_POST["name"]."'
	,'".$_POST["num"]."'
	,'".$_POST["pw"]."'
	,'".$_POST["pwqr"]."'
	,'".$_POST["zy"]."'
	,'".$_POST["phone"]."'
	,'".$_POST["email"]."'
	,'".$_POST["copy"]."')"; 
mysql_query("SET NAMES UTF8");
mysql_query($exec, $link);
mysql_close($link);
echo "add success!";
?>