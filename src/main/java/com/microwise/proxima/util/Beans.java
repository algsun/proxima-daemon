package com.microwise.proxima.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * 提供 spring bean 注解中的各种便利的注解
 *
 * @author bastengao
 * @date 12-9-27 Time: 下午6:36
 * @check  @wang.yunlong & li.jianfei   #38   2012-12-18
 */
public class Beans {

	/**
	 * 单例
	 */
	@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
	@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@Scope(BeanScopes.SINGLETON)
	public @interface Singleton {
	}

	/**
	 * 原型
	 */
	@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
	@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@Scope(BeanScopes.PROTOTYPE)
	public @interface Prototype {
	}

	/**
	 * 用于Dao的impl的注解
	 */
	@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
	@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@Repository
	@Scope(BeanScopes.PROTOTYPE)
	public static @interface Dao {
		String value() default "";
	}

	/**
	 * 用于Service的impl的注解
	 */
	@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
	@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@org.springframework.stereotype.Service
	@Scope(BeanScopes.PROTOTYPE)
	public static @interface Service {
		String value() default "";
	}

	/**
	 * 用于Action的注解
	 */
	@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
	@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@Component
	@Scope(BeanScopes.PROTOTYPE)
	public static @interface Action {
		String value() default "";
	}

	/**
	 * 用于spring bean(不是 java bean) 的注解
	 */
	@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
	@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@Component
	@Scope(BeanScopes.PROTOTYPE)
	public static @interface Bean {
		String value() default "";
	}

}
