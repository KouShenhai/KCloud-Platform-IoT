package io.laokou.generator.config.template;

import lombok.Data;

import java.util.List;

@Data
public class GeneratorInfo {
    private ProjectInfo project;
    private DeveloperInfo developer;
    private List<TemplateInfo> templates;
}
