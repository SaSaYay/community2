package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override//spring 容器
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext() {

		System.out.println(applicationContext);

//		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());
		AlphaDao alphaDao2 = applicationContext.getBean("alphaHibernate",AlphaDao.class);
		System.out.println(alphaDao2.select());
	}
	@Test
	public void  testBeanManagement(){
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
		AlphaService alphaService2 = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService2);
	}
	@Test
	public void testBeanConfig(){
		SimpleDateFormat bean = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(bean.format(new Date()));
	}
	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;
	@Test
	public void testDI(){
		System.out.println(alphaDao);

	}
}
