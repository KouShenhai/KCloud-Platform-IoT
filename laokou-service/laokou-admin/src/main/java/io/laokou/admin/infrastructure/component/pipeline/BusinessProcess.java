package io.laokou.admin.infrastructure.component.pipeline;
public interface BusinessProcess<T> {

    void process(T dto);

}
