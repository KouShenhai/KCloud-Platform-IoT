package io.laokou.admin.infrastructure.config;
import io.laokou.admin.infrastructure.component.action.PreParamCheckAction;
import io.laokou.admin.infrastructure.component.action.SendMessageAction;
import io.laokou.admin.infrastructure.component.pipeline.ProcessController;
import io.laokou.admin.infrastructure.component.pipeline.ProcessTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
@Configuration
public class PipelineConfig {

    @Autowired
    private SendMessageAction sendMessageAction;

    @Autowired
    private PreParamCheckAction preParamCheckAction;

    @Bean
    public ProcessTemplate processTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(preParamCheckAction,sendMessageAction));
        return processTemplate;
    }

    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        processController.setProcessTemplate(processTemplate());
        return processController;
    }

}
