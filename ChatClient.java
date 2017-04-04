import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChatClient extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8476684537362657667L;

	JTextField tf = new JTextField();
	JTextArea ta = new JTextArea(10, 40);
	PrintWriter pw = null;
	BufferedReader br = null;
	
	public ChatClient(){
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 300);
	
		tf.setEditable(false);
		ta.setEditable(false);
		JScrollPane jp=new JScrollPane(ta);
		this.getContentPane().add(tf, BorderLayout.NORTH);
		this.getContentPane().add(jp, BorderLayout.CENTER);
	
		tf.addActionListener(this);
	}
	
	public void haveFun() throws IOException{
		String serverAddress = JOptionPane.showInputDialog("IP Address?");
		Socket s = new Socket(serverAddress, 9234);
		pw = new PrintWriter(s.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		while(true){
			String line = br.readLine();
			if(line.startsWith("SUBMITNAME")){
				pw.println(JOptionPane.showInputDialog("What is your name?"));
			}else if(line.startsWith("ACCEPTED")){
				tf.setEditable(true);
			}else if (line.startsWith("MESSAGE")){
				ta.append(line + "\n");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ChatClient cl = new ChatClient();
		cl.setVisible(true);
		cl.haveFun();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		pw.println(tf.getText());
		tf.setText("");
		
	}

}