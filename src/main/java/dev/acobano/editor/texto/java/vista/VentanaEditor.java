package dev.acobano.editor.texto.java.vista;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Clase Java engargada de instanciar la ventana o frame Swing donde 
 * se desplegará el editor de texto en la pantalla.
 * @author Álvaro Cobano
 */
public class VentanaEditor extends JFrame
{
    //CONSTRUCTOR:
    public VentanaEditor()
    {
        //Visibilidad de la ventana:
        this.setVisible(true);
        
        //Título de la ventana:
        this.setTitle("CobText");
        
        //La ventana ocupará la pantalla completa:
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //La ventana se cerrará al presionar el botón 'X':
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        //Inicializamos el panel que será contenido en la ventana:
        PanelEditor panel = new PanelEditor();
        this.setContentPane(panel);
    }
}