package dev.acobano.editor.texto.java.vista;

import dev.acobano.editor.texto.java.controlador.GestorEventosEditor;
import java.awt.*;          //API Java de interfaz básica (Abstract Window Toolkit)
import java.awt.event.*;    //API Java para los eventos de la interfaz.
import javax.swing.*;       //API Java para generar los componentes de la interfaz.
import javax.swing.text.*;  //API Java para algunos eventos de botón del menú.

/**
 * Clase Java que representa el panel Swing donde irán adheridos los diferentes
 * componentes en cuyo conjunto reside la interfaz de usuario del editor de texto.
 * @author Álvaro Cobano
 */
public class PanelEditor extends JPanel
{
    //ATRIBUTOS:   
    //Componentes principales:
    private JMenuBar barraNavegacion;
    private JToolBar menuHerramientas;
    private PanelConPestanasCerrable panelPestanas;
    
    //Componentes secundarios:
    
    //Atributos de uso como proceso:
    private GestorEventosEditor handler;
    
    //CONSTANTES:
    private static final String[] TIPOS_FUENTE = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    private static final Integer[] TAMANOS_FUENTE = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
    
    
    //CONSTRUCTOR:
    public PanelEditor()
    {       
        //Hacemos que el panel se acomode al frame:
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        //Inicializamos el objeto gestor del controlador:
        this.handler = new GestorEventosEditor();
        
        //Instanciamos los componentes integrantes del panel:
        this.inicializarBarraNavegacion();
        this.inicializarMenuHerramientas();
        
        this.panelPestanas = new PanelConPestanasCerrable();
        this.panelPestanas.setVisible(false);
        this.add(panelPestanas);
    }
    
    
    //MÉTODOS PRIVADOS DE INSTANCIACIÓN:
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
        JMenuItem abrirArchivo = new JMenuItem("Abrir documento");
        JMenuItem nuevoArchivo = new JMenuItem("Nuevo documento");
        JMenuItem guardarArchivo = new JMenuItem("Guardar como fichero de texto");
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
        nuevoArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        abrirArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        guardarArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        
        buscar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        deshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        rehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        cortar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        copiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        pegar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        seleccionarTodo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        
        //Instanciamos los eventos de click a los JMenuItems:
        nuevoArchivo.addActionListener((ActionEvent e) -> {
            this.handler.crearNuevoDocumento(this.panelPestanas);
        });
        
        abrirArchivo.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas);
        });
        
        guardarArchivo.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumento(this.panelPestanas);
        });
        
        //Pegamos el menú en el panel:
        this.add(this.barraNavegacion);
    }
        
    private void inicializarMenuHerramientas()
    {
        this.menuHerramientas = new JToolBar(JToolBar.HORIZONTAL);
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
        
        JButton negrita = new JButton(new ImageIcon("src/main/resources/icons/bold.png"));
        JButton cursiva = new JButton(new ImageIcon("src/main/resources/icons/italics.png"));
        JButton subrayado = new JButton(new ImageIcon("src/main/resources/icons/underlined.png"));
        JButton resaltado = new JButton(new ImageIcon("src/main/resources/icons/marker.png"));
        JButton izquierda = new JButton(new ImageIcon("src/main/resources/icons/alignleft.png"));
        JButton centro = new JButton(new ImageIcon("src/main/resources/icons/aligncenter.png"));
        JButton derecha = new JButton(new ImageIcon("src/main/resources/icons/alignright.png"));
        JButton justificado = new JButton(new ImageIcon("src/main/resources/icons/alignjustify.png"));
        JButton insertarImagen = new JButton(new ImageIcon("src/main/resources/icons/addimage.png"));
        
        JButton selectorColor = new JButton(new ImageIcon("src/main/resources/icons/color.png"));
        JComboBox selectorFuente = new JComboBox(TIPOS_FUENTE);
        JSpinner selectorTamano = new JSpinner(new SpinnerListModel(TAMANOS_FUENTE));
        
        //Pegamos estos nuevos componentes en el menú:
        this.menuHerramientas.add(nuevo);
        this.menuHerramientas.add(abrir);
        this.menuHerramientas.add(guardar);
        this.menuHerramientas.add(guardarPDF);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(deshacer);
        this.menuHerramientas.add(rehacer);
        this.menuHerramientas.add(cortar);
        this.menuHerramientas.add(copiar);
        this.menuHerramientas.add(pegar);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(negrita);
        this.menuHerramientas.add(cursiva);
        this.menuHerramientas.add(subrayado);
        this.menuHerramientas.add(resaltado);
        this.menuHerramientas.add(izquierda);
        this.menuHerramientas.add(centro);
        this.menuHerramientas.add(derecha);
        this.menuHerramientas.add(justificado);
        this.menuHerramientas.add(insertarImagen);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(selectorColor);
        this.menuHerramientas.add(selectorFuente);
        this.menuHerramientas.add(selectorTamano);
        
        //Agregamos tooltips para cuando el usuario haga hover sobre los botones:
        nuevo.setToolTipText("Nuevo documento (CTRL + N)");
        abrir.setToolTipText("Abrir documento (CTRL + O)");
        guardar.setToolTipText("Guardar como fichero de texto (CTRL + S)");
        guardarPDF.setToolTipText("Guardar como archivo PDF");        
        deshacer.setToolTipText("Deshacer");
        rehacer.setToolTipText("Rehacer");
        cortar.setToolTipText("Cortar");
        copiar.setToolTipText("Copiar");
        pegar.setToolTipText("Pegar");
        negrita.setToolTipText("Negrita");
        cursiva.setToolTipText("Cursiva");
        subrayado.setToolTipText("Subrayado");
        resaltado.setToolTipText("Resaltado");
        izquierda.setToolTipText("Alinear a la izquierda");
        centro.setToolTipText("Centrar");
        derecha.setToolTipText("Alinear a la derecha");
        justificado.setToolTipText("Justificar");
        insertarImagen.setToolTipText("Insertar nueva imagen");
        selectorColor.setToolTipText("Color de fuente");
        selectorTamano.setToolTipText("Tamaño de fuente");
        selectorFuente.setToolTipText("Tipo de fuente");
        
        //Creamos los respectios eventos de botón:
        nuevo.addActionListener((ActionEvent e) -> {
            this.handler.crearNuevoDocumento(this.panelPestanas);
        });
        
        abrir.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas);
        });
        
        guardar.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumento(this.panelPestanas);
        });
        
        cortar.addActionListener(new StyledEditorKit.CutAction());
        copiar.addActionListener(new StyledEditorKit.CopyAction());
        pegar.addActionListener(new StyledEditorKit.PasteAction());
        
        negrita.addActionListener(new StyledEditorKit.BoldAction());
        cursiva.addActionListener(new StyledEditorKit.ItalicAction());
        subrayado.addActionListener(new StyledEditorKit.UnderlineAction());
        izquierda.addActionListener(new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT));
        centro.addActionListener(new StyledEditorKit.AlignmentAction("Centro", StyleConstants.ALIGN_CENTER));
        derecha.addActionListener(new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT));
        justificado.addActionListener(new StyledEditorKit.AlignmentAction("Justificado", StyleConstants.ALIGN_JUSTIFIED));
        
        //Pegamos el JToolBar en el panel:
        this.add(this.menuHerramientas);
    }
    
    
    //MÉTODOS GETTERS:
    public JMenuBar getBarraNavegacion()
    {
        return this.barraNavegacion;
    }
    
    public JToolBar getMenuHerramientas()
    {
        return this.menuHerramientas;
    }
    
    public PanelConPestanasCerrable getPanelPestanas()
    {
        return this.panelPestanas;
    }
}