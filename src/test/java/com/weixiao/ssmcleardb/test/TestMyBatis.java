package com.weixiao.ssmcleardb.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.weixiao.ssmcleardb.model.User;
import com.weixiao.ssmcleardb.service.UserManager;

@RunWith(SpringJUnit4ClassRunner.class) // 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })

public class TestMyBatis {
	private static Logger logger = Logger.getLogger(TestMyBatis.class);

	@Resource
	private UserManager userManager;

	// @Before
	// public void before() {
	// ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	// userManager = (UserManager) ac.getBean("userManager");
	// }

	@Test
	public void test1() {
		User user = userManager.get(1L);
		// System.out.println(user.getName());
		// logger.info("值："+user.getName());
		logger.info(JSON.toJSONString(user));
	}
}