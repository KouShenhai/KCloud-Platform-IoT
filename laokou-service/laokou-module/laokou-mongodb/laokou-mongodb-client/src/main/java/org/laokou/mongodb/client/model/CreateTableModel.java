package org.laokou.mongodb.client.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * @author laokou
 */
@Data
public class CreateTableModel implements Serializable {

    private String tableName;

    private List<String> familyList;

}
