/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gotkcups.pdfkit;

import com.borland.dx.sql.dataset.ConnectionDescriptor;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.ProcedureDescriptor;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.cwd.db.ColumnFactory;
import com.cwd.db.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rfteves
 */
public class FBAUsage {
    private String shipto;
    private String shipfrom;
    private int numberOfPages;
    private long fileSize;
    private String recordType;
    private String fbaLabel;
    private boolean valid;

    private FBAUsage(String labelText) {
        try {
            Pattern p = Pattern.compile("^FBA[A-Z0-9]{9,15}$");
            Matcher m = null;
            BufferedReader reader = new BufferedReader(new StringReader(labelText.toUpperCase()));
            String line = null;
            int phase = 0;
            int index = -1;
            while ((line = reader.readLine()) != null) {
                if (phase == 1) {
                    phase++;
                    index = line.indexOf("FBA:");
                    if (index == -1) {
                        this.shipfrom = line;
                        this.shipto = line;
                    } else {
                        this.shipfrom = line.substring(0, index).trim();
                        this.shipto = line.substring(index + 4).trim();
                    }
                }
                if (line.startsWith("SHIP FROM:")) {
                    phase++;
                } else if (line.startsWith("FBA")) {
                    m = p.matcher(line);
                    if (m.matches()) {
                        this.fbaLabel = line;
                        phase++;
                    }
                } else if (line.startsWith("PLEASE LEAVE THIS LABEL UNCOVERED")) {
                        phase++;
                }
            }
            this.valid = phase == 4;
        } catch (IOException ex) {
            Logger.getLogger(FBAUsage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the shipto
     */
    public String getShipto() {
        return shipto;
    }

    /**
     * @return the numberOfPages
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @return the recordType
     */
    public String getRecordType() {
        return recordType;
    }

    public boolean hasEnoughCredits() {
        boolean value = true;
        return value;
    }

    public void log(BigDecimal filesize) throws SQLException {
        if (valid == false) return;
        Data data = getData();
        data.getTarget().open();
        data.getTarget().insertRow(false);
        data.getTarget().setString("shipto", shipto);
        data.getTarget().setString("shipfrom", shipfrom);
        data.getTarget().setString("fbalabel", fbaLabel);
        data.getTarget().setString("recordtype", "Upload");
        data.getTarget().setBigDecimal("filesize", filesize);
        data.getTarget().saveChanges();
    }

    private Data getData() throws SQLException {
        Data data = Data.getData();
        data.getParameters().addColumn(ColumnFactory.createStringColumn("shipto"));
        data.getSourcedb().setConnection(new ConnectionDescriptor("jdbc:mysql://teves.us/amazonkeurig",
                "rfteves", "Wrx654xrw", false, "com.mysql.jdbc.Driver"));
        data.getTarget().setQuery(new QueryDescriptor(data.getSourcedb(),
                "SELECT * FROM fbalabels where shipto=:shipto",
                data.getParameters(), true, Load.ALL));
        data.getProcedureDataSet().setProcedure(new ProcedureDescriptor(data.getSourcedb(),
                "call fbausage(?)",
                data.getParameters(), true, Load.ALL));
        return data;
    }

    public static FBAUsage getInstance(String labelText) {
        return new FBAUsage(labelText);
    }

    /**
     * @return the shipfrom
     */
    public String getShipfrom() {
        return shipfrom;
    }

    /**
     * @return the fbaLabel
     */
    public String getFbaLabel() {
        return fbaLabel;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }
}
