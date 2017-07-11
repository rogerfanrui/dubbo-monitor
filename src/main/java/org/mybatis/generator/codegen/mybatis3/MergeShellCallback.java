
package org.mybatis.generator.codegen.mybatis3;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;



/**
 * 
 * @author fanrui
 *
 */
public class MergeShellCallback extends DefaultShellCallback {
   

  
    public MergeShellCallback(boolean overwrite) {
        super(overwrite);
       
    }
    
    public boolean isMergeSupported() {
        return true;
    }
    

    public String mergeJavaFile(String newFileSource,
            String existingFileFullPath, String[] javadocTags, String fileEncoding)
            throws ShellException {
    	JavaFileMerge jm = new JavaFileMerge(newFileSource, existingFileFullPath, javadocTags, fileEncoding);
    	if(jm.canMerge()){
    		return jm.merge();
    	}
    	XmlFileMerge xm = new XmlFileMerge(newFileSource, existingFileFullPath, javadocTags, fileEncoding);
    	if(xm.canMerge()){
    		return xm.merge();
    	}
    	
    	return newFileSource;
        
    }





}
