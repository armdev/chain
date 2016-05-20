package demo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class BlockChain {

    @Autowired
    private Hasher hasher;

    private final Map<String, Leaf> leaves = new HashMap<String, Leaf>();

//    private final List<Node> nodes = new ArrayList<Node>();

//    private Node mostRecent;

    private BTree root;

    private boolean insertToRight = true;

    public String addEntry(String entry) {
        //create guid for leaf entry
        String key = UUID.randomUUID().toString();

        //hash the entry
        String hash = hasher.hash(entry);

        //create a leaf for the entry
        Leaf leaf = new Leaf(hash);
        leaves.put(key, leaf);

//        Node node = new Node();
//        node.hash = hash;
//
//        //add node to the entries collection
//        nodes.add(node);

        //update the tree
        updateTree(leaf);

        //set this node as the most recent

        //update the tree

        //set the new root node

        //return the guid
        return key;

    }

    private void updateTree(Node leaf) {
        //special case, if this is the first leaf
        if(root == null) {
            root = leaf;
//            mostRecent = leaf;
//            root.left = leaf;
//            root.right = leaf;
            return;
        }

        //otherwise add the leaf to the "right" of the tree

        if(insertToRight) {
            root = insertToRight(leaf.getHash(), root);
            insertToRight = false;
        } else {
            root = insertToLeft(leaf.getHash(), root);
            insertToRight = true;
        }

//        //if there is only one thing in the tree....
//        if (root instanceof Leaf) {
//            root = new Node(leaf.getValue(), leaf, new Leaf());
//        } else // t must be a Node:
//        {
//            BinaryTree right_answer = insertToRight(v, t.right());
//            // build answer tree by combining existing left subtree with newly
//            //  constructed right subtree with existing value in a new Node:
//            answer = new Node(t.value(), t.left(), right_answer);
//        }
//        return answer;
//        // Notice that this method does _not_ change any links within tree,  t !
//        //  Instead, it builds an  answer  tree that shares most of t's subtrees
//        //  and has new Nodes along the path from  answer's  root to where
//        //  v  is inserted.
//
//
//        //build up tree starting with new leaf and most recent leaf
//        createNodePath(mostRecent, leaf);

        //recalculate hashes up the new path
//        recalcHashes(leaf);
    }

    public BTree insertToRight(String hash, BTree existing) {
        //just one entry in existing?
        if (existing instanceof Leaf) {
            String newHash = hasher.hash(existing.getHash() + hash);
            return new Node(newHash, existing, new Leaf(hash));
        }

        BTree right = insertToRight(hash, existing.getRight());

        // build answer tree by combining existing left subtree with newly
        //  constructed right subtree with existing value in a new Node:
        return new Node(existing.getHash(), existing.getLeft(), right);

        // Notice that this method does _not_ change any links within tree,  t !
        //  Instead, it builds an  answer  tree that shares most of t's subtrees
        //  and has new Nodes along the path from  answer's  root to where
        //  v  is inserted.
    }

    public BTree insertToLeft(String hash, BTree existing) {
        //just one entry in existing?
        if (existing instanceof Leaf) {
            String newHash = hasher.hash(existing.getHash() + hash);
            return new Node(newHash, new Leaf(hash), existing);
        }

        BTree left = insertToLeft(hash, existing.getLeft());

        // build answer tree by combining existing left subtree with newly
        //  constructed right subtree with existing value in a new Node:
        return new Node(existing.getHash(), left, existing.getRight());

        // Notice that this method does _not_ change any links within tree,  t !
        //  Instead, it builds an  answer  tree that shares most of t's subtrees
        //  and has new Nodes along the path from  answer's  root to where
        //  v  is inserted.
    }

//    //TODO first add/adjust brtanches and then recalc hashes?
//    private void createNodePath(Node left, Node right) {
//        //are we at the root level?
//        if(root.equals(left)) {
//            //is there room for right in root?
//            if(root.right == null) {
//                root.right = right;
////                root.hash = hasher.hash(left.hash + right.hash);
//                return;
//            } else {
//                //need a new root!
//                Node newRoot = new Node();
////                newRoot.hash = hasher.hash(left.hash + right.hash);
//                newRoot.left = left;
//                newRoot.right = right;
//                left.parent = newRoot;
//                right.parent = newRoot;
//                root = newRoot;
//                return;
//            }
//        }
//
//        //otherwise we are still working our way to the top
//
//        //is the left node is an only child?
//        if(left.parent.right == null) {
//            //in this case we need to pair it with the new node
////            left.parent.hash = hasher.hash(left.hash + right.hash);
//            right.parent = left.parent;
//            right.parent.right = right;
////            nodes.add(right);
//        } else {
//            //left node already has a sibling, so we need to create a new parent node
//            Node newParent = new Node();
//            newParent.left = right;
//            right.parent = newParent;
//
//            //continue up the next tier
//            createNodePath(left.parent, newParent);
//        }
//
//
//        //situation two, the most recent node already as a sibling
//        //in this case we need to create anew branch for the new node.
//
//
//
//        //otherwise keep going
////        createNodePath();
//    }

//    private void recalcHashes(Node node) {
//        //special case: we are at the root and it's the only entry
//        if(node.right == null && node.left == null) {
//            return;
//        }
//
//        //node has no sibling, just inherit child hash
//        if(node.right == null) {
//            node.hash = node.left.hash;
//        } else {
//            //set has to the hash of the concatenated child hashes
//            node.hash = hasher.hash(node.left.hash + node.right.hash);
//        }
//
//        //are we at the root?
//        if(root.equals(node)) {
//            return;
//        } else {
//            //keep going
//            recalcHashes(node.parent);
//        }
//    }

    //is this the right entry for this id?
    public boolean validate(String id, String entry) {
        Node leaf = leaves.get(id);
        if(leaf == null) {
            return false;
        }

        return leaf.getHash().equals(hasher.hash(entry));
    }

    public void print() {
        printContentsIndented("  ", root);
        System.out.println("\n");
    }

    /**
     * printContentsIndented prints tree  t  with initial indentation  indent
     */
    private void printContentsIndented(String indent, BTree t) {
        if (t instanceof Leaf) {
            System.out.println(indent + "Leaf: value = " + t.getHash());
        } else // t instance of Node:
        {
            System.out.println(indent + "Node--------");
            String new_indent = indent + "| ";
            // the  toString  method concocts a string value for an object:
            System.out.println(new_indent + "value = " + t.getHash());
            System.out.println(new_indent + "left =");
            printContentsIndented(new_indent + "  ", t.getLeft());
            System.out.println(new_indent + "right =");
            printContentsIndented(new_indent + "  ", t.getRight());
        }
    }

//    //validate the tree, from the specified leaf up to the root
//    public boolean validate(String id) {
//        Node leaf = leaves.get(id);
//        if(leaf == null) {
//            return false;
//        }
//
//        //special case where there is only one entry
//        if(root.equals(leaf)) {
//            return true;
//        }
//
//        return validate(leaf);

        //climb the tree and validate path to the root node
//        Node leaf = leaves.get(id);
//        if(leaf == null) {
//            return false;
//        }

//        return false;
//    }

//    private boolean validate(Node node) {
//        //special case, only one entry
//        if(root.equals(node)) {
//            return true;
//        }
//
//        //if no left and right, then its a leaf, go up a level
//        if(node.left == null && node.right == null) {
//            return validate(node.parent);
//        }
//
//        //concatenate and hash left and right, compare to hash
//        if( hasher.hash(node.left.hash + node.right.hash).equals(node.hash)) {
//            //node hash is good, are we at the root?
//            if(root.equals(node)) {
//                return true;
//            }
//            else {
//                return validate(node.parent);
//            }
//        }
//        //node hash was bad
//        return false;
//    }
//
//    public boolean validate() {
//        for(Node leaf : leaves.values()) {
//            if( ! validate(leaf) ) {
//                return false;
//            }
//        }
//        return true;
//    }
}
