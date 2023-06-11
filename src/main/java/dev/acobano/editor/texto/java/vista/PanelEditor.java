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
        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuEdicion = new JMenu("Edición");
        JMenu herramientas = new JMenu ("Herramientas");
        
        //Se insertan los JMenu en el JMenuBar:
        this.barraNavegacion.add(menuArchivo);
        this.barraNavegacion.add(menuEdicion);
        this.barraNavegacion.add(herramientas);
        
        //Se crean los JMenuItem de cada uno de los JMenu:
        //JMenuItem -> JMenu Archivo:
        JMenuItem abrirArchivo = new JMenuItem("Abrir documento");
        JMenuItem nuevoArchivo = new JMenuItem("Nuevo documento");
        JMenuItem guardarArchivo = new JMenuItem("Guardar como fichero de texto");
        JMenuItem guardarPDF = new JMenuItem("Guardar como archivo PDF");
        
        menuArchivo.add(abrirArchivo);
        menuArchivo.add(nuevoArchivo);
        menuArchivo.add(guardarArchivo);
        menuArchivo.add(guardarPDF);
        
        //JMenuItem -> JMenu Edición:
        JMenuItem buscar = new JMenuItem("Buscar...");
        JMenuItem itemDeshacer = new JMenuItem("Deshacer");
        JMenuItem itemRehacer = new JMenuItem("Rehacer");
        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem pegar = new JMenuItem("Pegar");
        JMenuItem seleccionarTodo = new JMenuItem("Seleccionar todo");
        
        menuEdicion.add(buscar);
        menuEdicion.add(itemDeshacer);
        menuEdicion.add(itemRehacer);
        menuEdicion.add(cortar);
        menuEdicion.add(copiar);
        menuEdicion.add(pegar);
        menuEdicion.add(seleccionarTodo);
        
        //Les damos atajos de teclado (CTRL + tecla):
        nuevoArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        abrirArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        guardarArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        
        buscar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        itemDeshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        itemRehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
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
        
        itemDeshacer.addActionListener((ActionEvent e) -> {
            this.handler.deshacerCambios(this.panelPestanas);
        });
        
        itemRehacer.addActionListener((ActionEvent e) -> {
            this.handler.rehacerCambios(this.panelPestanas);
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
        JButton btnNuevo = new JButton(new ImageIcon("src/main/resources/icons/text.png"));
        JButton btnAbrir = new JButton(new ImageIcon("src/main/resources/icons/file.png"));
        JButton guardar = new JButton(new ImageIcon("src/main/resources/icons/save.png"));
        JButton guardarPDF = new JButton(new ImageIcon("src/main/resources/icons/pdf.png"));
        
        JButton btnDeshacer = new JButton(new ImageIcon("src/main/resources/icons/undo.png"));
        JButton btnRehacer = new JButton(new ImageIcon("src/main/resources/icons/redo.png"));
        JButton btnCortar = new JButton(new ImageIcon("src/main/resources/icons/cut.png"));
        JButton btnCopiar = new JButton(new ImageIcon("src/main/resources/icons/copy.png"));
        JButton btnPegar = new JButton(new ImageIcon("src/main/resources/icons/paste.png"));
        
        JButton btnNegrita = new JButton(new ImageIcon("src/main/resources/icons/bold.png"));
        JButton btnCursiva = new JButton(new ImageIcon("src/main/resources/icons/italics.png"));
        JButton btnSubrayado = new JButton(new ImageIcon("src/main/resources/icons/underlined.png"));
        JButton btnResaltado = new JButton(new ImageIcon("src/main/resources/icons/marker.png"));
        JButton btnIzqda = new JButton(new ImageIcon("src/main/resources/icons/alignleft.png"));
        JButton btnCentro = new JButton(new ImageIcon("src/main/resources/icons/aligncenter.png"));
        JButton btnDerecha = new JButton(new ImageIcon("src/main/resources/icons/alignright.png"));
        JButton btnJustificado = new JButton(new ImageIcon("src/main/resources/icons/alignjustify.png"));
        JButton insertarImagen = new JButton(new ImageIcon("src/main/resources/icons/addimage.png"));
        
        JButton btnSelectorColor = new JButton(new ImageIcon("src/main/resources/icons/color.png"));
        JComboBox selectorFuente = new JComboBox(TIPOS_FUENTE);
        JSpinner selectorTamano = new JSpinner(new SpinnerListModel(TAMANOS_FUENTE));
        
        //Pegamos estos nuevos componentes en el menú:
        this.menuHerramientas.add(btnNuevo);
        this.menuHerramientas.add(btnAbrir);
        this.menuHerramientas.add(guardar);
        this.menuHerramientas.add(guardarPDF);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnDeshacer);
        this.menuHerramientas.add(btnRehacer);
        this.menuHerramientas.add(btnCortar);
        this.menuHerramientas.add(btnCopiar);
        this.menuHerramientas.add(btnPegar);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnNegrita);
        this.menuHerramientas.add(btnCursiva);
        this.menuHerramientas.add(btnSubrayado);
        this.menuHerramientas.add(btnResaltado);
        this.menuHerramientas.add(btnIzqda);
        this.menuHerramientas.add(btnCentro);
        this.menuHerramientas.add(btnDerecha);
        this.menuHerramientas.add(btnJustificado);
        this.menuHerramientas.add(insertarImagen);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnSelectorColor);
        this.menuHerramientas.add(selectorFuente);
        this.menuHerramientas.add(selectorTamano);
        
        //Agregamos tooltips para cuando el usuario haga hover sobre los botones:
        btnNuevo.setToolTipText("Nuevo documento (CTRL + N)");
        btnAbrir.setToolTipText("Abrir documento (CTRL + O)");
        guardar.setToolTipText("Guardar como fichero de texto (CTRL + S)");
        guardarPDF.setToolTipText("Guardar como archivo PDF");        
        btnDeshacer.setToolTipText("Deshacer (CTRL + Z)");
        btnRehacer.setToolTipText("Rehacer (CTRL + Y)");
        btnCortar.setToolTipText("Cortar");
        btnCopiar.setToolTipText("Copiar");
        btnPegar.setToolTipText("Pegar");
        btnNegrita.setToolTipText("Negrita");
        btnCursiva.setToolTipText("Cursiva");
        btnSubrayado.setToolTipText("Subrayado");
        btnResaltado.setToolTipText("Resaltado");
        btnIzqda.setToolTipText("Alinear a la izquierda");
        btnCentro.setToolTipText("Centrar");
        btnDerecha.setToolTipText("Alinear a la derecha");
        btnJustificado.setToolTipText("Justificar");
        insertarImagen.setToolTipText("Insertar nueva imagen");
        btnSelectorColor.setToolTipText("Color de fuente");
        selectorTamano.setToolTipText("Tamaño de fuente");
        selectorFuente.setToolTipText("Tipo de fuente");
        
        //Creamos los respectios eventos de botón:
        btnNuevo.addActionListener((ActionEvent e) -> {
            this.handler.crearNuevoDocumento(this.panelPestanas);
        });
        
        btnAbrir.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas);
        });
        
        guardar.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumento(this.panelPestanas);
        });
        
        btnDeshacer.addActionListener((ActionEvent e) -> {
            this.handler.deshacerCambios(this.panelPestanas);
        });
        
        btnRehacer.addActionListener((ActionEvent e) -> {
            this.handler.rehacerCambios(this.panelPestanas);
        });
        
        btnSelectorColor.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorTexto(this.panelPestanas);
        });
        
        btnResaltado.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorResaltado(this.panelPestanas);
        });
        
        btnCortar.addActionListener(new StyledEditorKit.CutAction());
        btnCopiar.addActionListener(new StyledEditorKit.CopyAction());
        btnPegar.addActionListener(new StyledEditorKit.PasteAction());
        
        btnNegrita.addActionListener(new StyledEditorKit.BoldAction());
        btnCursiva.addActionListener(new StyledEditorKit.ItalicAction());
        btnSubrayado.addActionListener(new StyledEditorKit.UnderlineAction());
        btnIzqda.addActionListener(new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT));
        btnCentro.addActionListener(new StyledEditorKit.AlignmentAction("Centro", StyleConstants.ALIGN_CENTER));
        btnDerecha.addActionListener(new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT));
        btnJustificado.addActionListener(new StyledEditorKit.AlignmentAction("Justificado", StyleConstants.ALIGN_JUSTIFIED));
        
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