package dev.acobano.editor.texto.java.controlador;

import dev.acobano.editor.texto.java.vista.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.UndoManager;
import org.odftoolkit.simple.TextDocument;

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
        this.contador = 0;
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
            this.contador = 0;
        
        this.contador++;
        panelPestanas.crearPestana("Documento " + this.contador, doc, this.listaDocumentos, this.listaManager, piePagina);
        panelPestanas.setSelectedIndex(this.listaDocumentos.size());
        panelPestanas.setVisible(true);
        this.listaDocumentos.add(panelTexto);
        this.listaManager.add(manager);
    }
    
    public void crearDocumentoHTML(PanelConPestanasCerrable panelPestanas, 
                               JPanel piePagina, 
                               JLabel etqCursor, 
                               JLabel etqDoc,
                               JPopupMenu menuContextual)
    {
        JTextPane panelTexto = new JTextPane();
        UndoManager manager = new UndoManager();
        
        //Cambiamos el tipo de contenido del JTextPane a HTML:
        panelTexto.setContentType("text/html");
        PanelDocumento doc = new PanelDocumento(panelTexto, manager, etqCursor, etqDoc, menuContextual);
          
        try
        {          
            //Empleamos un Input Stream para traer la información del archivo:
            FileReader lectorArchivo = new FileReader("src/main/resources/templates/html.txt");
            BufferedReader flujoEntrada = new BufferedReader(lectorArchivo);
            String linea = "";
        
            //Leemos línea a línea del archivo y lo almacenamos en el String:
            while (linea != null)
            {
                linea = flujoEntrada.readLine();

                if (linea != null)
                {
                    Document d = panelTexto.getDocument();
                    d.insertString(d.getLength(), linea + "\n", null);
                }
            }
            
            //Cerramos los streams:
            flujoEntrada.close();
            lectorArchivo.close();
        }
        catch (Exception ex) {}
        
        if (this.listaDocumentos.isEmpty())
            this.contador = 0;
                              
        this.contador++;
        panelPestanas.crearPestana("Documento " + this.contador, doc, this.listaDocumentos, this.listaManager, piePagina);
        panelPestanas.setSelectedIndex(this.listaDocumentos.size());
        panelPestanas.setVisible(true);                    
        this.listaDocumentos.add(panelTexto);
        this.listaManager.add(manager);
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
                    FileReader lectorArchivo = new FileReader(f.getPath());
                    BufferedReader flujoEntrada = new BufferedReader(lectorArchivo);
                    String linea = "";
                    
                    //Leemos línea a línea del archivo y lo almacenamos en el String:
                    while (linea != null)
                    {
                        linea = flujoEntrada.readLine();
                        
                        if (linea != null)
                        {
                            Document d = panelTexto.getDocument();
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
                
                JOptionPane.showMessageDialog(null, 
                                          "Ha ocurrido un error inesperado en el proceso de lectura.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(null,
                                              "No ha seleccionado ningún archivo que abrir en este momento.",
                                              "AVISO",
                                              JOptionPane.INFORMATION_MESSAGE);
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
                    guardadorArchivo.setDialogTitle("GUARDAR COMO ARCHIVO TXT");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de texto plano TXT", "txt"));
                    break;
                }
                case RTF: 
                {
                    guardadorArchivo.setDialogTitle("GUARDAR COMO ARCHIVO RTF");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de texto con formato RTF", "rtf"));                
                    break;
                }
                case PDF:
                {
                    guardadorArchivo.setDialogTitle("GUARDAR COMO ARCHIVO PDF");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de almacenamiento digital PDF", "pdf"));
                    break;
                }
                case DOCX:
                {
                    guardadorArchivo.setDialogTitle("GUARDAR COMO ARCHIVO DOC");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de texto Microsoft Word", "doc", "docx"));
                    break;
                }
                case ODT:
                {
                    guardadorArchivo.setDialogTitle("GUARDAR COMO ARCHIVO ODT");
                    guardadorArchivo.setFileFilter(new FileNameExtensionFilter("Archivos de texto OpenOffice Writer", "odt"));
                    break;
                }
                //Caso por defecto, se presiona el botón 'Guardar como'
                case INDEFINIDO:
                    guardadorArchivo.setDialogTitle("Guardar como...");              
            }
        
            //El usuario escoge una ruta y presiona un botón del JFileChooser:
            int opcion = guardadorArchivo.showSaveDialog(null);

            //Si ha presionado el botón 'Aceptar', entonces se inicia el proceso de guardado:
            if (opcion == JFileChooser.APPROVE_OPTION)
            {
                File ficheroAGuardar = guardadorArchivo.getSelectedFile();
                String rutaFichero = ficheroAGuardar.getAbsolutePath();
            
                //Verificamos la extensión del archivo y enviamos a los métodos especializados:
                switch (formato)
                {
                    case TXT:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".txt"))
                            ficheroAGuardar = new File(rutaFichero + ".txt");
                    
                        this.escribirArchivo(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }                    
                    case RTF:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".rtf"))
                            ficheroAGuardar = new File(rutaFichero + ".rtf");
                    
                        this.guardarComoRTF(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }                   
                    case PDF:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".pdf"))
                            ficheroAGuardar = new File(rutaFichero + ".pdf");
                    
                        this.guardarComoPDF(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }
                    case DOCX:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".doc") &&
                            !rutaFichero.toLowerCase().endsWith(".docx"))
                                ficheroAGuardar = new File(rutaFichero + ".docx");
                    
                        this.guardarComoDOCX(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }
                    case ODT:
                    {
                        if (!rutaFichero.toLowerCase().endsWith(".odt"))
                                ficheroAGuardar = new File(rutaFichero + ".odt");
                    
                        this.guardarComoODT(panelPestanas.getSelectedIndex(), ficheroAGuardar);
                        break;
                    }
                    //Caso por defecto, se llama al flujo de salida básico de Java:
                    case INDEFINIDO:
                        this.escribirArchivo(this.listaArchivosAbiertos.size(), ficheroAGuardar);
                }
                 
                //Cambiamos el título de la pestaña del documento por el nombre del archivo:
                panelPestanas.getLabelCabecera(panelPestanas.getSelectedIndex()).setText(ficheroAGuardar.getName());                
                   
                //Introducimos el fichero en nuestro ArrayList:
                this.listaArchivosAbiertos.add(ficheroAGuardar);

                //Avisamos al usuario que el procedimiento de guardado ha sido exitoso:
                JOptionPane.showMessageDialog(null, 
                                              "El documento " + ficheroAGuardar.getName() +
                                              " ha sido guardado exitosamente en la ruta:\n" + rutaFichero, 
                                              "ARCHIVO GUARDADO", 
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        }
        //En caso de no tener ningún archivo abierto, se avisa al usuario:
        else
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser guardado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
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
    
    public void guardarComoODT(int indice, File ficheroAGuardar)
    {
        try
        {
            //Creamos un nuevo documento ODT:
            TextDocument doc = TextDocument.newTextDocument();
            
            //Agregamos el contenido del JTextPane al documento:
            doc.addParagraph(this.listaDocumentos.get(indice).getText());
            
            //Guardamos el documento como un archivo ODT:
            doc.save(ficheroAGuardar);
        }
        catch (Exception ex)
        {
            Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ex);
            
            JOptionPane.showMessageDialog(null, 
                                          "Ha ocurrido un error inesperado en el proceso de guardado.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deshacerCambios(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            if (this.listaManager.get(indice).canUndo())
                this.listaManager.get(indice).undo();
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void rehacerCambios(int indice)
    {
        if (!this.listaDocumentos.isEmpty()) 
        {
            if (this.listaManager.get(indice).canRedo())
                this.listaManager.get(indice).redo();
        }
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
        if (!this.listaDocumentos.isEmpty())
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
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void cambiarColorResaltado(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
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
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void cambiarFuente(int indice, Object nombreFuente)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());

            //Cambiamos la familia de la fuente del texto:
            StyleConstants.setFontFamily(atributos, String.valueOf(nombreFuente));

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
    
    public void cambiarTamanoFuente(int indice, Object tamanoFuente)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());

            //Cambiamos el tamaño de la fuente del texto:
            StyleConstants.setFontSize(atributos, Integer.parseInt(String.valueOf(tamanoFuente)));

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
    
    public void tacharTexto(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());

            //Cambiamos el atributo para que tache el texto:
            StyleConstants.setStrikeThrough(atributos, true);

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
                
                //Creamos una JTable con las filas y columnas obtenidas:
                JTable tabla = new JTable(numFilas, numColumnas);
                
                //Cambiamos los valores de estilo para que sea más visible:
                tabla.setBorder(new LineBorder(Color.BLACK));
                tabla.setFont(new Font((String) fuente, Font.PLAIN, (int) tamano));
                tabla.getColumnModel().getColumn(0).setPreferredWidth(90);
                
                //Insertamos la tabla en el documento:
                this.listaDocumentos.get(indice).insertComponent(tabla);
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
                                          "No hay ningún documento abierto actualmente en el que poder insertar una tabla.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void insertarHiperenlace(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Ejecutamos un JOptionPane con un JTextField donde el usuario
            //pueda ingresar la URL del hiperenlace a insertar:
            JTextField textFieldURL = new JTextField();
        
            Object[] mensaje = {"Ingrese la URL del hiperenlace: ", textFieldURL};

            int opcion = JOptionPane.showOptionDialog(null, 
                                                      mensaje, 
                                                      "Buscar texto", 
                                                      JOptionPane.OK_CANCEL_OPTION, 
                                                      JOptionPane.PLAIN_MESSAGE,
                                                      null, null, null);

            if (opcion == JOptionPane.OK_OPTION)
            {
                String textoURL = textFieldURL.getText();
                
                if (!textoURL.equals(""))
                {
                    //Creamos un HyperlinkListener y lo asignamos al JTextPane activo:
                    this.listaDocumentos.get(indice).addHyperlinkListener(e -> {
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                        {
                            String url = e.getURL().toString();
                            
                            try 
                            {
                                Desktop.getDesktop().browse(new URI(url));
                            } 
                            catch (IOException | URISyntaxException ex) {
                                Logger.getLogger(GestorEventosEditor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    
                    //Insertamos el hiperenlace en el JTextPane mediante la clase 'SimpleAttributeSet':
                    SimpleAttributeSet atributo = new SimpleAttributeSet();
                    StyleConstants.setForeground(atributo, Color.BLUE);
                    StyleConstants.setUnderline(atributo, true);
                    this.listaDocumentos.get(indice).setCharacterAttributes(atributo, false);
                    
                    try {
                        HyperlinkEvent evento = new HyperlinkEvent(this.listaDocumentos.get(indice),
                                HyperlinkEvent.EventType.ACTIVATED,
                                new URL(textoURL));
                    
                        //Agregamos el hiperenlace:
                        this.listaDocumentos.get(indice)
                                .getStyledDocument()
                                .insertString(this.listaDocumentos
                                              .get(indice)
                                              .getStyledDocument()
                                              .getLength(),
                                        evento.getDescription(),
                                        atributo);
                    }
                    catch (MalformedURLException me)
                    {
                        JOptionPane.showMessageDialog(null, 
                                                      "La URL que ha introducido no es válida.", 
                                                      "ERROR",
                                                      JOptionPane.ERROR_MESSAGE);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(GestorEventosEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, 
                                                  "No es posible insertar un hiperenlace sin URL.", 
                                                  "ERROR",
                                                  JOptionPane.WARNING_MESSAGE);
            }
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente en el que poder insertar un hiperenlace.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void cortarTexto(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
            this.listaDocumentos.get(indice).cut();
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void copiarTexto(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
            this.listaDocumentos.get(indice).copy();
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void pegarTexto(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
            this.listaDocumentos.get(indice).paste();
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void alinearTextoIzquierda(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
            
            //Creamos el estilo de cursiva para el texto:
            StyleConstants.setAlignment(atributos, StyleConstants.ALIGN_LEFT);

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
    
    public void alinearTextoDerecha(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
            
            //Creamos el estilo de cursiva para el texto:
            StyleConstants.setAlignment(atributos, StyleConstants.ALIGN_RIGHT);

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
    
    public void centrarTexto(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
            
            //Creamos el estilo de cursiva para el texto:
            StyleConstants.setAlignment(atributos, StyleConstants.ALIGN_CENTER);

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
    
    public void justificarTexto(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
            
            //Creamos el estilo de cursiva para el texto:
            StyleConstants.setAlignment(atributos, StyleConstants.ALIGN_JUSTIFIED);

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
    
    public void escribirNegrita(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
            
            //Creamos el estilo de cursiva para el texto:
            StyleConstants.setBold(atributos, true);

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
    
    public void escribirCursiva(int indice)
    {        
        if (!this.listaDocumentos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
            
            //Creamos el estilo de cursiva para el texto:
            StyleConstants.setItalic(atributos, true);

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
    
    public void escribirSubrayado(int indice)
    {
        if (!this.listaArchivosAbiertos.isEmpty())
        {
            //Obtenemos los atributos actuales del texto seleccionado:
            SimpleAttributeSet atributos = new SimpleAttributeSet(this.listaDocumentos.get(indice).getCharacterAttributes());
            
            //Creamos el estilo de subrayado para el texto:
            StyleConstants.setUnderline(atributos, true);

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
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
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
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser modificado.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void buscarEnTexto(int indice)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            //Ejecutamos un JOptionPane con un JTextField donde el usuario
            //pueda ingresar la palabra o texto a buscar:
            JTextField textFieldBuscar = new JTextField();
        
            Object[] mensaje = {"Ingrese el texto a buscar: ", textFieldBuscar};

            int opcion = JOptionPane.showOptionDialog(null, 
                                                      mensaje, 
                                                      "BUSCAR TEXTO", 
                                                      JOptionPane.OK_CANCEL_OPTION, 
                                                      JOptionPane.PLAIN_MESSAGE,
                                                      null, null, null);
            
            if (opcion == JOptionPane.OK_OPTION)
            {
                String textoBuscar = textFieldBuscar.getText();
                String textoDocumento = this.listaDocumentos.get(indice).getText();
                
                //En caso de que la palabra a buscar esté en el documento,
                //la remarcaremos con el ratón para que el usuario la encuentre:
                if (textoDocumento.contains(textoBuscar))
                {
                    int cursorInicio = textoDocumento.indexOf(textoBuscar);
                    int cursorFinal = cursorInicio + textoBuscar.length();
                    this.listaDocumentos.get(indice).select(cursorInicio, cursorFinal);
                    this.listaDocumentos.get(indice).requestFocus();
                }
                else
                JOptionPane.showMessageDialog(null, 
                                              "La palabra que ha introducido para buscar no aparece en el documento seleccionado.", 
                                              "PALABRA NO ENCONTRADA", 
                                              JOptionPane.INFORMATION_MESSAGE);
            }
                //En caso contrario, informamos al usuario que no existe:
                else
                    JOptionPane.showMessageDialog(null, 
                                                  "No ha seleccionado ningún texto a buscar en el documento.", 
                                                  "AVISO", 
                                                  JOptionPane.INFORMATION_MESSAGE);
            
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente al que pueda buscar ningún texto.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void imprimirDocumento(PanelDocumento pDoc)
    {
        if (!this.listaDocumentos.isEmpty())
        {
            PrinterJob flujoImpresion = PrinterJob.getPrinterJob();
            flujoImpresion.setPrintable(pDoc);
            
            if (flujoImpresion.printDialog())
            {
                try
                {
                    flujoImpresion.print();
                }
                catch (PrinterException pe)
                {
                    JOptionPane.showMessageDialog(null, 
                                                  "Ha ocurrido un error inesperado en el proceso de impresión.", 
                                                  "ERROR", 
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
            else
                JOptionPane.showMessageDialog(null, 
                                              "El proceso de impresión ha sido cancelado.", 
                                              "AVISO", 
                                              JOptionPane.INFORMATION_MESSAGE);
                
        }
        else
            //En caso de no tener ningún archivo abierto, se avisa al usuario:
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente al que pueda buscar ningún texto.", 
                                          "ERROR", 
                                          JOptionPane.WARNING_MESSAGE);
    }
    
    public void cerrarTodosDocumentos()
    {
        this.listaDocumentos.clear();
        this.listaArchivosAbiertos.clear();
        this.listaManager.clear();
        this.contador = 1;
    }
}
