package dev.acobano.editor.texto.java.controlador;

import dev.acobano.editor.texto.java.vista.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.UndoManager;

/**
 * Clase Java encargada del área de gestión de eventos delegados desde la sección
 * de 'Vista' de la aplicación. Es capaz de usar los atributos requeridos para el
 * empleo de ésta como proceso, además de poseer todos los métodos necesarios para
 * ejecutar las acciones deseadas en el editor de texto.
 * @author Álvaro Cobano
 */
public class GestorEventosEditor 
{   
    //ATRIBUTOS:
    private ArrayList<JTextPane> listaDocumentos;
    private ArrayList<File> listaArchivosAbiertos;
    private ArrayList<UndoManager> listaManager;
    private int contador;
    
    
    //CONSTRUCTOR:
    public GestorEventosEditor()
    {
        this.listaDocumentos = new ArrayList<>();
        this.listaArchivosAbiertos = new ArrayList<>();
        this.listaManager = new ArrayList<>();
        this.contador = 1;
    }
    
    
    //MÉTODOS 'GETTERS':
    public ArrayList<JTextPane> getListaDocumentos()
    {
        return this.listaDocumentos;
    }
    
    public ArrayList<File> getListaArchivosAbiertos()
    {
        return this.listaArchivosAbiertos;
    }
    
    
    //EVENTOS DE ACCIÓN:
    public void crearNuevoDocumento(PanelConPestanasCerrable panelPestanas, 
                                    JPanel piePagina, 
                                    JLabel etqCursor, 
                                    JLabel etqDoc,
                                    JPopupMenu menuContextual)
    {
        JTextPane panelTexto = new JTextPane();
        UndoManager manager = new UndoManager();
        PanelDocumento doc = new PanelDocumento(panelTexto, manager, etqCursor, etqDoc, menuContextual);
        
        if (this.listaDocumentos.isEmpty())
            this.contador = 1;
        
        panelPestanas.crearPestana("Documento " + this.contador, doc, this.listaDocumentos, this.listaManager, piePagina);
        panelPestanas.setSelectedIndex(this.listaDocumentos.size());
        panelPestanas.setVisible(true);
        this.listaDocumentos.add(panelTexto);
        this.listaManager.add(manager);
        this.contador++;
    }
    
