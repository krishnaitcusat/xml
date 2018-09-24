package apps.xml;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class MyXmlReader {
	
	FileInputStream finFilein;
	String          sUniquePropName;
	
	public void setUniquePropName(String sUniquePropName) {
		this.sUniquePropName = sUniquePropName;
	}

	public MyXmlReader(String pFilePath) throws FileNotFoundException
	{
		FileInputStream lFilein=new FileInputStream(pFilePath);
		finFilein=lFilein;
	}
	
	public void close() throws IOException
	{
		finFilein.close();
	}
	
	public Tag getNextTag() throws Exception
	{
		Tag lNextTag=new Tag();
		lNextTag.setUniquePropName(sUniquePropName);
		StringBuilder lTag=new StringBuilder();
		StringBuilder lCBT=new StringBuilder();
		int lNextChar;
		
		lNextChar=finFilein.read();
		while(lNextChar!=-1&&lNextChar!='<')
		{
			lCBT.append((char)lNextChar);
			lNextChar=finFilein.read();
		}
		if(lNextChar!=-1)
			lTag.append((char)lNextChar);
		lNextChar=finFilein.read();
		while(lNextChar!=-1&&lNextChar!='>')
		{
			lTag.append((char)lNextChar);
			lNextChar=finFilein.read();
		}
		
		if(lNextChar!=-1)
			lTag.append((char)lNextChar);
		
		lNextTag.setContentBeforeTag(lCBT.toString());
		String lTagStr=lTag.toString().trim().equals("")?null:lTag.toString();
		if(lTagStr!=null&&!lTagStr.endsWith(">"))
			throw new Exception("Invalid Tag Found : "+lTagStr);
		lNextTag.setTagStr(lTagStr);
		return lNextTag;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		MyXmlReader lXmlReader=new MyXmlReader("/home/krishna/Desktop/book.xml");
		lXmlReader.setUniquePropName("id");
		Tag lTag=lXmlReader.getNextTag();
		while(lTag.getTagStr()!=null)
		{
			//System.out.print(lTag.getContentBeforeTag()+lTag.getTagStr());
			lTag=lXmlReader.getNextTag();
		}
		//System.out.println("ContentBeforeTag : "+lTag.getContentBeforeTag());
		//System.out.println("Tag              : "+lTag.getTag());
		lXmlReader.close();

	}

}