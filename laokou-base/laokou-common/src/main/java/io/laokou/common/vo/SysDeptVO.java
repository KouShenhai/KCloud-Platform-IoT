package io.laokou.common.vo;

import io.laokou.common.utils.TreeUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysDeptVO extends TreeUtil.TreeNo<SysDeptVO> implements Serializable {

    private Integer sort;

}
