//Eyiara Oladipo

//Calculator GUI
//Get input from user
//Use delimiter to split the conditions
//20 + 5 - 1 => if condition is -
import java.lang.Math;   
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.util.Scanner;
import java.util.Stack;
import java.io.*;


public class Project1_DS {
	static char[] operators; 
	static double[] inputNumbers = new double[40];
	static String postOrderReversal = "";
	static JTextArea calculatorResultsArea;
	static String globalUserInput;
	static JTextArea calculatorInput;
	//Node of a tree in the binary tree
	public static class TreeNode{
		String data;
		TreeNode leftChild;
		TreeNode rightChild;
		
		//Node constructor
		public TreeNode(String data) {
			this.data = data;
			this.leftChild = null;
			this.rightChild = null;
		}	
	}
	
	
	//Expression tree - the tree itself, that can be
	//traversed etc, contains the root node
	public static class ExpressionTree{
		static TreeNode root;	
		public ExpressionTree(TreeNode data) {
			root = data;
		}
		
		public ExpressionTree() {
			root = null;
		}

		public void postOrderTraverse(TreeNode subtree) {
			  if (subtree !=null) {
				  postOrderTraverse(subtree.leftChild);
				  postOrderTraverse(subtree.rightChild);
				  updateStack(subtree.data);
				  postOrderReversal += subtree.data;
			   }
		}
		
		public String getRoot() {
			return root.data;
		}
	}
	
	static Stack<String> userInputStack = new Stack<String>();
	static String answer = "";
	//123*+\
	
	//123
	public static void updateStack(String data) {
			switch(data){
				case "*":
				case "+":
				case "/":
				case "-":
					String secondInput = (String)userInputStack.pop();	
					String firstInput = (String)userInputStack.pop();
					if(data.equals("*")) {
//						int result = Character.getNumericValue(firstInput) * Character.getNumericValue(secondInput);
//						int result = Integer.parseInt(firstInput) * Integer.parseInt(secondInput);
						double result = Double.parseDouble(firstInput) * Double.parseDouble(secondInput);
						
						userInputStack.push(""+result);
					}
					if(data.equals("+")) {
						double result = Double.parseDouble(firstInput) + Double.parseDouble(secondInput);
						userInputStack.push(""+result);
					}
					if(data.equals("/")) {
						double result = Double.parseDouble(firstInput) / Double.parseDouble(secondInput);
						userInputStack.push(""+result);
					}
					if(data.equals("-")) {
						double result = Double.parseDouble(firstInput) - Double.parseDouble(secondInput);
						userInputStack.push(""+result);				
					}				
					answer = (String)userInputStack.peek();
					break;
					default: 
					userInputStack.push(""+data);							
			}		
		
		
//		System.out.println("The solution is: " + userInputStack.peek());
	}
	
	public static int previousRootAssociability(String expressionTreeRoot, String currentNodeRoot) {
		int associability = 0 ;
		switch(expressionTreeRoot) {
		case "+":
		case "-":
			if(currentNodeRoot.equals("*")  || currentNodeRoot.equals("/") ) {
				associability = 1;
			}
			break;
		case "*":
		case "/": 
			if(currentNodeRoot.equals("+") || currentNodeRoot.equals("-")) {
				associability = -1;
			}
			break;
		}
		return associability;
	}
	
	public static void createUserInputBinaryTree(int numberOfOperators) {
		postOrderReversal = "";
		ExpressionTree operatorTree = new ExpressionTree();
		for(int i = 0; i < operators.length; i++) {	
			//also the root
			TreeNode node = new TreeNode(""+operators[i]);
			TreeNode leftChildNode = new TreeNode(""+inputNumbers[i]);
			TreeNode rightChildNode = new TreeNode(""+inputNumbers[i + 1]);
			node.leftChild = leftChildNode;
			node.rightChild = rightChildNode;
			if(i == 0) {
				operatorTree = new ExpressionTree(node);
			}
						
			if(i != 0) {
				//Check for same associabiity 
				//For direct child node
				int associability = previousRootAssociability(operatorTree.getRoot(), node.data);
				
				if(associability == 0) {
					node.leftChild = operatorTree.root;
					operatorTree = new ExpressionTree(node);
				}
				
				if(associability == 1) {
					node.leftChild =  operatorTree.root.rightChild;
					operatorTree.root.rightChild = node;
				}
				
				if(associability == -1) {
					node.leftChild =  operatorTree.root;
					operatorTree = new ExpressionTree(node);
				}
			}
		}
		operatorTree.postOrderTraverse(operatorTree.root);	
		updateCalculatorInterface(answer);
	}
	
	public static void updateCalculatorInterface(String answer) {
		calculatorResultsArea.append(globalUserInput + " = " +  answer + "\n" + "--------------------" + "\n");
		calculatorInput.setText("");
	}

	public static int getNumberOfOperatorsInUserInput(String userInput) {
		int operatorNumbers = 0;
		Scanner userInputScanner = new Scanner(userInput);
		userInputScanner.useDelimiter("[-/*+]+");	
		while(userInputScanner.hasNext()) {
			operatorNumbers++;
			String inputNumber = userInputScanner.next();
		}
		userInputScanner.close();
		return operatorNumbers - 1;
	}
	
