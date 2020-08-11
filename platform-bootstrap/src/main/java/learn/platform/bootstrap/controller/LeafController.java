package learn.platform.bootstrap.controller;

import learn.platform.bootstrap.service.impl.SegmentServiceImpl;
import learn.platform.bootstrap.service.impl.SnowflakeServiceImpl;
import learn.platform.common.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LeafController {

    @Resource
    private SegmentServiceImpl segmentServiceImpl;
    @Resource
    private SnowflakeServiceImpl snowflakeServiceImpl;

    @PostMapping("/leaf/segment/{tag}")
    public Result generateSegmentLeaf(@PathVariable String tag) {
        return segmentServiceImpl.generateLeaf(tag);
    }

    @PostMapping("/leaf/snowflake/{tag}")
    public Result generateSnowflakeLeaf(@PathVariable String tag) {
        return snowflakeServiceImpl.generateLeaf(tag);
    }
}
