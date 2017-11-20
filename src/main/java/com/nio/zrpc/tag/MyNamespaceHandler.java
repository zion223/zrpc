package com.nio.zrpc.tag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.nio.zrpc.tag.parser.ReferenceBeanDefinitionParser;
import com.nio.zrpc.tag.parser.RegistryBeanDefinitionParser;
import com.nio.zrpc.tag.parser.ServiceBeanDefinitionParser;

public class MyNamespaceHandler extends NamespaceHandlerSupport{

	public void init() {
		 registerBeanDefinitionParser("service", new ServiceBeanDefinitionParser());  
		 registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParser());  
		 registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser());  
		
	}

}
