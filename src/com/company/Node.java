package com.company;

import java.util.ArrayList;

/**
 * Created by Ian on 2/3/2015.
 *
 *  This Node class has various methods within it used to correctly
 *  identify and access all vertices as necessary.  Each Node can have
 *  its name stored (label), its children and neighbors, along with
 *  whether it has been visited as seen in variables initialized below.
 *
 *  @author Ian Norris
 */

@SuppressWarnings("ALL")
public class Node implements Comparable{

    boolean visited, faceDownStream;
    public ArrayList<Node> children = new ArrayList<Node>();
    public String label;
    private Object objectHolder;

    /** Adds Node child to ArrayList of this Node's children
     *
     * @param child     Node which is added to ArrayList
     */
    public void setChildren(Node child){
        this.children.add(child);
    }

    /** Returns this Node's children
     *
     * @return   children
     * */
    public ArrayList<Node> getChildren(){
        return this.children;
    }

    /** Sets this Node's name to a given String value
     *
     * @param name   name
     */
    public Node(String name){
        this.label = name;
    }

    /** Marks Node as visited
     *
     * @param visit Boolean marker
     */
    public void setVisited(boolean visit) {
        this.visited = visit;
    }

    /** Marks direction that you are traveling through the Node
     *
     * @param setFace Boolean marker
     */
    public void setFaceDownStream(boolean setFace){
        this.faceDownStream = setFace;
    }

    /** Adds Node to neighbors ArrayList of this Node
     *
     */
    public void visitNode() {

        if(!this.visited){
            setVisited(true);
            setFaceDownStream(true);
        }
    }

    /** Override CompareTo method so that default Equals method analyzes Nodes correctly
     *
     * @param object  Node object
     * @return 0
     */
    @Override
    public int compareTo(Object object) {
        return 0;
    }

    /** Override toString() method such that value of Node can be displayed
     *
     * @return  Return name of Node
     */
    @Override
    public String toString() {
        return label;
    }

    /** Overrides equals default method such that Node values can be compared
     *
     * @param object Node object
     * @return  Returns whether this Node equals Node sent to method
     */
    @Override
    public boolean equals(Object object) {
        if(object instanceof Node){
            Node toCompare = (Node) object;
            return this.label.equals(toCompare.label);
        }
        return false;
    }

    /** This method Overrides default hashCode method such that HashMap can be
     * reconfigured to store Nodes and Boolean markers.  It also sets and returns
     * this Node's hashCode.
     *
     * @return   Return hashCode of this Node's value
     */
    @Override
    public int hashCode() {
        return this.label.hashCode();
    }
}
