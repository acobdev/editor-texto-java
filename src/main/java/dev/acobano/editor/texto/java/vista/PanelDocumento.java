package dev.acobano.editor.texto.java.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.undo.UndoManager;

/**
 * Clase Java Swing que instancia un panel donde irá el documento de texto a
 * escribir dentro de su correspondiente pestaña.
 * @author Álvaro Cobano
 */
public class PanelDocumento extends JPanel
{
    //ATRIBUTOS:
    private JScrollPane barraDeslizable;
    private JTextPane documento;
    
    //CONSTRUCTOR:
    public PanelDocumento(JTextPane doc, UndoManager manager)
    {
        //Setteamos el layout del panel:
        this.setLayout(new BorderLayout());
        
        //Inicializamos el componente TextPane de este panel:        
        this.documento = doc;
        this.documento.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 24, 
                                                      Toolkit.getDefaultToolkit().getScreenSize().height - 305));
        
        //Inicializamos el componente Scroll de este panel:
        this.barraDeslizable = new JScrollPane(documento, 
                                               JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);      
        this.barraDeslizable.setBounds(0, 0, 100, 300);
        
        //Instanciamos el manager al panel de texto:
        this.documento.getDocument().addUndoableEditListener(manager);
        
        //Pegamos los componentes en el panel:
        this.add(this.barraDeslizable);
    }
    
    //MÉTODOS 'GETTERS':
    public JScrollPane getBarraDeslizable()
    {
        return this.barraDeslizable;
    }
    
    public JTextPane getDocumento()
    {
        return this.documento;
    }
}
