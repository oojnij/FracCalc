import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/*
 * FracCalcGui is intended to interface with the TEALS project FracCalc
 * This GUI expects the user to put in a proper input in the label as defined
 * in Frac Calc.  The GUI then calls FracCalc's processCommand Method (provided by the student)
 * 2018 Mr. Binz
 * Kamiak High School
 * Last Modified 11/9/2018
 */
public class FracCalcGui implements ActionListener{
    private JButton[] mainButtons;
    private String[] values = {"1", "2", "3", " + ", "4", "5", "6", " - ", "7", "8", "9", " * ", "_", "0", "' '", "/"};
    private JButton enter;
    private JButton clear;
    private JLabel display;
    private JFrame mainFrame;
    private JPanel north;
    private JPanel center;
    private JPanel south;
    private int fontSize = 40;

    public static void mainUI(String[] args){
        FracCalcGui gui = new FracCalcGui();
    }

    public FracCalcGui(){
        mainFrame = new JFrame("Frac Calc GUI");
        mainFrame.setLayout(new BorderLayout());
        center = new JPanel();
        center.setLayout(new GridLayout(4, 4));
        north = new JPanel();
        north.setLayout(new FlowLayout());
        south = new JPanel();
        south.setLayout(new GridLayout(1, 2));

        display = new JLabel("Frac Calc GUI");
        display.setFont(new Font("Serif", Font.BOLD, fontSize));
        north.add(display);

        mainButtons = new JButton[values.length];
        for(int i = 0; i<values.length; i++){
            mainButtons[i] = new JButton(values[i]);
            mainButtons[i].setFont(new Font("Serif", Font.BOLD, fontSize));
            mainButtons[i].addActionListener(this);
            center.add(mainButtons[i]);
        }

        clear = new JButton("CLEAR");
        clear.setFont(new Font("Serif", Font.BOLD, fontSize));
        clear.addActionListener(this);
        enter = new JButton("ENTER");
        enter.setFont(new Font("Serif", Font.BOLD, fontSize));
        enter.addActionListener(this);
        south.add(clear);
        south.add(enter);

        mainFrame.setSize(new Dimension(500, 700));
        mainFrame.add(north, BorderLayout.NORTH);
        mainFrame.add(center, BorderLayout.CENTER);
        mainFrame.add(south, BorderLayout.SOUTH);
        mainFrame.setVisible(true);

    }
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if(command.equals("CLEAR")) {
            display.setText("Frac Calc GUI");
        } else if(command.equals("ENTER")){
            display.setText(FracCalc.processCommand(display.getText()));
            //Uncomment the below line to display to the console as well as to the GUI
            //System.out.println(display.getText());
        } else if(command.equals("' '")){
            display.setText(display.getText() + " "); 
        } else{
            if(display.getText().contains("Frac")){
                display.setText(command); 
            }else{
                display.setText(display.getText() + command);
            }
        }

    }
}