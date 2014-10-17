import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * Created by stirlingcrow on 10/17/2014.
 */
public class main
{

    public static void main(String args[]) throws Exception
    {
        System.out.println("Hey there!");
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));


        ArrayList<File> fileList = listFilesForFolder(new File(System.getProperty("user.dir").toString()));

        System.out.println("Number of pics: " + fileList.size());

        Collections.sort(fileList, new SortForFileNumber());

        PDDocument document = new PDDocument();

        int x = 0;
        for (File pic : fileList)
        {
            PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
            document.addPage(page);

            BufferedImage img = ImageIO.read(new File(fileList.get(x).getName()));
            System.out.println(fileList.get(x).getName());
            PDXObjectImage ximage = new PDJpeg(document, img, 1.0f);
            PDPageContentStream contentStream = new PDPageContentStream(document, page, true, false);
            Dimension scaledDim = getScaledDimension(new Dimension(ximage.getWidth(),  ximage.getHeight()), page.getMediaBox().createDimension());
            contentStream.drawXObject(ximage, 1, 1, scaledDim.width, scaledDim.height);
            contentStream.close();
            x++;

        }
        // Create a new blank page and add it to the document
//        PDPage blankPage = new PDPage();
//
//        PDPageContentStream cos = new PDPageContentStream(document, blankPage);
//
//        document.addPage( blankPage );
//
//        PDPage page2 = new PDPage(PDPage.PAGE_SIZE_A4);
//        document.addPage(page2);
//
//        cos = new PDPageContentStream(document, page2);
//
//        try
//        {
//            BufferedImage awtImage = ImageIO.read(new File("Simple.jpg"));
//            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
//            float scale = 0.5f; // alter this value to set the image size
//            cos.drawXObject(ximage, 50, 50, ximage.getWidth()*scale, ximage.getHeight()*scale);
//
//            cos.close();
//        }
//        catch (FileNotFoundException fnfex)
//        {
//            System.out.println("No image for you");
//        }
//
//        PDPage page3 = new PDPage(PDPage.PAGE_SIZE_A4);
//        document.addPage(page3);
//
//        //cos = new PDPageContentStream(document, page3);
//
//        BufferedImage img = ImageIO.read(new File("Simple.jpg"));
//        PDXObjectImage ximage = new PDJpeg(document, img, 1.0f);
//        PDPageContentStream contentStream = new PDPageContentStream(document, page3, true, false);
//        Dimension scaledDim = getScaledDimension(new Dimension(ximage.getWidth(),  ximage.getHeight()), page3.getMediaBox().createDimension());
//        contentStream.drawXObject(ximage, 1, 1, scaledDim.width, scaledDim.height);
//        contentStream.close();


        // Save the newly created document
        try
        {
            document.save("BlankPage.pdf");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (COSVisitorException e)
        {
            e.printStackTrace();
        }

        // finally make sure that the document is properly
        // closed.
        try
        {
            document.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary)
    {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }


    public static ArrayList<File> listFilesForFolder(final File folder)
    {
        ArrayList<File> listOfFiles = new ArrayList<File>();

        for (final File fileEntry : folder.listFiles())
        {
            if (fileEntry.getName().contains(".jpg"))
            {
                listOfFiles.add(fileEntry);
            }

//            if (fileEntry.isDirectory()) {
//                listFilesForFolder(fileEntry);
//            } else {
//                System.out.println(fileEntry.getName());
//            }
        }

        return listOfFiles;
    }


    private static class SortForFileNumber implements java.util.Comparator<File>
    {

        @Override
        public int compare(File file1, File file2)
        {
            Integer result = null;

            String file1Name = file1.getName();
            String file2Name = file2.getName();

            String delimiters = "[_]";

            String[] file1Array = file1Name.split(delimiters);
            String[] file2Array = file2Name.split(delimiters);


//            int y = 0;
//            for (String x : file1Array){
//                System.out.println("It is " + x + "Place: " + y);
//                y++;
//            }

            if (file1Array == null || file2Array == null)
            {
                System.out.println("One of these is null");
                System.out.println("File 1 is " + file1Array + "  and File 2 is " + file2Array);
            }


            int file1Num = Integer.parseInt(file1Array[1]);
            int file2Num = Integer.parseInt(file2Array[1]);






            if (file1Num == file2Num)
            {
                result = 0;

            }
            else if (file1Num < file2Num)
            {
                result = -1;

            }
            else
            {
                result = 1;

            }


            System.out.println("The result is" + result);
            return result;

//            if (result != null && result != 0 )
//            {
//                return result;
//            }
//            else
//            {
//                return person1.compareTo(person2);
//            }

        }

    }

}
