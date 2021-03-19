package org.df.smartmvc;

import org.df.smartmvc.config.AppConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @program: SmartMVC
 * @description: 单元测试基类
 * @author: duanf
 * @create: 2021-03-18 18:19
 **/
@RunWith(SpringJUnit4ClassRunner.class)  //junit提供的扩展接口，这里指定使用SpringJUnit4ClassRunner作为junit的测试环境
@ContextConfiguration(classes = AppConfig.class)  //加载配置文件
public class BaseJunit4Test {
}
