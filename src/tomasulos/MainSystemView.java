/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomasulos;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javafx.animation.KeyValue;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;



/**
 *
 * @author Hikmat
 */
public class MainSystemView extends javax.swing.JFrame {

    /**
     * Creates new form MainSystemView
     */
    
    private int loadNum = 1;
    private char resnum = 'M';
    private Map<String,String> mapp = new HashMap<String,String>();
    private final int LOADTIME = 2;
    private final int ADDTIME = 2;
    private final int MULTTIME = 10;
    private final int DIVTIME = 40;
    private Vector<Instruction> vec = new Vector<>();
    private Vector<Integer> endtimes = new Vector<>();
    private Vector<Integer> wbtimes = new Vector<>();
    private int pointer = 0;

    private void nextFunction()
    {
        incrementClock();
        CheckCompletion();
        CheckWriting();
        DispenseInstructions();
    }
    
    private void CheckCompletion()
    {
        int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
        int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
        int newvalue = tens*10 + units;
        for (int i = 0; i < endtimes.size(); i++) 
        {    
            if(endtimes.get(i) == newvalue)
                jTable2.setValueAt(newvalue, i, 1);
        }
    }
    
    private void UpdateStatuses(int j)
    {
            int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
            int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
            int newtime = tens*10 + units;
            String oper = jTable3.getValueAt(j, 3).toString();
            int i=0;
            jTable3.setValueAt(newtime, j, 0);
            for (int k = 0; k < jTable1.getRowCount(); k++) 
            {
                if (jTable1.getValueAt(k, 0).equals(oper))
                    i = k;
            }
                    for (int k = 0; k < jTable1.getRowCount(); k++) 
                    {
                        if(jTable1.getValueAt(k, 0).equals(oper))
                        {
                            int timing=0;
                            if(vec.get(i).operation == "LD")
                                timing = LOADTIME;
                            else if (vec.get(i).operation == "ADDD" || vec.get(i).operation == "SUBD")
                                timing = ADDTIME;
                            else if (vec.get(i).operation == "MULTD")
                                timing = MULTTIME;
                            else if (vec.get(i).operation == "DIVD")
                                timing = DIVTIME;
                            endtimes.set(k, newtime+timing);
                            wbtimes.set(k, endtimes.get(k)+1);
                            
                        }
                    }
    }
    
    private void SolveDependents(int i)
    {
        String Station = "";
        if(vec.get(i).operation == "LD")
        {
            //find where it is
            for (int j = 0; j < jTable5.getRowCount(); j++) 
            {
                if (jTable5.getValueAt(j, 1) != null && jTable5.getValueAt(j, 0).toString().contains(mapp.get(vec.get(i).operation)) && jTable5.getValueAt(j, 2).toString().equals(vec.get(i).vec.get(1)))
                {
                    Station = jTable5.getValueAt(j, 0).toString();
                }
            }
           
        }
        else
        {
            for (int j = 0; j < jTable3.getRowCount(); j++) 
            {
                if (jTable3.getValueAt(j, 2) != null)
                {
                    if (jTable3.getValueAt(j, 3).equals(jTable1.getValueAt(i, 0))) {
                        Station = jTable3.getValueAt(j, 1).toString();
                    }
                }
            }
        }
        
        int newval = -1;
        for (int j = 0; j < jTable4.getColumnCount(); j++)
        {
            if(jTable4.getValueAt(0, j)!= null)
            {   
                if(jTable4.getValueAt(0, j).toString().contains(Station))
                newval = j;}
        }
        Complete(i);
        if(newval == -1)
            return;
            
            String newData = jTable4.getValueAt(0, newval).toString();
            for (int j = 0; j < jTable3.getRowCount(); j++) 
            {

                if(jTable3.getValueAt(j, 6) != null)
                if(jTable3.getValueAt(j, 6).equals(Station))
                {
                    jTable3.setValueAt(null, j, 6);
                    jTable3.setValueAt(newData, j, 4);
                    if(jTable3.getValueAt(j, 6) == null && jTable3.getValueAt(j, 7) == null)
                    {
                        UpdateStatuses(j);
                    }
                }
                if(jTable3.getValueAt(j, 7) != null)
                if(jTable3.getValueAt(j, 7).equals(Station))
                {
                    jTable3.setValueAt(null, j, 7);
                    jTable3.setValueAt(newData, j, 5);
                    if(jTable3.getValueAt(j, 6) == null && jTable3.getValueAt(j, 7) == null)
                    {
                        UpdateStatuses(j);
                    }
                }
            }
        }
    
    
    private void CheckWriting()
    {
        int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
        int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
        int newvalue = tens*10 + units;
        for (int i = 0; i < wbtimes.size(); i++) 
        {    
            if(wbtimes.get(i) == newvalue)
            {
                SolveDependents(i);
            }
            
        }
    }
    