    public void abrirDocumento(PanelConPestanasCerrable panelPestanas, 
                               JPanel piePagina, 
                               JLabel etqCursor, 
                               JLabel etqDoc,
                               JPopupMenu menuContextual)
    {
        JFileChooser selectorArchivo = new JFileChooser();
        selectorArchivo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int resultado = selectorArchivo.showOpenDialog(null);
        
        if (resultado == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                boolean existeArchivo = false;
                File f = selectorArchivo.getSelectedFile();
                
                for (File f1 : this.listaArchivosAbiertos)
                    if (f1.getPath().equals(f.getPath()))
                    {                        
                        existeArchivo = true;
                        break;
                    }
                
                if (!existeArchivo)
                {
                    this.listaArchivosAbiertos.add(f);
                    JTextPane panelTexto = new JTextPane();
                    UndoManager manager = new UndoManager();
                    PanelDocumento doc = new PanelDocumento(panelTexto, manager, etqCursor, etqDoc, menuContextual);
                    
                    //Empleamos un Input Stream para traer la información del archivo:
                    BufferedReader flujoEntrada = new BufferedReader(new FileReader(f.getPath()));
                    String linea = "";
                    
                    //Leemos línea a línea del archivo y lo almacenamos en el String:
                    while (linea != null)
                    {
                        linea = flujoEntrada.readLine();
                        
                        if (linea != null)
                        {
                            Document d = doc.getDocumento().getDocument();
                            d.insertString(d.getLength(), linea + "\n", null);
                        }
                    }
                                        
                    panelPestanas.crearPestana(f.getName(), 
                                               doc, 
                                               this.listaDocumentos, 
                                               this.listaArchivosAbiertos, f, 
                                               this.listaManager,
                                               piePagina);
                    panelPestanas.setSelectedIndex(this.listaDocumentos.size());
                    panelPestanas.setVisible(true);                    
                    this.listaDocumentos.add(panelTexto);
                    this.listaManager.add(manager);
                    flujoEntrada.close();
                }
                else
                    JOptionPane.showMessageDialog(null, 
                                                  "El documento seleccionado ya se encuentra abierto en otra pestaña del editor.", 
                                                  "ERROR", 
                                                  JOptionPane.WARNING_MESSAGE);
            }
            catch (FileNotFoundException fnfe)
            {
                JOptionPane.showMessageDialog(null, 
                                              "No se ha encontrado ningún archivo en la ruta seleccionada.", 
                                              "ERROR", 
                                              JOptionPane.ERROR_MESSAGE);
            } 
            catch (IOException | BadLocationException ex) 
            {
                Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void guardarDocumento(PanelConPestanasCerrable panelPestanas)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //En primer lugar, deberemos reconocer si el archivo ya está guardado en el sistema:
            boolean esNuevoGuardado = true;
            File guardado = new File("");
            
            for (File f : this.listaArchivosAbiertos)
                if (f.getName().equals(panelPestanas.getLabelCabecera(panelPestanas.getSelectedIndex()).getText()))
                {
                    esNuevoGuardado = false;
                    guardado = f;
                }
                    
            
            if (esNuevoGuardado)
            {
                JFileChooser guardadorArchivo = new JFileChooser();
                guardadorArchivo.setDialogTitle("Guardar documento");
                int opcion = guardadorArchivo.showSaveDialog(null);

                if (opcion == JFileChooser.APPROVE_OPTION)
                {
                    File archivo = guardadorArchivo.getSelectedFile();
                    this.listaArchivosAbiertos.add(archivo);
                    this.escribirArchivo(panelPestanas.getSelectedIndex(), archivo);                 
                }
            //En caso de que ya se encuentre abierto, lo que haremos será 
            //sobreescribirlo con el flujo de salida de datos por defecto:
            } else {
                this.escribirArchivo(panelPestanas.getSelectedIndex(), guardado);
            }
        }           
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser guardado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void guardarDocumentoComo(PanelConPestanasCerrable panelPestanas, ExtensionesFormatosSoportados formato)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //El usuario selecciona la ruta del archivo mediante un FileChooser:
            JFileChooser guardadorArchivo = new JFileChooser();
            guardadorArchivo.setDialogTitle("Guardar como...");
        
            //Cambiamos el título y mensaje del FileChooser según el formato escogido:
            switch (formato)
            {
                case TXT: 
                {
                    guardadorArchivo.setDialogTitle("Guardar como archivo TXT");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de texto plano TXT", "txt"));
                    break;
                }
                case RTF: 
                {
                    guardadorArchivo.setDialogTitle("Guardar como archivo RTF");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de texto con formato RTF", "rtf"));                
                    break;
                }
                case PDF:
                {
                    guardadorArchivo.setDialogTitle("Guardar como archivo PDF");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de almacenamiento digital PDF", "pdf"));
                    break;
                }
                case DOCX:
                {
                    guardadorArchivo.setDialogTitle("Guardar como archivo Word");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de texto Microsoft Word", "doc", "docx"));
                    break;
                }
                //Caso por defecto, se presiona el botón 'Guardar como'
                default:
                    guardadorArchivo.setDialogTitle("Guardar como...");              
            }
        
            //El usuario escoge una ruta y presiona un botón del JFileChooser:
            int opcion = guardadorArchivo.showSaveDialog(null);

            //Si ha presionado el botón 'Aceptar', entonces se inicia el proceso de guardado:
            if (opcion == JFileChooser.APPROVE_OPTION)
            {
                File ficheroAGuardar = guardadorArchivo.getSelectedFile();
                String rutaFichero = ficheroAGuardar.getAbsolutePath();
                
                //Cambiamos el título de la pestaña del documento por el nombre del archivo:
                panelPestanas.getLabelCabecera(panelPestanas.getSelectedIndex()).setText(ficheroAGuardar.getName());
            
                //Verificamos la extensión del archivo y enviamos a los métodos especializados:
                switch (formato)
                {
                    case TXT:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".txt"))
                            rutaFichero.concat(".txt");
                    
                        this.escribirArchivo(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }                    
                    case RTF:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".rtf"))
                            rutaFichero.concat(".rtf");
                    
                        this.guardarComoRTF(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }                   
                    case PDF:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".pdf"))
                            rutaFichero.concat(".pdf");
                    
                        this.guardarComoPDF(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }
                    case DOCX:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".doc") &&
                            !rutaFichero.toLowerCase().endsWith(".docx"))
                                rutaFichero.concat(".docx");
                    
                        this.guardarComoDOCX(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }
                    //Caso por defecto, se llama al flujo de salida básico de Java:
                    default:
                        this.escribirArchivo(this.listaArchivosAbiertos.size(), ficheroAGuardar);
                   
                    //Introducimos el fichero en nuestro ArrayList:
                    this.listaArchivosAbiertos.add(ficheroAGuardar);

                    //Avisamos al usuario que el procedimiento de guardado ha sido exitoso:
                    JOptionPane.showMessageDialog(null, 
                                                  "El documento " + ficheroAGuardar.getName() +
                                                  " ha sido guardado exitosamente en la ruta: " + ficheroAGuardar.getAbsolutePath(), 
                                                  "ARCHIVO GUARDADO", 
                                                  JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        //En caso de no tener ningún archivo abierto, se avisa al usuario:
        else
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser guardado.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
    }
    
    private void escribirArchivo(int indice, File ficheroAGuardar)
    {
        try
        {
            //Recogemos el texto escrito en el documento a guardar:
            String textoDocumento = this.listaDocumentos.get(indice).getText();
            
            //Instanciamos los streams de salida para los datos:
            FileWriter flujoSalida = new FileWriter(ficheroAGuardar);
            BufferedWriter buferMemoria = new BufferedWriter(flujoSalida);
            
            //Escribimos el texto en el fichero mediante el stream:
            buferMemoria.write(textoDocumento);
            
            //Cerramos los streams:
            buferMemoria.close();
            flujoSalida.close();
        }
        catch(IOException ioe)
        {
            Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ioe);
            
            JOptionPane.showMessageDialog(null, 
                                          "Ha ocurrido un error inesperado en el proceso de guardado.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarComoRTF(int indice, File ficheroAGuardar)
    {
        try
        {
            //Guardamos el contenido del JTextPane en formato RTF:
            RTFEditorKit editorKit = new RTFEditorKit();
            StyledDocument doc = this.listaDocumentos.get(indice).getStyledDocument();
            FileOutputStream fos = new FileOutputStream(ficheroAGuardar);
            editorKit.write(fos, doc, 0, doc.getLength());
            fos.close();
        }
        catch (IOException | BadLocationException ex)
        {
            Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ex);
            
            JOptionPane.showMessageDialog(null, 
                                          "Ha ocurrido un error inesperado en el proceso de guardado.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarComoPDF(int indice, File ficheroAGuardar)
    {
        try
        {                
            //Creamos un nuevo documento y página PDF:
            PDDocument doc = new PDDocument();
            PDPage pagina = new PDPage(PDRectangle.A4);
            doc.addPage(pagina);
            
            //Insertamos el contenido en la página PDF:
            PDPageContentStream flujoContenido = new PDPageContentStream(doc, pagina, AppendMode.APPEND, true);
            flujoContenido.beginText();
            flujoContenido.newLineAtOffset(50, 700);
            flujoContenido.showText(this.listaDocumentos.get(indice).getText());
            flujoContenido.endText();
            flujoContenido.close();
                         
            //Guardamos el documento como archivo PDF:
            doc.save(ficheroAGuardar);
            doc.close();
        }
        catch (IOException ioe)
        {
            Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ioe);
            
            JOptionPane.showMessageDialog(null, 
                                          "Ha ocurrido un error inesperado en el proceso de guardado.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarComoDOCX(int indice, File ficheroAGuardar)
    {
        try
        {
            //Guardamos los datos del JTextPane en formato Word gracias a la dependencia Apache POI:
            FileOutputStream fos = new FileOutputStream(ficheroAGuardar);
            XWPFDocument documento = new XWPFDocument();
            XWPFParagraph parrafo = documento.createParagraph();
            XWPFRun run = parrafo.createRun();
            run.setText(this.listaDocumentos.get(indice).getText());
            documento.write(fos);
            fos.close();
        }
        catch (IOException ioe)
        {
            Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ioe);
            
            JOptionPane.showMessageDialog(null, 
                                          "Ha ocurrido un error inesperado en el proceso de guardado.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deshacerCambios(int indice)
    {
        if (this.listaManager.get(indice).canUndo())
            this.listaManager.get(indice).undo();
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void rehacerCambios(int indice)
    {
        if (this.listaManager.get(indice).canRedo())
            this.listaManager.get(indice).redo();
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void seleccionarTexto(int indice)
    {
        if (!this.listaManager.isEmpty())
            this.listaDocumentos.get(indice).selectAll();
        else
            
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void cambiarColorTexto(int indice)
    {
        //Obtenemos los atributos actuales del texto seleccionado:
        SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
        
        //Instanciamos un JColorChooser para que el usuario escoja un color de la paleta:
        Color color = JColorChooser.showDialog(null, "Elija un color para la letra", this.listaDocumentos.get(indice).getSelectedTextColor());
        
        if (color != null)
        {
            //Cambiamos el color de las letras del texto:
            StyleConstants.setForeground(atributos, color);

            //Damos los nuevos atributos al texto:
            this.listaDocumentos.get(indice).setCharacterAttributes(atributos, false);
            
            //Ponemos el foco en el documento para mejor UX:
            this.listaDocumentos.get(indice).requestFocus();
        }
        else
            JOptionPane.showMessageDialog(null,
                                          "No ha seleccionado ningún color de la paleta para cambiar el color de la letra.",
                                          "AVISO",
                                          JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void cambiarColorResaltado(int indice)
    {
        //Obtenemos los atributos actuales del texto seleccionado:
        SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
        
        //Instanciamos un JColorChooser para que el usuario escoja un color de la paleta:
        Color color = JColorChooser.showDialog(null, 
                                               "Elija un color para el resaltado", 
                                               this.listaDocumentos.get(indice).getSelectedTextColor());
        
        if (color != null)
        {
            //Cambiamos el color del fondo del texto:
            StyleConstants.setBackground(atributos, color);

            //Damos los nuevos atributos al texto:
            this.listaDocumentos.get(indice).setCharacterAttributes(atributos, false);
            
            //Ponemos el foco en el documento para mejor UX:
            this.listaDocumentos.get(indice).requestFocus();
        }else
            JOptionPane.showMessageDialog(null,
                                          "No ha seleccionado ningún color de la paleta para cambiar el color del resaltado.",
                                          "AVISO",
                                          JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void cambiarFuente(int indice, Object valorSeleccionado)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());

            //Cambiamos la familia de la fuente del texto:
            StyleConstants.setFontFamily(atributos, String.valueOf(valorSeleccionado));

            //Damos los nuevos atributos al texto:
            this.listaDocumentos.get(indice).setCharacterAttributes(atributos, false);
            
            //Ponemos el foco en el documento para mejor UX:
            this.listaDocumentos.get(indice).requestFocus();
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void cambiarTamanoFuente(int indice, Object valorSeleccionado)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());

            //Cambiamos el tamaño de la fuente del texto:
            StyleConstants.setFontSize(atributos, Integer.parseInt(String.valueOf(valorSeleccionado)));

            //Damos los nuevos atributos al texto:
            this.listaDocumentos.get(indice).setCharacterAttributes(atributos, false);
            
            //Ponemos el foco en el documento para mejor UX:
            this.listaDocumentos.get(indice).requestFocus();
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void insertarImagen(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Instanciamos un JFileChooser para que el usuario eliga la imagen que desee del sistema:
            JFileChooser selectorImagen = new JFileChooser();
            selectorImagen.setDialogTitle("Seleccionar imagen");
            selectorImagen.setFileFilter(new FileNameExtensionFilter("Imágenes del sistema",
                                                                     "jpg", "jpeg", "svg", "png", "tiff", "gif"));
            int opcion = selectorImagen.showOpenDialog(null);

            //En caso de aceptar, accedemos al archivo de imagen y lo ponemos en el JTextPane seleccionado:
            if (opcion == JFileChooser.APPROVE_OPTION)
            {
                File archivo = selectorImagen.getSelectedFile();
                ImageIcon imagenAInsertar = new ImageIcon(archivo.getAbsolutePath());
                this.listaDocumentos.get(indice).insertIcon(imagenAInsertar);
            }
            else
            JOptionPane.showMessageDialog(null, 
                                          "No ha seleccionado ninguna imagen para su inserción en el documento.",
                                          "AVISO",
                                          JOptionPane.INFORMATION_MESSAGE);
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void insertarTabla(int indice, Object fuente, Object tamano)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //En primer lugar, creamos un JOptionPane para saber el número de filas y columnas:
            JPanel panelDatos = new JPanel(new GridLayout(2, 2));
            JLabel etqFilas = new JLabel("Nº de filas: ");
            JSpinner selectorFilas = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
            JLabel etqColumnas = new JLabel("Nº de columnas: ");
            JSpinner selectorColumnas = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

            panelDatos.add(etqFilas);
            panelDatos.add(selectorFilas);
            panelDatos.add(etqColumnas);
            panelDatos.add(selectorColumnas);

            int opcion = JOptionPane.showConfirmDialog(null, 
                                                       panelDatos, 
                                                       "Especificar tamaño de tabla", 
                                                       JOptionPane.OK_CANCEL_OPTION, 
                                                       JOptionPane.PLAIN_MESSAGE);

            if (opcion == JOptionPane.OK_OPTION)
            {
                //Obtenemos los valores introducidos por el usuario:
                int numFilas = (int) selectorFilas.getValue();
                int numColumnas = (int) selectorColumnas.getValue();
                
                //Creamos una JTable con las filas y columnas pedidas por el usuario:
                TableModel modelo = new AbstractTableModel() {
                    @Override
                    public int getRowCount() { return numFilas; }

                    @Override
                    public int getColumnCount() { return numColumnas; }

                    @Override
                    public Object getValueAt(int row, int col) { return numFilas * numColumnas; }
                    
                };
                JTable tabla = new JTable(modelo);
                
                //Insertamos el JTable en su respectivo panel:
                this.listaDocumentos.get(indice).add(new JScrollPane(tabla), BorderLayout.CENTER);
            }
            else
                JOptionPane.showMessageDialog(null, 
                                              "No ha insertado ningún dato con el que insertar la tabla en el documento.",
                                              "AVISO",
                                              JOptionPane.INFORMATION_MESSAGE);
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public Action cortarTexto()
    {
        return new StyledEditorKit.CutAction();
    }
    
    public Action copiarTexto()
    {
        return new StyledEditorKit.CopyAction();
    }
    
    public Action pegarTexto()
    {
        return new StyledEditorKit.PasteAction();
    }
    
    public Action alinearTextoIzquierda()
    {
        return new StyledEditorKit.AlignmentAction("Izquierda", StyleConstants.ALIGN_LEFT);
    }
    
    public Action alinearTextoDerecha()
    {
        return new StyledEditorKit.AlignmentAction("Derecha", StyleConstants.ALIGN_RIGHT);
    }
    
    public Action centrarTexto()
    {
        return new StyledEditorKit.AlignmentAction("Centro", StyleConstants.ALIGN_CENTER);
    }
    
    public Action justificarTexto()
    {
        return new StyledEditorKit.AlignmentAction("Justificado", StyleConstants.ALIGN_JUSTIFIED);
    }
    
    public Action escribirNegrita()
    {
        return new StyledEditorKit.BoldAction();
    }
    
    public Action escribirCursiva()
    {
        return new StyledEditorKit.ItalicAction();
    }
    
    public Action escribirSubrayado()
    {
        return new StyledEditorKit.UnderlineAction();
    }
    
    public void escribirSuperindice(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());

            //Creamos el estilo de superindice para el texto:
            StyleConstants.setSuperscript(atributos, true);

            //Damos los nuevos atributos al texto:
            this.listaDocumentos.get(indice).setCharacterAttributes(atributos, false);
            
            //Ponemos el foco en el documento para mejor UX:
            this.listaDocumentos.get(indice).requestFocus();
        }
    }
    
    public void escribirSubindice(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());

            //Creamos el estilo de subindice para el texto:
            StyleConstants.setSubscript(atributos, true);

            //Damos los nuevos atributos al texto:
            this.listaDocumentos.get(indice).setCharacterAttributes(atributos, false);
            
            //Ponemos el foco en el documento para mejor UX:
            this.listaDocumentos.get(indice).requestFocus();
        }
    }
    
    public void cerrarTodosDocumentos()
    {
        this.listaDocumentos.clear();
        this.listaArchivosAbiertos.clear();
        this.listaManager.clear();
        this.contador = 1;
    }
}
