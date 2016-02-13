/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomasulos;

import java.util.Vector;

/**
 *
 * @author Hikmat
 */
public class Instruction {
  public String operation;
  public Vector<String> vec = new Vector<String>();

    public Instruction(String operation) {
        this.operation = operation;
    }

    public Instruction() {
        this(" ");
    }
    
    public void addOperand(String op)
    {
        vec.add(op);
    }
    
  @Override
    public String toString()
    {
        String str = operation + " ";
        for (String string : vec) {
            
            str += string + ", ";
        }
       return str.substring(0,str.length()-2);
    }
    
    public void setOperation(String In)
    {
        operation = In;
    }
    
    
}
