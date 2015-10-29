// EnSoft Software Internship Interview Program
// Written by: Ian Norris

package com.company;

import java.util.*;

/** This class when compiled runs a Depth First Search for an undirected Graph
 *  that is entered into the compiler by the user.  The user must paste in all
 *  input into the compiler via the following patterns:
 *  Map:
 *   - Node pairs: Example --> (x1 x2)
 *  Avoid:
 *   - Nodes: Example --> (x1 x2 x3 x4 ...)
 *  Peggy:
 *   - Nodes: Example --> (x1 x2 x3 x4 ...)
 *  Sam:
 *   - Nodes: Example --> (x1 x2 x3 x4 ...)
 *
 *  Upon entering in all node information press enter and all valid meeting
 *  locations will be printed out in alphabetical order by the program upon its
 *  completion.
 */

class LetsDoLunch{

    private static ArrayList<Node> nodeStore = new ArrayList<Node>();
    private static Scanner scan = new Scanner(System.in);
    private static ArrayList<Node> peggy = new ArrayList<Node>();
    private static ArrayList<Node> sam = new ArrayList<Node>();
    private static HashMap<Node,Boolean> validityNodeStore = new HashMap<Node,Boolean>();
    private static HashMap<Node,Boolean> validityNodeStore2 = new HashMap<Node,Boolean>();
    private static HashMap<String, Node> previouslyStoredNodes = new HashMap<String,Node>();

    /** Method receives a Node to start with and a target Node, and marks all valid
     * meeting locations between these Nodes by iterating through the map using a
     * Recursive DFS Algorithm.
     *
     * @param currentNode    current Node to analyze and iterate from
     * @param targetNode     target Nodes to reach using DFS Algorithm
     */
    private static void graphDFSByRecursion(Node currentNode, ArrayList<Node> targetNode) {

        //Each Return statement within this algorithm simply returns to the last Node that was visited.

        if (null == currentNode) {
            return;
        }
        if (currentNode.visited){
            currentNode.faceDownStream = false;
        } else {
            currentNode.visitNode();
        }

        // Debugging print line statement below
        //System.out.println(currentNode.label);
        //System.out.println(validityNodeStore.values());
        //System.out.println(validityNodeStore.keySet());

        if (targetNode.contains(currentNode)) {
            validityNodeStore.put(currentNode, true);
        }
        if (targetNode.contains(currentNode) && currentNode.getChildren().isEmpty()) {
            validityNodeStore.put(currentNode, true);
            return;
        }
        if (!targetNode.contains(currentNode) && currentNode.getChildren().isEmpty()) {
            validityNodeStore.put(currentNode, false);
            return;
        }

        boolean validNeighborNode = false;
        int visitedInvalidChildCount = 0;

        if (!currentNode.getChildren().isEmpty()) {
            for (Node child : currentNode.getChildren()) {
                if (child.visited) {
                    if (child.faceDownStream){
                        if (targetNode.contains(child)){
                            validityNodeStore.put(currentNode, true);
                        }
                    } else {
                        //Check if this is an avoided Node prior to determining validity
                        if (nodeStore.contains(currentNode)) {
                            validityNodeStore.put(currentNode, true);
                        }
                        //If child is facing downstream, cycle has been detected
                        //Return in all scenarios given in logic below
                        if (!child.faceDownStream && !targetNode.contains(child) && !validityNodeStore.get(child)) {
                            return;
                        }
                    }
                }else {
                    if (nodeStore.contains(currentNode)) {
                        //Visit Unvisited children that are not to be avoided
                        graphDFSByRecursion(child, targetNode);

                        // Debugging print line statement below
                        //System.out.println(currentNode.label);
                        //System.out.println(validityNodeStore.values());
                        //System.out.println(validityNodeStore.keySet());

                        if (child.faceDownStream && !validityNodeStore.get(child)) {
                            validNeighborNode = true;
                            if (!validityNodeStore.get(child)) {
                                visitedInvalidChildCount++;
                            }
                        }
                    }
                }
            }
        }
        if (visitedInvalidChildCount != currentNode.getChildren().size()) {
            //At least one child is valid, so current Node must be valid
            validityNodeStore.put(currentNode, true);
            return;
        }
        if (!validNeighborNode){
            validityNodeStore.put(currentNode, false);
        }
    }

    /** This method takes valid meeting location Node values and
     * Sorts them in alphabetical order.
     *
     * @param map Representative variable for receiving valid meeting locations HashMap
     * @return Returns list of all the sorted valid meeting locations
     */
    private static List<String> sortByValues(HashMap<Node, Boolean> map) {
        List<String> sortedKeys = new ArrayList<String>();
        for(int t = 0; t < map.size(); t++) {
            sortedKeys.add(t, map.keySet().toArray()[t].toString());
        }
        Collections.sort(sortedKeys,String.CASE_INSENSITIVE_ORDER);
        return sortedKeys;
    }

    /** This method takes a String array consisting of either Peggy or
     * Sam values Node names along with an ArrayList containing the corresponding
     * Nodes themselves, and initializes the aforementioned ArrayList with all
     * correct Nodes from the Node storage ArrayList
     *
     * @param nodeNameSet Array consisting of names of either Starting or Ending Nodes
     * @param nodeStorageArray ArrayList consisting of set of either Starting or Ending Node Objects
     */
    private static void startingNodeInitialize(String[] nodeNameSet, ArrayList<Node> nodeStorageArray ){
        for(String nodeName: nodeNameSet){
            for(Node node: nodeStore){
                if (node.label.equals(nodeName)){
                    nodeStorageArray.add(node);
                }
            }
        }
    }

