package org.mybatis.generator.codegen.mybatis3;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个插件主要用来rename mapper以及xml的名称
 * @author fanrui
 *
 */
public class RenameModuleNamePlugin extends PluginAdapter {

	private Logger log = LoggerFactory.getLogger(RenameModuleNamePlugin.class);

	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		super.initialized(introspectedTable);
		TableConfiguration tc = introspectedTable.getTableConfiguration();
		String tableName = tc.getTableName();
		String domainName = tc.getDomainObjectName();
		String mapperName = tc.getMapperName();

		String moduleName = getModule(tableName);
		String updateDomainName = null;
		String updateMapperName = null;
		if (!StringUtils.isNotBlank(domainName) && moduleName != null) {
			updateDomainName = moduleName + "." + getGenName(tableName) + "Dto";
			tc.setDomainObjectName(updateDomainName);
		}
		if (!StringUtils.isNotBlank(mapperName) && moduleName != null) {
			updateMapperName = moduleName + "." + getGenName(tableName)
					+ "Mapper";
			tc.setMapperName(updateMapperName);
		}

		if (updateDomainName != null || updateMapperName != null) {
			FullyQualifiedTable table = new FullyQualifiedTable(
					tc.getCatalog(),
					tc.getSchema(),
					tc.getTableName(),
					tc.getDomainObjectName(),
					tc.getAlias(),
					isTrue(tc
							.getProperty(PropertyRegistry.TABLE_IGNORE_QUALIFIERS_AT_RUNTIME)),
					tc.getProperty(PropertyRegistry.TABLE_RUNTIME_CATALOG),
					tc.getProperty(PropertyRegistry.TABLE_RUNTIME_SCHEMA),
					tc.getProperty(PropertyRegistry.TABLE_RUNTIME_TABLE_NAME),
					false, introspectedTable.getContext());

			introspectedTable.setFullyQualifiedTable(table);

			introspectedTable.initialize();
		}
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
