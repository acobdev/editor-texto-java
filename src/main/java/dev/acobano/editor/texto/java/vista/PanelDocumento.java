package dev.acobano.editor.texto.java.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;

/**
 * Clase Java Swing que instancia un panel donde irá el documento de texto a
 * escribir dentro de su correspondiente pestaña.
 * @author Álvaro Cobano
 */
public class PanelDocumento extends JPanel
{
    //ATRIBUTOS:
    //Componentes del panel:
    private JScrollPane barraDeslizable;
    private JTextPane documento;
    private TextLineNumber tln;
    
    //CONSTRUCTOR:
    public PanelDocumento(JTextPane doc, UndoManager manager, JLabel etqCursor, JLabel etqDoc)
    {
        //Setteamos el layout del panel:
        this.setLayout(new BorderLayout());
        
        //Inicializamos el componente TextPane de este panel:        
        this.documento = doc;
        
        //Instanciamos el manager al panel de texto:
        this.documento.getDocument().addUndoableEditListener(manager);
    
        //Ponemos el foco en el documento para mejor UX:
        this.documento.requestFocusInWindow();
        
        //Inicializamos el componente Scroll de este panel:
        this.barraDeslizable = new JScrollPane(documento, 
                                               JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        //Inicializamos el TextLineNumber para que muestre los números de línea:
        this.tln = new TextLineNumber(this.documento);
        this.barraDeslizable.setRowHeaderView(this.tln);
        
        //Agregamos un CaretListener al JTextPane para escuchar los cambios de posición del cursor:
        this.documento.addCaretListener((CaretEvent ce) -> {
            //Obtenemos la posición del cursor:
            Document d = this.documento.getDocument();
            int posicionCursor = ce.getDot();
            
            //Obtenemos la línea donde se encuentra el cursor:
            Element root = d.getDefaultRootElement();
            int numeroLineaCursor = root.getElementIndex(posicionCursor) + 1;
            
            //Obtenemos la columna donde se encuentra el cursor:
            Element linea = root.getElement(numeroLineaCursor - 1);
            int numColumnaCursor = posicionCursor - linea.getStartOffset() + 1;
            
            //Obtenemos la cantidad de caracteres y de líneas:
            int contadorCaracteres = d.getLength();
            int contadorLineas = root.getElementCount();
            int contadorPalabras = this.documento.getText().split("\\s+").length;
            
            etqCursor.setText("   Línea: " + numeroLineaCursor +
                              "  -  Columna: " + numColumnaCursor + 
                              "  -  Posición: " + posicionCursor);
            
            etqDoc.setText(contadorLineas + " línea(s)  -  " +
                           contadorCaracteres + " caracteres  -  " +
                           contadorPalabras + " palabras   ");
        });
        
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
