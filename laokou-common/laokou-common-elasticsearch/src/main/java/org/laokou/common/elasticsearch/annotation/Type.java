/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.elasticsearch.annotation;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import lombok.Getter;

/**
 * @author laokou
 */
@Getter
public enum Type {

	AUTO {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	TEXT {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {
			Document.SubField subField = mapping.subField();
			mappingBuilder.properties(mapping.field(),
					fn -> fn.text(t -> t.index(mapping.index())
						.eagerGlobalOrdinals(mapping.eagerGlobalOrdinals())
						.searchAnalyzer(mapping.searchAnalyzer())
						.analyzer(mapping.analyzer())
						.fields("keyword", f -> f.keyword(p -> p.ignoreAbove(subField.ignoreAbove())))));
		}
	},
	KEYWORD {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {
			mappingBuilder.properties(mapping.field(),
					fn -> fn.keyword(t -> t.index(mapping.index()).eagerGlobalOrdinals(mapping.eagerGlobalOrdinals())));
		}
	},
	LONG {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {
			mappingBuilder.properties(mapping.field(), fn -> fn.long_(t -> t.index(mapping.index())));
		}
	},
	INTEGER {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	SHORT {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	BYTE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	DOUBLE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	FLOAT {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	DATE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {
			mappingBuilder.properties(mapping.field(),
					fn -> fn.date(t -> t.index(mapping.index()).format(mapping.format())));
		}
	},
	BOOLEAN {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	BINARY {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	INTEGER_RANGE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	FLOAT_RANGE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	LONG_RANGE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	DOUBLE_RANGE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	DATE_RANGE {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	OBJECT {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	},
	IP {
		@Override
		public void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {

		}
	};

	public abstract void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping);

}
