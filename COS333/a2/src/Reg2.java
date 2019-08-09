import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.awt.event.KeyAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;
import java.sql.PreparedStatement;
import java.io.*;
import java.util.*;

public class Reg2 extends JFrame
{
	private String host;
	private int port;
	JList<String> dataList = new JList<String>();
	public Reg2(String host, int port)
	{
		try 
		{
			this.host = host;
			this.port = port;
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JTextField deptTextField = new JTextField(35);
			
			JTextField courseNumTextField = new JTextField(10);
			JTextField areaTextField = new JTextField(10);
			JTextField titleTextField = new JTextField(10);
			courseNumTextField.addKeyListener(new KeyAdapter() {
								public void keyReleased(KeyEvent e) {
									String deptField = deptTextField.getText();
									String courseNumField = courseNumTextField.getText();
									String areaField = areaTextField.getText();
									String titleField = titleTextField.getText();

									Vector<String> info = toSocket(deptField, courseNumField, areaField, titleField);
									if (info.isEmpty())
									{
										dataList.setListData(info);
										dataList.updateUI();
									}
									else if (info.get(0).equals("err"))
									{
										JOptionPane.showMessageDialog(null,info.get(1), "", JOptionPane.INFORMATION_MESSAGE);
									}
									else 
									{
										dataList.setListData(info);
										dataList.setSelectedIndex(0);
										dataList.updateUI();
									}
								}

								public void keyTyped(KeyEvent e) {
								}

								public void keyPressed(KeyEvent e) {
								}
							});
			areaTextField.addKeyListener(new KeyAdapter() {
								public void keyReleased(KeyEvent e) {
									String deptField = deptTextField.getText();
									String courseNumField = courseNumTextField.getText();
									String areaField = areaTextField.getText();
									String titleField = titleTextField.getText();


									Vector<String> info = toSocket(deptField, courseNumField, areaField, titleField);
									if (info.isEmpty())
									{
										dataList.setListData(info);
										dataList.updateUI();
									}
									else if (info.get(0).equals("err"))
									{
										JOptionPane.showMessageDialog(null,info.get(1), "", JOptionPane.INFORMATION_MESSAGE);
									}
									else 
									{
										dataList.setListData(info);
										dataList.setSelectedIndex(0);
										dataList.updateUI();
									}
								}

								public void keyTyped(KeyEvent e) {
								}

								public void keyPressed(KeyEvent e) {
								}
							});
			titleTextField.addKeyListener(new KeyAdapter() {
								public void keyReleased(KeyEvent e) {
									String deptField = deptTextField.getText();
									String courseNumField = courseNumTextField.getText();
									String areaField = areaTextField.getText();
									String titleField = titleTextField.getText();

									Vector<String> info = toSocket(deptField, courseNumField, areaField, titleField);
									if (info.isEmpty())
									{
										dataList.setListData(info);
										dataList.updateUI();
									}
									else if (info.get(0).equals("err"))
									{
										JOptionPane.showMessageDialog(null,info.get(1), "", JOptionPane.INFORMATION_MESSAGE);
									}
									else 
									{
										dataList.setListData(info);
										dataList.setSelectedIndex(0);
										dataList.updateUI();
									}
								}

								public void keyTyped(KeyEvent e) {
								}

								public void keyPressed(KeyEvent e) {
								}
							});
			deptTextField.addKeyListener(new KeyAdapter() {
								public void keyReleased(KeyEvent e) {
									String deptField = deptTextField.getText();
									String courseNumField = courseNumTextField.getText();
									String areaField = areaTextField.getText();
									String titleField = titleTextField.getText();

									
									Vector<String> info = toSocket(deptField, courseNumField, areaField, titleField);
									if (info.isEmpty())
									{
										dataList.setListData(info);
										dataList.updateUI();
									}
									else if (info.get(0).equals("err"))
									{
										JOptionPane.showMessageDialog(null,info.get(1), "", JOptionPane.INFORMATION_MESSAGE);
									}
									else 
									{
										dataList.setListData(info);
										dataList.setSelectedIndex(0);
										dataList.updateUI();
									}
								}

								public void keyTyped(KeyEvent e) {
								}

								public void keyPressed(KeyEvent e) {
								}
							});												
			// Labal for textbox
			JLabel deptLabel =      new JLabel("Department:");
			JLabel courseNumLabel = new JLabel("CourseNum:");
			JLabel areaLabel =      new JLabel("           Area:");
			JLabel titleLabel =     new JLabel("           Title:");
			
			// gridbox
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			JPanel dataPair = new JPanel();
			dataPair.setLayout(new GridBagLayout());
			dataPair.add(deptLabel);
			dataPair.add(deptTextField, gbc);
			
			JPanel courseNumPair = new JPanel();
			courseNumPair.setLayout(new GridBagLayout());
			courseNumPair.add(courseNumLabel);
			courseNumPair.add(courseNumTextField, gbc);

			JPanel areaPair = new JPanel();
			areaPair.setLayout(new GridBagLayout());
			areaPair.add(areaLabel);
			areaPair.add(areaTextField, gbc);

			JPanel titlePair = new JPanel();
			titlePair.setLayout(new GridBagLayout());
			titlePair.add(titleLabel);
			titlePair.add(titleTextField, gbc);
					
			JPanel userInputPanel = new JPanel();
			userInputPanel.setLayout(new BoxLayout (userInputPanel, BoxLayout.Y_AXIS));
			userInputPanel.add(dataPair, Component.RIGHT_ALIGNMENT);
			userInputPanel.add(courseNumPair, Component.RIGHT_ALIGNMENT);
			userInputPanel.add(areaPair, Component.RIGHT_ALIGNMENT);
			userInputPanel.add(titlePair, Component.RIGHT_ALIGNMENT);
		
		dataList.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
						if (dataList.getModel().getSize() > 0)
						{
							String classid = dataList.getSelectedValue();
							classid = classid.substring(0, classid.indexOf(" "));
							String t = toSocket(classid);
							if (t.startsWith("err"))
								JOptionPane.showMessageDialog(null, t.substring(3), "Class Info", JOptionPane.INFORMATION_MESSAGE);
							else 
								JOptionPane.showMessageDialog(null, t, "Class Info", JOptionPane.INFORMATION_MESSAGE);
						}	
				}
			}
		});
		
		dataList.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					if (dataList.getModel().getSize() > 0) {
						String classid = dataList.getSelectedValue();
						classid = classid.substring(0, classid.indexOf(" "));
						String t = toSocket(classid);
						if (t.startsWith("err"))
							JOptionPane.showMessageDialog(null, t.substring(3), "Class Info", JOptionPane.INFORMATION_MESSAGE);
						else 
							JOptionPane.showMessageDialog(null, t, "Class Info", JOptionPane.INFORMATION_MESSAGE);
					}	
				}
			}
		});

		    
		    JScrollPane scrollableData = new JScrollPane(dataList);
		    scrollableData.setHorizontalScrollBarPolicy(
		    		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		    scrollableData.setVerticalScrollBarPolicy(
		    		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		    
		    JPanel listBoxPanel = new JPanel();
		    listBoxPanel.setLayout(new BorderLayout());
		    listBoxPanel.add(scrollableData);
		    
		    /* Panel and component for title panel */
		    
		    JLabel frameTitleLabel = new JLabel("Course Search Program");
		    JPanel titlePanel = new JPanel();
		    titlePanel.add(frameTitleLabel);
		    
		    /* Frame and frame layout */
		    JFrame frame = new JFrame();
		    frame.setTitle("Course Search");
		    frame.add(userInputPanel, BorderLayout.NORTH);
		    frame.add(listBoxPanel, BorderLayout.CENTER);
		    
			
			/* Wrapping it all up */
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenWidth = screenSize.width;
			int screenHeight = screenSize.height;
			frame.setSize(screenWidth/2, screenHeight/2);
			
			frame.setLocationByPlatform(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			Vector<String> start = toSocket("", "", "", "");
			if (start.get(0).equals("err"))
				JOptionPane.showMessageDialog(null, start.get(1), "", JOptionPane.INFORMATION_MESSAGE);
			else
				dataList.setListData(start);
			
		}
		catch (Exception e) {System.err.println(e);System.exit(1);}
		
	}
	
	private Vector<String> toSocket(String a, String b, String c, String d)
	{
		Vector<String> info = new Vector<String>();
		try
		{
			Socket socket = new Socket(host, port);

			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			OutputStream socketOutputStream = socket.getOutputStream();
			PrintWriter socketPrintWriter = new PrintWriter(socketOutputStream);
			
			if (a.equals("")) a = "%"; else a = "%" + replaceWildCard(a) + "%";
			if (b.equals("")) b = "%"; else b = "%" + replaceWildCard(b) + "%";
			if (c.equals("")) c = "%"; else c = "%" + replaceWildCard(c) + "%";
			if (d.equals("")) d = "%"; else d = "%" + replaceWildCard(d) + "%";

			socketPrintWriter.println(a + " " + b + " " + c + " " + d);
			socketPrintWriter.flush();
			if (true)
			{
				info = (Vector<String>) in.readObject();
				
			}
			else
			{
				System.err.println("The echo server crashed");
			}
			socket.close();
			
		}
		catch (Exception e) {info.add("err"); info.add(e.toString());}
		return info;
	}
	
	private String toSocket(String id)
	{
		String info = null;
		try
		{
			Socket socket = new Socket(host, port);

			InputStream socketInputStream = socket.getInputStream();
			Scanner socketScanner = new Scanner(socketInputStream);

			OutputStream socketOutputStream = socket.getOutputStream();
			PrintWriter socketPrintWriter = new PrintWriter(socketOutputStream);
			
			
			socketPrintWriter.println(id);
			socketPrintWriter.flush();
	
	
			if (true)
			{
				socketScanner.useDelimiter("!");
				info = socketScanner.next().substring(4);
			}
			else
			{
				System.err.println("The echo server crashed");
			}
			socket.close();
		}
		catch (Exception e) {return "err" + e.toString();}
		return info;
	}
	
	public String replaceWildCard(String str)
	{
		str = str.replace("_", "[_]");
		str = str.replaceAll("%", "[%]");	
		return str;	
	}
	
	public static void main(String[] args) 
	{
		if (args.length != 2)
		{  
			System.err.println("Usage: reg host port");
			System.exit(1);
		}
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		new Reg2(host,port).setVisible(true);
	}
}