    private void Complete (int i)
    {
        int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
        int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
        int newvalue = tens*10 + units;
        String inst = mapp.get(vec.get(i).operation);
        jTable2.setValueAt(newvalue, i, 2);
        if(inst.equals("Load"))
        for (int j = 0; j < jTable5.getRowCount(); j++) 
        {
            if(jTable5.getValueAt(j, 1) != null)
                if (jTable5.getValueAt(j, 0).toString().contains(inst) && jTable5.getValueAt(j, 2).toString().equals(vec.get(i).vec.get(1)))
                {
                    jTable5.setValueAt(null,j,1);
                    jTable5.setValueAt(null,j,2);
                    int k = Integer.parseInt(vec.get(i).vec.get(0).substring(vec.get(i).vec.get(0).indexOf('F')+1, vec.get(i).vec.get(0).indexOf('F')+2));
                    k--;
                    jTable4.setValueAt("M(A"+ loadNum++ +")",0,k);
                }
        }
        else
        {
            inst  = jTable1.getValueAt(i, 0).toString();
            for (int j = 0; j < jTable3.getRowCount(); j++) 
            {
                if(jTable3.getValueAt(j, 3) != null)
                    if (jTable3.getValueAt(j, 3).toString().equals(inst))
                    {
                        jTable3.setValueAt(null, j, 0);
                        jTable3.setValueAt(null, j, 2);
                        jTable3.setValueAt(null, j, 3);
                        jTable3.setValueAt(null, j, 4);
                        jTable3.setValueAt(null, j, 5);
                        jTable3.setValueAt(null, j, 6);
                        int k = Integer.parseInt(vec.get(i).vec.get(0).charAt(vec.get(i).vec.get(0).indexOf('F')+1)+"");
                        k--;
                        jTable4.setValueAt("(M-"+ resnum++ +")",0,k);
                    }
            }
        }
        
    }
    
