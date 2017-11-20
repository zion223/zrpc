package com.nio.zrpc.tag.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.google.common.net.HostAndPort;
import com.nio.zrpc.core.InvokeService;
import com.nio.zrpc.tag.definition.RegistryDefinition;
import com.nio.zrpc.tag.definition.ServiceDefinition;
import com.orbitz.consul.Consul;

public class RegistryBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {
	private static final Logger log = LoggerFactory.getLogger(RegistryBeanDefinitionParser.class);

	@Override
	protected Class<?> getBeanClass(Element element) {
		return RegistryDefinition.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String address = element.getAttribute("address");
		String id = element.getAttribute("id");
		
		//consul://127.0.0.1:8500
		//取后面的地址 127.0.0.1:8500
		address=address.substring(address.lastIndexOf("/")+1);
		log.info("服务注册地址:"+address);
		if (StringUtils.hasText(address)) {
			bean.addPropertyValue("address", address);
		}
		if (StringUtils.hasText(id)) {
			bean.addPropertyValue("id", id);
		}
		
		
		//通过address地址构造Consul实例
		//static Consul consul=Consul.builder().withHostAndPort(HostAndPort.fromString("127.0.0.1:8500")).build();
	

	}


}
