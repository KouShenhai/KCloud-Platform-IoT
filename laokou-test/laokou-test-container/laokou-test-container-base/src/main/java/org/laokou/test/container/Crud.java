package org.laokou.test.container;

import java.util.Map;

public interface Crud {

	String create(Map<String, Object> params);
	String remove(Map<String, Object> params);
	String modify(Map<String, Object> params);
	String imp(Map<String, Object> params);
	String find(Map<String, Object> params);
	String type(Map<String, Object> params);


}