    private void DispenseInstructions() 
    {
        if(pointer >= vec.size())
            return;
        String instr = vec.get(pointer).operation;
        
        if(instr.equals("LD"))
        {
            for (int i = 0; i < jTable5.getRowCount(); i++) {
                if (jTable5.getValueAt(i, 0).toString().contains("Load") && jTable5.getValueAt(i, 1) == null)
                {
                    jTable5.setValueAt(true, i, 1);
                    jTable5.setValueAt(vec.get(pointer).vec.get(1), i, 2);
                    int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
                    int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
                    int newvalue = tens*10 + units;
                    jTable2.setValueAt(newvalue, pointer, 0);
                    endtimes.set(pointer, newvalue+LOADTIME);
                    wbtimes.set(pointer, endtimes.get(pointer)+1);
                    int k = Integer.parseInt(vec.get(pointer).vec.get(0).substring(vec.get(pointer).vec.get(0).indexOf('F')+1, vec.get(i).vec.get(0).indexOf('F')+2));
                    k--;
                    jTable4.setValueAt(jTable5.getValueAt(i, 0).toString(),0,k);
                    pointer++;
                    return;
                }
            }
        }
        else if (instr.equals("SUBD") || instr.equals("ADDD"))
        {
            for (int i = 0; i < jTable3.getRowCount(); i++) {
                if (jTable3.getValueAt(i, 1).toString().contains("ADD") && jTable3.getValueAt(i, 2) == null)
                {
                    jTable3.setValueAt(true, i, 2);
                    jTable3.setValueAt(jTable1.getValueAt(pointer,0), i, 3);
                    int r2= vec.get(pointer).vec.get(1).indexOf("F");
                    r2= Integer.parseInt(vec.get(pointer).vec.get(1).charAt(r2+1)+"");
                    r2--;
                    if(jTable4.getValueAt(0,r2) != null)
                    {
                        if(jTable4.getValueAt(0,r2).toString().contains("("))
                            jTable3.setValueAt(jTable4.getValueAt(0,r2), i, 4);
                        else
                            jTable3.setValueAt(jTable4.getValueAt(0,r2), i, 6);
                    }
                    else
                    {
                        if(vec.get(pointer).vec.get(1).contains("R"))
                            jTable3.setValueAt("R"+(++r2), i, 4);
                        else
                            jTable3.setValueAt("F"+(++r2), i, 4);
                    }
                    
                    int r3= vec.get(pointer).vec.get(2).indexOf("F");
                    r3= Integer.parseInt(vec.get(pointer).vec.get(2).charAt(r3+1)+"");
                    r3--;
                    if(jTable4.getValueAt(0,r3) != null)
                    {
                        if(jTable4.getValueAt(0,r3).toString().contains("("))
                            jTable3.setValueAt(jTable4.getValueAt(0,r3), i, 5);
                        else
                            jTable3.setValueAt(jTable4.getValueAt(0,r3), i, 7);
                    }
                    
                    else
                    {
                        if(vec.get(pointer).vec.get(2).contains("R"))
                            jTable3.setValueAt("R"+(++r3), i, 5);
                        else
                            jTable3.setValueAt("F"+(++r3), i, 5);
                    }
                    
                    int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
                    int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
                    int newvalue = tens*10 + units;
                    jTable2.setValueAt(newvalue, pointer, 0);
                    if(jTable3.getValueAt(i, 4) != null && jTable3.getValueAt(i, 5) != null)
                    {
                        jTable3.setValueAt(newvalue, i, 0);
                        endtimes.set(pointer, newvalue+ADDTIME);
                        wbtimes.set(pointer, endtimes.get(pointer)+1);
                    }
                    int k= vec.get(pointer).vec.get(0).indexOf("F");
                    k= Integer.parseInt(vec.get(pointer).vec.get(0).charAt(k+1)+"");
                    k--;
                    jTable4.setValueAt(jTable3.getValueAt(i, 1).toString(),0,k);
                    pointer++;
                    return;
                }
            }
        }
        else if (instr.equals("MULTD"))
        {
            for (int i = 0; i < jTable3.getRowCount(); i++) {
                if (jTable3.getValueAt(i, 1).toString().contains("MULT") && jTable3.getValueAt(i, 2) == null)
                {
                    jTable3.setValueAt(true, i, 2);
                    jTable3.setValueAt(jTable1.getValueAt(pointer,0), i, 3);
                    int r2= vec.get(pointer).vec.get(1).indexOf("F");
                    r2= Integer.parseInt(vec.get(pointer).vec.get(1).charAt(r2+1)+"");
                    r2--;
                    if(jTable4.getValueAt(0,r2) != null)
                    {
                        if(jTable4.getValueAt(0,r2).toString().contains("("))
                            jTable3.setValueAt(jTable4.getValueAt(0,r2), i, 4);
                        else
                            jTable3.setValueAt(jTable4.getValueAt(0,r2), i, 6);
                    }
                    else
                    {
                        if(vec.get(pointer).vec.get(1).contains("R"))
                            jTable3.setValueAt("R"+(++r2), i, 4);
                        else
                            jTable3.setValueAt("F"+(++r2), i, 4);
                    }
                    
                    int r3= vec.get(pointer).vec.get(2).indexOf("F");
                    r3= Integer.parseInt(vec.get(pointer).vec.get(2).charAt(r3+1)+"");
                    r3--;
                    if(jTable4.getValueAt(0,r3) != null)
                    {
                        if(jTable4.getValueAt(0,r3).toString().contains("("))
                            jTable3.setValueAt(jTable4.getValueAt(0,r3), i, 5);
                        else
                            jTable3.setValueAt(jTable4.getValueAt(0,r3), i, 7);
                    }
                    
                    else
                    {
                        if(vec.get(pointer).vec.get(2).contains("R"))
                            jTable3.setValueAt("R"+(++r3), i, 5);
                        else
                            jTable3.setValueAt("F"+(++r3), i, 5);
                    }
                    
                    int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
                    int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
                    int newvalue = tens*10 + units;
                    jTable2.setValueAt(newvalue, pointer, 0);
                    if(jTable3.getValueAt(i, 4) != null && jTable3.getValueAt(i, 4) != null)
                    {
                        jTable3.setValueAt(newvalue, i, 0);
                        endtimes.set(pointer, newvalue+MULTTIME);
                        wbtimes.set(pointer, endtimes.get(pointer)+1);
                    }
                    int k= vec.get(pointer).vec.get(0).indexOf("F");
                    k= Integer.parseInt(vec.get(pointer).vec.get(0).charAt(k+1)+"");
                    k--;
                    jTable4.setValueAt(jTable3.getValueAt(i, 1).toString(),0,k);
                    pointer++;
                    return;
                }
            }
        }
        else if (instr.equals("DIVD"))
        {
            for (int i = 0; i < jTable3.getRowCount(); i++) {
                if (jTable3.getValueAt(i, 1).toString().contains("DIV") && jTable3.getValueAt(i, 2) == null)
                {
                    jTable3.setValueAt(true, i, 2);
                    jTable3.setValueAt(jTable1.getValueAt(pointer,0), i, 3);
                    int r2= vec.get(pointer).vec.get(1).indexOf("F");
                    r2= Integer.parseInt(vec.get(pointer).vec.get(1).charAt(r2+1)+"");
                    r2--;
                    if(jTable4.getValueAt(0,r2) != null)
                    {
                        if(jTable4.getValueAt(0,r2).toString().contains("("))
                            jTable3.setValueAt(jTable4.getValueAt(0,r2), i, 4);
                        else
                            jTable3.setValueAt(jTable4.getValueAt(0,r2), i, 6);
                    }
                    else
                    {
                        if(vec.get(pointer).vec.get(1).contains("R"))
                            jTable3.setValueAt("R"+(++r2), i, 4);
                        else
                            jTable3.setValueAt("F"+(++r2), i, 4);
                    }
                    
                    int r3= vec.get(pointer).vec.get(2).indexOf("F");
                    r3= Integer.parseInt(vec.get(pointer).vec.get(2).charAt(r3+1)+"");
                    r3--;
                    if(jTable4.getValueAt(0,r3) != null)
                    {
                        if(jTable4.getValueAt(0,r3).toString().contains("("))
                            jTable3.setValueAt(jTable4.getValueAt(0,r3), i, 5);
                        else
                            jTable3.setValueAt(jTable4.getValueAt(0,r3), i, 7);
                    }
                    
                    else
                    {
                        if(vec.get(pointer).vec.get(2).contains("R"))
                            jTable3.setValueAt("R"+(++r3), i, 5);
                        else
                            jTable3.setValueAt("F"+(++r3), i, 5);
                    }
                    
                    int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
                    int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
                    int newvalue = tens*10 + units;
                    jTable2.setValueAt(newvalue, pointer, 0);
                    if(jTable3.getValueAt(i, 4) != null && jTable3.getValueAt(i, 4) != null)
                    {
                        jTable3.setValueAt(newvalue, i, 0);
                        endtimes.set(pointer, newvalue+DIVTIME);
                        wbtimes.set(pointer, endtimes.get(pointer)+1);
                    }
                    int k= vec.get(pointer).vec.get(0).indexOf("F");
                    k= Integer.parseInt(vec.get(pointer).vec.get(0).charAt(k+1)+"");
                    k--;
                    jTable4.setValueAt(jTable3.getValueAt(i, 1).toString(),0,k);
                    pointer++;
                    return;
                }
            }
        }
        
    }
    
