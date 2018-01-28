package com.nio.zrpc.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.nio.entity.User;

public class KryoUtil {

	@Test
	public void testkryo(){
		
		User user = new User();
		user.setAge(17);
		user.setName("zhangsan");
		
		String usermsg = serializationObject(user);
		System.out.println(usermsg);
		User user2 = deserializationObject(usermsg, User.class);
		System.out.println(user2.getName());
	}
	public static <T extends Serializable> String serializationObject(T obj) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(obj.getClass(), new JavaSerializer());
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, obj);
        output.flush();
        output.close();
 
        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return new String(new Base64().encode(b));
    }
	  @SuppressWarnings("unchecked")
	  public static <T extends Serializable> T deserializationObject(String obj,
	            Class<T> clazz) {
	        Kryo kryo = new Kryo();
	        kryo.setReferences(false);
	        kryo.register(clazz, new JavaSerializer());
	 
	        ByteArrayInputStream bais = new ByteArrayInputStream(
	                new Base64().decode(obj));
	        Input input = new Input(bais);
	        return (T) kryo.readClassAndObject(input);
	    }
}
