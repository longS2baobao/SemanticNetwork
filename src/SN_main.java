import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class SN_main {
	public static Hashtable<String,Hashtable<String,String>> Hst_SN = new Hashtable<String,Hashtable<String,String>>();
	
	public static void main(String[] args) {
		loadData();
		while(true)
		{
			try {
				searchSN();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadData()
	{		
		//load data to Hst_SN			
		Hashtable<String, String> hst = new Hashtable<String,String>();
		hst.put("instance_of", "snowman");
		Hst_SN.put("frosty", hst);
		
		Hashtable<String, String> hst1 = new Hashtable<String,String>();
		hst1.put("made_of", "snow");
		Hst_SN.put("snowman", hst1);
		
		Hashtable<String, String> hst2 = new Hashtable<String,String>();
		hst2.put("form_of", "water");
		hst2.put("hardness", "soft");
		hst2.put("texture", "slippery");
		hst2.put("color", "white");
		hst2.put("temperature", "cold");
		Hst_SN.put("snow", hst2);
		
		Hashtable<String, String> hst3 = new Hashtable<String,String>();
		hst3.put("form_of", "water");
		hst3.put("hardness", "hard");
		hst3.put("texture", "slippery");
		hst3.put("color", "clear");
		hst3.put("temperature", "cold");
		Hst_SN.put("ice", hst3);
	}
	
	public static void searchSN() throws IOException
	{
		//read question
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		System.out.println("Enter Question: ");
		String question = br.readLine();
		
		//parse question
		String node_1="";
		String attr="";
		String node_2="";
		String[] words = question.split(" ");
		for (String word : words)
		{
			//test the current word is a node
			if (Hst_SN.containsKey(word))
			{
				node_1 = word;
			}
			//test the current word is a relation
			for(String s : Hst_SN.keySet())
			{
				if(Hst_SN.get(s).containsKey(word))	
				{
					attr = word;
				}
				if(Hst_SN.get(s).containsValue(word))
				{
					node_2 = word;
				}
			}
		}
		
		if ((!node_1.isEmpty()||!node_2.isEmpty())&&!attr.isEmpty())
		{
			if(!node_2.isEmpty())
			{
				// do a backward search when the node appears as an 
				// Node1 <- Relation <- Node2
				if(!search_each_child_backward(node_2,attr))
				{
					// when backward search can't find the value
					// the question is asking for a forward search
					search_each_child_forward(node_1,attr);
				}
			}
			else
			{
				// Node1 -> Relation -> Node2
				search_each_child_forward(node_1,attr);
			}
		}
		else if (!attr.isEmpty())
		{
			search_attribute(attr);
		}
	}
	
	public static boolean search_each_child_forward(String node, String attr)
	{
		if (Hst_SN.containsKey(node))
		{
			// attribute found
			if (Hst_SN.get(node).containsKey(attr)) 
			{
				System.out.println(Hst_SN.get(node).get(attr));
				return true;
			}
			else
			{
					//find the node attribute points to
					for (String attribute : Hst_SN.get(node).keySet())
					{
						String new_node = Hst_SN.get(node).get(attribute);
						search_each_child_forward(new_node,attr);
					}
			}
		}
		return false;
	}
	
	public static boolean search_each_child_backward(String b_node, String attr)
	{
		boolean isFound = false;
		for (String f_node : Hst_SN.keySet())
		{
			if (Hst_SN.get(f_node).containsKey(attr))
			{
				if (Hst_SN.get(f_node).get(attr).equalsIgnoreCase(b_node))
				{
					System.out.println(f_node);
					isFound = true;
				}
			}
		}
		return isFound;
	}
	
	public static boolean search_attribute(String attr)
	{
		boolean isFound = false;
		for (String node : Hst_SN.keySet())
		{
			if (Hst_SN.get(node).containsKey(attr))
			{
				System.out.println(Hst_SN.get(node).get(attr));
				isFound= true;
			}
		}
		return isFound;
	}
}
