/*
 * Copyright (c) 2015. All work is owned by Sam Collins and is not to be redistributed without his permission.
 * He is also not liable for any damage done and is not required to maintain any work.
 */

package org.cyberworks.builder.gui;

import org.cyberworks.builder.xml.ModFile;
import org.cyberworks.builder.xml.Mods;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by scollins on 07/12/15.
 */
public class Window {

    private JTextField modDir;
    private JButton selectDirButton;
    private JButton startHashButton;
    private JProgressBar hashProgress;
    private JPanel modSelect;

    public Window() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, "Look and feel setting failed", ex);
        }

        startHashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!getModDir().isEmpty()) {
                    try {
                        JAXBContext jc = JAXBContext.newInstance(Mods.class);
                        Mods mods = new Mods();
                        mods.setModFiles(new ArrayList<>());

                        try (Stream<Path> paths = Files.walk(Paths.get(getModDir()))) {

                            paths.forEach(filePath -> {
                                try {
                                    File mod = new File(filePath.toString());
                                    if (mod.isFile() && mod.exists()) {
                                        ModFile curFile = new ModFile();
                                        curFile.setName(mod.getName());
                                        curFile.setFolder(filePath.toString());
                                        curFile.setHash(calcSHA1(mod));
                                        mods.getModFiles().add(curFile);
                                    }
                                } catch (IOException | NoSuchAlgorithmException ex) {
                                    Logger.getLogger("builder").log(Level.SEVERE, null, ex);
                                }
                            });

                        } catch (IOException ex) {
                            Logger.getLogger("builder").log(Level.SEVERE, null, ex);
                        }

                        Marshaller marshaller = jc.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                        marshaller.marshal(mods, System.out);
                    } catch (Exception ex) {
                        Logger.getLogger("builder").log(Level.SEVERE, null, ex);
                    }
                } else {
                    Logger.getLogger("builder").log(Level.SEVERE, "No file is set");
                }
            }
        });
        selectDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Documents"));
                chooser.setDialogTitle("Select Mods Directory");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    System.out.print(chooser.getSelectedFile().getAbsolutePath());
                    setModDir(chooser.getSelectedFile().getAbsolutePath());
                } else {
                    System.out.print("Nope");
                    //utils.showErrMsg(this, "Please select a mod directory");
                }
            }
        });
        modDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print(modDir.getText());
                setModDir("asddas");
            }
        });
    }

    public void display(String jFameName) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(jFameName);
        frame.setContentPane(new Window().modSelect);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.toFront();
    }


    public void setModDir(String dir) {
        modDir.setText(dir);
    }

    public String getModDir() {
        return modDir.getText();
    }

    /**
     * Read the file and calculate the SHA-1 checksum
     *
     * @param file
     *            the file to read
     * @return the hex representation of the SHA-1 using uppercase chars
     * @throws FileNotFoundException
     *             if the file does not exist, is a directory rather than a
     *             regular file, or for some other reason cannot be opened for
     *             reading
     * @throws IOException
     *             if an I/O error occurs
     * @throws NoSuchAlgorithmException
     *             should never happen
     */
    private static String calcSHA1(File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (InputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }
            return new HexBinaryAdapter().marshal(sha1.digest());
        }
    }

}
