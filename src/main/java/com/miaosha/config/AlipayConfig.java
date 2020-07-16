package com.miaosha.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2018050502638388";
	//2088022650123818
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDYjdzCwbdC9rlBcf6Sx9VuXz2njtebsMKkTxicfwlGJdnmMXLHuwxaB1Bw+myDy7e+S2wghWMXwGvjVd7VxDiMGdznSIHnp4sAhe18qazNc8yWLQb33vxwZupxXBlWvqZF6LoeXMgVKxwxIDrEgHPO7KJ9z7DtImu/3gLBC/QpusWHJyco9p4hN4fIOW/Hr6xKbS9avEFhhrmacLxloEKBTCQz9nPFb9C6E+OX0FWojbCiPcIrBUUKxrwHkvjibO4lmYhWiHdqjQ02loqc6gO5zDZSS9WQrEDjLdpRoQWGlXKTw4Nie1ID0ER130T+7UW9aGw2BMEkr649rcTHWb5HAgMBAAECggEAbry0fB8lQKf7+dx6KsLngGZRl+JynNnZ3r3XvwczTdzDPXRh1oLvAQameu+EGC1Pi47z/vlWv+mVAg6CQdvycqozDU5Fp5lEeoMB+3Kku/cQUmc6Hfy4KB5omolXJ8WcLzmgGzCk3DZMV0683ok1L0ZQnXdcaRbl9Jpp3B/pvoAjqYqxP7b+I2HrzsRBb5wHpa8cdc9KmanggQHCrKLDqdpvWHXqRZIh7in3GxsO7Q1J7DrMa2Rqof1YlawKNuy/pHclX9Njiv47iGE50fA7kPQYlzFbG8OgE4urQYhQRvpj3HtWJc7kAONksmoXEhDCd6VjyrjTIQHmQPKSwmINEQKBgQD4G2kLvdaoi2RRzK72710JHiasseW5wQJ13nwXpzzG+SRliJoR2f8jYFqsqeyjLZnj3LsfwYkTqA5bTmHFzBQttqXdpivTKj5JCDyS4N8cUfThSyzRCiDubDArwWiB6KEsdfaKUi/wHF09LKq8wWjeLMm6CCWAWMtPL2rZoMzeuQKBgQDfcXv6uJ5vvhTJ6yHZYL21eFozwjlGFhMWIZxytNQ+n24U4721+T+Aj9CYTvhUVLXy0QoWuh08Bf4fogDqV8/MaNHIOn0sLG7kJwr9BPRtOJ7YtCYM/QwhFgxtJJXYxb1f95juslggHNGboik1LqFjy77aJQlxiJAwf4vo57UE/wKBgQCYkOJrofUZ931c/6ynlFF1heDqWQqWyyUkLgfI08H/6LyE4xc62v6VVm7rtiIZxbTX9afrwyrzt2f8pZph8ziFU8dzIPfaBiP+7N2yxCzuUUdwMrYAZWYSttx4S5pkKBTULkXIkPmBgS+kqo7jO1utI4V4rAHDhtaeft4JrWPNoQKBgQCaZ+ms0Hdr8IadIgc6GEgAZzKuIcFCVW7LFF7wRVWGENYZSWeHoL3pMXiMR8YDQKabLSkzm1koaVu6sFDBn/zQ3H5bR0/IfswCjZhiUFI4R7A9QAAraZJAKuRbZ7TFH6BF+OuNx2BukHuoeN7RCXAtblZsmLt14VECsqEUSUomMQKBgEXxmuJdFwOtqsbxfTIw23iB8ajdXh0WfY3LDKPziol/vjud5gc1MzJIgolgVjStFqsf1UzSYofmXFVVUCUS6QotcaLbfb4Uwla8MPJbirtqEx98Taoo3NI8vDwxCZpE3BJbmaeR0J0BRE/GLEr+odnIERoJTlu1PMitkkjOUHsV" ;
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2I3cwsG3Qva5QXH+ksfVbl89p47Xm7DCpE8YnH8JRiXZ5jFyx7sMWgdQcPpsg8u3vktsIIVjF8Br41Xe1cQ4jBnc50iB56eLAIXtfKmszXPMli0G9978cGbqcVwZVr6mRei6HlzIFSscMSA6xIBzzuyifc+w7SJrv94CwQv0KbrFhycnKPaeITeHyDlvx6+sSm0vWrxBYYa5mnC8ZaBCgUwkM/ZzxW/QuhPjl9BVqI2woj3CKwVFCsa8B5L44mzuJZmIVoh3ao0NNpaKnOoDucw2UkvVkKxA4y3aUaEFhpVyk8ODYntSA9BEdd9E/u1FvWhsNgTBJK+uPa3Ex1m+RwIDAQAB"  ;

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8080/alipay/update_order";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8080/alipay/update_order";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

