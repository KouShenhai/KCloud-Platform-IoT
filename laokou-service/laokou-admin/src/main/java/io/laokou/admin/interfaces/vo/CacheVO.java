package io.laokou.admin.interfaces.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 下午 3:19
 */
@Data
public class CacheVO {

    private Long keysSize;
    private Map<String,String> info;
    private List<Map<String,String>> commandStats;

}
