import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) throws Exception{
        if (args == null || args.length == 0) {
            System.out.println("输入有误！");
            return;
        }
        String fileName = args[0];
        List<List<Integer>> coloredInfo = getColoredInfo(fileName);
        System.out.println(String.format("彩色页面共%d页，具体为:\n%s",
                coloredInfo.get(0).size(),coloredInfo.get(0).toString().replaceAll("[\\[\\]]","")));
        System.out.println(String.format("黑白页面共%d页，具体为:\n%s",
                coloredInfo.get(1).size(),coloredInfo.get(1).toString().replaceAll("[\\[\\]]","")));
    }
    public static List<List<Integer>> getColoredInfo(String fileName){
        List<List<Integer>> res = new ArrayList<>();
        try {
            PDDocument coloredDoc = new PDDocument();
            PDDocument grayDoc = new PDDocument();
            res.add(new ArrayList<>());
            res.add(new ArrayList<>());
            List<Integer> coloredNums = res.get(0);
            List<Integer> grayNums = res.get(1);
            res.add(new ArrayList<>());
            File file = new File(fileName);
            PDDocument document = null;
            document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            int pagesNum = document.getNumberOfPages();
            for(int i = 0; i < pagesNum; i ++) {
                System.out.println("processing the " + (i + 1) + "th page..");
                PDPage onePage = document.getPage(i);
                BufferedImage pdfImage = renderer.renderImageWithDPI(i, 200 / 4); // Windows native DPI
                if (isColored(pdfImage)) {
                    coloredNums.add(i + 1);
                    coloredDoc.addPage(onePage);
                } else {
                    grayNums.add(i + 1);
                    grayDoc.addPage(onePage);
                }
            }
            coloredDoc.save(fileName + "_彩色部分.pdf");
            coloredDoc.close();
            grayDoc.save(fileName + "_黑白部分.pdf");
            grayDoc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public static boolean isColored(BufferedImage image) {
        int height = image.getHeight();
        int width  = image.getWidth();
        int r,g,b;
        for(int i = 0;i < width; i++){
            for(int j = 0;j < height; j++){
                int pixel = image.getRGB(i, j);
                r = (pixel & 0xff0000) >> 16;
                g = (pixel & 0xff00) >> 8;
                b = (pixel & 0xff);
                if (!(r == g && r == b)) {
                    return true;
                }
            }
        }
        return false;
    }
}
