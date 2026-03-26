package com.ute.foodiedash.infrastructure.payment.vnpay.adapter;

import com.ute.foodiedash.application.paymentmethod.port.PaymentPort;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import com.ute.foodiedash.infrastructure.payment.vnpay.config.VnpayProperties;
import com.ute.foodiedash.infrastructure.payment.vnpay.utils.VnpayUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class VnpayAdapter implements PaymentPort {
    private final VnpayProperties props;

    @Override
    public PaymentMethodCode getCode() {
        return PaymentMethodCode.VNPAY;
    }

    @Override
    public String createPaymentUrl(Long orderId, long amount) {
        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String vnpTxnRef = String.valueOf(orderId);
        String vnpIpAddr = "127.0.0.1";

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", props.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", props.getReturnUrl());
        vnpParams.put("vnp_IpAddr", vnpIpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        String hashData = VnpayUtils.buildHashData(vnpParams);
        String query = VnpayUtils.buildQuery(vnpParams);
        String vnpSecureHash = VnpayUtils.hmacSHA512(props.getHashSecret(), hashData);
        return props.getUrl() + "?" + query + "&vnp_SecureHash=" + vnpSecureHash;
    }
}
