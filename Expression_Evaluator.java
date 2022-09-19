import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IStack {
  
  /*** Removes the element at the top of stack and returnsthat element.
  * @return top of stack element, or through exception if empty
  */
  
  public Object pop();
  
  /*** Get the element at the top of stack without removing it from stack.
  * @return top of stack element, or through exception if empty
  */
  
  public Object peek();
  
  /*** Pushes an item onto the top of this stack.
  * @param object to insert*
  */
  
  public void push(Object element);
  
  /*** Tests if this stack is empty
  * @return true if stack empty
  */
  public boolean isEmpty();
  
  public int size();
}


class MyStack implements IStack {

    public static final int CAPACITY = 500;
    private int capacity;
    private int t = -1;
    private Object s[];
    private static Boolean error = false;
    
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner s = new Scanner(System.in);
        MyStack myStack = new MyStack();
        String operation;
        Object element;
        String elements_string = s.nextLine().replaceAll("\\[|\\]", "");
        String[] elements = elements_string.split(", ");
        
        for (int i = 0; i < elements.length; i++){
            if (!elements[elements.length - 1 - i].isEmpty()){
                myStack.push(elements[elements.length - 1 - i]);    
            }
        }
        
            
        while(!error){
            try{
                operation = s.nextLine();
            }catch(Exception e){
                break;
            }
            
            switch(operation){
                
                case("size"):
                    System.out.println(myStack.size());
                    break;
                
                case("isEmpty"):
                    if (myStack.isEmpty()){
                        System.out.println("True");
                    } else {
                        System.out.println("False");
                    }
                    break;
                
                case("push"):
                    element = s.nextLine();
                    myStack.push(element);
                    if (error){
                        System.out.println("Error");    
                    }else{
                        myStack.print();  
                    }
                    break;
                
                case("pop"):
                    myStack.pop();
                    if (error){
                        System.out.println("Error");   
                    }else{
                        myStack.print();  
                    }
                    break;
                
                case("peek"):
                    myStack.peek();
                    if (error){
                        System.out.println("Error");
                    }else{
                        System.out.println(myStack.peek());    
                    }
                    break;
                
                default:
                    System.out.println("Error");
                    break;
            }
        }
    }
    
    MyStack(){
        this(CAPACITY);
    }
    
    MyStack(int cap){
        capacity = cap;
        s = new Object[capacity];
    }
    
    public int size(){
        return(t + 1);
    }
    
    public boolean isEmpty(){
        return (size() == 0);
    }
    
    public void push(Object element){
        if (size() == capacity){
            error = true;
            return;
        }
        s[++t] = element;
    }
    
    
    public Object pop(){
        if (isEmpty()){
            error = true;
            return null;
        }
        Object element = s[t];
        s[t--] = null;
        return element;
    }
    
    public Object peek(){
        if (isEmpty()){
            error = true;
            return null;
        }
        return s[t];
    }
    
    public void print(){
        if (isEmpty()){
            System.out.println("[]");
        } else {
            System.out.print("[");
            for (int i = 0; i < t; i++){
                System.out.print(s[t - i]);
                System.out.print(", ");
            }
            System.out.print(s[0]);
            System.out.println("]");
        }
    }
}

interface IExpressionEvaluator {
  
/**
* Takes a symbolic/numeric infix expression as input and converts it to
* postfix notation. There is no assumption on spaces between terms or the
* length of the term (e.g., two digits symbolic or numeric term)
*
* @param expression infix expression
* @return postfix expression
*/
  
public String infixToPostfix(String expression);
  
  
/**
* Evaluate a postfix numeric expression, with a single space separator
* @param expression postfix expression
* @return the expression evaluated value
*/
  
public int evaluate(String expression);

}


public class Evaluator implements IExpressionEvaluator {
    public static String[] operands;
    
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. */
        Scanner s = new Scanner(System.in);
        Evaluator eva = new Evaluator();
        String in = s.nextLine();
        operands = new String[3];
        char ch;
        
        for(int i = 0; i < 3; i++){
            operands[i] = s.nextLine().split("=")[1];
        }
        
        String post = eva.infixToPostfix(in);
        System.out.println(post);
        
