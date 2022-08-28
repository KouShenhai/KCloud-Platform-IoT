package io.laokou.admin.infrastructure.common.index;
import lombok.Data;
import java.io.Serializable;
@Data
public class ResourceIndex implements Serializable {

    private Long id;

    private String title;

    private String code;

    private String remark;

    private String tags;

    private String ym;
}
