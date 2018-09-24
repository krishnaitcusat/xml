package apps.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;



public class XmlParser {
	
	MyXmlReader iMyXmlReader;
	Tag   iRootTag=new Tag();;
	Stack<Tag> iTagStack=new Stack<Tag>();
	HashMap<String,Integer> gPropValueIndexMap=new HashMap<String,Integer>();
	String          sUniquePropName;
	
	public XmlParser(String pFilePath) throws Exception
	{
		iMyXmlReader=new MyXmlReader(pFilePath);
	}
	
	
	private void log(Object pObject)
	{
		System.out.print(pObject.toString());
	}
	
	public boolean parse()
	{
		boolean isSuccess=true;
		Tag lCurrTag;
		try
		{
			Tag lNextTag=iMyXmlReader.getNextTag();
			while(lNextTag.getTagStr()!=null)
			{
				log("\nProcessing Tag : "+lNextTag.getTagStr());
				if(iTagStack.isEmpty())
				{
					log(" Tag is empty.Adding tag: "+lNextTag.getTagStr()+"\n");
					iTagStack.push(lNextTag);
					iRootTag=lNextTag;
				}
				else
				{
					if(lNextTag.getTagName().startsWith("</"))
					{
						lCurrTag=iTagStack.pop();// remove the top tag from the stack
						if(lCurrTag.getTagName().equals(lNextTag.getTagName().replace("/", "")))
						{
							lCurrTag.setEndTag(lNextTag);
							log(" End Tag Found : "+lNextTag.getTagStr()+".Removed It from Stack.Set it as EndTag for :"+lCurrTag.getTagStr()+"\n");
						}
						else
						{
							log("\nCould Not Found The Starting Tag For The Ending Tag : "+lNextTag.getTagStr()+"\n");
							return false;
						}
					}
					else
					{
						lCurrTag=iTagStack.peek();// pick the top tag from the stack dont remove
						lCurrTag.addTag(lNextTag);log(" .Adding Tag :"+lNextTag.getTagStr()+" in list of Tag : "+lCurrTag.getTagStr()+"\n");
						iTagStack.push(lNextTag);
						log("Pusing Tag : "+lNextTag.getTagStr());
					}
						
					
				}
				
				
				
				lNextTag=iMyXmlReader.getNextTag();
			}
			
			if(!iTagStack.isEmpty())
			{
				log("\nError: parsing Error. Invalid tags found as follow:\n");
				while(!iTagStack.isEmpty())
				{
					log(iTagStack.pop().getTagName()+"\n");
					return false;
				}
			}
			
		}
		catch(Exception ex)
		{ 
			ex.printStackTrace();
			System.out.println(ex);
			isSuccess=false;
		}
		
		log("\n Successfully Parsed The Xml File.");
		return isSuccess;
		
		
	}
	
	
	public void printDoc(Tag pTag)
	{
		//log(pTag.getTagName()+" : "+pTag.getIndex()+"\n");
		log(pTag.getContentBeforeTag()+pTag.getTagStr());
		for(Tag lTag:pTag.getTagList())
		{
			printDoc(lTag);
		}
		if(pTag.getEndTag()!=null)
		{
			log(pTag.getEndTag().getContentBeforeTag()+pTag.getEndTag().getTagStr());
		}
		
	}
	
	
	public void setPropMap(Tag pTag,String pParentPropValue)
	{
		String sUniquePropValue=null;
		String sTagStr=pTag.getTagStr();
		if(sTagStr!=null)
		{
			if(sUniquePropName!=null)
			{
				if(sTagStr.contains(sUniquePropName+"=\""))
				{
					int startIndex=sTagStr.indexOf(sUniquePropName+"=\"");
					startIndex=sTagStr.indexOf("\"",startIndex);
					int endIndex=sTagStr.indexOf("\"", ++startIndex);
					sUniquePropValue=sTagStr.substring(startIndex, endIndex);
					if(pParentPropValue!=null)
						sUniquePropValue=pParentPropValue+"."+sUniquePropValue;
					gPropValueIndexMap.put(sUniquePropValue, pTag.getIndex());
				}
			}
		}
		
		for(Tag lTag:pTag.getTagList())
		{
			setPropMap(lTag,sUniquePropValue);
		}
		
	}
	
	
	
	public void setTagIndex(Tag pTag,String pParentPropValue)
	{
		String sUniquePropValue=null;
		String sTagStr=pTag.getTagStr();
		if(sTagStr!=null)
		{
			if(sUniquePropName!=null)
			{
				if(sTagStr.contains(sUniquePropName+"=\""))
				{
					System.out.println(pTag.getTagStr()+":"+pTag.getIndex());
					int startIndex=sTagStr.indexOf(sUniquePropName+"=\"");
					startIndex=sTagStr.indexOf("\"",startIndex);
					int endIndex=sTagStr.indexOf("\"", ++startIndex);
					sUniquePropValue=sTagStr.substring(startIndex, endIndex);
					Integer lIndex=null;
					if(pParentPropValue!=null)
						sUniquePropValue=pParentPropValue+"."+sUniquePropValue;
					lIndex=gPropValueIndexMap.get(sUniquePropValue);
					if(lIndex==null||lIndex==-1)
					{
						//System.out.println(pTag.getTagStr()+":"+pTag.getIndex());
						pTag.setIndex(100);
						System.out.println("\nNot Found : "+pParentPropValue+"."+sUniquePropValue);
					}
					else
						pTag.setIndex(lIndex);
					System.out.println(pTag.getTagStr()+":"+pTag.getIndex());
				}
			}
		}
		
		for(Tag lTag:pTag.getTagList())
		{
			setTagIndex(lTag,sUniquePropValue);
		}
		
	}
	
	
	
	
	public void sortTags(Tag pTag)
	{
		if(pTag.getTagList()!=null&&pTag.getTagList().size()>1)
		Collections.sort(pTag.getTagList());
		
		for(Tag lTag:pTag.getTagList())
		{
			sortTags(lTag);
		}
		
	}
	
	
	
	
	
	
	
	public int getIndexOf(String pPropValue)
	{
		int index=-1;
		Integer indx=gPropValueIndexMap.get(pPropValue);
		if(indx!=null)
			index=indx;
		return index;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		XmlParser xmlParser=new XmlParser("/home/krishna/Desktop/book.xml");
        xmlParser.setUniquePropName("id");
		xmlParser.parse();
		//xmlParser.printDoc(xmlParser.iRootTag);
		xmlParser.setPropMap(xmlParser.iRootTag,null);
		
		XmlParser xmlParser2=new XmlParser("/home/krishna/Desktop/book1.xml");
        xmlParser2.setUniquePropName("id");
		xmlParser2.parse();
		xmlParser2.gPropValueIndexMap=xmlParser.gPropValueIndexMap;
		xmlParser2.setTagIndex(xmlParser2.iRootTag, null);
		xmlParser2.sortTags(xmlParser2.iRootTag);
		System.out.println("\n--------------------------------------------------------------------------------------------------------------");
		xmlParser2.printDoc(xmlParser2.iRootTag);
		
		

	}


	public void setUniquePropName(String pPropName) {
		// TODO Auto-generated method stub
		sUniquePropName=pPropName;
	}

}