    private void incrementClock()
    {
        int tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
        int units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
        units++;
        int newvalue = tens*10 + units;
        String clockvalue="";
        if (newvalue < 10)
            clockvalue = "0"+newvalue;
        else
            clockvalue = ""+newvalue;
        jLabel3.setText(clockvalue);
        }
    
    public void AddReservStations()
    {
        int rows = jTable3.getRowCount();
        for (int i = 0; i < rows; i++) {
            int Time =0;
            if(jTable3.getValueAt(i, 0) != null)
            Time = Integer.parseInt(jTable3.getValueAt(i, 0).toString());
            String name = jTable3.getValueAt(i, 1)== null ? null : jTable3.getValueAt(i, 1).toString();
            boolean busy =  jTable3.getValueAt(i, 2) == null ? false : true;
            String Operation = jTable3.getValueAt(i, 3)== null ? null : jTable3.getValueAt(i, 3).toString();
            String Vj = jTable3.getValueAt(i, 4)== null ? null : jTable3.getValueAt(i, 4).toString();
            String Vk = jTable3.getValueAt(i, 5)== null ? null : jTable3.getValueAt(i, 5).toString();
            String Qj = jTable3.getValueAt(i, 6)== null ? null : jTable3.getValueAt(i, 6).toString();
            String Qk = jTable3.getValueAt(i, 7)== null ? null : jTable3.getValueAt(i, 7).toString();
        }
        
        
    }
    
