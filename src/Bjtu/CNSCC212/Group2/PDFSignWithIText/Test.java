package Bjtu.CNSCC212.Group2.PDFSignWithIText;



import java.io.FileInputStream;
import java.io.FileOutputStream;



public class Test {
    public static void main(String[] args) throws  Exception {



        PDFSign pdfSign = new PDFSign();

        pdfSign.setProperties("C:\\Users\\user\\Desktop\\CNSCC212 Advanced Programming PDFSign\\Test.pdf",
                                "C:\\Users\\user\\Desktop\\CNSCC212 Advanced Programming PDFSign\\Signed_Test.pdf",
                               "18726077",
                                "C:\\Users\\user\\Desktop\\CNSCC212 Advanced Programming PDFSign\\HaoYukun.png",
                                "C:\\Users\\user\\Desktop\\CNSCC212 Advanced Programming PDFSign\\Certification.p12",
                                "For Advanced programming","Bjtu Weihai");
        pdfSign.sign();

        System.out.println("签名完成");
    }
}

