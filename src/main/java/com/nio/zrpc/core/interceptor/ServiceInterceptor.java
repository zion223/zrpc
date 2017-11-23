package com.nio.zrpc.core.interceptor;

import java.io.IOException;


public interface ServiceInterceptor {

	Object doIntercepptor() throws IOException,NoSuchMethodException, SecurityException, ClassNotFoundException,InstantiationException,IllegalAccessException;
}
