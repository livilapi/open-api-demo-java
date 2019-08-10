<?php
if (!defined('_GNUBOARD_')) exit; // 개별 페이지 접근 불가
?>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.0/css/bootstrap.css"/>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<style type="text/css">
.btn_app_download {display:inline-block;height:40px;margin-bottom:5px;}
.btn_app_download img {height:100%;}
</style>

<div id="point" class="new_win">
    <h1 id="win_title" style="margin-top: 0;"><?php echo $g5['title'] ?></h1>

    <div class="tbl_head01 tbl_wrap">
		<ul style="list-style:none;margin:0;padding:0;line-height:2em;">
			<li>- 빗컴페이 앱를 이용해서 결제할 수 있습니다.</li>
			<li>- 빗컴페이 앱을 설치하시기 바랍니다.</li>
			<li>
				<a href="https://play.google.com/store/apps/details?id=com.ffk.originalbitcom" target="_blank" class="btn_app_download"><img src="img/btn_app_download_google.png" alt="Google Play"/></a>
				<!-- <a href="https://itunes.apple.com/app/" target="_blank" class="btn_app_download"><img src="img/btn_app_download_apple.png" alt="Apple App Store"/></a> -->
			</li>
		</ul>
</div>

<div class="container">
    <div class="row" style="padding-bottom: 15px;">
				<div class="col-xs-12" style="text-align: center;">
          <img src="img/dummy_qr_code.jpg" id="imgSrc" style="width: 220px;border: 1px solid #dcdcdc;"/>
        </div>
		</div>
    <div class="row">
        <div class="col-xs-12" style="border-left: 1px solid #cccccc; text-align: center;">
            <form id="defaultForm" method="post" class="form-horizontal">

                <div class="form-group">
                    <div class="col-lg-8">
                        <input  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"  type="text" class="form-control" name="price" id="price" placeholder="충전하실 금액(원)을 입력하세요" style="height: 50px;">
            
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-lg-9 col-lg-offset-3">
                        <button type="submit" class="btn btn-primary" style="height: 50px; width: 100%; font-size: 18px;">충전하기</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">

$('[type="submit"]').on('click', function (e) {
	e.preventDefault();

	submit();
});

async function submit() {
	
	try {
		var now = new Date();
		var params = {
			"appKey": 9747629, // ACCESS KEY
			"coinId": 4, // 2(BS), 3(ETH), 4(USDT), 5(BTC), 6(BCH), 7(XRP) // Choose the coin merchant want to receive.
			"currencyType": 3, // 1(CNY), 2(USD), 3(KRW) // The type of legal currency for the item price.
			"customParams": '',
			"itemName": "POINT", // ITEM NAME
			"orderNum": String(now.getFullYear()).substring(2) + now.getTime(), // This is a 15-digit order number that allows the merchant to distinguish an order.
			"price": $('#price').val(), // Item price based on specified legal currency.
			"requestTimeStr": timestampToTime(Date.parse(now)) // It's time to order. NOTE: Use the merchant's time zone.
		}
		params.customParams = JSON.stringify({
			"point": params.price,
			"tid": 'ORN-' + params.orderNum,
			"title": params.price + '포인트',
			"oid": 'POINT_' + params.price,
			"mid": '<?=$member['mb_id']?>'
		})
		params.sign = await $.ajax({
			"cache": true,
			"type": "POST",
			"url": "./gererate_signature.php",
			"data": params
		});

		$.ajax({
			cache: true,
			type: "POST",
			url:  "http://payapi.excatch.com/api/bus/qrcode/create", // Test API Server
			// url:  "https://payapi.bitcom.com/api/bus/qrcode/create", // Real Operating API Server
			data: params,
			error: function (jqXHR, exception) {
				console.error(jqXHR, exception);
			},
			success: function(response) {
				var res = response;
				
				if (res.code === 200) {
					$("#imgSrc").attr('src',"data:image/jpeg;base64,"+res.qrcodeData)
					document.getElementById("demo_code").innerHTML = JSON.stringify(res);
				} else {
					alert(res.msg);
				}
			}
		});

		setInterval(async () => {
			var sReceipt = await $.get('./polling_receipt.php?ORN=' + params.orderNum);
			if (sReceipt == 'BitcomPay-ORN-' + params.orderNum) {
				location.href = '/bbs/point.php';
			} 
		}, 1*1000);
	} catch (exception) {
		console.error(exception);
	}
}

function timestampToTime(timestamp) {
    let date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    let D = date.getDate()<10?'0'+date.getDate() + ' ':date.getDate() + ' ';
    let h = date.getHours()<10?'0'+date.getHours() + ':':date.getHours() + ':';
    let m = date.getMinutes()<10?'0'+date.getMinutes() + ':':date.getMinutes() + ':';
    let s = date.getSeconds()<10?'0'+date.getSeconds():date.getSeconds();
    return Y+M+D+h+m+s;
}
</script>
