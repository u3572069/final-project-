package busInfo;
import java.util.ArrayList;

public class VancouverBusStopSearch {
	
	private Node root;
	
	private class Node {
        private char value;           // sorted by key
        private int key;       // associated data
        private Node left, eq, right;  // left and right subtrees

        private Node(char value) {
            this.value = value;
            key = 0;
            left = null;
            eq = null;
            right = null;
        }
    }
	
	public VancouverBusStopSearch()
	{
		root = null;
	}
	
	public void insert(String word, int k)
	{
		root = insert(root,word,k);
	}
	
	private Node insert(Node r, String word, int k)
	{
		if (r == null)
            r = new Node(word.charAt(0));
 
        if (word.charAt(0) < r.value)
            r.left = insert(r.left, word, k);
        else if (word.charAt(0) > r.value)
            r.right = insert(r.right, word, k);
        else
        {
            if (word.substring(1).length() != 0)
                r.eq = insert(r.eq, word.substring(1), k);
            else
                r.key = k;
        }
        return r;
	}
	
	private Node search(String word)
	{
		return search(root, word); 
	}
	
	private Node search(Node r, String word)
	{
		if(r == null || word.length() == 0)
		{
			return null;
		}
		if(word.charAt(0) < r.value)
		{
			return search(r.left, word);
		}
		else if(word.charAt(0) > r.value)
		{
			return search(r.right, word);
		}
		else
		{
			if(word.substring(1).length() == 0)
			{
				return r;
			}
			else return search(r.eq, word.substring(1));
		}
		
	}
	
	public void match(String word, ArrayList<String> stops) 
	{
		if(search(word) == null)
		{
			System.out.println("No match fitting your criteria.");
		}
		else
		{
			Node node = search(word);
			if(node.eq == null)
			{
				System.out.println("Stop ID: " + stops.get(node.key));
				System.out.println("Stop Code: " + stops.get(node.key+1));
				System.out.println("Stop Name: " + stops.get(node.key+2));
				System.out.println("Stop ID: " + stops.get(node.key+3));
				System.out.println("Stop Desc: " + stops.get(node.key+4));
				System.out.println("Zone ID: " + stops.get(node.key+5));
				System.out.println("Stop UR;: " + stops.get(node.key+6));
				System.out.println("Location Type: " + stops.get(node.key+7));
			}
				
			else match(node.eq,stops);
		} 
	}
	
	private void match(Node r,ArrayList<String> stops)
	{
		if(r != null)
		{
			match(r.left, stops);
			if(r.key != 0)
			{
				System.out.println("Stop ID: " + stops.get(r.key));
				System.out.println("Stop Code: " + stops.get(r.key+1));
				System.out.println("Stop Name: " + stops.get(r.key+2));
				System.out.println("Stop ID: " + stops.get(r.key+3));
				System.out.println("Stop Desc: " + stops.get(r.key+4));
				System.out.println("Zone ID: " + stops.get(r.key+5));
				System.out.println("Stop UR;: " + stops.get(r.key+6));
				System.out.println("Location Type: " + stops.get(r.key+7));
			}
			match(r.eq, stops);
			match(r.right, stops);
		}
	}
}
