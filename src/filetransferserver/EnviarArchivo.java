/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransferserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import filetransferserver.Enviar_imagen;
import javax.swing.JOptionPane;

/**
 *
 * @author cristian mendoza
 */
public class EnviarArchivo {
    public String ip;
    public static void main(String args[]) {

    }
    private String nombreArchivo = "";

    public EnviarArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @SuppressWarnings("empty-statement")
    public void enviarArchivo() {
        int con_win = 1, lim = 30, umbral = 20;
        String congestion = "Control de Congestión", fast = "Restransmisión rápida", slow = "Slow Start";
        grafica graf;
        graf = new grafica();
        graf.plotGraph();
        try {
            int seg = 0, conta = 1, dato = 0, send = 0;
            // Creamos la direccion IP de la maquina que recibira el archivo
            Enviar_imagen vr = new Enviar_imagen();
            InetAddress direccion = InetAddress.getByName(ip);

            //habilitar puertos en el sistema operativo por si falla
            // Creamos el Socket con la direccion y elpuerto de comunicacion
            int puerto = 4480;

            //puerto= Integer.parseInt(vr.puerto);
            
            System.out.println("dirección: "+ip);
            Socket socket = new Socket(direccion, puerto);
            socket.setSoTimeout(2000);
            socket.setKeepAlive(true);

            String p = "4480";

            // Creamos el archivo que vamos a enviar
            File archivo = new File(nombreArchivo);

            // Obtenemos el tamaño del archivo
            //RENO
            int tamañoArchivo = (int) archivo.length();

            int ima = tamañoArchivo;
            int[] imagen = new int[10];
            int frag = tamañoArchivo / 10; //fragmentar archivo
            for (int i = 0; i < 10; i++) {
                imagen[i] = frag;
            }
            System.out.println(frag);
            int j = 0;

            for (con_win = 1; seg < imagen[j];) {

                if (seg + con_win > imagen[j]) {
                    seg = imagen[j];
                } else {
                    if (con_win >= lim) {
                        System.out.println(congestion);
                        con_win = con_win / 2;
                        umbral = con_win;
                    } else {
                        if (con_win >= umbral || dato == 1) {
                            System.out.println(fast);
                            con_win++;
                            seg += con_win;
                            send = 1;
                        } else {
                            if (con_win * 2 > lim) {
                                graf.plotTcp(conta, con_win * 2, umbral, lim);
                                System.out.println(congestion);
                                umbral = con_win;

                                con_win += 1;
                                dato = 1;
                                seg += con_win;
                                send = 1;
                            } else {
                                System.out.println(slow);

                                con_win = con_win * 2;
                                seg += con_win;
                                send = 1;
                            }
                        }
                    }
                }

                graf.plotTcp(conta, con_win, umbral, lim);
                imagen[j] = imagen[j] - 1000;
                conta++;
                Thread.sleep(100);
                System.out.println(conta + "    " + con_win + "    " + umbral + "    " + lim);
                System.out.println(imagen[j]);

            }
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Enviando Imagen: " + archivo.getName());

            // Enviamos el nombre del archivo 
            dos.writeUTF(archivo.getName());

            // Enviamos el tamaño del archivo
            dos.writeInt(tamañoArchivo);

            // Creamos flujo de entrada para realizar la lectura del archivo en bytes
            FileInputStream fis = new FileInputStream(nombreArchivo);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // Creamos el flujo de salida para enviar los datos del archivo en bytes
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

            // Creamos un array de tipo byte con el tamaño del archivo 
            byte[] buffer = new byte[ima];
            bis.read(buffer);

            // Realizamos el envio de los bytes que conforman el archivo
            for (int i = 0; i < buffer.length; i++) {
                bos.write(buffer[i]);
            }
            // Cerramos socket y flujos
            bis.close();
            bos.close();

            // Leemos el archivo y lo introducimos en el array de bytes 
            System.out.println(ima);

            socket.close();
            // Creamos el flujo de salida, este tipo de flujo nos permite 
            // hacer la escritura de diferentes tipos de datos tales como
            // Strings, boolean, caracteres y la familia de enteros, etc.

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e.toString());
        }

    }

}
