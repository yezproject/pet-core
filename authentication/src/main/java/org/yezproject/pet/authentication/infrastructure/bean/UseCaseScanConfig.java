package org.yezproject.pet.authentication.infrastructure.bean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.yezproject.pet.domain_common.UseCase;

@Configuration
@ComponentScan(
        basePackages = "org.yezproject.pet.authentication.application",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION, value = UseCase.class
        )
)
public class UseCaseScanConfig {
}