	public static void validateUserInput(String userInput, int inputTillDelimiter) {
		Scanner scanUserInput;
		int numberOfInputNumbers = 0;
		int numberOfOperatorsInInput = 0;
		globalUserInput = userInput;
		scanUserInput = new Scanner(userInput);
		scanUserInput.useDelimiter("[-/*+]+");	
		//While we can keep getting letters, and the inputs till a delimiter isnt the end of the string
		
		int numberOfOperators = getNumberOfOperatorsInUserInput(userInput);
		operators = new char[numberOfOperators]; 
		
		while(scanUserInput.hasNext()) {
			String inputNumber = scanUserInput.next();
			inputTillDelimiter += inputNumber.length();
			inputNumbers[numberOfInputNumbers] = Double.parseDouble(inputNumber);
			if(scanUserInput.hasNext() && inputTillDelimiter != userInput.length()) {
				operators[numberOfOperatorsInInput] = userInput.charAt(inputTillDelimiter);
				inputTillDelimiter++;
				numberOfOperatorsInInput++;
				numberOfInputNumbers++;
			}
		}
		
		scanUserInput.close();
		//Clearing stack
		userInputStack.clear();
		createUserInputBinaryTree(numberOfOperators);
	}
	
	public static void createCalculatorGui() {
		JFrame calculatorFrame = new JFrame();
		calculatorFrame.setSize(400, 541);
		calculatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		calculatorFrame.getContentPane().setLayout(new GridLayout(3, 1, 0, 0));
		JPanel topInputPanel = new JPanel();
		topInputPanel.setBackground(Color.DARK_GRAY);
		topInputPanel.setLayout(null);
		calculatorFrame.getContentPane().add(topInputPanel);
		calculatorInput = new JTextArea(3, 35);
		calculatorInput.setBackground(SystemColor.window);
		calculatorInput.setBounds(53, 65, 284, 58);
		topInputPanel.add(calculatorInput);
		calculatorInput.setLineWrap(true);
		calculatorInput.setEditable(false);
		
		JLabel lblNewLabel = new JLabel("CALCULATOR");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("SimSun-ExtB", Font.BOLD, 16));
		lblNewLabel.setBounds(138, 25, 102, 14);
		topInputPanel.add(lblNewLabel);
		
		//Calculator Results
		JPanel calculatorResults = new JPanel();
		JScrollPane calculatorResultsAreaScrollbar = new JScrollPane();
		calculatorFrame.getContentPane().add(calculatorResultsAreaScrollbar);
		calculatorResultsArea = new JTextArea(10, 35);
		calculatorResultsArea.setBackground(SystemColor.textHighlightText);
		calculatorResultsArea.setBounds(new Rectangle(11, 11, 0, 0));
		calculatorResultsArea.setFont(new Font("SimSun", Font.BOLD, 13));
		calculatorResultsAreaScrollbar.setViewportView(calculatorResultsArea);
		calculatorResultsArea.setEditable(false);
		
		//Calculator Buttons
		JPanel calculatorButtonsPanel = new JPanel();
		calculatorButtonsPanel.setLayout(new GridLayout(4,3));
		JButton numbersButtonsArray[] = new JButton[18];

		

		class calculatorButtonListener implements ActionListener {
			int inputTillDelimiter = 0;
			public void actionPerformed(ActionEvent e) {
				JButton buttonClicked = (JButton)e.getSource();
				if(buttonClicked.getText() == "Clear"){
					inputTillDelimiter = 0;
					calculatorInput.setText("");
				}else if(buttonClicked.getName() == "Backspace") {
				String currentText = calculatorInput.getText();
				if(currentText.length() > 0) {
					currentText = currentText.substring(0, currentText.length() - 1);
					calculatorInput.setText("");
					calculatorInput.setText(currentText);
				}
			}else if(buttonClicked.getText() != "=") {
					calculatorInput.append(buttonClicked.getText());
				}
				else {
					inputTillDelimiter = 0;
					String userInput = calculatorInput.getText();
					if(Character.isDigit(userInput.charAt(0))) {
					validateUserInput(userInput, inputTillDelimiter);
					}else {
						calculatorResultsArea.append("Please enter a valid input :*) \n");
					}
				}
			}		
		}
		for(int i = 0; i <= 17; i++) {
			numbersButtonsArray[i] = new JButton();
			numbersButtonsArray[i].setBackground(Color.DARK_GRAY);
			numbersButtonsArray[i].setForeground(Color.WHITE);
//			Border bord = new Border();
//			numbersButtonsArray[i].setBorder());
			numbersButtonsArray[i].setText(""+i);
			if(i == 10) {
				numbersButtonsArray[i].setText("+");
			}
			if(i == 11) {
				numbersButtonsArray[i].setText("-");
			}if(i == 12) {
				numbersButtonsArray[i].setText("*");
			}if(i == 13) {
				numbersButtonsArray[i].setText("/");
			}
		if(i == 14) {
			numbersButtonsArray[i].setText(".");
		}
		if(i == 15) {
				numbersButtonsArray[i].setText("Clear");
			}
		if(i == 16) {
			numbersButtonsArray[i].setText("");
			numbersButtonsArray[i].setName("Backspace");
			ImageIcon icon = new ImageIcon("C:\\Users\\oladi\\Downloads\\backspace-16.png");
			numbersButtonsArray[i].setIcon(icon);
		}
			if(i == 17) {
				numbersButtonsArray[i].setText("=");
			}
			calculatorButtonsPanel.add(numbersButtonsArray[i]);
			numbersButtonsArray[i].addActionListener(new calculatorButtonListener());
		}
		
		calculatorFrame.getContentPane().add(calculatorButtonsPanel);
		calculatorFrame.setVisible(true);
	}
	Project1_DS(){
		createCalculatorGui();
	}
	public static void main(String[] args) {
		new Project1_DS();
	}
}
