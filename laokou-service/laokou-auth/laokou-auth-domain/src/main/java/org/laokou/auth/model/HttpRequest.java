package org.laokou.auth.model;

import java.util.Map;

/**
 * @author laokou
 */
@FunctionalInterface
public interface HttpRequest {

	Map<String, String[]> getParameterMap();

}
