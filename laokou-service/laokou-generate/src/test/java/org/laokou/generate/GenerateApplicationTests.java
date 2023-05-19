package org.laokou.generate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.generate.mapper.ColumnMapper;
import org.laokou.generate.vo.ColumnVO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class GenerateApplicationTests {

	private final ColumnMapper columnMapper;
	private final List<String> ignoreColumns = List.of("id","create_by","create_time","update_by","update_time","sys_org_code","tenant_id");

	@Test
	void contextLoads() {
		String tableName = "dw_product_stock_change";
		String className = "ProductAlarmItem";
		List<ColumnVO> columns = columnMapper.getColumns(tableName);
		List<ColumnVO> list = columns.stream().filter(i -> !ignoreColumns.contains(i.getColumnName())).toList();
		print(list,tableName,className);
	}

	private void print(List<ColumnVO> list,String tableName,String className) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("import com.baomidou.mybatisplus.annotation.TableName;\n");
		stringBuilder.append("import com.dw.common.mybatisplus.base.BaseEntity;\n");
		stringBuilder.append("import io.swagger.annotations.ApiModelProperty;\n");
		stringBuilder.append("import lombok.Data;\n");
		stringBuilder.append("import lombok.EqualsAndHashCode;\n");
		stringBuilder.append("@EqualsAndHashCode(callSuper = true)\n");
		stringBuilder.append("@Data\n");
		stringBuilder.append("@TableName(\"").append(tableName).append("\")\n");
		stringBuilder.append("public class ").append(className).append(" extends BaseEntity {\n\n");
		for (ColumnVO vo : list) {
			stringBuilder.append("    ").append("@ApiModelProperty(\"").append(vo.getColumnComment()).append("\")").append("\n");
			stringBuilder.append("    ").append("private String ").append(StringUtil.toCamelCase(vo.getColumnName())).append(";").append("\n\n");
		}
		stringBuilder.append("}\n");
		System.out.println(stringBuilder);
	}

}
