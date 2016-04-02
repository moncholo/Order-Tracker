package Files;

import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Model.Request;
import Model.RequestHandler;
import Model.Response;

/**
 * Created by lotus on 2/04/16.
 */
public class FileHandler {

    private int firstImageId;

    public FileHandler()
    {
    }

    public int getFirstImageId()
    {
        return firstImageId;
    }

    public void downloadFile(String path,String indentifierFile)
    {
        try {

            Request request = new Request("GET", "GetImagen.php?id_producto=" + indentifierFile);
            Response resp = new RequestHandler().sendRequest(request);

            firstImageId = resp.getJsonArray().getJSONObject(0).getInt("id");

            for (int i=0;i<resp.getJsonArray().length();i++) {
                File file = new File("/mnt/sdcard/Download/" + resp.getJsonArray().getJSONObject(i).getString("id") + ".jpg");
                if (!file.exists())
                    saveFile(file, resp.getJsonArray().getJSONObject(i).getString("imagen_base64"));
            }

        }
        catch(Exception e)
        {
        }
    }

    private void saveFile(File file,String textoBytes) throws IOException {
        byte[] bytes = Base64.decode(textoBytes, Base64.DEFAULT);
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(bytes);
        fop.flush();
        fop.close();
    }


}
