
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;


public class GUI extends JFrame implements ActionListener{

    JPanel downSectionPanel,left,center,right;
    JMenuBar menubar;
    JMenu menu, menuLanguage;
    JMenuItem mSave, mOpen, mExit, langPolish, langEnglish;
    JLabel leftLabel, leftLabel1, angleLabel,  prismLabel;
    JRadioButton water, air, glass, ice, diamond, firstPrism, secPrism, selected1, selected2;
    ButtonGroup group, prismGroup;
    JTextField waveLenghtTf, angleText;
    JCheckBox box;
    javax.swing.border.Border blackline;
    JButton colorButton, setButton;
    JSlider angleSlider;
    Prism p;
    JSlider colorPick;
    int rgb;

    static final int ASLIDER_MIN = 1;
    static final int ASLIDER_MAX = 90;
    static final int ASLIDER_INIT = 42;



    public GUI(){

        setTitle("Zalamanie i rozszczepienie swiatla w pryzmacie");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1080,840);
        setLayout(new BorderLayout());
        setResizable(false);

        //Menu

        menubar = new JMenuBar();
        menu = new JMenu("Menu");
        mSave = new JMenuItem("Zapisz");
        mOpen = new JMenuItem("Otworz");
        mExit = new JMenuItem("Zamknij");
        menuLanguage = new JMenu("Jezyk");
        langPolish = new JMenuItem("Polski");
        langEnglish = new JMenuItem("Angielski");

        setJMenuBar(menubar);
        menubar.add(menu);
        menu.add(mSave);
        menu.addSeparator();
        menu.add(mOpen);
        menu.addSeparator();
        menu.add(mExit);
        menu.addSeparator();
        menu.add(menuLanguage);
        menuLanguage.add(langPolish);
        menuLanguage.addSeparator();
        menuLanguage.add(langEnglish);

        //Panel w którym będzie odbywała się symulacja 
        p = new Prism();
        add(p, BorderLayout.CENTER);


        //Sekcja dolna

        downSectionPanel = new JPanel();
        downSectionPanel.setLayout(new GridLayout(1,3));
        add(downSectionPanel, BorderLayout.PAGE_END);
        left = new JPanel();
        center = new JPanel();
        right = new JPanel();
        blackline = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        // Sekcja z lewej (z wyborem  ośrodka)

        downSectionPanel.add(left);
        left.setLayout(new GridLayout(6,1,5,5));
        left.setBorder(blackline);
        leftLabel1 = new JLabel("Wybierz osrodek:");
        leftLabel1.setFont(new Font("Arial", Font.BOLD, 15));
        group = new ButtonGroup();
        water = new JRadioButton("woda");
        water.setActionCommand("woda");
        air = new JRadioButton("powietrze");
        air.setActionCommand("powietrze");
        glass = new JRadioButton("szklo");
        glass.setActionCommand("szklo");
        ice = new JRadioButton("lod");
        ice.setActionCommand("lod");
        diamond = new JRadioButton("diament");
        diamond.setActionCommand("diament");
        water.setSelected(true);


        group.add(water);
        group.add(air);
        group.add(glass);
        group.add(ice);
        group.add(diamond);
        left.add(leftLabel1);
        left.add(water);
        left.add(air);
        left.add(glass);
        left.add(ice);
        left.add(diamond);
        
