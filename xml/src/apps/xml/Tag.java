package apps.xml;

import java.util.ArrayList;
import java.util.HashMap;

public class Tag implements Comparable<Tag> {
	
	private String sTagStr;
	private String sContentBeforeTag;
	private ArrayList<Tag> alTagList=new ArrayList<Tag>();
	private Tag sEndTag;
	private String sTagName;
	private String sUniquePropName;
	private String sUniquePropValue;
	private int index=1;
	private int listCount=0;
	
	
	
	
	
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getUniquePropName() {
		return sUniquePropName;
	}
	public void setUniquePropName(String sUniquePropName) {
		this.sUniquePropName = sUniquePropName;
	}
	public String getUniquePropValue() {
		return sUniquePropValue;
	}
	public void setUniquePropValue(String sUniquePropValue) {
		this.sUniquePropValue = sUniquePropValue;
	}
	public String getTagName() {
		return sTagName;
	}
	public Tag getEndTag() {
		return sEndTag;
	}
	public void setEndTag(Tag sEndTag) {
		this.sEndTag = sEndTag;
	}
	public ArrayList<Tag> getTagList() {
		return alTagList;
	}
	public String getTagStr() {
		return sTagStr;
	}
	public void setTagStr(String sTagStr) {
		this.sTagStr = sTagStr;
		
		if(sTagStr!=null)
		{
			sTagName=sTagStr.split(" ")[0];
			if(!sTagName.endsWith(">"))
				sTagName=sTagStr.split(" ")[0]+">";
			if(sUniquePropName!=null)
			{
				if(sTagStr.contains(sUniquePropName+"=\""))
				{
					int startIndex=sTagStr.indexOf(sUniquePropName+"=\"");
					startIndex=sTagStr.indexOf("\"",startIndex);
					int endIndex=sTagStr.indexOf("\"", ++startIndex);
					//System.out.print(sTagStr+" : "+startIndex+":"+endIndex);
					sUniquePropValue=sTagStr.substring(startIndex, endIndex);
					//System.out.println(" : "+sUniquePropValue);
				}
			}
		}
		
		
	}
	public String getContentBeforeTag() {
		return sContentBeforeTag;
	}
	public void setContentBeforeTag(String sContentBeforeTag) {
		this.sContentBeforeTag = sContentBeforeTag;
	}
	public void addTag(Tag pTag)
	{
		listCount++;
		pTag.setIndex(listCount);
		alTagList.add(pTag);
	}
	
	public void setPropValueIndexMap(HashMap<String,HashMap<String,Integer>> pPropValueIndexMap)
	{
		
		
	}
	@Override
	public int compareTo(Tag arg0) {
		// TODO Auto-generated method stub
		if(arg0.getIndex()>this.index)
			return -1;
		else if(arg0.getIndex()<this.index)
			return 1;
		else
			return 0;
	}

}