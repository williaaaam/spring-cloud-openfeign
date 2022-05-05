package org.springframework.cloud.openfeign.example;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author william
 * @title
 * @desc
 * @date 2022/5/5
 **/
@RestController
@RequestMapping("/api/sh")
public class FooController {

	@RequestMapping("/hello")
	public String bar(){
		return "Wow";
	}

}
