package dev.acobano.editor.texto.java.vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Clase Java Swing extendida de JTabbedPane que permite instanciar pestañas
 * fácilmente cerrables gracias a un botón situado en la parte superior de ésta.
 * @author Álvaro Cobano
 */
public class PestanaCerrable extends JTabbedPane
{    
    //CONSTRUCTOR:
    public PestanaCerrable()
    {
        super();
    }
    
    //MÉTODO SOBREESCRITO:
    @Override
    public void addTab(String titulo, final Component componente)
    {
        super.addTab(titulo, componente);
        int indicePestana = this.indexOfComponent(componente);
        
        JPanel cabeceraPestana = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cabeceraPestana.setOpaque(false);
        this.setTabPlacement(JTabbedPane.TOP);
        
        //Diseñamos la cabecera de la pestaña para que sea cerrable:
        JLabel etqTitulo = new JLabel(titulo);
        JButton btnCerrar = new JButton(new ImageIcon("src/main/resources/icons/close.png"));
        btnCerrar.setFocusable(false);
        btnCerrar.setOpaque(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        
        //Evento de botón:
        btnCerrar.addActionListener((ActionEvent e) -> {
            removeTabAt(indicePestana);
        });
        
        //Inserción de los componentes en el panel:
        cabeceraPestana.add(etqTitulo);
        cabeceraPestana.add(btnCerrar);
        this.setTabComponentAt(indicePestana, cabeceraPestana);
    }
}