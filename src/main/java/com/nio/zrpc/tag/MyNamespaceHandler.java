package com.nio.zrpc.tag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.nio.zrpc.tag.parser.ReferenceBeanDefinitionParser;
import com.nio.zrpc.tag.parser.RegistryBeanDefinitionParser;
import com.nio.zrpc.tag.parser.ServiceBeanDefinitionParser;
import com.nio.zrpc.tag.parser.ZkServiceBeanDefinitionParser;

public class MyNamespaceHandler extends NamespaceHandlerSupport{

	public void init() {
		 registerBeanDefinitionParser("service", new ServiceBeanDefinitionParser());  
		 registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParser());  
		 registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser());  
		 registerBeanDefinitionParser("zkservice", new ZkServiceBeanDefinitionParser());  
		
	}

}
