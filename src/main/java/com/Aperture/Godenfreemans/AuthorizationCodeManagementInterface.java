package com.Aperture.Godenfreemans;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AuthorizationCodeManagementInterface {
    private JPanel Root;
    private JTable table1;
    private JButton button1;
    private JTextArea logTextView;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;

    private ArrayList<AuthorizationCode> authorizationCodeArrayList = new ArrayList<>();

    private AuthorizationCodeManagementInterface() {


        Gson gson = new Gson();

        File jsonFile = new File("AuthorizationCode.json");
        if (!jsonFile.exists()) {
            try {
                logTextView.append((jsonFile.createNewFile() ? "-- Create file successful." : "Create file fail") + "\n");
            } catch (IOException e) {
                logTextView.append(e.toString() + "\n");
            }
        }
        try {
            FileReader jsonFileReader = new FileReader(jsonFile);
            BufferedReader bufferedReader = new BufferedReader(jsonFileReader);
            StringBuilder jsonStringBuilder = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(s).append("\n");
            }
            bufferedReader.close();

            String oldJson = jsonStringBuilder.toString();
            if (!oldJson.isEmpty()) {
                authorizationCodeArrayList = new ArrayList<>(gson.fromJson(oldJson,
                        new TypeToken<List<AuthorizationCode>>() {}.getType()));
            } else {
                authorizationCodeArrayList = new ArrayList<>();
            }
            logTextView.append("-- get " + authorizationCodeArrayList.size() + " Code.\n");
//            for (AuthorizationCode authorizationCode : authorizationCodeArrayList) {
//                System.out.println(authorizationCode.getCode());
//            }
        } catch (IOException ignored) { }

        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Gson gson = new Gson();

                File jsonFile = new File("AuthorizationCode.json");
                if (!jsonFile.exists()) {
                    try {
                        logTextView.append(jsonFile.createNewFile() ? "-- Create file successful." : "Create file fail" + "\n");
                    } catch (IOException e) {
                        logTextView.append(e.toString() + "\n");
                    }
                } else {
                    if (new File("AuthorizationCode.json.bak").exists()) {
                        logTextView.append(new File("AuthorizationCode.json.bak").delete() ?
                                "-- Delete file successful." : "Delete file fail" + "\n");
                    }
                    try {
                        Files.copy(jsonFile.toPath(), new File("AuthorizationCode.json.bak").toPath());
                    } catch (IOException e) {
                        logTextView.append("-- Backup file fail" + "\n");
                    }

                }
                try {
                    FileReader jsonFileReader = new FileReader(jsonFile);
                    BufferedReader bufferedReader = new BufferedReader(jsonFileReader);
                    StringBuilder jsonStringBuilder = new StringBuilder();
                    String s;
                    while ((s = bufferedReader.readLine()) != null) {
                        jsonStringBuilder.append(s).append("\n");
                    }
                    bufferedReader.close();

                    String oldJson = jsonStringBuilder.toString();
                    String newJson;
                    AuthorizationCode newAuthorizationCode = new AuthorizationCode(
                            textField1.getText(),
                            textField2.getText(),
                            textField3.getText(),
                            textField4.getText());
                    if (!oldJson.isEmpty()) {
                        ArrayList<AuthorizationCode> arrayList = new ArrayList<>(gson.fromJson(oldJson,
                                new TypeToken<List<AuthorizationCode>>() {}.getType()));
                        arrayList.add(newAuthorizationCode);
                        authorizationCodeArrayList.add(newAuthorizationCode);
                        newJson = gson.toJson(arrayList);
                    } else {
                        ArrayList<AuthorizationCode> arrayList = new ArrayList<>();
                        arrayList.add(newAuthorizationCode);
                        authorizationCodeArrayList.add(newAuthorizationCode);
                        newJson = gson.toJson(arrayList);
                    }
                    BufferedOutputStream jsonBufferedOutputStream =
                            new BufferedOutputStream(new FileOutputStream(jsonFile));
                    jsonBufferedOutputStream.write(newJson.getBytes());
                    jsonBufferedOutputStream.flush();
                    jsonBufferedOutputStream.close();
                    logTextView.append("-- Save new code successful." + "\n");
                } catch (IOException ignored) { }
                table1.updateUI();
            }
        });

        table1.setModel(new TableModel() {
            @Override
            public int getRowCount() {
                return authorizationCodeArrayList.size();
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int i) {
                switch (i) {
                    case 0:
                        return "code";
                    case 1:
                        return "totalNumber";
                    case 2:
                        return "AuthorizedNumber";
                    case 3:
                        return "remarks";
                    case 4:
                        return "data";
                }
                return null;
            }

            @Override
            public Class<?> getColumnClass(int i) {
                return AuthorizationCode.class;
            }

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                switch (i1) {
                    case 0:
                        return authorizationCodeArrayList.get(i).getCode();
                    case 1:
                        return authorizationCodeArrayList.get(i).getTotalNumber();
                    case 2:
                        return authorizationCodeArrayList.get(i).getAuthorizedNumber();
                    case 3:
                        return authorizationCodeArrayList.get(i).getRemarks();
                    case 4:
                        return authorizationCodeArrayList.get(i).getData();
                    case 5:
                        return authorizationCodeArrayList.get(i).getCode();
                }

                return null;
            }

            @Override
            public void setValueAt(Object o, int i, int i1) {
                authorizationCodeArrayList.get(i).setAuthorizedNumber((String) o);
            }

            @Override
            public void addTableModelListener(TableModelListener tableModelListener) {

            }

            @Override
            public void removeTableModelListener(TableModelListener tableModelListener) {

            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(
                                new StringSelection(authorizationCodeArrayList.get(table1.getSelectedRow()).getCode()),
                                null);
                logTextView.append("-- Select code has copied in clipboard." + "\n");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("AuthorizationCodeManagementInterface");
        frame.setContentPane(new AuthorizationCodeManagementInterface().Root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Root = new JPanel();
        Root.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Label");
        Root.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        Root.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        Root.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("AuthorizationCodeManagementResourceBundle").getString("RB_code"));
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        panel2.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("AuthorizationCodeManagementResourceBundle").getString("RB_totalNumber"));
        panel3.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField2 = new JTextField();
        panel3.add(textField2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("AuthorizationCodeManagementResourceBundle").getString("RB_remarks"));
        panel4.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField4 = new JTextField();
        panel4.add(textField4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("AuthorizationCodeManagementResourceBundle").getString("RB_AuthorizedNumber"));
        panel5.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField3 = new JTextField();
        panel5.add(textField3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("AuthorizationCodeManagementResourceBundle").getString("Label"));
        panel1.add(label6, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("Button");
        panel1.add(button1, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        Root.add(scrollPane2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        logTextView = new JTextArea();
        logTextView.setEditable(false);
        logTextView.setRows(5);
        scrollPane2.setViewportView(logTextView);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() { return Root; }
}
