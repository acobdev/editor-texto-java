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
    private ArrayList<JLabel> listaDatosCursor;
    private ArrayList<JLabel> listaDatosDocumento;
    
    
    //CONSTRUCTOR:
    public PanelConPestanasCerrable()
    {
        super();
        this.listaCabeceras = new ArrayList<>();
        this.listaDatosCursor = new ArrayList<>();
        this.listaDatosDocumento = new ArrayList<>();
    }
    
    
    //MÉTODOS PARA INSTANCIAR LA PESTAÑA CERRABLE:
    public void crearPestana(String titulo, 
                             final Component componente, 
                             ArrayList<JTextPane> listaDocumentos, 
                             ArrayList<UndoManager> listaManager,
                             JPanel piePagina)
    {
        super.addTab(titulo, componente);
        int indicePestana = listaDocumentos.size();
        
        JPanel cabeceraPestana = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cabeceraPestana.setOpaque(false);
        this.setTabPlacement(JTabbedPane.TOP);
        piePagina.setVisible(true);
        
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
            this.listaCabeceras.remove(this.getSelectedIndex());
            listaManager.remove(this.getSelectedIndex());            
            PanelDocumento pDocBorrar = (PanelDocumento) componente;
            listaDocumentos.remove(pDocBorrar.getDocumento());
            this.remove(componente);
            
            if (listaDocumentos.isEmpty())
            {                
                this.setVisible(false);
                piePagina.setVisible(false);
            }
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
                             ArrayList<UndoManager> listaManager,
                             JPanel piePagina) 
    {
        super.addTab(titulo, componente);
        int indicePestana = listaDocumentos.size();
        
        JPanel cabeceraPestana = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cabeceraPestana.setOpaque(false);
        this.setTabPlacement(JTabbedPane.TOP);
        piePagina.setVisible(true);
        
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
            listaManager.remove(this.getSelectedIndex());
            listaArchivosAbiertos.remove(archivo);
            PanelDocumento pDoc = (PanelDocumento) componente;
            listaDocumentos.remove(pDoc.getDocumento());
            this.remove(componente);
            
            if (listaDocumentos.isEmpty())
            {
                this.setVisible(false);
                piePagina.setVisible(false);
            }
        });
        
        //Inserción de los componentes en el panel:
        cabeceraPestana.add(etqTitulo);
        cabeceraPestana.add(btnCerrar);
        
        this.setTabComponentAt(indicePestana, cabeceraPestana);
    }
    
    //MÉTODO 'GETTER':    
    public JLabel getLabelCabecera(int indice)
    {
        return this.listaCabeceras.get(indice);
    }
}