        if (!post.equals("Error")){
            System.out.println(eva.evaluate(post));    
        } 
    }
    
    public boolean contains(char c, char[] array) {
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }
    
    public int evaluate(String expression){
        Scanner s = new Scanner(System.in);
        MyStack evStack = new MyStack();
        char[] operations = {'+', '-', '*', '/', '^'};
        char operand;
        char[] postfix = expression.toCharArray();
        int result = 0;
        
        for (int i = 0; i < postfix.length; i++){
            if (contains(postfix[i], operations)){
                if (evStack.size() == 1){
                    if (postfix[i] == '-'){
                        result = -1 * (int)evStack.pop();
                    }
                } else if (evStack.size() > 1){
                    if(postfix[i] == '+'){
                        result = (int)evStack.pop() + (int)evStack.pop();
                    } else if (postfix[i] == '-'){
                        result = -((int)evStack.pop() - (int)evStack.pop());
                    } else if (postfix[i] == '*'){
                        result = (int)evStack.pop() * (int)evStack.pop();
                    } else if (postfix[i] == '/'){
                        int divisor = (int)evStack.pop();
                        int dividend = (int)evStack.pop();
                        result = dividend / divisor;
                    } else if (postfix[i] == '^'){
                        int power = (int)evStack.pop();
                        int base = (int)evStack.pop();
                        result = (int)Math.pow(base, power);
                    }
                }
                evStack.push(result);
            } else {
                result = Integer.parseInt(operands[postfix[i] - 97]);
                evStack.push(result);
            }
        }    
        return result;
    }
    
    public Boolean precedence(char stackChar, char infixChar){
        if (infixChar == '^'){
            return true;
        } else if (infixChar == '*' || infixChar == '/'){
            if (stackChar == '^' || stackChar == '*' || stackChar == '/'){
                return false;
            } else {
                return true;
            }
        } else if (infixChar == '+' || infixChar == '-'){
            return false;
        }
        return false;
    }
    
    public String infixToPostfix(String expression) {
        
        Scanner s = new Scanner(System.in);
        MyStack myStack = new MyStack();
        char[] operations = {'+', '-', '*', '/', '^'};
        String postfix = "";
        char[] infix = expression.toCharArray();
        int paren = 0;

        if (infix[0] == '*' || infix[0] == '/' || infix[0] == '^' || infix[0] == ')'){
            return "Error";
        } else if (contains(infix[infix.length - 1], operations) || infix[infix.length - 1] == '('){
            return "Error";
        }
        
        for (int i = 0; i < infix.length; i++){
            if (infix[i] == '('){
                try{
                    if (!contains(infix[i-1], operations) && infix[i-1] != '(' || contains(infix[i+1], operations)){
                        return "Error";
                    }
                }catch(Exception e){    
                }
                myStack.push(infix[i]);
                paren++;
            }else if (infix[i] == ')'){
                try{
                    if (contains(infix[i-1], operations) || !contains(infix[i+1], operations)  && infix[i+1] != ')'){
                        return "Error";
                    }
                }catch(Exception e){    
                }
                if (paren > 0){
                    while ((char)myStack.peek() != '('){
                        postfix += myStack.pop();
                    }
                    myStack.pop();
                    paren--;
                } else {
                    return "Error";
                }
            } else if (contains(infix[i], operations)){
                if ((infix[i] == '*' || infix[i] == '/' || infix[i] == '^') && contains(infix[i-1], operations)){
                    return "Error";
                }
                else if (infix[i] == '-' && infix[i+1] == '-'){
                    try{
                        if (!contains(infix[i-1], operations) && infix[i-1] != '('){
                            while (myStack.size() > 0 && contains((char)myStack.peek(), operations) && !precedence((char)myStack.peek(), '+')){
                                postfix += myStack.pop();
                            }
                            myStack.push('+');
                        }
                    }catch(Exception e){    
                    }
                    i++;
                }
                else if (myStack.size() > 0 && contains((char)myStack.peek(), operations)){
                    while (myStack.size() > 0 && contains((char)myStack.peek(), operations) && !precedence((char)myStack.peek(), infix[i])){
                        postfix += myStack.pop();
                    }
                    myStack.push(infix[i]);
                } 
                else {
                    myStack.push(infix[i]);
                }
            } else {
                try{
                    if (!contains(infix[i-1], operations) && infix[i-1] != '('){
                        return "Error";
                    }
                }catch(Exception e){    
                }
                postfix += infix[i];
            }
        }
        
        if (paren == 0){
            while(myStack.size() > 0){
                postfix += myStack.pop();
            }
        } else {
            return "Error";
        }
        
        if (postfix.indexOf('a') == -1 && postfix.indexOf('b') == -1 && postfix.indexOf('c') == -1){
            return "Error";
        }
        
        return postfix;
    }
}