/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.laokou.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.apache.ibatis.type.JdbcType.*;

/**
 * @author Liukefu
 */
public class GeneratorMain {

	/**
	 * 配置 生成代码的所有表
	 */
	private static final List<String> TABLES = List.of("boot_sys_dict", "boot_sys_dept");

	private static final String URL = "jdbc:mysql://192.168.30.133:3306/kcloud_platform_alibaba?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false";

	private static final String USERNAME = "root";

	private static final String PASSWORD = "laokou123";

	/**
	 * 配置 父包名
	 */
	private static final String PACKAGE_PATH = "org.laokou";

	/**
	 * 配置 模块名
	 */
	private static final String MODULE_NAME = "sys";

	/**
	 * 输出目录
	 */
	private static final String GENERATOR_PATH = "generator";

	/**
	 * 配置 代码输出目录
	 */
	private static String OUTPUT_DIR = "";

	public static void main(String[] args) {
		// 默认生成到 KCloud-Platform-Alibaba
		if (StringUtil.isEmpty(OUTPUT_DIR)) {
			OUTPUT_DIR = Objects.requireNonNull(GeneratorMain.class.getClassLoader().getResource("")).getPath();
			OUTPUT_DIR = OUTPUT_DIR.substring(0, OUTPUT_DIR.length() - 15) + File.separator + "src" + File.separator
					+ "main" + File.separator + "java";
			File srcFolder = new File(OUTPUT_DIR);
			OUTPUT_DIR = srcFolder.getAbsolutePath();
		}

		FastAutoGenerator.create(URL, USERNAME, PASSWORD).globalConfig(builder -> {
			builder.author("laokou") // 设置作者
				// .enableSwagger() // 开启 swagger 模式
				.enableSpringdoc() // 开启 springdoc 模式
				.outputDir(GENERATOR_PATH); // 指定输出目录
		}).dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
			// 自定义类型转换
			if (metaInfo.getJdbcType() == SMALLINT) {
				return DbColumnType.INTEGER;
			}
			else if (metaInfo.getJdbcType() == TINYINT) {
				return DbColumnType.INTEGER;
			}
			else if (metaInfo.getJdbcType() == BIT) {
				return DbColumnType.INTEGER;
			}
			return typeRegistry.getColumnType(metaInfo);
		})).packageConfig(builder -> {
			builder.parent(PACKAGE_PATH) // 设置父包名
				.moduleName(MODULE_NAME) // 设置模块名
				// .entity("entity")
				.pathInfo(Collections.singletonMap(OutputFile.xml, MODULE_NAME + File.separator + "xml")); // 设置mapperXml生成路径
		}).strategyConfig(builder -> {
			builder.addInclude(TABLES) // 设置需要生成的表名
				.addTablePrefix("act_", "flw_", "oauth2_", "boot_", "t_") // 设置过滤表前缀
				.serviceBuilder() // service策略配置
				.formatServiceFileName("%sService")
				.formatServiceImplFileName("%sServiceImpl")
				.enableFileOverride()
				.entityBuilder() // 实体类策略配置
				.formatFileName("%sDO")
				.enableFileOverride()
				// .idType(IdType.ASSIGN_ID)//主键策略 雪花算法自动生成的id
				// .addTableFills(new Column("create_date", FieldFill.INSERT)) //
				// 自动填充配置
				// .addTableFills(new Property("update_date",
				// FieldFill.INSERT_UPDATE))
				.superClass(BaseDO.class)
				.addSuperEntityColumns("id", "creator", "editor", "create_date", "update_date", "del_flag", "version")
				.enableLombok() // 开启lombok
				.logicDeleteColumnName("delFlag")// 说明逻辑删除是哪个字段
				.enableTableFieldAnnotation()// 属性加上注解说明
				.controllerBuilder() // controller 策略配置
				.formatFileName("%sController")
				.enableRestStyle() // 开启RestController注解
				.enableFileOverride()
				.mapperBuilder()// mapper策略配置
				.formatMapperFileName("%sMapper")
				.enableMapperAnnotation()// @mapper注解开启
				.formatXmlFileName("%sMapper")
				.superClass(BatchMapper.class)
				.enableFileOverride();
		}).templateConfig(builder -> {
			builder.entity("/templates/mybatisplus/entity.java")
				.mapper("/templates/mybatisplus/mapper.java")
				.xml("/templates/mybatisplus/mapper.xml")
				.service("/templates/mybatisplus/service.java")
				.serviceImpl("/templates/mybatisplus/serviceImpl.java")
				.controller("/templates/mybatisplus/controller.java");
		}).injectionConfig(builder -> {
			List<CustomFile> customFiles = new ArrayList<>();

			customFiles.add(new CustomFile.Builder().fileName("Qo.java")
				.templatePath("/templates/mybatisplus/qo.java.ftl")
				.enableFileOverride()
				.build());
			customFiles.add(new CustomFile.Builder().fileName("VO.java")
				.templatePath("/templates/mybatisplus/vo.java.ftl")
				.enableFileOverride()
				.build());
			customFiles.add(new CustomFile.Builder().fileName("DTO.java")
				.templatePath("/templates/mybatisplus/dto.java.ftl")
				.enableFileOverride()
				.build());
			customFiles.add(new CustomFile.Builder().fileName("Excel.java")
				.templatePath("/templates/mybatisplus/excel.java.ftl")
				.enableFileOverride()
				.build());

			builder.customFile(customFiles);
		})
			// 使用Freemarker引擎模板，默认的是Velocity引擎模板
			.templateEngine(new EnhanceFreemarkerTemplateEngine())
			.execute();
	}

}
