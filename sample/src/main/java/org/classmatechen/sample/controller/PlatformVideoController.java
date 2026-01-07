package org.classmatechen.sample.controller;

import org.classmatechen.common.Platform;
import org.classmatechen.common.Result;
import org.classmatechen.sample.service.PlatformVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platformvideo")
public class PlatformVideoController {

    @Autowired
    private PlatformVideoService videoService;

    @PostMapping("/download")
    public Result<Void> download(@RequestParam("platform") Integer platform) {
        videoService.loadResource(Platform.platform(platform));
        return Result.ok();
    }
}
