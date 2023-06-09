package dev.acobano.editor.texto.java.vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.*;
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
public class PanelDocumento extends JPanel implements Printable
{
    /*******************/
    /***  ATRIBUTOS  ***/
    /*******************/
    //Componentes del panel:
    private JScrollPane barraDeslizable;
    private JTextPane documento;
    private TextLineNumber tln;
    
    /*********************/
    /***  CONSTRUCTOR  ***/
    /*********************/
    public PanelDocumento(JTextPane doc, UndoManager manager, JLabel etqCursor, JLabel etqDoc, JPopupMenu menuContextual)
    {
        //Setteamos el layout del panel:
        this.setLayout(new BorderLayout());
        
        //Inicializamos el componente TextPane de este panel:        
        this.documento = doc;
        
        //Instanciamos el manager al panel de texto:
        this.documento.getDocument().addUndoableEditListener(manager);
    
        //Ponemos el foco en el documento para mejor UX:
        this.documento.requestFocus();
        
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
            
            etqCursor.setText("    LÍNEA: " + numeroLineaCursor +
                              "  |  COLUMNA: " + numColumnaCursor + 
                              "  |  POSICIÓN: " + (posicionCursor + 1));
            
            String textoEtqDocumento = String.valueOf(contadorLineas);
            
            if (contadorLineas == 1)
                textoEtqDocumento += " línea  |  ";
            else
                textoEtqDocumento += " líneas  |  ";
            
            textoEtqDocumento += String.valueOf(contadorCaracteres);
            
            if (contadorCaracteres == 1)
                textoEtqDocumento += " carácter  |  ";
            else
                textoEtqDocumento += " caracteres  |  ";
            
            textoEtqDocumento += String.valueOf(contadorPalabras);
            
            if (contadorPalabras == 1)
                textoEtqDocumento += " palabra    ";
            else
                textoEtqDocumento += " palabras    ";
            
            etqDoc.setText(textoEtqDocumento);
        });
        
        //Agregamos un MouseListener para que aparezca el JPopupMenu al hacer click derecho:
        this.documento.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            if (SwingUtilities.isRightMouseButton(me))
                if (me.getClickCount() == 1)
                    menuContextual.show(documento, me.getX(), me.getY());
        }});
        
        //Pegamos los componentes en el panel:
        this.add(this.barraDeslizable);
    }
    
    /***************************/
    /***  MÉTODOS 'GETTERS'  ***/
    /***************************/
    
    /**
     * Método 'getter' que devuelve la barra deslizable del panel.
     * @return Componente JScrollPane
     */
    public JScrollPane getBarraDeslizable()
    {
        return this.barraDeslizable;
    }
    
    /**
     * Método 'getter' que devuelve el documento de texto del panel.
     * @return Componente JTextPane.
     */
    public JTextPane getDocumento()
    {
        return this.documento;
    }

    @Override
    public int print(Graphics grphcs, PageFormat pf, int pageIndex) throws PrinterException 
    {
        if (pageIndex != 0)
            return Printable.NO_SUCH_PAGE;
        else
        {
            Graphics2D g = (Graphics2D) grphcs;
            PageFormat formatoA4 = this.getFormatoA4();
            g.translate(formatoA4.getImageableX(), formatoA4.getImageableY());
            printAll(g);
            
            return Printable.PAGE_EXISTS;
        }
    }
    
    /**
     * Método 'getter' que devuelve un formato de página especificado A4.
     * @return Objeto de la clase PageFormat.
     */
    public PageFormat getFormatoA4()
    {
        PageFormat pf = new PageFormat();
        Paper folio = new Paper();
        
        //Establecemos el tamaño del folio en A4 (72 píxeles por pulgada)
        folio.setSize(595, 842);
        
        //Establecemos el área imprimible dentro del folio:
        folio.setImageableArea(36, 36, 523, 770);
        
        //Pegamos el folio al formato y lo devolvemos:
        pf.setPaper(folio);
        return pf;
    }
}
