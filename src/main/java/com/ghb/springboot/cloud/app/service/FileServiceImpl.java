package com.ghb.springboot.cloud.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletResponse;

import com.ghb.springboot.cloud.app.entity.Configuracion;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements IFileService{

    private IConfiguracionService configuracionService;
    private ICifradoService cifradoService;

    @Autowired
    public FileServiceImpl(IConfiguracionService configuracionService, ICifradoService cifradoService) {
        this.configuracionService = configuracionService;
        this.cifradoService = cifradoService;
    }

    @Override
    public String subirArchivo(MultipartFile file,String dir) {
        
        Configuracion root=configuracionService.findByParametro("ROOT");
        Configuracion keyParam=configuracionService.findByParametro("KEY_FILE");
        Configuracion ivParam=configuracionService.findByParametro("IV_FILE");
        Integer bkFile=Integer.parseInt(configuracionService.findByParametro("BK_FILE").getValor());
        Long size=configuracionService.tamanoFileUpload()*1024*1024;

        if(file.isEmpty())
            return "Selecciona un archivo a subir";

        if(keyParam.getValor().equals("0") || ivParam.getValor().equals("0"))
            return "Para subir archivos el administrador debe generar los archivos KEY y IV.";

        if(file.getSize()>size)
            return "El tamaño máximo establecido es "+configuracionService.tamanoFileUpload()+" MB";

        try {
            byte[] bytes=file.getBytes();
            
            if(!new File(root.getValor()+dir+file.getOriginalFilename()).exists())
            {
                Path ruta=Paths.get(root.getValor()+dir+file.getOriginalFilename());            
                Files.write(ruta,bytes);

                cifradoService.encriptar(root.getValor()+dir+file.getOriginalFilename());

                return "El archivo "+file.getOriginalFilename()+" se ha subido exitosamente";
            }
            else
            {
                String[] parts = file.getOriginalFilename().split("\\.");
                Path source=null;
                
                if(parts.length!=1)
                {
                    for(int i=bkFile-2;i>=0;i--)
                    {
                        if(i==0)
                        {
                            source=Paths.get(root.getValor()+dir+file.getOriginalFilename());
                            Files.move(source,source.resolveSibling(parts[0]+"_"+(i+1)+"."+parts[1]),
                                StandardCopyOption.REPLACE_EXISTING);

                            break;
                        }
                        if(new File(root.getValor()+dir+parts[0]+"_"+i+"."+parts[1]).exists())
                        {
                            source=Paths.get(root.getValor()+dir+parts[0]+"_"+i+"."+parts[1]);
                            Files.move(source,source.resolveSibling(parts[0]+"_"+(i+1)+"."+parts[1]),
                                StandardCopyOption.REPLACE_EXISTING);
                        }
                        

                    }
                }
                else
                {
                    for(int i=bkFile-2;i>=0;i--)
                    {
                        if(i==0)
                        {
                            source=Paths.get(root.getValor()+dir+file.getOriginalFilename());
                            Files.move(source,source.resolveSibling(file.getOriginalFilename()+"_"+(i+1)),
                                StandardCopyOption.REPLACE_EXISTING);

                            break;
                        }
                        if(new File(root.getValor()+dir+file.getOriginalFilename()+"_"+i).exists())
                        {
                            source=Paths.get(root.getValor()+dir+file.getOriginalFilename()+"_"+i);
                            Files.move(source,source.resolveSibling(file.getOriginalFilename()+"_"+(i+1)),
                                StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }

                    Path ruta=Paths.get(root.getValor()+dir+file.getOriginalFilename());            
                    Files.write(ruta,bytes);

                    cifradoService.encriptar(root.getValor()+dir+file.getOriginalFilename());

                    return "El archivo "+file.getOriginalFilename()+" se ha actualizado exitosamente";
                

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "Error al subir archivo";
        }
        
    }

    /*@Override
    public ResponseEntity<Object> descargasArchivo(String dir,String archivo) {
        
        Configuracion root=configuracionService.findByParametro("ROOT");
        
        if(System.getProperty("os.name").contains("Windows"))
            dir=dir.replace("/", "\\");

        ResponseEntity<Object> responseEntity=null;
        try {
            
            cifradoService.desencriptar(root.getValor()+dir+archivo);
            
            //File file = new File(root.getValor()+dir+archivo+".unlock");
            Path f=Paths.get(root.getValor()+dir+archivo+".unlock");
            InputStreamResource resource= new InputStreamResource(new FileInputStream(f.toFile()));
            //f.getFileSystem().close();
            
            HttpHeaders headers=new HttpHeaders();

            headers.add("Content-Disposition", 
            String.format("attachment; filename=\"%s\"", archivo));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            responseEntity=ResponseEntity.ok().headers(headers)
            .contentLength(f.toFile().length())
            .contentType(MediaType.parseMediaType("application/txt"))
            .body(resource);
            
            f=null;
            resource=null;
            headers=null;
            return responseEntity;
            //Files.delete(Paths.get(root.getValor()+dir+archivo+".unlock"));
            //Files.delete(Paths.get(root.getValor()+dir+archivo+".unlock"));
            //Files.delete(Paths.get(root.getValor()+dir+archivo+".unlock"));

        } catch (Exception e) {
            return responseEntity;
        }
        finally
        {
            try {
                responseEntity=null;
                Files.delete(Paths.get(root.getValor()+dir+archivo+".unlock"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        
    }*/
    
    @Override
    public void descargasArchivo(String dir,String archivo, HttpServletResponse response)
    {
        Configuracion root=configuracionService.findByParametro("ROOT");
        
        if(System.getProperty("os.name").contains("Windows"))
            dir=dir.replace("/", "\\");

        cifradoService.desencriptar(root.getValor()+dir+archivo);
        Path file = Paths.get(root.getValor()+dir+archivo+".unlock");

        response.setContentType("application/txt");
        response.setHeader("Content-disposition", "attachment; filename=" + archivo);

        try {
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file.toFile());

            IOUtils.copy(in, out);
            out.close();
            in.close();
            Files.delete(file);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
