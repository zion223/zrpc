package com.nio.zrpc.tag.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.nio.zrpc.registry.zookeeper.entity.ZkServiceRegisterDefinition;
import com.nio.zrpc.server.ZrpcServer;
import com.nio.zrpc.tag.definition.ZkServiceDefinition;

public class ZkServiceBeanDefinitionParser implements BeanDefinitionParser {

	private Class<?> clazz;

	public ZkServiceBeanDefinitionParser(Class<?> cls) {
		this.clazz = cls;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
		rootBeanDefinition.setBeanClass(clazz);
		String id = element.getAttribute("interfaceName");
		String port = element.getAttribute("port");
		rootBeanDefinition.getPropertyValues().add("id", id);
		parserContext.getRegistry().registerBeanDefinition(id, rootBeanDefinition);
		NamedNodeMap nnm = element.getAttributes();
		for (int i = 0; i < nnm.getLength(); i++) {
			Node node = nnm.item(i);
			String key = node.getLocalName();
			String value = node.getNodeValue();

			rootBeanDefinition.getPropertyValues().add(key, value);

		}
		
		ZrpcServer.ZkserviceList.add(new ZkServiceRegisterDefinition(id, port));

		return rootBeanDefinition;
	}

}
