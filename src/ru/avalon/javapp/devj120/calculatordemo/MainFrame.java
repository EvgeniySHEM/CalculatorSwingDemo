package ru.avalon.javapp.devj120.calculatordemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private final JLabel screen;
    private double register;
//    private char op;
    private Operation op;
    private boolean nextDigitResetsScreen;

    public MainFrame() {
        setBounds(800, 400, 400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("My calculator");

        screen = new JLabel("0", SwingConstants.RIGHT);
        screen.setFont(screen.getFont().deriveFont(48.f));
        add(screen, BorderLayout.NORTH);

        JPanel p = new JPanel(new GridLayout(4, 4));
        p.add(createButton("7", e -> processDigit('7')));
        p.add(createButton("8", e -> processDigit('8')));
        p.add(createButton("9", e -> processDigit('9')));
        p.add(createButton("+", e -> processOp(Operation.ADD)));

        p.add(createButton("4", e -> processDigit('4')));
        p.add(createButton("5", e -> processDigit('5')));
        p.add(createButton("6", e -> processDigit('6')));
        p.add(createButton("-", e -> processOp(Operation.SUB)));

        p.add(createButton("1", e -> processDigit('1')));
        p.add(createButton("2", e -> processDigit('2')));
        p.add(createButton("3", e -> processDigit('3')));
        p.add(createButton("*", e -> processOp(Operation.MUL)));

        p.add(createButton("0", e -> processDigit('0')));
        p.add(createButton(".", e -> processDot()));
        p.add(createButton("c", e -> {
            register = 0;
            op = null;
            screen.setText("0");
        }));
        p.add(createButton("/", e -> processOp(Operation.DIV)));

        add(p, BorderLayout.CENTER);
        add(createButton("=", e -> processOp(null)), BorderLayout.SOUTH);

    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(36.f));
        button.addActionListener(listener);
        return button;
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }

    private void processDigit(char digit) {
        String num = screen.getText();
        if(num.equals("0") || nextDigitResetsScreen) {
            num = Character.toString(digit);
            nextDigitResetsScreen = false;
        } else
            num += digit;
        screen.setText(num);

    }

    private void processDot () {
        String num;
        if(nextDigitResetsScreen) {
            num = "0";
            nextDigitResetsScreen = false;
        } else
        num = screen.getText();

        if(!num.contains(".")) {
            num += ".";
            screen.setText(num);
        }
    }

    private void processOp(Operation op) {
        double d;
        try {
            d = Double.parseDouble(screen.getText());
        } catch (NumberFormatException e) {
            return;
        }
        boolean error = false;
//        switch (this.op) {
//            case '+' -> register += d;
//            case '-' -> register -= d;
//            case '*' -> register *= d;
//            case '/' -> {
//                if (d != 0)
//                    register /= d;
//                else
//                    error = true;
//                break;
//            }
//            default -> register = d;
//        }
        if(this.op != null) {
            try {
                register = this.op.operator.eval(register, d);
            } catch (ArithmeticException e) {
                error = true;
            }
        } else {
            register = d;
        }
        screen.setText(error ? "error" : Double.toString(register));
        this.op = op;
        nextDigitResetsScreen = true;
    }

    private interface BinaryDoubleOperation {
        double eval(double a1, double a2);
    }

    private static enum Operation {
        ADD((a1, a2) -> a1 + a2),
        SUB((a1, a2) -> a1 - a2),
        MUL((a1, a2) -> a1 * a2),
        DIV((a1, a2) -> a1 / a2);

        BinaryDoubleOperation operator;

        Operation(BinaryDoubleOperation operator) {
            this.operator = operator;
        }


    }

}
