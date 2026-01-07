package org.classmatechen.core.controller;

import org.classmatechen.common.Result;
import org.classmatechen.core.application.local.promotion.LocalPromotionAppService;
import org.classmatechen.core.application.local.promotion.command.GeneratePromotionConfigCommand;
import org.classmatechen.core.application.local.promotion.command.PublishPlatformPromotionCommand;
import org.classmatechen.core.application.local.promotion.dto.PromotionConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/local-promotion")
public class LocalPromotionController {

    @Autowired
    private LocalPromotionAppService appService;

    @GetMapping("/generate-platform-config")
    public Result<PromotionConfigDTO> generatePlatformConfig(
        @RequestParam("localPromotionId") Long localPromotionId,
        @RequestParam("platform") Integer platform
    ) {

        return Result.ok(appService.generatePlatformConfig(new GeneratePromotionConfigCommand(localPromotionId, platform)));
    }

    @PostMapping("/create-platform-promotion")
    public Result<Void> createPlatformPromotion(
        @RequestBody PublishPlatformPromotionCommand command
    ) {

        appService.publishPlatformPromotion(command);
        return Result.ok();
    }
}
