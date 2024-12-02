package uiowa.cs3010.maze6;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//This code is not efficient, nor secure. I wrote it when I was absolutely baked. I do not code well when baked.
public class KMain extends JFrame{
    public static void main(String[] args) {
        JFrame difficultySelect=new JFrame("KMain Menu");
        difficultySelect.setDefaultCloseOperation(EXIT_ON_CLOSE);
        difficultySelect.setLayout(new FlowLayout());
        difficultySelect.setSize(300,300);
        JButton cjDifficulty=new JButton("CJ's Hard Mode");
        JButton kDifficulty=new JButton("Kay's Easy Mode");
        cjDifficulty.setPreferredSize(new Dimension(300,200));
        kDifficulty.setPreferredSize(new Dimension(300,200));
        kDifficulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KGameController game = new KGameController();
                difficultySelect.dispose();
                game.setVisible(true);
            }
        });
        difficultySelect.add(cjDifficulty);
        difficultySelect.add(kDifficulty);
        difficultySelect.pack();
        difficultySelect.setLocationRelativeTo(null);
        difficultySelect.setVisible(true);

    }
}