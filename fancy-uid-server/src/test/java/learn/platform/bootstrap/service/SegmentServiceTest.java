package learn.platform.bootstrap.service;

import learn.platform.bootstrap.service.impl.SegmentServiceImpl;
import org.fancy.common.Result;
import learn.platform.id.mapper.IDAllocMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SegmentServiceTest {

    @Autowired
    private SegmentServiceImpl service;

    @Test
    public void testGenerateLeaf() {
        String tag = "test-tag";
        for (int i = 0; i < 10; i++) {
            Result result = service.generateLeaf(tag);
            System.out.println(result);
            Assert.assertNotNull(result);
        }
    }

    @Autowired
    private IDAllocMapper allocMapper;

    @Test
    public void testMapper() {
        Assert.assertEquals(1, allocMapper.getAllTags().size());
    }
}
