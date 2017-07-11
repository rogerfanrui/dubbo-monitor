package org.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;

public class AddMergeIdentifyPlugin extends PluginAdapter {
	
	public static String IDENTITY_START="//---------------MERGE-PLUGIN-START------------------------";
	public static  String IDENTITY_END="//---------------MERGE-PLUGIN-END------------------------";
	public static String IDENTITY_XML_START="<!--WRITE-start-->";
	public static String IDENTITY_XML_END="<!--WRITE-end-->";
	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if(interfaze!=null &&!interfaze.getFileCommentLines().contains(IDENTITY_START)){
			interfaze.addFileCommentLine(IDENTITY_START);
			interfaze.addFileCommentLine("");
			interfaze.addFileCommentLine(IDENTITY_END);
		}
		if(topLevelClass !=null &&!topLevelClass.getFileCommentLines().contains(IDENTITY_START)){
			topLevelClass.addFileCommentLine(IDENTITY_START);
			topLevelClass.addFileCommentLine("");
			topLevelClass.addFileCommentLine(IDENTITY_END);
		}
		
		
		
		
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		if(topLevelClass !=null &&!topLevelClass.getFileCommentLines().contains(IDENTITY_START)){
			topLevelClass.addFileCommentLine(IDENTITY_START);
			topLevelClass.addFileCommentLine("");
			topLevelClass.addFileCommentLine(IDENTITY_END);
		}
		
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		if(topLevelClass !=null &&!topLevelClass.getFileCommentLines().contains(IDENTITY_START)){
			topLevelClass.addFileCommentLine(IDENTITY_START);
			topLevelClass.addFileCommentLine("");
			topLevelClass.addFileCommentLine(IDENTITY_END);
		}
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		Element start_textElement= new TextElement(IDENTITY_XML_START);
		Element end_textElement= new TextElement(IDENTITY_XML_END);
		
		if(!document.getRootElement().getElements().contains(start_textElement)){
			document.getRootElement().addElement(0, start_textElement);
			document.getRootElement().addElement(1,end_textElement );
		}
		
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}
	
	

}
