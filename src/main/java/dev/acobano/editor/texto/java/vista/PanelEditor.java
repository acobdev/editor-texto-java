package dev.acobano.editor.texto.java.vista;

import java.awt.*;          //API Java de interfaz básica (Abstract Window Toolkit)
import java.awt.event.*;    //API Java para los eventos de la interfaz.
import javax.swing.*;       //API Java para generar los componentes de la interfaz.

/**
 * Clase Java que representa el panel Swing donde irán adheridos los diferentes
 * componentes en cuyo conjunto reside la interfaz de usuario del editor de texto.
 * @author Álvaro Cobano
 */
public class PanelEditor extends JPanel
{
    //ATRIBUTOS:   
    private JMenuBar barraNavegacion;
    private JToolBar menuHerramientas;
    
    //CONSTRUCTOR:
    public PanelEditor()
    {       
        //Hacemos que el panel se acomode al frame:
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        //Le ponemos un layout para que coja toda la extensión del panel:
        this.setLayout(new GridLayout(3, 0));
        
        //Instanciamos los componentes integrantes del panel:
        this.inicializarBarraNavegacion();
        this.inicializarMenuHerramientas();
    }
    
    //MÉTODOS DE INSTANCIACIÓN:
    private void inicializarBarraNavegacion()
    {
        //Inicializamos la barra de herramientas:
        this.barraNavegacion = new JMenuBar();
        
        //Se crean los JMenu que estarán dentro del JMenuBar:
        JMenu archivo = new JMenu("Archivo");
        JMenu edicion = new JMenu("Edición");
        JMenu herramientas = new JMenu ("Herramientas");
        
        //Se insertan los JMenu en el JMenuBar:
        this.barraNavegacion.add(archivo);
        this.barraNavegacion.add(edicion);
        this.barraNavegacion.add(herramientas);
        
        //Se crean los JMenuItem de cada uno de los JMenu:
        //JMenuItem -> JMenu Archivo:
        JMenuItem abrirArchivo = new JMenuItem("Abrir...");
        JMenuItem nuevoArchivo = new JMenuItem("Nuevo");
        JMenuItem guardarArchivo = new JMenuItem("Guardar...");
        JMenuItem guardarPDF = new JMenuItem("Guardar como archivo PDF");
        
        archivo.add(abrirArchivo);
        archivo.add(nuevoArchivo);
        archivo.add(guardarArchivo);
        archivo.add(guardarPDF);
        
        //JMenuItem -> JMenu Edición:
        JMenuItem buscar = new JMenuItem("Buscar...");
        JMenuItem deshacer = new JMenuItem("Deshacer");
        JMenuItem rehacer = new JMenuItem("Rehacer");
        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem pegar = new JMenuItem("Pegar");
        JMenuItem seleccionarTodo = new JMenuItem("Seleccionar todo");
        
        edicion.add(buscar);
        edicion.add(deshacer);
        edicion.add(rehacer);
        edicion.add(cortar);
        edicion.add(copiar);
        edicion.add(pegar);
        edicion.add(seleccionarTodo);
        
        //Les damos atajos de teclado (CTRL + tecla):
        abrirArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        nuevoArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        guardarArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        
        buscar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        deshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        rehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        cortar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        copiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        pegar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        seleccionarTodo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        
        
        this.add(this.barraNavegacion);
    }
    
    
    private void inicializarMenuHerramientas()
    {
        this.menuHerramientas = new JToolBar();
        this.menuHerramientas.setFloatable(false);
        this.menuHerramientas.setVisible(true);
        
        //Instanciamos los botones del menú:
        JButton nuevo = new JButton(new ImageIcon("src/main/resources/icons/text.png"));
        JButton abrir = new JButton(new ImageIcon("src/main/resources/icons/file.png"));
        JButton guardar = new JButton(new ImageIcon("src/main/resources/icons/save.png"));
        JButton guardarPDF = new JButton(new ImageIcon("src/main/resources/icons/pdf.png"));
        
        JButton deshacer = new JButton(new ImageIcon("src/main/resources/icons/undo.png"));
        JButton rehacer = new JButton(new ImageIcon("src/main/resources/icons/redo.png"));
        JButton cortar = new JButton(new ImageIcon("src/main/resources/icons/cut.png"));
        JButton copiar = new JButton(new ImageIcon("src/main/resources/icons/copy.png"));
        JButton pegar = new JButton(new ImageIcon("src/main/resources/icons/paste.png"));
        
        //Pegamos estos nuevos componentes en el menú:
        this.menuHerramientas.add(nuevo);
        this.menuHerramientas.add(abrir);
        this.menuHerramientas.add(guardar);
        this.menuHerramientas.add(guardarPDF);
        this.menuHerramientas.add(new JSeparator());
        this.menuHerramientas.add(deshacer);
        this.menuHerramientas.add(rehacer);
        this.menuHerramientas.add(cortar);
        this.menuHerramientas.add(copiar);
        this.menuHerramientas.add(pegar);
        this.menuHerramientas.add(new JSeparator());
        
        this.add(this.menuHerramientas);
    }
}