        /*
         * Wybór ośrodka (zmiana koloru tła i współ. załamania)
         * *
         */
        water.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            	p.setBackground(new Color(224, 255, 255));
                p.setCoef2(1.33);
                selected1 = water;
            }
        });

        ice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               // p.setColor(new Color(0x733F5F7B, true));
            	p.setBackground(new Color(240, 255, 255));
            
                p.setCoef2(1.31);
                selected1 = ice;
            }
        });
        diamond.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //p.setColor(new Color(0x91267B59, true));
            	p.setBackground(new Color(230, 230, 250));
                p.setCoef2(2.42);
                selected1 = diamond;
            }
        });
        glass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               // p.setColor(new Color(0xA47B7178, true));
            	p.setBackground(new Color(152, 251, 152));
                p.setCoef2(1.5);
                selected1 = glass;
            }
        });
        air.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //p.setColor(new Color(0xA0425A7B, true));
            	p.setBackground(new Color(245, 245, 245));
                p.setCoef2(1);
                selected1 = air;
            }
        });

       water.doClick();


        /* Sekcja środkowa
         * (z długości fali,
         * wyborem wiązki światła białego,
         * wyborem koloru tła
         * oraz przyciskiem "USTAW")
         */

        downSectionPanel.add(center);
        center.setLayout(new GridLayout(7,1,10,10));
        center.setBorder(blackline);
        leftLabel = new JLabel("Podaj dlugosc fali:");
        leftLabel.setFont(new Font("Arial", Font.BOLD, 15));
        waveLenghtTf = new JTextField();
        box = new JCheckBox("Wiazka swiatla bialego");
        colorButton = new JButton("Wybierz kolor tla");		//Wybór koloru tła
        colorPick = new JSlider(JSlider.HORIZONTAL, 380, 780, 500);
        colorPick.setMinorTickSpacing(40);
        colorPick.setMajorTickSpacing(40);
        colorPick.setMinorTickSpacing(10);
        colorPick.setPaintTicks(true);
        Hashtable<Integer, JLabel> labels2 = new Hashtable<>(); 
        labels2.put(380, new JLabel("380"));
        labels2.put(420, new JLabel("420"));
        labels2.put(460, new JLabel("460"));
        labels2.put(500, new JLabel("500"));
        labels2.put(540, new JLabel("540"));
        labels2.put(580, new JLabel("580"));
        labels2.put(620, new JLabel("620"));
        labels2.put(660, new JLabel("660"));
        labels2.put(700, new JLabel("700"));
        labels2.put(740, new JLabel("740"));
        labels2.put(780, new JLabel("780"));
        colorPick.setLabelTable(labels2);
        colorPick.setPaintLabels(true);

        box.addActionListener(new ActionListener() { //białe światło 
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(box.isSelected()) {
                    p.setLineColor(Color.WHITE); // ustawia kolor wiązki na biały 
                    waveLenghtTf.setEnabled(false); // dezaktywuje wybór długości fali
                    colorPick.setEnabled(false); // dezaktywuje suwak 
                    p.setWhiteLight(true); // ustawienie wartości zmiennej whitelight na true 
                }
                else {
                    waveLenghtTf.setEnabled(true);
                    colorPick.setEnabled(true);
                    p.setWhiteLight(false);

                }
            }
        });

        waveLenghtTf.setText(Integer.toString(colorPick.getValue())); // ustawienie tekstu z wartości suwaka (dł.fali)
        colorPick.addChangeListener(new ChangeListener() { // zmiana koloru linii w zależności od wartości suwaka  
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                rgb = colorPick.getValue(); // zmienna przechowująca wartość z suwaka 
                Color kolor_fali;
                if (rgb >= 380 && rgb < 430) {                //FIOLETOWY
                    kolor_fali = new Color(90, 0, 90);
                } else if (rgb >= 430 && rgb < 450) {                //INDYGO
                    kolor_fali = new Color(75, 0, 130);
                } else if (rgb >= 450 && rgb < 500) {                //NIEBIESKI
                    kolor_fali = new Color(0, 0, 255);
                } else if (rgb >= 500 && rgb < 520) {                //CYJAN
                    kolor_fali = new Color(0, 255, 255);
                } else if (rgb >= 520 && rgb < 565) {                //ZIELONY
                    kolor_fali = new Color(0, 255, 0);
                } else if (rgb >= 565 && rgb < 590) {                //ŻÓŁTY
                    kolor_fali = new Color(252, 233, 3);
                } else if (rgb >= 590 && rgb < 635) {                //POMARAŃCZOWY
                    kolor_fali = new Color(252, 147, 3);
                } else if (rgb >= 635 && rgb < 701) {                //CZEROWNY
                    kolor_fali = new Color(255, 0, 0);
                } else {
                    kolor_fali = new Color(255, 255, 255);
                }
                p.setLineColor(kolor_fali);
                waveLenghtTf.setText(Integer.toString(rgb));
            }
        });


         // zmiana koloru tła 
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Color color = JColorChooser.showDialog(null, "title", Color.BLUE);;
                p.setBackground(color);
            }
        });



        setButton = new JButton("USTAW");
        setButton.setFont(new Font("Arial", Font.BOLD, 25));
        setButton.setBackground(Color.cyan);
       // setButton.setOpaque(true);
        //setButton.setBorderPainted(false);
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { // ustawienie koloru linii w zależności od wartości w polu tekstowym 
                Color kolor_fali;
                int col = Integer.parseInt(waveLenghtTf.getText());
                if (col >= 380 && col < 430) {                //FIOLETOWY
                    kolor_fali = new Color(90, 0, 90);
                } else if (col >= 430 && col < 450) {                //INDYGO
                    kolor_fali = new Color(75, 0, 130);
                } else if (col >= 450 && col < 500) {                //NIEBIESKI
                    kolor_fali = new Color(0, 0, 255);
                } else if (col >= 500 && col < 520) {                //CYJAN
                    kolor_fali = new Color(0, 255, 255);
                } else if (col >= 520 && col < 565) {                //ZIELONY
                    kolor_fali = new Color(0, 255, 0);
                } else if (col >= 565 && col < 590) {                //ŻÓŁTY
                    kolor_fali = new Color(252, 233, 3);
                } else if (col >= 590 && col < 635) {                //POMARAŃCZOWY
                    kolor_fali = new Color(252, 147, 3);
                } else if (col >= 635 && col < 701) {                //CZEROWNY
                    kolor_fali = new Color(255, 0, 0);
                } else {
                    kolor_fali = new Color(255, 255, 255);
                }
                p.setLineColor(kolor_fali);
                p.setBeamAngle(Integer.parseInt(angleText.getText()));
                colorPick.setValue(Integer.parseInt(waveLenghtTf.getText()));
                angleSlider.setValue(Integer.parseInt(angleText.getText()));
                // odświerzenie symulacji 
                if(firstPrism.isSelected())
                    firstPrism.doClick();
                if (secPrism.isSelected())
                    secPrism.doClick();

            }
        });


        center.add(leftLabel);
        center.add(waveLenghtTf);
        center.add(box);
        center.add(colorPick);
        center.add(colorButton);
        center.add(setButton);

        /*Sekcja z prawej
         * (ustawianie kąta padania: pole tekstowe/suwak
         * wybór pryzmatu
         */

        downSectionPanel.add(right);
        right.setLayout(new GridLayout(6,1,5,5));
        right.setBorder(blackline);
        angleLabel = new JLabel("Kat padania wiazki:");
        angleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        prismLabel = new JLabel("Pryzmat:");
        prismLabel.setFont(new Font("Arial", Font.BOLD, 15));
        angleText = new JTextField();					//pole tekstowe
        angleSlider = new JSlider(JSlider.HORIZONTAL,	//suwak
                ASLIDER_MIN, ASLIDER_MAX, ASLIDER_INIT);
        angleSlider.setMinorTickSpacing(10);
        angleSlider.setMajorTickSpacing(10);
        angleSlider.setMinorTickSpacing(5);
        angleSlider.setPaintTicks(true);
        angleText.setText(Integer.toString(angleSlider.getValue()));
        // pobranie wartości ze slidera i ustwienie wartości w polu tekstowym (kąt padania)
        angleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                p.setBeamAngle(angleSlider.getValue());
                angleText.setText(Integer.toString(angleSlider.getValue()));
            }
        });
        Hashtable<Integer, JLabel> labels = new Hashtable<>(); 
        labels.put(0, new JLabel("0"));
        labels.put(10, new JLabel("10"));
        labels.put(20, new JLabel("20"));
        labels.put(30, new JLabel("30"));
        labels.put(40, new JLabel("40"));
        labels.put(50, new JLabel("50"));
        labels.put(60, new JLabel("60"));
        labels.put(70, new JLabel("70"));
        labels.put(80, new JLabel("80"));
        labels.put(90, new JLabel("90"));
        angleSlider.setLabelTable(labels);
        angleSlider.setPaintLabels(true);
        prismGroup = new ButtonGroup();
        firstPrism = new JRadioButton("60-60-60");
        firstPrism.setSelected(true);
        secPrism = new JRadioButton("45-90-45");
        //ustawienie wartości 
        p.setSecondRadioB(secPrism);
        p.setFristRadioB(firstPrism);
        prismGroup.add(firstPrism);
        prismGroup.add(secPrism);
        right.add(angleLabel);
        right.add(angleText);
        right.add(angleSlider);
        right.add(prismLabel);
        right.add(firstPrism);
        right.add(secPrism);


        //ZMIANA JEZYKA NA ANGIELSKI 
        ActionListener englishListener = new ActionListener () {
            public void actionPerformed (ActionEvent a) {
                setTitle("Prism Simulation");
                mSave.setText("Save file");
                mOpen.setText("Open file");
                mExit.setText("Exit");
                menuLanguage.setText("Choose language");
                langPolish.setText("Polish");
                langEnglish.setText("English");
                water.setText("water");
                air.setText("air");
                glass.setText("glass");
                ice.setText("ice");
                diamond.setText("diamond");
                leftLabel.setText("Wavelenght:");
                box.setText("White light");
                colorButton.setText("Choose background color");
                setButton.setText("SET");
                angleLabel.setText("Beam Angle:");
                prismLabel.setText("Prism:");
                leftLabel1.setText("Choose material:");
            }
        };
        langEnglish.addActionListener(englishListener);

        //ZMIANA JEZYKA NA POLSKI 
        ActionListener polishListener = new ActionListener () {
            public void actionPerformed (ActionEvent a) {
                setTitle("Załamanie i rozszczepienie światla w pryzmacie");
                mSave.setText("Zapisz");
                mOpen.setText("Otwórz");
                mExit.setText("Zamknij");
                menuLanguage.setText("Język");
                langPolish.setText("Polski");
                langEnglish.setText("Angielski");
                water.setText("woda");
                air.setText("powietrze");
                glass.setText("szkło");
                ice.setText("lód");
                diamond.setText("diament");
                leftLabel.setText("Podaj długość fali:");
                box.setText("Wiązka światła białego");
                colorButton.setText("Wybierz kolor tła");
                setButton.setText("USTAW");
                angleLabel.setText("Kąt padania wiązki:");
                prismLabel.setText("Pryzmat:");
                leftLabel1.setText("Wybierz ośrodek:");

            }
        };
        langPolish.addActionListener(polishListener);
        
