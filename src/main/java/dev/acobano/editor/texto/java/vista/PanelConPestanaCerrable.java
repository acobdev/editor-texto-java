package dev.acobano.editor.texto.java.vista;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Clase Java Swing extendida de JTabbedPane que permite instanciar pestañas
 * fácilmente cerrables gracias a un botón situado en la parte superior de ésta.
 * @author Álvaro Cobano
 */
public class PanelConPestanaCerrable extends JTabbedPane
{   
    //CONSTRUCTOR:
    public PanelConPestanaCerrable()
    {
        super();
    }
    
    //MÉTODO PARA INSTANCIAR LA PESTAÑA CERRABLE:
    public void crearPestana(String titulo, final Component componente, ArrayList<JTextPane> listaDocumentos)
    {
        super.addTab(titulo, componente);
        int indicePestana = listaDocumentos.size() + 1;
        
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
            PanelDocumento doc = (PanelDocumento) componente;
            listaDocumentos.remove(doc.getDocumento());
            
            if (listaDocumentos.isEmpty())
                this.setVisible(false);
        });
        
        //Inserción de los componentes en el panel:
        cabeceraPestana.add(etqTitulo);
        cabeceraPestana.add(btnCerrar);
        this.setTabComponentAt(indicePestana, cabeceraPestana);
    }

    void crearPestana(String titulo, final Component componente, ArrayList<JTextPane> listaDocumentos, ArrayList<File> listaArchivosAbiertos, File f) 
    {
        super.addTab(titulo, componente);
        int indicePestana = listaDocumentos.size();
        
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
            PanelDocumento doc = (PanelDocumento) componente;
            listaDocumentos.remove(doc.getDocumento());
            listaArchivosAbiertos.remove(f);
            
            if (listaDocumentos.isEmpty())
                this.setVisible(false);
        });
        
        //Inserción de los componentes en el panel:
        cabeceraPestana.add(etqTitulo);
        cabeceraPestana.add(btnCerrar);
        this.setTabComponentAt(indicePestana, cabeceraPestana);
    }
}