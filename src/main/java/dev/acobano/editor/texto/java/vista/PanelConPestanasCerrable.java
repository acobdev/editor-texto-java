package dev.acobano.editor.texto.java.vista;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.undo.UndoManager;

/**
 * Clase Java Swing extendida de JTabbedPane que permite instanciar pestañas
 * fácilmente cerrables gracias a un botón situado en la parte superior de ésta.
 * @author Álvaro Cobano
 */
public class PanelConPestanasCerrable extends JTabbedPane
{  
    //ATRIBUTO:
    private ArrayList<JLabel> listaCabeceras;
    
    
    //CONSTRUCTOR:
    public PanelConPestanasCerrable()
    {
        super();
        this.listaCabeceras = new ArrayList<>();
    }
    
    
    //MÉTODOS PARA INSTANCIAR LA PESTAÑA CERRABLE:
    public void crearPestana(String titulo, 
                             final Component componente, 
                             ArrayList<JTextPane> listaDocumentos, 
                             ArrayList<UndoManager> listaManager)
    {
        super.addTab(titulo, componente);
        int indicePestana = listaDocumentos.size();
        
        JPanel cabeceraPestana = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cabeceraPestana.setOpaque(false);
        this.setTabPlacement(JTabbedPane.TOP);
        
        //Diseñamos la cabecera de la pestaña para que sea cerrable:
        JLabel etqTitulo = new JLabel(titulo);
        this.listaCabeceras.add(etqTitulo);
        JButton btnCerrar = new JButton(new ImageIcon("src/main/resources/icons/close.png"));
        btnCerrar.setFocusable(false);
        btnCerrar.setOpaque(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        
        //Evento de botón:
        btnCerrar.addActionListener((ActionEvent e) -> {
            this.remove(componente);
            PanelDocumento pDoc = (PanelDocumento) componente;
            listaDocumentos.remove(pDoc.getDocumento());
            listaManager.remove(this.getSelectedIndex());
            
            if (listaDocumentos.isEmpty())
                this.setVisible(false);
        });
        
        //Inserción de los componentes en el panel:
        cabeceraPestana.add(etqTitulo);
        cabeceraPestana.add(btnCerrar);
        this.setTabComponentAt(indicePestana, cabeceraPestana);
    }

    public void crearPestana(String titulo, 
                             final Component componente, 
                             ArrayList<JTextPane> listaDocumentos, 
                             ArrayList<File> listaArchivosAbiertos, 
                             File archivo, 
                             ArrayList<UndoManager> listaManager) 
    {
        super.addTab(titulo, componente);
        int indicePestana = listaDocumentos.size();
        
        JPanel cabeceraPestana = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cabeceraPestana.setOpaque(false);
        this.setTabPlacement(JTabbedPane.TOP);
        
        //Diseñamos la cabecera de la pestaña para que sea cerrable:
        JLabel etqTitulo = new JLabel(titulo);
        this.listaCabeceras.add(etqTitulo);
        JButton btnCerrar = new JButton(new ImageIcon("src/main/resources/icons/close.png"));
        btnCerrar.setFocusable(false);
        btnCerrar.setOpaque(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        
        //Evento de botón:
        btnCerrar.addActionListener((ActionEvent e) -> {
            removeTabAt(indicePestana);
            PanelDocumento pDoc = (PanelDocumento) componente;
            listaDocumentos.remove(pDoc.getDocumento());
            listaManager.remove(this.getSelectedIndex());
            listaArchivosAbiertos.remove(archivo);
            
            if (listaDocumentos.isEmpty())
                this.setVisible(false);
        });
        
        //Inserción de los componentes en el panel:
        cabeceraPestana.add(etqTitulo);
        cabeceraPestana.add(btnCerrar);
        
        this.setTabComponentAt(indicePestana, cabeceraPestana);
        
        //if (!listaArchivosAbiertos.contains(f))
    }
    
    //MÉTODO 'GETTER':    
    public JLabel getLabel(int indice)
    {
        return this.listaCabeceras.get(indice);
    }
}