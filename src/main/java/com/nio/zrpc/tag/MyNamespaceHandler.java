package com.nio.zrpc.tag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport{

	public void init() {
		 registerBeanDefinitionParser("service", new ServiceBeanDefinitionParser());  
		 registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParser());  
		
	}

}
