package org.mybatis.generator.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.TableConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成服务类
 * @author fanrui
 *
 */
public class MybatisServicePlugin extends PluginAdapter {

	private Logger log = LoggerFactory.getLogger(MybatisServicePlugin.class);
//	private AddMergeIdentifyPlugin addCommentPlugin = new AddMergeIdentifyPlugin();

	private FullyQualifiedJavaType slf4jLogger;
	private FullyQualifiedJavaType slf4jLoggerFactory;
	private FullyQualifiedJavaType listType;
	private FullyQualifiedJavaType autowired;
	private FullyQualifiedJavaType service;
	private FullyQualifiedJavaType transactional;
	private String servicePack;
	private String project;

	private static String PARENT_DAO_INTERFACE="org.mybatis.generator.gen.GenericDao";
	private static String PARENT_SERVICE_INTERFACE="org.mybatis.generator.gen.GenericService";
	
	private static String PARENT_SERVICE_IMPL="org.mybatis.generator.gen.GenericServiceImpl";

	public MybatisServicePlugin() {
		super();
		
	}

	/**
	 * 读取配置文件
	 */
	@Override
	public boolean validate(List<String> warnings) {
		this.servicePack = properties.getProperty("targetPackage");
		this.project = properties.getProperty("targetProject");
		if (servicePack == null || project == null) {
			log.error("必须指定targetPackage 以及targetProject ");
			return false;
		}
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		super.initialized(introspectedTable);
		slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
		slf4jLoggerFactory = new FullyQualifiedJavaType(
				"org.slf4j.LoggerFactory");
		listType = new FullyQualifiedJavaType("java.util.List");
		autowired = new FullyQualifiedJavaType(
				"org.springframework.beans.factory.annotation.Autowired");
		service = new FullyQualifiedJavaType(
				"org.springframework.stereotype.Service");
		transactional= new FullyQualifiedJavaType(
				"org.springframework.transaction.annotation.Transactional");
	}


	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
			IntrospectedTable introspectedTable) {

		TableConfiguration tc = introspectedTable.getTableConfiguration();
		String tableName = tc.getTableName();
		String moduleName = getModule(tableName);
		if (moduleName == null) {
			return null;
		}
		// services res_shop_card com.xxx.xxx.res.ShopCardService
		FullyQualifiedJavaType interfaceType = new FullyQualifiedJavaType(
				servicePack + "." + moduleName + "." + getGenName(tableName)
						+ "Service");
		// services res_shop_card com.xxx.xxx.res.impl.ShopCardServiceImpl
		FullyQualifiedJavaType serviceType = new FullyQualifiedJavaType(
				servicePack + "." + moduleName + "." + "impl" + "."
						+ getGenName(tableName) + "ServiceImpl");
		List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();

		FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
				introspectedTable.getMyBatis3JavaMapperType());
		FullyQualifiedJavaType pojoType = new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType());
		FullyQualifiedJavaType pojoExampleType = new FullyQualifiedJavaType(
				introspectedTable.getExampleType());

		Interface interface1 = new Interface(interfaceType);
		TopLevelClass topLevelClass = new TopLevelClass(serviceType);

		interface1.addImportedType(pojoType);
		interface1.addImportedType(pojoExampleType);
		interface1.addImportedType(listType);
		topLevelClass.addImportedType(mapperType);
		topLevelClass.addImportedType(interfaceType);
		topLevelClass.addImportedType(pojoType);
		topLevelClass.addImportedType(pojoExampleType);
		topLevelClass.addImportedType(new FullyQualifiedJavaType(PARENT_DAO_INTERFACE));
		topLevelClass.addImportedType(new FullyQualifiedJavaType(PARENT_SERVICE_IMPL));
		topLevelClass.addImportedType(listType);
		topLevelClass.addImportedType(slf4jLogger);
		topLevelClass.addImportedType(slf4jLoggerFactory);

		topLevelClass.addImportedType(service);
		topLevelClass.addImportedType(autowired);
		topLevelClass.addImportedType(transactional);

		// 接口
		adInterface(interface1, introspectedTable, files);

		// 实现类
		addServiceImpl(topLevelClass, introspectedTable, interfaceType, files);
		addLogger(topLevelClass);

		return files;
	}

	private void addLogger(TopLevelClass topLevelClass) {
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("LoggerFactory.getLogger("
				+ topLevelClass.getType().getShortName() + ".class)"); 
		field.setName("logger"); // set var name
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("Logger")); // type
		field.setVisibility(JavaVisibility.PUBLIC);
		topLevelClass.addField(field);
	}


	protected void adInterface(Interface interface1,
			IntrospectedTable introspectedTable, List<GeneratedJavaFile> files) {

		interface1.setVisibility(JavaVisibility.PUBLIC);

		FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(
				PARENT_SERVICE_INTERFACE+"<"
						+ introspectedTable.getBaseRecordType()
						+ ",java.lang.Long>");
		interface1.addSuperInterface(superInterface);
		// selectByExample
		interface1.addMethod(getSelectByExampleMethod(introspectedTable));
		// selectOneByExample
		interface1.addMethod(getSOneByExampleMethod(introspectedTable));
		interface1.addMethod(getupdateByExampleMethod(introspectedTable));
		interface1.addMethod(getupdateByExampleSelectiveMethod(introspectedTable));
		
		GeneratedJavaFile file = new GeneratedJavaFile(interface1, project,
				context.getJavaFormatter());
		//this.addCommentPlugin.clientGenerated(interface1, null, introspectedTable);
		files.add(file);
	}


	protected void addServiceImpl(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable,
			FullyQualifiedJavaType interfaceType, List<GeneratedJavaFile> files) {
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		// set implements interface
		topLevelClass.addSuperInterface(interfaceType);
		FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(
				PARENT_SERVICE_IMPL+"<"
						+ introspectedTable.getBaseRecordType()
						+ ",java.lang.Long>");
		topLevelClass.setSuperClass(superClass);

		topLevelClass.addAnnotation("@Service");
		topLevelClass.addAnnotation("@Transactional");
		// OrdCarMapper ordCarMapper;
		addMapperField(topLevelClass, introspectedTable);
		addDaoMethod(topLevelClass, introspectedTable);

		FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
				introspectedTable.getMyBatis3JavaMapperType());

		// selectByExample
		Method selectByExMethod = getSelectByExampleMethod(introspectedTable);
		selectByExMethod.addAnnotation("@Override");
		StringBuilder sb = new StringBuilder();
		sb.append("return ").append(getDaoFieldName(mapperType))
				.append(".selectByExample(e)").append(";");
		selectByExMethod.addBodyLine(sb.toString());
		topLevelClass.addMethod(selectByExMethod);
		// selectOneByExample
		Method sOneByExampleMethod = getSOneByExampleMethod(introspectedTable);
		sOneByExampleMethod.addAnnotation("@Override");
		sb = new StringBuilder();
		sb.append(
				"List<" + introspectedTable.getBaseRecordType()
						+ "> lists = this.")
				.append(getDaoFieldName(mapperType))
				.append(".selectByExample(e);");
		sOneByExampleMethod.addBodyLine(sb.toString());
		sOneByExampleMethod
				.addBodyLine("return (lists==null||lists.isEmpty())?null:lists.get(0);");
		topLevelClass.addMethod(sOneByExampleMethod);

		// selectByExample
		Method updateByExample = getupdateByExampleMethod(introspectedTable);
		updateByExample.addAnnotation("@Override");
		 sb = new StringBuilder();
		sb.append("return ").append(getDaoFieldName(mapperType))
				.append(".updateByExample(record,e)").append(";");
		updateByExample.addBodyLine(sb.toString());
		topLevelClass.addMethod(updateByExample);
		
		// updateByPrimaryKeySelective
		Method updateByPrimaryKeySelective = getupdateByExampleSelectiveMethod(introspectedTable);
		updateByPrimaryKeySelective.addAnnotation("@Override");
		 sb = new StringBuilder();
		sb.append("return ").append(getDaoFieldName(mapperType))
				.append(".updateByExampleSelective(record,e)").append(";");
		updateByPrimaryKeySelective.addBodyLine(sb.toString());
		topLevelClass.addMethod(updateByPrimaryKeySelective);
				
				
		GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project,
				context.getJavaFormatter());
		//this.addCommentPlugin.clientGenerated(null, topLevelClass, introspectedTable);
		files.add(file);
	}

	private Method getSelectByExampleMethod(IntrospectedTable introspectedTable) {
		Method method = new Method("selectByExample");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("java.util.List<"
				+ introspectedTable.getBaseRecordType() + ">"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType(
				introspectedTable.getExampleType()), "e"));
		return method;
	}

	private Method getSOneByExampleMethod(IntrospectedTable introspectedTable) {
		Method method = new Method("selectOneByExample");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType(introspectedTable
				.getBaseRecordType()));
		method.addParameter(new Parameter(new FullyQualifiedJavaType(
				introspectedTable.getExampleType()), "e"));
		return method;
	}
	
	private Method getupdateByExampleMethod(IntrospectedTable introspectedTable) {
		Method method = new Method("updateByExample");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType()), "record"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType(
				introspectedTable.getExampleType()), "e"));
		return method;
	}
	private Method getupdateByExampleSelectiveMethod(IntrospectedTable introspectedTable) {
		Method method = new Method("updateByExampleSelective");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType(
				introspectedTable.getBaseRecordType()), "record"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType(
				introspectedTable.getExampleType()), "e"));
		return method;
	}

	/**
	 * 属性 mamper
	 */
	protected void addMapperField(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {

		FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
				introspectedTable.getMyBatis3JavaMapperType());
		// add dao
		Field field = new Field();
		field.setName(toLowerCase(mapperType.getShortName())); // set var name
		topLevelClass.addImportedType(mapperType);
		field.setType(mapperType); // type
		field.setVisibility(JavaVisibility.PRIVATE);
		field.addAnnotation("@Autowired");
		topLevelClass.addField(field);
	}

	/**
	 * 添加 getDao()
	 */
	protected void addDaoMethod(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
				introspectedTable.getMyBatis3JavaMapperType());
		Method method = new Method();
		method.setName("getDao");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addAnnotation("@Override");
		method.addAnnotation("@SuppressWarnings(\"unchecked\")");
		StringBuilder sb = new StringBuilder();
		sb.append("return ").append(getDaoFieldName(mapperType)).append(";");
		method.addBodyLine(sb.toString());
		method.setReturnType(new FullyQualifiedJavaType(
				PARENT_DAO_INTERFACE+"<"
						+ introspectedTable.getBaseRecordType()
						+ ",java.lang.Long>"));
		topLevelClass.addMethod(method);
	}

	private String getDaoFieldName(FullyQualifiedJavaType mapperType) {
		return toLowerCase(mapperType.getShortName());
	}

	/**
	 * BaseUsers to baseUsers
	 * 
	 * @param tableName
	 * @return
	 */
	protected String toLowerCase(String tableName) {
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * BaseUsers to baseUsers
	 * 
	 * @param tableName
	 * @return
	 */
	protected String toUpperCase(String tableName) {
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}



	private String getModule(String tableName) {
		String[] tableSplits = tableName.split("_");
		if (tableSplits.length < 2) {
			log.warn("表名未遵守[module]_[NAME][+]约定，无法生成");
			return null;
		}
		return tableSplits[0];
	}

	private String getGenName(String tableName) {
		return underlineToCamel2(tableName.substring(
				tableName.indexOf("_") + 1, tableName.length()));
	}

	public static String underlineToCamel2(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(param);
		Matcher mc = Pattern.compile("_").matcher(param);
		int i = 0;
		while (mc.find()) {
			int position = mc.end() - (i++);
			// String.valueOf(Character.toUpperCase(sb.charAt(position)));
			sb.replace(position - 1, position + 1,
					sb.substring(position, position + 1).toUpperCase());
		}
		return captureName(sb.toString());
	}

	public static String captureName(String name) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return name;

	}

}
