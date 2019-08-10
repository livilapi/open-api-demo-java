<?php
include_once('./_common.php');

if ($is_guest)
    alert_close('회원만 접근하실 수 있습니다.');

$g5['title'] = '포인트 충전';
include_once(G5_PATH.'/head.sub.php');

include_once('_config.php');
include_once('point_popup.skin.php');

include_once(G5_PATH.'/tail.sub.php');
