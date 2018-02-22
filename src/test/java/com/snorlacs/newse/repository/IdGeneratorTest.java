package com.snorlacs.newse.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdGeneratorTest {
    
    @Autowired
    private IdGenerator generator1;

    @Autowired
    private IdGenerator generator2;

    @Test
    public void testInterferenceOfMultipleGenerator() throws Exception {
        Assert.assertEquals(1, generator1.getId());
        Assert.assertEquals(2, generator1.getId());
        Assert.assertEquals(1, generator2.getId());
        Assert.assertEquals(2, generator2.getId());
    }
}