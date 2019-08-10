<?php
include_once('./_common.php');


// JSON 헤더 설정 ##############################################################

header('Content-type: application/json; charset=utf-8');
header("Expire: -1");
header("Cache-Control: no-cache");
header("Pragma: no-cache");

// IP 검증 #####################################################################

// 전송받은 데이터 #############################################################

// $_REQUEST == Array
// (
//     [code] => 1001
//     [currencyCoinNum] => 1000.00000000
//     [subCurrency] => 0.90
//     [customParams] => {\"point\":\"1000\",\"tid\":\"BitcomPay-ORN-191550733993160\",\"title\":\"1000포인트\",\"oid\":\"POINT_1000\"}
//     [paymentCoinName] => USDT
//     [sign] => FECAA2FAA7836788E138A0510BE2320A
//     [orderNum] => 191550733993160
//     [message] => Successful transaction
// )

// 파라메터 변수화(stdClass Object)
$cParams = json_decode(str_replace('\\', '', $_REQUEST['customParams']));

// $cParams == stdClass Object
// (
//     [point] => 1000
//     [tid] => BitcomPay-ORN-191550733993160
//     [title] => 1000포인트
//     [oid] => POINT_1000
// )

// 포인트 부여
if (!insert_point($cParams->mid, $cParams->point, '포인트충전 - PayBank', '@member', $cParams->mid, 'PayBank-'.$cParams->tid)) {
	die(json_encode($result));
}

die(print('{"data": "SUCCESS"}'));
?>
