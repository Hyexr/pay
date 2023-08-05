<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>支付</title>
</head>

<body>
<div id="myqrcode"></div>
<div id="orderId" hidden >${orderId}</div>
<div id="return" >${returnUrl}</div>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    jQuery('#myqrcode').qrcode({
        text : "${codeUrl}"
    });

    $(function () {
        setInterval(function () {
            console.log('轮询');
            $.ajax({
                url: "/pay/queryByOrderId",
                data: {
                    orderId: $('#orderId').text()

                },
                success: function (result) {
                    console.log(result)
                    if(result.platformNumber != null && result.platformStatus === 'SUCCESS'){
                        console.log(1)
                        // console.log($('#returnUrl').text())
                        location.href = "${returnUrl}"
                    }
                },
                error: function (result) {
                    alert('支付失败');
                }
            })
        }, 3000)
    })
</script>
</body>
</html>
