package dev.acobano.editor.texto.java.vista;

import dev.acobano.editor.texto.java.controlador.*;
import java.awt.*;          //API Java de interfaz básica (Abstract Window Toolkit)
import java.awt.event.*;    //API Java para los eventos de la interfaz.
import javax.swing.*;       //API Java para generar los componentes de la interfaz.
import javax.swing.event.*; //API Java para el evento 'ChangeListener' del JSpinner.
import javax.swing.text.*;  //API Java para algunos eventos de botón del menú.

/**
 * Clase Java que representa el panel Swing donde irán adheridos los diferentes
 * componentes en cuyo conjunto reside la interfaz de usuario del editor de texto.
 * @author Álvaro Cobano
 */
public class PanelEditor extends JPanel
{
    /*******************/
    /***  ATRIBUTOS  ***/
    /*******************/
    
    //Componentes principales:
    private JMenuBar barraNavegacion;
    private JToolBar menuHerramientas;
    private PanelConPestanasCerrable panelPestanas;
    private JPanel piePagina;
    private JPopupMenu menuContextual;
    
    //Componentes secundarios:
    private JLabel etqCursor, etqDocumento;
    private JComboBox selectorFuente;
    private JSpinner selectorTamano;
    
    //Atributos de uso como proceso:
    private GestorEventosEditor handler;
    
    
    /********************/
    /***  CONSTANTES  ***/
    /********************/ 
    private static final String[] TIPOS_FUENTE = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    private static final Integer[] TAMANOS_FUENTE = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
    
    
    /**********************/
    /***  CONSTRUCTOR   ***/
    /**********************/
    public PanelEditor()
    {       
        //Hacemos que el panel se acomode al frame:
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        
        //Inicializamos el objeto gestor del controlador:
        this.handler = new GestorEventosEditor();
        
        //Instanciamos los componentes integrantes del panel:
        this.inicializarMenuContextual();   
        this.inicializarMenuHerramientas();
        this.inicializarBarraNavegacion();       
        this.inicializarPiePagina();
        this.inicializarPanelPestanas();
        this.inicializarLayoutPanel();
        this.inicializarAtajosTeclado();
    }
    
    
    /********************************************/
    /***  MÉTODOS PRIVADOS DE INICIALIZACIÓN  ***/
    /********************************************/
    
