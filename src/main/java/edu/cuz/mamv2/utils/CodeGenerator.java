package edu.cuz.mamv2.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * @author VM
 * @date 2022/1/17 21:39
 * @description
 */
public class CodeGenerator {
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localserver:3306/mam", "root", "asdfgh");

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    public void generate() {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("VM")
                            .fileOverride() // 文件重写
                            .commentDate("yyyy/MM/dd hh:mm")
                            .outputDir(PROJECT_PATH + "/src/main/java") // 输出路径
                            .disableOpenDir(); // 禁止打开输出文件夹，不关闭会报错
                })
                // 包名配置
                .packageConfig(builder -> {
                    builder.parent("edu.cuz")
                            .moduleName("mamv2")
                            .controller("controller")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.Impl")
                            .mapper("mapper")
                            // 自定义xml文件生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml, PROJECT_PATH + "/src/main/resources/mapper"));
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude("mam_catalogInfo") // 需要生成的表
                            .addTablePrefix("mam_") // 删除表前缀
                            .serviceBuilder().formatServiceFileName("%sService") // 去掉Service前的I
                            .controllerBuilder().enableRestStyle().enableHyphenStyle()
                            .entityBuilder().enableLombok();
                })
                .execute();
    }

    public static void main(String[] args) {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generate();
    }
}
