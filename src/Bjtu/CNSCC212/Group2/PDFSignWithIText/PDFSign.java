package Bjtu.CNSCC212.Group2.PDFSignWithIText;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;

public class PDFSign extends Aes{

    private static char[] password = "18726077".toCharArray();//keystory密码
    private static String fileSrc = "D:\\Download\\CNSCC212 Advance Programming\\Test.pdf" ;//原始pdf
    private static String fileDest = "D:\\Download\\CNSCC212 Advance Programming\\Signed_Test.pdf" ;//签名完成的pdf
    private static String stampPath = "D:\\Download\\CNSCC212 Advance Programming\\Stamp.png";//签章图片
    private static String p12StreamPath = "D:\\Download\\CNSCC212 Advance Programming\\Certification.p12";
    private static String reason = "For CNSCC212 Advanced Programming";
    private static String location = "Weihai Campus, Beijing Jiaotong University\n" +
                              "Xiandai Road 69, Nanhai Xinqu (Nanhai New Area)\n" +
                              "Weihai City, Shandong, China";


    public static void sign()
            throws GeneralSecurityException, IOException, DocumentException {
        FileInputStream src = new FileInputStream(fileSrc);
        FileOutputStream dest = new FileOutputStream(fileDest);
        InputStream p12Stream = new FileInputStream(p12StreamPath);

        //读取keystore ，获得私钥和证书链
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(p12Stream, password);
        String alias = (String)keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);
        Certificate[] chain = keyStore.getCertificateChain(alias);

        // Creating the reader and the stamper，开始pdfreader
        PdfReader reader = new PdfReader(src);
        //目标文件输出流
        //创建签章工具PdfStamper ，最后一个boolean参数
        //false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
        //true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
        PdfStamper stamper = PdfStamper.createSignature(reader, dest, '\0', null, false);
        // 获取数字签章属性对象，设定数字签章的属性
        PdfSignatureAppearance pdfSignatureAppearance = stamper.getSignatureAppearance();
        pdfSignatureAppearance.setReason(reason);
        pdfSignatureAppearance.setLocation(location);
        //设置签名的位置，页码，签名域名称，多次追加签名的时候，签名域名称不能一样
        //签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
        //四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
        pdfSignatureAppearance.setVisibleSignature(new Rectangle(0, 800, 100, 700),reader.getNumberOfPages(), "sig1");
        //pdfSignatureAppearance.setVisibleSignature(new Rectangle(Rectangle.ALIGN_RIGHT + 1, Rectangle.ALIGN_BOTTOM, Rectangle.ALIGN_RIGHT, Rectangle.ALIGN_BOTTOM+1), reader.getNumberOfPages(), "sig1");
        //读取图章图片，这个image是itext包的image
        Image image = Image.getInstance(stampPath);
        pdfSignatureAppearance.setSignatureGraphic(image);
        pdfSignatureAppearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
        //设置图章的显示方式
        pdfSignatureAppearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);


        // 摘要算法
        ExternalDigest digest = new BouncyCastleDigest();
        // 签名算法
        ExternalSignature signature = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA1, null);
        // 调用itext签名方法完成pdf签章CryptoStandard.CMS 签名方式
        MakeSignature.signDetached(pdfSignatureAppearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
    }

    public void setReason(String reason){
        this.reason = reason;
        System.out.println("New reason set successfully.");
    }

    public String getReason(){
        return this.reason;
    }

    public void setP12StreamPath(String p12StreamPath){
        this.p12StreamPath = p12StreamPath;
        System.out.println("New p12 Stream path set successfully.");
    }

    public String getKeystorePath(){
        return this.p12StreamPath;
    }

    public void setLocation(String location){
        this.location = location;
        System.out.println("New location set successfully.");
    }

    public String getLocation(){
        return this.location;
    }

    public void setFileSrc(String fileSrc){
        this.fileSrc = fileSrc;
        System.out.println("New file source set successfully.");
    }

    public String getFileSrc(){
        return this.fileSrc;
    }

    public void setFileDest(String fileDest){
        this.fileDest = fileDest;
        System.out.println("New file destination set successfully.");
    }

    public String getFileDest(){
        return this.fileDest;
    }

    public void setStampPath(String stampPath){
        this.stampPath = stampPath;
        System.out.println("New stamp file source set successfully.");
    }

    public void setPassword(String password){
        this.password = password.toCharArray();
        System.out.println("New password set successfully.");
    }

    public String getPassword(char[] password) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        return Aes.aesEncrypt(password.toString(),Aes.keyGenerator()).toString();
    }


    public void setProperties(String fileSrc, String fileDest, String password, String stampPath, String p12StreamPath,
                              String reason , String location){
        setFileSrc(fileSrc);
        setFileDest(fileDest);
        setStampPath(stampPath);
        setP12StreamPath(p12StreamPath);
        setReason(reason);
        setLocation(location);
    }

}
