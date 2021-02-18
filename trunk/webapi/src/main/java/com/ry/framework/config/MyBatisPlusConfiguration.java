package com.ry.framework.config;


import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.ry.framework.aspectj.lang.enums.DataSourceType;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
@NacosConfigurationProperties(dataId = "${spring.application.name}.${spring.cloud.nacos.file-extension}", autoRefreshed = true)
@MapperScan(basePackages = {"com.ry.project.**.mapper", "com.baomidou.mybatisplus.samples.quickstart.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MyBatisPlusConfiguration {

    @Primary    //多数据源时需要加上该注解，加一个即可
    @Bean(name = "masterDs")
    @ConfigurationProperties("spring.datasource.druid.master")
    public AtomikosNonXADataSourceBean masterDs() {
        return DataSourceBuilder.create().type(AtomikosNonXADataSourceBean.class).build();
    }

    /**@Bean(name = "slaveDs")
    @ConfigurationProperties("spring.datasource.druid.slave")
    public AtomikosNonXADataSourceBean slaveDs() {
        return DataSourceBuilder.create().type(AtomikosNonXADataSourceBean.class).build();
    }**/

    @Bean(name = "sqlSessionTemplate")
    public MySqlSessionTemplate customSqlSessionTemplate() throws Exception {
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>() {{
            put(DataSourceType.MASTER.name(), createSqlSessionFactory(masterDs()));
            //put(DataSourceType.SLAVE.name(), createSqlSessionFactory(slaveDs()));
        }};
        MySqlSessionTemplate sqlSessionTemplate = new MySqlSessionTemplate(sqlSessionFactoryMap.get(DataSourceType.MASTER.name()));
        sqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryMap);
        return sqlSessionTemplate;
    }

    /**
     * 创建数据源
     *
     * @param dataSource
     * @return
     */
    private SqlSessionFactory createSqlSessionFactory(AtomikosNonXADataSourceBean dataSource) throws Exception {
        dataSource.init();//项目启动则初始化连接
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("com.ry.project.**.domain");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/com/ry/project/**/mapper/xml/*.xml"));
        sqlSessionFactory.setVfs(SpringBootVFS.class);

        MybatisConfiguration configuration = new MybatisConfiguration();
        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        // 配置打印sql语句
        configuration.setLogImpl(StdOutImpl.class);
        sqlSessionFactory.setConfiguration(configuration);
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor()
        });
        //重写了GlobalConfig的MyGlobalConfig注入到sqlSessionFactory使其生效
        MyGlobalConfig globalConfig = new MyGlobalConfig();
        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.afterPropertiesSet();
        return sqlSessionFactory.getObject();
    }

    /*
     * 自定义的分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