    /**
     * Método privado que inicializa todos los detalles referentes a la
     * barra de navegación superior del editor de texto.
     */
    private void inicializarBarraNavegacion()
    {
        //Inicializamos la barra de menuInsertar:
        this.barraNavegacion = new JMenuBar();
        
        //Se crean los JMenu que estarán dentro del JMenuBar:
        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuEdicion = new JMenu("Edición");
        JMenu menuFormato = new JMenu("Formato");
        JMenu menuInsertar = new JMenu ("Insertar");
        
        //Se insertan los JMenu en el JMenuBar:
        this.barraNavegacion.add(menuArchivo);
        this.barraNavegacion.add(menuEdicion);
        this.barraNavegacion.add(menuFormato);
        this.barraNavegacion.add(menuInsertar);
        
        //Se crean los JMenuItem de cada uno de los JMenu:
        //JMenuItem -> JMenu Archivo:
        JMenuItem itemAbrir = new JMenuItem("Abrir documento");
        JMenu menuNuevo = new JMenu("Nuevo documento...");
        JMenuItem itemNuevoBlanco = new JMenuItem("Nuevo documento en blanco");
        JMenuItem itemNuevoHTML = new JMenuItem("Nuevo documento HTML");
        JMenuItem itemGuardar = new JMenuItem("Guardar documento...");
        JMenu menuGuardarComo = new JMenu("Guardar como...");
        JMenuItem itemGuardarTXT = new JMenuItem("Guardar como fichero de texto plano TXT");
        JMenuItem itemGuardarRTF = new JMenuItem("Guardar como fichero con formato RTF");
        JMenuItem itemGuardarPDF = new JMenuItem("Guardar como fichero con formato PDF");
        JMenuItem itemGuardarDOC = new JMenuItem("Guardar como fichero Microsoft Word");
        JMenuItem itemGuardarODT = new JMenuItem("Guardar como fichero OpenOffice Writer");
        JMenuItem itemImprimir = new JMenuItem("Imprimir documento");
        JMenuItem itemEliminarTodos = new JMenuItem("Cerrar todos los documentos");
        JMenuItem itemSalir = new JMenuItem("Salir");
        
        menuArchivo.add(itemAbrir);
        menuNuevo.add(itemNuevoBlanco);
        menuNuevo.add(itemNuevoHTML);
        menuArchivo.add(menuNuevo);
        menuArchivo.add(itemGuardar);
        menuGuardarComo.add(itemGuardarTXT);
        menuGuardarComo.add(itemGuardarRTF);
        menuGuardarComo.add(itemGuardarPDF);
        menuGuardarComo.add(itemGuardarDOC);
        menuGuardarComo.add(itemGuardarODT);
        menuArchivo.add(menuGuardarComo);
        menuArchivo.add(itemImprimir);
        menuArchivo.add(new JSeparator(JSeparator.HORIZONTAL));
        menuArchivo.add(itemEliminarTodos);
        menuArchivo.add(itemSalir);
        
        //JMenuItem -> JMenu Edición:
        JMenuItem itemDeshacer = new JMenuItem("Deshacer");
        JMenuItem itemRehacer = new JMenuItem("Rehacer");
        JMenuItem itemCortar = new JMenuItem("Cortar");
        JMenuItem itemCopiar = new JMenuItem("Copiar");
        JMenuItem itemPegar = new JMenuItem("Pegar");
        JMenuItem itemSeleccionTodo = new JMenuItem("Seleccionar todo");
        JMenuItem itemBuscar = new JMenuItem("Buscar...");
        
        menuEdicion.add(itemDeshacer);
        menuEdicion.add(itemRehacer);
        menuEdicion.add(new JSeparator(JSeparator.HORIZONTAL));
        menuEdicion.add(itemCortar);
        menuEdicion.add(itemCopiar);
        menuEdicion.add(itemPegar);
        menuEdicion.add(itemSeleccionTodo);
        menuEdicion.add(new JSeparator(JSeparator.HORIZONTAL));
        menuEdicion.add(itemBuscar);
        
        //JMenuItem - JMenu 'Formato':
        JMenuItem itemNegrita = new JMenuItem("Negrita");
        JMenuItem itemCursiva = new JMenuItem("Cursiva");
        JMenuItem itemSubrayado = new JMenuItem("Subrayado");
        JMenuItem itemTachado = new JMenuItem("Tachado");
        JMenuItem itemSubindice = new JMenuItem("Subíndice");
        JMenuItem itemSuperindice = new JMenuItem("Superíndice");
        
        menuFormato.add(itemNegrita);
        menuFormato.add(itemCursiva);
        menuFormato.add(itemSubrayado);
        menuFormato.add(itemTachado);
        menuFormato.add(new JSeparator(JSeparator.HORIZONTAL));
        menuFormato.add(itemSubindice);
        menuFormato.add(itemSuperindice);
        
        //JMenuItem - JMenu 'Insertar':
        JMenuItem itemImagen = new JMenuItem("Insertar imagen");
        JMenuItem itemTabla = new JMenuItem("Insertar tabla");
        JMenuItem itemURL = new JMenuItem("Insertar hiperenlace");
        
        menuInsertar.add(itemImagen);
        menuInsertar.add(itemTabla);
        menuInsertar.add(itemURL);
        
        //Instanciamos los eventos de click a los JMenuItems:
        itemNuevoBlanco.addActionListener((ActionEvent e) -> {
            this.handler.crearNuevoDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento, this.menuContextual);
            this.handler.cambiarFuente(this.getIndicePestana(), this.getFuenteSeleccionada());
            this.handler.cambiarTamanoFuente(this.getIndicePestana(), this.getTamanoFuenteSeleccionado());
        });
        
        itemNuevoHTML.addActionListener((ActionEvent e) -> {
            this.handler.crearDocumentoHTML(PanelEditor.this.panelPestanas, PanelEditor.this.piePagina, PanelEditor.this.etqCursor, PanelEditor.this.etqDocumento, PanelEditor.this.menuContextual);
            this.handler.cambiarFuente(this.getIndicePestana(), this.getFuenteSeleccionada());
            this.handler.cambiarTamanoFuente(this.getIndicePestana(), this.getTamanoFuenteSeleccionado());
        });
        
