package org.laokou.common.i18n.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Datas", description = "对象集合")
public class Datas<T> extends DTO {

	@Schema(name = "total", description = "总数")
	private long total;

	@Schema(name = "records", description = "对象集合")
	private List<T> records;

	/**
	 * 构建空对象集合
	 * @param <T> 泛型
	 * @return 空对象集合
	 */
	public static <T> Datas<T> of() {
		return new Datas<>(0, new ArrayList<>(0));
	}

}
