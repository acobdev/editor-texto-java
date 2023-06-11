package dev.acobano.editor.texto.java.controlador;

import dev.acobano.editor.texto.java.vista.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author acobano
 */
public class GestorEventosEditor 
{   
    //ATRIBUTOS:
    private ArrayList<JTextPane> listaDocumentos;
    private ArrayList<File> listaArchivosAbiertos;
    private int contador;
    
    
    //CONSTRUCTOR:
    public GestorEventosEditor()
    {
        this.listaDocumentos = new ArrayList<>();
        this.listaArchivosAbiertos = new ArrayList<>();
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
    public void crearNuevoDocumento(PanelConPestanasCerrable panelPestanas)
    {
        JTextPane panelTexto = new JTextPane();
        PanelDocumento doc = new PanelDocumento(panelTexto);
        
        if (this.listaDocumentos.isEmpty())
            this.contador = 1;
        
        panelPestanas.crearPestana("Documento " + this.contador, doc, this.listaDocumentos);
        panelPestanas.setSelectedIndex(this.listaDocumentos.size());
        this.listaDocumentos.add(panelTexto);
        panelPestanas.setVisible(true);
        this.contador++;
    }
    
    public void abrirDocumento(PanelConPestanasCerrable panelPestanas)
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
                    PanelDocumento doc = new PanelDocumento(panelTexto);
                    
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
                                        
                    panelPestanas.crearPestana(f.getName(), doc, this.listaDocumentos, this.listaArchivosAbiertos, f);
                    panelPestanas.setSelectedIndex(this.listaDocumentos.size());
                    listaDocumentos.add(panelTexto);
                    panelPestanas.setVisible(true);
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
            
            for (File f : this.listaArchivosAbiertos)
                if (f.getName().equals(panelPestanas.getLabel(panelPestanas.getSelectedIndex()).getText()))
                    esNuevoGuardado = false;
            
            if (esNuevoGuardado)
            {
                JFileChooser guardadorArchivo = new JFileChooser();
                int opcion = guardadorArchivo.showSaveDialog(null);

                if (opcion == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        File archivo = guardadorArchivo.getSelectedFile();
                        this.listaArchivosAbiertos.add(archivo);
                        panelPestanas.getLabel(panelPestanas.getSelectedIndex()).setText(archivo.getName());
                        FileWriter flujoSalida = new FileWriter(archivo);
                        String textoDocumento = this.listaDocumentos.get(panelPestanas.getSelectedIndex()).getText();
                        
                        for(int i=0;i<textoDocumento.length(); i++)
                            flujoSalida.write(textoDocumento.charAt(i));
                        
                        flujoSalida.close();
                    }
                    catch(IOException ioe)
                    {
                        Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ioe);
                    }                    
                }
            //En caso de que ya se encuentre abierto, lo que haremos será sobreescribirlo:
            } else {
                try
                {
                    FileWriter flujoSalida = new FileWriter(this.listaArchivosAbiertos.get(panelPestanas.getSelectedIndex()));
                    String textoDocumento = this.listaDocumentos.get(panelPestanas.getSelectedIndex()).getText();
                        
                    for(int i=0;i<textoDocumento.length(); i++)
                        flujoSalida.write(textoDocumento.charAt(i));
                        
                    flujoSalida.close();
                }
                catch(IOException ioe)
                {
                    Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ioe);
                }
            }
        }           
        else
            JOptionPane.showMessageDialog(null, 
                                          "No hay ningún documento abierto actualmente que pueda ser guardado.", 
                                          "ERROR", 
                                          JOptionPane.ERROR_MESSAGE);
    }
    
    public void guardarDocumentoComo(PanelConPestanasCerrable panelPestanas)
    {
        JFileChooser guardadorArchivo = new JFileChooser();
        int opcion = guardadorArchivo.showSaveDialog(null);

        if (opcion == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                File archivo = guardadorArchivo.getSelectedFile();
                this.listaArchivosAbiertos.add(archivo);
                panelPestanas.getLabel(panelPestanas.getSelectedIndex()).setText(archivo.getName());
                FileWriter flujoSalida = new FileWriter(archivo);
                String textoDocumento = this.listaDocumentos.get(panelPestanas.getSelectedIndex()).getText();
                        
                for(int i=0;i<textoDocumento.length(); i++)
                    flujoSalida.write(textoDocumento.charAt(i));
                        
                flujoSalida.close();
            }
            catch(IOException ioe)
            {
                Logger.getLogger(PanelEditor.class.getName()).log(Level.SEVERE, null, ioe);
            }                    
        }
    }
}
