package com.example.lowpricesapp.panel;

import com.example.lowpricesapp.MyOwnLayout;
import com.example.lowpricesapp.service.ParseWebPageService;
import com.example.lowpricesapp.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

@Controller
public class AddProductPanel extends MyOwnLayout {
    private JButton cancel;
    private JButton save;
    private JButton load;
    private JTextArea webAdress;
    private static Logger logger = Logger.getLogger(AddProductPanel.class);

    @Autowired
    private MainPanel mainPanel;
    @Autowired
    private ProductPanel productPanel;
    @Autowired
    private ProductService productService;
    @Autowired
    private ParseWebPageService parseWebPageService;

    public AddProductPanel() {
    }

    @PostConstruct
    public void init() {
        logger.info("AddProductPanel constructor");
        setLayout(null);
        webAdress = new JTextArea("", 3, 30);
        webAdress.setLineWrap(true);
        webAdress.setBounds(setCenterXPositionInWindow(30,
                webAdress.getPreferredSize()));
        add(webAdress);

        load = new JButton("Load");
        load.setBounds(setLeftUpperCorner(780, 14, load.getPreferredSize()));
        add(load);
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDataAboutProduct();
            }
        });



        save = new JButton("Save");
        save.setBounds(setCenterXPositionInWindow(740, save.getPreferredSize()));
        add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                saveAction();
            }
        });

        cancel = new JButton("Back");
        cancel.setBounds(setLeftUpperCorner(300, 727, cancel.getPreferredSize()));
        add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToMainPanel();
            }
        });
        logger.info("End of AddProductPanel constructor");
    }

    private void saveAction() {
        String result;
        try {
            result = productService.saveNewProduct(
                    productPanel.getAllTextField(),
                    productPanel.getImage());

            JOptionPane.showConfirmDialog(this, result,
                    result, JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);

            goToMainPanel();
        } catch (Exception e1) {
            e1.printStackTrace();

            logger.warn("some exceptions:" + e1.getMessage());
            JOptionPane
                    .showConfirmDialog(
                            this,
                            "oh yoy, some exceptions, look to warning log file",
                            "something goes wrong: " + e1.getMessage(),
                            JOptionPane.CLOSED_OPTION,
                            JOptionPane.ERROR_MESSAGE);
        }
    }

    public void goToMainPanel() {

        clearPanel();

        JFrame root = (JFrame) SwingUtilities.getRoot(AddProductPanel.this);
        root.remove(AddProductPanel.this);
        root.add(mainPanel);
        root.validate();
        root.repaint();
    }

    private void clearPanel() {

        webAdress.setText("");
        productPanel.clearAll();
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

    }

    public void setProductParseWebPage(ParseWebPageService parseWebPageService) {
        this.parseWebPageService = parseWebPageService;
    }

    private void getDataAboutProduct() {
        try {

            String data[] = parseWebPageService.parseDataFromUrl(webAdress
                    .getText());
//            logger.info("informacje ze strony: " + data[0] + ",  " + data[1]);
            productPanel.setAllTextField(data);

            BufferedImage img = parseWebPageService.getImage();
            productPanel.setImage(img);

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.warn("ex from parsing www page, some exceptions: "
                    + e1.getMessage());
        }
        productPanel.setBounds(300, 100, 600, 600);
        add(productPanel);
        validate();
        repaint();
    }
}
