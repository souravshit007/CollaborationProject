package com.niit.collaboration.config;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.niit.collaboration")
public class AppConfig<logger> extends WebMvcConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
	@Bean
	public ViewResolver viewResolver() {
		logger.debug("Starting of the metnod viewResolver");

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".html");
		logger.debug("Ending of the metnod viewResolver");

		return viewResolver;

	
	
}
