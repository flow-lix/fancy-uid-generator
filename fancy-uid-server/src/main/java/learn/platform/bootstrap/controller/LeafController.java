package learn.platform.bootstrap.controller;

import learn.platform.bootstrap.service.LeafService;
import org.fancy.common.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LeafController {

    @Resource("")
    private LeafService segmentServiceImpl;
    @Resource
    private LeafService snowflakeServiceImpl;

    @PostMapping("/leaf/segment/{tag}")
    public Result generateSegmentLeaf(@PathVariable String tag) {
        return segmentServiceImpl.generateLeaf(tag);
    }

    @PostMapping("/leaf/snowflake/{tag}")
    public Result generateSnowflakeLeaf(@PathVariable String tag) {
        return snowflakeServiceImpl.generateLeaf(tag);
    }
}
