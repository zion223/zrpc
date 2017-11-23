package com.nio.zrpc.tag.parser;


import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.nio.zrpc.registry.consul.entity.ServiceRegisterDefinition;
import com.nio.zrpc.server.ZrpcServer;
import com.nio.zrpc.tag.definition.ServiceDefinition;

public class ServiceBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ServiceDefinition.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String id = element.getAttribute("id");
		String name= element.getAttribute("name");
		String address= element.getAttribute("address");
		String tag = element.getAttribute("tag");
		String portmsg = element.getAttribute("port");
		
		
		Integer port =Integer.parseInt(portmsg);

		if (StringUtils.hasText(address)) {
			bean.addPropertyValue("address", address);
		}
		if (StringUtils.hasText(id)) {
			bean.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(name)) {
			bean.addPropertyValue("name", name);
		}
		if (StringUtils.hasText(tag)) {
			bean.addPropertyValue("ref", tag);
		}
		
		bean.addPropertyValue("port", port);
		

		
		//这是使用TCP协议时的
		//InvokeService.services.put(ref, service);
		//InvokeService.services.put(interfaceName, service);
		
		
		ZrpcServer.ConserviceList.add(new ServiceRegisterDefinition(id, name,tag,address, portmsg));
	
	
	}


}
