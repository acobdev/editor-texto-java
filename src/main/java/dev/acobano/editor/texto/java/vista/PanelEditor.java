package dev.acobano.editor.texto.java.vista;

import dev.acobano.editor.texto.java.controlador.GestorEventosEditor;
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
    //ATRIBUTOS:   
    //Componentes principales:
    private JMenuBar barraNavegacion;
    private JToolBar menuHerramientas;
    private PanelConPestanasCerrable panelPestanas;
    private JPanel piePagina;
    private JPopupMenu menuContextual;
    
    //Componentes secundarios:
    private JLabel etqCursor, etqDocumento;
    JComboBox selectorFuente;
    JSpinner selectorTamano;
    
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
        this.inicializarPiePagina();        
        this.inicializarMenuHerramientas();
        this.inicializarBarraNavegacion();
        this.inicializarPanelPestanas();
        this.inicializarLayoutPanel();
        this.inicializarMenuContextual();
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
            this.handler.crearNuevoDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento);
            this.handler.getListaDocumentos().get(this.panelPestanas.getSelectedIndex()).setComponentPopupMenu(this.menuContextual);
            this.handler.getListaDocumentos().get(this.panelPestanas.getSelectedIndex()).setFont(new Font(String.valueOf(this.selectorFuente.getSelectedItem()), Font.PLAIN, (Integer) this.selectorTamano.getValue()));
        });
        
        abrirArchivo.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento);
            this.handler.getListaDocumentos().get(this.panelPestanas.getSelectedIndex()).setComponentPopupMenu(this.menuContextual);
        });
        
        guardarArchivo.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumento(this.panelPestanas);
        });
        
        itemDeshacer.addActionListener((ActionEvent e) -> {
            this.handler.deshacerCambios(this.panelPestanas.getSelectedIndex());
        });
        
        itemRehacer.addActionListener((ActionEvent e) -> {
            this.handler.rehacerCambios(this.panelPestanas.getSelectedIndex());
        });
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
        //JButton guardarPDF = new JButton(new ImageIcon("src/main/resources/icons/pdf.png"));
        
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
        JButton btnSubindice = new JButton(new ImageIcon("src/main/resources/icons/subscript.png"));
        JButton btnSuperindice = new JButton(new ImageIcon("src/main/resources/icons/superscript.png"));
        //JButton insertarImagen = new JButton(new ImageIcon("src/main/resources/icons/addimage.png"));
        
        JButton btnSelectorColor = new JButton(new ImageIcon("src/main/resources/icons/color.png"));
        this.selectorFuente = new JComboBox(TIPOS_FUENTE);
        this.selectorTamano = new JSpinner(new SpinnerListModel(TAMANOS_FUENTE));
        selectorTamano.setPreferredSize(new Dimension(46, 64));
        
        //Pegamos estos nuevos componentes en el menú:       
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(selectorFuente);
        this.menuHerramientas.add(selectorTamano);
        this.menuHerramientas.add(btnSelectorColor);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnNuevo);
        this.menuHerramientas.add(btnAbrir);
        this.menuHerramientas.add(guardar);
        //this.menuHerramientas.add(guardarPDF);
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
        this.menuHerramientas.add(btnSubindice);
        this.menuHerramientas.add(btnSuperindice);
        this.menuHerramientas.add(btnResaltado);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnIzqda);
        this.menuHerramientas.add(btnCentro);
        this.menuHerramientas.add(btnDerecha);
        this.menuHerramientas.add(btnJustificado);       
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        //this.menuHerramientas.add(insertarImagen);
        
        
        //Agregamos tooltips para cuando el usuario haga hover sobre los botones:
        btnNuevo.setToolTipText("Nuevo documento (CTRL + N)");
        btnAbrir.setToolTipText("Abrir documento (CTRL + O)");
        guardar.setToolTipText("Guardar como fichero de texto (CTRL + S)");
        //guardarPDF.setToolTipText("Guardar como archivo PDF");        
        btnDeshacer.setToolTipText("Deshacer (CTRL + Z)");
        btnRehacer.setToolTipText("Rehacer (CTRL + Y)");
        btnCortar.setToolTipText("Cortar (CTRL + X)");
        btnCopiar.setToolTipText("Copiar (CTRL + C)");
        btnPegar.setToolTipText("Pegar (CTRL + V)");
        btnNegrita.setToolTipText("Negrita");
        btnCursiva.setToolTipText("Cursiva");
        btnSubrayado.setToolTipText("Subrayado");
        btnResaltado.setToolTipText("Resaltado");
        btnSubindice.setToolTipText("Subíndice");
        btnSuperindice.setToolTipText("Superíndice");
        btnIzqda.setToolTipText("Alinear a la izquierda");
        btnCentro.setToolTipText("Centrar");
        btnDerecha.setToolTipText("Alinear a la derecha");
        btnJustificado.setToolTipText("Justificar");
        //insertarImagen.setToolTipText("Insertar nueva imagen");
        btnSelectorColor.setToolTipText("Color de fuente");
        selectorTamano.setToolTipText("Tamaño de fuente");
        selectorFuente.setToolTipText("Tipo de fuente");
        
        //Creamos los respectios eventos de botón:
        btnNuevo.addActionListener((ActionEvent e) -> {
            this.handler.crearNuevoDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento);
        });
        
        btnAbrir.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento);
        });
        
        guardar.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumento(this.panelPestanas);
        });
        
        btnDeshacer.addActionListener((ActionEvent e) -> {
            this.handler.deshacerCambios(this.panelPestanas.getSelectedIndex());
        });
        
        btnRehacer.addActionListener((ActionEvent e) -> {
            this.handler.rehacerCambios(this.panelPestanas.getSelectedIndex());
        });
        
        btnSubindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSubindice(this.panelPestanas.getSelectedIndex());
        });
        
        btnSuperindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSuperindice(this.panelPestanas.getSelectedIndex());
        });
        
        btnSelectorColor.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorTexto(this.panelPestanas.getSelectedIndex());
        });
        
        btnResaltado.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorResaltado(this.panelPestanas.getSelectedIndex());
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
        
        selectorFuente.addItemListener((ItemEvent ie) -> {
            this.handler.cambiarFuente(this.panelPestanas.getSelectedIndex(), selectorFuente.getSelectedItem());
        });
        
        selectorTamano.addChangeListener((ChangeEvent ce) -> {
            this.handler.cambiarTamanoFuente(this.panelPestanas.getSelectedIndex(), selectorTamano.getValue());
        });
    }
    
    
    private void inicializarPiePagina()
    {
        //Inicializamos los componentes:
        this.piePagina = new JPanel();
        this.etqCursor = new JLabel();
        this.etqDocumento = new JLabel();
        
        this.piePagina.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 30));
        this.piePagina.setLayout(new BorderLayout());
        this.piePagina.add(etqCursor, BorderLayout.WEST);
        this.piePagina.add(etqDocumento, BorderLayout.EAST);
        this.piePagina.setVisible(false);
    }
    
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
    
    private void inicializarLayoutPanel()
    {
        //Creamos un nuevo objeto para definir el layout:
        this.setLayout(new BorderLayout());
        
        //Pegamos los componentes definidos en los anteriores métodos en el panel:
        this.add(this.menuHerramientas, BorderLayout.NORTH);
        this.add(this.panelPestanas, BorderLayout.CENTER);
        this.add(this.piePagina, BorderLayout.SOUTH);
    }
    
    private void inicializarMenuContextual()
    {
        //Inicializamos el componente JPopupMenu:
        this.menuContextual = new JPopupMenu();
        
        //Instanciamos los JMenuItem que estarán dentro del JPopupMenu:
        //JMenuItem 'Tipo de letra':
        JMenu menuTipoLetra = new JMenu("Tipo de fuente...");        
        for (String s : TIPOS_FUENTE)
        {
            JMenuItem itemFuente = new JMenuItem(s);
            itemFuente.addActionListener((ActionEvent e) -> {
                this.handler.cambiarFuente(this.panelPestanas.getSelectedIndex(), s);
            });
            menuTipoLetra.add(itemFuente);
        }
        
        //JMenuItem 'Tamaño de fuente':
        JMenu menuTamano = new JMenu("Tamaño de fuente...");
        for (Integer i : TAMANOS_FUENTE)
        {
            JMenuItem itemTamano = new JMenuItem(String.valueOf(i));
            itemTamano.addActionListener((ActionEvent e) -> {
                this.handler.cambiarTamanoFuente(this.panelPestanas.getSelectedIndex(), i);
            });
            menuTamano.add(itemTamano);
        }
        
        //JMenuItem 'Estilos':
        JMenu menuEstilo = new JMenu("Estilo...");
        JMenuItem itemNegrita = new JMenuItem("Negrita");
        JMenuItem itemCursiva = new JMenuItem("Cursiva");
        JMenuItem itemSubrayado = new JMenuItem("Subrayado");
        JMenuItem itemSubindice = new JMenuItem("Subíndice");
        JMenuItem itemSuperindice = new JMenuItem("Superíndice");
        
        itemNegrita.addActionListener(new StyledEditorKit.BoldAction());
        itemCursiva.addActionListener(new StyledEditorKit.ItalicAction());
        itemSubrayado.addActionListener(new StyledEditorKit.UnderlineAction());
        
        itemSubindice.addActionListener((ActionEvent e) -> {
                this.handler.escribirSubindice(this.panelPestanas.getSelectedIndex());
            });
        
        itemSuperindice.addActionListener((ActionEvent e) -> {
                this.handler.escribirSuperindice(this.panelPestanas.getSelectedIndex());
            });
        
        menuEstilo.add(itemNegrita);
        menuEstilo.add(itemCursiva);
        menuEstilo.add(itemSubrayado);
        menuEstilo.add(new JSeparator(JSeparator.HORIZONTAL));
        menuEstilo.add(itemSubindice);
        menuEstilo.add(itemSuperindice);
        
        //JMenuItem 'Alineación':
        JMenu menuAlineacion = new JMenu("Alineación...");
        JMenuItem itemIzqda = new JMenuItem("Izquierda");
        JMenuItem itemDcha = new JMenuItem("Derecha");
        JMenuItem itemCentro = new JMenuItem("Centrado");
        JMenuItem itemJustificado = new JMenuItem("Justificado");
        
        itemIzqda.addActionListener(new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT));
        itemCentro.addActionListener(new StyledEditorKit.AlignmentAction("Centro", StyleConstants.ALIGN_CENTER));
        itemDcha.addActionListener(new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT));
        itemJustificado.addActionListener(new StyledEditorKit.AlignmentAction("Justificado", StyleConstants.ALIGN_JUSTIFIED));
        
        menuAlineacion.add(itemIzqda);
        menuAlineacion.add(itemDcha);
        menuAlineacion.add(itemCentro);
        menuAlineacion.add(itemJustificado);
        
        //Insertamos los JMenuItem en el JPopupMenu:
        this.menuContextual.add(menuTipoLetra);
        this.menuContextual.add(menuTamano);
        this.menuContextual.add(menuEstilo);
        this.menuContextual.add(menuAlineacion);
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
