package com.ute.foodiedash.infrastructure.payment.vnpay.adapter;

import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import com.ute.foodiedash.infrastructure.payment.vnpay.config.VnpayProperties;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.ute.foodiedash.infrastructure.payment.vnpay.utils.VnpayUtils.hmacSHA512;
import static org.assertj.core.api.Assertions.assertThat;

class VnpayAdapterTest {

    private static final DateTimeFormatter VNP_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Test
    void getCode_shouldReturnVnpay() {
        VnpayAdapter adapter = new VnpayAdapter(testVnpayProperties());
        assertThat(adapter.getCode()).isEqualTo(PaymentMethodCode.VNPAY);
    }

    @Test
    void createPaymentUrl_shouldBuildExpectedQueryAndSecureHash() {
        VnpayProperties props = testVnpayProperties();
        VnpayAdapter adapter = new VnpayAdapter(props);

        long amount = 10200L;
        Long orderId = 23L;
        String url = adapter.createPaymentUrl(orderId, amount);

        assertThat(url).startsWith(props.getUrl() + "?");

        Map<String, String> params = parseQueryParams(url);

        // Required params exist
        assertThat(params)
                .containsEntry("vnp_Version", "2.1.0")
                .containsEntry("vnp_Command", "pay")
                .containsEntry("vnp_TmnCode", props.getTmnCode())
                .containsEntry("vnp_CurrCode", "VND")
                .containsEntry("vnp_TxnRef", String.valueOf(orderId))
                .containsEntry("vnp_OrderInfo", "Thanh toan don hang " + orderId)
                .containsEntry("vnp_OrderType", "other")
                .containsEntry("vnp_Locale", "vn")
                .containsEntry("vnp_ReturnUrl", props.getReturnUrl())
                .containsEntry("vnp_IpAddr", "127.0.0.1");

        // Amount is multiplied by 100 (VNPay minor units)
        assertThat(params.get("vnp_Amount")).isEqualTo(String.valueOf(amount * 100));

        // Dates exist and expire is 15 minutes after create
        LocalDateTime createDate = LocalDateTime.parse(params.get("vnp_CreateDate"), VNP_TIME);
        LocalDateTime expireDate = LocalDateTime.parse(params.get("vnp_ExpireDate"), VNP_TIME);
        assertThat(Duration.between(createDate, expireDate)).isEqualTo(Duration.ofMinutes(15));

        // Secure hash matches
        String actualSecureHash = params.get("vnp_SecureHash");
        assertThat(actualSecureHash).isNotBlank();

        String expectedHashData = buildHashDataExcludingSecureHash(params);
        String expectedSecureHash = hmacSHA512(props.getHashSecret(), expectedHashData);
        assertThat(actualSecureHash).isEqualTo(expectedSecureHash);
    }

    private static VnpayProperties testVnpayProperties() {
        VnpayProperties props = new VnpayProperties();
        props.setTmnCode("F8HQIZN4");
        props.setHashSecret("H9E966FA0MM8ODKY8PNQC8EMQCFSON5G");
        props.setUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
        props.setReturnUrl("http://localhost:8080/api/payment/vnpay/return");
        props.setIpnUrl("http://localhost:8080/api/payment/vnpay/ipn");
        return props;
    }

    private static Map<String, String> parseQueryParams(String url) {
        int idx = url.indexOf('?');
        assertThat(idx).isGreaterThanOrEqualTo(0);
        String query = url.substring(idx + 1);

        Map<String, String> out = new HashMap<>();
        for (String part : query.split("&")) {
            int eq = part.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            String key = URLDecoder.decode(part.substring(0, eq), StandardCharsets.US_ASCII);
            String value = URLDecoder.decode(part.substring(eq + 1), StandardCharsets.US_ASCII);
            out.put(key, value);
        }
        return out;
    }

    /**
     * Rebuild hashData exactly like {@link VnpayAdapter} does:
     * sort keys and concatenate key=value with '&', excluding vnp_SecureHash.
     */
    private static String buildHashDataExcludingSecureHash(Map<String, String> params) {
        TreeMap<String, String> sorted = new TreeMap<>();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if ("vnp_SecureHash".equals(e.getKey())) {
                continue;
            }
            sorted.put(e.getKey(), e.getValue());
        }

        List<String> keys = new ArrayList<>(sorted.keySet());

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sorted.get(key);
            if (value == null || value.isEmpty()) {
                continue;
            }
            hashData.append(key).append('=').append(value);
            if (i < keys.size() - 1) {
                hashData.append('&');
            }
        }
        return hashData.toString();
    }
}

