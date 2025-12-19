package org.classmatechen.core.application.local.promotion.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneratePromotionConfigCommand {

    private Long localPromotionId;
    private Integer platform;
}
