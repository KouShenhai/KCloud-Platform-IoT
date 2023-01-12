package org.laokou.mongodb.client.model;

import lombok.Data;

import java.io.Serializable;
/**
 * @author laokou
 */
@Data
public class MongodbModel  implements Serializable {
    private String data;
    private String collectionName;
}
