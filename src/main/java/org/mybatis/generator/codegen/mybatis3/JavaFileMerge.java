package org.mybatis.generator.codegen.mybatis3;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.mybatis.generator.plugin.AddMergeIdentifyPlugin;

public class JavaFileMerge {
	private String newFileSource;
	private String existingFileFullPath;
	private String fileEncoding;
	private String fileContent;
	private String realMergeFile;

	public JavaFileMerge(String newFileSource, String existingFileFullPath,
			String[] javadocTags, String fileEncoding) {
		this.newFileSource = newFileSource;
		this.existingFileFullPath = existingFileFullPath;
		this.fileEncoding = fileEncoding;
		try {
			fileContent = FileUtils.readFileToString(new File(
					existingFileFullPath), fileEncoding);
			realMergeFile = getMergeFile(fileContent);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean canMerge() {
		if (realMergeFile == null || realMergeFile.equals("")) {
			return false;
		}
		return true;
	}

	private String getMergeFile(String content) {
		Pattern p = Pattern.compile(AddMergeIdentifyPlugin.IDENTITY_START
				+ "\\s*(.*)\\s*" + AddMergeIdentifyPlugin.IDENTITY_END, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(content);
		String result = null;
		while (m.find()) {
			result = (m.group(1)).trim();
		}
		return result;
	}

	public String merge() {
		return newFileSource.subSequence(0, newFileSource.lastIndexOf("}") - 1)+"\t"+AddMergeIdentifyPlugin.IDENTITY_START
				+ "\n\t" + realMergeFile+"\n\t"+ AddMergeIdentifyPlugin.IDENTITY_END+ "\n}";

	}

	public static void main(String[] args) throws IOException {
		
		String str =	 FileUtils.readFileToString(new File(
				"D:\\git\\renren-security-boot\\src\\main\\java\\com\\rogerfan\\veclean\\service\\res\\impl\\ShopCardServiceImpl.java"), "UTF-8");
		Pattern p = Pattern.compile(AddMergeIdentifyPlugin.IDENTITY_START
				+ "\\s*(.*)\\s*" + AddMergeIdentifyPlugin.IDENTITY_END, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(str);
		String result = null;
		while (m.find()) {
			result = (m.group(1)).trim();
		}
		System.out.println(result); 
		
		
	}
}
