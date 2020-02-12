import net.sf.saxon.style.XSLOutput;

import java.awt.dnd.DnDConstants;

/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value
        if (this._front == null && this._back == null){
            return 0;
        } else if (this._front == this._back){
            return 1;
        } else {
            int size = 0;
            DNode temp = this._front;
            while (temp != null){
                temp = temp._next;
                size ++;
            }
            return size;
        }
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        // FIXME: Implement this method and return correct value
        if (i >= 0) {
            DNode temp = this._front;
            while (i > 0){
                temp = temp._next;
                i--;
            }
            return temp._val;
        }else {
            DNode temp = this._back;
            while (i < -1) {
                temp = temp._prev;
                i++;
            }
            return temp._val;
        }
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        DNode temp = new DNode(d);
        if (this._front == null & this._back == null){
            this._back = temp;
            this._front = temp;
        }else{
            temp._next = this._front;
            this._front._prev = temp;
            this._front = temp;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this method
        DNode temp = new DNode(d);
        if (this._back == null && this._front == null){
            this._back = temp;
            this._front = temp;
        }else{
            this._back._next = temp;
            temp._prev = this._back;
            this._back = temp;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        DNode temp = new DNode(d);
        if (this._back == null && this._front == null){
            this._front = temp;
            this._back = temp;
        } else {
            if (index == 0) {
                temp._next = this._front;
                this._front._prev = temp;
                this._front = temp;
            }else if(index > 0){
                DNode point = this._front;
                index -= 1;
                while (index > 0) {
                    index--;
                    point = point._next;
                }
                if (point._next != null){
                    temp._next = point._next;
                    point._next._prev = temp;
                    point._next = temp;
                    temp._prev = point;
                } else {
                    temp._prev = point;
                    point._next = temp;
                    this._back = temp;
                }
            }else{
                DNode point2 = this._back;
                if (index == -1) {
                    temp._prev = point2;
                    point2._next = temp;
                    this._back = temp;
                }else {
                    index += 1;
                    while (index < -1) {
                        index++;
                        point2 = point2._prev;
                    }
                    if (point2._prev != null){
                        temp._prev = point2._prev;
                        point2._prev._next = temp;
                        point2._prev = temp;
                        temp._next = point2;
                    } else {
                        temp._next = this._front;
                        this._front._prev = temp;
                        this._front = temp;
                    }
                }

            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct
        int var;
        if (this._front == this._back) {
            var = this._front._val;
            this._front = null;
            this._back = null;
        }else{
            var = this._front._val;
            this._front._next._prev = null;
            this._front = this._front._next;
        }
        return var;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
        int var;
        if (this._back == this._front) {
            var = this._back._val;
            this._back = null;
            this._front = null;
        }else{
            var = this._back._val;
            this._back = this._back._prev;
            this._back._next = null;
        }
        return var;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        int var = 0;
        if (this._front == this._back) {
            var = this._back._val;
            this._front = null;
            this._back = null;
        } else {
            if (index == 0) {
                var = this._front._val;
                this._front._next._prev = null;
                this._front = this._front._next;
            } else if (index > 0) {
                DNode temp = this._front;
                index -= 1;
                while (index > 0){
                    index--;
                    temp = temp._next;
                }
                var = temp._next._val;
                if (temp._next._next != null){
                    temp._next._next._prev = temp;
                    temp._next = temp._next._next;
                } else {
                    this._back = this._back._prev;
                    this._back._next = null;
                }
            } else {
                if (index == -1) {
                    var = this._back._val;
                    this._back = this._back._prev;
                    this._back._next = null;
                } else {
                    DNode point = this._back;
                    index += 2;
                    while(index < 0){
                        index++;
                        point = point._prev;
                    }
                    var = point._prev._val;
                    if (point._prev._prev != null){
                        point._prev._prev._next = point;
                        point._prev = point._prev._prev;
                    } else {
                        this._front._next._prev = null;
                        this._front = this._front._next;
                    }
                }
            }
        }
        return var;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
        if (this._back == null && this._front == null){
            String list2string = "[]";
            return list2string;
        }else{
            DNode temp = this._front;
            String outcome = "[";
            while (temp != null) {
                if (temp._next != null) {
                    outcome = outcome + temp._val + ", ";
                } else {
                    outcome = outcome + temp._val;
                }
                temp = temp._next;
                if (temp == null) {
                    outcome += "]";
                }
            }
            return outcome;
        }
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
