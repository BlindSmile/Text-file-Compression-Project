package pro1;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

public class DataCompression{

    private static HashMap<Character,String> encoder;
    private static HashMap<String,Character> decoder;
    public static int frequencyHolder[];
    public static int totalFrequency = 0;//No. of characters in original file
    public static int totalEncoded = 0;//No. of characters in encoded file
    public static int totalDecoded = 0;//No. of characters in decoded file
    private final String sourcePath = "C:\\Users\\007ad\\OneDrive\\Desktop\\Adi\\javapl\\project\\test.txt";//C:\Users\nEW u\Documents
    private final String destinationPath = "C:\\Users\\007ad\\OneDrive\\Desktop\\Adi\\javapl\\project";

    public static void main(String args[])throws Exception{
        DataCompression dc=new DataCompression();
        dc.countFrequency();
        Node root=dc.encode();
        dc.generateKey();
        dc.generateEncodedFile();
        dc.decode(root);
        dc.checkFileSize();
        dc.generateEncodedBitFile();
    }
    public void countFrequency()throws Exception{
        frequencyHolder=new int[256];
        FileReader fr=new FileReader(sourcePath);
        int x;
        while((x=fr.read())!=-1)
        {
            if(x<256){
            frequencyHolder[x]++;
            totalFrequency++;
            }
        }
        System.out.println("TOTAL FREQUENCY = "+totalFrequency);
    }
    
    public Node encode(){
        int n=frequencyHolder.length;
        
        PriorityQueue<Node> minheap=new PriorityQueue<>(n,FREQUENCY_COMPARATOR);
        for(int i=0;i<n;i++){
            if(frequencyHolder[i]!=0){
                minheap.add(new Node((char)i,frequencyHolder[i]));
            }
        }
        while(minheap.size()!=1){
            Node x=minheap.poll();
            Node y=minheap.poll();
            x.setCode("0");
            y.setCode("1");
            long sum=x.getFrequency()+y.getFrequency();

            minheap.add(new Node('\0',sum,x,y));
        }
        Node root=minheap.peek();
        encoder=new HashMap<>();

        encodeData(root,"");
        System.out.println();

        for(int i=0;i<n;i++){
            if(frequencyHolder[i]!=0){
                System.out.println((char)i+"\t"+frequencyHolder[i]+"\t"+encoder.get((char)i));
            }
        }
        return root;
    }

    private static final Comparator<Node> FREQUENCY_COMPARATOR = (Node o1, Node o2) -> (int) (o1.getFrequency()-o2.getFrequency());

