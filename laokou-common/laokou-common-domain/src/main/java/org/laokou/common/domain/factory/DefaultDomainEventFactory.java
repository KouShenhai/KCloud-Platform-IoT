package org.laokou.common.domain.factory;

import com.lmax.disruptor.EventFactory;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

public class DefaultDomainEventFactory implements EventFactory<DefaultDomainEvent> {

	@Override
	public DefaultDomainEvent newInstance() {
		return new DefaultDomainEvent();
	}

}
