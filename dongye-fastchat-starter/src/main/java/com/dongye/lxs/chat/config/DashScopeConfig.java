package com.dongye.lxs.chat.config;

import com.alibaba.dashscope.app.ApplicationParam;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * &#064;Description:  百炼平台 apiKey appid配置文件
 * &#064;Date:  2024/7/27 15:00
 * &#064;Author:  李祥生
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dongye.fastchat.dashscope")
public class DashScopeConfig {
    /**
     *百炼平台主页获取
     */
   private String apiKey;

   /**
    *创建应用时获取
    */
    private String appId;


    @Bean
    public  ApplicationParam  applicationParam() {
        return ApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appId)
                .build();
    }
}
