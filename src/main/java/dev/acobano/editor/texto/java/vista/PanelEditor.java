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
    //ATRIBUTOS:   
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
        this.inicializarMenuContextual();   
        this.inicializarMenuHerramientas();
        this.inicializarBarraNavegacion();       
        this.inicializarPiePagina();
        this.inicializarPanelPestanas();
        this.inicializarLayoutPanel();
        this.inicializarAtajosTeclado();
    }
    
    
    //MÉTODOS PRIVADOS DE INICIALIZACIÓN:
    private void inicializarBarraNavegacion()
    {
        //Inicializamos la barra de menuInsertar:
        this.barraNavegacion = new JMenuBar();
        
        //Se crean los JMenu que estarán dentro del JMenuBar:
        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuEdicion = new JMenu("Edición");
        JMenu menuInsertar = new JMenu ("Insertar");
        
        //Se insertan los JMenu en el JMenuBar:
        this.barraNavegacion.add(menuArchivo);
        this.barraNavegacion.add(menuEdicion);
        this.barraNavegacion.add(menuInsertar);
        
        //Se crean los JMenuItem de cada uno de los JMenu:
        //JMenuItem -> JMenu Archivo:
        JMenuItem itemAbrir = new JMenuItem("Abrir documento");
        JMenu menuNuevo = new JMenu("Nuevo documento...");
        JMenuItem itemNuevoBlanco = new JMenuItem("Nuevo documento en blanco");
        JMenuItem itemNuevoHTML = new JMenuItem("Nuevo documento HTML");
        JMenu menuGuardar = new JMenu("Guardar...");
        JMenuItem itemGuardarTXT = new JMenuItem("Guardar como fichero TXT");
        JMenuItem itemGuardarRTF = new JMenuItem("Guardar como fichero RTF");
        JMenuItem itemGuardarPDF = new JMenuItem("Guardar como fichero PDF");
        JMenuItem itemGuardarDOC = new JMenuItem("Guardar como fichero Microsoft Word");
        JMenuItem itemGuardarComo = new JMenuItem("Guardar como...");
        JMenuItem itemEliminarTodos = new JMenuItem("Cerrar todos los documentos");
        JMenuItem itemSalir = new JMenuItem("Salir");
        
        menuArchivo.add(itemAbrir);
        menuNuevo.add(itemNuevoBlanco);
        menuNuevo.add(itemNuevoHTML);
        menuArchivo.add(menuNuevo);
        menuGuardar.add(itemGuardarTXT);
        menuGuardar.add(itemGuardarRTF);
        menuGuardar.add(itemGuardarPDF);
        menuGuardar.add(itemGuardarDOC);
        menuArchivo.add(menuGuardar);
        menuArchivo.add(itemGuardarComo);
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
        
        //JMenuItem - JMenu 'Insertar':
        JMenuItem itemImagen = new JMenuItem("Insertar imagen");
        JMenuItem itemTabla = new JMenuItem("Insertar tabla");
        
        menuInsertar.add(itemImagen);
        menuInsertar.add(itemTabla);
        
        //Instanciamos los eventos de click a los JMenuItems:
        itemNuevoBlanco.addActionListener((ActionEvent e) -> {
            this.handler.crearNuevoDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento, this.menuContextual);
            this.handler.getListaDocumentos().get(this.getIndicePestana()).setFont(new Font(String.valueOf(this.selectorFuente.getSelectedItem()), Font.PLAIN, (Integer) this.selectorTamano.getValue()));
        });
        
        itemNuevoHTML.addActionListener((ActionEvent e) -> {
            this.handler.crearDocumentoHTML(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento, this.menuContextual);
            this.handler.getListaDocumentos().get(this.getIndicePestana()).setFont(new Font(String.valueOf(this.selectorFuente.getSelectedItem()), Font.PLAIN, (Integer) this.selectorTamano.getValue()));
        });
        
        itemAbrir.addActionListener((ActionEvent e) -> {
            this.handler.abrirDocumento(this.panelPestanas, this.piePagina, this.etqCursor, this.etqDocumento, this.menuContextual);
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
        
        itemGuardarComo.addActionListener((ActionEvent e) -> {
            this.handler.guardarDocumentoComo(this.panelPestanas, null);
        });
        
        itemEliminarTodos.addActionListener((ActionEvent e) -> {
            this.panelPestanas.removeAll();
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
        
        itemImagen.addActionListener((ActionEvent e) -> {
            this.handler.insertarImagen(this.getIndicePestana());
        });
        
        itemTabla.addActionListener((ActionEvent e) -> {
            this.handler.insertarTabla(this.getIndicePestana(), 
                                       this.selectorFuente.getSelectedItem(), 
                                       this.selectorTamano.getValue());
        });
        
        itemCortar.addActionListener(this.handler.cortarTexto());
        itemCopiar.addActionListener(this.handler.copiarTexto());
        itemPegar.addActionListener(this.handler.pegarTexto());
    }
        
    private void inicializarMenuHerramientas()
    {
        this.menuHerramientas = new JToolBar(JToolBar.HORIZONTAL);
        this.menuHerramientas.setFloatable(false);
        this.menuHerramientas.setVisible(true);
        
        //Instanciamos los botones del menú:
        JButton btnNuevo = new JButton(new ImageIcon("src/main/resources/icons/text.png"));
        JButton btnAbrir = new JButton(new ImageIcon("src/main/resources/icons/file.png"));
        JButton btnGuardar = new JButton(new ImageIcon("src/main/resources/icons/save.png"));
        JButton btnGuardarComo = new JButton(new ImageIcon("src/main/resources/icons/saveas.png"));     
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
        JButton btnSelectorColor = new JButton(new ImageIcon("src/main/resources/icons/color.png"));
        
        this.selectorFuente = new JComboBox(TIPOS_FUENTE);
        SpinnerListModel modelo = new SpinnerListModel(TAMANOS_FUENTE);
        modelo.setValue(14);
        this.selectorTamano = new JSpinner(modelo);
        this.selectorTamano.setPreferredSize(new Dimension(46, 64));
        
        //Pegamos estos nuevos componentes en el menú:       
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(selectorFuente);
        this.menuHerramientas.add(selectorTamano);
        this.menuHerramientas.add(btnSelectorColor);
        this.menuHerramientas.add(new JSeparator(JSeparator.VERTICAL));
        this.menuHerramientas.add(btnNuevo);
        this.menuHerramientas.add(btnAbrir);
        this.menuHerramientas.add(btnGuardar);
        this.menuHerramientas.add(btnGuardarComo);
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
        
        
        //Agregamos tooltips para cuando el usuario haga hover sobre los botones:
        btnNuevo.setToolTipText("Nuevo documento (CTRL + N)");
        btnAbrir.setToolTipText("Abrir documento (CTRL + O)");
        btnGuardar.setToolTipText("Guardar documento (CTRL + S)");
        btnGuardarComo.setToolTipText("Guardar como...");        
        btnDeshacer.setToolTipText("Deshacer (CTRL + Z)");
        btnRehacer.setToolTipText("Rehacer (CTRL + Y)");
        btnCortar.setToolTipText("Cortar (CTRL + X)");
        btnCopiar.setToolTipText("Copiar (CTRL + C)");
        btnPegar.setToolTipText("Pegar (CTRL + V)");
        btnNegrita.setToolTipText("Negrita (CTRL + B)");
        btnCursiva.setToolTipText("Cursiva (CTRL + I)");
        btnSubrayado.setToolTipText("Subrayado (CTRL + U)");
        btnResaltado.setToolTipText("Resaltado");
        btnSubindice.setToolTipText("Subíndice (CTRL + '-')");
        btnSuperindice.setToolTipText("Superíndice (CTRL + '+')");
        btnIzqda.setToolTipText("Alinear a la izquierda (CTRL + Q)");
        btnCentro.setToolTipText("Centrar (CTRL + T)");
        btnDerecha.setToolTipText("Alinear a la derecha (CTRL + D)");
        btnJustificado.setToolTipText("Justificar (CTRL + J)");
        btnSelectorColor.setToolTipText("Color de fuente");
        selectorTamano.setToolTipText("Tamaño de fuente");
        selectorFuente.setToolTipText("Tipo de fuente");
        
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
            this.handler.guardarDocumentoComo(this.panelPestanas, null);
        });
        
        btnDeshacer.addActionListener((ActionEvent e) -> {
            this.handler.deshacerCambios(this.getIndicePestana());
        });
        
        btnRehacer.addActionListener((ActionEvent e) -> {
            this.handler.rehacerCambios(this.getIndicePestana());
        });
        
        btnSubindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSubindice(this.getIndicePestana());
        });
        
        btnSuperindice.addActionListener((ActionEvent e) -> {
            this.handler.escribirSuperindice(this.getIndicePestana());
        });
        
        btnSelectorColor.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorTexto(this.getIndicePestana());
        });
        
        btnResaltado.addActionListener((ActionEvent e) -> {
            this.handler.cambiarColorResaltado(this.getIndicePestana());
        });
        
        btnCortar.addActionListener(this.handler.cortarTexto());
        btnCopiar.addActionListener(this.handler.copiarTexto());
        btnPegar.addActionListener(this.handler.pegarTexto());
        btnNegrita.addActionListener(this.handler.escribirNegrita());
        btnCursiva.addActionListener(this.handler.escribirCursiva());
        btnSubrayado.addActionListener(this.handler.escribirSubrayado());
        btnIzqda.addActionListener(this.handler.alinearTextoIzquierda());
        btnCentro.addActionListener(this.handler.centrarTexto());
        btnDerecha.addActionListener(this.handler.alinearTextoDerecha());
        btnJustificado.addActionListener(this.handler.justificarTexto());
        
        selectorFuente.addItemListener((ItemEvent ie) -> {
            this.handler.cambiarFuente(this.getIndicePestana(), selectorFuente.getSelectedItem());
        });
        
        selectorTamano.addChangeListener((ChangeEvent ce) -> {
            this.handler.cambiarTamanoFuente(this.getIndicePestana(), selectorTamano.getValue());
        });
    }
    
    
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
        
        itemNegrita.addActionListener(this.handler.escribirNegrita());
        itemCursiva.addActionListener(this.handler.escribirCursiva());
        itemSubrayado.addActionListener(this.handler.escribirSubrayado());
        
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
        
        itemIzqda.addActionListener(this.handler.alinearTextoIzquierda());
        itemCentro.addActionListener(this.handler.centrarTexto());
        itemDcha.addActionListener(this.handler.alinearTextoDerecha());
        itemJustificado.addActionListener(this.handler.justificarTexto());
        
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
    
    private void inicializarAtajosTeclado()
    {
        //Instanciamos los atajos de teclado (CTRL + tecla):
        KeyStroke atajoSeleccionTodo = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoNegrita = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoCopiar = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoAlinearDcha = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);        
        KeyStroke atajoResaltado = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoTachado = KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoCursiva = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoJustificar = KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoNuevo = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoAbrir = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoPegar = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoCentrar = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoGuardar = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoAlinearIzqda = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK);
        KeyStroke atajoSubrayado =  KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK);
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
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoAlinearIzqda, "alinearIzda");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoAlinearDcha, "alinearDcha");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoCentrar, "centrar");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(atajoJustificar, "justificar");
        
        //Asignamos el manejador a la acción mediante el mapa de acciones:
        this.getActionMap().put("seleccionarTodo", accionSeleccionTodo);
        this.getActionMap().put("nuevo", accionNuevo);
        this.getActionMap().put("abrir", accionAbrir);
        this.getActionMap().put("guardar", accionGuardar);
        this.getActionMap().put("deshacer", accionDeshacer);
        this.getActionMap().put("rehacer", accionRehacer);
        this.getActionMap().put("subindice", accionSubindice);
        this.getActionMap().put("superindice", accionSuperindice);
        this.getActionMap().put("negrita", this.handler.escribirNegrita());
        this.getActionMap().put("cursiva", this.handler.escribirCursiva());
        this.getActionMap().put("subrayado", this.handler.escribirSubrayado());
        this.getActionMap().put("tachado", accionTachar);
        this.getActionMap().put("cortar", this.handler.cortarTexto());
        this.getActionMap().put("copiar", this.handler.copiarTexto());
        this.getActionMap().put(("pegar"), this.handler.pegarTexto());
        this.getActionMap().put("alinearIzda", this.handler.alinearTextoIzquierda());
        this.getActionMap().put("alinearDcha", this.handler.alinearTextoDerecha());
        this.getActionMap().put("centrar", this.handler.centrarTexto());
        this.getActionMap().put("justificar", this.handler.justificarTexto());
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
    
    public JPopupMenu getMenuContextual()
    {
        return this.menuContextual;
    }
    
    public int getIndicePestana()
    {
        return this.panelPestanas.getSelectedIndex();
    }
}
