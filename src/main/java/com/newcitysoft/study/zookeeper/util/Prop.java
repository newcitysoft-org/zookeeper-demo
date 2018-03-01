package com.newcitysoft.study.zookeeper.util;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Prop. Prop can load properties file from CLASSPATH or File object.
 */
public class Prop {

	private final static String DEFAULT_ENCODING = "UTF-8";
	private Properties properties = null;
	private String fileName = null;

	/**
	 * Prop constructor.
	 * @see #Prop(String, String)
	 */
	public Prop(String fileName) {
		this(fileName, DEFAULT_ENCODING);
	}

	/**
	 * Prop constructor
	 * <p>
	 * Example:<br>
	 * Prop prop = new Prop("my_config.txt", "UTF-8");<br>
	 * String userName = prop.get("userName");<br><br>
	 *
	 * prop = new Prop("com/jfinal/file_in_sub_path_of_classpath.txt", "UTF-8");<br>
	 * String value = prop.get("key");
	 *
	 * @param fileName the properties file's name in classpath or the sub directory of classpath
	 * @param encoding the encoding
	 */
	public Prop(String fileName, String encoding) {
		InputStream inputStream = null;
		try {
			inputStream = getClassLoader().getResourceAsStream(fileName);
			if (inputStream == null) {
				throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
			}
			properties = new Properties();
			properties.load(new InputStreamReader(inputStream, encoding));
			this.fileName = fileName;
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		}
		finally {
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private ClassLoader getClassLoader() {
		ClassLoader ret = Thread.currentThread().getContextClassLoader();
		return ret != null ? ret : getClass().getClassLoader();
	}

	/**
	 * Prop constructor.
	 * @see #Prop(File, String)
	 */
	public Prop(File file) {
		this(file, DEFAULT_ENCODING);
	}

	/**
	 * Prop constructor
	 * <p>
	 * Example:<br>
	 * Prop prop = new Prop(new File("/var/config/my_config.txt"), "UTF-8");<br>
	 * String userName = prop.get("userName");
	 *
	 * @param file the properties File object
	 * @param encoding the encoding
	 */
	public Prop(File file, String encoding) {
		if (file == null) {
			throw new IllegalArgumentException("File can not be null.");
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException("File not found : " + file.getName());
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			properties = new Properties();
			properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		}
		finally {
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Prop append(Prop prop) {
		if (prop == null) {
			throw new IllegalArgumentException("prop can not be null");
		}
		properties.putAll(prop.getProperties());
		return this;
	}

	public Prop append(String fileName, String encoding) {
		return append(new Prop(fileName, encoding));
	}

	public Prop append(String fileName) {
		return append(fileName, DEFAULT_ENCODING);
	}

	public Prop appendIfExists(String fileName, String encoding) {
		try {
			return append(new Prop(fileName, encoding));
		} catch (Exception e) {
			return this;
		}
	}

	public Prop appendIfExists(String fileName) {
		return appendIfExists(fileName, DEFAULT_ENCODING);
	}

	public Prop append(File file, String encoding) {
		return append(new Prop(file, encoding));
	}

	public Prop append(File file) {
		return append(file, DEFAULT_ENCODING);
	}

	public Prop appendIfExists(File file, String encoding) {
		if (file.exists()) {
			append(new Prop(file, encoding));
		}
		return this;
	}

	public Prop appendIfExists(File file) {
		return appendIfExists(file, DEFAULT_ENCODING);
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public Integer getInt(String key) {
		return getInt(key, null);
	}

	public Integer getInt(String key, Integer defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			return Integer.parseInt(value.trim());
		}
		return defaultValue;
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			return Long.parseLong(value.trim());
		}
		return defaultValue;
	}

	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			value = value.toLowerCase().trim();
			if ("true".equals(value)) {
				return true;
			} else if ("false".equals(value)) {
				return false;
			}
			throw new RuntimeException("The value can not parse to Boolean : " + value);
		}
		return defaultValue;
	}

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public Properties getProperties() {
		return properties;
	}

	public void store (Properties pps) {
		// pps = new Properties();
		String file = getClassLoader().getResource(fileName).getPath();
		System.out.println(file);
		InputStream in = null;
		try {
			//从输入流中读取属性列表（键和元素对）
			in = getClassLoader().getResourceAsStream(fileName);;
			//调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
			pps.load(in);
			//强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
			OutputStream out = new FileOutputStream(file);
			//pps.setProperty(pKey, pValue);
			//以适合使用 load 方法加载到 Properties 表中的格式，
			//将此 Properties 表中的属性列表（键和元素对）写入输出流
			pps.store(out,"");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(Throwables.getStackTraceAsString(e));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(Throwables.getStackTraceAsString(e));
		}
	}
}
