import java.util.ArrayList;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
 * store Comparable objects. Instead, it can store any type of object
 * (represented by type T) and an associated priority value.
 * @author Jilin He */
public class ArrayHeap<T> {

    /* DO NOT CHANGE THESE METHODS. */

    /** An ArrayList that stores the nodes in this binary heap. */
    private ArrayList<Node> contents;

    /** A constructor that initializes an empty ArrayHeap. */
    public ArrayHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /** Returns the number of elements in the priority queue. */
    public int size() {
        return contents.size() - 1;
    }

    /** Returns the node at index INDEX. */
    private Node getNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /** Sets the node at INDEX to N */
    private void setNode(int index, Node n) {
        // In the case that the ArrayList is not big enough
        // add null elements until it is the right size
        while (index + 1 > contents.size()) {
            contents.add(null);
        }
        contents.set(index, n);
    }

    /** Returns and removes the node located at INDEX. */
    private Node removeNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.remove(index);
        }
    }

    /** Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        this.contents.set(index1, node2);
        this.contents.set(index2, node1);
    }

    /** Prints out the heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /** Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getNode(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getNode(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getNode(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /** A Node class that stores items and their associated priorities. */
    public class Node {
        private T _item;
        private double _priority;

        private Node(T item, double priority) {
            this._item = item;
            this._priority = priority;
        }

        public T item() {
            return this._item;
        }

        public double priority() {
            return this._priority;
        }

        public void setPriority(double priority) {
            this._priority = priority;
        }

        @Override
        public String toString() {
            return this._item.toString() + ", " + this._priority;
        }
    }

    /* FILL IN THE METHODS BELOW. */

    /** Returns the index of the left child of the node at i. */
    private int getLeftOf(int i) {
        return 2 * i;
    }

    /** Returns the index of the right child of the node at i. */
    private int getRightOf(int i) {
        return (2 * i) + 1;
    }

    /** Returns the index of the node that is the parent of the
     *  node at i. */
    private int getParentOf(int i) {
        return i / 2;
    }

    /** Returns the index of the node with smaller priority. If one
     * node is null, then returns the index of the non-null node.
     * Precondition: at least one of the nodes is not null. */
    private int min(int index1, int index2) {
        Node temp1 = getNode(index1);
        Node temp2 = getNode(index2);
        if (temp1 == null) {
            return index2;
        } else if (temp2 == null) {
            return index1;
        } else {
            double prio1 = temp1._priority;
            double prio2 = temp2._priority;
            if (prio1 < prio2) {
                return index1;
            } else {
                return index2;
            }
        }
    }

    /** Returns the item with the smallest priority value, but does
     * not remove it from the heap. If multiple items have the minimum
     * priority value, returns any of them. Returns null if heap is
     * empty. */
    public T peek() {
        if (size() == 0) {
            return null;
        }
        return getNode(1)._item;
    }

    /** Bubbles up the node currently at the given index until no longer
     *  needed. */
    private void bubbleUp(int index) {
        int pIndex = getParentOf(index);
        while (min(pIndex, index) == index && index != 1) {
            swap(pIndex, index);
            index = pIndex;
            pIndex = getParentOf(index);
        }
    }

    /** Bubbles down the node currently at the given index until no longer
     *  needed. */
    private void bubbleDown(int index) {
        int lIndex = getLeftOf(index);
        int rIndex = getRightOf(index);
        boolean running = true;
        while (running) {
            Node pNode = getNode(index);
            Node rNode = getNode(rIndex);
            Node lNode = getNode(lIndex);
            if (rNode != null && lNode != null) {
                double priop = pNode._priority;
                double priol = lNode._priority;
                double prior = rNode._priority;
                if (priop >= priol && priop >= prior) {
                    if (priol >= prior) {
                        swap(rIndex, index);
                        index = rIndex;
                    } else {
                        swap(lIndex, index);
                        index = lIndex;
                    }
                    lIndex = getLeftOf(index);
                    rIndex = getRightOf(index);
                } else if (priop >= priol || priop >= prior) {
                    if (priop >= priol) {
                        swap(lIndex, index);
                        index = lIndex;
                    } else if (priop >= prior) {
                        swap(rIndex, index);
                        index = rIndex;
                    }
                    lIndex = getLeftOf(index);
                    rIndex = getRightOf(index);
                } else {
                    running = false;
                }
            } else if (rNode == null && lNode == null) {
                running = false;
            } else if (rNode == null) {
                double priop1 = pNode._priority;
                double priol1 = lNode._priority;
                if (priol1 < priop1) {
                    swap(index, lIndex);
                    index = lIndex;
                    lIndex = getLeftOf(index);
                    rIndex = getRightOf(index);
                } else {
                    running = false;
                }
            } else {
                running = false;
            }

        }
    }

    /** Inserts an item with the given priority value. Assume that item is
     * not already in the heap. Same as enqueue, or offer. */
    public void insert(T item, double priority) {
        Node tempIn = new Node(item, priority);
        setNode(size() + 1, tempIn);
        int tIndex = size();
        bubbleUp(tIndex);
    }

    /** Returns the element with the smallest priority value, and removes
     * it from the heap. If multiple items have the minimum priority value,
     * removes any of them. Returns null if the heap is empty. Same as
     * dequeue, or poll. */
    public T removeMin() {
        if (size() == 0) {
            return null;
        }
        swap(1, size());
        Node tempNode = removeNode(size());
        bubbleDown(1);
//        bubbleDown(1);
        return tempNode._item;
    }

    /** Changes the node in this heap with the given item to have the given
     * priority. You can assume the heap will not have two nodes with the
     * same item. Does nothing if the item is not in the heap. Check for
     * item equality with .equals(), not == */
    public void changePriority(T item, double priority) {
        int index = 0;
        for (int i = 1; i <= size(); i++) {
            Node tempNode = getNode(i);
            if (tempNode != null) {
                if (tempNode._item.equals(item)) {
                    index = i;
                    tempNode.setPriority(priority);
                    break;
                }
            } else {
                break;
            }
        }
        Node tmpNode = getNode(index);
        Node tmpNodeL = getNode(getLeftOf(index));
        Node tmpNodeR = getNode(getRightOf(index));
        Node tmpNodeP = getNode(getParentOf(index));
        if (tmpNode != null && tmpNodeP != null) {
            if (tmpNode.priority() < tmpNodeP.priority()) {
                bubbleUp(index);
            } else if (tmpNodeL != null && tmpNodeR != null) {
                if (tmpNodeL.priority() < tmpNode.priority()
                        || tmpNodeR.priority() < tmpNode.priority()) {
                    bubbleDown(index);
                }
            }
        }
    }
}
