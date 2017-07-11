package org.mybatis.generator.codegen.mybatis3;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.dom.xml.Document;

/**
 * 该类主要是为了解决xml 每次生成重复的问题
 * @author fanrui
 *
 */
public class CustomerIntrospectedTableMyBatis3Impl extends IntrospectedTableMyBatis3Impl{
	 @Override
	    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
	        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

	        if (xmlMapperGenerator != null) {
	            Document document = xmlMapperGenerator.getDocument();
	            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
	                getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
	                context.getSqlMapGeneratorConfiguration().getTargetProject(),
	                false, context.getXmlFormatter());
	            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
	                answer.add(gxf);
	            }
	        }

	        return answer;
	    }
}
