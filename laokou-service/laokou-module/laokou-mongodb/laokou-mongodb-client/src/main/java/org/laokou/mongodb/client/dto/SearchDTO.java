package org.laokou.mongodb.client.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author laokou
 */
@Data
public class SearchDTO implements Serializable {

    private String field;

    private String value;

    
}
