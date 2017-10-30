package com.nio.zrpc.tag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.nio.zrpc.core.InvokeService;
import com.nio.zrpc.tag.definition.ServiceDefinition;

public class ServiceBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ServiceDefinition.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		Object service = null;
		String interfaceName = element.getAttribute("interfaceName");
		String interfaceImplName = element.getAttribute("interfaceImplName");
		String ref = element.getAttribute("ref");
		String id = element.getAttribute("id");

		if (StringUtils.hasText(interfaceName)) {
			bean.addPropertyValue("interfaceName", interfaceName);
		}
		if (StringUtils.hasText(interfaceImplName)) {
			bean.addPropertyValue("interfaceImplName", interfaceImplName);
		}
		if (StringUtils.hasText(ref)) {
			bean.addPropertyValue("ref", ref);
		}
		if (StringUtils.hasText(id)) {
			bean.addPropertyValue("id", id);
		}

		try {
			service = Class.forName(interfaceImplName).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		InvokeService.services.put(ref, service);
		InvokeService.services.put(interfaceName, service);
	}

}
