import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) throws Exception{
        if (args == null || args.length != 2) {
            System.out.println("输入有误！");
            return;
        }
        String fileName = args[1];
        boolean isOneSide = args[0].equals("单页打印");
        List<List<Integer>> coloredInfo = getColoredInfo(fileName, isOneSide);
        if (isOneSide) {
            System.out.println();
            System.out.println("单页打印模式：");
            System.out.println(String.format("彩色页面共%d页，具体为:\n%s",
                    coloredInfo.get(0).size(),coloredInfo.get(0).toString().replaceAll("[\\[\\]]","")));
            System.out.println(String.format("黑白页面共%d页，具体为:\n%s",
                    coloredInfo.get(1).size(),coloredInfo.get(1).toString().replaceAll("[\\[\\]]","")));
        } else {
            System.out.println();
            System.out.println("双页打印模式：");
            System.out.println(String.format("彩色页面共%d页，具体为:\n%s",
                    coloredInfo.get(0).size(),coloredInfo.get(0).toString().replaceAll("[\\[\\]]","")));
            System.out.println(String.format("黑白页面共%d页，具体为:\n%s",
                    coloredInfo.get(1).size(),coloredInfo.get(1).toString().replaceAll("[\\[\\]]","")));
        }
    }
    public static List<List<Integer>> getColoredInfo(String fileName, boolean isOneSide){
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
            if (isOneSide) {
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
            } else {//双面打印时，需要两页两页考虑
                for(int i = 0; i < pagesNum / 2; i ++) {
                    int idx1 = i * 2;
                    int idx2 = i * 2 + 1;
                    PDPage onePage1 = document.getPage(idx1);
                    PDPage onePage2 = document.getPage(idx2);
                    System.out.println(String.format("processing the %d and %dth page..", idx1 + 1, idx2 + 1));
                    BufferedImage pdfImage1 = renderer.renderImageWithDPI(idx1, 200 / 4); // Windows native DPI
                    BufferedImage pdfImage2 = renderer.renderImageWithDPI(idx2, 200 / 4); // Windows native DPI
                    if (isColored(pdfImage1) || isColored(pdfImage2)) {
                        coloredNums.add(idx1 + 1);
                        coloredNums.add(idx2 + 1);
                        coloredDoc.addPage(onePage1);
                        coloredDoc.addPage(onePage2);
                    } else {
                        grayNums.add(idx1 + 1);
                        grayNums.add(idx2 + 1);
                        grayDoc.addPage(onePage1);
                        grayDoc.addPage(onePage2);
                    }
                }
                if ((pagesNum & 1) == 1) {
                    int idx = pagesNum - 1;
                    PDPage onePage = document.getPage(idx);
                    System.out.println(String.format("processing the %d page..", idx + 1));
                    BufferedImage pdfImage = renderer.renderImageWithDPI(idx, 200 / 4); // Windows native DPI
                    if (isColored(pdfImage)) {
                        coloredNums.add(idx + 1);
                        coloredDoc.addPage(onePage);
                    } else {
                        grayNums.add(idx + 1);
                        grayDoc.addPage(onePage);
                    }
                }
            }
            String printMode = isOneSide?"_单面打印":"_双面打印";
            coloredDoc.save(fileName + "_彩色部分" + printMode + ".pdf");
            coloredDoc.close();
            grayDoc.save(fileName + "_黑白部分" + printMode + ".pdf");
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
