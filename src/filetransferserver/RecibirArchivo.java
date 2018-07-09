package filetransferserver;

import java.net.*;
import java.io.*;
import java.util.*;

public class RecibirArchivo {

    private ServerSocket servidor = null;
    public int tam;
    public String nombreArchivo;

    public RecibirArchivo() throws IOException {
        // Creamos socket servidor escuchando en el mismo puerto donde se comunica el cliente

        servidor = new ServerSocket(4480);
        //habilitar puertos en el sistema por si no funciona
        System.out.println("Esperando imágenes");
    }

    public void iniciarServidor() {
        while (true) {

            try {
                // Creamos el socket que atendera el servidor
                Socket cliente = servidor.accept();

                // Creamos flujo de entrada para leer los datos que envia el cliente 
                DataInputStream dis = new DataInputStream(cliente.getInputStream());

                // Obtenemos el nombre del archivo
                nombreArchivo = dis.readUTF().toString();

                // Obtenemos el tamaño del archivo
                tam = dis.readInt();

                System.out.println("Recibiendo imagen " + nombreArchivo);

                // Creamos flujo de salida, este flujo nos sirve para 
                // indicar donde guardaremos el archivo
                FileOutputStream fos = new FileOutputStream("/Users/Dayana/Documentos/" + nombreArchivo);
                BufferedOutputStream out = new BufferedOutputStream(fos);
                BufferedInputStream in = new BufferedInputStream(cliente.getInputStream());

                // Creamos el array de bytes para leer los datos del archivo
                byte[] buffer = new byte[tam];

                // Obtenemos el archivo mediante la lectura de bytes enviados
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) in.read();
                }

                // Escribimos el archivo 
                out.write(buffer);

                // Cerramos flujos
                out.flush();
                in.close();
                out.close();
                cliente.close();

                System.out.println("Imagen recibida " + nombreArchivo);

            } catch (Exception e) {
                System.out.println("Recibir: " + e.toString());
            }
        }
    }

    // Lanzamos el servidor para la recepción de archivos
}
