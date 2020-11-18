/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.ViewFactoryCreator;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.FlowAjaxThymeleafView;

import java.util.Collections;


@Configuration
public class WebFlowWithMvcConfig extends AbstractFlowConfiguration
{
	@Bean
	public FlowDefinitionRegistry flowRegistry()
	{
		return getFlowDefinitionRegistryBuilder().setBasePath("classpath:flows").
			addFlowLocationPattern("/flow.xml").
			setFlowBuilderServices(this.flowBuilderServices()).build();
	}

	@Bean
	public FlowExecutor flowExecutor()
	{
		return getFlowExecutorBuilder(this.flowRegistry()).build();
	}


	@Bean
	public FlowBuilderServices flowBuilderServices()
	{
		return getFlowBuilderServicesBuilder().
			setViewFactoryCreator(this.mvcViewFactoryCreator()).
			setValidator(this.localValidatorFacotryBean).build();
	}


	@Bean
	public FlowHandlerMapping flowHandlerMapping() {
		FlowHandlerMapping handlerMapping = new FlowHandlerMapping();
		//handlerMapping.setOrder(-1);
		handlerMapping.setOrder(0);
		handlerMapping.setFlowRegistry(this.flowRegistry());
		return handlerMapping;
	}


	@Bean
	public FlowHandlerAdapter flowHandlerAdapter() {
		FlowHandlerAdapter handlerAdapter = new FlowHandlerAdapter();
		handlerAdapter.setFlowExecutor(this.flowExecutor());
		handlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
		return handlerAdapter;
	}


	@Bean
	public ViewFactoryCreator mvcViewFactoryCreator() {
		MvcViewFactoryCreator factoryCreator = new MvcViewFactoryCreator();
		factoryCreator.setViewResolvers(
			Collections.singletonList(this.thymeleafViewResolver()));
		factoryCreator.setUseSpringBeanBinding(true);
		return factoryCreator;
	}


	@Bean
	public AjaxThymeleafViewResolver thymeleafViewResolver()
	{
		AjaxThymeleafViewResolver ajaxResolver = new AjaxThymeleafViewResolver();
		ajaxResolver.setViewClass(FlowAjaxThymeleafView.class);
		ajaxResolver.setTemplateEngine(this.templateEngine);
		ajaxResolver.setCharacterEncoding("UTF-8");

		return ajaxResolver;
	}


	@Autowired
	private LocalValidatorFactoryBean localValidatorFacotryBean;

	@Autowired
	private SpringTemplateEngine templateEngine;
}