// ZAPIS DO PLIKU z wykorzystaniem obiektu Properties(key,value)
        mSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Properties prop = new Properties();
                prop.put("Osrodek", selected1.getText());
                prop.put("Dlugosc fali", Integer.toString(colorPick.getValue()));
                prop.put("Kat padania", Integer.toString(angleSlider.getValue()));
                if(firstPrism.isSelected())
                    prop.put("Pryzmat", firstPrism.getText());
                if(secPrism.isSelected())
                    prop.put("Pryzmat", secPrism.getText());
                prop.put("Swiatlo biale", Boolean.toString(box.isSelected()));
                prop.put("Kolor tla", Integer.toString(p.getBackground().hashCode()));

                try {
                    prop.store(new FileOutputStream("prop.properties"),"");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
     // ODCZYT Z PLIKU z wykorzystaniem obiektu Properties(key,value)
        mOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Properties prop = new Properties();
                try {
                    prop.load(new FileInputStream("prop.properties"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(String key : prop.stringPropertyNames()) {
                    String value = prop.getProperty(key);
                    if(key.equals("Osrodek")) {
                        if(value.equals(water.getText())){
                            water.setSelected(true);
                            water.doClick();
                        }
                        if(value.equals(air.getText())){
                            air.setSelected(true);
                            air.doClick();
                        }
                        if(value.equals(diamond.getText())){
                            diamond.setSelected(true);
                            diamond.doClick();
                        }
                        if(value.equals(ice.getText())){
                            ice.setSelected(true);
                            ice.doClick();
                        }
                        if(value.equals(glass.getText())){
                            glass.setSelected(true);
                            glass.doClick();
                        }
                    }
                    if(key.equals("Dlugosc fali")) {
                        colorPick.setValue(Integer.parseInt(value));
                    }
                    if(key.equals("Kat padania")) {
                        angleSlider.setValue(Integer.parseInt(value));
                    }
                    if(key.equals("Pryzmat")) {
                        if(value.equals("60-60-60")) {
                            firstPrism.setSelected(true);
                            firstPrism.doClick();
                        }
                        if(value.equals("45-90-45")){
                            secPrism.setSelected(true);
                            secPrism.doClick();
                        }
                    }
                    if(key.equals("Swiatlo biale")) {
                        if(Boolean.parseBoolean(value)) {
                            box.setSelected(true);
                            //box.setSelected(true);
                            box.doClick();
                        }
                    }
                    if(key.equals("Kolor tla")) {
                        p.setBackground(new Color(Integer.parseInt(value)));
                    }
                }
            }
        });
// zakończenie programu po wciśniętu Exit z menu
        mExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}