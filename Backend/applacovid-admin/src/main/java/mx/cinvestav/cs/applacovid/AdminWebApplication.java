/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid;


import mx.cinvestav.cs.applacovid.configuration.CustomDateTimeFormatter;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.Formatter;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Locale;


/**
 * Spring Boot Application definition.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"mx.cinvestav.cs.applacovid"})
@EntityScan(basePackages = "mx.cinvestav.cs.applacovid.jpa")
@EnableJpaRepositories(basePackages = "mx.cinvestav.cs.applacovid.repository")
public class AdminWebApplication implements WebMvcConfigurer
{
	/**
	 * Default constructor.
	 */
	public AdminWebApplication()
	{
	}


	/**
	 * Returns an application scoped {@link ApplicationListener} to listen to application
	 * events.
	 * @return The {@link ApplicationListener} instance for this application.
	 */
	@Bean
	ApplicationListener<ApplicationEvent> applicationListener()
	{
		return event ->
		{
			if (event instanceof ApplicationReadyEvent)
			{
				final ApplicationReadyEvent readyEvent = (ApplicationReadyEvent) event;

				if (readyEvent.getApplicationContext() ==
					AdminWebApplication.this.applicationContext)
				{
				}
			}
		};
	}


	/**
	 * Creates a {@link SessionLocaleResolver} bean, which uses the locale attribute in
	 * the user's session to set the locale, and defaults to en_US.
	 *
	 * @return A {@link SessionLocaleResolver} bean.
	 */
	@Bean
	public LocaleResolver localeResolver()
	{
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}


	/**
	 * Creates a {@link LocaleChangeInterceptor} bean that allows for changing the current
	 * locale using the {@code lang} request parameter.
	 *
	 * @return A {@link LocaleChangeInterceptor} bean.
	 */
	@Bean
	public LocaleChangeInterceptor getLocaleChangeInterceptor()
	{
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}


	/**
	 * Adds Spring MVC lifecycle interceptors for pre- and post-processing of controller
	 * method invocations.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(this.getLocaleChangeInterceptor());
	}


	/**
	 * Context closed event.
	 *
	 * @param event The event.
	 */
	@EventListener
	public void handleContextClosed(ContextClosedEvent event)
	{
		logger.info("{} context closed.", this.applicationName);
	}


	/**
	 * Context stopped event.
	 *
	 * @param event The event.
	 */
	@EventListener
	public void handleContextStopped(ContextStoppedEvent event)
	{
		logger.info("{} context stopped.", this.applicationName);
	}


	/**
	 * Context started event.
	 *
	 * @param event The event.
	 */
	@EventListener
	public void handleContextStarted(ContextStartedEvent event)
	{
		logger.info("{} context started.", this.applicationName);
	}


	/**
	 * Setup conversion service.
	 *
	 * @return A {@link DefaultFormattingConversionService} instance.
	 */
	@Bean
	public DefaultFormattingConversionService defaultConversionService()
	{
		DefaultFormattingConversionService conversionService =
			new DefaultFormattingConversionService(false);

		conversionService.addFormatterForFieldAnnotation(
			new NumberFormatAnnotationFormatterFactory());

		conversionService.addFormatterForFieldType(LocalDateTime.class, dateFormatter());

		DomainClassConverter<FormattingConversionService> converter =
			new DomainClassConverter<FormattingConversionService>(conversionService);
		converter.setApplicationContext(this.applicationContext);

		return conversionService;
	}


	@Bean
	public Formatter dateFormatter()
	{
		return new CustomDateTimeFormatter();
	}


	/**
	 * Configures the Thymeleaf view resolver.
	 *
	 * @return The {@link ThymeleafViewResolver} instance.
	 */
	@Bean
	public ViewResolver viewResolver()
	{
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setOrder(0);
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");

		return resolver;
	}


	/**
	 * Configures the Spring template engine.
	 *
	 * @return The {@link SpringTemplateEngine} instance.
	 */
	@Bean
	@Primary
	public SpringTemplateEngine templateEngine()
	{
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();

		templateEngine.addTemplateResolver(classloaderTemplateResolver());
		templateEngine.addTemplateResolver(resourceTemplateResolver());

		templateEngine.setEnableSpringELCompiler(true);

		templateEngine.addDialect(new LayoutDialect());
		templateEngine.addDialect(new SpringSecurityDialect());

		return templateEngine;
	}


	/**
	 * Configures the Spring resource teplate resolver.
	 *
	 * @return The {@link SpringResourceTemplateResolver} instance.
	 */
	protected ITemplateResolver resourceTemplateResolver()
	{
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();

		resolver.setOrder(2);
		resolver.setApplicationContext(this.applicationContext);
		resolver.setPrefix(this.thymeleafProperties.getPrefix());
		resolver.setSuffix(this.thymeleafProperties.getSuffix());
		resolver.setTemplateMode(this.thymeleafProperties.getMode());
		resolver.setCacheable(this.thymeleafProperties.isCache());

		logger.info("Initialized {} with prefix = {}, suffix = {} and mode = {}.",
			resolver.getClass().getSimpleName(),
			resolver.getPrefix(),
			resolver.getSuffix(),
			resolver.getTemplateMode());

		return resolver;
	}


	/**
	 * Configures the class loader resource template resolver.
	 *
	 * @return The {@link ClassLoaderTemplateResolver} instance.
	 */
	protected ITemplateResolver classloaderTemplateResolver()
	{
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

		resolver.setOrder(1);
		resolver.setPrefix(this.thymeleafProperties.getPrefix());
		resolver.setSuffix(this.thymeleafProperties.getSuffix());
		resolver.setTemplateMode(this.thymeleafProperties.getMode());
		resolver.setCacheable(this.thymeleafProperties.isCache());

		resolver.getResolvablePatternSpec().addPattern("templates/*");

		logger.info("Initialized {} with prefix = {}, suffix = {} and mode = {}.",
			resolver.getClass().getSimpleName(),
			resolver.getPrefix(),
			resolver.getSuffix(),
			resolver.getTemplateMode());

		return resolver;
	}


	/**
	 * Main entry point into the application.
	 *
	 * @param args The program arguments.
	 */
	public static void main(String[] args)
	{
		SpringApplication.run(AdminWebApplication.class, args);
	}


	Logger logger = LoggerFactory.getLogger(AdminWebApplication.class);

	/**
	 * Injected {@link WebApplicationContext} field.
	 */
	@Inject
	WebApplicationContext applicationContext;

	/**
	 * Thymeleaf properties field.
	 */
	@Inject
	ThymeleafProperties thymeleafProperties;

	/**
	 * The application name field.
	 */
	@Value("${spring.application.name}")
	String applicationName;

	/**
	 * The Spring Environment.
	 */
	@Inject
	Environment environment;
}