    public MainSystemView() {
        initComponents();
        mapp.put("LD","Load");
        mapp.put("SD","Store");
        mapp.put("ADDD", "ADD");
        mapp.put("SUBD", "SUB");
        mapp.put("MULTD","MULT");
        mapp.put("DIVD", "DIV");
        AddReservStations();
    }
    
    
    
    public void addInstruction(String OP,Instruction I)
    {
        vec.add(I);
        endtimes.add(-1);
        wbtimes.add(-1);
        addNewInTable(jTable1, new Object[]{OP});
        addNewInTable(jTable2, new Object[]{null,null,null});
    }
    
    public String getRegName(String Op)
    {
        if(!Op.contains("(")) return Op;
        
        return Op.substring(Op.indexOf('(')+1, Op.indexOf(')')-1);
    }
   
    public void addNewInTable(JTable t1,Object[] a)
    {
        
        DefaultTableModel model = (DefaultTableModel) t1.getModel();
        model.addRow(a);
        t1.setModel(model);
     
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
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 32767));
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TOMASULO's ALGORITHM");
        setAlwaysOnTop(true);
        setResizable(false);

        jTable3.setAutoCreateRowSorter(true);
        jTable3.setBackground(new java.awt.Color(153, 153, 255));
        jTable3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        jTable3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, "ADD1 ", null, null, null, null, null, null},
                {null, "ADD2 ", null, null, null, null, null, null},
                {null, "MULT1", null, null, null, null, null, null},
                {null, "MULT2 ", null, null, null, null, null, null},
                {null, "DIV1 ", null, null, null, null, null, null}
            },
            new String [] {
                "Time", "Name", "Busy", "Operation", "Vj", "Vk", "Qj", "Qk"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setToolTipText("Instruction");
        jTable3.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jTable3.setGridColor(new java.awt.Color(204, 204, 204));
        jTable3.setIntercellSpacing(new java.awt.Dimension(1, 2));
        jTable3.setSelectionBackground(new java.awt.Color(204, 0, 0));
        jTable3.setSelectionForeground(new java.awt.Color(0, 153, 153));
        jTable3.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable3);

        jLabel1.setBackground(new java.awt.Color(0, 0, 153));
        jLabel1.setFont(new java.awt.Font("Tekton Pro Ext", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("RESERVATION STATIONS");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102)));
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(244, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(259, 259, 259))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jLabel2.setBackground(new java.awt.Color(102, 0, 102));
        jLabel2.setFont(new java.awt.Font("Tekton Pro Ext", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(" CLOCK CYCLE");
        jLabel2.setOpaque(true);

        jLabel3.setBackground(new java.awt.Color(204, 153, 255));
        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel3.setText("00");
        jLabel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        jLabel3.setOpaque(true);

        jTable4.setAutoCreateRowSorter(true);
        jTable4.setBackground(new java.awt.Color(153, 255, 153));
        jTable4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        jTable4.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "F1 ", "F2", "F3", "F4", "F5", "F6 ", "F7", "F8", "F9"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setToolTipText("Instruction");
        jTable4.setGridColor(new java.awt.Color(255, 255, 255));
        jTable4.setSelectionBackground(new java.awt.Color(204, 0, 0));
        jTable4.setSelectionForeground(new java.awt.Color(0, 153, 153));
        jTable4.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTable4);

        jLabel4.setBackground(new java.awt.Color(0, 102, 0));
        jLabel4.setFont(new java.awt.Font("Tekton Pro Ext", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("FUNCTIONAL UNIT");
        jLabel4.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(140, 140, 140))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
        );

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setBackground(new java.awt.Color(255, 204, 204));
        jTable2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        jTable2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Issue", "Exec", "WriteBack"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setToolTipText("Instruction");
        jTable2.setGridColor(new java.awt.Color(255, 255, 255));
        jTable2.setSelectionBackground(new java.awt.Color(204, 0, 0));
        jTable2.setSelectionForeground(new java.awt.Color(0, 153, 153));
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(255, 204, 204));
        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        jTable1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Instruction"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("Instruction");
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionBackground(new java.awt.Color(204, 0, 0));
        jTable1.setSelectionForeground(new java.awt.Color(0, 153, 153));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setBackground(new java.awt.Color(204, 0, 0));
        jLabel5.setFont(new java.awt.Font("Tekton Pro Ext", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("INSTRUCTION STATUS");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102)));
        jLabel5.setOpaque(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        jSeparator1.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator1.setOpaque(true);

        jSeparator2.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator2.setOpaque(true);

        jSeparator3.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator3.setOpaque(true);

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Tekton Pro Ext", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("LOAD STATION");
        jLabel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102), new java.awt.Color(0, 0, 102)));
        jLabel6.setOpaque(true);

        jTable5.setAutoCreateRowSorter(true);
        jTable5.setBackground(new java.awt.Color(204, 204, 204));
        jTable5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        jTable5.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {" Load1", null, null},
                {" Load2", null, null},
                {" Load3", null, null}
            },
            new String [] {
                "Name", "Busy", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.setToolTipText("Instruction");
        jTable5.setGridColor(new java.awt.Color(255, 255, 255));
        jTable5.setSelectionBackground(new java.awt.Color(204, 0, 0));
        jTable5.setSelectionForeground(new java.awt.Color(0, 153, 153));
        jTable5.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jLabel6)
                .addContainerGap(83, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 218, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(15, Short.MAX_VALUE)))
        );

        jButton1.setText("QUIT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add Instruction");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Next");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("End");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton5.setText("Jump");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(514, 514, 514)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1))
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(194, 194, 194)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jSeparator1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)))))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int steps = Integer.parseInt(jTextField1.getText());
        int tens;
        int units; 
        int newvalue; 
        do
        {
        nextFunction();
        tens = Integer.parseInt(jLabel3.getText().charAt(0)+"");
        units = Integer.parseInt(jLabel3.getText().charAt(1)+"");
        newvalue = tens*10 + units;
        }while (newvalue != steps);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        boolean flag = true;
        do
        {
        flag = false;
        nextFunction();
            for (int i = 0; i < jTable2.getRowCount(); i++) 
            {
                if(jTable2.getValueAt(i, 2) == null)
                    flag = true;
            }
    
        }while(flag != false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        nextFunction();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new InstructionFeed(this).setVisible(true);
        this.setVisible(false);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        this.dispose();
        return;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

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
            java.util.logging.Logger.getLogger(MainSystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainSystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainSystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainSystemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainSystemView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
