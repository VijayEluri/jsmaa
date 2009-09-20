package fi.smaa.common;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.not;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reportMatcher;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.beans.Model;

public class JUnitUtil {
	public static void testSetter(Model source, String propertyName, Object oldValue, Object newValue) {
		PropertyChangeListener mock = mockListener(source, propertyName, oldValue, newValue);
		
		source.addPropertyChangeListener(mock);
		Object desc = null;
		try {
			getSetterMethod(source, propertyName, newValue).invoke(source, newValue);
			desc = getGetterMethod(source, propertyName).invoke(source);
		} catch (Exception e) {
			fail(e.toString());
		}
			
		assertEquals(newValue, desc);
		verify(mock);
	}
	
	@SuppressWarnings("unchecked")
	public static <B> B serializeObject(B b) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		
		oos.writeObject(b);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		return (B) ois.readObject();
	}


	public static PropertyChangeListener mockListener(Model source,
			String propertyName, Object oldValue, Object newValue) {
		PropertyChangeListener mock = createMock(PropertyChangeListener.class);
		PropertyChangeEvent event = new PropertyChangeEvent(
				source, propertyName, oldValue, newValue);
		mock.propertyChange(eqEvent(event));
		mock.propertyChange(not(eqEvent(event)));
		expectLastCall().anyTimes();
		replay(mock);
		return mock;
	}
	
	
	private static Method getGetterMethod(Model source, String propertyName)
			throws NoSuchMethodException {
		return source.getClass().getMethod(deriveGetter(propertyName));
	}

	private static Method getSetterMethod(Model source, String propertyName,
			Object newValue) throws NoSuchMethodException {
		Method[] methods = source.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().equals(deriveSetter(propertyName))) {
				return m;
			}
		}
		throw new NoSuchMethodException("Cannot find method " + deriveSetter(propertyName) + 
				" of class " + source.getClass().getCanonicalName());
	}
	
	private static Method get1ParamMethod(Model source, String methodName, Object methodParam) 
	throws NoSuchMethodException {
		// TODO: we should check that the method has 1 param and that the param is correct type
		Method[] methods = source.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		throw new NoSuchMethodException("no method " + methodName);
	}
	
	private static String deriveGetter(String propertyName) {
		return "get" + capitalize(propertyName);
	}

	private static String deriveSetter(String propertyName) {
		return "set" + capitalize(propertyName);
	}

	private static String capitalize(String propertyName) {
		return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

	public static PropertyChangeEvent eqEvent(PropertyChangeEvent in) {
	    reportMatcher(new PropertyChangeEventMatcher(in));
	    return null;
	}

	@SuppressWarnings("unchecked")
	public static void testAdder(Model source, String propertyName, String methodName, Object toAdd) {
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		list2.add(toAdd);
		
		PropertyChangeListener mock = mockListener(source, propertyName, list1, list2);
		source.addPropertyChangeListener(mock);
		Object actual = null;
		try {
			get1ParamMethod(source, methodName, toAdd).invoke(source, toAdd);
			actual = getGetterMethod(source, propertyName).invoke(source);
		} catch (Exception e) {
			fail(e.toString());
		}
		
		assertTrue(((List) actual).contains(toAdd));
		assertTrue(1 == ((List) actual).size());
		verify(mock);
	}


	@SuppressWarnings("unchecked")
	public static void testDeleter(Model source, String propertyName, String deleteMethodName, Object toDelete) throws Exception {
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		list1.add(toDelete);

		// set the parameter
		getSetterMethod(source, propertyName, list1).invoke(source, list1);

		PropertyChangeListener mock = mockListener(source, propertyName, list1, list2);
		source.addPropertyChangeListener(mock);		

		get1ParamMethod(source, deleteMethodName, toDelete).invoke(source, toDelete);		

		Object actual = getGetterMethod(source, propertyName).invoke(source);
		assertTrue(0 ==  ((List) actual).size());
		verify(mock);
	}

}
