package com.nio.zrpc.tag.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import com.nio.zrpc.tag.definition.RegistryDefinition;


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
		
		//Consul://127.0.0.1:8500
		//Zookeeper://192.168.252.144:2181
		//取后面的地址 127.0.0.1:8500
		String _address=address.substring(address.lastIndexOf("/")+1);
		log.info("服务注册地址:"+_address);
		if (StringUtils.hasText(_address)) {
			bean.addPropertyValue("address", _address);
		}
		
		
		bean.addPropertyValue("name",address.split(":")[0]);
		
	}



}
