package com.ry;

import com.alibaba.fastjson.JSONArray;
import com.ry.common.core.dledc.response.SchoolInfo;
import com.ry.common.utils.StringUtils;
import com.ry.project.bussiness.service.IDledcService;
import com.ry.project.common.vo.IdName;
import com.ry.project.system.domain.SysDept;
import com.ry.project.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.boot.CommandLineRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 启动程序
 *
 * @author
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@ServletComponentScan
@EnableSwagger2 // 启动swagger注解
@EnableCaching // 启动缓存
@EnableAsync // 开启异步
@EnableTransactionManagement
@EnableDiscoveryClient
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
