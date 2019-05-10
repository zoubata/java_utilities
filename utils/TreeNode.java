package com.zoubworld.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;



public class TreeNode<T> /*implements Iterable<TreeNode<T>>*/ {
	TreeNode tree;// this is the tree that contain any node
    T data;
    TreeNode<T> parent;
//    List<TreeNode<T>> children;
    Set<TreeNode<T>> children;

    /** add a link between a father and a child node's, if it doesn't exist it create the node(s).
     * */
    public void addFatherChild(T fatherdata,T childdata)
    {
    	if (tree==null)
    		tree=this;
    	{
    	TreeNode<T> father=tree.getNode(fatherdata);
    	TreeNode<T> child=tree.getNode(childdata); 
    	if (father==null)
    		father=new TreeNode(tree,fatherdata);
    	if (child==null)
    		child=new TreeNode(tree,childdata);    	
    	father.children.add(child);
    	child.SetParent(father);
    	}
    	
    }
    
    public void SetParent(TreeNode<T> father) {
    	parent=father;
		
	}

	/**
	 * @return the parent
	 */
	public TreeNode<T> getParent() {
		return parent;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	private TreeNode<T> getNode(T fatherdata) {
//    	for (TreeNode<T> tn:((List<TreeNode<T>> )tree.children))
        for (TreeNode<T> tn:((Set<TreeNode<T>> )tree.children))
    		if(tn.data==fatherdata)
    			return tn;
		return null;
	}

	public TreeNode(TreeNode treeRoot,T data) {
    	tree=treeRoot;
//      this.children = new LinkedList<TreeNode<T>>();       
      this.children = new HashSet<TreeNode<T>>();       
        this.data = data;
        if (tree!=null)
            if (tree!=this)                	
            	tree.children.add(this);
    }

    /**
	 * @return the "tree name" it contain any node related to this tree even if tey aren't like together
	 */
	public TreeNode getTree() {
		return tree;
	}

	public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(tree, child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

	public T getData() {
		
		return data;
	}

//	public List<TreeNode<T>>  getChilds() {
	public Set<TreeNode<T>>  getChilds() {
		
		return children;
	}
	
	public Set<TreeNode<T>>  getRoots() {
		
		 Set<TreeNode<T>> list=new HashSet<TreeNode<T>> ();
//			for(TreeNode<T> t:(List<TreeNode<T>>) tree.getChilds())			
		 for(TreeNode<T> t:(Set<TreeNode<T>>) tree.getChilds())			
		 list.add(t.getOlderParent());
		return list;
	}

	private TreeNode<T> getOlderParent() {
		TreeNode<T> t=this;
	//	System.out.print(t.getData()+"<")	;
		while(t.getParent()!=null)
		{
			t=t.getParent();
	//	System.out.print(t.getData()+"<")	;
		}
	//	System.out.println();
		return t;
	}

	/*@Override
	public Iterator<TreeNode<T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}*/

    // other features ...
/*
 * TreeNode<String> root = new TreeNode<String>("root");
{
    TreeNode<String> node0 = root.addChild("node0");
    TreeNode<String> node1 = root.addChild("node1");
    TreeNode<String> node2 = root.addChild("node2");
    {
        TreeNode<String> node20 = node2.addChild(null);
        TreeNode<String> node21 = node2.addChild("node21");
        {
            TreeNode<String> node210 = node20.addChild("node210");
        }
    }
}
*/
	

	  @Override
	  public String toString() {
	    return printTree(0);
	  }

	  private static final int indent = 15;

	  private String printTree(int increment) {
	    String s = "\n";
	    String inc = "";
	    for (int i = 0; i < increment; ++i) {
	      inc = inc + " ";
	    }
	    s = inc +"->"+ data.toString();
	    for (TreeNode<T> child : this.children) {
	      s += "\n" + child.printTree(increment + indent);
	    }
	    return s;
	  }
}