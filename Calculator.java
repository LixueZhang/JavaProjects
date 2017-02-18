/*
 * a simple calculator designed by java.
 * Author: Lixue Zhang
 */

package windowAPP;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Calculator extends JFrame {
	private static final long serialVersionUID = -1641338545437576169L;
	private JLabel showRes;
	private JButton zero, one, two, three, four, five, six, seven, eight, nine;
	private JButton point, ad, dec, mul, div, eq;
	private JButton clc, quit;
	Math calc;
	EventResponse er;

	public Calculator()
	{
		super("calculator");
		data = "";
		init_var();
		panel_layout();
		setSize(300, 300);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		calc = new Math();
		init_action_listener();
		
	}
	
	private void init_action_listener()
	{
		er = new EventResponse();
		zero.addActionListener(er);
		one.addActionListener(er);
		two.addActionListener(er);
		three.addActionListener(er);
		four.addActionListener(er);
		five.addActionListener(er);
		six.addActionListener(er);
		seven.addActionListener(er);
		eight.addActionListener(er);
		nine.addActionListener(er);
		ad.addActionListener(er);
		dec.addActionListener(er);
		mul.addActionListener(er);
		div.addActionListener(er);
		eq.addActionListener(er);
		point.addActionListener(er);
		clc.addActionListener(er);
		quit.addActionListener(er);
	}
	
	private void init_var()
	{
		// 0 ~ 9
		zero = new JButton("0");
		zero.setBackground(Color.BLACK); 
		one = new JButton("1");
		two = new JButton("2");
		three = new JButton("3");
		four = new JButton("4");
		five = new JButton("5");
		six = new JButton("6");
		seven = new JButton("7");
		eight = new JButton("8");
		nine = new JButton("9");
		// other
		point = new JButton(".");
		ad = new JButton("+");
		dec = new JButton("-");
		mul = new JButton("x");
		div = new JButton("/");
		eq = new JButton("=");
		clc = new JButton("clear");
		quit = new JButton("quit");
		//screen
		showRes = new JLabel("0", JLabel.LEFT);
		showRes.setBounds(0,0, 300, 30);
		showRes.setOpaque(true);
		showRes.setBackground(Color.GREEN); 
	}
	
	private void panel_layout()
	{
		JPanel panel_center = new JPanel(new GridLayout(4,4));
		JPanel panel_south = new JPanel(new FlowLayout());
		JPanel panel_north = new JPanel();
		add(panel_north, BorderLayout.NORTH);
		panel_north.add(showRes);
		add(panel_south, BorderLayout.SOUTH);
		add(panel_center);
		//line 1
		panel_center.add(seven);
		panel_center.add(eight);
		panel_center.add(nine);
		panel_center.add(div);
		//line 2
		panel_center.add(four);
		panel_center.add(five);
		panel_center.add(six);
		panel_center.add(mul);
		//line 3
		panel_center.add(one);
		panel_center.add(two);
		panel_center.add(three);
		panel_center.add(dec);
		//line 4
		panel_center.add(zero);
		panel_center.add(point);
		panel_center.add(eq);
		panel_center.add(ad);
		// add clear and quit
		panel_south.add(clc);
		panel_south.add(quit);
	}
	//input data. 
	String data;
	String prior_data;
	class EventResponse implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==clc)
			{
				showRes.setText("0");
				data = "";
				calc.clear();				
			}
			else if(e.getSource() == quit)
			{
				System.exit(0);
			}
			else if(e.getSource() == ad || e.getSource()==dec || e.getSource()==mul || 
					e.getSource()==div || e.getSource() == eq)
			{				
				String cur_op = ((JButton) e.getSource()).getText();
				if(data.equals(""))
					data = prior_data;
				double ret = calc.mathCompute(Double.parseDouble(data), cur_op.charAt(0));
				
				if(ret%1 != 0)
					showRes.setText(""+ ret); 
				else
					showRes.setText(""+ (int)ret); 
				if(e.getSource() == eq)
					data = ret+"";
				else
				{
					if(!data.equals(""))
						prior_data = data;
					data = "";
				}
			}
			else
			{
				String cur_op = ((JButton) e.getSource()).getText();
				data += cur_op;
				showRes.setText(data);
			}
		}	
	}
	
	public static void main(String argv[])
	{
		new Calculator();
	}
}
/*
 * used to do + - * /
 */
class Math
{
	Stack<Double> operands;
	Stack<Character> operators;
	
	public Math()
	{
		operands = new Stack<Double>();
		operators = new Stack<Character>();
	}
	
	public double mathCompute(double data, char op)
	{
		double temp = data;
		if(op=='+' || op=='-') //clear the stack and return the result.
		{
			while(!operators.isEmpty())
			{
				temp = arithmetic(operands.pop(), temp, operators.pop());
			}
			operands.push(temp);
			operators.push(op);
		}
		else if(op=='x' || op=='/')
		{
			if(!operators.isEmpty() && (operators.peek()=='x' || operators.peek()=='/'))
				temp = arithmetic(operands.pop(), temp, operators.pop());
			operands.push(temp);
			operators.push(op);
		}
		else
		{
			while(!operators.isEmpty())
				temp = arithmetic(operands.pop(), temp, operators.pop());
		}
		return temp;
	}
	private double arithmetic(double left, double right, char op)
	{
		if(op == '+')
			return left + right;
		if(op == '-')
			return left - right;
		if(op == 'x')
			return left * right;
		if(op == '/')
			return left / right;
		return 0.0;
	}
	public void clear()
	{
		while(!operands.isEmpty()) operands.pop();
		while(!operators.isEmpty()) operators.pop();
	}
}