        itemAbrir.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento, this.menuContextual);
        });
        
        itemGuardar.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumento(this.panelPestanas);
        });
        
        itemGuardarTXT.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.TXT);
        });
        
        itemGuardarRTF.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.RTF);
        });
        
        itemGuardarPDF.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.PDF);
        });
        
        itemGuardarDOC.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.DOCX);
        });
        
        itemGuardarODT.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.ODT);
        });
        
        itemImprimir.addActionListener((ActionEvent e) -> {
            if (!this.handler.getListaDocumentos().isEmpty())
                this.handler.imprimirDocumento(this.panelPestanas.getPanelDocumentoActivo());
        });
        
        itemEliminarTodos.addActionListener((ActionEvent e) -> {
            this.panelPestanas.removeAll();
            this.etqCursor.setText("");
            this.etqDocumento.setText("");
            this.handler.cerrarTodosDocumentos();
        });
        
        itemSalir.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        
        itemDeshacer.addActionListener((ActionEvent e) -> {
            this.handler.deshacerCambios(this.getIndicePestana());
        });
        
        itemRehacer.addActionListener((ActionEvent e) -> {
            this.handler.rehacerCambios(this.getIndicePestana());
        });
        
        itemSeleccionTodo.addActionListener((ActionEvent e) -> {
            this.handler.seleccionarTexto(this.getIndicePestana());
        });
        
        itemBuscar.addActionListener((ActionEvent e) -> {
            this.handler.buscarEnTexto(this.getIndicePestana());
        });
        
        itemNegrita.addActionListener(new StyledEditorKit.BoldAction());
        itemCursiva.addActionListener(new StyledEditorKit.ItalicAction());
        itemSubrayado.addActionListener(new StyledEditorKit.UnderlineAction());
        
        itemTachado.addActionListener((ActionEvent e) -> {
            this.handler.tacharTexto(this.getIndicePestana());
        });
        
        itemSubindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSubindice(this.getIndicePestana());
        });
        
        itemSuperindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSuperindice(this.getIndicePestana());
        });
        
        itemImagen.addActionListener((ActionEvent e) -> {
            this.handler.insertarImagen(this.getIndicePestana());
        });
        
        itemTabla.addActionListener((ActionEvent e) -> {
            this.handler.insertarTabla(this.getIndicePestana(), 
                                       this.getFuenteSeleccionada(), 
                                       this.getTamanoFuenteSeleccionado());
        });
        
        itemURL.addActionListener((ActionEvent e) -> {
            this.handler.insertarHiperenlace(this.getIndicePestana());
        });
        
        itemCortar.addActionListener((ActionEvent e) -> {
            this.handler.cortarTexto(this.getIndicePestana());
        });
        
        itemCopiar.addActionListener((ActionEvent e) -> {
            this.handler.copiarTexto(this.getIndicePestana());
        });
        
        itemPegar.addActionListener((ActionEvent e) -> {
            this.handler.pegarTexto(this.getIndicePestana());
        });
        
        //Añadimos Accelerators a los items para indicar al usuario su atajo de teclado:
        itemNuevoBlanco.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        itemAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        itemGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        itemImprimir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        itemDeshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        itemRehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        itemCortar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        itemCopiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        itemPegar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        itemSeleccionTodo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        itemBuscar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        itemNegrita.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
        itemCursiva.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
        itemSubrayado.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
        itemTachado.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK));
        itemSubindice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
        itemSuperindice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));
        itemImagen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
        itemTabla.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        itemURL.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
    }
        
    /**
     * Método privado que inicializa todos los detalles referentes al
     * menú de botones con las herramientas del editor de texto.
     */
    private void inicializarMenuHerramientas()
    {
        //Inicializamos el JToolBar:
        this.menuHerramientas = new JToolBar(JToolBar.HORIZONTAL);
        this.menuHerramientas.setFloatable(false);
        this.menuHerramientas.setVisible(true);
        
        //Instanciamos los botones del panel de menú:
        JButton btnNuevo = new JButton(new ImageIcon("src/main/resources/icons/new.png"));
        JButton btnAbrir = new JButton(new ImageIcon("src/main/resources/icons/open.png"));
        JButton btnGuardar = new JButton(new ImageIcon("src/main/resources/icons/save.png"));
        JButton btnGuardarComo = new JButton(new ImageIcon("src/main/resources/icons/saveas.png"));   
        JButton btnGuardarTXT = new JButton(new ImageIcon("src/main/resources/icons/saveastext.png"));
        JButton btnGuardarPDF = new JButton(new ImageIcon("src/main/resources/icons/saveaspdf.png"));
        JButton btnCortar = new JButton(new ImageIcon("src/main/resources/icons/cut.png"));
        JButton btnCopiar = new JButton(new ImageIcon("src/main/resources/icons/copy.png"));
        JButton btnPegar = new JButton(new ImageIcon("src/main/resources/icons/paste.png"));
        JButton btnDeshacer = new JButton(new ImageIcon("src/main/resources/icons/undo.png"));
        JButton btnRehacer = new JButton(new ImageIcon("src/main/resources/icons/redo.png"));
        JButton btnImagen = new JButton(new ImageIcon("src/main/resources/icons/addimage.png"));
        JButton btnTabla = new JButton(new ImageIcon("src/main/resources/icons/addtable.png"));
        JButton btnURL = new JButton(new ImageIcon("src/main/resources/icons/hyperlink.png"));
        JButton btnBuscar = new JButton(new ImageIcon("src/main/resources/icons/search.png"));
        JButton btnImprimir = new JButton(new ImageIcon("src/main/resources/icons/print.png"));
        JButton btnNegrita = new JButton(new ImageIcon("src/main/resources/icons/bold.png"));
        JButton btnCursiva = new JButton(new ImageIcon("src/main/resources/icons/italics.png"));
        JButton btnSubrayado = new JButton(new ImageIcon("src/main/resources/icons/underlined.png"));
        JButton btnTachado = new JButton(new ImageIcon("src/main/resources/icons/strikethrough.png"));
        JButton btnSubindice = new JButton(new ImageIcon("src/main/resources/icons/subscript.png"));
        JButton btnSuperindice = new JButton(new ImageIcon("src/main/resources/icons/superscript.png"));
        JButton btnIzqda = new JButton(new ImageIcon("src/main/resources/icons/alignleft.png"));
        JButton btnCentro = new JButton(new ImageIcon("src/main/resources/icons/aligncenter.png"));
        JButton btnDerecha = new JButton(new ImageIcon("src/main/resources/icons/alignright.png"));
        JButton btnJustificado = new JButton(new ImageIcon("src/main/resources/icons/alignjustify.png"));
        JButton btnColor = new JButton(new ImageIcon("src/main/resources/icons/brush.png"));        
        JButton btnResaltado = new JButton(new ImageIcon("src/main/resources/icons/marker.png"));
        
        this.selectorFuente = new JComboBox(TIPOS_FUENTE);
        this.selectorFuente.setPreferredSize(new Dimension(160, 48));
        SpinnerListModel modelo = new SpinnerListModel(TAMANOS_FUENTE);
        modelo.setValue(14);
        this.selectorTamano = new JSpinner(modelo);
        this.selectorTamano.setPreferredSize(new Dimension(30, 48));
        
        //Pegamos estos nuevos componentes en el menú:
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnNuevo);
        this.menuHerramientas.add(btnAbrir);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnGuardar);
        this.menuHerramientas.add(btnGuardarComo);
        this.menuHerramientas.add(btnGuardarTXT);
        this.menuHerramientas.add(btnGuardarPDF);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnCortar);
        this.menuHerramientas.add(btnCopiar);
        this.menuHerramientas.add(btnPegar);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnDeshacer);
        this.menuHerramientas.add(btnRehacer);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnNegrita);
        this.menuHerramientas.add(btnCursiva);
        this.menuHerramientas.add(btnSubrayado);
        this.menuHerramientas.add(btnTachado);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnSubindice);
        this.menuHerramientas.add(btnSuperindice);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnIzqda);
        this.menuHerramientas.add(btnCentro);
        this.menuHerramientas.add(btnDerecha);
        this.menuHerramientas.add(btnJustificado);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnColor);
        this.menuHerramientas.add(btnResaltado);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnBuscar);
        this.menuHerramientas.add(btnImprimir);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(selectorFuente);
        this.menuHerramientas.add(selectorTamano);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        
        //Agregamos tooltips para cuando el usuario haga hover sobre los botones:
        btnNuevo.setToolTipText("Nuevo documento en blanco (CTRL + N)");
        btnAbrir.setToolTipText("Abrir documento (CTRL + O)");
        btnGuardar.setToolTipText("Guardar documento (CTRL + S)");
        btnGuardarComo.setToolTipText("Guardar como...");
        btnGuardarTXT.setToolTipText("Guardar como documento de texto TXT");
        btnGuardarPDF.setToolTipText("Guardar como archivo PDF");
        btnCortar.setToolTipText("Cortar (CTRL + X)");
        btnCopiar.setToolTipText("Copiar (CTRL + C)");
        btnPegar.setToolTipText("Pegar (CTRL + V)");
        btnDeshacer.setToolTipText("Deshacer (CTRL + Z)");
        btnRehacer.setToolTipText("Rehacer (CTRL + Y)");
        btnImagen.setToolTipText("Insertar imagen (CTRL + M)");
        btnTabla.setToolTipText("Insertar tabla (CTRL + T)");
        btnURL.setToolTipText("Insertar hiperenlace (CTRL + H)");
        btnBuscar.setToolTipText("Buscar en texto (CTRL + F)");
        btnImprimir.setToolTipText("Mandar documento a la impresora (CTRL + P)");
        btnNegrita.setToolTipText("Negrita (CTRL + B)");
        btnCursiva.setToolTipText("Cursiva (CTRL + I)");
        btnSubrayado.setToolTipText("Subrayado (CTRL + U)");
        btnTachado.setToolTipText("Tachado (CTRL + K)");
        btnSubindice.setToolTipText("Subíndice (CTRL + Menos)");
        btnSuperindice.setToolTipText("Superíndice (CTRL + Más)");
        btnIzqda.setToolTipText("Alinear a la izquierda (CTRL + L)");
        btnCentro.setToolTipText("Centrar (CTRL + Q)");
        btnDerecha.setToolTipText("Alinear a la derecha (CTRL + R)");
        btnJustificado.setToolTipText("Justificar (CTRL + J)");
        btnColor.setToolTipText("Color de fuente");
        btnResaltado.setToolTipText("Color de fondo de carácter (CTRL + G)");
        this.selectorTamano.setToolTipText("Tamaño de fuente");
        this.selectorFuente.setToolTipText("Tipo de fuente");
        
        //Creamos los respectios eventos de botón:
        btnNuevo.addActionListener((ActionEvent e) -> {
            this.handler.crearNuevoDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento, this.menuContextual);
        });
        
        btnAbrir.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento, this.menuContextual);
        });
        
        btnGuardar.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumento(this.panelPestanas);
        });
        
        btnGuardarComo.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.INDEFINIDO);
        });
        
        btnGuardarTXT.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.TXT);
        });
        
        btnGuardarPDF.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, ExtensionesFormatosSoportados.PDF);
        });
        
        btnCortar.addActionListener((ActionEvent e) -> {
            this.handler.cortarTexto(this.getIndicePestana());
        });
        
        btnCopiar.addActionListener((ActionEvent e) -> {
            this.handler.copiarTexto(this.getIndicePestana());
        });
        
        btnPegar.addActionListener((ActionEvent e) -> {
            this.handler.pegarTexto(this.getIndicePestana());
        });
        
        btnDeshacer.addActionListener((ActionEvent e) -> {
            this.handler.deshacerCambios(this.getIndicePestana());
        });
        
        btnRehacer.addActionListener((ActionEvent e) -> {
            this.handler.rehacerCambios(this.getIndicePestana());
        });
        
        btnImagen.addActionListener((ActionEvent e) -> {
            this.handler.insertarImagen(this.getIndicePestana());
        });
        
        btnTabla.addActionListener((ActionEvent e) -> {
            this.handler.insertarTabla(this.getIndicePestana(), 
                                       this.getFuenteSeleccionada(), 
                                       this.getTamanoFuenteSeleccionado());
        });
        
        btnURL.addActionListener((ActionEvent e) -> {
            this.handler.insertarHiperenlace(this.getIndicePestana());
        });
        
        btnBuscar.addActionListener((ActionEvent e) -> {
            this.handler.buscarEnTexto(this.getIndicePestana());
        });
        
        btnImprimir.addActionListener((ActionEvent e) -> {
            if (!this.handler.getListaDocumentos().isEmpty())
                this.handler.imprimirDocumento(this.panelPestanas.getPanelDocumentoActivo());
        });
        
        btnNegrita.addActionListener(new StyledEditorKit.BoldAction());        
        btnCursiva.addActionListener(new StyledEditorKit.ItalicAction());        
        btnSubrayado.addActionListener(new StyledEditorKit.UnderlineAction());
        
        btnTachado.addActionListener((ActionEvent e) -> {
            this.handler.tacharTexto(this.getIndicePestana());
        });
                
        btnSubindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSubindice(this.getIndicePestana());
        });
        
        btnSuperindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSuperindice(this.getIndicePestana());
        });
        
        btnIzqda.addActionListener(new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT));        
        btnCentro.addActionListener(new StyledEditorKit.AlignmentAction("Centrado", StyleConstants.ALIGN_CENTER));       
        btnDerecha.addActionListener(new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT));        
        btnJustificado.addActionListener(new StyledEditorKit.AlignmentAction("Justificado", StyleConstants.ALIGN_JUSTIFIED));
        
        btnColor.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorTexto(this.getIndicePestana());
        });
        
        btnResaltado.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorResaltado(this.getIndicePestana());
        });
        
        selectorFuente.addItemListener((ItemEvent ie) -> {
            if (!this.handler.getListaDocumentos().isEmpty())
                this.handler.cambiarFuente(this.getIndicePestana(), getFuenteSeleccionada());
        });
        
        selectorTamano.addChangeListener((ChangeEvent ce) -> {
            if (!this.handler.getListaDocumentos().isEmpty())
                this.handler.cambiarTamanoFuente(this.getIndicePestana(), getTamanoFuenteSeleccionado());
        });
    }
    
    /**
     * Método privado que inicializa todos los detalles referentes al
     * pie de página situado en la parte inferior del editor de texto.
     */
    private void inicializarPiePagina()
    {
        //Inicializamos los componentes:
        this.piePagina = new JPanel();
        this.etqCursor = new JLabel();
        this.etqDocumento = new JLabel();
        
        //Pegamos los componentes secundarios en el pie de página:
        this.piePagina.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 30));
        this.piePagina.setLayout(new BorderLayout());
        this.piePagina.add(etqCursor, BorderLayout.WEST);
        this.piePagina.add(etqDocumento, BorderLayout.EAST);
        this.piePagina.setVisible(false);
    }
    
    /**
     * Método privado que inicializa todos los detalles referentes al
     * panel central del editor de texto donde se encuentran las pestañas
     * con los documentos.
     */
    private void inicializarPanelPestanas()
    {
        int alturaPanel = Toolkit.getDefaultToolkit().getScreenSize().height -
                          this.barraNavegacion.getHeight() -
                          this.menuHerramientas.getHeight() -
                          this.piePagina.getHeight();
        int anchuraPanel = Toolkit.getDefaultToolkit().getScreenSize().width;
        this.panelPestanas = new PanelConPestanasCerrable();
        this.panelPestanas.setVisible(false);
        this.panelPestanas.setSize(anchuraPanel, alturaPanel);
    }
    
    /**
     * Método privado que inicializa el layout del editor de texto.
     */
    private void inicializarLayoutPanel()
    {
        //Creamos un nuevo objeto para definir el layout:
        this.setLayout(new BorderLayout());
        
        //Pegamos los componentes definidos en los anteriores métodos en el panel:
        this.add(this.menuHerramientas, BorderLayout.NORTH);
        this.add(this.panelPestanas, BorderLayout.CENTER);
        this.add(this.piePagina, BorderLayout.SOUTH);
    }
    
    /**
     * Método privado que inicializa todos los detalles referentes al
     * menú contextual del editor de texto al hacer click derecho en cualquier
     * documento abierto.
     */
    private void inicializarMenuContextual()
    {
        //Inicializamos el componente JPopupMenu:
        this.menuContextual = new JPopupMenu();
        
        //Instanciamos los JMenu que estarán dentro del JPopupMenu:
        //JMenuItem - > JMenu 'Tipo de letra':
        JMenu menuTipoLetra = new JMenu("Tipo de fuente...");        
        for (String fuente : TIPOS_FUENTE)
        {
            JMenuItem itemFuente = new JMenuItem(fuente);
            itemFuente.addActionListener((ActionEvent e) -> {
                this.handler.cambiarFuente(this.getIndicePestana(), fuente);
            });
            menuTipoLetra.add(itemFuente);
        }
        
        //JMenuItem -> JMenu 'Tamaño de fuente':
        JMenu menuTamano = new JMenu("Tamaño de fuente...");
        for (Integer tamano : TAMANOS_FUENTE)
        {
            JMenuItem itemTamano = new JMenuItem(String.valueOf(tamano));
            itemTamano.addActionListener((ActionEvent e) -> {
                this.handler.cambiarTamanoFuente(this.getIndicePestana(), tamano);
            });
            menuTamano.add(itemTamano);
        }
        
        //JMenuItem -> JMenu 'Estilos':
        JMenu menuEstilo = new JMenu("Estilo...");
        JMenuItem itemNegrita = new JMenuItem("Negrita");
        JMenuItem itemCursiva = new JMenuItem("Cursiva");
        JMenuItem itemSubrayado = new JMenuItem("Subrayado");
        JMenuItem itemTachado = new JMenuItem("Tachado");
        JMenuItem itemSubindice = new JMenuItem("Subíndice");
        JMenuItem itemSuperindice = new JMenuItem("Superíndice");
        
        itemNegrita.addActionListener(new StyledEditorKit.BoldAction());        
        itemCursiva.addActionListener(new StyledEditorKit.ItalicAction());        
        itemSubrayado.addActionListener(new StyledEditorKit.UnderlineAction());
        
        itemTachado.addActionListener((ActionEvent e) -> {
                this.handler.tacharTexto(this.getIndicePestana());
            });
        
        itemSubindice.addActionListener((ActionEvent e) -> {
                this.handler.escribirSubindice(this.getIndicePestana());
            });
        
        itemSuperindice.addActionListener((ActionEvent e) -> {
                this.handler.escribirSuperindice(this.getIndicePestana());
            });
        
        menuEstilo.add(itemNegrita);
        menuEstilo.add(itemCursiva);
        menuEstilo.add(itemSubrayado);
        menuEstilo.add(itemTachado);
        menuEstilo.add(new JSeparator(JSeparator.HORIZONTAL));
        menuEstilo.add(itemSubindice);
        menuEstilo.add(itemSuperindice);
        
        //JMenuItem -> JMenu 'Alineación':
        JMenu menuAlineacion = new JMenu("Alineación...");
        JMenuItem itemIzqda = new JMenuItem("Izquierda");
        JMenuItem itemDcha = new JMenuItem("Derecha");
        JMenuItem itemCentro = new JMenuItem("Centrado");
        JMenuItem itemJustificado = new JMenuItem("Justificado");
        
        itemIzqda.addActionListener(new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT));        
        itemCentro.addActionListener(new StyledEditorKit.AlignmentAction("Centrado", StyleConstants.ALIGN_CENTER));        
        itemDcha.addActionListener(new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT));        
        itemJustificado.addActionListener(new StyledEditorKit.AlignmentAction("Justificado", StyleConstants.ALIGN_JUSTIFIED));
        
        menuAlineacion.add(itemIzqda);
        menuAlineacion.add(itemDcha);
        menuAlineacion.add(itemCentro);
        menuAlineacion.add(itemJustificado);
        
        //JMenuItem -> JMenu 'Portapapeles':
        JMenu menuPortapapeles = new JMenu("Portapapeles...");
        JMenuItem itemCortar = new JMenuItem("Cortar");
        JMenuItem itemCopiar = new JMenuItem("Copiar");
        JMenuItem itemPegar = new JMenuItem("Pegar");
        JMenuItem itemSeleccion = new JMenuItem("Seleccionar todo");
        
        itemCortar.addActionListener((ActionEvent e) -> {
            this.handler.cortarTexto(this.getIndicePestana());
        });
        
        itemCopiar.addActionListener((ActionEvent e) -> {
            this.handler.copiarTexto(this.getIndicePestana());
        });
        
        itemPegar.addActionListener((ActionEvent e) -> {
            this.handler.pegarTexto(this.getIndicePestana());
        });
        
        itemSeleccion.addActionListener((ActionEvent e) -> {
            this.handler.seleccionarTexto(this.getIndicePestana());
        });
        
        menuPortapapeles.add(itemCortar);
        menuPortapapeles.add(itemCopiar);
        menuPortapapeles.add(itemPegar);
        menuPortapapeles.add(new JSeparator(JSeparator.HORIZONTAL));
        menuPortapapeles.add(itemSeleccion);
        
        //Insertamos los JMenuItem en el JPopupMenu:
        this.menuContextual.add(menuTipoLetra);
        this.menuContextual.add(menuTamano);
        this.menuContextual.add(menuEstilo);
        this.menuContextual.add(menuAlineacion);
        this.menuContextual.add(menuPortapapeles);
    }
    
    /**
     * Método privado que inicializa los atajos de teclado del editor de texto.
     */
    private void inicializarAtajosTeclado()
    {
        //Instanciamos los atajos de teclado (CTRL + tecla):
        KeyStroke atajoSeleccionTodo = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoNegrita = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoCopiar = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);      
        KeyStroke atajoBuscar = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoResaltado = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoURL = KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoCursiva = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoJustificar = KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK);       
        KeyStroke atajoTachado = KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoAlinearIzqda = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoImagen = KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoNuevo = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoAbrir = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoImprimir = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoCentrar = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoAlinearDcha = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);    
        KeyStroke atajoGuardar = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoTabla = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoSubrayado =  KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoPegar = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoCortar = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoRehacer = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoDeshacer = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoSubindice = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoSuperindice = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK);
        
        //Instanciamos las acciones aparejadas a dichos atajos:
        Action accionSeleccionTodo = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.seleccionarTexto(getIndicePestana());
            }
        };
        
        Action accionNuevo = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.crearNuevoDocumento(panelPestanas, piePagina, etqCursor, etqDocumento, menuContextual);
            }
        };
        
        Action accionAbrir = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.abrirDocumento(panelPestanas, piePagina, etqCursor, etqDocumento, menuContextual);
            }
        };
        
        Action accionGuardar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.guardarDocumento(panelPestanas);
            }
        };
        
        Action accionNegrita = new StyledEditorKit.BoldAction();        
        Action accionCursiva = new StyledEditorKit.ItalicAction();        
        Action accionSubrayar = new StyledEditorKit.UnderlineAction();
        
        Action accionTachar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.tacharTexto(getIndicePestana());
            }
            
        };
        
        Action accionResaltar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.cambiarColorTexto(getIndicePestana());
            }
        };
        
        Action accionDeshacer = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.deshacerCambios(getIndicePestana());
            }
        };
        
        Action accionRehacer = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.rehacerCambios(getIndicePestana());
            }
        };
        
        Action accionCortar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.cortarTexto(getIndicePestana());
            }            
        };
        
        Action accionCopiar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.copiarTexto(getIndicePestana());
            }            
        };
        
        Action accionPegar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.pegarTexto(getIndicePestana());
            }            
        };
        
        Action accionSubindice = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.escribirSubindice(getIndicePestana());
            }
        };
        
        Action accionSuperindice = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.escribirSuperindice(getIndicePestana());
            }
        };
        
        Action accionAlinearIzda = new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT);        
        Action accionAlinearDcha = new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT);        
        Action accionCentrar = new StyledEditorKit.AlignmentAction("Centrado", StyleConstants.ALIGN_CENTER);        
        Action accionJustificar = new StyledEditorKit.AlignmentAction("Justificado", StyleConstants.ALIGN_JUSTIFIED);
               
        Action accionBuscar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.buscarEnTexto(getIndicePestana());
            }            
        };
        
        Action accionImprimir = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.imprimirDocumento(panelPestanas.getPanelDocumentoActivo());
            }            
        };
        
        Action accionImagen = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.insertarImagen(getIndicePestana());
            }            
        };
        
        Action accionTabla = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.insertarTabla(getIndicePestana(), 
                                      getFuenteSeleccionada(), 
                                      getTamanoFuenteSeleccionado());
            }            
        };
        
        Action accionURL = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                handler.insertarHiperenlace(getIndicePestana());
            }            
        };
        
        //Asignamos los atajos de teclado al manejador mediante el mapa de entrada:
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoSeleccionTodo, "seleccionarTodo");        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoNuevo, "nuevo");        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoAbrir, "abrir");       
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoGuardar, "guardar");       
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoDeshacer, "deshacer");      
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoRehacer, "rehacer");        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoSubindice, "subindice");        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoSuperindice, "superindice");        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoNegrita, "negrita");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoCursiva, "cursiva");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoSubrayado, "subrayado");        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoTachado, "tachado");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoCortar, "cortar");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoCopiar, "copiar");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoPegar, "pegar");        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoResaltado, "resaltar");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoAlinearIzqda, "alinearIzda");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoAlinearDcha, "alinearDcha");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoCentrar, "centrar");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoJustificar, "justificar");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoBuscar, "buscar");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoImprimir, "imprimir");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoImagen, "insertarImagen");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoTabla, "insertarTabla");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoURL, "insertarHiperenlace");
        
        //Asignamos el manejador a la acción mediante el mapa de acciones:
        this.getActionMap().put("seleccionarTodo", accionSeleccionTodo);
        this.getActionMap().put("nuevo", accionNuevo);
        this.getActionMap().put("abrir", accionAbrir);
        this.getActionMap().put("guardar", accionGuardar);
        this.getActionMap().put("deshacer", accionDeshacer);
        this.getActionMap().put("rehacer", accionRehacer);
        this.getActionMap().put("subindice", accionSubindice);
        this.getActionMap().put("superindice", accionSuperindice);
        this.getActionMap().put("negrita", accionNegrita);
        this.getActionMap().put("cursiva", accionCursiva);
        this.getActionMap().put("subrayado", accionSubrayar);
        this.getActionMap().put("tachado", accionTachar);
        this.getActionMap().put("cortar", accionCortar);
        this.getActionMap().put("copiar", accionCopiar);
        this.getActionMap().put("pegar", accionPegar);
        this.getActionMap().put("resaltar", accionResaltar);
        this.getActionMap().put("alinearIzda", accionAlinearIzda);
        this.getActionMap().put("alinearDcha", accionAlinearDcha);
        this.getActionMap().put("centrar", accionCentrar);
        this.getActionMap().put("justificar", accionJustificar);
        this.getActionMap().put("buscar", accionBuscar);
        this.getActionMap().put("imprimir", accionImprimir);
        this.getActionMap().put("insertarImagen", accionImagen);
        this.getActionMap().put("insertarTabla", accionTabla);
        this.getActionMap().put("insertarHiperenlacw", accionURL);
    }
    
    
    /***************************/
    /***  MÉTODOS 'GETTERS'  ***/
    /***************************/
    
    /**
     * Método 'getter' que llama a la barra de navegación del editor de texto.
     * @return Componente JMenuBar.
     */
    public JMenuBar getBarraNavegacion()
    {
        return this.barraNavegacion;
    }
    
    /**
     * Método 'getter' que llama al panel central de pestañas con documento
     * del editor de texto.
     * @return Componente heredado de JTabbedPane.
     */
    public PanelConPestanasCerrable getPanelPestanas()
    {
        return this.panelPestanas;
    }
    
    /**
     * Método 'getter' que llama al menú contextual que aparece al hacer click
     * derecho en cualquier documento activo.
     * @return Componente JPopupMenu.
     */
    public JPopupMenu getMenuContextual()
    {
        return this.menuContextual;
    }
    
    /**
     * Método 'getter' que devuelve el número de índice de la pestaña del 
     * documento que se encuentra activo en el editor de texto en el momento
     * de su llamada.
     * @return Entero positivo.
     */
    public int getIndicePestana()
    {
        return this.panelPestanas.getSelectedIndex();
    }
    
    /**
     * Método 'getter' que devuelve la fuente seleccionada en el JComboBox 
     * situado en el menú de herramientas del editor de texto.
     * @return Objeto Java.
     */
    public Object getFuenteSeleccionada()
    {
        return this.selectorFuente.getSelectedItem();
    }
    
    /**
     * Método 'getter' que devuelve el tamaño de fuente seleccionado en el JSpinner
     * situado en el menú de herramientas del editor de texto.
     * @return Objeto Java.
     */
    public Object getTamanoFuenteSeleccionado()
    {
        return this.selectorTamano.getValue();
    }
}
