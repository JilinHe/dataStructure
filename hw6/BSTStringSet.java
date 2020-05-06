import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Jilin He
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if (!this.contains(s)) {
            _root = putHelper(_root, s);
        }
    }

    @Override
    public boolean contains(String s) {
        return containsHelper(_root, s);
    }

    @Override
    public List<String> asList() {
        if (listHelper(_root) == null) {
            return new ArrayList<String>();
        }
        List<String> temp2 = new ArrayList<String>();
        BSTIterator bstiter = new BSTIterator(_root);
        while (bstiter.hasNext()) {
            temp2.add(bstiter.next());
        }
        return temp2;
    }

    /** List Helper function. */
    public String listHelper(Node node) {
        if (_root == null) {
            return null;
        }
        if (node.left == null && node.right == null) {
            return node.s;
        } else if (node.left != null && node.right != null) {
            return listHelper(node.left) + " " + node.s + " " + listHelper(node.right);
        } else if (node.left != null) {
            return listHelper(node.left) + " " + node.s;
        } else {
            return node.s + " " + listHelper(node.right);
        }
    }

    /** Put Helper function. */
    public Node putHelper(Node node, String s) {
        if (node == null) {
            return new Node(s);
        }
        if (s.compareTo(node.s) == 0) {
            node.s = s;
        } else if (s.compareTo(node.s) < 0) {
            node.left = putHelper(node.left, s);
        } else {
            node.right = putHelper(node.right, s);
        }
        return node;
    }

    /** Contain Helper function. */
    public boolean containsHelper(Node node, String s) {
        if (node == null) {
            return false;
        }
        if (s.compareTo(node.s) == 0) {
            return true;
        } else if (s.compareTo(node.s) < 0) {
            return containsHelper(node.left, s);
        } else {
            return containsHelper(node.right, s);
        }
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // @Override
    public Iterator<String> iterator(String low, String high) {
        Stack<Node> todo = new Stack<Node>();
        Stack<Node> todo1 = new Stack<Node>();
        iteratorHelper(_root, low, high, todo);
        while (!todo.empty()) {
            Node node = todo.pop();
            node.right = null;
            node.left = null;
            todo1.push(node);
        }
        BSTIterator iter = new BSTIterator(null);
        iter._toDo = todo1;
        return iter;
    }

    public void iteratorHelper(Node node, String low, String high, Stack<Node> todo) {
        if (node != null) {
            int comLeft = low.compareTo(node.s);
            int comRight = high.compareTo(node.s);
            if (comLeft < 0) {
                iteratorHelper(node.left, low, high, todo);
            }
            if (comLeft <= 0 && comRight > 0) {
                todo.push(node);
            }
            if (comRight > 0) {
                iteratorHelper(node.right, low, high, todo);
            }
        }
    }

    /** Root node of the tree. */
    private Node _root;
}
