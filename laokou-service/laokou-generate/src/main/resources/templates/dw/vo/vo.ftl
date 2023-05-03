import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ${author}
 */
@Data
@ApiModel(value = "${className}DTO 对象",description = "${name}")
public class ${className}DTO implements Serializable {

    @ApiModelProperty("ID")
    private String id;

    <#list column as columns>
        @ApiModelProperty("${column.name}")
        private String ${columns};
    </#list>
}