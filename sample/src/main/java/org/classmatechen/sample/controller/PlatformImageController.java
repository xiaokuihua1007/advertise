package org.classmatechen.sample.controller;

import org.classmatechen.common.Platform;
import org.classmatechen.common.Result;
import org.classmatechen.sample.service.PlatformImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platformimage")
public class PlatformImageController {

    @Autowired
    private PlatformImageService imageService;

    @PostMapping("/download")
    public Result<Void> download(@RequestParam("platform") Integer platform) {
        imageService.loadResource(Platform.platform(platform));
        return Result.ok();
    }
}
