package com.nio.zrpc.tag.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.nio.zrpc.registry.consul.entity.ServiceRegisterDefinition;
import com.nio.zrpc.registry.zookeeper.entity.ZkServiceRegisterDefinition;
import com.nio.zrpc.server.ZrpcServer;
import com.nio.zrpc.tag.definition.ZkServiceDefinition;

public class ZkServiceBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ZkServiceDefinition.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String id = element.getAttribute("id");
		String interfaceName= element.getAttribute("interfaceName");
		String ref= element.getAttribute("ref");
		
		

		if (StringUtils.hasText(id)) {
			bean.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(interfaceName)) {
			bean.addPropertyValue("InterfaceName", interfaceName);
		}
		if (StringUtils.hasText(ref)) {
			bean.addPropertyValue("ref",ref);
		}
		//在zookeeper中注册
		ZrpcServer.ZkserviceList.add(new ZkServiceRegisterDefinition(interfaceName, ref));

	}


}
