package org.springframework.cloud.openfeign.example;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * jvm字节码指令invokedynamic是基于java.lang.MethodHandle实现的，同时该指令也是实现Lambda的技术基础；
 * invokedynamic指令能够在JVM字节码指令层面实现在运行期才确认被调用方法的接收对象的类型，接收对象例如 obj.println("Hello World !"),obj的具体类型
 *
 * @author william
 * @title
 * @desc
 * @date 2022/5/5
 **/
public class MethodHandleTests {


	public static String printStatic(Object target, String methodName, String argument) throws Throwable {
		// 定义方法句柄（返回值和参数类型）
		MethodType methodType = MethodType.methodType(String.class, String.class);
		MethodHandle methodHandle = MethodHandles.lookup().findStatic(MethodHandleTests.class, methodName, methodType);
		return (String) methodHandle.invokeExact(argument);
	}

	public static String printVirtual(Object target, String methodName, String argument) throws Throwable {
		// 定义方法句柄（返回值和参数类型）
		MethodType methodType = MethodType.methodType(String.class, String.class);
		// Lookup在只在创建MethodHandler时检查执行lookup的类对目标方法的访问权限，而不是在通过MethodHandler调用特定方法时检查，
		// Java反射API会每次调用都需要检查权限，这是Lookup与Java反射API最关键的不同
		MethodHandle methodHandle = MethodHandles.lookup().findVirtual(target.getClass(), methodName, methodType);
		// 将target作为invoke或者invokeExact的第一个参数，即方法调用的接收对象
		return (String) methodHandle.bindTo(target).invokeExact(argument);
	}

	public String bar(String param) {
		return "Hi " + param;
	}


	public static String foo(String param) {
		return "Hi " + param;
	}

	public static void main(String[] args) throws Throwable {
		// new MethodHandleTests();
		System.out.println(printStatic(null, "foo", "Shanghai"));
		System.out.println(printVirtual(new MethodHandleTests(), "bar", "Shanghai"));
	}
}