    private void encodeData(Node root,String str)throws NullPointerException{
        if(root.getCode()!=null)
        str+=root.getCode();

        if(root.getlChild()==null&&root.getrChild()==null){
            encoder.put(root.getCharacter(),str);
            return;
        }
        encodeData(root.getlChild(),str);
        encodeData(root.getrChild(),str);
    }
    public void generateKey()throws Exception{
        System.out.println("Inside Generate key....");
        StringBuilder contents=new StringBuilder();
        for(var entry:encoder.entrySet()){
            contents.append(getEscapeSequence(entry.getKey())).append(",").append(entry.getValue()).append("\n");
        }

        try(FileWriter fw = new FileWriter(destinationPath+"\\key.csv");){
            fw.write(contents.toString());
        }

        
    }
    private static String getEscapeSequence(char h)
    {
        switch(h)
        {
            case '\n': return "\\n";
            case '\t': return "\\t";
            //case " ": return "\\u32";
            default: return Character.toString(h);
        }   
    }
    private void generateEncodedFile()throws Exception{
        System.out.println("gen encoded text");
        StringBuilder contents=new StringBuilder();
        try(FileReader fr = new FileReader(sourcePath);)
        {
            int c;
            while((c = fr.read())!= -1)
                if(c<256)
                    contents.append(encoder.get((char)c));  
        }
        
        try(FileWriter fw = new FileWriter(destinationPath+"\\encoded.txt");)
        {
            fw.write(contents.toString());
        } 
    }
    private void decode(Node root)throws Exception{
        System.out.println("Decoding");
        Node hroot=root;
        StringBuilder contents=new StringBuilder();
        try(FileReader fr=new FileReader(destinationPath+"\\encoded.txt");)
        {
            int c;
            while((c=fr.read())!=-1){
                if(c<256){
                    contents.append((char)c);
                }
            }
        }
        StringBuilder output=new StringBuilder();
        for(int i=0;i<contents.length();i++)
        {
            char ch=contents.charAt(i);
            if(ch=='0'){
                root=root.getlChild();
            }
            else if(ch=='1'){
                root=root.getrChild();
            }
            if(root.getlChild()==null && root.getrChild()==null){
                output.append(root.getCharacter());
                root=hroot;
            }
        }
        totalDecoded=output.length();
        //System.out.println("Total Decoded = "+totalDecoded);
        try(FileWriter fw=new FileWriter(destinationPath+"\\decoded.txt");){
            fw.write(output.toString());
        }
    }
    /*
    Prove that Huffman coding leads to data compression
    */
    private void checkFileSize()
    {
        System.out.println("Total no. of bits in original file = "+totalFrequency*8);
        for(int i=0;i<frequencyHolder.length;i++)
           if(frequencyHolder[i]!=0)
           {
               
               totalEncoded+=frequencyHolder[i]*encoder.get((char)i).length();
           }
               
        System.out.println("Total no. of bits in encoded file = "+totalEncoded);
        System.out.println("Total no. of bits in decoded file = "+totalDecoded*8);
    }
     private void generateEncodedBitFile()throws Exception
    {
        System.out.println("Inside abyss");
        ArrayList<Byte> contByte = new ArrayList<>();  
        StringBuffer contents=new StringBuffer();
        
        try(FileInputStream fis = new FileInputStream(sourcePath);)
        {
            int c;
            while((c = fis.read())!= -1)
                if(c<256)
                {
                    
                    contents.append(encoder.get((char)c));  
                //    System.out.print(encoder.get((char)c)+",");//Integer.parseInt(g,2)
                    
                    //catch(StringIndexOutOfBoundsException ae){;}
                }       
        }   
        try
        {
            byte b=0;
            System.out.println("\nspill:"+contents);
            int len;
            for(len=contents.length();len<contents.length()+8;len++)
                if(len%8==0)break;
                 
           for(int i=0;i<len;i++)
            {
                if(i>=contents.length())
                    b = (byte)(b<<1);
                else
                {
                    b = (byte)((b<<1)|Character.getNumericValue(contents.charAt(i)));
                }
                //System.out.print(">"+decToBinary(b));
                if(((i+1)%8==0&&i!=0)||i==len-1)
                    {
                        //System.out.println("="+b);
                        contByte.add((byte)b);
                        b=0;
                    }
            }
        }
        catch(NullPointerException ne){;}
        try(FileOutputStream fos = new FileOutputStream(destinationPath+"\\encodedbit.txt");)
        {
            for(byte b:contByte)
            {
                fos.write(b);
            }
        } 
       System.out.println("Outside abyss");
    }
    private void decodeBit(Node root)throws Exception
    {
        Node hRoot = root;
        ArrayList<Byte> contByte = new ArrayList<>();  
        try(FileInputStream fis = new FileInputStream(destinationPath+"\\encodedbit.txt");)
        {
            byte b;
            while((b = (byte)fis.read())!= -1)
                //if(c<256)
                {
                    //System.out.print("="+b);
                    contByte.add(b);
                }   
        }
        //System.out.println("check contByte:"+contByte);
        StringBuilder output = new StringBuilder();
        StringBuffer contents = new StringBuffer();
        for(byte b:contByte)
        {
            byte cb=b;
            contents.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0')); 
            
        }
        System.out.println("\ncontents decoded="+contents);
        for(int i=0;i<contents.length();i++)
        {
            char ch = contents.charAt(i);
            if(ch=='0')
                root =  root.getlChild();
            else if(ch=='1')
                root = root.getrChild();
            if(root.getrChild()==null && root.getlChild()==null)
            {
                //System.out.print(root.getCharacter());
                output.append(root.getCharacter());
                root=hRoot;
            }
        }
        totalDecoded = output.length();
        try(FileWriter fw = new FileWriter(destinationPath+"\\decodedbit.txt");)
        {
            fw.write(output.toString());
        } 
    }
}