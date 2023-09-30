/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author User
 */
public class NewJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 204, 0));
        jPanel1.setForeground(new java.awt.Color(204, 204, 255));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(jButton1)
                .addContainerGap(163, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jButton1)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:z
             // Specify the folder path where you want to save the PDF file
        String folderPath = "C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\pdf\\pdf\\";

        // Ensure that the folder exists; create it if it doesn't
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Specify the output PDF file path
        String outputPath = folderPath + "header_example.pdf";

       
try {
    Document document = new Document(PageSize.LETTER, 36, 36, 36, 36); // 1-inch margins (72 points per inch)

    PdfWriter.getInstance(document, new FileOutputStream(outputPath));
    document.open();

    // Create a table with 3 columns (as requested)
    PdfPTable table = new PdfPTable(3);
  table.setWidthPercentage(100); // Make the table width 100% of the page
    float[] columnWidths = {18f, 68f, 16f}; // Specify the column widths as percentages
    table.setWidths(columnWidths);

    // Column 1: Left-aligned Image (1x1)
    PdfPCell cell1 = new PdfPCell();
    cell1.setHorizontalAlignment(Element.ALIGN_RIGHT); // Align content to the left
    Image leftImage = Image.getInstance(getClass().getResource("/icons/compesaLogo2.png"));
    leftImage.scaleAbsolute(72, 72);
    cell1.addElement(leftImage);
    cell1.setBorderWidth(0);
    table.addCell(cell1);

    // Column 2: Centered Text (3 lines)
  PdfPCell cell2 = new PdfPCell();

// Create a font for Arial, size 16, and bold
Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

// Create a font for Arial, size 12
Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

// Create a Paragraph and set the alignment to center
Paragraph paragraph = new Paragraph();
paragraph.setAlignment(Element.ALIGN_CENTER);

// Add the title (Computer Engineering Students Association) with the bold font
Chunk titleChunk = new Chunk("Computer Engineering Students Association", titleFont);
paragraph.add(titleChunk);

// Add the rest of the text with the regular font and line breaks
paragraph.add(new Chunk("\nUNIVERSITY OF BOHOL\n", regularFont));
paragraph.add(new Chunk("www.universityofbohol.edu.ph (038)-411-3484, Fax No. (038) 411-3101\n", regularFont));
paragraph.add(new Chunk("COLLEGE OF ENGINEERING AND TECHNOLOGY", regularFont));

// Add the Paragraph to the PdfPCell
cell2.addElement(paragraph);

// Set the border width to 0
cell2.setBorderWidth(0);

// Add the cell to the table
table.addCell(cell2);


    // Column 3: Right-aligned Image (2x2)
    PdfPCell cell3 = new PdfPCell();
    cell3.setHorizontalAlignment(Element.ALIGN_LEFT); // Align content to the right
    Image rightImage = Image.getInstance(getClass().getResource("/icons/cetLogo1.png"));
    rightImage.scaleAbsolute(72, 72); // Adjust size as needed
    cell3.addElement(rightImage);
    cell3.setBorderWidth(0);
    table.addCell(cell3);

    // Add the table to the document as a header
    document.add(table);

    // Rest of your document content goes here

    document.close();
} catch (DocumentException | IOException e) {
    e.printStackTrace();
}

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