    /**  This method takes all nodes that the user inputs that need to be avoided,
     *  and then removes them from the Node storage Array List.
     *
     * @param removeSet Variable which represents all Nodes the user specifies to avoid
     * @param counter Index to evaluate in the set of Nodes which need to be avoided
     */
    private static void removeNodes(String[] removeSet, int counter){
        for (int e = 0; e < nodeStore.size(); e++){
            if ((nodeStore.get(e)).label.equals(removeSet[counter])){
                validityNodeStore.keySet().removeAll(Collections.singleton(nodeStore.get(e)));
                nodeStore.removeAll(Collections.singleton(nodeStore.get(e)));
            }
        }
    }

    /**  This method takes all nodes that are entered in to the system, and initializes their state,
     *  puts them into the Node Storage Array List, and sets their relation to other nodes within the
     *  map that is entered in by the user.  If the node is already stored, then that previous node is
     *  given a new relation to other nodes.
     *
     * @param temporaryNodeNameSet Stores each pair of nodes in a String Array
     */
    private static void readInNodeSets(String[] temporaryNodeNameSet){
        
        if (!previouslyStoredNodes.containsKey(temporaryNodeNameSet[0]) &&
                !previouslyStoredNodes.containsKey(temporaryNodeNameSet[1])) {
            Node nodeParent = new Node(temporaryNodeNameSet[0]);
            nodeParent.visited = false;
            Node nodeChild = new Node(temporaryNodeNameSet[1]);
            nodeChild.visited = false;
            nodeParent.setChildren(nodeChild);
            previouslyStoredNodes.put(temporaryNodeNameSet[0], nodeParent);
            previouslyStoredNodes.put(temporaryNodeNameSet[1],nodeChild);
            nodeStore.add(nodeParent);
            nodeStore.add(nodeChild);
            validityNodeStore.put(nodeParent, false);
            validityNodeStore.put(nodeChild,false);
        } else if (previouslyStoredNodes.containsKey(temporaryNodeNameSet[0]) &&
                !previouslyStoredNodes.containsKey(temporaryNodeNameSet[1])) {
            Node nodeParent = previouslyStoredNodes.get(temporaryNodeNameSet[0]);
            nodeParent.visited = false;
            Node nodeChild = new Node(temporaryNodeNameSet[1]);
            nodeChild.visited = false;
            nodeParent.setChildren(nodeChild);
            previouslyStoredNodes.put(temporaryNodeNameSet[1], nodeChild);
            nodeStore.add(nodeChild);
            validityNodeStore.put(nodeChild,false);
        } else if (!previouslyStoredNodes.containsKey(temporaryNodeNameSet[0]) &&
                previouslyStoredNodes.containsKey(temporaryNodeNameSet[1])){
            Node nodeChild = previouslyStoredNodes.get(temporaryNodeNameSet[1]);
            Node nodeParent = new Node(temporaryNodeNameSet[0]);
            nodeParent.visited = false;
            nodeChild.visited = false;
            nodeParent.setChildren(nodeChild);
            previouslyStoredNodes.put(temporaryNodeNameSet[0], nodeParent);
            nodeStore.add(nodeParent);
            validityNodeStore.put(nodeParent,false);
        } else {
            Node nodeParent = previouslyStoredNodes.get(temporaryNodeNameSet[0]);
            Node nodeChild = previouslyStoredNodes.get(temporaryNodeNameSet[1]);
            nodeParent.setChildren(nodeChild);
        }
    }

    /** Runs Let's Do Lunch Program as directed in original problem statement
     * found at: http://ensoftupdate.com/download/jobs/programming-exercise-0114.pdf
     *
     * @param args contains the supplied command-line arguments as an array of String objects
     */
    public static void main(String[] args) {
        
        String[] avoidInputNodes, peggyInputNodes, samInputNodes, temporaryNodeNameSet;

        while (scan.hasNextLine()) {
            String input = scan.nextLine();
            if(input.isEmpty()){
                break;
            }
            if (input.equals("Map:")) {
                while (scan.hasNextLine()){
                    input = scan.nextLine();
                    temporaryNodeNameSet = input.split(" ");
                    if (input.startsWith("Avoid:")){
                        break;
                    }
                    if(temporaryNodeNameSet.length == 2) {
                        readInNodeSets(temporaryNodeNameSet);
                    }
                }
            }
            if (input.equals("Avoid:")) {
                input = scan.nextLine();
                avoidInputNodes = input.split(" ");
                for (int b = 0; b < avoidInputNodes.length; b++) {
                    removeNodes(avoidInputNodes, b);
                }
            }
            if (input.equals("Peggy:")) {
                input = scan.nextLine();
                peggyInputNodes = input.split(" ");
                startingNodeInitialize(peggyInputNodes,peggy);
            }
            if (input.equals("Sam:")) {
                input = scan.nextLine();
                samInputNodes = input.split(" ");
                startingNodeInitialize(samInputNodes,sam);
            }
        }

        // Debugging Print Line Statements - print out node sets and each node's child
        //System.out.println(validityNodeStore.keySet());
        //System.out.println(nodeStore);
        //for (int t =0; t < nodeStore.size(); t++){
        //System.out.println(nodeStore.get(t));
        //System.out.println(nodeStore.get(t).getChildren());
        //}

        for(Node startingNode: peggy){
            graphDFSByRecursion(startingNode, sam);
            for(Node storedNode: nodeStore){
                if (validityNodeStore.get(storedNode)){
                    validityNodeStore2.put(storedNode,true);
                }
            }
            // Print Line statements used for debugging
            //System.out.println(validityNodeStore.values());
            //System.out.println(validityNodeStore.keySet());
            //System.out.println(validityNodeStore2.values());
            //System.out.println(validityNodeStore2.keySet());
        }
        List<String> validPlaces = sortByValues(validityNodeStore2);
        for(String place: validPlaces){
            System.out.println(place);
        }
    }
}

