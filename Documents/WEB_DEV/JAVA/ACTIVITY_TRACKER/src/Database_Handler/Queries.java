/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database_Handler;

import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author faith
 */
public class Queries {
    private Connection conn=ConnectDB.DatabaseConn();
    private PreparedStatement stmt=null;
    private ResultSet rs=null;

    
    //INSERT ACTIVITY
    public void InsertActivity(JTextField ActivityName, JDateChooser ActivityDate, JTextField ActivityVenue, JTextField ActivityHr, JTextField ActivityMin, JCheckBox AM, JCheckBox PM, JRadioButton Star){
        
        try {
            String sql="Insert into "+A_Table+" ("+Act+", "+date+", "+venue+", "+time+", "+starred+") values (?,?,?,?,?)";
            setStmt(getConn().prepareStatement(sql));
            getStmt().setString(1, ActivityName.getText());
            getStmt().setString(2, ((JTextComponent)ActivityDate.getDateEditor().getUiComponent()).getText());
            getStmt().setString(3, ActivityVenue.getText());
            
            //Getting the time
            String AM_PM;
            if(AM.isSelected()){
                AM_PM="AM";
            }else if(PM.isSelected()){
                    AM_PM="PM";
            } else{AM_PM="";}
            
            String Time= ActivityHr.getText()+":"+ActivityMin.getText()+" "+AM_PM;
            getStmt().setString(4, Time);
            
            //Getting the Star Selected or Otherwise
            if(Star.isSelected()){
                getStmt().setString(5, "*");
            }
            
            getStmt().executeUpdate();
            getStmt().close();
            getConn().close();
            JOptionPane.showMessageDialog(null, "Saved");
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void UpdateActivity(JTextField ActivityName, JDateChooser ActivityDate, JTextField ActivityVenue, JTextField ActivityHr, JTextField ActivityMin, JCheckBox AM, JCheckBox PM, JRadioButton Star, JLabel IdLbl){
        
        try {
            String sql="Update "+A_Table+" set "+Act+"=? , "+date+"=? , "+venue+"=? , "+time+"=? , "+starred+"=? where "+id+"='"+IdLbl.getText()+"' ";
            setStmt(getConn().prepareStatement(sql));
            getStmt().setString(1, ActivityName.getText());
            getStmt().setString(2, ((JTextComponent)ActivityDate.getDateEditor().getUiComponent()).getText());
            getStmt().setString(3, ActivityVenue.getText());
            
            
            //Getting the time
            String AM_PM;
            if(AM.isSelected()){
                AM_PM="AM";
            }else if(PM.isSelected()){
                    AM_PM="PM";
            } else{AM_PM="";}
            
            String Time= ActivityHr.getText()+":"+ActivityMin.getText()+" "+AM_PM;
            getStmt().setString(4, Time);
            
            //Getting the Star Selected or Otherwise
            if(Star.isSelected()){
                getStmt().setString(5, "*");
            }
            
            getStmt().executeUpdate();
            getStmt().close();
            getConn().close();
            JOptionPane.showMessageDialog(null, "Updated");
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void GetAllActivities(JTable AllActivity_Table){
        try {
            String sql="Select "+id+", "+Act+" as 'ACTIVITY NAME', "+date+", "+venue+" as 'VENUE', "+time+" from "+A_Table+" ";
            setStmt(getConn().prepareStatement(sql));
            setRs(getStmt().executeQuery());
            AllActivity_Table.setModel(DbUtils.resultSetToTableModel(getRs()));
            
            getStmt().close();
            getRs().close();
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void GetStarredActivities(JTable Starred_Activities){
        try {
            String sql="Select "+id+", "+Act+" as 'ACTIVITY NAME', "+date+", "+venue+" as 'VENUE', "+time+" from "+A_Table+" where "+starred+"='*' ";
            setStmt(getConn().prepareStatement(sql));
            setRs(getStmt().executeQuery());
            Starred_Activities.setModel(DbUtils.resultSetToTableModel(getRs()));
            
            getStmt().close();
            getRs().close();
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void DeleteActivity(JTable AllActivity_Table){
        try {
            int row=AllActivity_Table.getSelectedRow();
            String Table_click = (AllActivity_Table.getModel().getValueAt(row, 0).toString());
            
            String del="Delete from "+A_Table+" where "+id+"='"+Table_click+"' ";
            setStmt(getConn().prepareStatement(del));
            getStmt().executeUpdate();
            
            getStmt().close();
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void CheckifStarred( JTable AllActivity_Table, JButton starActivityBtn){
        try {
            int row=AllActivity_Table.getSelectedRow();
            String Table_click = (AllActivity_Table.getModel().getValueAt(row, 0).toString());
            
            String del="Select "+starred+" from "+A_Table+" where "+id+"='"+Table_click+"' and "+starred+"='*' ";
            setStmt(getConn().prepareStatement(del));
            setRs(getStmt().executeQuery());
            if(getRs().next()){
                starActivityBtn.setEnabled(false);
            }else{starActivityBtn.setEnabled(true);}
            
            
            getStmt().close();
            getRs().close();
        } catch (Exception ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateStarred(JTable AllActivity_Table){
        try {
            int row=AllActivity_Table.getSelectedRow();
            String Table_click = (AllActivity_Table.getModel().getValueAt(row, 0).toString());
            
            String star="Update "+A_Table+" set "+starred+"='*' where "+id+"='"+Table_click+"' ";
            setStmt(getConn().prepareStatement(star));
            getStmt().executeUpdate();
            JOptionPane.showMessageDialog(null, "Added Starred");
           
            getStmt().close();
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void removeStarred(JTable Starred_Activities){
        try {
            int row=Starred_Activities.getSelectedRow();
            String Table_click = (Starred_Activities.getModel().getValueAt(row, 0).toString());
            
            String star="Update "+A_Table+" set "+starred+"="+null+" where "+id+"='"+Table_click+"' ";
            setStmt(getConn().prepareStatement(star));
            getStmt().executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Removed from starred");
           
            getStmt().close();
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void searchActivity(JTextField searchField, JTable myTable){
         try {
            String sql="Select "+id+", "+Act+" as 'ACTIVITY NAME', "+date+", "+venue+" as 'VENUE', "+time+" from "+A_Table+" where "+id+" like '%"+searchField.getText()+"%'  or "+Act+" like '%"+searchField.getText()+"%' or "+date+" like '%"+searchField.getText()+"%' or "+venue+" like '%"+searchField.getText()+"%' or "+time+" like '%"+searchField.getText()+"%' ";
            setStmt(getConn().prepareStatement(sql));
            setRs(getStmt().executeQuery());
            myTable.setModel(DbUtils.resultSetToTableModel(getRs()));
            
            getRs().close();
            getStmt().close();
        } catch (Exception ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    public void searchStarredActivity(JTextField searchField, JTable myTable){
         try {
            String sql="Select "+id+", "+Act+" as 'ACTIVITY NAME', "+date+", "+venue+" as 'VENUE', "+time+" from "+A_Table+" where "+starred+"='*'  and ( "+id+" like '%"+searchField.getText()+"%'  or "+Act+" like '%"+searchField.getText()+"%' or "+date+" like '%"+searchField.getText()+"%' or "+venue+" like '%"+searchField.getText()+"%' or "+time+" like '%"+searchField.getText()+"%') ";
            setStmt(getConn().prepareStatement(sql));
            setRs(getStmt().executeQuery());
            myTable.setModel(DbUtils.resultSetToTableModel(getRs()));
            
            getRs().close();
            getStmt().close();
        } catch (Exception ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
//ALL DATABASE VARIABLES
    private final String A_Table="ALL_ACTIVITIES";
    
     private final String id="ID";
    private final String Act="ACTIVITY";
    private final String date="DATE";
    private final String venue="VENUE_PLACE";
    private final String time="TIME";
    private final String starred="STARRED";
    
    
    
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public PreparedStatement getStmt() {
        return stmt;
    }

    public void setStmt(PreparedStatement stmt) {
        this.stmt = stmt;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }
    
}