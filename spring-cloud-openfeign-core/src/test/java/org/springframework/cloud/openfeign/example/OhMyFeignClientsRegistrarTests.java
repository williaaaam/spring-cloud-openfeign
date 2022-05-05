/*
 * Copyright 2013-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.openfeign.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Proxy;

/**
 * @author Spencer Gibb
 * @author Gang Li
 * @author Michal Domagala
 */
public class OhMyFeignClientsRegistrarTests {

	@Test
	public void testProxy() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(FallbackTestConfig.class);
		applicationContext.register(OhMyFallback.class);
		applicationContext.refresh();
		// 默认Lazy-init
		FallbackClient client = applicationContext.getBean(FallbackClient.class);
		System.out.println(applicationContext.getBean(OhMyFallback.class));
		System.out.println(client.helloM("23333"));

	}

	/**
	 * name 是目标服务的名称
	 */
	@FeignClient(name = "fallbackTestClient", url = "www.baidu.com", path = "统一前置访问路径",
		fallback = OhMyFallback.class)
	protected interface FallbackClient {
		/**
		 * OpenFeign 接口中定义的路径和参数必须与你要调用的目标服务中的保持一致(如果参数和返回值是自定义对象，那么字段要对应，返回值和参数不需要一定对应)。
		 *
		 * @param userName
		 * @return
		 */
		@RequestMapping(method = RequestMethod.GET, value = "api/sh/hello")
		// 参数如果不加注解，默认@RequestBody注解（请求体)
		String helloM(@RequestParam("userName") String userName);

	}

	protected static class OhMyFallback implements FallbackClient {

		@Override
		public String helloM(String userName) {
			return "OhMyFallback...";
		}
	}

	@Configuration(proxyBeanMethods = false)
	@EnableAutoConfiguration
	@EnableFeignClients(clients = {FallbackClient.class})
	protected static class FallbackTestConfig {

	}

}
